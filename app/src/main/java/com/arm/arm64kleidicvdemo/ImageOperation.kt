package com.arm.arm64kleidicvdemo

import org.opencv.core.Core
import org.opencv.core.CvType
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc

enum class ImageOperation(val displayName: String) {
    GAUSSIAN_BLUR("Gaussian Blur") {
        override fun apply(src: Mat, dst: Mat) {
            Imgproc.GaussianBlur(src, dst, Size(7.0, 7.0), 0.0)
        }
    },
    SOBEL("Sobel") {
        override fun apply(src: Mat, dst: Mat) {
            Imgproc.Sobel(src, dst, CvType.CV_16S, 1, 0, 3
                , 1.0, 0.0, Core.BORDER_REPLICATE)
        }
    },
    RESIZE("Resize") {
        override fun apply(src: Mat, dst: Mat) {
            Imgproc.resize(
                src,
                dst,
                Size(src.cols() / 2.0, src.rows() / 2.0)
            )
        }
    },
    ROTATE_90("Rotate 90°") {
        override fun apply(src: Mat, dst: Mat) {
            Core.rotate(src, dst, Core.ROTATE_90_CLOCKWISE)
        }
    };

    abstract fun apply(src: Mat, dst: Mat)

    companion object {
        fun fromDisplayName(name: String): ImageOperation? =
            values().find { it.displayName == name }
    }
}