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
	    
	    
	    //section for components of first text box input an associated data
	    //this text box allows a user to input a number and be shown the resulting top
	    //words from the input file. A label 'View the top ___ words' is created. A text input
	    //is created to capture the users input. A button is created to execute the function
	    //The label and button are styled using the above string on line 48
	    Label labelWord = new Label("View the top ___ words");
	    labelWord.setStyle(style);
	    wordInput = new TextField();
	    wordButton = new Button("See words");
	    wordButton.setStyle(style);

	    //section for components of second text box input an associated data
	    //this text box allows a user to input a word and be shown the resulting occurrences of that
	    //word from the input file. A label 'Search for word: ' is created. A text input
	    //is created to capture the users input. A button is created to execute the function
	    //The label and button are styled using the above string on line 48
	    Label labelSearch = new Label("Search for word: ");
	    labelSearch.setStyle(style);
	    TextField searchInput = new TextField();
	    Button searchButton = new Button("Search");
	    searchButton.setStyle(style);

	    //creating a horizontal box to display the number function's label
	    //textbox, and button all inline with eachother, in that order
	    HBox hbox1 = new HBox(20);
	    hbox1.getChildren().addAll(labelWord, wordInput, wordButton);

	    //creating a horizontal box to display the word search's label
	    //textbox, and button all inline with eachother, in that order
	    HBox hbox2 = new HBox(20);
	    hbox2.getChildren().addAll(labelSearch, searchInput, searchButton);

	    //creating a main vertical box layout to put the horizontal boxes linearly
	    //add padding to aid in aesthetics
	    VBox mainLayout = new VBox(20);
	    mainLayout.getChildren().addAll(hbox1, hbox2);
	    mainLayout.setPadding(new Insets(20));

	    //creating the input scene based on the vertical box above, and set it to 600 x 400 pixels
	    Scene wordScene = new Scene(mainLayout, 600, 400);

	    //creating a return button so a user can get from an output screen to the main screen of
	    //the GUI. Label it 'Return to Main' and style it according to line 48
	    Button returnButton = new Button("Return to Main");
	    returnButton.setStyle(style);
	    //create a non-editable text area to display the results of the users query
	    resultTextArea = new TextArea();
	    resultTextArea.setEditable(false);
	    //create a vertical box to hold the text and the button
	    VBox resultLayout = new VBox(20);
	    resultLayout.getChildren().addAll(resultTextArea, returnButton);
	    //create the result scene to display the output of the function the user queried
	    resultScene = new Scene(resultLayout, 600, 600);

	    //when the return button is pressed, set the scene to the main scene of the GUI
	    //Set the text fields to "", so it is blank for the user to use again
	    returnButton.setOnAction(event -> {
	        window.setScene(wordScene);
	        wordInput.setText(""); // Clear the text input field
	        searchInput.setText(""); // Clear the search input field
	    });

	    //set the primary stage to the main scene of the GUI
	    //Give the window the title 'Word Counting GUI'
	    //show the stage
	    primaryStage.setScene(wordScene);
	    primaryStage.setTitle("Word Counting GUI");
	    primaryStage.show();

	    //create a hashmap to store words and amount of occurrences in input file
	    map = new HashMap<String, Integer>();
	    //call countEachWord function, pass in file location and map, created above
	    countEachWord("../Module6GUI/Module 2 Assignment Input.htm", map);
	    //create an arrayList to hold the output of the function
	    nlist = new ArrayList<>(map.entrySet());
	    //sort the list by occurrences, going from greatest to least
	    nlist.sort(Entry.comparingByValue(Comparator.reverseOrder()));

	    //if the user enters a number x, we shall return them the top 'x' words with
	    //the most occurrences in the file. When the user presses the button to query
	    //this function
	    wordButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	//save the input entered by the user
	            String input = wordInput.getText();
	            //if the input is digits only
	            if (verifyDigits(input)) {
	            	//parse the number into an integer
	                int number = Integer.parseInt(input);
	                //create a list to hold the results
	                //results come from the generateResults() function, pass in the number just saved
	                List<String> res = generateResults(nlist, number);
	                //set the text in the result area to the result list res. Separate each entry
	                //with a new line
	                resultTextArea.setText(String.join("\n", res));
	                //show the user the result scene
	                window.setScene(resultScene);
	                //clear the text input field
	                wordInput.setText(""); 
	            
	            //if the user input is not only digits, alert them that the input is invalid and
	            //they need to enter a valid number. The alert will persist until the user interacts
	            //with it
	            } else {
	                Alert alert = new Alert(AlertType.INFORMATION);
	                alert.setTitle("Invalid Input");
	                alert.setContentText("Please enter a valid number.");
	                alert.showAndWait();
	            }
	        }
	    });
	    
	    //if the user enters a word, we shall return them the amount of occurrences of that word
	    //in a given input file. When the user presses the button to query this function
	    searchButton.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	        	//save the input by the user. Normalize it in the same way the poem was normalized
	            String input = searchInput.getText();
	            input = normalizeString(input);
	            //retrieve the amount of occurrences of the user's word in the given input file
	            Integer wordCount = map.get(input);
	            //if the word is not in the hashmap, return 0 because it was not in the input file at all
	            if (wordCount == null) {
	                wordCount = 0;
	            }
	            //alert the user of the result. Pass in their input and the resulting occurrence value
	            //this scene persists until the user interacts with it
	            //reset the textbox to be blank
	            Alert alert = new Alert(AlertType.INFORMATION);
	            alert.setTitle("Search Result");
	            alert.setContentText("The word \"" + input + "\" appears " + wordCount + " times.");
	            alert.showAndWait();
	            searchInput.setText(""); // Clear the search input field
	        }
	    });
	}

	
	//function to take an input file location, and index the occurrence of the words
	//in the document. The method takes in a HTML file and only parses words inside 
	//paragraph tags
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
		word = normalizeString(word);
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
		
		//function to verify if input string is only digits or not
		boolean verifyDigits(String numberstr) {
			if (numberstr.matches("\\d+")) {
				return true;
			}
			else {
				return false;
			}
			
		}
		
		//function to take list and return a list of length 'amount' in the format
		// '1: the=58' , '2: and=38' , etc.
		static List<String> generateResults(List<Entry<String, Integer>> list, int amount) {
			List<String> results = new ArrayList<>();
			for (int i = 0; i < amount; i++) {
				results.add(i+1 + ": " + list.get(i));
			}
			return results;
		}

		static String normalizeString(String input) {
			String result = input.toLowerCase().replaceAll(",", "").replaceAll("!", "")
	                .replaceAll("\\.", "").replaceAll("'", "");
			return result;
		}
		
		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
}


