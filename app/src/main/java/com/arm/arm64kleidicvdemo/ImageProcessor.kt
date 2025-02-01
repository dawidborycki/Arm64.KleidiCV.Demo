package com.arm.arm64kleidicvdemo

import org.opencv.core.Mat

class ImageProcessor {
    fun applyOperation(src: Mat, dst: Mat, operation: ImageOperation) {
        operation.apply(src, dst)
    }
}