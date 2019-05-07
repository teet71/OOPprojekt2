package oop;


import javafx.animation.AnimationTimer;
import javafx.animation.ScaleTransition;
import javafx.animation.Transition;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Minigolf extends Application {

    static int kordus = 0;

    public static Rectangle[][] failMänguväljaks(String rada) throws Exception {
        Rectangle[][] tagatis = new Rectangle[100][100];
        try (Scanner sc = new Scanner(new File("rajad/" + rada + ".txt"), StandardCharsets.UTF_8)) {
            int rida_arv = 0;
            while (rida_arv < 100) {
                String rida = sc.nextLine();
                if (rida.length() != 100) throw new ViganeRadaException("Leidus vigane rida", rida_arv);
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
                        case 'J': {
                            tagatis[rida_arv][i] = new Rectangle(6, 6, Color.LIGHTBLUE);
                            break;
                        }
                        default: {
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
                    } else if (mänguväljak[i][j].getFill() == Color.LIGHTBLUE) {
                        rida.append('J');
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

    public static Scene mänguStseen(String rada, Stage primaryStage, Scene peamenüü) throws Exception {
        Switch saabvajutada = new Switch();
        Switch pallvees = new Switch();
        Text skoor = new Text("Skoor: ");
        skoor.setX(10);
        skoor.setY(590);
        Text skoor2 = new Text(Integer.toString(kordus));
        skoor2.setX(55);
        skoor2.setY(590);
        Text parim = new Text("Parim:");
        parim.setX(10);
        parim.setY(570);
        Group juur = new Group();
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
        juur.getChildren().addAll(auk, pall, skoor, parim, skoor2);
        Scene mängimisSteen = new Scene(juur, 600, 600, Color.BLUE);
        mängimisSteen.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                if (saabvajutada.isaBoolean()) {
                    saabvajutada.setaBoolean(false);
                    pallvees.setaBoolean(false);
                    kordus++;
                    skoor2.setText(String.valueOf(kordus));
                    double[] alguspunkt = {pall.getCenterX(), pall.getCenterY()};
                    double[] sihtmärk = {event.getSceneX(), event.getSceneY()};
                    pall.setKiirus_x(0.05 * (sihtmärk[0] - pall.getCenterX()));
                    pall.setKiirus_y(0.05 * (sihtmärk[1] - pall.getCenterY()));
                    AnimationTimer timer = new AnimationTimer() {
                        @Override

                        public void handle(long now) {
                            double count = 0;
                            mainLoop:
                            while (count < 20) {
                                if (pallvees.isaBoolean()) {
                                    pall.setRadius(Math.max(0.0, pall.getRadius() - 0.2));
                                    count = 20;
                                    if (pall.getRadius() == 0) {
                                        pall.setRadius(8);
                                        pall.setCenterY(alguspunkt[1]);
                                        pall.setCenterX(alguspunkt[0]);
                                        pallvees.setaBoolean(false);
                                        saabvajutada.setaBoolean(true);
                                        this.stop();
                                    }
                                } else {
                                    if (Math.abs(pall.getCenterX() - auk.getCenterX()) < 12 && Math.abs(pall.getCenterY() - auk.getCenterY()) < 12) {
                                        pall.setKiirus_x(pall.getKiirus_x() + 0.001 * (auk.getCenterX() - pall.getCenterX()));
                                        pall.setKiirus_y(pall.getKiirus_y() + 0.001 * (auk.getCenterY() - pall.getCenterY()));
                                        if (Math.abs(pall.getCenterX() - auk.getCenterX()) < 1 && Math.abs(pall.getCenterY() - auk.getCenterY()) < 1) {
                                            this.stop();
                                            pall.setRadius(6);
                                            primaryStage.setScene(peamenüü);
                                            kordus = 0;
                                            //kui on augus!
                                        }
                                    }
                                    int x = (int) Math.round((pall.getCenterX() - 3) / 6);
                                    int y = (int) Math.round(pall.getCenterY() / 6);
                                    x = Math.min(95, x);
                                    x = Math.max(5, x);
                                    y = Math.min(95, y);
                                    y = Math.max(5, y);

                                    count++;
                                    pall.setCenterY(pall.getCenterY() + (pall.getKiirus_y() * 0.05));
                                    pall.setCenterX(pall.getCenterX() + (pall.getKiirus_x() * 0.05));
                                    for (int i = x - 5; i < x + 5; i++) {
                                        for (int j = y - 5; j < y + 5; j++) {
                                            if (mänguväli[j][i].getFill().equals(Color.GRAY)) {
                                                if (mänguväli[j][i].getBoundsInParent().intersects(pall.getBoundsInParent())) {
                                                    double angle;
                                                    double[] keskloik = new double[2];
                                                    keskloik[0] = mänguväli[j][i].getTranslateX() + 3 - (pall.getCenterX());
                                                    keskloik[1] = -(mänguväli[j][i].getTranslateY() + 3) + (pall.getCenterY());
                                                    if (keskloik[0] >= 0) {
                                                        if (keskloik[1] >= 0) {
                                                            angle = 90 - Math.toDegrees(Math.atan(keskloik[1] / keskloik[0]));

                                                        } else {
                                                            angle = 90 + Math.toDegrees(Math.atan(Math.abs(keskloik[1]) / keskloik[0]));
                                                        }
                                                    } else {
                                                        if (keskloik[1] >= 0) {
                                                            angle = 360 - Math.toDegrees(Math.atan(Math.abs(keskloik[0]) / keskloik[1]));

                                                        } else {
                                                            angle = 180 + Math.toDegrees(Math.atan(Math.abs(Math.abs(keskloik[0]) / keskloik[1])));
                                                        }
                                                    }
                                                    if (angle < 45.0 || angle > 315.0 || ((angle > 135) && (angle < 225))) {
                                                        pall.setKiirus_y(-pall.getKiirus_y() * 0.8);
                                                        pall.setCenterY(pall.getCenterY() + (0.1 * pall.getKiirus_y()));
                                                        pall.setCenterX(pall.getCenterX() - (0.1 * pall.getKiirus_x()));
                                                    } else {
                                                        pall.setKiirus_x(-pall.getKiirus_x() * 0.8);
                                                        pall.setCenterX(pall.getCenterX() + (0.1 * pall.getKiirus_x()));
                                                        pall.setCenterY(pall.getCenterY() - (0.1 * pall.getKiirus_y()));
                                                    }
                                                    break mainLoop;
                                                }
                                            }
                                        }
                                    }
                                    x = (int) Math.round((pall.getCenterX() - 3) / 6);
                                    y = (int) Math.round(pall.getCenterY() / 6);
                                    x = Math.min(99, x);
                                    x = Math.max(0, x);
                                    y = Math.min(99, y);
                                    y = Math.max(0, y);
                                    if (mänguväli[y][x].getFill().equals(Color.GREEN)) {
                                        pall.setKiirus_x((pall.getKiirus_x() * 0.999));
                                        pall.setKiirus_y((pall.getKiirus_y() * 0.999));
                                    } else if (mänguväli[y][x].getFill().equals(Color.YELLOW)) {
                                        pall.setKiirus_x((pall.getKiirus_x() * 0.995));
                                        pall.setKiirus_y((pall.getKiirus_y() * 0.995));
                                    } else if (mänguväli[y][x].getFill().equals(Color.LIGHTBLUE)) {
                                        pall.setKiirus_x((pall.getKiirus_x() * 1.002));
                                        pall.setKiirus_y((pall.getKiirus_y() * 1.002));
                                    } else if (mänguväli[y][x].getFill().equals(Color.BLUE)) {
                                        pall.setKiirus_x(0);
                                        pall.setKiirus_y(0);
                                        pallvees.setaBoolean(true);
                                        break;
                                    }
                                    if (Math.abs(pall.getKiirus_y()) + Math.abs(pall.getKiirus_x()) < 0.03 && !pallvees.isaBoolean()) {
                                        pall.setKiirus_y(0);
                                        pall.setKiirus_x(0);
                                        saabvajutada.setaBoolean(true);
                                        this.stop();
                                        break;
                                    }
                                }
                            }


                        }
                    };
                    timer.start();
                }


            }
        });
        return mängimisSteen;
    }

    public static Scene mapEditorStseen(Stage primaryStage, Scene peamenüü) throws Exception {
        Circle lõppauk = new Circle(0, Color.BLACK);
        Text alguspunkt = new Text("P");
        alguspunkt.setFont(new Font(10));
        alguspunkt.setFill(Color.RED);
        Circle pintsel = new Circle(0, Color.GREEN);
        Group juur = new Group();
        Rectangle[][] test = new Rectangle[100][100];
        Button vesi = new Button("vesi");
        vesi.setTranslateX(100);
        vesi.setTranslateY(610);
        vesi.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                pintsel.setFill(Color.BLUE);
                pintsel.setRadius(0);
            }
        });
        Button liiv = new Button("liiv");
        liiv.setTranslateX(150);
        liiv.setTranslateY(610);
        liiv.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                pintsel.setFill(Color.YELLOW);
                pintsel.setRadius(0);
            }
        });
        Button jää = new Button("jää");
        jää.setTranslateX(10);
        jää.setTranslateY(610);
        jää.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                pintsel.setFill(Color.LIGHTBLUE);
                pintsel.setRadius(0);
            }
        });
        Button muru = new Button("muru");
        muru.setTranslateX(200);
        muru.setTranslateY(610);
        muru.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                pintsel.setFill(Color.GREEN);
                pintsel.setRadius(0);
            }
        });
        Button sein = new Button("sein");
        sein.setTranslateX(250);
        sein.setTranslateY(610);
        sein.setOnMouseClicked(mouseEvent -> {
            pintsel.setFill(Color.GRAY);
            pintsel.setRadius(0);
        });
        Button auk = new Button("auk");
        auk.setTranslateX(300);
        auk.setTranslateY(610);
        auk.setOnMouseClicked(mouseEvent -> pintsel.setRadius(1));
        Button algus = new Button("algus");
        algus.setTranslateX(350);
        algus.setTranslateY(610);
        algus.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                pintsel.setRadius(2);
            }
        });
        Button finish = new Button("finish");
        finish.setTranslateX(450);
        finish.setTranslateY(610);
        finish.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                int[] aukkord = {(int) Math.round(lõppauk.getCenterX() / 6), (int) Math.round(lõppauk.getCenterY() / 6)};
                int[] alguskord = {(int) Math.round(alguspunkt.getX() / 6), (int) Math.round(alguspunkt.getY() / 6)};
                try {
                    radaFailiks("test", test, alguskord, aukkord);
                } catch (Exception e) {
                    primaryStage.close();
                }
                primaryStage.setScene(peamenüü);
            }
        });
        juur.getChildren().addAll(vesi, liiv, muru, sein, jää);
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test[i].length; j++) {
                test[i][j] = new Rectangle(6, 6, Color.BLUE);
            }
        }
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test[i].length; j++) {
                juur.getChildren().add(test[i][j]);
                juur.getChildren().get(juur.getChildren().size() - 1).setTranslateX(6 * j);
                juur.getChildren().get(juur.getChildren().size() - 1).setTranslateY(6 * i);
            }
        }
        juur.getChildren().addAll(auk, algus, finish);
        juur.getChildren().addAll(lõppauk, alguspunkt);
        Scene radaloomissteen = new Scene(juur, 600, 650, Color.SNOW);
        radaloomissteen.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int x = (int) Math.round(event.getSceneX() / 6);
                int y = (int) Math.round(event.getSceneY() / 6);
                if (y < 100) {
                    if (pintsel.getRadius() == 0) {
                        for (int i = Math.max(0, x - 1); i < Math.min(100, x + 1); i++) {
                            for (int j = Math.max(0, y - 1); j < Math.min(100, y + 1); j++) {
                                test[j][i].setFill(pintsel.getFill());
                            }
                        }
                    }
                }
            }
        });
        radaloomissteen.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int x = (int) Math.round(event.getSceneX() / 6);
                int y = (int) Math.round(event.getSceneY() / 6);
                if (pintsel.getRadius() == 2) {
                    alguspunkt.setText("S");
                    alguspunkt.setX(x * 6);
                    alguspunkt.setY(y * 6);
                } else if (pintsel.getRadius() == 1) {
                    lõppauk.setRadius(10);
                    lõppauk.setCenterX(x * 6);
                    lõppauk.setCenterY(y * 6);
                } else if (pintsel.getRadius() == 0) {
                    for (int i = Math.max(0, x - 1); i < Math.min(100, x + 1); i++) {
                        for (int j = Math.max(0, y - 1); j < Math.min(100, y + 1); j++) {
                            test[j][i].setFill(pintsel.getFill());
                        }
                    }
                }
            }
        });

        return radaloomissteen;
    }

    public static Scene menüüStseen(Stage primaryStage) throws Exception {
        Group juur = new Group();
        Button play = new Button("Mängi");
        play.setOnMouseClicked(mouseEvent -> {
            try {
                primaryStage.setScene(mänguStseen("test", primaryStage, primaryStage.getScene()));
            } catch (Exception e) {
                primaryStage.close();
            }
        });
        juur.getChildren().add(play);
        Button ehita = new Button("ehita");
        ehita.setTranslateX(300);
        ehita.setOnMouseClicked(new EventHandler<MouseEvent>() {
            public void handle(MouseEvent mouseEvent) {
                try {
                    primaryStage.setScene(mapEditorStseen(primaryStage, primaryStage.getScene()));
                } catch (Exception e) {
                    primaryStage.close();
                }
            }
        });
        juur.getChildren().add(ehita);
        Scene menüüSteen = new Scene(juur, 600, 600, Color.BLUE);
        return menüüSteen;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("MiniGolf");
        primaryStage.setScene(menüüStseen(primaryStage));
        primaryStage.show();
    }

    public static void main(String[] args) throws Exception {
        launch(args);

    }
}
