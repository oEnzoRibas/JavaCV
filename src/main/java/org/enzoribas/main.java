package org.enzoribas;
import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.global.opencv_core;

public class main {
    public static void main(String[] args) {
        try {
            Loader.load(opencv_core.class);
            System.out.println("✅ OpenCV inicializado!");
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
        }
    }
    }
