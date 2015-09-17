/**
 * This is a class that fills the polygon.
   This is based on scan-line fill algorithm
   In this we take all the vertices and sort them on their Y coordinate and place them in a global edge table.
   Then we use the scan line and put the vertices from global table to the active edge table.
   After that we use the active edge table to draw the span in the polygon
 * Version 1.0
 * 
 * @author Amit Doshi
 *
 * Date: 02/28/2015
   
 */

import java.util.*;

public class Rasterizer {

    /**
     * number of scanlines
     */
    int n_scanlines;

    /**
     * Constructor
     *
     * @param n - number of scanlines
     *
     */
    Rasterizer(int n)
    {
        n_scanlines = n;
    }

	/* This function is used to set pixels between the x coordinates of the active edge table.
	 * @param n integer
	 * @param []x array of int
	 * @param []y array of int
	 *
	 * @return void
	 */	
    public void drawPolygon(int n, Float[] x, Float[] y, simpleCanvas C)
    {
        float ymin=n_scanlines, ymax=0;
        ArrayList<EdgeBucket> buckets = new ArrayList<EdgeBucket>();    //A list to hold the created buckets

        //Create EdgeBuckets for all pairs n, n+1
        for(int i=0; i< n-1; i++){
            if(y[i].intValue() != y[i+1].intValue()){
                buckets.add(createEdgeBucket(x[i].intValue(),y[i].intValue(),x[i+1].intValue(), y[i+1].intValue()));

                //find the yMin and yMax values for this polygon
                if(ymin > y[i].intValue()){
                    ymin = y[i].intValue();
                }
                if(ymax < y[i].intValue()){
                    ymax = y[i].intValue();
                }
            }
        }

        //Create EdgeBucket for the edge connecting the last and first points.
        if(y[n-1].intValue() != y[0].intValue()){
            buckets.add(createEdgeBucket(x[n-1].intValue(),y[n-1].intValue(),x[0].intValue(), y[0].intValue()));
            if(ymin > y[n-1] || ymin == -1){
                ymin = y[n-1];
            }
            if(ymax < y[n-1] || ymax == -1){
                ymax = y[n-1];
            }
        }

        EdgeTable eTable = new EdgeTable(ymax, ymin);

        //Add each EdgeBucket to the EdgeTable
        for(EdgeBucket e : buckets){
            eTable.addEdgeBucket(e);
        }


        EdgeBucket first, last;
        float xStart, xEnd;

        ActiveEdgeList ael = new ActiveEdgeList();  //Initialize an empty ActiveEdgeList

        //Iterate from the lowest to highest scanline
        for(float scanLine=ymin; scanLine <= ymax; scanLine++){

            //Add all edgebuckets from the edgetable that begin at this scanline
            ael.add(eTable.getFirstEB(scanLine));

            //Remove all EdgeBuckets with ymax <= the current scanline
            ael.removePassedEdges(scanLine);

            //Sort the edgebuckets
            ael.sort();

            //Iterate over each pair in the active edge list
            for(int j = 0; j < ael.size()-1; j += 2){
                first = ael.get(j);
                last = ael.get(j+1);

                xStart = first.getxVal();

                //Round up for the exterior to interior crossing
                if(first.getSum() > 0 && first.isNegative() == false){
                    xStart++;
                }

                //The interior to exterior crossing pixel is either
                //1. Excluded if sum == 0 or
                //2. Rounded down if sum != 0
                xEnd = last.getxVal() - 1;

                //Set all pixels from xStart to xEnd
                for (int xPix = (int)xStart; xPix <= xEnd; xPix++){
                    try{
                        C.setPixel(xPix,(int)scanLine);
                    }catch(ArrayIndexOutOfBoundsException e){
                        e.printStackTrace();
                    }
                }

                //Update the sum and x values
                first.updateSumAndX();
                last.updateSumAndX();


            }

        }

    }

