package org.enzoribas.EdgeDetector;


import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_core.Mat;
import org.enzoribas.Config;

import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.Canny;

public class CannyEdgeDetector {
    static {
        Loader.load(org.bytedeco.opencv.global.opencv_core.class);
    }

    public static void main(String[] args) {
        String projectPath = Config.PROJECT_PATH + "EdgeDetector/images/";
        String inputImagePath = projectPath + "input.jpg";
        Mat src = imread(inputImagePath, IMREAD_GRAYSCALE);
        if (src.empty()) {
            System.out.println("Could not load image!");
            return;
        }

        Mat edges = new Mat();
        Canny(src, edges, 50, 170);

        String outputDir = Config.PROJECT_PATH + "EdgeDetector/images/output/canny/";;
        String fileName = "edges.png";

        imwrite(outputDir+fileName, edges);
        System.out.println("Saved " + fileName + " to " + outputDir);
    }
}

