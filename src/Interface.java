import javafx.application.Application;

import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Interface extends Application {


        public static void main(String[] args) {
            launch(args);
        }

        public List<Point> randomPoints() {
            int nbSommets = ThreadLocalRandom.current().nextInt(15, 20);
            List<Point> points = new ArrayList<>(nbSommets);
            for(int i = 0 ; i < nbSommets ; ++i) {
                int x = ThreadLocalRandom.current().nextInt(100, 800);
                int y = ThreadLocalRandom.current().nextInt(100, 800);
                points.add(new Point(x,y));
            }
            return points;
        }

        @Override
        public void start(Stage primaryStage) {
            Group root = new Group();
            Canvas canvas = new Canvas(1000, 1000);
            GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            List<Point2D> points = new ArrayList<>();
            /*Point p1 = new Point(300,300);
            Point p2 = new Point(500,600);
            Point p3 = new Point(200,900);
            Point p4 = new Point(900,400);
            Point p5 = new Point(950,800);
            Point p6 = new Point(600,800);
            Point p7 = new Point(100,100);
            List<Point> pointsE = new ArrayList<>();
            pointsE.add(p1);
            pointsE.add(p2);
            pointsE.add(p3);
            pointsE.add(p4);
            pointsE.add(p5);
            pointsE.add(p6);
            pointsE.add(p7);*/

            List<Point> pointsE = randomPoints();
            EnveloppeConvexe env = new EnveloppeConvexe(pointsE);
            for (Point point : env.enveloppeConvexe) {
                System.out.println(point.x);
                System.out.println(point.y);
                System.out.println('\n');
            }
            for (Point point: pointsE) {
                points.add(new Point2D(point.x,point.y));
            }
            /*points.add(new Point2D(p1.x, p1.y));
            points.add(new Point2D(p2.x, p2.y));
            points.add(new Point2D(p3.x, p3.y));
            points.add(new Point2D(p4.x, p4.y));
            points.add(new Point2D(p5.x, p5.y));
            points.add(new Point2D(p6.x, p6.y));
            points.add(new Point2D(p7.x, p7.y));*/
            double radius = 2.5;
            for (Point2D point:  points) {
                graphicsContext.strokeOval(point.getX(),point.getY(),radius*2,radius*2);
            }

            for (int i = 0; i < env.enveloppeConvexe.size()-1 ; ++i) {
                graphicsContext.strokeLine(
                        env.enveloppeConvexe.get(i).x + radius,
                        env.enveloppeConvexe.get(i).y + radius,
                        env.enveloppeConvexe.get(i+1).x + radius,
                        env.enveloppeConvexe.get(i+1).y + radius);
            }
            graphicsContext.strokeLine(
                    env.enveloppeConvexe.get(0).x + radius,
                    env.enveloppeConvexe.get(0).y + radius,
                    env.enveloppeConvexe.get(env.enveloppeConvexe.size()-1).x + radius,
                    env.enveloppeConvexe.get(env.enveloppeConvexe.size()-1).y + radius);
            root.getChildren().add(canvas);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }