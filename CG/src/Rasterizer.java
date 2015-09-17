/**
 * The class has the implementation of the mid point line drawing algorithm 
 * Version 1.0
 * 
 * @author Amit Doshi
 *
 * Date: 02/18/2015
 * 
 */
//  
//
//

/**
 * 
 * A simple class for performing rasterization algorithms.
 *
 */

import java.util.*;

public class Rasterizer {
	public int x1,x2,y1,y2;
    
    /**
     * number of scanlines
     */
    int n_scanlines;
    
    /**
     * Constructor
     *
     * @param n number of scanlines
     *
     */
    Rasterizer (int n)
    {
        n_scanlines = n;
    }
    
    /**
     * Draw a line from (x0,y0) to (x1, y1) on the simpleCanvas C.
     *
     * Implementation should be using the Midpoint Method
     *
     * You are to add the implementation here using only calls
     * to C.setPxiel()
     *
     * @param x1 x coord of first endpoint
     * @param y1 y coord of first endpoint
     * @param x2 x coord of second endpoint
     * @param y2 y coord of second endpoint
     * @param C  The canvas on which to apply the draw command.
     * 
     */
  /* This is the implementation of the Bresenham's line mid point algorithm
    The input are the x,y start and end coordinates.
   	Based on the value of x1,x2 and y1,y2 we calculate the line direction
   	We calculate the dy and dx and based on that we go to the x or y direction.
   	We calculate the mid point of the 2 pixels and the midpoint determines which side of the line m is on, and selects the appropriate pixel 	

   */
    public void drawLine (int x1, int y1, int x2, int y2, simpleCanvas C){
        int delta = 0;            //For calculating delta
 
        int dy = Math.abs(y2 - y1); // calculate dy
        int dx = Math.abs(x2 - x1); // calculate  dx
 
        int dy2 = dy;
        int dx2 = dx;
 
        int xi = x1 < x2 ? 1 : -1; // line direction
        int yi = y1 < y2 ? 1 : -1;
 
        if (dy <= dx) {             // if distance of x coordinate is more
            while(x1!=x2) {
                C.setPixel( x1, y1);  // illuminating the pixel
                if (x1 == x2)
                    break;             //exit from the loop if the second end point is reached
                x1 =x1+ xi;
                delta =delta+ dy2;
                if (delta > dx) {                    
                    delta =delta- dx2;
                    y1 =y1+ yi;              //selecting the pixel on y axis based on yi
                }
            }
        } else {                   // if distance of y coordinate is more
            while(y1!=y2) {
            	C.setPixel( x1, y1);    // illuminating the pixel
                if (y1 == y2)
                    break;              //exit from the loop if the second end point is reached
                y1 =y1+ yi;
                delta = delta+ dx2;
                if (delta > dy) {                    
                    delta = delta- dy2;
                    x1 =x1+ xi;        //selecting the pixel on x axis based on xi
                }
            }
        }
    }
}
