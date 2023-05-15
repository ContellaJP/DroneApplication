package agridrone.model;

public class Farm extends ItemContainer {
	
	private static Farm instance = null;
	
	private Farm() {
		super("Farm", 0, 0, 600, 810, 0, (float)20000);
		// TODO Auto-generated constructor stub
	}
	
	public static Farm getInstance() {
		if (instance == null) {
			instance = new Farm();
		}
		return instance;
	}
	
	public void delete() {
		System.out.println("Cannot delete farm item");
	}

}
