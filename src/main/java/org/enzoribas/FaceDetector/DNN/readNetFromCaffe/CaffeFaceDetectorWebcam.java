package org.enzoribas.FaceDetector.DNN.readNetFromCaffe;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.bytedeco.opencv.opencv_dnn.Net;
import org.enzoribas.Config;
import org.opencv.core.CvType;

import static org.bytedeco.opencv.global.opencv_dnn.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import static org.bytedeco.opencv.global.opencv_highgui.*; // para imshow e waitKey

public class CaffeFaceDetectorWebcam {
    public static void main(String[] args) {
        String projectPath = Config.PROJECT_PATH + "FaceDetector/DNN/readNetFromCaffe/";
        String imagePath = projectPath + "images/input.jpg";
        String protoPath = projectPath + "resources/deploy.prototxt";
        String modelPath = projectPath + "resources/res10_300x300_ssd_iter_140000.caffemodel";

        Net net = readNetFromCaffe(protoPath, modelPath);

        VideoCapture capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            System.out.println("Não foi possível abrir a webcam!");
            return;
        }

        Mat frame = new Mat();
        while (true) {
            capture.read(frame);
            if (frame.empty()) break;

            Mat blob = blobFromImage(frame,
                    1.0,
                    new Size(300, 300),
                    new Scalar(104.0, 177.0, 123.0, 0.0),
                    false,
                    false,
                    CvType.CV_32F);
            net.setInput(blob);
            Mat detections = net.forward();

            FloatPointer data = new FloatPointer(detections.createIndexer().pointer().position(0));
            int faceCount = 0;
            for (int i = 0; i < detections.size(2); i++) {
                float confidence = data.get(i * 7 + 2);
                if (confidence > 0.5) {
                    faceCount++;
                    int x1 = (int) (data.get(i * 7 + 3) * frame.cols());
                    int y1 = (int) (data.get(i * 7 + 4) * frame.rows());
                    int x2 = (int) (data.get(i * 7 + 5) * frame.cols());
                    int y2 = (int) (data.get(i * 7 + 6) * frame.rows());
                    rectangle(frame, new Point(x1, y1), new Point(x2, y2), new Scalar(0, 255, 0, 0), 2, 8, 0);
                }
            }
            System.out.println("Número de rostos detectados: " + faceCount);

            imshow("WEBCAM", frame);
            if (waitKey(1) == 27) break;
        }

        capture.release();
        destroyAllWindows();
    }
}
