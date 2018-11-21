public class Point implements Comparable<Point>{

    public int x;
    public int y;

    public Point(int x, int y) {
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
