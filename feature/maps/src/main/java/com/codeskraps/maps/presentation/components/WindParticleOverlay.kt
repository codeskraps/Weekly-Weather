package com.codeskraps.maps.presentation.components

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.PorterDuff
import android.util.Log
import android.view.Choreographer
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.codeskraps.maps.domain.model.WindData
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random
import androidx.core.graphics.createBitmap
import androidx.core.graphics.set

private const val TAG = "WeatherApp:WindOverlay"
private const val PARTICLE_COUNT = 400
private const val SPEED_SCALE = 12f
private const val TAIL_LENGTH = 30f
private const val MIN_AGE = 80f
private const val MAX_AGE = 200f

@Composable
fun WindParticleOverlay(
    windData: WindData,
    cameraPositionState: CameraPositionState,
    modifier: Modifier = Modifier
) {
    val state = remember { ParticleState() }

    LaunchedEffect(windData) {
        state.initialized = false
        state.heatmapKey = ""
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->
            WindSurfaceView(context, state).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        },
        update = { view ->
            view.windData = windData
            view.cameraPositionState = cameraPositionState
        }
    )
}

@Suppress("ViewConstructor")
private class WindSurfaceView(
    context: Context,
    private val state: ParticleState
) : SurfaceView(context), Choreographer.FrameCallback, SurfaceHolder.Callback {

    var windData: WindData? = null
    var cameraPositionState: CameraPositionState? = null

    private var running = false

    private val corePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
    }
    private val outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeCap = Paint.Cap.ROUND
    }
    private val heatmapPaint = Paint().apply {
        isFilterBitmap = true
    }

    init {
        setZOrderOnTop(true)
        holder.setFormat(PixelFormat.TRANSLUCENT)
        holder.addCallback(this)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        Log.d(TAG, "Surface created")
        running = true
        Choreographer.getInstance().postFrameCallback(this)
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {}

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        Log.d(TAG, "Surface destroyed")
        running = false
        Choreographer.getInstance().removeFrameCallback(this)
        state.heatmapBitmap?.recycle()
        state.heatmapBitmap = null
    }

    override fun doFrame(frameTimeNanos: Long) {
        if (!running) return
        drawFrame()
        Choreographer.getInstance().postFrameCallback(this)
    }

    private fun drawFrame() {
        val w = width
        val h = height
        if (w == 0 || h == 0) return

        val canvas: Canvas
        try {
            canvas = holder.lockCanvas() ?: return
        } catch (_: Exception) {
            return
        }

        try {
            canvas.drawColor(0, PorterDuff.Mode.CLEAR)

            val data = windData ?: return
            val camState = cameraPositionState ?: return
            val projection = camState.projection ?: return

            val gridPoints = data.points.map { point ->
                val screenPoint = projection.toScreenLocation(LatLng(point.latitude, point.longitude))
                GridScreenPoint(
                    screenX = screenPoint.x.toFloat(),
                    screenY = screenPoint.y.toFloat(),
                    speedMs = point.speedMs,
                    directionDeg = point.directionDeg
                )
            }

            if (gridPoints.isEmpty()) return

            // Draw heatmap
            drawHeatmap(canvas, gridPoints, w, h)

            // Initialize particles
            if (!state.initialized) {
                Log.d(TAG, "Initializing ${state.particles.size} particles, view=${w}x${h}")
                for (p in state.particles) {
                    p.x = Random.nextFloat() * w
                    p.y = Random.nextFloat() * h
                    p.age = Random.nextFloat() * MAX_AGE
                    p.maxAge = MIN_AGE + Random.nextFloat() * (MAX_AGE - MIN_AGE)
                    assignVelocity(p, gridPoints)
                }
                state.initialized = true
            }

            // Update and draw particles
            for (p in state.particles) {
                p.age += 1f
                p.x += p.vx
                p.y += p.vy

                if (p.age >= p.maxAge || p.x < -50f || p.x > w + 50f || p.y < -50f || p.y > h + 50f) {
                    p.x = Random.nextFloat() * w
                    p.y = Random.nextFloat() * h
                    p.age = 0f
                    p.maxAge = MIN_AGE + Random.nextFloat() * (MAX_AGE - MIN_AGE)
                    assignVelocity(p, gridPoints)
                }

                val lifeRatio = p.age / p.maxAge
                val alpha = ((1f - abs(2f * lifeRatio - 1f)) * 0.85f * 255).toInt().coerceIn(0, 255)

                if (alpha > 5) {
                    val tailX = p.x - p.vx * TAIL_LENGTH
                    val tailY = p.y - p.vy * TAIL_LENGTH
                    val strokeW = 6f + (p.speed / 5f).coerceAtMost(6f)

                    outlinePaint.color = 0x000000 or (((alpha * 0.6f).toInt().coerceIn(0, 255)) shl 24)
                    outlinePaint.strokeWidth = strokeW + 4f
                    canvas.drawLine(tailX, tailY, p.x, p.y, outlinePaint)

                    corePaint.color = 0xFFFFFF or ((alpha and 0xFF) shl 24)
                    corePaint.strokeWidth = strokeW
                    canvas.drawLine(tailX, tailY, p.x, p.y, corePaint)
                }
            }
        } finally {
            try {
                holder.unlockCanvasAndPost(canvas)
            } catch (_: Exception) {
                // Surface may have been destroyed
            }
        }
    }

    private fun drawHeatmap(canvas: Canvas, gridPoints: List<GridScreenPoint>, w: Int, h: Int) {
        val heatmapKey = gridPoints.joinToString(",") { "${it.screenX.toInt()},${it.screenY.toInt()}" }
        val dst = android.graphics.Rect(0, 0, w, h)

        if (heatmapKey == state.heatmapKey && state.heatmapBitmap != null) {
            val bmp = state.heatmapBitmap!!
            val src = android.graphics.Rect(0, 0, bmp.width, bmp.height)
            canvas.drawBitmap(bmp, src, dst, heatmapPaint)
            return
        }
        state.heatmapKey = heatmapKey

        val scale = 8
        val bw = w / scale
        val bh = h / scale
        if (bw <= 0 || bh <= 0) return
        val heatmap = createBitmap(bw, bh)

        for (y in 0 until bh) {
            for (x in 0 until bw) {
                val px = (x * scale + scale / 2).toFloat()
                val py = (y * scale + scale / 2).toFloat()

                var totalWeight = 0f
                var weightedSpeed = 0f
                for (gp in gridPoints) {
                    val dx = px - gp.screenX
                    val dy = py - gp.screenY
                    val dist = sqrt(dx * dx + dy * dy).coerceAtLeast(1f)
                    val weight = 1f / (dist * dist)
                    totalWeight += weight
                    weightedSpeed += gp.speedMs * weight
                }
                val speed = if (totalWeight > 0) weightedSpeed / totalWeight else 0f
                heatmap[x, y] = windSpeedColor(speed)
            }
        }

        state.heatmapBitmap?.recycle()
        state.heatmapBitmap = heatmap

        val src = android.graphics.Rect(0, 0, bw, bh)
        canvas.drawBitmap(heatmap, src, dst, heatmapPaint)
    }

    /**
     * Absolute scale: 0-100 km/h (0-27.8 m/s)
     * Blue (calm) -> Cyan (25) -> Green (50) -> Yellow (75) -> Red (100+)
     */
    private fun windSpeedColor(speedMs: Float): Int {
        val maxSpeedMs = 27.8f
        val ratio = (speedMs / maxSpeedMs).coerceIn(0f, 1f)
        val alpha = (0.25f * 255).toInt()

        val r: Int
        val g: Int
        val b: Int
        when {
            ratio < 0.25f -> {
                val t = ratio / 0.25f
                r = 0; g = (t * 200).toInt(); b = (180 + t * 75).toInt()
            }
            ratio < 0.5f -> {
                val t = (ratio - 0.25f) / 0.25f
                r = 0; g = (200 + t * 55).toInt(); b = (255 - t * 200).toInt()
            }
            ratio < 0.75f -> {
                val t = (ratio - 0.5f) / 0.25f
                r = (t * 255).toInt(); g = 255; b = 0
            }
            else -> {
                val t = (ratio - 0.75f) / 0.25f
                r = 255; g = (255 - t * 200).toInt(); b = 0
            }
        }
        return (alpha shl 24) or ((r and 0xFF) shl 16) or ((g and 0xFF) shl 8) or (b and 0xFF)
    }
}

