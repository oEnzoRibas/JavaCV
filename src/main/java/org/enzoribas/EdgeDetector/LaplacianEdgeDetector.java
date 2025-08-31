package org.enzoribas.EdgeDetector;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_core.Mat;

import static org.bytedeco.opencv.global.opencv_core.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

public class LaplacianEdgeDetector {
    static {
        Loader.load(org.bytedeco.opencv.global.opencv_core.class);
    }

    public static void main(String[] args) {
        Mat src = imread(Config.INPUT_IMAGE, IMREAD_GRAYSCALE);
        if (src.empty()) {
            System.out.println("Could not load image!");
            return;
        }

        Mat grad = new Mat();

        // Laplacian
        Laplacian(src, grad, CV_16S, 5, 1, 1, BORDER_DEFAULT);
        convertScaleAbs(grad, grad);

        String outputDir = Config.OUTPUT_IMAGE + "/laplacian/";
        String fileName = "edges.png";

        imwrite(outputDir+fileName, grad);
        System.out.println("Saved " + fileName + " to " + outputDir);
    }
}
