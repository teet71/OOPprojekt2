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
                if (rida.length()!=100) throw new ViganeRadaException("Leidus vigane rida", rida_arv);
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
                        default:{
                            throw new ViganeRadaException("Leidus vigane rida", rida_arv);
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
                    pall.setKiirus_x(0.05*(sihtmärk[0] - pall.getCenterX()));
                    pall.setKiirus_y(0.05*(sihtmärk[1] - pall.getCenterY()));
                    AnimationTimer timer = new AnimationTimer() {
                        @Override

                        public void handle(long now) {
                            double count = 0;
                            mainLoop: while (count<10){
                            if (Math.abs(pall.getCenterX()-auk.getCenterX())<12&&Math.abs(pall.getCenterY()-auk.getCenterY())<12){
                                pall.setKiirus_x(pall.getKiirus_x()+0.005*(auk.getCenterX()-pall.getCenterX()));
                                pall.setKiirus_y(pall.getKiirus_y()+0.005*(auk.getCenterY()-pall.getCenterY()));
                                if (Math.abs(pall.getCenterX()-auk.getCenterX())<1&&Math.abs(pall.getCenterY()-auk.getCenterY())<1){
                                    this.stop();
                                    pall.setRadius(6);
                                    //kui on augus!
                                }
                            }
                            int x = (int) Math.round((pall.getCenterX()-3) / 6);
                            int y = (int) Math.round(pall.getCenterY() / 6);
                            x = Math.min(95,x);
                            x = Math.max(5,x);
                            y = Math.min(95,y);
                            y = Math.max(5,y);

                            count++;
                                pall.setCenterY(pall.getCenterY()+(pall.getKiirus_y()*0.1));
                                pall.setCenterX(pall.getCenterX()+(pall.getKiirus_x()*0.1));
                                for (int i = x-5; i < x+5; i++) {
                                    for (int j = y-5; j < y+5; j++) {
                                    if (mänguväli[j][i].getFill().equals(Color.GRAY)){
                                        if (mänguväli[j][i].getBoundsInParent().intersects(pall.getBoundsInParent())){
                                            double angle;
                                            double[] keskloik = new double[2];
                                            keskloik[0] =  mänguväli[j][i].getTranslateX()+3 - (pall.getCenterX());
                                            keskloik[1] = -(mänguväli[j][i].getTranslateY()+3)+(pall.getCenterY());
                                            if (keskloik[0] >= 0){
                                                if (keskloik[1] >= 0){
                                                    angle = 90 - Math.toDegrees(Math.atan(keskloik[1]/keskloik[0]));

                                                }
                                                else {
                                                    angle = 90 + Math.toDegrees(Math.atan(Math.abs(keskloik[1])/keskloik[0]));
                                                }}
                                            else {
                                                if (keskloik[1] >= 0){
                                                    angle = 360 - Math.toDegrees(Math.atan(Math.abs(keskloik[0])/keskloik[1]));

                                                }
                                                else {
                                                    angle = 180 + Math.toDegrees(Math.atan(Math.abs(Math.abs(keskloik[0])/keskloik[1])));
                                                }
                                            }
                                            System.out.println(angle);
                                            if (angle<45.0||angle>315.0||((angle>135)&&(angle<225))){
                                                pall.setKiirus_y(-pall.getKiirus_y()*0.8);
                                                pall.setCenterY(pall.getCenterY()+(0.1*pall.getKiirus_y()));
                                                pall.setCenterX(pall.getCenterX()-(0.1*pall.getKiirus_x()));
                                            }
                                            else  {
                                                pall.setKiirus_x(-pall.getKiirus_x()*0.8);
                                                pall.setCenterX(pall.getCenterX()+(0.1*pall.getKiirus_x()));
                                                pall.setCenterY(pall.getCenterY()-(0.1*pall.getKiirus_y()));
                                            }
                                            break mainLoop;}
                            }}}
                            x = (int) Math.round((pall.getCenterX()-3) / 6);
                            y = (int) Math.round(pall.getCenterY() / 6);
                            x = Math.min(99,x);
                            x = Math.max(0,x);
                            y = Math.min(99,y);
                            y = Math.max(0,y);
                            if (mänguväli[y][x].getFill().equals(Color.GREEN)) {
                                pall.setKiirus_x((pall.getKiirus_x() * 0.995));
                                pall.setKiirus_y((pall.getKiirus_y() * 0.995));
                            }
                            else if (mänguväli[y][x].getFill().equals(Color.YELLOW)) {
                                pall.setKiirus_x((pall.getKiirus_x() * 0.98));
                                pall.setKiirus_y((pall.getKiirus_y() * 0.98));
                            }
                            else if (mänguväli[y][x].getFill().equals(Color.BLUE)) {
                                pall.setKiirus_x(0);
                                pall.setKiirus_y(0);
                                pall.setCenterX(alguspunkt[0]);
                                pall.setCenterY(alguspunkt[1]);
                                saabvajutada.setaBoolean(true);
                                this.stop();
                            }
                            if (Math.abs(pall.getKiirus_y())+Math.abs(pall.getKiirus_x()) < 0.01) {
                                    pall.setKiirus_y(0);
                                    pall.setKiirus_x(0);
                                    saabvajutada.setaBoolean(true);
                                    this.stop();
                                }
                        }


                    }};
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
                if (i<55&&i>45&&j>45&&j<55) test[i][j] = new Rectangle(6, 6, Color.GRAY);
                else if (i==20||i==80||j==20||j==80) test[i][j] = new Rectangle(6, 6, Color.GRAY);
                else if (i>35&&i<65&&j > 20 && j < 80) test[i][j] = new Rectangle(6, 6, Color.YELLOW);
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
