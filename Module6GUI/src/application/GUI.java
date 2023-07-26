package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;

public class GUI extends Application implements EventHandler<ActionEvent>{
	
	Stage window;
	Scene scene1, scene2;
	Button button1, button2, wordButton;
	TextField wordInput;
	List <Entry<String, Integer>> nlist;  
	Map<String, Integer> map;
	Scene resultScene, searchResultScene;
	TextArea resultTextArea, searchResultTextArea;
	
	
	public static void main(String[] args) {
		launch(args);
	}
		
	@Override
	public void start(Stage primaryStage) throws Exception {
	    window = primaryStage;
	    window.setTitle("Word Counting GUI");

	    // Set global style for the labels, buttons and textfields
	    String style = "-fx-font: 14 arial; -fx-base: #b6e7c9;";

	    Label labelWord = new Label("View the top ___ words");
	    labelWord.setStyle(style);
	    wordInput = new TextField();
	    wordButton = new Button("See words");
	    wordButton.setStyle(style);

	    Label labelSearch = new Label("Search for word: ");
	    labelSearch.setStyle(style);
	    TextField searchInput = new TextField();
	    Button searchButton = new Button("Search");
	    searchButton.setStyle(style);

	    HBox hbox1 = new HBox(20);
	    hbox1.getChildren().addAll(labelWord, wordInput, wordButton);

	    HBox hbox2 = new HBox(20);
	    hbox2.getChildren().addAll(labelSearch, searchInput, searchButton);

	    VBox mainLayout = new VBox(20);
	    mainLayout.getChildren().addAll(hbox1, hbox2);
	    mainLayout.setPadding(new Insets(20)); // Add some padding around the edges

	    Scene wordScene = new Scene(mainLayout, 600, 400);

	    Button returnButton = new Button("Return to Main");
	    returnButton.setStyle(style);
	    resultTextArea = new TextArea();
	    resultTextArea.setEditable(false);
	    VBox resultLayout = new VBox(20);
	    resultLayout.getChildren().addAll(resultTextArea, returnButton);
	    resultScene = new Scene(resultLayout, 600, 600);

	    returnButton.setOnAction(event -> {
	        window.setScene(wordScene);
	        wordInput.setText(""); // Clear the text input field
	        searchInput.setText(""); // Clear the search input field
	    });

	    primaryStage.setScene(wordScene);
	    primaryStage.setTitle("Word Counting GUI");
	    primaryStage.show();

	    map = new HashMap<String, Integer>();
	    countEachWord("../Module 2 Assignment Input.htm", map);
	    nlist = new ArrayList<>(map.entrySet());
	    nlist.sort(Entry.comparingByValue(Comparator.reverseOrder()));

	    wordButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	            String input = wordInput.getText();
	            if (input.matches("\\d+")) {
	                int number = Integer.parseInt(input);
	                List<String> res = generateResults(nlist, number);
	                resultTextArea.setText(String.join("\n", res));
	                window.setScene(resultScene);
	                wordInput.setText(""); // Clear the text input field
	            } else {
	                Alert alert = new Alert(AlertType.INFORMATION);
	                alert.setTitle("Invalid Input");
	                alert.setContentText("Please enter a valid number.");
	                alert.showAndWait();
	            }
	        }
	    });

	    searchButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	            String input = searchInput.getText().toLowerCase().replaceAll(",", "").replaceAll("!", "")
	                    .replaceAll("\\.", "").replaceAll("'", "");
	            Integer wordCount = map.get(input);
	            if (wordCount == null) {
	                wordCount = 0;
	            }
	            Alert alert = new Alert(AlertType.INFORMATION);
	            alert.setTitle("Search Result");
	            alert.setContentText("The word \"" + input + "\" appears " + wordCount + " times.");
	            alert.showAndWait();
	            searchInput.setText(""); // Clear the search input field
	        }
	    });
	}
	
	
	
	static void countEachWord(String fileName, Map<String, Integer> words) throws FileNotFoundException {
		//read in file
		Scanner file = new Scanner(new File(fileName));
		//while we are not at the end of the file
		while(file.hasNext()) {
		//set word to lowercase
		String word = file.next().toLowerCase();
		//if the word is a paragraph tag <p>
		if(word.contentEquals("<p>")) {
		//while we are not at the closing paragraph tag </p>
		do {
		//coercion
		//remove any capitalization as well as special chars !.,'
		word = file.next().toLowerCase();
		word = word.replaceAll(",", "").replaceAll("!", "")
		.replaceAll("\\.", "").replaceAll("'", "");
		//if the word contains chars that make it an HTML tag
		if(word.contains("<") || word.contains(">") || word.contains("/")
		|| word.contains(":") || word.contains("&")
		|| word.contains("“") || word.contains("“")) {
		//skip the word
		continue;
		}
		//otherwise get the current count from the map
		Integer count = words.get(word);
		//if the count exists, increment it
		if(count != null)
		{
		count++;
		}
		//otherwise create count and set it to 1
		else {
		count = 1;
		}
		//save the new count into the map so long as the word != </p>
		if(!word.contentEquals("</p>")) {
		words.put(word, count);
		}

		}while(!word.contentEquals("</p>"));

		}



		}
		//close the file
		file.close();
		}

		//helper function to print the results of the map
		static void printResults(List <Entry<String, Integer>> list, int amount) {
		//for loop to iterate over items
		for(int i = 0; i < amount; i++)
		{
		//print out index as well as word/count pair
		System.out.println(i+1 + ": " + list.get(i));
		}
		}
		
		static List<String> generateResults(List<Entry<String, Integer>> list, int amount) {
			List<String> results = new ArrayList<>();
			for (int i = 0; i < amount; i++) {
				results.add(i+1 + ": " + list.get(i));
			}
			return results;
		}
		
		static void searchResults(List <Entry<String, Integer>> list, String search) {
			
		}

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
}


