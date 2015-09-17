/**
 * The class is for drawing a line
 * Version 1.0
 * 
 * @author Amit Doshi
 *
 * Date: 02/18/2015
 * 
 */

import java.awt.*;
import java.awt.event.*;

public class lineTest {

    public lineTest () {}

    static public void main(String[] args) {

        simpleCanvas T = new simpleCanvas( 600, 600 );
        Rasterizer R = new Rasterizer( 600 );

        T.setColor (0.0f, 0.0f, 0.0f);
        T.clear();
        T.setColor (1.0f, 1.0f, 1.0f);

// Idea for lettering style from:
// http://patorjk.com/software/taag/#p=display&f=Star%20Wars&t=Type%20Something
//          _______   ______   
//         /  _____| /  __  \
//        |  |  __  |  |  |  | 
//        |  | |_ | |  |  |  | 
//        |  |__| | |  `--'  | 
//         \______|  \______/

        //######## The letter 'G' in green ########
        T.setColor( 0.0f, 1.0f, 0.0f );
        R.drawLine( 80, 340, 220, 340, T );   // Horizontal left to right 
        R.drawLine( 40, 380, 80, 340, T );    // 315 degree slope        
        R.drawLine( 220, 340, 260, 380, T );  // 45 degree slope          
        R.drawLine( 260, 380, 260, 440, T );  // Vertical bottom to top
        R.drawLine( 260, 440, 180, 440, T );  // Horizontal right to left
        R.drawLine( 180, 440, 180, 400, T );
        R.drawLine( 180, 400, 220, 400, T );
        R.drawLine( 220, 400, 200, 380, T );
        R.drawLine( 200, 380, 100, 380, T );
        R.drawLine( 100, 380, 80, 400, T );
        R.drawLine( 80, 400, 80, 500, T );
        R.drawLine( 80, 500, 100, 520, T );
        R.drawLine( 100, 520, 200, 520, T );
        R.drawLine( 200, 520, 220, 500, T );
        R.drawLine( 220, 500, 220, 480, T );
        R.drawLine( 220, 480, 260, 480, T );
        R.drawLine( 260, 480, 260, 520, T );
        R.drawLine( 260, 520, 220, 560, T );  // 135 degree slope
        R.drawLine( 220, 560, 80, 560, T );
        R.drawLine( 80, 560, 40, 520, T );    // 225 degree slope
        R.drawLine( 40, 520, 40, 380, T );    // Vertical top to bottom

        //######## The letter 'O' in red ########
        T.setColor( 1.0f, 0.0f, 0.0f );
        R.drawLine( 450, 320, 520, 340, T );  // 16.6 degree slope
        R.drawLine( 520, 340, 540, 360, T );  // 45 degree slope
        R.drawLine( 540, 360, 560, 450, T );  // 77.47 degree slope
        R.drawLine( 560, 450, 540, 540, T );  // 102.83 degree slope
        R.drawLine( 540, 540, 520, 560, T );  // 135 degree slope
        R.drawLine( 520, 560, 450, 580, T );  // 163.3 degree slope
        R.drawLine( 450, 580, 380, 560, T );  // 196.71 degree slope
        R.drawLine( 380, 560, 360, 540, T );  // 225 degree slope
        R.drawLine( 360, 540, 340, 450, T );  
        R.drawLine( 340, 450, 360, 360, T );
        R.drawLine( 360, 360, 380, 340, T );
        R.drawLine( 380, 340, 450, 320, T );
        R.drawLine( 420, 380, 480, 380, T );
        R.drawLine( 480, 380, 520, 420, T );
        R.drawLine( 520, 420, 520, 480, T );
        R.drawLine( 520, 480, 480, 520, T );
        R.drawLine( 480, 520, 420, 520, T );
        R.drawLine( 420, 520, 380, 480, T );
        R.drawLine( 380, 480, 380, 420, T );
        R.drawLine( 380, 420, 420, 380, T );

        //############# Use blue color (0,0.5,1) to write your initials ############# 
        //letter A in blue
        T.setColor( 0.0f, 0.5f, 1.0f );
        R.drawLine(100, 240, 150, 240, T);//-----  horizontal
        R.drawLine(75, 265, 175, 265, T);//------- horizontal
        R.drawLine(75, 265, 45, 70, T);//  /     -88 degree slope 
        R.drawLine(100, 240, 90, 175, T);//  /   -88 degree slope
        R.drawLine(87, 155, 75, 70, T);//  /     -88 degree slope
        R.drawLine(45, 70, 75, 70, T);//  -      horizontal
        R.drawLine(175, 265, 200, 70, T);//  \   88 degree slope 
        R.drawLine(150, 240, 160, 175, T);//  \88 degree slope 
        R.drawLine(162, 155, 175, 70, T);//  \88 degree slope 
        R.drawLine(200, 70, 175, 70, T);//  - horizontal
        R.drawLine(87, 155, 162, 155, T);//  -- horizontal
        R.drawLine(90, 175, 160, 175, T);//  --  horizontal
        //letter D in blue
        R.drawLine(340, 265, 340, 70, T);//|    vertical
        R.drawLine(340, 265, 390, 265, T);//-  horizontal
        R.drawLine(365, 240, 365, 95, T);//|   vertical
        R.drawLine(365, 240, 390, 240, T);//- horizontal
        R.drawLine(340, 70, 390, 70, T);//-horizontal
        R.drawLine(365, 95, 390, 95, T);//- horizontal
        R.drawLine(480, 150, 480, 190, T);//| vertical
        R.drawLine(390, 240, 480, 190, T);// 30 degree slope
        R.drawLine(390, 95, 480, 150, T);// 30 degree slope
        R.drawLine(505, 138, 505, 202, T);//| vertical
        R.drawLine(390, 265, 505, 202, T);//  30 degree slope
        R.drawLine(390, 70, 505, 138, T);//   30 degree slope


        Frame f = new Frame( "Line Test" );
        f.add("Center", T);
        f.pack();
        f.setResizable (false);
        f.setVisible(true);

        f.addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        } );

    }

}
