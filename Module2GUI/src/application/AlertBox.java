package application;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.*;
import javafx.geometry.*;

public class AlertBox {

	public static void display(String title, String message, String buttonText) {
		
		Stage window = new Stage();
		
		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle(title);
		window.setMinWidth(300);
		Label label = new Label();
		label.setText(message);
		Button closeButton = new Button(buttonText);
		closeButton.setOnAction(e -> window.close());
		
		VBox layout = new VBox(20);
		layout.getChildren().addAll(label, closeButton);
		layout.setAlignment(Pos.CENTER);
		
		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
	}
}





























