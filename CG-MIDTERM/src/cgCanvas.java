/**
 * This class implements the 2d-pipeline
 * Version 1.0
 * 
 * @author Amit Doshi
 *
 * Date: 03/22/2015
*/
import Jama.*;
import java.util.*;


public class cgCanvas extends simpleCanvas {
    protected ArrayList<Poly> polyVertx;  //list of polyid
    protected int id;   
    protected Matrix curTransform; //matrix
    private Matrix viewport; //viewing matrix
    public float bttm, top, left, right;//clipping parameters
    public int x,y, width, height;//viewPort parameters

    /**
     * Constructor
     */
    cgCanvas (int w, int h)
    {
        super (w, h);
        polyVertx = new ArrayList<Poly>();
        id = 0;
        curTransform = Matrix.identity(3,3);
    }

    /**
		add polgon in arraylist
     *
     * @param x - Array of x coords of the vertices
     * @param y - Array of y coords of the vertices 
     * @param n - Number of verticies
     *
     * @return a id
     */
    public int addPoly (float x[], float y[], int n)
    {
        int curid = id;
        id++;
        Poly p = new Poly(x,y,n);
        polyVertx.add(curid, p);

        return curid;
    }

    /**
     * drawPoly - Draw the polygon with the given id.  Should draw the
     *        polygon after applying the current transformation on the
     *        vertices of the polygon.
     *
     * @param polyID - the ID of the polygon to be drawn.
     */
    public void drawPoly (int polyID)
    {
        Poly p = polyVertx.get(polyID);

        //Apply transform
        p = p.transform(curTransform);
        Float x[], y[];
        x = p.getXs().toArray(new Float[p.getXs().size()]);
        y= p.getYs().toArray(new Float[p.getYs().size()]);
        //Clip
        float fx[], fy[],nx[],ny[];
    
        fx = new float[x.length];
        fy = new float[y.length];

        for(int i=0; i< x.length;i++){
            fx[i] = x[i];
            fy[i] = y[i];
        }
        nx = new float[100];
        ny = new float[100];
        clipper c = new clipper();
        int len = c.clipPolygon(x.length,fx,fy,nx,ny,left,bttm,right,top);

        Poly another = new Poly(nx,ny,len);
        another = another.transform(viewport);

        Rasterizer r = new Rasterizer(this.getHeight());


        ArrayList<Float> xVertx = another.getXs(), yVertx= another.getYs();
        r.drawPolygon(len,xVertx.toArray(new Float[xVertx.size()]),yVertx.toArray(new Float[yVertx.size()]),this);
    }

    /**
     * clearTransform - sets the current transformation to be the identity
     *
     */
    public void clearTransform()
    {
    	  double[][]vals = {{1,0,0}, {0,1,0}, {0,0,1}};
    	  curTransform = new Matrix(vals);
    }

    /**
     * translate - Add a translation to the current transformation by
     *             pre-multiplying the appropriate translation matrix to
     *             the current transformation matrix.
     *
     * @param x - Amount of translation in x.
     * @param y - Amount of translation in y.
     *
     */
    public void translate (float x, float y)
    {
        double temp [][] = {{1,0,x},{0,1,y},{0,0,1}};
        Matrix m = Matrix.constructWithCopy(temp);
        curTransform = m.times(curTransform);

    }

    /**
     * rotate - Add a rotation to the current transformation by
     *          pre-multiplying the appropriate rotation matrix to the
     *          current transformation matrix.
     *
     * @param degrees - Amount of rotation in degrees.
     *
     */
    public void rotate (float degrees)
    {   double rads= Math.toRadians(degrees);
        double temp [][] = {{Math.cos(rads),-1.0 * Math.sin(rads),0},{Math.sin(rads),Math.cos(rads),0},{0,0,1}};
        Matrix m = Matrix.constructWithCopy(temp);
        curTransform = m.times(curTransform);
    }

