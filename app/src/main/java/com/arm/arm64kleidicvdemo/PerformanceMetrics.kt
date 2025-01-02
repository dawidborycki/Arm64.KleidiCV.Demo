package com.arm.arm64kleidicvdemo

import kotlin.math.pow
import kotlin.math.sqrt

data class PerformanceMetrics(private val durationsNano: List<Long>) {
    private val toMillis = 1_000_000.0

    val average: Double
        get() = durationsNano.average() / toMillis

    val min: Double
        get() = durationsNano.min() / toMillis

    val max: Double
        get() = durationsNano.max() / toMillis

    val standardDeviation: Double
        get() {
            val mean = durationsNano.average()
            return sqrt(durationsNano.map { (it - mean).pow(2) }.average()) / toMillis
        }

    override fun toString(): String = buildString {
        append("Average: %.2f ms\n".format(average))
        append("Min: %.2f ms\n".format(min))
        append("Max: %.2f ms\n".format(max))
        append("Std Dev: %.2f ms".format(standardDeviation))
    }
}