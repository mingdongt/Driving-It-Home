package SensingSystem;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.math.Vector2;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.infrastructure.Light;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.unimelb.swen30006.partc.core.objects.WorldObject;
import com.unimelb.swen30006.partc.roads.Intersection;
import com.unimelb.swen30006.partc.roads.Road;


public class Grid {
	
	private static final float MIDDAY = 15f;
	private World world;
	private Car car;
	private Unit[][] Units;
	//Shape of the grid
	private Rectangle2D.Double shape;
	//Velocities of current existing world objects
	private HashMap<WorldObject,Vector2> velocities;		
	//Previous positions of the world objects existing within the unit in the last delta
	private HashMap<WorldObject,Point2D.Double> prePositions;	
	
	//All the areas lighted by the street lights in the world
	private ArrayList<Rectangle2D.Double> worldLightAreas;
	private ArrayList<Rectangle2D.Double> worldBrightAreas;
	
	public Grid(World world,Car car) {
		
		this.world=world;
		this.prePositions = new HashMap<WorldObject,Point2D.Double>();
		this.car=car;
		this.worldBrightAreas = new ArrayList<Rectangle2D.Double>();
		this.worldLightAreas = new ArrayList<Rectangle2D.Double>();
		
		processLight();		
	}
	
	public Unit[][] getUnits(){
		
		return Units;
	}
	
	public Vector2 getVelocity(WorldObject w){
		
		return this.velocities.get(w);
		
	}
	
	
	public void processUnits(Point2D.Double pos,int visibility,float delta){
		
		setShape(pos,visibility);
		
		//Collect things which have an overlap with the grid
		WorldObject[] containedObjects = this.world.objectsAtPoint(pos);
		Road []containedRoad = this.world.roadsAroundPoint(pos);
		Intersection []containedIntersection = intersectionsAroundPoint();
		Rectangle2D.Double[] containedBrightAreas =  brightAreasAroundPoint();
		Rectangle2D.Double[] containedLightAreas = lightAreasAroundPoint();
		
		//Update velocities for contained world objects
		findVelocities(containedObjects,delta);
		
	
		this.Units=new Unit[visibility][visibility];
		
		//Update the status of every unit 
		for (int i = 0;i <visibility; i ++){
			
			for (int j = 0;j <visibility; j++){
				
				this.Units[i][j]=new Unit();
				//
				this.Units[i][j].setShape(new Rectangle2D.Double(pos.getX() - visibility + j*2, pos.getY() + visibility - i*2, 2, 2)); 
				findAllWithinUnit(this.Units[i][j].getShape(),Units[i][j],containedObjects,containedRoad,containedIntersection,containedBrightAreas,containedLightAreas);
				
			}
		}
	}
	
	private void findVelocities(WorldObject[] containedObjects,float delta){
		//Temp used to contain the positions of currently existing world objects
		HashMap<WorldObject,Point2D.Double> temp = new HashMap<WorldObject,Point2D.Double>();
		//New velocities map
		this.velocities = new HashMap<WorldObject,Vector2>();		
		
		for (int i=0;i<containedObjects.length;i++){
			Vector2 objectVelocity = new Vector2();
			//New object, absolute velocity=0 
			if (this.prePositions.get(containedObjects[i])==null){		
				objectVelocity.x = 0;
				objectVelocity.y = 0;
			}			
			else{
				//Get previous position and current position
				Point2D.Double pre = this.prePositions.get(containedObjects[i]);	
				Point2D.Double current = containedObjects[i].getPosition();		
				//Calculate the absolute velocity
				objectVelocity.x = (float) ((float)current.x-pre.x)/delta;
				objectVelocity.y = (float) ((float)current.y-pre.y)/delta;

			}
			//Find relative velocity and record it as well as position 
			objectVelocity.sub(this.car.getVelocity());
			this.velocities.put(containedObjects[i],objectVelocity);
			temp.put(containedObjects[i], containedObjects[i].getPosition());
		}
		//Update the previous positions
		this.prePositions=temp;		
	}
	