    /**
     * scale - Add a scale to the current transformation by pre-multiplying
     *         the appropriate scaling matrix to the current transformation
     *         matrix.
     *
     * @param x - Amount of scaling in x.
     * @param y - Amount of scaling in y.
     *
     */
    public void scale (float x, float y)
    {
        double temp [][] = {{x,0,0},{0,y,0},{0,0,1}};
        Matrix m = Matrix.constructWithCopy(temp);
        curTransform = m.times(curTransform);
    }

    /**
     * setClipWindow - defines the clip window
     *
     * @param bottom - y coord of bottom edge of clip window (in world coords)
     * @param top - y coord of top edge of clip window (in world coords)
     * @param left - x coord of left edge of clip window (in world coords)
     * @param right - x coord of right edge of clip window (in world coords)
     *
     */
    public void setClipWindow (float bottom, float top, float left, float right)
    {
        this.bttm = bottom;
        this.left = left;
        this.top = top;
        this.right = right;
    }


    /**
     * setViewport - defines the viewport
     *
     * @param x - x coord of lower left of view window (in screen coords)
     * @param y - y coord of lower left of view window (in screen coords)
     * @param width - width of view window (in world coords)
     * @param height - width of view window (in world coords)
     *
     */
    public void setViewport (int x, int y, int width, int height)
    {
    	this.x = x;
    	this.y = y;
    	this. width = width;
    	this.height = height;
       double  sx = (width)/(right-left),
                sy = (height)/(top - bttm),
                tx = (right*x - left*(x+width))/(right-left),
                ty = (top*y - bttm*(y+height))/(top- bttm);
        double [][] viewCol= {{sx,0,tx},{0,sy,ty},{0,0,1}};

        viewport = Matrix.constructWithCopy(viewCol);
    }

    public class Poly {
        protected ArrayList<Float> xVertx;  //X vertices
        protected ArrayList<Float> yVertx;  //Y vertices
        int n;

        public int getN() {
    		return n;
    	}

    	public void setN(int n) {
    		this.n = n;
    	}

    	/**
         * Construct a new empty polygon
         */
        public Poly(){
            xVertx = new ArrayList<Float>();
            yVertx = new ArrayList<Float>();
            this.n=n;
        }

        /**
         * Construct a polygon with the given vertices
         * @param x The x vertices of the polygon
         * @param y The y vertices of the polygon
         * @param n The number of vertices in the polygon
         */
        public Poly(float x[], float y[], int n){
            xVertx = new ArrayList<Float>();
            yVertx = new ArrayList<Float>();
            this.n=n;

            for(int i=0; i< n; i++){
                xVertx.add( (float) x[i]);
                yVertx.add( (float) y[i]);
            }
        }

        /**
         * Returns the list of x vertices
         * @return The list of X vertices
         */
        public ArrayList<Float> getXs() {
            return xVertx;
        }

        /**
         * Set the list of x veritces
         * @param xVertx The x vertices
         */
        public void setXs(ArrayList<Float> xVertx) {
            this.xVertx = xVertx;
        }

        /**
         * Returns the list of Y vertices
         * @return The list of Y vertices
         */
        public ArrayList<Float> getYs() {
            return yVertx;
        }

        /**
         * Sets the list of y vertices
         * @param yVertx The list of y vertices
         */
        public void setYs(ArrayList<Float> yVertx) {
            this.yVertx = yVertx;
        }

        /**
         * Applies the transformation matrix m to all vertices in the polygon
         * @param m The transformation matrix
         * @return A new polygon with all the vertices transformed by m
         */
        public Poly transform(Matrix m){
            Poly result = null;
            double col [][]= {{1},{1},{1}}; //Column vector
            Matrix colM;
            ArrayList<Float> newxVertx, newyVertx;
            newxVertx = new ArrayList<Float>();
            newyVertx = new ArrayList<Float>();
            for(int i=0; i< xVertx.size(); i++){
                col[0][0] = xVertx.get(i);
                col[1][0] = yVertx.get(i);
                colM = Matrix.constructWithCopy(col);
                colM = m.times(colM);
                newxVertx.add(i, (float) colM.get(0, 0));
                newyVertx.add(i, (float) colM.get(1, 0));
            }
            result = new Poly();
            result.setXs(newxVertx);
            result.setYs(newyVertx);
            return result;
        }
    }
}