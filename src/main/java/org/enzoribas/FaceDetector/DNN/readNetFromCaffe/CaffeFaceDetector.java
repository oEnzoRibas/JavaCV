package org.enzoribas.FaceDetector.DNN.readNetFromCaffe;

import org.bytedeco.javacpp.FloatPointer;
import org.bytedeco.javacpp.indexer.FloatIndexer;
import org.bytedeco.opencv.opencv_dnn.Net;
import static org.bytedeco.opencv.global.opencv_dnn.*;
import static org.bytedeco.opencv.global.opencv_imgcodecs.*;
import static org.bytedeco.opencv.global.opencv_imgproc.*;
import org.bytedeco.opencv.opencv_core.*;


import org.enzoribas.Config;
import org.opencv.core.CvType;

import javax.swing.*;

public class CaffeFaceDetector {
    public static void main(String[] args) {
        String projectPath = Config.PROJECT_PATH + "FaceDetector/DNN/readNetFromCaffe/";
        String imagePath = projectPath + "images/input.jpg";
        String protoPath = projectPath + "resources/deploy.prototxt";
        String modelPath = projectPath + "resources/res10_300x300_ssd_iter_140000.caffemodel";

        System.out.println("Carregando rede...");
        Net net = readNetFromCaffe(protoPath, modelPath); // Bytedeco
        System.out.println("Rede carregada com sucesso!");

        System.out.println("Carregando imagem...");
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Selecionar Arquivo");
        jfc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png"));
        int result = jfc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            imagePath = jfc.getSelectedFile().getAbsolutePath();
        } else {
            System.out.println("Nenhum arquivo selecionado. Usando imagem padrão.");
        }

        Mat image = imread(imagePath);
        if (image.empty()) {
            System.out.println("Could not load image!");
            return;
        }
        System.out.println("Imagem carregada com sucesso!");

        System.out.println("Detectando faces...");
        Mat blob = blobFromImage(image,
                1.0,
                new Size(300, 300),
                new Scalar(104.0, 177.0, 123.0, 0.0),
                false,
                false,
                CvType.CV_32F);
        System.out.println("Blob criado com sucesso!");

        net.setInput(blob);

        System.out.println("Realizando forward pass...");
        Mat detections = net.forward();

        FloatPointer data = new FloatPointer(detections.data().asByteBuffer().asFloatBuffer());
        FloatIndexer indexer = detections.createIndexer();
        int rows = detections.size(2);
        for (int i = 0; i < rows; i++) {
            float confidence = indexer.get(0, 0, i, 2);
            if (confidence > 0.5) {
                int x1 = (int) (indexer.get(0, 0, i, 3) * image.cols());
                int y1 = (int) (indexer.get(0, 0, i, 4) * image.rows());
                int x2 = (int) (indexer.get(0, 0, i, 5) * image.cols());
                int y2 = (int) (indexer.get(0, 0, i, 6) * image.rows());
                rectangle(image, new Point(x1, y1), new Point(x2, y2), new Scalar(0, 255, 0, 0), 2, 8, 0);
            }
        }
        System.out.println("Faces detectadas e retângulos desenhados!");

        System.out.println("Salvando imagem de saída...");
        imwrite(projectPath + "images/output/faces_dnn.jpg", image);
        System.out.println("Detecção finalizada!");
    }
}
