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
import java.util.stream.Collectors;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import java.sql.*;
/**
 * This class is an example of a GUI. The class utilizes JavaFX
 * to present the user with a GUI. The GUI will allow the user to query
 * Edgar Allen Poe's "The Raven" to find information about the words and the amount of times they
 * occur inside the file. More specifically, the GUI class allows a user to interact with
 * any HTML file in the same way as described above, however currently the file is pointed towards
 * Poe's poem.
 * 
 * @author Tristin Young
 * @version 20230728
 * 
 */
public class GUI extends Application implements EventHandler<ActionEvent>{
	
	/**
	 * Alias for JavaFX's default primaryStage
	 */
	Stage window;
	/**
	 * This button runs the query that returns the top 'x' words that occurred in the file. The amount of
	 * words returned to the user is dictated by wordInput
	 */
	Button wordButton;
	/**
	 * This text box takes an integer. This text box should receive an integer and pass it into the query that returns
	 * the top 'x' words that occurred in the file. In this example, 'x' would be the number that the user entered.
	 */
	TextField wordInput;
	/**
	 * List is created to store an ordered version of the hashmap once the file has been fully parsed.
	 */
	List <Entry<String, Integer>> nlist;  
	/**
	 * Map is a hashmap that takes a string and an integer. The purpose of the map is to keep the words and 
	 * their amount of occurrences in pairs. The map is updated when the countEachWord() function is called.
	 */
	Map<String, Integer> map;
	/**
	 * This is the scene the user will see when they have chosen to execute the query that returns the top 'x'
	 * words based on how frequently they occurred in the input file. The scene will display the resultTextArea as well
	 * as a resultButton.
	 */
	Scene resultScene;
	/**
	 * This is the scene the user will see when they have chosen to execute the query that returns amount of times a given
	 * input string occurs in the input file. The scene will display the SearchesultTextArea as well as a resultButton.
	 * as a resultButton.
	 */
	Scene searchResultScene;
	/**
	 * This is a non-editable text area that is to be meant for the resultScene. It will be populated with the top 'x' words
	 * based on frequency in the file, where x is a user given number.
	 */
	TextArea resultTextArea;
	/**
	 * This is a non-editable text area that is meant for the searchResultScene. It will be populated with "The word 'inputString'
	 * occurs 'x' times, where inputString is the string input by the user, and x is the number of times the given string occurred 
	 * in the file.
	 */
	TextArea searchResultTextArea;
	/**
	 * The main method is used to call the launch() function, which is vital to all JavaFX projects.
	 * The launch method is a built in JavFX method and, in turn, calls the start method.
	 * @param args args is the default main method argument
	 */
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
	    countEachWord("../Module6GUI/Module 2 Assignment Input.htm");
	    //create an arrayList to hold the output of the function
	    nlist = new ArrayList<>(map.entrySet());
	    nlist = nlist.stream()
	             .filter(e -> e.getValue() != null)
	             .collect(Collectors.toList());
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
	            	try {
	            		//create a list to hold the results
		                //results come from the generateResults() function, pass in the number just saved
		                List<String> res = generateResults(number);
		                //set the text in the result area to the result list res. Separate each entry
		                //with a new line
		                resultTextArea.setText(String.join("\n", res));
		                //show the user the result scene
		                window.setScene(resultScene);
		                //clear the text input field
		                wordInput.setText(""); 
	            	}catch(SQLException e){
	                    Alert alert = new Alert(AlertType.ERROR);
	                    alert.setTitle("DB Error");
	                    alert.setContentText("A problem occurred while fetching word count data from the database");
	                    alert.showAndWait();
	            	}
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
	            try {
	            	Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/sd1", "root", "password");
	        		Statement stmt = conn.createStatement();
	        		ResultSet res = stmt.executeQuery("SELECT count FROM sd1.wordcounts WHERE word = '" + input + "'");
		            //retrieve the amount of occurrences of the user's word in the given input file
		            Integer wordCount = 0;
		            if(res.next()) {
		            	wordCount = res.getInt("count");
		            }
		            
		            conn.close();
		            stmt.close();
		            res.close();

		            //alert the user of the result. Pass in their input and the resulting occurrence value
		            //this scene persists until the user interacts with it
		            //reset the textbox to be blank
		            Alert alert = new Alert(AlertType.INFORMATION);
		            alert.setTitle("Search Result");
		            alert.setContentText("The word \"" + input + "\" appears " + wordCount + " times.");
		            alert.showAndWait();
		            searchInput.setText(""); // Clear the search input field
		            
	            }catch(SQLException e) {
	            	e.printStackTrace();
	            }

	            
	        }
	    });
	}

	
	//function to take an input file location, and index the occurrence of the words
	//in the document. The method takes in a HTML file and only parses words inside 
	//paragraph tags
	/**
	 * This function takes in an HTML file location, and parses the words in between paragraph tags.
	 * There is normalization of the words (all lowercase, punctuation removed). Once a word is parsed, it gets added
	 * to a hashmap where each word is tracked along with the amount of times that word has occurred inside the given
	 * input file. The method will throw an exception if the file location cannot be reached.
	 * @param fileName The filepath of the desired HTML file.
	 * @param words The hashmap that is to be updated with the output of countEachWord().
	 * @throws FileNotFoundException If the file location cannot be reached, throw an exception and halt the program.
	 */
	static void countEachWord(String fileName) throws FileNotFoundException, SQLException {
	    // Establish a connection to the MySQL database.
	    Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/sd1", "root", "password");
		
	    // Read in file
	    Scanner file = new Scanner(new File(fileName));
	    
	    // While we are not at the end of the file
	    while(file.hasNext()) {
	        // Set word to lowercase
	        String word = file.next().toLowerCase();
			
	        // If the word is a paragraph tag <p>
	        if(word.contentEquals("<p>")) {
	            // While we are not at the closing paragraph tag </p>
	            do {
	                // Coercion: remove any capitalization as well as special chars !.,'
	                word = file.next().toLowerCase();
	                word = normalizeString(word);
					
	                // If the word contains chars that make it an HTML tag
	                if(word.contains("<") || word.contains(">") || word.contains("/") || word.contains(":") || word.contains("&")
	                    || word.contains("“") || word.contains("“")) {
	                    // Skip the word
	                    continue;
	                }
					
	                // Use a PreparedStatement to ensure proper SQL syntax and prevent SQL injection
	                PreparedStatement ps = conn.prepareStatement("SELECT count FROM sd1.wordcounts WHERE word = ?");
	                ps.setString(1, word);
	                ResultSet sqlOutput = ps.executeQuery();

	                int count = 0;
	                if(sqlOutput.next()) {
	                    // If the word is in the database, increment its count
	                    count = sqlOutput.getInt("count");
	                    String sql = "UPDATE sd1.wordcounts SET count = ? WHERE word = ?";
	                    PreparedStatement psUpdate = conn.prepareStatement(sql);
	                    psUpdate.setInt(1, count+1);
	                    psUpdate.setString(2, word);
	                    psUpdate.executeUpdate();
	                } else {
	                    // If the word is not in the database, add it with a count of 1
	                    count = 1;
	                    String sql = "INSERT INTO sd1.wordcounts (word, count) VALUES (?, ?)";
	                    PreparedStatement psInsert = conn.prepareStatement(sql);
	                    psInsert.setString(1, word);
	                    psInsert.setInt(2, count);
	                    psInsert.executeUpdate();
	                }
	            } while(!word.contentEquals("</p>"));
	        }
	    }
	    // Close the file
	    file.close();
	    // Close the connection to the database
	    conn.close();
	}

		//helper function to print the results of the map
	/**
	 * Helper function to print the results of the method to the console.
	 * @param list Takes in a sorted list of type (String, Integer). List is sorted by Integer, greatest to least.
	 * @param amount The amount of word-occurrence pairs desired to be printed to the console
	 */
		static void printResults(List <Entry<String, Integer>> list, int amount) {
		//for loop to iterate over items
		for(int i = 0; i < amount; i++)
		{
		//print out index as well as word/count pair
		System.out.println(i+1 + ": " + list.get(i));
		}
		}
		
		//function to verify if input string is only digits or not
		/**
		 * Function that takes a string as input and returns a boolean depending on if the string is a number or not (numerical number).
		 * The function will return false if the string contains anything other than digits; only digits will return a value of true.
		 * @param numberstr The string to be verified. 
		 * @return Boolean return, true if the string is all digits, false otherwise.
		 */
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
		/**
		 * Takes in a list and an integer. The list is already sorted. Loop through the list,
		 * appending each items to a results list. When the loop index is equal to the integer parameter,
		 * return the results list.
		 * @param list A list of type (String, Integer) that is sorted by the integer from greatest to least
		 * @param amount The amount of items desired in the returning list
		 * @return Returns a list of type (String). Each string in the returning list has format "index: word=occurrences".
		 */
		static List<String> generateResults(int amount)throws SQLException {
			List<String> results = new ArrayList<>();
			Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/sd1", "root", "password");
			Statement stmt = conn.createStatement();
			ResultSet res = stmt.executeQuery("SELECT * FROM sd1.wordcounts ORDER BY count DESC LIMIT " + amount);
			
			while(res.next()) {
				String word = res.getString("word");
				int count = res.getInt("count");
				results.add(word + ": " + count);
			}
			
			conn.close();
			stmt.close();
			res.close();
			
			return results;
		}

		/**
		 * Method to normalize a string (within reason). Method takes an input string and removes all punctuation and sets 
		 * all characters to lowercase.
		 * @param input The string to be normalized
		 * @return Returnsn the same string, without punctuation and all lowercase letters
		 */
		static String normalizeString(String input) {
			String result = input.toLowerCase().replaceAll(",", "").replaceAll("!", "")
	                .replaceAll("\\.", "").replaceAll("'", "");
			return result;
		}
		
		/**
		 * Auto generated method required by JavaFX
		 */
		@Override
		public void handle(ActionEvent arg0) {
			// TODO Auto-generated method stub
			
		}
		
}


