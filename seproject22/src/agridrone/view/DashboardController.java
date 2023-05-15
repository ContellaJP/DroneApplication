package agridrone.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;
import java.net.UnknownHostException;

import agridrone.MainApp;
import agridrone.model.Drone;
import agridrone.model.Farm;
import agridrone.model.Item;
import agridrone.model.ItemAbstract;
import agridrone.model.ItemContainer;
import agridrone.model.MarketValueVisitor;
import agridrone.model.TelloDrone;

public class DashboardController {
	
	@FXML
	private TreeView<ItemAbstract> itemList;
	
	@FXML
	private Label detailsLabel;
	
	@FXML
	private Label nameLabel;
	
	@FXML
	private Label priceLabel;
	
	@FXML
	private Label locationLabel;
	
	@FXML
	private Label sizeLabel;
	
	@FXML
	private Label marketValueLabel;
	
	@FXML
	private RadioButton visitButton;
	
//	@FXML
//    private Button parentButton;
	
	@FXML
	private RadioButton scanButton;
	
	@FXML
	private Button launchSim;
	
	@FXML
	private Button launchDrone;
	
	@FXML
	private AnchorPane myVisual;
	
	private Drone drone;
	
	private ItemContainer commCent;
	
	private ImageView ImageBox = new ImageView(new Image("Drone.png"));
	
	//main app reference
	private MainApp mainApp;
	
	//main farm reference
	private Farm farm;
	
	//initializing max height for accurate altitude on launch
	private int maxHeight;
	
	
	//TelloDrone reference
	private TelloDrone Tello;
	
	DashboardController Controller;
	
	//singleton stuff
	private static final DashboardController singleton = new DashboardController();
	public final int WINDOW_WIDTH = 1260;
	public final int WINDOW_HEIGHT = 960;
	
	static public DashboardController getSingleton() {
		return DashboardController.singleton;
	}

	public DashboardController() {
		
	}
	
