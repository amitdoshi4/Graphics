//
//  Rasterizer.java
//  
//
//  Created by Joe Geigel on 1/21/10.
//  Implemented by Grant Kurtz
//

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

public class RasterizerMy{

	int YCOORD = 0;
	int XCOORD = 1;
	int DELTAX = 2;
	int DELTAY = 3;
	int TEMP = 4;
	int n_scanlines;

	//Arraylist to store the active edge table
	ArrayList<Integer[]> activeTable = new ArrayList<Integer[]>();
	
	//Hashmap to store the global edge table
	Map<Integer,ArrayList<Integer[]>> globalTable = new HashMap<Integer,ArrayList<Integer[]>>();

	//constructor
	Rasterizer(int n){
		n_scanlines = n;
	}

	
/* This function builds the edge table of the given array of x and y coordinates.
 * In this the keys of the map represents a scan line.
 * @param n integer
 * @param []x array of int
 * @param []y array of int
 *
 * @return void
 */	
	private void buildGlobalEdgeTable(int n, int[] x, int[] y){

		// initialize each internal array first
		for(int line = 0; line < n_scanlines; line++){
			globalTable.put(line, new ArrayList<Integer[]>());
		}

		for(int i = 0; i < n; i++){
			Integer[] arrVertex = new Integer[5];  //array of vertices having x,y coordinate and dy,dx and constant
			int next = (i + 1) % n;

			// We need the max of y of both vertices, and the x of min y
			
			if(y[i] >= y[next]){
				arrVertex[YCOORD] = y[i];
				arrVertex[XCOORD] = x[next];
			}
			else{
				arrVertex[YCOORD] = y[next];
				arrVertex[XCOORD] = x[i];
			}

			// Store the slope of the edge
			arrVertex[DELTAX] = x[i] - x[next];
			arrVertex[DELTAY] = y[i] - y[next];
			arrVertex[TEMP] = 0;

			//Adding the edge with minimum of two adjacent y coordinate
			globalTable.get(Math.min(y[i], y[next])).add(arrVertex);
		}
	}

	/* This function is used to set pixels between the x coordinates of the active edge table.
	 * @param n integer
	 * @param []x array of int
	 * @param []y array of int
	 *
	 * @return void
	 */	
	
	public void drawPolygon(int n, Float[] floats, Float[] floats2, simpleCanvas C){
		int xInt[] = new int[floats.length];
		int yInt[] = new int[floats2.length];
		for(int i =0;i<floats.length;i++)
			xInt[i] = Math.round(floats[i]);
		for(int i =0;i<floats.length;i++)
			yInt[i] = Math.round(floats2[i]);
		buildGlobalEdgeTable(n, xInt, yInt);
        int line=0;
		while(line<n_scanlines){
			activeTable.addAll(globalTable.get(line));			//build the active edge table
			sortActiveEdgeTable();
			for(int i = 0; i < activeTable.size(); i++){
				if(activeTable.get(i)[YCOORD] == line){   //check if the vertex lie on the scan line
					activeTable.set(i, null);
					continue;
				}
			}
			while(activeTable.contains(null)){
				activeTable.remove(null);
			}
			for(int edge = 0; edge < activeTable.size(); edge++)
			{

				if(edge != activeTable.size() - 1){
					int x1= activeTable.get(edge)[XCOORD];
					int x2 = activeTable.get(edge + 1)[XCOORD];
					while(x1<x2){                        // fill the pixels between the x(that cuts the scan line) coordinates of the same y coordinate
						C.setPixel(x1, line);
						x1++;
					}
				}
				activeTable.get(edge)[TEMP] += activeTable.get(edge)[DELTAX];
				if(activeTable.get(edge)[DELTAY] != 0){
					adjustX(edge);
				}
			}
	line++;
	}
	}
/* This function adjusts the x coordinate on the active edge table based on the slope:- DELTAY/DELTAX
 * @param n edge
 *
 * @return void
 */	

	private void adjustX(int edge) {

		activeTable.get(edge)[XCOORD] += (activeTable.get(edge)[TEMP]
								  / activeTable.get(edge)[DELTAY]);

		
		if(activeTable.get(edge)[TEMP] < 0){
			activeTable.get(edge)[TEMP] = (Math.abs(activeTable.get(edge)[TEMP])
									 % Math.abs(activeTable.get(edge)[DELTAY]))
									* -1;
		}
		else{
			activeTable.get(edge)[TEMP] = Math.abs(activeTable.get(edge)[TEMP])
									% Math.abs(activeTable.get(edge)[DELTAY]);
		}
	
		
	}


/* This function sorts the active edge table based on the y coordinate.
 * It is based on buuble sort.
 * @param void
 *
 * @return void
 */	

	private void sortActiveEdgeTable(){
		for(int i = 0; i < activeTable.size(); i++){
			for(int k = 1; k < (activeTable.size() - i); k++){
				if(activeTable.get(k - 1)[1]
				   > activeTable.get(k)[1]){
					Collections.swap(activeTable, k - 1, k);
				}
			}
		}
	}

}