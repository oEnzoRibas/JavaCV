package org.enzoribas.HaarCascade;

import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.enzoribas.Config;

import javax.swing.*;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;
import static org.bytedeco.opencv.global.opencv_imgcodecs.imwrite;
import static org.bytedeco.opencv.global.opencv_imgproc.LINE_8;
import static org.bytedeco.opencv.global.opencv_imgproc.rectangle;

public class HaarCascade {

    public static void main(String[] args) {

        String projectPath = Config.PROJECT_PATH + "HaarCascade/"; // Adjust the input image path as needed
        String inputImagePath = projectPath+ "images/input/" + "cars.jpg";
        String Object = "objects";

        System.out.println("Carregando imagem...");
        JFileChooser jfc = new JFileChooser();
        jfc.setDialogTitle("Selecionar Arquivo");
        jfc.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "Image files", "jpg", "jpeg", "png"));
        int result = jfc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            inputImagePath = jfc.getSelectedFile().getAbsolutePath();
        } else {
            System.out.println("Nenhum arquivo selecionado. Usando imagem padrão.");
        }

        Mat src = imread(inputImagePath);
        if (src.empty()) {
            System.out.println("Could not load image!");
            return;
        }

        String modelPath = "Resources/Objects/Cars/";
        String modelName = "cascade2";
        String modelExtension =".xml";

        // Carrega classificador Haar (vem com o OpenCV)
        CascadeClassifier Classifier = new CascadeClassifier(projectPath+modelPath+modelName+modelExtension);

        RectVector objects = new RectVector();
        Classifier.detectMultiScale(
                src,                                 // imagem de entrada
                objects,                               // onde armazenar os retângulos detectados
                1.1,                                 // scaleFactor: quanto reduzir a imagem a cada escala (1,1 = 10% menor)
                10,                                  // minNeighbors: quantos vizinhos são necessários para confirmar um objeto
                0,                                  // flags: geralmente 0
                new Size(30, 30),     // minSize: tamanho mínimo do rosto
                new Size()                          // maxSize: tamanho máximo (ou vazio)
        );

        // Desenha retângulos nos rostos detectados
        for (int i = 0; i < objects.size(); i++) {
            Rect rect = objects.get(i);
            rectangle(src, rect, new Scalar(0, 255, 0, 0), 3, LINE_8, 0);;
        }


        String outputDir = projectPath +  "images/output/"+modelName+"/";;
        String fileName = (100*Math.random())+modelName+".png";

        imwrite(outputDir + fileName, src);
        System.out.println("✅ Detected " + objects.size() +" "+ Object);
        System.out.println("Saved " + fileName + " to " + outputDir);
    }
}
