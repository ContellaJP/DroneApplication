package Drone_adapter;

import java.io.IOException;

public interface Flight_Capabilities {
	
	
	//methods initialized to allow for the connection between the adaptee Drone and the conrete adapter class TelloDrone
	
	public void launch() throws IOException;
	
	public void land() throws IOException;
	
	public void IncreaseAltitude(int up) throws IOException;
	
	public void DecreaseAltitude(int down) throws IOException;
	
	public void flyforward(int forward) throws IOException;
	
	
	
	public void TurnCW(double degrees) throws IOException;
	
	public void TurnCCW(double degrees) throws IOException;
	

	
	public int itemheight(int height) throws IOException, NumberFormatException;
	
	public void hoverInPlace(int seconds) throws InterruptedException, IOException;
	
	
	
	
	

}
