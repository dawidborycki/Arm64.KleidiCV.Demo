package com.arm.arm64kleidicvdemo

import org.opencv.core.Mat

class ImageProcessor {
    fun applyOperation(mat: Mat, operation: ImageOperation) {
        operation.apply(mat)
    }
}