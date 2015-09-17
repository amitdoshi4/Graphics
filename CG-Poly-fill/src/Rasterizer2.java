//
//  Rasterizer.java
//  
//
//  Created by Joe Geigel on 1/21/10.
//  Copyright 2010 __MyCompanyName__. All rights reserved.
//

/**
 * 
 * This is a class that performas rasterization algorithms
 *
 */

import java.util.*;

public class Rasterizer2 {
    
	 int BLACK=0xff000000, WHITE=0xffffffff;
	    int edges; // number of edges
	    int activeEdges; // number of  active edges

	    // the polygon
	    int[] x; // x coordinates
	    int[] y; // y coordinates
	    int n;  // number of coordinates

	    // edge table
	    double[] ex;    // x coordinates
	    int[] ey1;  // upper y coordinates
	    int[] ey2;  // lower y coordinates
	    double[] eslope;   // inverse slopes (1/m)

	    // sorted edge table (indexes into edge table) (currently not used)
	    int[] sedge;

	    // active edge table (indexes into edge table)
	    int[] aedge; 
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
    public void setPolygon(int[] x, int[] y, int n) {
        this.x = x;
        this.y = y;
        this.n = n;
    }
    
    /** Generates the edge table. */
    void buildEdgeTable(int[] x, int[] y, int n) {
        int length, iplus1, x1, x2, y1, y2;
        edges = 0;
        for (int i=0; i<n; i++) {
            iplus1 = i==n-1?0:i+1;
            y1 = y[i];  y2 = y[iplus1];
            x1 = x[i];  x2 = x[iplus1];
            if (y1==y2)
                continue; //ignore horizontal lines
            if (y1>y2) { // swap ends
                int tmp = y1;
                y1=y2; y2=tmp;
                tmp=x1;
                x1=x2; x2=tmp;
            }
            double slope = (double)(x2-x1)/(y2-y1);
            ex[edges] = x1 ; 
            ey1[edges] = y1;
            ey2[edges] = y2;
            eslope[edges] = slope;
            edges++;   
        }
        for (int i=0; i<edges; i++)
            sedge[i] = i;
        activeEdges = 0;
        //quickSort(sedge);
    }
    Rasterizer (int n)
    {
        n_scanlines = n;
    }
    
    /**
     * Draw a filled polygon in the simpleCanvas C.
     *
     * The polygon has n distinct vertices. The 
     * coordinates of the vertices making up the polygon are stored in the 
     * x and y arrays.  The ith vertex will have coordinate  (x[i], y[i])
     *
     * You are to add the implementation here using only calls
     * to C.setPixel()
     */
    void allocateArrays(int n) {
        if (ex==null || n>ex.length) {
            ex = new double[n];
            ey1 = new int[n];
            ey2 = new int[n];
            sedge = new int[n];
            aedge = new int[n];
            eslope = new double[n];
        }
    }
    void removeInactiveEdges(int y) {
        int i = 0;
        while (i<activeEdges) {
            int index = aedge[i];
            if (y<ey1[index] || y>=ey2[index]) {
                for (int j=i; j<activeEdges-1; j++)
                    aedge[j] = aedge[j+1];
                activeEdges--; 
            } else
                i++;         
        }
    }
    void activateEdges(int y) {
        for (int i=0; i<edges; i++) {
            int edge =sedge[i];
            if (y==ey1[edge]) {
                int index = 0;
                while (index<activeEdges && ex[edge]>ex[aedge[index]])
                    index++;
                for (int j=activeEdges-1; j>=index; j--) 
                    aedge[j+1] = aedge[j];
                aedge[index] = edge;
                activeEdges++;
            }
        }
    }

    void sortActiveEdges() {
        int min, tmp;
        for (int i=0; i<activeEdges; i++) {
            min = i;
            for (int j=i; j<activeEdges; j++)
                if (ex[aedge[j]] <ex[aedge[min]]) min = j;
            tmp=aedge[min];
            aedge[min] = aedge[i]; 
            aedge[i]=tmp;
        }
    }
    public void drawPolygon(int n, int x[], int z[], simpleCanvas C)
    {

        allocateArrays(n);
        buildEdgeTable(x, z, n);
        //printEdges();
        int x1, x2, offset, index, width;
        width=n;

        for (int y=0; y<n; y++) {
            removeInactiveEdges(y);
            activateEdges(y);
            offset = y*width;
            for (int i=0; i<activeEdges; i++) {
                x1 = (int)(ex[aedge[i]]+0.5);
                if (x1<0) x1=0;
                //if (x1>width) x1 = width;
                x2 = (int)(ex[aedge[i+1]]+0.5); 
                if (x2<0) x2=0; 
                //if (x2>width) x2 = width;
                //IJ.log(y+" "+x1+"  "+x2);
                C.setPixel(2,n);
                System.out.println(x1+"   " + x2);
            }           
            updateXCoordinates();
        }
        
    
    }
    void updateXCoordinates() {
        int index;
        double x1=-Double.MAX_VALUE, x2;
        boolean sorted = true;
        for (int i=0; i<activeEdges; i++) {
            index = aedge[i];
            x2 = ex[index] + eslope[index];
            ex[index] = x2;
            if (x2<x1) sorted = false;
            x1 = x2;
        }
        if (!sorted) 
            sortActiveEdges();
    }
    
}