	private EventHandler<ActionEvent> renameEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			showDialogBox(0);
		}
	};
	private EventHandler<ActionEvent> changeLocationEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			showDialogBox(1);
		}
	};
	private EventHandler<ActionEvent> changePriceEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			showDialogBox(2);
		}
	};
	
	private EventHandler<ActionEvent> changeMarketValueEvent = new EventHandler<ActionEvent>() {
        public void handle(ActionEvent e) {
            showDialogBox(3);
        }
    };
    
    
	private EventHandler<ActionEvent> changeDimensionsEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			showDialogBox(4);
		}
	};
	private EventHandler<ActionEvent> addItemEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			showDialogBox(5);
			
			
		}
	};
	private EventHandler<ActionEvent> addContainerEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			showDialogBox(6);
		}
	};
	private EventHandler<ActionEvent> deleteItemEvent = new EventHandler<ActionEvent>() {
		public void handle(ActionEvent e) {
			TreeItem<ItemAbstract> current = getSelected();
			current.getValue().delete();
			renderTree();
		}
	};
	
	
	
	
	@FXML
	public void initialize() {
		//initial farm, comm center and drone data
		farm = Farm.getInstance();
        commCent = new ItemContainer("Command Center", 10, 10, 90, 90, 20, 1000);
        drone = new Drone("Drone", commCent.getLocationX() + 5, commCent.getLocationY() + 5, 80, 80, 5, 1000, 1000);
        commCent.addItemAbstract(drone);
        farm.addItemAbstract(commCent);
		
        
		renderTree();
		
		
		
		
		

//		initial null load
		showItemDetails(null);
		//sets the label of each item in the tree table to the name of the item
		itemList.setCellFactory(p ->  new TreeCellFactory());
		
		//selection listener
		itemList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<ItemAbstract>>() {
			@Override
			public void changed(ObservableValue<? extends TreeItem<ItemAbstract>> observable, TreeItem<ItemAbstract> oldValue, TreeItem<ItemAbstract> newValue) {
				if (newValue != null) {
					showItemDetails(newValue.getValue());
				} else {
					showItemDetails(null);
				}
				
			}
		});
		
		
		

		
		EventHandler<ActionEvent> toggleController = new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent e) {
				
				try {
					if(scanButton.isSelected()) {
					drone.scanFarm(myVisual.getPrefHeight(), myVisual.getPrefWidth(), ImageBox, scanButton);
					}
					
					else if(visitButton.isSelected()) {
						drone.gotoItem(itemList.getSelectionModel().getSelectedItem().getValue(), ImageBox, scanButton);
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
			
		};
		
		EventHandler<ActionEvent> LaunchDronebutton = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				try {
					Controller = new DashboardController();
					Controller.setMaxHeight(maxHeight);
					
					Tello = new TelloDrone(drone, Controller);
				} catch (SocketException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (UnknownHostException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (FileNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					if(scanButton.isSelected()) {
						
					try {
						Tello.ScanFarm();
					} catch (InterruptedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					}
					
					else if(visitButton.isSelected()) {
						try {
							Tello.Gotoitem();
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		};
		
//		EventHandler<ActionEvent> gotoItemFarm = new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent e) {
//				try {
//					drone.gotoItem(itemList.getSelectionModel().getSelectedItem().getValue(), ImageBox, scanButton);
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//			}
//		};
		
		
		
		
//		EventHandler<ActionEvent> gotoParentFarm = new EventHandler<ActionEvent>() {
//			@Override
//			public void handle(ActionEvent e) {
//				drone.gotoParent(ImageBox, scanButton);
//			}
//		};
		
		
		
		
		
		launchSim.setOnAction(toggleController);
		launchDrone.setOnAction(LaunchDronebutton);
//		visitButton.setOnAction(gotoItemFarm);
//		parentButton.setOnAction(gotoParentFarm);
		
		
		
		
	}
	
	//get currently selected TreeItem
	public TreeItem<ItemAbstract> getSelected() {
		return itemList.getSelectionModel().getSelectedItem();
	}
	
	private ContextMenu makeContextMenu(int type) {
		//Item context menu
		ContextMenu contextMenu = new ContextMenu();
		MenuItem menuAction1 = new MenuItem("Rename");
        menuAction1.setOnAction(renameEvent);
        MenuItem menuAction2 = new MenuItem("Change Location");
        menuAction2.setOnAction(changeLocationEvent);
        MenuItem menuAction3 = new MenuItem("Change Price");
        menuAction3.setOnAction(changePriceEvent);
        MenuItem menuAction4 = new MenuItem("Change Market Value");
        menuAction4.setOnAction(changeMarketValueEvent);
        MenuItem menuAction5 = new MenuItem("Change Dimensions");
        menuAction5.setOnAction(changeDimensionsEvent);
        MenuItem menuAction6 = new MenuItem("Add Item");
        menuAction6.setOnAction(addItemEvent);
        MenuItem menuAction7 = new MenuItem("Add Item Containter");
        menuAction7.setOnAction(addContainerEvent);
        MenuItem menuAction8 = new MenuItem("Delete");
        menuAction8.setOnAction(deleteItemEvent);
        switch(type) {
        case 1:
            contextMenu.getItems().addAll(menuAction1, menuAction2, menuAction3, menuAction4, menuAction6, menuAction7, menuAction8);
            break;
        case 2:
            contextMenu.getItems().addAll(menuAction1, menuAction2, menuAction3, menuAction5, menuAction6, menuAction7, menuAction8);
            break;
        case 3:
            contextMenu.getItems().addAll(menuAction1, menuAction2, menuAction3, menuAction4, menuAction5, menuAction8);
            break;
        case 4:
            contextMenu.getItems().addAll(menuAction3, menuAction6, menuAction7);
        }
		
		return contextMenu;
	}
	
	private final class TreeCellFactory extends TreeCell<ItemAbstract> {

		
		public TreeCellFactory() {}
		
		@Override
		public void updateItem(ItemAbstract item, boolean empty) {
			super.updateItem(item, empty);
			
			//if empty Item
			if (empty) {
				setText(null);
			} 
			//otherwise set text to name
			else {
				setText(item.getName());
			}
			
			//set the context menu based off item type
			if (item instanceof Item) {
				setContextMenu(makeContextMenu(1));
			}
			
			if (item instanceof ItemContainer) {
				setContextMenu(makeContextMenu(2));
			}
			
			if(item instanceof Drone) {
				setContextMenu(makeContextMenu(3));
			}
			if (item instanceof Farm) {
				setContextMenu(makeContextMenu(4));
			}
		
			//check each item against current maxHeight and replace if greater
	        if (item != null) {
	        	if (item.getHeight() > maxHeight) {
	        	maxHeight = item.getHeight();
	        	
	        }
		}
		}
		
		
	}
	
	
	
	//sets the labels on the page to info corresponding with the selected item
	private void showItemDetails(ItemAbstract item) {
		if(item instanceof Item && item != null) {
		        marketValueLabel.setText(Double.toString(((Item) item).getMarketValue()));
		        }
		if(item instanceof ItemContainer && item != null) {
            double sum = ((ItemContainer) item).getContainerMV(new MarketValueVisitor());
            marketValueLabel.setText("" + sum);
            
        }
        if (item != null) {
            
            detailsLabel.setText(item.getName() + " Details");
            nameLabel.setText(item.getName());
            priceLabel.setText(Double.toString(item.getPrice()));
            locationLabel.setText("(" + Integer.toString(item.getLocationX()) + ", " + Integer.toString(item.getLocationY()) + ")");
            sizeLabel.setText(Integer.toString(item.getLength()) + " x " + Integer.toString(item.getWidth()) + " x " + Integer.toString(item.getHeight()));
            visitButton.setText("Visit " + item.getName());
            if (item.getName().equals("Drone")) {
                visitButton.setText("Cannot visit Drone with Drone");
                visitButton.setDisable(true);
            } else {

                visitButton.setDisable(false);
            }
    
        } else {
            detailsLabel.setText("Select an item to see Details");
            nameLabel.setText("");
            priceLabel.setText("");
            locationLabel.setText("");
            sizeLabel.setText("");
            visitButton.setText("Select an item from the menu to visit it with Drone");
            visitButton.setDisable(true);

        }
        
        
      
	}
	
	
	
	
	
	//render the tree based of the current state of this.farm
	public void renderTree() {

		myVisual.getChildren().clear();
		ImageBox.setLayoutX(drone.getLocationX());
		ImageBox.setLayoutY(drone.getLocationY());
		ImageBox.setFitHeight(drone.getLength());
		ImageBox.setFitWidth(drone.getWidth());
        myVisual.getChildren().add(ImageBox);
        TreeItem<ItemAbstract> root = getTreeChildren(this.farm);
        itemList.setRoot(root);
	}
	

	//function recursively builds out a tree to populate the TableTreeView
	public TreeItem<ItemAbstract> getTreeChildren(ItemAbstract item) {
		//Parent node
		TreeItem<ItemAbstract> parent = new TreeItem<>(item);
		
		//drawing rectangles
        Rectangle rect = new Rectangle();
        rect.setX(item.getLocationX());
        rect.setY(item.getLocationY());
        rect.setWidth(item.getWidth());
        rect.setHeight(item.getLength());
        rect.setStroke(Color.BLACK);
        rect.setFill(Color.TRANSPARENT);
        
        Text text = new Text(item.getLocationX(),item.getLocationY(),item.getName());
        if (!(item instanceof Drone || item instanceof Farm)) {
        	myVisual.getChildren().add(rect);
        	myVisual.getChildren().add(text);
        }
        
        if (item.getHeight() > this.maxHeight) {
        	this.maxHeight = item.getHeight();
        }

		//if the item has children
		if (item instanceof ItemContainer) {
			//for each child, create a child tree item, populate it recursively with getTreeChildren()
			for (ItemAbstract cont : ((ItemContainer) item).getContents()) {
				TreeItem<ItemAbstract> child = getTreeChildren(cont);
				parent.getChildren().add(child);
			}
			//set to expanded
			parent.setExpanded(true);
			return parent;
		}
		return parent;

	}

	//main app setter
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;

	}
	
	public void setMaxHeight(int height) {
		this.maxHeight = height;
	}
	
	//max height for dashboard getter 
		public int getMaxHeight() {
			return this.maxHeight;
		}
	
	//dialog pane
	@FXML
	public void showDialogBox(int c) {
		TreeItem<ItemAbstract> currentItem = getSelected();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("DialogBox.fxml"));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        if (root == null) return;
        Stage dialogBox = new Stage();
        dialogBox.setAlwaysOnTop(true);
        DialogController dialogController = loader.getController();
        dialogBox.setScene(new Scene(root));
        dialogBox.initOwner(mainApp.getPrimaryStage());
        dialogBox.initModality(Modality.APPLICATION_MODAL);
        dialogController.setStage(dialogBox);
        dialogController.buildDialogBox(c, currentItem);
        dialogBox.setOnCloseRequest(e -> dialogController.removeController());
        dialogBox.showAndWait();
        renderTree();
		
	}

}


