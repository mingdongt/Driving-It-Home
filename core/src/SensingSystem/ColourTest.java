package SensingSystem;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;



    @SuppressWarnings("serial")
	public class ColourTest extends JPanel {

    	public List<Point> units;
        public static int maxrow, maxcol;
        public JFrame window;
        
        private com.badlogic.gdx.graphics.Color[][] colMap = null;
        
        public ColourTest() {

            units = new ArrayList<Point>();
            window = new JFrame();
            window.setSize(10, 10);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.add(this);
            window.setVisible(true);
        }

        @Override

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int counter = 0;
            for (Point unit : units) {

                int cellX = 10 + (unit.x * 10);
                int cellY = 10 + (unit.y * 10);
				float r, g1, b;
				com.badlogic.gdx.graphics.Color c = colMap[(cellX/10)-1][(cellY/10)-1];
				r = (float) (c.r * c.a + c.r * (255 - c.a));
				g1 = (float) (c.g * c.a + c.g * (255 - c.a));
				b = (float) (c.b * c.a + c.b * (255 - c.a));
				System.out.printf("[%4d%4d%4d] ", (int)r, (int)g1, (int)b );
				if(++counter % colMap.length == 0)
					System.out.println();
                g.setColor(new Color((int)r, (int)g1, (int)b));
                g.fillRect(cellX, cellY, 10, 10);
            }
            System.out.println("\n");
            g.setColor(Color.BLACK);
            g.drawRect(10, 10, maxcol, maxrow);

            for (int i = 10; i <= maxcol; i += 10) {
                g.drawLine(i, 10, i, maxrow+10);
            }

            for (int i = 10; i <= maxrow; i += 10) {
                g.drawLine(10, i, maxcol+10, i);
            }
        }
        
        public void updateColMap(final com.badlogic.gdx.graphics.Color[][]  colMap)
        {  	
        	int prevlen = -1;
        	if (this.colMap != null) 
        		prevlen = this.colMap.length;

        	this.colMap = colMap;
        	if (prevlen != colMap.length) {
        		units = new ArrayList<Point>();
	            maxrow = maxcol = colMap.length*10;
	            window.setSize(maxrow+40, maxcol+60);
	            for (int i=0; i<(maxrow/10);i++) {
	            	for (int j=0; j<(maxcol/10);j++){
	            		units.add(new Point(i, j));
	            	}
	            }
        	}
            paintComponent(this.getGraphics());
        }

    
}