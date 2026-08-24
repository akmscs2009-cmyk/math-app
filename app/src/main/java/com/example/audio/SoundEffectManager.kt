package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlin.math.sin

object SoundEffectManager {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private const val SAMPLE_RATE = 22050

    /**
     * Cheerful, joyful clapping/fanfare sound for correct answers (C5, E5, G5, C6, G5, C6)
     */
    fun playCorrectSound() {
        scope.launch {
            val notes = listOf(
                523.25 to 80,  // C5
                659.25 to 80,  // E5
                783.99 to 80,  // G5
                1046.50 to 120, // C6
                783.99 to 90,  // G5
                1046.50 to 260 // C6 (long triumphant finish)
            )
            playTonesSequence(notes)
        }
    }

    /**
     * Classic comical sad cartoon descending tone (wah-wah-wah-waaah) for wrong answers
     */
    fun playIncorrectSound() {
        scope.launch {
            val notes = listOf(
                370.00 to 180, // F#4
                349.23 to 180, // F4
                329.63 to 180, // E4
                293.66 to 400  // D4 (long sad drop)
            )
            playTonesSequence(notes)
        }
    }

    /**
     * Alerting timeout chime when the 1-minute block screen is triggered
     */
    fun playBlockAlertSound() {
        scope.launch {
            val notes = listOf(
                440.00 to 120, // A4
                349.23 to 120, // F4
                261.63 to 350  // C4
            )
            playTonesSequence(notes)
        }
    }

    private fun playTonesSequence(notes: List<Pair<Double, Int>>) {
        try {
            var totalSamples = 0
            for ((_, durationMs) in notes) {
                totalSamples += (SAMPLE_RATE * durationMs) / 1000
            }

            val pcmData = ShortArray(totalSamples)
            var currentOffset = 0

            for ((freq, durationMs) in notes) {
                val numSamples = (SAMPLE_RATE * durationMs) / 1000
                for (i in 0 until numSamples) {
                    val angle = 2.0 * Math.PI * i / (SAMPLE_RATE / freq)
                    // Linear attack & decay envelope to eliminate audio clicks
                    val envelope = when {
                        i < numSamples * 0.1 -> i / (numSamples * 0.1)
                        i > numSamples * 0.7 -> (numSamples - i) / (numSamples * 0.3)
                        else -> 1.0
                    }
                    val sampleValue = (sin(angle) * envelope * Short.MAX_VALUE * 0.7).toInt().toShort()
                    pcmData[currentOffset + i] = sampleValue
                }
                currentOffset += numSamples
            }

            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(pcmData.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(pcmData, 0, pcmData.size)
            audioTrack.play()

            // Release after playing duration
            val totalDurationMs = notes.sumOf { it.second }
            Thread.sleep(totalDurationMs.toLong() + 100)
            audioTrack.stop()
            audioTrack.release()
        } catch (_: Exception) {
            // Gracefully ignore audio synthesis errors in silent/unsupported environments
        }
    }
}
