/**
 * A Class representing an Edge
 *
 * Created by Kristen Mills on 2/20/14.
 */
public class Edge implements Comparable<Edge>{

    int ymax;
    int x;
    int dx;
    int dy;
    int sum;

    /**
     * Creates an Edge
     *
     * @param x0 the first x coordinate
     * @param y0 the first y coordinate
     * @param x1 the second x coordinate
     * @param y1 the second y coordinate
     * @return the scanline of the edge
     */
    public int createEdge(int x0, int y0, int x1, int y1){
        int scanline = y0 > y1 ? y1 : y0;
        ymax = y0 > y1 ? y0 : y1;
        x = y0 < y1 ? x0 : x1;
        dx = x1 - x0;
        dy = y1 - y0;
        sum = 0;
        return scanline;
    }

    /**
     * Getter for y max
     *
     * @return ymax
     */
    public int getYmax() {
        return ymax;
    }

    /**
     * Getter for x
     *
     * @return the value x is currently set to
     */
    public int getX() {
        return x;
    }

    /**
     * Getter for Dy
     *
     * @return the change in y
     */
    public int getDy() {
        return dy;
    }

    /**
     * Adjusts the sum and x values according to the algorithm
     */
    public void adjust(){
        sum += Math.abs(dx);
        while(Math.abs(dy) < sum){
            if(dy/(float)dx < 0){
                x -= 1;
            }else {
                x += 1;
            }
            sum -= Math.abs(dy);
        }
    }

    /**
     * Compares to edges
     * @param e the other edge
     * @return 0 1 or -1 based on the two edge values
     */
    @Override
    public int compareTo(Edge e) {
        if(e.x == x){
           return new Float(dx/(float)dy).compareTo(e.dx/(float)e.dy);
        }else{
            return new Integer(x).compareTo(e.x);
        }
    }
}