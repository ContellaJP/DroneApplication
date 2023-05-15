package agridrone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;


import agridrone.model.ItemAbstract;
import agridrone.model.ItemContainer;
import agridrone.view.DashboardController;
import agridrone.view.DialogController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.LoadException;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;




public class MainApp extends Application {
	

	
	private Stage primaryStage;
	
	

	@Override
	public void start(Stage stage) throws IOException {

		FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("view/Dashboard.fxml"));
		Scene scene = new Scene(
				fxmlLoader.load());
		this.primaryStage = stage;
		primaryStage.setTitle("Agricultural Drone App");
		primaryStage.setScene(scene);
		primaryStage.show();
		DashboardController controller = fxmlLoader.getController();
		controller.setMainApp(this);
		

	}
	
	public Stage getPrimaryStage() {
		return this.primaryStage;
	}
	

	public static void main(String[] args) {
		launch(args);
	}
}