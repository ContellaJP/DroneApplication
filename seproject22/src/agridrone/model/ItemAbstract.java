package agridrone.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class ItemAbstract {
	private String name;
	private int locationX;
	private int locationY;
	private int width;
	private int length;
	private int height;
	private double price;
	private ItemContainer parentContainer;
	
	public ItemAbstract(String name, int x, int y, int w, int l, int h, double price) {
		this.name = name;
		this.locationX = x;
		this.locationY = y;
		this.width = w;
		this.length = l;
		this.height = h;
		this.price = price;
		this.parentContainer = null;
	}
	
	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getLocationX() {
		return this.locationX;
	}
	public void setLocationX(int x) {
		this.locationX = x;
	}
	
	public int getLocationY() {
		return this.locationY;
	}
	public void setLocationY(int y) {
		this.locationY = y;
	}
	
	public int getWidth() {
		return this.width;
	}
	public void setWidth(int w) {
		this.width = w;
	}
	
	public int getLength() {
		return this.length;
	}
	public void setLength(int l) {
		this.length = l;
	}
	
	public int getHeight() {
		return this.height;
	}
	public void setHeight(int h) {
		this.height = h;
	}
	
	public double getPrice() {
		return this.price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	public ItemContainer getParentContainer() {
		return this.parentContainer;
	}
	public void setParentContainer(ItemContainer parent) {
		this.parentContainer = parent;
	}
	
	public abstract void delete();
	
	

	
}
