package oop;


import javafx.animation.*;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Minigolf extends Application {

    static private int kordus = 0;
    static private boolean saabvajutada;
    static private boolean pallvees;
    static private boolean pallsees;

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

    public static List<String> listFilesForFolder() {//Tuleb lisada path siia
        List<String> results = new ArrayList<>();
        File[] files = new File("rajad\\").listFiles();

        for (File file : files) {
            if (file.isFile()) {
                results.add(file.getName().substring(0, file.getName().length() - 4));
            }
        }
        return results;
    }

    public static void radaFailiks(Rectangle[][] mänguväljak, Pall algus, Circle lõpp, String rada, int rekord) throws Exception {
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
            bw.write((int) algus.getCenterX() / 6 + " " + (int) algus.getCenterY() / 6 + " "
                    + (int) lõpp.getCenterX() / 6 + " " + (int) lõpp.getCenterY() / 6 + " " + rekord);
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

    public static String failistRekord(String rada) throws Exception {
        String parim;
        try (Scanner sc = new Scanner(new File("rajad/" + rada + ".txt"), StandardCharsets.UTF_8)) {
            int rida_arv = 0;
            while (rida_arv < 100) {
                sc.nextLine();
                rida_arv++;
            }
            String[] rida = sc.nextLine().split(" ");
            parim = rida[4];
        }
        return parim;
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

    public static Scene mänguStseen(Stage primaryStage, String rada) throws Exception {
        saabvajutada = true;
        pallvees = false;
        pallsees = false;
        Text skoor = new Text("Skoor: ");
        skoor.setX(10);
        skoor.setY(590);
        Text skoor2 = new Text(Integer.toString(kordus));
        skoor2.setX(55);
        skoor2.setY(590);
        Text parim = new Text("Parim:");
        parim.setX(10);
        parim.setY(570);
        Text parim2 = new Text();
        if (failistRekord(rada).equals("0")) parim2.setText("puudub");
        else parim2.setText(failistRekord(rada));
        parim2.setX(55);
        parim2.setY(570);
        Group juur = new Group();

        Rectangle[][] mänguväli = failMänguväljaks(rada);
        for (int i = 0; i < mänguväli.length; i++) {
            for (int j = 0; j < mänguväli[i].length; j++) {
                juur.getChildren().add(mänguväli[i][j]);
                juur.getChildren().get(juur.getChildren().size() - 1).setTranslateX(6 * j);
                juur.getChildren().get(juur.getChildren().size() - 1).setTranslateY(6 * i);
            }
        }
        Circle auk = failistAuk(rada);
        Pall pall = failistPall(rada);
        juur.getChildren().addAll(auk, pall, skoor, parim, skoor2, parim2);
        Scene mängimisSteen = new Scene(juur, 600, 600, Color.GREEN);
        mängimisSteen.setOnMouseClicked(new EventHandler<>() {
            @Override
            public void handle(MouseEvent event) {
                if (saabvajutada&&!pallsees) {
                    saabvajutada = false;
                    pallvees = false;
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
                                try {
                                if (pall.getCenterY()<-3||pall.getCenterY()>603||pall.getCenterX()<-3||pall.getCenterX()>603) {
                                    throw new PallVäljasMapistException("Pall sattus väljas poole rada!");
                                }}
                                catch (PallVäljasMapistException e) {
                                        System.out.println(e.getMessage());
                                        primaryStage.close();
                                        break mainLoop;
                                    }
                                if (pallsees) {
                                    this.stop();
                                    LocalDateTime aeg = LocalDateTime.now();
                                    while (LocalDateTime.now().getSecond()-aeg.getSecond()<1.2){
                                    }
                                    try {
                                        primaryStage.setScene(menüüStseen(primaryStage));
                                    } catch (Exception e) {
                                        primaryStage.close();
                                    }

                                    try {
                                        if (kordus < Integer.parseInt(failistRekord(rada))||Integer.parseInt(failistRekord(rada))==0) {
                                            radaFailiks(failMänguväljaks(rada), failistPall(rada), failistAuk(rada), rada, kordus);
                                        }
                                    } catch (Exception e) {
                                        primaryStage.close();
                                    }
                                    kordus = 0;
                                    break mainLoop;
                                }
                                else if (pallvees) {
                                    pall.setRadius(Math.max(0.0, pall.getRadius() - 0.2));
                                    count = 20;
                                    if (pall.getRadius() == 0) {
                                        pall.setRadius(8);
                                        pall.setCenterY(alguspunkt[1]);
                                        pall.setCenterX(alguspunkt[0]);
                                        pallvees = false;
                                        saabvajutada = true;
                                        this.stop();
                                    }
                                } else {
                                    if (Math.abs(pall.getCenterX() - auk.getCenterX()) < 11 && Math.abs(pall.getCenterY() - auk.getCenterY()) < 11) {
                                        pall.setKiirus_x(pall.getKiirus_x() + 0.0003 * (auk.getCenterX() - pall.getCenterX())*(Math.abs(auk.getCenterX() - pall.getCenterX())));
                                        pall.setKiirus_y(pall.getKiirus_y() + 0.0003 * (auk.getCenterY() - pall.getCenterY())*(Math.abs(auk.getCenterY() - pall.getCenterY())));
                                        if (Math.abs(pall.getCenterX() - auk.getCenterX()) < 2 && Math.abs(pall.getCenterY() - auk.getCenterY()) < 2) {
                                            pall.setRadius(6.8);
                                            pallsees = true;
                                            break mainLoop;
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
                                        pall.setKiirus_x((pall.getKiirus_x() * 0.9995));
                                        pall.setKiirus_y((pall.getKiirus_y() * 0.9995));
                                    } else if (mänguväli[y][x].getFill().equals(Color.BLUE)) {
                                        pall.setKiirus_x(0);
                                        pall.setKiirus_y(0);
                                        pallvees = true;
                                        break;
                                    }
                                    if (Math.abs(pall.getKiirus_y()) + Math.abs(pall.getKiirus_x()) < 0.06 && !pallvees) {
                                        pall.setKiirus_y(0);
                                        pall.setKiirus_x(0);
                                        saabvajutada = true;
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

    public static Scene mapEditorStseen(Stage primaryStage, Scene peamenüü){
        Circle lõppauk = new Circle(0, Color.BLACK);
        Text alguspunkt = new Text("P");
        TextField nimi = new TextField();
        nimi.setTranslateX(450);
        nimi.setTranslateY(610);
        alguspunkt.setFont(new Font(10));
        alguspunkt.setFill(Color.RED);
        Circle pintsel = new Circle(0, Color.GREEN);
        Group juur = new Group();
        Rectangle[][] test = new Rectangle[100][100];
        Button vesi = new Button("vesi");
        Button fill = new Button("fill");
        fill.setTranslateX(100);
        fill.setTranslateY(610);
        fill.setOnMouseClicked(mouseEvent -> {

            if (fill.getText().equals("fill")){
                fill.setText("fill off");
                pintsel.setRadius(10);
            }
            else {
                fill.setText("fill");
                pintsel.setRadius(0);
            }
        });
        vesi.setTranslateX(155);
        vesi.setTranslateY(610);
        vesi.setOnMouseClicked(mouseEvent -> {
            pintsel.setFill(Color.BLUE);
            if (fill.getText().equals("fill")) pintsel.setRadius(0);
            else pintsel.setRadius(10);
        });
        Button liiv = new Button("liiv");
        liiv.setTranslateX(195);
        liiv.setTranslateY(610);
        liiv.setOnMouseClicked(mouseEvent -> {
            pintsel.setFill(Color.YELLOW);
            if (fill.getText().equals("fill")) pintsel.setRadius(0);
            else pintsel.setRadius(10);
        });
        Button jää = new Button("jää");
        jää.setTranslateX(270);
        jää.setTranslateY(610);
        jää.setOnMouseClicked(mouseEvent -> {
            pintsel.setFill(Color.LIGHTBLUE);
            if (fill.getText().equals("fill")) pintsel.setRadius(0);
            else pintsel.setRadius(10);
        });
        Button muru = new Button("muru");
        muru.setTranslateX(305);
        muru.setTranslateY(610);
        muru.setOnMouseClicked(mouseEvent -> {
            pintsel.setFill(Color.GREEN);
            if (fill.getText().equals("fill")) pintsel.setRadius(0);
            else pintsel.setRadius(10);
        });
        Button sein = new Button("sein");
        sein.setTranslateX(230);
        sein.setTranslateY(610);
        sein.setOnMouseClicked(mouseEvent -> {
            pintsel.setFill(Color.GRAY);
            if (fill.getText().equals("fill")) pintsel.setRadius(0);
            else pintsel.setRadius(10);
        });

        Button auk = new Button("auk");
        auk.setTranslateX(60);
        auk.setTranslateY(610);
        auk.setOnMouseClicked(mouseEvent -> pintsel.setRadius(1));
        Button algus = new Button("algus");
        algus.setTranslateX(10);
        algus.setTranslateY(610);
        algus.setOnMouseClicked(mouseEvent -> pintsel.setRadius(2));
        Button finish = new Button("salvesta");
        finish.setTranslateX(385);
        finish.setTranslateY(610);
        finish.setOnMouseClicked(mouseEvent -> {
            try {
                if ((alguspunkt.getX()==0&&alguspunkt.getY()==0)||(lõppauk.getCenterX()==0&&lõppauk.getCenterY()==0)||nimi.getText().equals("")) throw new PoolikRadaException("VIGA! Teie rajal puudus algus, lõpp või nimi!");
                else radaFailiks(test, new Pall(alguspunkt.getX(), alguspunkt.getY(), 4, Color.WHITE), lõppauk, nimi.getText(), 0);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                primaryStage.close();
            }
            primaryStage.setScene(peamenüü);
        });
        juur.getChildren().addAll(vesi, liiv, muru, sein, jää);
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test[i].length; j++) {
                test[i][j] = new Rectangle(6, 6, Color.GREEN);
            }
        }
        for (int i = 0; i < test.length; i++) {
            for (int j = 0; j < test[i].length; j++) {
                juur.getChildren().add(test[i][j]);
                juur.getChildren().get(juur.getChildren().size() - 1).setTranslateX(6 * j);
                juur.getChildren().get(juur.getChildren().size() - 1).setTranslateY(6 * i);
            }
        }
        juur.getChildren().addAll(auk, algus, finish, nimi, fill, lõppauk, alguspunkt);
        Scene radaloomissteen = new Scene(juur, 600, 650, Color.SNOW);
        radaloomissteen.setOnMouseDragged(event -> {
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
        });
        radaloomissteen.setOnMouseClicked(event -> {
            int x = (int) Math.round(event.getSceneX() / 6);
            int y = (int) Math.round(event.getSceneY() / 6);
            if (x<100&&y<100){
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
            else if (pintsel.getRadius() == 10 && !(pintsel.getFill().equals(test[y][x].getFill()))) {
                fill(test, pintsel.getFill(),test[y][x].getFill(), y, x);
            }
        }});
        return radaloomissteen;
    }
    public static void fill(Rectangle[][] väljak, Paint värv, Paint alusvärv, int i, int j){
        Stack<int[]> blocks = new Stack<int[]>();
        int[] temp = {i, j};
        blocks.push(temp);
        while (!(blocks.isEmpty())){
            temp = blocks.pop();
            fill_abi(väljak, värv, alusvärv,temp[0], temp[1], blocks);
        }
    }
    public static void fill_abi(Rectangle[][] väljak, Paint värv, Paint alusvärv, int i, int j, Stack<int[]> blocks){
            if (i > 0) {
                if (väljak[i - 1][j].getFill().equals(alusvärv)) {
                    väljak[i - 1][j].setFill(värv);
                    int[] temp= {i-1,j};
                    blocks.push(temp);
                }
            }
            if (j > 0) {
                if (väljak[i][j-1].getFill().equals(alusvärv)) {
                    väljak[i][j-1].setFill(värv);
                    int[] temp= {i,j-1};
                    blocks.push(temp);
                }
            }
            if (i < 99) {
                if (väljak[i + 1][j].getFill().equals(alusvärv)){
                    väljak[i + 1][j].setFill(värv);
                    int[] temp= {i+1,j};
                    blocks.push(temp);
                }
            }
            if (j < 99) {
                if (väljak[i][j + 1].getFill().equals(alusvärv)){
                    väljak[i][j+1].setFill(värv);
                    int[] temp= {i,j+1};
                    blocks.push(temp);
                }
            }
        }

    public static Scene menüüStseen(Stage primaryStage){
        Group juur = new Group();
        Button play = new Button("Mängi");
        play.setTranslateX(350);
        play.setTranslateY(225);
        ImagePattern tagataust = new ImagePattern(new Image("file:Main_Menu2.png")); // sellega saab muuta tagatausta
        Scene menüüSteen = new Scene(juur, 600, 600, tagataust);
        primaryStage.setResizable(false);
        play.setOnMouseClicked(mouseEvent -> {
            juur.getChildren().clear();
            for (int i = 0; i < listFilesForFolder().size(); i++) {
                ImagePattern tagataust2 = new ImagePattern(new Image("file:Raja_valik.png")); // sellega saab muuta tagatausta
                menüüSteen.setFill(tagataust2);
                Button nupp = new Button(listFilesForFolder().get(i));
                nupp.setLayoutX(350);
                nupp.setLayoutY(120 + i * 40);
                juur.getChildren().add(nupp);
                nupp.setOnMouseClicked(event -> {
                    try {
                        primaryStage.setScene(mänguStseen(primaryStage, nupp.getText()));
                    } catch (Exception e) {
                        primaryStage.close();
                    }
                });
            }
        });
        juur.getChildren().add(play);
        Button ehita = new Button("Ehita");
        ehita.setTranslateX(450);
        ehita.setTranslateY(225);
        ehita.setOnMouseClicked(mouseEvent -> {
            try {
                primaryStage.setScene(mapEditorStseen(primaryStage, primaryStage.getScene()));
            } catch (Exception e) {
                primaryStage.close();
            }
        });
        juur.getChildren().add(ehita);
        return menüüSteen;
    }

    @Override
    public void start(Stage primaryStage){
        primaryStage.setTitle("MiniGolf");
        primaryStage.setScene(menüüStseen(primaryStage));
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);

    }
}
