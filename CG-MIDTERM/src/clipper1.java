
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map.Entry;

/**
 * This Class is a polygon clipper that implements the Sutherland-Hodgman Polygon Clipper algorithm.
 * It makes use of the subroutines like input, output, intersect.
 * 
 * Version 1.0
 * 
 * @author Amit Doshi
 *
 * Date: 03/10/2015
 */

public class clipper1 {


    public final String LEFT = "LEFT";
    public final String RIGHT = "RIGHT";
    public final String TOP = "TOP";
    public final String BOTTOM = "BOTTOM";
    // Class level arrays for in and out coordinates
    public float xIn[] = new float[4];
    public float yIn[] = new float[4];
    public float xOut[] = new float[4];
    public float yOut[] = new float[4];
    public int inVer;
    
    public int numberOfVertices;
/*
 * This is an inner class that defines the clipping boundary
 */
    public class ClipBoundary {
        // Vertices of the boundary (line segment).
   
        Coordinates c1, c2;

        /* Type of boundary. */
        //LEFT,RIGHT,TOP,BOTTOM
        String boundary;

        /* Constructor. */
        public ClipBoundary(Coordinates lower, Coordinates upper, String boundary) {
            this.c1 = lower;
            this.c2 = upper;
            this.boundary = boundary;
        }

        
        /* Finds the intersection point between the boundary line segment (c1 & c2)
           and the given vertices (p & s).
         * @param p Coordinates
         * @param s Coordinates
         *
         * @return Coordinates
         */
        public Coordinates intersect(Coordinates p, Coordinates s) {

            double d = (c2.x-c1.x) * (s.y-p.y) - (c2.y-c1.y) * (s.x-p.x);
            if (d == 0) return null;

            double vx = ((s.x-p.x)*(c2.x*c1.y-c2.y*c1.x)-(c2.x-c1.x)*(s.x*p.y-s.y*p.x))/d;
            double vy = ((s.y-p.y)*(c2.x*c1.y-c2.y*c1.x)-(c2.y-c1.y)*(s.x*p.y-s.y*p.x))/d;

            return new Coordinates(vx, vy);

        }

        /* Check if the point is inside the clipping boundary by comparing 
           the appropriate vertex axis and boundary axis.
         * @param v  Coordinates
         * 
         * @return boolean
        */
        public boolean inside(Coordinates v) {
        	if(boundary.equalsIgnoreCase(LEFT))
        		return v.x > c1.x; 
        	else if(boundary.equalsIgnoreCase(RIGHT))
        		return v.x < c1.x;
        	else if(boundary.equalsIgnoreCase(TOP))
        		return v.y < c1.y;
        	else if(boundary.equalsIgnoreCase(BOTTOM))
        		return v.y > c1.y;
        		else
        			return true;
        }
    }


    /* Coordinates Data Type: Contains a set of Cartesian points. */
    public class Coordinates {
        double x, y;

        public Coordinates(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }
    
    /**
     * 
     * Clip the polygon with vertex count in and vertices inx/iny
     * against the rectangular clipping region specified by lower-left corner
     * (x0,y0) and upper-right corner (x1,y1). The resulting vertices are
     * placed in outx/outy.  
     * 
     * This method should return the vertex count of polygon
     * resulting from the clipping.
     *
     * @param in the number of vertices in the polygon to be clipped
     * @param inx  x coords of vertices of polygon to be clipped.
     * @param int  y coords of vertices of polygon to be clipped.
     * @param outx  x coords of vertices of polygon resulting after clipping.
     * @param outy  y coords of vertices of polygon resulting after clipping.
     * @param x0  x coord of lower left of clipping rectangle.
     * @param y0  y coord of lower left of clipping rectangle.
     * @param x1  x coord of upper right of clipping rectangle.
     * @param y1  y coord of upper right of clipping rectangle.
     *
     * @return number of vertices in the polygon resulting after clipping
     * 
     */
    public int clipPolygon(int in, float inx[], float iny[], float outx[], 
                    float outy[], float x0, float y0, float x1, float y1)
    {

        /* Defines an initial result object and continues to run the Sutherland-
           Hodgman until no clipping remain. */
        ArrayList<ClipBoundary> bounds = new ArrayList<ClipBoundary>();
        bounds.add(new ClipBoundary(new Coordinates(x0, y0), new Coordinates(x0, y1), LEFT));
        bounds.add(new ClipBoundary(new Coordinates(x1, y0), new Coordinates(x1, y1), RIGHT));
        bounds.add(new ClipBoundary(new Coordinates(x0, y1), new Coordinates(x1, y1), TOP));
        bounds.add(new ClipBoundary(new Coordinates(x0, y0), new Coordinates(x1, y0), BOTTOM));
        Iterator<ClipBoundary> it = bounds.iterator();
        xIn = inx;
        yIn = iny;
        xOut = outx;
        yOut = outy;
        inVer = in;
        int out = 0;
        out = polygonClipper(it.next());

        /* Check that there remaining vertices to clip. If out is zero, then
           the polgyon lies completely outside the clipping region.*/
        while (it.hasNext()){ //&& out > 0) {
           // r = SHPC(r.inx, r.iny, r.outx, r.outy, r.in, r.out, it.next());
        	out = polygonClipper( it.next());
        }
     inVer = 0;
        return out;
    }
    /* Routine to add Coordinates v to the out axis arrays at index out.
     * @param v Coordinates
     * @param outx[] float
     * @param outy[] float
     * @param out int
     * 
     * @return void
     */
    public void output(Coordinates v, float outx[], float outy[], int out)
    {
        xOut[out] = (float) v.x;
        yOut[out] = (float) v.y;
    }
    
    /* This is the method that implements Sutherland-Hodgman Polygon Clipper algorithm and makes the use of the 
     * helper subroutines like inside, intersect, output to return the number of vertices.
     * 
     * @param b ClipBoundary
     * 
     * @return out int number of vertices
    */
    public int polygonClipper(ClipBoundary b) {

        // Initialize the out array size and the first vertex to the last vertex
        // in the input array.
       int out = 0;
    	//numberOfVertices =0;
        Coordinates p = new Coordinates(xIn[inVer-1], yIn[inVer-1]);

        // Compare each pair of vertices with respect to the given boundary.
        for (int j = 0; j < inVer; j++) {

            Coordinates s = new Coordinates(xIn[j], yIn[j]);

            if (b.inside(s)) {
                if (b.inside(p)) {
                    // Both vertices inside region
                    output(s, xOut, yOut, out);
                    out++;
                } else {
                    // Previous outside region;
                    // Successor inside region.
                    Coordinates i = b.intersect(p, s);
                    output(i, xOut, yOut, out);
                    out++;
                    output(s, xOut, yOut, out);
                    out++;
                }
            } else {
                if (b.inside(p)) {
                    // Previous inside region;
                    // Successor outside region.
                    Coordinates i = b.intersect(p, s);
                    output(i, xOut, yOut, out);
                    out++;
                }
                // Else: Both outside region. No output.
            }
            p = s;
        }
        // Return copies of new incoming arrays with the outgoing lengths.

        xIn = xOut;
        yIn = yOut;
        inVer = out;
        return out;

    }

}