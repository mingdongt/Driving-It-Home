package SensingSystem;
import com.badlogic.gdx.graphics.Color;
import com.unimelb.swen30006.partc.core.infrastructure.Light;
public class ColorMapGenerator implements MapGenerator<Color> {

	private final static Color OFF_COLOUR = Color.LIGHT_GRAY;
	
	private static final Color INTERSECTION_COLOUR = Color.DARK_GRAY;
	private static final Color INTERSECTION_LINE = Color.LIGHT_GRAY;
	private static final Color ROAD_COLOUR = Color.DARK_GRAY;
	private static final Color MARKING_COLOUR = Color.LIGHT_GRAY;
	private Color LIGHT_COLOUR;
	private static final float LIGHT_BRITNESS = 0.4f;
	public ColorMapGenerator(){
		
	}
	
	public Color[][] getMap(Grid grid) {
		
		Unit[][] units = grid.getUnits();
		int length = units.length;
		
		Color[][] Map=new Color[length][length];
		
		for (int i=0;i<length;i++){
			for (int j=0;j<length;j++){
				
				//Put the color in rendering order 
				Map[i][j] = grid.getWorld().getEnvironmentColour();
				if (!units[i][j].getRoads().isEmpty()){
					Map[i][j] = ROAD_COLOUR;
				}
				if (units[i][j].isMarked()){
					Map[i][j] = MARKING_COLOUR;	
				}
				if (units[i][j].getIntersection() != null){
					Map[i][j] = INTERSECTION_COLOUR;
				}
				if (units[i][j].isOnIntersectionLine()){
					Map[i][j] = INTERSECTION_LINE;
				}
				if (!units[i][j].getObjects().isEmpty()){
					if (units[i][j].getObjects().get(0) instanceof Light){
						Map[i][j] = OFF_COLOUR;
						LIGHT_COLOUR=units[i][j].getObjects().get(0).getColour();
						
					}
					else{
						Map[i][j] = units[i][j].getObjects().get(0).getColour();
					}
				}
				if (!units[i][j].getBrights().isEmpty()){
					Map[i][j] = LIGHT_COLOUR;
				}
				//Finally, if there are light areas which have an overlap area with the unit
				if (!units[i][j].getLights().isEmpty()){
					if (units[i][j].getLights().size() != 1){
						Map[i][j]=new Color(LIGHT_COLOUR.r*LIGHT_BRITNESS+ Map[i][j].r*(1-LIGHT_BRITNESS),LIGHT_COLOUR.g*LIGHT_BRITNESS+ Map[i][j].g*(1-LIGHT_BRITNESS),LIGHT_COLOUR.b*LIGHT_BRITNESS+ Map[i][j].b*(1-LIGHT_BRITNESS),1);
						Map[i][j]=new Color(LIGHT_COLOUR.r*LIGHT_BRITNESS+ Map[i][j].r*(1-LIGHT_BRITNESS),LIGHT_COLOUR.g*LIGHT_BRITNESS+ Map[i][j].g*(1-LIGHT_BRITNESS),LIGHT_COLOUR.b*LIGHT_BRITNESS+ Map[i][j].b*(1-LIGHT_BRITNESS),1);

					}
					
					else{
						Map[i][j]=new Color(LIGHT_COLOUR.r*LIGHT_BRITNESS+ Map[i][j].r*(1-LIGHT_BRITNESS),LIGHT_COLOUR.g*LIGHT_BRITNESS+ Map[i][j].g*(1-LIGHT_BRITNESS),LIGHT_COLOUR.b*LIGHT_BRITNESS+ Map[i][j].b*(1-LIGHT_BRITNESS),1);

					}
				}
				
			}
		}
		return Map;
	}

}
