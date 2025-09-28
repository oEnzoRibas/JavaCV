package org.enzoribas.HandTracker.HaarCascade;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.enzoribas.Config;
import org.opencv.core.CvType;

import java.util.Scanner;

import static org.bytedeco.opencv.global.opencv_dnn.blobFromImage;
import static org.bytedeco.opencv.global.opencv_highgui.imshow;
import static org.bytedeco.opencv.global.opencv_highgui.waitKey;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.*;

public class HaarCascadeHandTrackerWebcam {

    public static void main(String[] args) {

        String projectPath = Config.PROJECT_PATH + "HandTracker/HaarCascade/"; // Adjust the input image path as needed
        String inputImagePath = projectPath + "images/" + "input.jpg";
        String Object = "hands";



        Scanner kIn = new Scanner(System.in);

        // Carrega classificador Haar (vem com o OpenCV)
        CascadeClassifier handTracker = new CascadeClassifier(projectPath + "resources/"+ "fist.xml");

        VideoCapture capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            System.out.println("Não foi possível abrir a webcam!");
            return;
        }

        Mat frame = new Mat();
        RectVector hands = new RectVector();

        while (true) {
            capture.read(frame);
            if (frame.empty()) break;

            handTracker.detectMultiScale(frame, hands);

            for (int i = 0; i < hands.size(); i++) {
                Rect hand = hands.get(i);
                rectangle(frame, hand, new Scalar(0, 255, 0, 0), 2, LINE_8, 0);
            }

            System.out.println("Número de mãos detectadas: " + hands.size());
            imshow("WEBCAM", frame);
            if (waitKey(1) == 27) break; // ESC para sair
        }

        capture.release();
    }
}
