public class Point implements Comparable<Point>{

    public double x;
    public double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public int compareTo(Point p) {
        if (this.x < p.x) {
            return -1;
        } else if(this.x> p.x) {
            return 1;
        }
        return 0;
    }
}
