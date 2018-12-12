import javafx.application.Application;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Interface extends Application {

        public static void main(String[] args) {
            launch(args);
        }

        public List<Point> randomPoints() {
            int nbSommets = ThreadLocalRandom.current().nextInt(50, 60);
            List<Point> points = new ArrayList<>(nbSommets);
            for(int i = 0 ; i < nbSommets ; ++i) {
                int x = ThreadLocalRandom.current().nextInt(100, 900);
                int y = ThreadLocalRandom.current().nextInt(100, 900);
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

            List<Point> pointsE = randomPoints();
            EnveloppeConvexe env = new EnveloppeConvexe(pointsE);
            for (Point point: pointsE) {
                points.add(new Point2D(point.x,point.y));
            }
            double radius = 2.5;
            for (Point2D point:  points) {
                graphicsContext.strokeOval(point.getX(),point.getY(),radius*2,radius*2);
            }

            int size = env.enveloppeConvexe.size();
           /* for (int i = 0 ; i < env.enveloppeConvexe.size() ; ++i) {
                graphicsContext.strokeLine(
                        env.enveloppeConvexe.get(i).x + radius,
                        env.enveloppeConvexe.get(i).y + radius,
                        env.enveloppeConvexe.get((i+1) % size).x + radius,
                        env.enveloppeConvexe.get((i+1) % size).y + radius);
            }*/

            TriangulationSimple triangulationSimple = new TriangulationSimple(env);

            /*for (Triangle triangle : triangulationSimple.triangles) {
                graphicsContext.strokeLine(triangle.p1.x + radius, triangle.p1.y + radius, triangle.p2.x + radius, triangle.p2.y + radius);
                graphicsContext.strokeLine(triangle.p2.x + radius, triangle.p2.y + radius, triangle.p3.x + radius, triangle.p3.y + radius);
                graphicsContext.strokeLine(triangle.p1.x + radius, triangle.p1.y + radius, triangle.p3.x + radius, triangle.p3.y + radius);
            }*/

            TriangulationDelaunay triangulationDelaunay = new TriangulationDelaunay(env);

            for (Triangle triangle : triangulationDelaunay.triangles) {
                graphicsContext.strokeLine(triangle.p1.x + radius, triangle.p1.y + radius, triangle.p2.x + radius, triangle.p2.y + radius);
                graphicsContext.strokeLine(triangle.p2.x + radius, triangle.p2.y + radius, triangle.p3.x + radius, triangle.p3.y + radius);
                graphicsContext.strokeLine(triangle.p1.x + radius, triangle.p1.y + radius, triangle.p3.x + radius, triangle.p3.y + radius);
            }

            //graphicsContext.strokeRect(triangulationDelaunay.rectangle.x, triangulationDelaunay.rectangle.y, triangulationDelaunay.rectangle.width, triangulationDelaunay.rectangle.height);
            /*for (Triangle triangle : triangulationDelaunay.triangles) {
                graphicsContext.strokeOval(
                        triangle.centre.x - triangle.rayonCercleCirconscrit,
                        triangle.centre.y - triangle.rayonCercleCirconscrit,
                        triangle.rayonCercleCirconscrit*2,
                        triangle.rayonCercleCirconscrit*2);
            }*/


            //TriangulationSimple triangulationSimple = new TriangulationSimple(env);

            root.getChildren().add(canvas);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();

        }
    }