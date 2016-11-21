package SensingSystem;


import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;
import com.unimelb.swen30006.partc.roads.RoadMarking;

public class Unit {

	private static final float MARKING_WIDTH = 1f;
	private static final float INTERSECTION_LINE_WIDTH = 1.0f;
	
	private Rectangle2D.Double shape;
	
	private ArrayList<Road> roads;
	
	private ArrayList<WorldObject> objects;
	
	private Intersection intersection;
	//Bright areas which have an overlap area with unit
	private ArrayList<Rectangle2D.Double> brights;
	//Light areas which have an overlap area with unit
	private ArrayList<Rectangle2D.Double> lights;

	
	public Unit() {
		
		this.roads = new ArrayList<Road>();
		this.objects = new ArrayList<WorldObject>();
		this.lights = new ArrayList<Rectangle2D.Double>();
		this.brights = new ArrayList<Rectangle2D.Double>();
	}
	

	/**
	 * @return the shape
	 */
	public Rectangle2D.Double getShape() {
		return shape;
	}


	/**
	 * @param shape the shape to set
	 */
	public void setShape(Rectangle2D.Double shape) {
		this.shape = shape;
	}
	
	/**
	 * @return the roads
	 */
	public ArrayList<Road> getRoads() {
		return roads;
	}


	/**
	 * @return the objects
	 */
	public ArrayList<WorldObject> getObjects() {
		return objects;
	}

	/**
	 * @return the brights
	 */
	public ArrayList<Rectangle2D.Double> getBrights() {
		return brights;
	}


	/**
	 * @return the lights
	 */
	public ArrayList<Rectangle2D.Double> getLights() {
		return lights;
	}


	/**
	 * @return the intersection
	 */
	public Intersection getIntersection() {
		return intersection;
	}


	/**
	 * @param intersection the intersection to set
	 */
	public void setIntersection(Intersection intersection) {
		this.intersection = intersection;
	}

	public boolean isMarked(){
		//If unit is on a road
		if (this.roads.size() != 0){
			//check whether the unit is on a certain marking contained by the road
			RoadMarking[] markers=this.roads.get(0).getMarkers();
			boolean horizontal = this.roads.get(0).getStartPos().y == this.roads.get(0).getEndPos().y;
			float horizontalWidth = horizontal ? 2*MARKING_WIDTH : MARKING_WIDTH;
			float verticalWidth = horizontal ? MARKING_WIDTH : 2*MARKING_WIDTH;
			
			
			for (int i = 0;i < markers.length; i ++){
				//generate the shape of markings
				Rectangle2D.Double markerBoundary = new Rectangle2D.Double((markers[i].position.x - MARKING_WIDTH), (float)(markers[i].position.y - MARKING_WIDTH), horizontalWidth, verticalWidth);
				
				if (this.shape.intersects(markerBoundary)){
					return true;
				}
			}		
		}
		
		else{
			return false;
		}
		
		return false;
		
	}
	
	public boolean isOnIntersectionLine(){
		//If unit is on an intersection
		if (this.intersection == null){	
			
			return false;
		}
		else{
			//check whether the unit is on a certain line contained by the intersection
			Rectangle2D.Double bottom= new Rectangle2D.Double(this.intersection.pos.x,this.intersection.pos.y,this.intersection.width,INTERSECTION_LINE_WIDTH);
			Rectangle2D.Double top= new Rectangle2D.Double(this.intersection.pos.x,this.intersection.pos.y+this.intersection.length-INTERSECTION_LINE_WIDTH,this.intersection.width,INTERSECTION_LINE_WIDTH);
			Rectangle2D.Double left= new Rectangle2D.Double(this.intersection.pos.x,this.intersection.pos.y,INTERSECTION_LINE_WIDTH,this.intersection.length);
			Rectangle2D.Double right= new Rectangle2D.Double(this.intersection.pos.x+this.intersection.width-INTERSECTION_LINE_WIDTH,this.intersection.pos.y,INTERSECTION_LINE_WIDTH,this.intersection.length);
			if (this.shape.intersects(bottom)||this.shape.intersects(top)||this.shape.intersects(left)||this.shape.intersects(right)){
				return true;
			}
			else{
				return false;
			}
		}
		
	}
	
	
	
	
	
	
}
			