    /* This class builds the edge table of the given array of x and y coordinates.
     * In this the keys of the map represents a scan line.
     * @param x1 The x part of Point 1
     * @param y1 The y part of Point 1
     * @param x2 The x part of Point 2
     * @param y2 The y part of Point 2
     * @return The EdgeBucket representing the edge connecting the two points
     * */
    public EdgeBucket createEdgeBucket(float x1, float y1, float x2, float y2){
        float firstscan, ymax, xinit, dx, dy;
        boolean isNegative;

        //Determine the first scanline the edge is encountered,
        //the last scanline the edge is encountered, and
        //the initial x value for this edge.
        if(y1 < y2){
            firstscan = y1;
            ymax = y2;
            xinit = x1;
        }
        else{
            firstscan = y2;
            ymax = y1;
            xinit=x2;
        }

        //Determine dx and dy
        dy = y2-y1;
        dx = x2-x1;

        //Determine whether the edge has a positive or negative inverse slope
        if((dy <0 && dx <=0) || (dy >0 && dx >= 0) ){
            isNegative = false;
        }
        else{
            isNegative = true;
        }

        //Make sure dx and dy are positive
        if(dx <0){
            dx*= -1;
        }

        if(dy <0){
            dy *= -1;
        }

        return new EdgeBucket(firstscan,ymax,xinit,isNegative,dx,dy);

    }

    /**
     * A class representing the ActiveEdgeList for the fillPolygon algorithm
     */
    class ActiveEdgeList{
        protected ArrayList<EdgeBucket> list;   //The list of EdgeBuckets

        /**
         * Creates a new ActiveEdgeList
         */
        public ActiveEdgeList(){
            list = new ArrayList<EdgeBucket>();
        }

        /**
         * Adds the EdgeBucket e and all the EdgeBuckets that follow it
         * to the ActiveEdgeList
         * @param e The first EdgeBucket in a linked list of EdgeBuckets to add
         */
        public void add(EdgeBucket e){
            EdgeBucket cur = e;

            while (cur != null){
                list.add(cur);
                cur = cur.getNext();
            }
        }

        /**
         * Removes all EdgeBuckets with ymax <= y
         * @param y The current scanline
         */
        public void removePassedEdges(float y){
            for(int i = 0; i< list.size(); i++){
                if(list.get(i).getYmax() <= y){
                    list.remove(i);
                    i--;
                }
            }
        }

        /**
         * Sorts the EdgeBuckets in the ActiveEdgeList
         */
        public void sort(){
            Collections.sort(list, new EdgeBucketComparator());
        }

        /**
         * Gets the EdgeBucket at the given index
         * @param index The index of the bucket to be retrieved
         * @return The EdgeBucket at the given index
         */
        public EdgeBucket get(int index){
            return list.get(index);
        }

        /**
         * Gets the size of the ActiveEdgeList
         * @return The size of the ActiveEdgeList
         */
        public int size(){
            return list.size();
        }

    }

    /**
     * Represents an EdgeBucket
     */
    class EdgeBucket {
        protected float firtscan; //The first scan line this edge is encountered
        protected float ymax; //The scanline at which this edge should be removed
        protected float xVal;    //The x value for this EdgeBucket
        protected boolean isNegative;   //Whether or not the slope is negative
        protected float dx;   //The dx component of this edge
        protected float dy;   //The dy component of this edge
        protected float sum;  //The sum component of this edge
        protected EdgeBucket next;  //The next EdgeBucket in the list

        /**
         * Create an EdgeBucket with the given values
         * @param firstscan The first scan line this edge is encountered
         * @param ymax The scanline at which this edge should be removed
         * @param xinit The initial x value for this edge
         * @param isNegative Whether or not the slope is negative
         * @param dx The dx component of this edge
         * @param dy The dy component of this edge
         */
        public EdgeBucket(float firstscan, float ymax, float xinit, boolean isNegative, float dx, float dy){
            this.firtscan = firstscan;
            this.ymax = ymax;
            this.xVal = xinit;
            this.isNegative = isNegative;
            this.dx =dx;
            this.dy = dy;
            this.sum = 0;   //Always starts at 0
        }

