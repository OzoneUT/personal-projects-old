import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import com.machinepublishers.jbrowserdriver.JBrowserDriver;

public class Driver {

	private final static String loginURL= "https://utdirect.utexas.edu/registration/classlist.WBX?";

	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		File listingHTML = new File("listingHTML.html");
		JBrowserDriver driver = new JBrowserDriver();
		if (!listingHTML.exists()) {
			aquireHTML(driver, input, listingHTML);
		} else {
			System.out.print("A file containing the HTML code for a classes "
					+ "listing already exists in this program's root directory.\n"
					+ "Continue using this file? (Y/N) ");
			String ans = input.nextLine().toLowerCase();
			if (!ans.equals("y") && !ans.equals("yes")) {
				listingHTML.delete();
				aquireHTML(driver, input, listingHTML);
			} 
		}
		// the html file should exist in all cases
		CourseListing storedList = new CourseListing(listingHTML);
		storedList.assignColors();
		storedList.displayAll();
		CalenDraw draw = new CalenDraw();
		draw.init(storedList);
	}	  

	private static void aquireHTML(JBrowserDriver driver, Scanner input, File listingHTML) {
		System.out.println("Connecting to the login page...");
		driver.get(loginURL);
		if (driver.getTitle().equals("UT EID Login")) {
			System.out.print("Connected. ");
		}
		boolean loginSuccess = false;
		while (!loginSuccess) {
			loginSuccess = login(input, driver);
		}
		String listingPageSource = selectSemester(input, driver);
		saveHTML(listingPageSource, listingHTML);
	}

	private static void saveHTML(String listingPageSource, File listingHTML) {
		try {
			listingHTML.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			PrintWriter writer = new PrintWriter(listingHTML);
			writer.println(listingPageSource);
			writer.close();
			System.out.println();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static boolean login(Scanner input, JBrowserDriver driver) {
		System.out.println("Login using your UTDirect credentials.\n");
		System.out.print("Username: ");
		String usr = input.nextLine();
		System.out.print("Password: ");
		//String pwd = System.console().readPassword().toString();
		String pwd = input.nextLine();
		System.out.print("\nAttempting login...");
		WebElement usrField = driver.findElementByName("IDToken1");
		WebElement pwdField = driver.findElementByName("IDToken2");
		WebElement submitButton = driver.findElementByName("Login.Submit");
		usrField.sendKeys(usr);
		pwdField.sendKeys(pwd);
		pwd = null;
		submitButton.click();
		boolean success = driver.getTitle().equals("University of Texas/Class Listing");
		if (!success)
			System.out.println(" Failed to log in. Please try again.");
		else
			System.out.println(" Success.\n");
		return success;
	}

	private static String selectSemester(Scanner input, JBrowserDriver driver) {
		WebElement dropdown = driver.findElementByName("sem");
		TreeMap<String, String> semesters = new TreeMap<>();
		populateChoices(dropdown, semesters, driver);
		int codeIdx = displayAndPrompt(semesters, input);
		processChoice(semesters, codeIdx, driver, input);
		// at this point, a valid page is loaded by the driver
		return driver.getPageSource();
	}

	private static void processChoice(TreeMap<String, String> semesters, int codeIdx, JBrowserDriver driver, Scanner input) {
		String semCode = (String) semesters.keySet().toArray()[codeIdx];
		String semURL = loginURL + "sem=" + semCode;
		driver.get(semURL);
		try {
			// if the textbook button does not exist, will throw exception. 
			// If it does, returns to calling method
			driver.findElementByLinkText("Look Up Textbooks & Compare Prices");
		} catch (NoSuchElementException e){
			System.out.println("There are no classes listed for this semester. Here are the options again: \n");
			codeIdx = displayAndPrompt(semesters, input);
			processChoice(semesters, codeIdx, driver, input);
		}
	}

	private static int displayAndPrompt(TreeMap<String, String> semesters, Scanner input) {
		int choiceNum = 0;
		for (String code : semesters.keySet()) {
			System.out.println(choiceNum + ". " + semesters.get(code));
			choiceNum++;
		}
		System.out.print("Choose a semester: ");
		int choice = insistInt(input);
		while (choice < 0 || choice >= semesters.keySet().size()) {
			System.out.print("Make a choice from the numbered list: ");
			choice = insistInt(input);
		}
		return choice;
	}

	private static void populateChoices(WebElement dropdown, 
			TreeMap<String, String> semesters, JBrowserDriver driver) {
		boolean cycled = false;
		while (!cycled) {
			String semCode = dropdown.getAttribute("value");
			dropdown.click();
			if (!semesters.containsKey(semCode)) {
				char monthCode = semCode.charAt(semCode.length() - 1);
				String year = semCode.substring(0, semCode.length() - 1);
				String monthVal = "";
				switch (monthCode) {
				case '2': monthVal = "Spring";
				break;
				case '6': monthVal = "Summer";
				break;
				case '9': monthVal = "Fall";
				break;
				default: monthVal = "(Unknown month)";
				break;
				}
				semesters.put(semCode, monthVal + " " + year);
				dropdown.click();
				driver.getKeyboard().sendKeys(Keys.ARROW_DOWN);
			} else {
				cycled = true;
			}
		}
	}

	private static int insistInt(Scanner input) {
		while (!input.hasNextInt()) {
			System.out.print("Make a choice from the numbered list: ");
			input.next();
		}
		return input.nextInt();
	}
}