	//Add things which have an overlap with the certain unit to the unit 
	private void findAllWithinUnit(Rectangle2D.Double unitBoundary, Unit unit, WorldObject[] containedObjects, Road []containedRoad, Intersection[] containedIntersection,Rectangle2D.Double[] containedBrightAreas,Rectangle2D.Double[] containedLightAreas){
		
		
		for (int m = 0; m < containedObjects.length; m++){
			if (containedObjects[m].boundary.intersects(unitBoundary) || unitBoundary.intersects(containedObjects[m].boundary)){
				unit.getObjects().add(containedObjects[m]);
			}
		}
		
		for (int m = 0; m < containedRoad.length; m++){
			if (containedRoad[m].overlaps(unitBoundary)){
				unit.getRoads().add(containedRoad[m]);
				
			}
		}
		
		for (int m = 0; m < containedIntersection.length; m++){
			
			Rectangle2D.Double intersectionBoundary = new Rectangle2D.Double((float)containedIntersection[m].pos.x, (float)containedIntersection[m].pos.y, containedIntersection[m].width, containedIntersection[m].length);
			if (intersectionBoundary.intersects(unitBoundary)){
				unit.setIntersection(containedIntersection[m]);
			}
		}
		
		if(((this.world.worldTime) < (MIDDAY/2)) || ((this.world.worldTime) > (MIDDAY + MIDDAY/2))){
			for (int m = 0; m < containedBrightAreas.length; m++){
				
				if (containedBrightAreas[m].intersects(unitBoundary)){
					unit.getBrights().add(containedBrightAreas[m]);
				}
			}
			
			for (int m = 0; m < containedLightAreas.length; m++){
			
				if (containedLightAreas[m].intersects(unitBoundary)){
					unit.getLights().add(containedLightAreas[m]);
				}
			}
		}
	}
	
	//Set the shape for grid
	private void setShape(Point2D.Double pos,int visibility){
		float xmin = (float) pos.x - visibility;
		float xmax = (float) pos.x + visibility;
		float ymin = (float) pos.y - visibility;
		float ymax = (float) pos.y + visibility;
		Rectangle2D.Double rect = new Rectangle2D.Double();
		rect.setFrameFromDiagonal(xmin, ymin, xmax, ymax);
		this.shape=rect;
	}
	
	//Find intersections which have an overlap with the grid
	private Intersection[] intersectionsAroundPoint(){

		ArrayList<Intersection> containedIntersections = new ArrayList<Intersection>();
		for(Intersection I: this.world.getIntersections()){
			Rectangle2D.Double IntersectionBoundary = new Rectangle2D.Double((float)I.pos.x, (float)I.pos.y, I.width, I.length);
			if(IntersectionBoundary.intersects(this.shape)){
				containedIntersections.add(I);
			}
		}
		return containedIntersections.toArray(new Intersection[containedIntersections.size()]);
	}
	
	//Find light areas lighted by street lights which have an overlap with the grid
	private Rectangle2D.Double[] brightAreasAroundPoint(){

		ArrayList<Rectangle2D.Double> containedBrightAreas = new ArrayList<Rectangle2D.Double>();
		
		for(Rectangle2D.Double R: this.worldBrightAreas){
			if(R.intersects(this.shape)){
				containedBrightAreas.add(R);
			}
		}
		return containedBrightAreas.toArray(new Rectangle2D.Double[containedBrightAreas.size()]);
	}
	
	//Find bright areas lighted by street lights which have an overlap with the grid
	private Rectangle2D.Double[] lightAreasAroundPoint(){

		ArrayList<Rectangle2D.Double> containedLightAreas = new ArrayList<Rectangle2D.Double>();
		
		for(Rectangle2D.Double R: this.worldLightAreas){
			if(R.intersects(this.shape)){
				containedLightAreas.add(R);
			}
		}
		return containedLightAreas.toArray(new Rectangle2D.Double[containedLightAreas.size()]);
	}
	
	
	
	//Generate the shapes of areas lighted by street lights 
	private void processLight(){
		
		WorldObject[] containedObjects = this.world.getObjects();
		
		for (int m = 0; m < containedObjects.length; m++){
			if (containedObjects[m] instanceof Light){
				Rectangle2D.Double BrightArea = new Rectangle2D.Double(containedObjects[m].getPosition().x-5,containedObjects[m].getPosition().y-5,10,10);
				Rectangle2D.Double LightArea = new Rectangle2D.Double(containedObjects[m].getPosition().x-25,containedObjects[m].getPosition().y-25,50,50);
				this.worldBrightAreas.add(BrightArea);
				this.worldLightAreas.add(LightArea);
			}
		}
		
	}

	public World getWorld() {
		return world;
	}
	

}