        /**
         * Updates the sum and Xval variables by:
         * 1. adding dx to sum
         * 2. incrementing/decrementing xVal when sum > dy
         */
        public void updateSumAndX(){
            sum += dx;
            if(dx != 0 && sum >= dy){
                while (sum > dy){
                    sum -=dy;
                    if(isNegative){
                        xVal--;
                    }else{
                        xVal++;
                    }
                }
            }
        }

        /**
         * Gets the sum component
         * @return The value of sum
         */
        public float getSum() {
            return sum;
        }

        /**
         * Gets the next EdgeBucket in the list
         * @return The next EdgeBucket in the list
         */
        public EdgeBucket getNext() {
            return next;
        }

        /**
         * Sets the next EdgeBucket in the list
         * @param next The next EdgeBucket in the list
         */
        public void setNext(EdgeBucket next) {
            this.next = next;
        }

        /**
         * Gets the first scanline this edge crosses
         * @return The first scanline this edge crosses
         */
        public float getFirtscan() {
            return firtscan;
        }

        /**
         * Gets the maximum y value for this EdgeBucket
         * @return The maximum y value for this EdgeBucket
         */
        public float getYmax() {
            return ymax;
        }

        /**
         * Returns the current x value for this EdgeBucket
         * @return The current x value for this EdgeBucket
         */
        public float getxVal() {
            return xVal;
        }

        /**
         * Gets whether the slope is negative or not
         * @return True if the slope is negative, false if otherwise
         */
        public boolean isNegative() {
            return isNegative;
        }

        /**
         * Gets the dx component of this edge
         * @return The dx component of this edge
         */
        public float getDx() {
            return dx;
        }

        /**
         * Gets the dy component of this edge
         * @return The dy component of this edge
         */
        public float getDy() {
            return dy;
        }

    }
    

    /**
     * A class representing the EdgeTable
     */
    class EdgeTable{
        protected EdgeBucket[] table;   //The table itself
        protected int ymin;             //Used for indexing

        /**
         * Create an edgetable for edges between (inclusive) ymin and ymax
         * @param ymax The maximum y value of the EdgeBuckets in the table
         * @param ymin The minimum y value of the EdgeBuckets in the table
         */
        public EdgeTable(int ymax, int ymin){
            table = new EdgeBucket[1+ymax-ymin];
            this.ymin = ymin;
        }

        public EdgeTable(float ymax, float ymin) {
            table = new EdgeBucket[1+(int)ymax-(int)ymin];
            this.ymin = (int)ymin;
        }

        /**
         * Adds an EdgeBucket to the EdgeTable
         * @param e The EdgeBucket to be added
         */
        public void addEdgeBucket(EdgeBucket e){
            int tIndex = (int)e.getFirtscan()- ymin;
            if(table[tIndex] == null){
                table[tIndex] = e;
            }
            else{
                e.setNext(table[tIndex]);
                table[tIndex] = e;
            }

        }

        /**
         * Gets the first EdgeBucket entry at the given index
         *
         * @param y The index of the EdgeTable (normally the current y value)
         * @return The first EdgeBucket at the given index
         */
        public EdgeBucket getFirstEB(float y){
            int tIndex = (int)y - ymin;
            return table[tIndex];
        }
    }


    /**
     * A comparator for sorting EdgeBuckets
     */
    class EdgeBucketComparator implements Comparator<EdgeBucket>{

        public int compare(EdgeBucket o1, EdgeBucket o2) {

            //Compare xVals
            if (o1.getxVal() != o2.getxVal()){
                Float x1, x2;
                x1 = o1.getxVal();
                x2 = o2.getxVal();
                return x1.compareTo(x2);
            }

            //Compare based on 1/m
            Float mInv1 = ((float)o1.getDx()/(float)o1.getDy());
            Float mInv2 = ((float)o2.getDx()/(float)o2.getDy());
            return mInv1.compareTo(mInv2);
        }
    }

}