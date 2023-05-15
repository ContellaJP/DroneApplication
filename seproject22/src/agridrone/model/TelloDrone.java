package agridrone.model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import Drone_adapter.Flight_Capabilities;
import agridrone.view.DashboardController;



public class TelloDrone implements Flight_Capabilities {
	

	DroneController Controller;
	
	private static Drone drone;
	
	private DashboardController DC;
	
	
	
	private final int maxGoto = 500, minGoto = 1, minheight = 20; //records maximum amount the drone can travel before making another call. also records the minimum
	
	private int maxHeight; //maximum height for all items in the tree
	
	
	
	
	//constructor to assign the values for the drone adaptee and the DashboardCOntroller
	public TelloDrone(Drone drone, DashboardController DC) throws SocketException, UnknownHostException, FileNotFoundException {
		this.drone = drone;
		this.DC = DC;
		Controller = new DroneController(9000, 8889, "192.168.10.1");
		
		
	}
	
	
	//activates drone SDK
	public void activateSDK() throws IOException {
		this.Controller.sendCommand("command");
	}

	//ends the commands for drone
	public void end() throws IOException, InterruptedException {
		this.Controller.closeControlSocket();
		System.out.println("Exit Program...");
	}
	
	//gets the maximum height of all the current items
	public int getMaxHeight() {
		maxHeight = DC.getMaxHeight()/25 * 30;
		return maxHeight;
	}

	//launches drone to start and recieve commands
	@Override
	public void launch() throws IOException {
		// TODO Auto-generated method stub
		this.Controller.sendCommand("takeoff");
		
	}

	//sends the command for the drone to land
	@Override
	public void land() throws IOException {
		// TODO Auto-generated method stub
		this.Controller.sendCommand("land");
		
	}
	
	//takes the height of the item from Drone and converts it into centimeters
	@Override
	public int itemheight(int height) throws IOException, NumberFormatException {
		// TODO Auto-generated method stub
		height = height/25 * 30;
		return height;
		
	}
	

	//sends the command to increase drone's altitude. Also checks if it goes past the maximum distance
	@Override
	public void IncreaseAltitude(int up) throws IOException {
		// TODO Auto-generated method stub
		if (up > maxGoto) {
			this.Controller.sendCommand("up " + maxGoto);
			IncreaseAltitude(up - maxGoto);
		}
		else {
		this.Controller.sendCommand("up " + up);
		}
	}

	
	//Sends the command to decrease drone's altitude. Also checks if it goes past the maximum distance
	@Override
	public void DecreaseAltitude(int down) throws IOException {
		// TODO Auto-generated method stub
		int height = itemheight(drone.ItemH);
		
		if (down > maxGoto) {
			this.Controller.sendCommand("down " + maxGoto);
			DecreaseAltitude(down - maxGoto);
		}
		else if (down <= minheight){
			
			this.Controller.sendCommand("down " + minheight);
		}
		else {
			this.Controller.sendCommand("down " + down);
		}
		
		
	}

	//sends the command for the drone to fly a certain distance
	@Override
	public void flyforward(int forward) throws IOException {
		// TODO Auto-generated method stub
		forward = forward/25 * 30;
		if (forward <= minGoto) {
		this.Controller.sendCommand("forward " + minGoto);
		}
		else if (forward > maxGoto){
			this.Controller.sendCommand("forward " + maxGoto);
			flyforward(Math.abs(forward - maxGoto));
		}
		else {
			this.Controller.sendCommand("forward " + forward);
		}
		
	}


	//sends command for drone to turn clockwise x number of degrees
	@Override
	public void TurnCW(double degrees) throws IOException {
		// TODO Auto-generated method stub
		degrees = Math.abs(degrees);
		Controller.sendCommand("cw " + (int) degrees);
		
		
		
	}

	// sends command for drone to turn counter-clockwise x number of degrees
	@Override
	public void TurnCCW(double degrees) throws IOException {
		// TODO Auto-generated method stub
		degrees = Math.abs(degrees);
		Controller.sendCommand("ccw " + (int) degrees);
		
	}



	//makes the drone hover in place for a certain number of seconds
	@Override
	public void hoverInPlace(int seconds) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		TimeUnit.MILLISECONDS.sleep(seconds);
		return;
		
		
	}
	
	//makes the drone stop in place
	public void stopInPlace() throws IOException {
		this.Controller.sendCommand("stop");
	}


	//method to make the drone go to the item selected in the simulator
	public void Gotoitem() throws IOException, InterruptedException {
		
		
		activateSDK();
		launch();
		IncreaseAltitude(getMaxHeight());
		
		
		if (drone.Checkrotation == 0) {
			TurnCCW(drone.degreeCCW);
		}
		else if (drone.Checkrotation == 1) {
			TurnCW(drone.degreeCW);
		}
		
		flyforward(drone.forwardDist);
		DecreaseAltitude(getMaxHeight() - itemheight(drone.ItemH));
		TurnCCW(360);
		
		hoverInPlace(drone.Hovering);
		
		
		TurnCW(180);
		
		flyforward(drone.forwardDist);
		
		if (drone.Checkrotation == 0) {
			TurnCCW(-drone.degreeCCW + 180);
		}
		else if (drone.Checkrotation == 1) {
			TurnCW(-drone.degreeCW + 180);
		}
		
		
		DecreaseAltitude((int) (itemheight(drone.ItemH) * .80));
		
		
		land();
		end();
		
	}
	
	//method that has the drone scan the entire 32x24 area
	public void ScanFarm() throws IOException, InterruptedException {
		
		
		activateSDK();
		launch();
		
		IncreaseAltitude(getMaxHeight());
		for (int i = 0; i < 8; i++) {
			flyforward(drone.forwardDist);
			TurnCW(drone.degreeCW);
			flyforward((int) Math.round(drone.getLength() * .625));
			TurnCW(drone.degreeCW);
			flyforward(drone.forwardDist);
			TurnCCW(drone.degreeCW);
			flyforward((int) Math.round( drone.getLength() * .625));
			TurnCCW(drone.degreeCW);
		}
		
		
		
		TurnCCW(drone.degreeCCW);
		flyforward((int) ((int) drone.forwardDist * 1.3));
		TurnCW(drone.degreeCW);
		DecreaseAltitude((int) (itemheight(drone.ItemH) * .80));
		land();
		end();
		
		
		
	}

	
	
	

}
