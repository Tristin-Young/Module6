package application;

import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.jupiter.api.Test;

/**
 * This is a basic JUnit test suite to test the GUI class. The GUI has two basic functions,
 * returning the most commonly used words from a given HTML file, or returning the amount of 
 * times a given string occurred in a given HTML file. These test aim to ensure the functionality
 * of the logic in the program.
 * @author Tristin Young
 * @version 20230728
 *
 */
class GUITest {

	//begin test
	/**
	 * Test that prints to the console notifying developer that the
	 * JUnit unit tests have started (will never fail).
	 */
	@Test
	void testStart() {
		System.out.println("Running JUnit tests.");
	}

	//verify the countEachWord() function is returning the proper amount of occurrences 
	//in the given input file. This is the most crucial part of the program, as the GUI 
	//actively uses this program the most
	/**
	 * Test case to ensure that the method countEachWord() is functioning properly. 
	 * Five input strings are passed in as parameters to the map.get() method after 
	 * countEachWord() has executed. The result from map.get() is captured and compared to the expected output using the 
	 *  assertEquals() method.
	 *  @throws FileNotFoundException If the file path cannot be located, the program will halt with 
	 *   an exception error.
	 */
	@Test
	void verifyCountEachWord() throws FileNotFoundException {
		//GUI gui = new GUI();
		Map<String, Integer> map = new HashMap<String, Integer>();
		GUI.countEachWord("../Module6GUI/Module 2 Assignment Input.htm", map);
		String input1 = "howdy";
		String input2 = "the";
		String input3 = "and";
		String input4 = "raven";
		String input5 = "cold";
		
		Integer expectedRes1 = 0;
		Integer expectedRes2 = 56;
		Integer expectedRes3 = 38;
		Integer expectedRes4 = 10;
		Integer expectedRes5 = 0;
		
		Integer result1 = ((result1 = map.get(input1)) != null) ? result1 : 0;
		Integer result2 = ((result2 = map.get(input2)) != null) ? result2 : 0;
		Integer result3 = ((result3 = map.get(input3)) != null) ? result3 : 0;
		Integer result4 = ((result4 = map.get(input4)) != null) ? result4 : 0;
		Integer result5 = ((result5 = map.get(input5)) != null) ? result5 : 0;
		
		assertEquals(expectedRes1, result1);
		assertEquals(expectedRes2, result2);
		assertEquals(expectedRes3, result3);
		assertEquals(expectedRes4, result4);
		assertEquals(expectedRes5, result5);
		
	}
		
	//verify generateResults functions properly
	//this test is to mainly ensure that generateResults() returns the correct
	//amount of items in the list
	/**
	 * Test case to ensure that the method generateResults() is functioning properly.
	 *  For the length of a given list, iterate through the list and call generateResults
	 *  (list, current iteration). This will produce every possible output to generateResults().
	 *   The length of the list that is passed as output can be compared to the current iteration.
	 *   If they match, the test case passed. 
	 *   @throws FileNotFoundException If the file path cannot be located, the program will halt with 
	 *   an exception error.
	 */
	@Test
	void verifyGenerateResults() throws FileNotFoundException {
		//GUI gui = new GUI();
		Map<String, Integer> map = new HashMap<String, Integer>();
		GUI.countEachWord("../Module6GUI/Module 2 Assignment Input.htm", map);
		List <Entry<String, Integer>> nlist = new ArrayList<>(map.entrySet());
		nlist.sort(Entry.comparingByValue(Comparator.reverseOrder()));
		int lastIndex = nlist.lastIndexOf(nlist);
		for(int i = 0; i < lastIndex; i++) {
			
			int result = GUI.generateResults(nlist, i).size();
			assertEquals(result, i);
		}
	}

	//verify digits are only present in given input string
	/**
	 * Test case to ensure that the method verifyDigits() is functioning properly. 
	 * Five input strings are passed in as parameters to verifyDigits(). The result
	 *  from verifyDigits() is captured and compared to the expected output using the 
	 *  assertEquals() method.
	 */
	@Test
	void verifyDigitValidation() {
		GUI gui = new GUI();
		String input1 = "23";
		String input2 = "dfg";
		String input3 = "3";
		String input4 = "23/";
		String input5 = "456,";
		
		Boolean expected1 = true;
		Boolean expected2 = false;
		Boolean expected3 = true;
		Boolean expected4 = false;
		Boolean expected5 = false;
		
		Boolean result1 = gui.verifyDigits(input1);
		Boolean result2 = gui.verifyDigits(input2);
		Boolean result3 = gui.verifyDigits(input3);
		Boolean result4 = gui.verifyDigits(input4);
		Boolean result5 = gui.verifyDigits(input5);
		
		assertEquals(expected1, result1);
		assertEquals(expected2, result2);
		assertEquals(expected3, result3);
		assertEquals(expected4, result4);
		assertEquals(expected5, result5);
	}
	
	//verify normalization of input works
	/**
	 * Test case to ensure that the method normalizeString() is functioning properly. 
	 * Five input strings are passed in as parameters to normalizeString(). The result
	 *  from normalizeString() is captured and compared to the expected output using the 
	 *  assertEquals() method.
	 */
	@Test
	void verifyNormalizeString() {
		
		String input1 = "HoWdy,";
		String input2 = "the.";
		String input3 = "!and";
		String input4 = "raven,";
		String input5 = "'cold'";
		
		String expectedRes1 = "howdy";
		String expectedRes2 = "the";
		String expectedRes3 = "and";
		String expectedRes4 = "raven";
		String expectedRes5 = "cold";
		
		String result1 = GUI.normalizeString(input1);
		String result2 = GUI.normalizeString(input2);
		String result3 = GUI.normalizeString(input3);
		String result4 = GUI.normalizeString(input4);
		String result5 = GUI.normalizeString(input5);
		
		assertEquals(expectedRes1, result1);
		assertEquals(expectedRes2, result2);
		assertEquals(expectedRes3, result3);
		assertEquals(expectedRes4, result4);
		assertEquals(expectedRes5, result5);
	}
	
	//test concluded
	/**
	 * Test that prints to the console notifying developer that the
	 * JUnit unit tests have all ran (will never fail).
	 */
	@Test
	void testFinish() {
		System.out.println("JUnit Tests competed.");
	}
}
