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
	
	//creating setup variables
	Stage window;
	Scene scene1, scene2;
	Button button1, button2, wordButton;
	TextField wordInput;
	List <Entry<String, Integer>> nlist;  
	Map<String, Integer> map;
	Scene resultScene, searchResultScene;
	TextArea resultTextArea, searchResultTextArea;
	
	//main function only used for launch function
	public static void main(String[] args) {
		launch(args);
	}
	
	//function to be executed upon program startup
	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//create a primary stage and set the title
	    window = primaryStage;
	    window.setTitle("Word Counting GUI");

	    //Set style
	    String style = "-fx-font: 14 arial; -fx-base: #b6e7c9;";

	    //field input for numeric selection
	    //the user will input a number, and the program is going to return
	    //the top X words that occur in the poem, based on the number input
	    
	    //creating a label and setting it to the style created above
	    Label labelWord = new Label("View the top ___ words");
	    labelWord.setStyle(style);
	    //create text field input to capture users input
	    wordInput = new TextField();
	    //creating a button for user to execute command with name 'See words'
	    //setting button to style created above
	    wordButton = new Button("See words");
	    wordButton.setStyle(style);

	    //field input for word search selection
	    //the user will enter a string, and the program will return the amount of
	    //Occurrences the string has in the given input text
	    
	    //creating a label
	    Label labelSearch = new Label("Search for word: ");
	    //setting style to style created above
	    labelSearch.setStyle(style);
	    //creating text field to capture user input
	    TextField searchInput = new TextField();
	    //creating button to execute search
	    Button searchButton = new Button("Search");
	    //styling button according to style above
	    searchButton.setStyle(style);

	    
	    //creating a horizontal box to hold the numeric selection elements 
	    HBox hbox1 = new HBox(20);
	    //adding the label, text input, and button for numeric selection
	    hbox1.getChildren().addAll(labelWord, wordInput, wordButton);

	    //creating another horizontal box to hold the word search selection
	    HBox hbox2 = new HBox(20);
	    //adding the label, text input, and button for word search selection
	    hbox2.getChildren().addAll(labelSearch, searchInput, searchButton);

	    //creating a vertical box to hold the hroizontal boxes
	    VBox mainLayout = new VBox(20);
	    //layer the two horizontal boxes on top of one another
	    mainLayout.getChildren().addAll(hbox1, hbox2);
	    //add some padding to the boxes to make it more visually appealing
	    mainLayout.setPadding(new Insets(20));

	    //creating a scene
	    Scene wordScene = new Scene(mainLayout, 600, 400);

	    //adding a return button for when user successfully executes command 
	    Button returnButton = new Button("Return to Main");
	    //set style of button
	    returnButton.setStyle(style);
	    //creating a result text area to diplay results to the user
	    //this section is not editable by the user
	    resultTextArea = new TextArea();
	    resultTextArea.setEditable(false);
	    
	    //creating a vertical box for the result text and button
	    VBox resultLayout = new VBox(20);
	    //adding elements to vertical box
	    resultLayout.getChildren().addAll(resultTextArea, returnButton);
	    
	    //creating a result scene
	    resultScene = new Scene(resultLayout, 600, 600);

	    //when the user returns to the main GUI, clear all inputs
	    returnButton.setOnAction(event -> {
	        window.setScene(wordScene);
	        wordInput.setText(""); 
	        // Clear the text input field
	        searchInput.setText(""); 
	        // Clear the search input field
	    });
	    
	    //default stage with title 'Word Counting GUI'
	    primaryStage.setScene(wordScene);
	    primaryStage.setTitle("Word Counting GUI");
	    //show this stage by default
	    primaryStage.show();

	    //inner code from module 2
	    //create a map to hold the words and occurrences
	    map = new HashMap<String, Integer>();
	    
	   //count each word in file specified
	    countEachWord("../Module2GUI/Module 2 Assignment Input.htm", map);
	    
	    //creating a list to sort by number of occurrences
	    nlist = new ArrayList<>(map.entrySet());
	    //order list to get results list
	    nlist.sort(Entry.comparingByValue(Comparator.reverseOrder()));

	    //when user presses wordButton (button for numeric selection)
	    wordButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	//get text in the text field
	            String input = wordInput.getText();
	            //if text is a digit
	            if (input.matches("\\d+")) {
	            	//parse the digit 
	                int number = Integer.parseInt(input);
	                //run generateResults() with proper arguments
	                List<String> res = generateResults(nlist, number);
	                //add the resulting text to the text box (un-editable by user)
	                resultTextArea.setText(String.join("\n", res));
	                //show the resulting scene
	                window.setScene(resultScene);
	                //clear user input on main GUI
	                wordInput.setText("");
	                
	            // otherwise alert for invalid input and return error
	            } else {
	                Alert alert = new Alert(AlertType.INFORMATION);
	                alert.setTitle("Invalid Input");
	                alert.setContentText("Please enter a valid number.");
	                alert.showAndWait();
	            }
	        }
	    });
	    
	    //when a user presses earchButton (button for word search selection)
	    searchButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	//get input text and modify it the same way as poem is modified to
	        	//ensure things like capitalization and punctuation do not affect results
	            String input = searchInput.getText().toLowerCase().replaceAll(",", "").replaceAll("!", "")
	                    .replaceAll("\\.", "").replaceAll("'", "");
	            //get word count associated with input word
	            Integer wordCount = map.get(input);
	            //if the word count does not exist, return the number zero
	            if (wordCount == null) {
	                wordCount = 0;
	            }
	            
	            //return an alert to the user informing them how many times their word occurs
	            //in the given input file
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
		
		//helper function to generate results
		static List<String> generateResults(List<Entry<String, Integer>> list, int amount) {
			List<String> results = new ArrayList<>();
			for (int i = 0; i < amount; i++) {
				results.add(i+1 + ": " + list.get(i));
			}
			return results;
		}

		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
}


