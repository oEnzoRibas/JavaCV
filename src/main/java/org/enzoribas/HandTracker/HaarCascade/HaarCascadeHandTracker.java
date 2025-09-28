package org.enzoribas.HandTracker.HaarCascade;

import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.enzoribas.Config;

import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.LINE_8;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;

public class HaarCascadeHandTracker {

    public static void main(String[] args) {

        String projectPath = Config.PROJECT_PATH + "HandTracker/HaarCascade/"; // Adjust the input image path as needed
        String inputImagePath = projectPath+ "images/" + "input.jpg";
        String Object = "hands";

        Mat src = imread(inputImagePath);
        if (src.empty()) {
            System.out.println("Could not load image!");
            return;
        }

        // Carrega classificador Haar (vem com o OpenCV)
        CascadeClassifier faceDetector = new CascadeClassifier(projectPath+"resources/hand.xml");

        RectVector faces = new RectVector();
        faceDetector.detectMultiScale(
                src,                                 // imagem de entrada
                faces,                               // onde armazenar os retângulos detectados
                1.1,                                 // scaleFactor: quanto reduzir a imagem a cada escala (1,1 = 10% menor)
                1,                                  // minNeighbors: quantos vizinhos são necessários para confirmar um rosto
                0,                                  // flags: geralmente 0
                new Size(150, 150),     // minSize: tamanho mínimo do rosto
                new Size()                          // maxSize: tamanho máximo (ou vazio)
        );

        // Desenha retângulos nos rostos detectados
        for (int i = 0; i < faces.size(); i++) {
            Rect rect = faces.get(i);
            rectangle(src, rect, new Scalar(0, 255, 0, 0), 10, LINE_8, 0);;
        }


        String outputDir = projectPath +  "images/output/";;
        String fileName = Object+".png";

        imwrite(outputDir + fileName, src);
        System.out.println("✅ Detected " + faces.size() + Object);
        System.out.println("Saved " + fileName + " to " + outputDir);
    }
}
