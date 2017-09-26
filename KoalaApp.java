import javafx.application.Application;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.geometry.Insets;


public class KoalaApp extends Application {

    public static final int NUM_LAYERS = 8;
    public static final int DIM = 1024;

    private static String filename;

    private Pane root;

    public static void main(String[] args) {
        try {
            filename = args[0];
            launch(args);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Enter a filename next time!");
        }
    }

    public KoalaDot[][] imageToKoala() throws Exception {
        File file = new File(filename);
        BufferedImage im = ImageIO.read(file);
        int num = (int) Math.pow(2, NUM_LAYERS);
        int wu = im.getWidth() / num / 2;
        int hu = im.getHeight() / num / 2;
        int pu = DIM / num / 2;

        KoalaDot[][] koalas = new KoalaDot[num][num];
        for (int i = 0; i < num; i++) {
            for (int j = 0; j < num; j++) {
                int ix = wu * (1 + 2 * i);
                int iy = hu * (1 + 2 * j);
                int px = pu * (1 + 2 * i);
                int py = pu * (1 +  2* j);
                int clr = im.getRGB(ix, iy);
                int r = (clr & 0x00ff0000) >> 16;
                int g = (clr & 0x0000ff00) >> 8;
                int b = clr & 0x000000ff;
                Color c = new Color(r / 256.0, g / 256.0, b / 256.0, 1);
                koalas[i][j] = new KoalaDot(c, root, px, py, pu);
            }
        }
        return koalas;
    }

    public KoalaDot[][] mergeKoala(KoalaDot[][] koalas) {
        int size = koalas.length / 2;
        if (size < 1) {
            return koalas;
        }
        KoalaDot[][] newKoalas = new KoalaDot[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newKoalas[i][j] = new KoalaDot(new KoalaDot[] {
                        koalas[2*i][2*j], koalas[2*i+1][2*j],
                        koalas[2*i][2*j+1], koalas[2*i+1][2*j+1]
                    });
            }
        }
        return newKoalas;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        root = new Pane();
        root.setBackground(new Background(new BackgroundFill(Color.color(.1, .1, .1), CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(root, DIM, DIM);
        KoalaDot[][] koalas = imageToKoala();
        for (int i = 0; i < NUM_LAYERS; i++) {
            //for (KoalaDot[] k: koalas) root.getChildren().addAll(k);
            koalas = mergeKoala(koalas);
        }
        KoalaDot masterKoala = koalas[0][0];
        masterKoala.setOpacity(1);
        masterKoala.makeReady();
        masterKoala.enable();
        root.getChildren().add(masterKoala);
        primaryStage.setTitle("Circles!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }


}
