package oop;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Minigolf extends Application {
    public static Rectangle[][] failMänguväljaks(String rada) throws Exception {
        Rectangle[][] tagatis = new Rectangle[100][100];
        try (Scanner sc = new Scanner(new File("rajad/" + rada + ".txt"), StandardCharsets.UTF_8)) {
            int rida_arv = 0;
            while (rida_arv<100) {
                String rida = sc.nextLine();
                for (int i = 0; i < rida.length(); i++) {
                    switch (rida.charAt(i)) {
                        case '#': {
                            tagatis[rida_arv][i] = new Rectangle(6, 6, Color.GRAY);
                            break;
                        }
                        case 'O': {
                            tagatis[rida_arv][i] = new Rectangle(6, 6, Color.BLUE);
                            break;
                        }
                        case 'S': {
                            tagatis[rida_arv][i] = new Rectangle(6, 6, Color.YELLOW);
                            break;
                        }
                        case '%': {
                            tagatis[rida_arv][i] = new Rectangle(6, 6, Color.GREEN);
                            break;
                        }
                    }
                }
                rida_arv++;
            }
        }
        return tagatis;
    }
    public static void radaFailiks(String rada, Rectangle[][] mänguväljak, int[] algus, int[] lõpp) throws Exception{
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(("rajad/"+rada+".txt"), StandardCharsets.UTF_8))) {
            for (int i = 0; i < mänguväljak.length; i++) {
                StringBuilder rida = new StringBuilder();
                for (int j = 0; j < mänguväljak[i].length; j++) {
                    if (mänguväljak[i][j].getFill() == Color.GREY) {
                        rida.append('#');
                    } else if (mänguväljak[i][j].getFill() == Color.BLUE) {
                        rida.append('O');
                    } else if (mänguväljak[i][j].getFill() == Color.YELLOW) {
                        rida.append('S');
                    }
                    if (mänguväljak[i][j].getFill() == Color.GREEN) {
                        rida.append('%');
                    }
                }
                bw.write(rida.toString());
                bw.newLine();
            }
            bw.write(algus[0]+" "+algus[1]+" "+lõpp[0]+" "+lõpp[1]);
        }
    }
    public static Pall failistPall(String rada) throws  Exception{
        Pall pall;
        try (Scanner sc = new Scanner(new File("rajad/" + rada + ".txt"), StandardCharsets.UTF_8)) {
            int rida_arv = 0;
            while (rida_arv<100) {
                sc.nextLine();
                rida_arv++;
            }
            String[] rida = sc.nextLine().split(" ");
            pall = new Pall(Integer.parseInt(rida[0])*6.0, Integer.parseInt(rida[1])*6.0, 3, Color.WHITE);
        }
        return pall;
    }
    public static Circle failistAuk(String rada) throws Exception{
        Circle auk;
        try (Scanner sc = new Scanner(new File("rajad/" + rada + ".txt"), StandardCharsets.UTF_8)) {
            int rida_arv = 0;
            while (rida_arv<100) {
                sc.nextLine();
                rida_arv++;
            }
            String[] rida = sc.nextLine().split(" ");
            auk = new Circle(Integer.parseInt(rida[2])*6.0, Integer.parseInt(rida[3])*6.0, 3, Color.BLACK);
        }
        return auk;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group juur = new Group();
        primaryStage.setTitle("MiniGolf");
        Rectangle[][] mänguväli = failMänguväljaks("test");
        for (int i = 0; i < mänguväli.length; i++) {
            for (int j = 0; j < mänguväli[i].length; j++) {
                juur.getChildren().add(mänguväli[i][j]);
                juur.getChildren().get(juur.getChildren().size()-1).setTranslateX(6*j);
                juur.getChildren().get(juur.getChildren().size()-1).setTranslateY(6*i);
            }
        }
        Pall pall = failistPall("test");
        Circle auk = failistAuk("test");
        juur.getChildren().addAll(pall, auk);
        Scene peaSteen = new Scene(juur, 600, 600, Color.BLUE);
        primaryStage.setScene(peaSteen);
        primaryStage.show();
    }

    public static void main(String[] args)throws  Exception {
        Rectangle[][] test = new Rectangle[100][100];
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test[i].length; j++) {
                if (j>20&&j<80&&i>20&&i<80) test[i][j] = new Rectangle(6,6, Color.GREEN);
                else test[i][j] = new Rectangle(6,6, Color.BLUE);
            }

        }
        int[] algus = {50, 75};
        int[] lõpp = {50, 25};
        radaFailiks("test", test, algus, lõpp);
        launch(args);

    }
}