private class ParticleState {
    val particles = Array(PARTICLE_COUNT) { WindParticle() }
    var initialized = false
    var heatmapBitmap: Bitmap? = null
    var heatmapKey: String = ""
}

private class WindParticle(
    var x: Float = 0f,
    var y: Float = 0f,
    var vx: Float = 0f,
    var vy: Float = 0f,
    var age: Float = 0f,
    var maxAge: Float = 0f,
    var speed: Float = 0f
)

private data class GridScreenPoint(
    val screenX: Float,
    val screenY: Float,
    val speedMs: Float,
    val directionDeg: Float
)

private fun assignVelocity(
    particle: WindParticle,
    gridPoints: List<GridScreenPoint>
) {
    if (gridPoints.isEmpty()) return

    val speed: Float
    val dirDeg: Float

    if (gridPoints.size >= 4) {
        var best1Dist = Float.MAX_VALUE
        var best2Dist = Float.MAX_VALUE
        var best3Dist = Float.MAX_VALUE
        var best4Dist = Float.MAX_VALUE
        var best1 = gridPoints[0]
        var best2 = gridPoints[0]
        var best3 = gridPoints[0]
        var best4 = gridPoints[0]

        for (gp in gridPoints) {
            val dx = particle.x - gp.screenX
            val dy = particle.y - gp.screenY
            val dist = dx * dx + dy * dy
            when {
                dist < best1Dist -> {
                    best4Dist = best3Dist; best4 = best3
                    best3Dist = best2Dist; best3 = best2
                    best2Dist = best1Dist; best2 = best1
                    best1Dist = dist; best1 = gp
                }
                dist < best2Dist -> {
                    best4Dist = best3Dist; best4 = best3
                    best3Dist = best2Dist; best3 = best2
                    best2Dist = dist; best2 = gp
                }
                dist < best3Dist -> {
                    best4Dist = best3Dist; best4 = best3
                    best3Dist = dist; best3 = gp
                }
                dist < best4Dist -> {
                    best4Dist = dist; best4 = gp
                }
            }
        }

        val near = arrayOf(best1, best2, best3, best4)
        val dists = floatArrayOf(best1Dist, best2Dist, best3Dist, best4Dist)

        var totalWeight = 0f
        var weightedSpeed = 0f
        var weightedVx = 0f
        var weightedVy = 0f

        for (i in 0..3) {
            val d = sqrt(dists[i]).coerceAtLeast(1f)
            val weight = 1f / (d * d)
            totalWeight += weight
            weightedSpeed += near[i].speedMs * weight
            val dirRad = Math.toRadians(near[i].directionDeg.toDouble())
            weightedVx += (sin(dirRad) * weight).toFloat()
            weightedVy += (-cos(dirRad) * weight).toFloat()
        }

        speed = weightedSpeed / totalWeight
        val avgVx = weightedVx / totalWeight
        val avgVy = weightedVy / totalWeight
        dirDeg = (Math.toDegrees(atan2(avgVx.toDouble(), (-avgVy).toDouble())).toFloat() + 360f) % 360f
    } else {
        var nearest = gridPoints[0]
        var minDist = Float.MAX_VALUE
        for (gp in gridPoints) {
            val dx = particle.x - gp.screenX
            val dy = particle.y - gp.screenY
            val dist = dx * dx + dy * dy
            if (dist < minDist) { minDist = dist; nearest = gp }
        }
        speed = nearest.speedMs
        dirDeg = nearest.directionDeg
    }

    // Add 180° to convert from "wind coming from" to "wind going to"
    val dirRad = Math.toRadians((dirDeg + 180.0) % 360.0)
    val pixelSpeed = speed * SPEED_SCALE / 60f

    particle.vx = (sin(dirRad) * pixelSpeed).toFloat()
    particle.vy = (-cos(dirRad) * pixelSpeed).toFloat()
    particle.speed = speed
}
