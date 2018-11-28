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

        @Override
        public void start(Stage primaryStage) {
            Group root = new Group();
            Canvas canvas = new Canvas(1000, 1000);
            GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            List<Point2D> points = new ArrayList<>();
            Point p1 = new Point(100,200);
            Point p2 = new Point(600,400);
            Point p3 = new Point(800,600);
            Point p4 = new Point(400,100);
            Point p5 = new Point(300,800);
            Point p6 = new Point(900,500);
            Point p7 = new Point(450,800);
            List<Point> pointsE = new ArrayList<>();
            pointsE.add(p1);
            pointsE.add(p2);
            pointsE.add(p3);
            pointsE.add(p4);
            pointsE.add(p5);
            pointsE.add(p6);
            pointsE.add(p7);

            EnveloppeConvexe env = new EnveloppeConvexe(pointsE);
            points.add(new Point2D(p1.x, p1.y));
            points.add(new Point2D(p2.x, p2.y));
            points.add(new Point2D(p3.x, p3.y));
            points.add(new Point2D(p4.x, p4.y));
            points.add(new Point2D(p5.x, p5.y));
            points.add(new Point2D(p6.x, p6.y));
            points.add(new Point2D(p7.x, p7.y));
            for (Point2D point:  points) {
                graphicsContext.strokeOval(point.getX(),point.getY(),5.0,5.0);
            }

            for (int i = 0; i < env.enveloppeConvexe.size()-1 ; ++i) {
                graphicsContext.strokeLine(env.enveloppeConvexe.get(i).x,env.enveloppeConvexe.get(i).y, env.enveloppeConvexe.get(i+1).x, env.enveloppeConvexe.get(i+1).y);
            }
            graphicsContext.strokeLine(env.enveloppeConvexe.get(0).x,env.enveloppeConvexe.get(0).y, env.enveloppeConvexe.get(env.enveloppeConvexe.size()-1).x, env.enveloppeConvexe.get(env.enveloppeConvexe.size()-1).y);
            root.getChildren().add(canvas);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        }
    }