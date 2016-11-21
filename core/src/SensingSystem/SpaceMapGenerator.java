package SensingSystem;

public class SpaceMapGenerator implements MapGenerator<Boolean> {

	/**
	 * 
	 */
	public Boolean[][] getMap(Grid grid) {
		
		Unit[][] Units = grid.getUnits();
		int length = Units.length;
		Boolean[][] Map=new Boolean[length][length];
		
		for (int i=0;i<length;i++){
			for (int j=0;j<length;j++){
				if (Units[i][j].getObjects().isEmpty()){     
					Map[i][j]=false;
					System.out.print("  ");				//test
				}
				else{
					Map[i][j]=true;
					System.out.print("¡ö ");			//test
				}
			}
			System.out.println("");					//test
		}
		System.out.println("finish");				//test
		return Map;
	}


}
