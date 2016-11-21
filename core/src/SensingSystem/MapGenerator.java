package SensingSystem;

public interface  MapGenerator<T> {
	//interface of map generators
	public abstract T[][] getMap(Grid grid);

}
