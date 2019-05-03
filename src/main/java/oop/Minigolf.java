package oop;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
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
            while (rida_arv < 100) {
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

    public static void radaFailiks(String rada, Rectangle[][] mänguväljak, int[] algus, int[] lõpp) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(("rajad/" + rada + ".txt"), StandardCharsets.UTF_8))) {
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
            bw.write(algus[0] + " " + algus[1] + " " + lõpp[0] + " " + lõpp[1]);
        }
    }

    public static Pall failistPall(String rada) throws Exception {
        Pall pall;
        try (Scanner sc = new Scanner(new File("rajad/" + rada + ".txt"), StandardCharsets.UTF_8)) {
            int rida_arv = 0;
            while (rida_arv < 100) {
                sc.nextLine();
                rida_arv++;
            }
            String[] rida = sc.nextLine().split(" ");
            pall = new Pall(Integer.parseInt(rida[0]) * 6.0, Integer.parseInt(rida[1]) * 6.0, 8, Color.WHITE);
        }
        return pall;
    }

    public static Circle failistAuk(String rada) throws Exception {
        Circle auk;
        try (Scanner sc = new Scanner(new File("rajad/" + rada + ".txt"), StandardCharsets.UTF_8)) {
            int rida_arv = 0;
            while (rida_arv < 100) {
                sc.nextLine();
                rida_arv++;
            }
            String[] rida = sc.nextLine().split(" ");
            auk = new Circle(Integer.parseInt(rida[2]) * 6.0, Integer.parseInt(rida[3]) * 6.0, 10, Color.BLACK);
        }
        return auk;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Switch saabvajutada = new Switch();
        Group juur = new Group();
        primaryStage.setTitle("MiniGolf");
        Rectangle[][] mänguväli = failMänguväljaks("test");
        for (int i = 0; i < mänguväli.length; i++) {
            for (int j = 0; j < mänguväli[i].length; j++) {
                juur.getChildren().add(mänguväli[i][j]);
                juur.getChildren().get(juur.getChildren().size() - 1).setTranslateX(6 * j);
                juur.getChildren().get(juur.getChildren().size() - 1).setTranslateY(6 * i);
            }
        }
        Circle auk = failistAuk("test");
        Pall pall = failistPall("test");
        juur.getChildren().addAll(auk, pall);
        Scene peaSteen = new Scene(juur, 600, 600, Color.BLUE);

        peaSteen.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (saabvajutada.isaBoolean()) {
                    saabvajutada.setaBoolean(false);
                    double[] alguspunkt = {pall.getCenterX(), pall.getCenterY()};
                    double[] sihtmärk = {event.getSceneX(), event.getSceneY()};
                    pall.setKiirus_x(sihtmärk[0] - pall.getCenterX());
                    pall.setKiirus_y(sihtmärk[1] - pall.getCenterY());
                    AnimationTimer timer = new AnimationTimer() {
                        @Override

                        public void handle(long now) {
                            if (Math.abs(pall.getCenterX()-auk.getCenterX())<10&&Math.abs(pall.getCenterY()-auk.getCenterY())<10){
                                pall.setKiirus_x(pall.getKiirus_x()+0.5*(auk.getCenterX()-pall.getCenterX()));
                                pall.setKiirus_y(pall.getKiirus_y()+0.5*(auk.getCenterY()-pall.getCenterY()));
                                if (Math.abs(pall.getCenterX()-auk.getCenterX())<1&&Math.abs(pall.getCenterY()-auk.getCenterY())<1){
                                    this.stop();
                                }
                            }
                            pall.setCenterY(pall.getCenterY() + pall.getKiirus_y() * 0.05);
                            pall.setCenterX(pall.getCenterX() + pall.getKiirus_x() * 0.05);
                            int x = (int) Math.round(pall.getCenterX() / 6);
                            int y = (int) Math.round(pall.getCenterY() / 6);
                            if (mänguväli[y][x].getFill().equals(Color.GREEN)) {
                                pall.setKiirus_x((pall.getKiirus_x() * 0.96));
                                pall.setKiirus_y((pall.getKiirus_y() * 0.96));
                                if (Math.abs(pall.getKiirus_y()) < 0.2) pall.setKiirus_y(0);
                                if (Math.abs(pall.getKiirus_x()) < 0.2) pall.setKiirus_x(0);
                                if (pall.getKiirus_y() == 0 && pall.getKiirus_x() == 0) {
                                    saabvajutada.setaBoolean(true);
                                    this.stop();
                                }
                            }
                            else if (mänguväli[y][x].getFill().equals(Color.YELLOW)) {
                                pall.setKiirus_x((pall.getKiirus_x() * 0.85));
                                pall.setKiirus_y((pall.getKiirus_y() * 0.85));
                                if (Math.abs(pall.getKiirus_y()) < 0.1) pall.setKiirus_y(0);
                                if (Math.abs(pall.getKiirus_x()) < 0.1) pall.setKiirus_x(0);
                                if (pall.getKiirus_y() == 0 && pall.getKiirus_x() == 0) {
                                    saabvajutada.setaBoolean(true);
                                    this.stop();
                                }
                            }
                            if (mänguväli[y][x].getFill().equals(Color.BLUE)) {
                                pall.setKiirus_x(0);
                                pall.setKiirus_y(0);
                                pall.setCenterX(alguspunkt[0]);
                                pall.setCenterY(alguspunkt[1]);
                                saabvajutada.setaBoolean(true);
                                this.stop();
                            }
                        }


                    };
                    timer.start();
                }


            }
        });
        primaryStage.setScene(peaSteen);
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        Rectangle[][] test = new Rectangle[100][100];
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test[i].length; j++) {
                if (i>35&&i<65&&j > 20 && j < 80) test[i][j] = new Rectangle(6, 6, Color.YELLOW);
                else if (j > 20 && j < 80 && i > 20 && i < 80) test[i][j] = new Rectangle(6, 6, Color.GREEN);
                else test[i][j] = new Rectangle(6, 6, Color.BLUE);
            }

        }
        int[] algus = {50, 75};
        int[] lõpp = {50, 25};
        radaFailiks("test", test, algus, lõpp);
        launch(args);

    }
}
