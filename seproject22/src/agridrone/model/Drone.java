package agridrone.model;

import java.io.IOException;

import Drone_adapter.Flight_Capabilities;
import javafx.animation.Animation;
import javafx.animation.Animation.Status;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class Drone extends Item implements Flight_Capabilities {
	
	
	int forwardDist; //records distance drone will go 
	double degreeCW; //records angle for drone turning clockwise
	double degreeCCW; //records angle for drone turning counter-clockwise
	int ItemH; //records height of item
	int Checkrotation; //records whether the drone will turn clockwise or counter clockwise
	int Hovering; //records the amount of time drone will hover in the air
	
	
	//Sequences for animations
	SequentialTransition sequence = new SequentialTransition();
	SequentialTransition sequence2 = new SequentialTransition();
	SequentialTransition sequence3 = new SequentialTransition();
	SequentialTransition sequence4 = new SequentialTransition();
	SequentialTransition sequence5 = new SequentialTransition();
	SequentialTransition sequence6 = new SequentialTransition();
	SequentialTransition sequence7 = new SequentialTransition();
	SequentialTransition sequence8 = new SequentialTransition();
	
	//Transitions to do animations
	TranslateTransition translate = new TranslateTransition();
	TranslateTransition translate2 = new TranslateTransition();
	TranslateTransition translate3 = new TranslateTransition();
	TranslateTransition translate4 = new TranslateTransition();
	TranslateTransition translate5 = new TranslateTransition();
	TranslateTransition translate6 = new TranslateTransition();
	TranslateTransition translate7 = new TranslateTransition();
	TranslateTransition translate8 = new TranslateTransition();
	TranslateTransition translate9 = new TranslateTransition();
	PauseTransition Pauseanim = new PauseTransition();
	
	//To rotate the drone to an angle
	RotateTransition Rotate1 = new RotateTransition();
	RotateTransition Rotate2 = new RotateTransition();
	RotateTransition Rotate3 = new RotateTransition();
	RotateTransition Rotate4 = new RotateTransition();
	
	RotateTransition Rotate5 = new RotateTransition();
	RotateTransition Rotate6 = new RotateTransition();
	RotateTransition Rotate7 = new RotateTransition();
	RotateTransition Rotate8 = new RotateTransition();
	
	
	public Drone(String name, int x, int y, int w, int l, int h, double price, double mv) {
		super(name, x, y, w, l, h, price, mv);
	}

	
	//Operates on the assumption the drone is in a position where it is facing the left side of the farm
	public void gotoItem(ItemAbstract item, ImageView drone, RadioButton scanButton) throws IOException {
		scanButton.setText("Scan Farm");
		if (this.getLocationX() != item.getLocationX() || this.getLocationY() != item.getLocationY()) {
			//goto item
			translate7.setNode(drone);
			translate8.setNode(drone);
			Duration time = Duration.millis(2000);
			translate7.setDuration(time);
			translate8.setDuration(time);
			
			
			double itemx = 0;
			double itemy = 0;
			double rotate = 0;
			double distance;
			
			
			itemheight(item.getHeight());
			itemx = item.getLocationX() - drone.getLayoutX();
			itemy = item.getLocationY() - drone.getLayoutY();
			
			
			rotate = (itemy/itemx);
			distance = Math.sqrt(Math.pow(itemx, 2) + Math.pow(itemy, 2));
			
			flyforward((int) distance);
			if ((item.getLocationY() <= this.getLocationY() && ((item.getLocationX() < this.getLocationX()) || item.getLocationX() > this.getLocationX()))) {
				
				Checkrotation = 0;
				rotate = 0;
				TurnCCW(rotate);
			}
			else if ((item.getLocationY() > this.getLocationY() && item.getLocationX() <= this.getLocationX() )) {
				Checkrotation = 1;
				rotate = Math.toDegrees(Math.asin(1));
				TurnCW(rotate);
			}
			else if(item.getLocationY() >= this.getLocationY() && item.getLocationX() < this.getLocationX() + 85) {
				Checkrotation = 1;
				rotate = Math.toDegrees(Math.asin(1)) - rotate;
				TurnCW(rotate);
			}
			
			else if ((item.getLocationY() < this.getLocationY() && item.getLocationX() == this.getLocationX() )) {
				Checkrotation = 0;
				rotate = -Math.toDegrees(90);
				TurnCCW(rotate);
			}
			else if (item.getLocationY() > this.getLocationY() &&  item.getLocationX() > this.getLocationX()) {
				
				Checkrotation = 1;
				rotate = Math.toDegrees(Math.atan(rotate));
				TurnCW(rotate);
				
			}
			else if ((item.getLocationY() == this.getLocationY() && item.getLocationX() < this.getLocationX() )) {
				Checkrotation = 0;
				rotate = Math.toDegrees(-180);
				TurnCCW(rotate);
			}
		
			
			
			Rotate1 = new RotateTransition(Duration.millis(3000), drone);
			Rotate1.setByAngle(rotate);
			
			
			translate7.setToX(itemx);
			translate7.setToY(itemy);
			
			Rotate2 = new RotateTransition(Duration.millis(3000), drone);
			Rotate2.setByAngle(360);
			
			
			Pauseanim = new PauseTransition(Duration.millis(4000));
			Hovering = 4000;
			
			
			
			Rotate3 = new RotateTransition(Duration.millis(3000), drone);
			Rotate3.setByAngle(180);
			
			
			translate8.setToX(-drone.getLayoutX() + this.getParentContainer().getLocationX() + 5);
			translate8.setToY(-drone.getLayoutY() - (-this.getParentContainer().getLocationY() - 5));
			
			Rotate4 = new RotateTransition(Duration.millis(3000), drone);
			Rotate4.setByAngle(-rotate + 180);
			
			this.setLocationX(this.getParentContainer().getLocationX() + 5);
			this.setLocationY(this.getParentContainer().getLocationY() + 5);
			
			

			sequence.stop();
			sequence = new SequentialTransition(Rotate1, translate7, Rotate2, Pauseanim, Rotate3, translate8, Rotate4);
			sequence.play();
		} else {
			System.out.println("Drone currently at " + item.getName() + ".");
		}
	}
	

	public void scanFarm(double VH, double VW, ImageView drone, RadioButton scanButton) throws IOException {
		//scanFarm
		//If pressed again while running, it cancels the animation for scan farm
//		scanButton.setText("Cancel Scan");
//		if (sequence.getStatus() == Animation.Status.RUNNING) {
//
//			sequence.stop();
//			gotoParent(drone, scanButton);
//			scanButton.setText("Scan Farm");
//		}
//		else {
		translate2.setNode(drone);
		translate3.setNode(drone);
		translate4.setNode(drone);
		translate5.setNode(drone);
		translate6.setNode(drone);
		translate7.setNode(drone);
		Duration time = Duration.millis(2000);
		translate.setDuration(time);
		translate2.setDuration(time);
		translate3.setDuration(time);
		translate4.setDuration(time);
		translate5.setDuration(time);
		translate5.setDuration(time);

		
		
		
		translate2.setByX(VW*.82);
		flyforward( (int) Math.round(VW*.82));
		
		
		Rotate3 = new RotateTransition(Duration.millis(3000), drone);
		Rotate3.setByAngle(90);
		TurnCW(90);
		
		translate3.setByY(this.getLength());
		
		Rotate4 = new RotateTransition(Duration.millis(3000), drone);
		Rotate4.setByAngle(90);
		
		
		translate4.setByX(-VW*.82);
		
		Rotate5 = new RotateTransition(Duration.millis(3000), drone);
		Rotate5.setByAngle(-90);
		TurnCCW(90);
		
		translate5.setByY(this.getLength());
		
		Rotate6 = new RotateTransition(Duration.millis(3000), drone);
		Rotate6.setByAngle(-90);
		
		Rotate7 = new RotateTransition(Duration.millis(3000), drone);
		Rotate7.setByAngle(-90);
		
		translate6.setToX(-drone.getLayoutX() + this.getParentContainer().getLocationX() + 5);
		translate6.setToY(-drone.getLayoutY() - (-this.getParentContainer().getLocationY()) + 5);
		
		Rotate8 = new RotateTransition(Duration.millis(3000), drone);
		Rotate8.setByAngle(90);
		
		
		this.setLocationX(15);
		this.setLocationY(15);
		
		sequence2 = new SequentialTransition(translate2, Rotate3, translate3, Rotate4, translate4, Rotate5, translate5, Rotate6);
		sequence3 = new SequentialTransition(translate2, Rotate3, translate3, Rotate4, translate4, Rotate5, translate5, Rotate6);
		sequence4 = new SequentialTransition(translate2, Rotate3, translate3, Rotate4, translate4, Rotate5, translate5, Rotate6);
		sequence5 = new SequentialTransition(translate2, Rotate3, translate3, Rotate4, translate4, Rotate5, translate5, Rotate6);
		sequence6 = new SequentialTransition(translate2, Rotate3, translate3, Rotate4, translate4, Rotate5, translate5, Rotate6);
		sequence7 = new SequentialTransition(Rotate7, translate6, Rotate8);
		sequence = new SequentialTransition(sequence2, sequence3, sequence4, sequence5, sequence6, sequence7);
		
		sequence.play();
		sequence.setOnFinished(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				scanButton.setText("Scan Farm");
			}
			
		});
		
		
		
		this.setLocationX(this.getParentContainer().getLocationX() + 5);
		this.setLocationY(this.getParentContainer().getLocationY() + 5);
		
		}

	
	
	public void delete() {
		System.out.println("Cannot delete Drone Item");
	}


	@Override
	public void launch() throws IOException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void land() throws IOException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void IncreaseAltitude(int up) throws IOException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void DecreaseAltitude(int down) throws IOException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void flyforward(int forward) throws IOException {
		// TODO Auto-generated method stub
		forwardDist = forward;
		
	}




	@Override
	public void TurnCW(double degrees) throws IOException {
		// TODO Auto-generated method stub
		degreeCW = degrees;
		
		
		
	}


	@Override
	public void TurnCCW(double degrees) throws IOException {
		// TODO Auto-generated method stub
		degreeCCW = degrees;
		
	}






	@Override
	public void hoverInPlace(int seconds) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		Hovering = seconds;
		
	}




	@Override
	public int itemheight(int height) throws IOException, NumberFormatException {
		// TODO Auto-generated method stub
		ItemH = height;
		return height;
	}


}
