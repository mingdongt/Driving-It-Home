package SensingSystem;

import java.awt.geom.Point2D;
import java.util.HashMap;

import com.unimelb.swen30006.partc.ai.interfaces.ISensing;
import com.unimelb.swen30006.partc.core.World;
import com.unimelb.swen30006.partc.core.objects.Car;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class SensingSystem implements ISensing{

	private Grid grid;
	private HashMap<String,MapGenerator<?>> mapGenerators;
	private static final String SPACE_MAP = "SPACEMAP";
	private static final String VELOCITY_MAP = "VELOCITYMAP";
	private static final String COLOR_MAP = "COLORMAP";
	
	public SensingSystem(World world,Car car) {
		
		mapGenerators = new  HashMap<String,MapGenerator<?>>();
		
		//Create the map generators
		SpaceMapGenerator spaceMap = new SpaceMapGenerator();
		mapGenerators.put(SPACE_MAP, spaceMap);
		
		VelocityMapGenerator velocityMap = new VelocityMapGenerator();
		mapGenerators.put(VELOCITY_MAP, velocityMap);
		
		ColorMapGenerator colorMap = new ColorMapGenerator();
		mapGenerators.put(COLOR_MAP, colorMap);
		
		this.grid= new Grid(world,car);
		
	}
	
	public boolean update(Point2D.Double pos, float delta, int visibility){
	
		this.grid.processUnits(pos,visibility,delta);
		return true;
	}
	
	
	public boolean[][] getSpaceMap(){
		
		Boolean[][] spaceMap = (Boolean [][]) mapGenerators.get(SPACE_MAP).getMap(grid);
		
		boolean[][] spaceMapPrimitive = new boolean[spaceMap[0].length][spaceMap[0].length];
		for(int i=0;i<spaceMap[0].length;i++) {
			
			for(int j=0;j<spaceMap[0].length;j++) {
				spaceMapPrimitive[i][j] = spaceMap[i][j];
			}
		}
		
		return spaceMapPrimitive;
	}
	

	@Override
	public Vector2[][] getVelocityMap() {
		
		return (Vector2 [][]) mapGenerators.get(VELOCITY_MAP).getMap(grid);
	}

	@Override
	public Color[][] getColourMap() {
		
		return (Color [][]) mapGenerators.get(COLOR_MAP).getMap(grid);
	}
	
	

}
