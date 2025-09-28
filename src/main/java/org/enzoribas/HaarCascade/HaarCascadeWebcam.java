package org.enzoribas.HaarCascade;

import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Rect;
import org.bytedeco.opencv.opencv_core.RectVector;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.enzoribas.Config;

import java.util.Scanner;

import static org.bytedeco.opencv.global.opencv_highgui.imshow;
import static org.bytedeco.opencv.global.opencv_highgui.waitKey;
import static org.bytedeco.opencv.global.opencv_imgproc.LINE_8;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;

public class HaarCascadeWebcam {

    public static void main(String[] args) {

        String projectPath = Config.PROJECT_PATH + "HaarCascade/"; // Adjust the input image path as needed
        String inputImagePath = projectPath+ "images/input/" + "input.jpg";
        String Object = "objects";

        String modelPath = "Resources/People/Faces/haarcascade_smile.xml";

        // Carrega classificador Haar (vem com o OpenCV)
        CascadeClassifier Classifier = new CascadeClassifier(projectPath+modelPath);

        VideoCapture capture = new VideoCapture(0);
        if (!capture.isOpened()) {
            System.out.println("Não foi possível abrir a webcam!");
            return;
        }

        Mat frame = new Mat();
        RectVector objects = new RectVector();

        while (true) {
            capture.read(frame);
            if (frame.empty()) break;

            Classifier.detectMultiScale(frame, objects);

            for (int i = 0; i < objects.size(); i++) {
                Rect hand = objects.get(i);
                rectangle(frame, hand, new Scalar(0, 255, 0, 0), 2, LINE_8, 0);
            }

            System.out.println("Número de " + Object +" " + objects.size());
            imshow("WEBCAM", frame);
            if (waitKey(1) == 27) break; // ESC para sair
        }

        capture.release();
    }
}
