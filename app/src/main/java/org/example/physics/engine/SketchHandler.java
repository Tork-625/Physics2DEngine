package org.example.physics.engine;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import org.example.physics.utils.RDP;

public class SketchHandler implements MouseListener, MouseMotionListener{
	
	public boolean canDrawNow = false;
	int xOld = 0;
	int yOld = 0;
	
	int xNew = 0;
	int yNew = 0;
	
	int [] xCoords = new int[2];
	int [] yCoords = new int[2];
	
	public List <int[]> points = new ArrayList<>(); 
	public List <int[]> simplifiedPoints = new ArrayList<>(); 

	
	@Override
	public void mousePressed(MouseEvent e) {
		canDrawNow = true;
		points.clear();
		simplifiedPoints.clear();
		if(canDrawNow == true) {
			points.add(new int[] {e.getX(), e.getY()});
		}
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		if(canDrawNow) {
			points.add(new int[] {e.getX(), e.getY()});
		}
	}
	@Override
	public void mouseReleased(MouseEvent e) {
		canDrawNow = false;
		simplifiedPoints = RDP.simplify(points);
		StrokePolygonator.polygonate(simplifiedPoints);
	}
	

	//////////////////////////////////////////////////////////////////////////////||
	@Override																	//||
	public void mouseEntered(MouseEvent e) {									//||
	}																			//||
	@Override																	//||
	public void mouseExited(MouseEvent e) {										//||
	}																			//||
	@Override																	//||
	public void mouseClicked(MouseEvent e) {									//||
	}																			//||
	@Override																	//||
	public void mouseMoved(MouseEvent e) {										//||
	}																			//||
	//////////////////////////////////////////////////////////////////////////////||


}

