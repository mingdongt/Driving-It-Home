package SensingSystem;

import com.badlogic.gdx.math.Vector2;

public class VelocityMapGenerator implements MapGenerator<Vector2> {
	
	
	public Vector2[][] getMap(Grid grid) {
		
		Unit[][] Units = grid.getUnits();
		int length = Units.length;
		
		Vector2[][] Map=new Vector2[length][length];
		
		for (int i=0;i<length;i++){
			for (int j=0;j<length;j++){
				//If there is no world object within the unit, v = 0
				if (Units[i][j].getObjects().isEmpty()){
					Map[i][j]=new Vector2(0,0);
					System.out.print("      ");				//test
				}
				else{				
					Map[i][j]=grid.getVelocity(Units[i][j].getObjects().get(0));
					System.out.printf("(%.1f,%.1f) ",Map[i][j].x,Map[i][j].y);		//test
				}
			}
			System.out.println("");		//test
		}
		System.out.println("finish");	//test
		return Map;
	}

}
