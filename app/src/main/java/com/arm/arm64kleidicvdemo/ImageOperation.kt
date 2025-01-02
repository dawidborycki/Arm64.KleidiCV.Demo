package com.arm.arm64kleidicvdemo

import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

enum class ImageOperation(val displayName: String) {
    GAUSSIAN_BLUR("Gaussian Blur") {
        override fun apply(mat: Mat) {
            Imgproc.GaussianBlur(mat, mat, Size(5.0, 5.0), 5.0)
        }
    },
    SOBEL("Sobel") {
        override fun apply(mat: Mat) {
            Imgproc.Sobel(mat, mat, CvType.CV_8U, 1, 1)
        }
    },
    RESIZE("Resize") {
        override fun apply(mat: Mat) {
            Imgproc.resize(
                mat,
                mat,
                Size(mat.cols() / 2.0, mat.rows() / 2.0)
            )
        }
    },
    ROTATE_90("Rotate 90°") {
        override fun apply(mat: Mat) {
            Core.rotate(mat, mat, Core.ROTATE_90_CLOCKWISE)
        }
    };

    abstract fun apply(mat: Mat)

    companion object {
        fun fromDisplayName(name: String): ImageOperation? =
            values().find { it.displayName == name }
    }
}