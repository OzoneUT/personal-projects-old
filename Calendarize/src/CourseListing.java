import java.awt.Color;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// this class maintains a list of Course objects and is responsible for parsing
// the HTML document passed into its constructor and storing the information in
// appropriate Course objects
public class CourseListing {
	
	// container object
	private ArrayList<Course> con;
	private int[] extrema;
	private ArrayList<Color> colors;
	private String semester;
	private String hours;
	
	public CourseListing(File htmlFile) {
		con = new ArrayList<>();
		colors = new ArrayList<>();
		semester = "";
		hours = "";
		initColors();
		storeTable(htmlFile);
	}
	
	private void initColors() {
		try {
			FileReader colorsFile = new FileReader("colorsRef.txt");
			Scanner fileSc = new Scanner(colorsFile);
			while (fileSc.hasNextLine()) {
				String line = fileSc.nextLine();
				Scanner lineSc = new Scanner(line);
				lineSc.next();
				int r = Integer.parseInt(lineSc.next());
				int g = Integer.parseInt(lineSc.next());
				int b = Integer.parseInt(lineSc.next());
				//System.out.println(r + " " + g + " " + b);
				Color temp = new Color(r,g,b);
				colors.add(temp);
				lineSc.close();
			}
			fileSc.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error initializing colors-using gray default");
		}
	}
	
	public void shuffleColors() {
		Collections.shuffle(colors);
	}

	public void add(Course c) {
		if (c == null) {
			throw new IllegalArgumentException("A Course cannot be null.");
		}
		con.add(c);
	}
	
	public Course get(int i) {
		if (i < 0 || i >= con.size())
			throw new IllegalArgumentException("Index out of bounds.");
		return con.get(i);
	}
	
	private void storeTable(File htmlFile) {
		try {
			Document listingDoc = Jsoup.parse(htmlFile, "UTF-8", "");
			//.get(0) necessary because select() returns Elements collection
			Element table = listingDoc.select("table").get(0);
			Elements rows = table.select("tr");
			
			// iterate through the table, skipping the first row of column names
			for (int i = 1; i < rows.size(); i++) {
				Element currentRow = rows.get(i);
				Elements cols = currentRow.select("td");
				Course newCourse = new Course();
				
				newCourse.setUnique(cols.get(0).text());
				newCourse.setCode(cols.get(1).text());
				newCourse.setTitle(cols.get(2).text());
				newCourse.addBldg(cols.get(3).text());
				newCourse.addRoom(cols.get(4).text());
				newCourse.addDay(cols.get(5).text());
				newCourse.addTime(cols.get(6).text());
				newCourse.setRemarks(cols.get(7).text());
				
				newCourse.storeTimesWeekly();
				this.add(newCourse);
			}
			getSemData(listingDoc);
		} catch (IOException e) {
			e.printStackTrace();
		}
		setExtrema();
	}
	
	private void getSemData(Document listingDoc) {
		final String CLASS_LISTING_TEXT = "Class Listing ";
		semester = (listingDoc.getElementById("pgTitle").text().substring(CLASS_LISTING_TEXT.length()));
		semester += " Class Listing";
		Elements temp = listingDoc.getElementsMatchingText("You are registered for");
		hours = (temp.get(temp.size() - 1).text());
	}

	public int[] getExtrema() {
		return extrema;
	}
	
	private void setExtrema() {
		extrema = new int[2];
		int earliestTime = Integer.MAX_VALUE;
	    int latestTime = -1;
		for (Course c : con) {
			if (c.getEarliestStart() < earliestTime)
				earliestTime = c.getEarliestStart();
			if (c.getLatestEnd() > latestTime)
				latestTime = c.getLatestEnd();
		}
		extrema[0] = earliestTime;
		extrema[1] = latestTime;
	}
	
	public void displayAll() {
		for (int i = 0; i < this.con.size(); i++) {
			System.out.println(this.get(i).toString());
		}
	}

	public void assignColors() {
		int colorsIdx = 0;
		for (Course c : con) {
			c.setColor(colors.get(colorsIdx));
			colorsIdx++;
			if (colorsIdx >= colors.size()) 
				colorsIdx = 0;
		}	
	}
	
	public int size() {
		return con.size();
	}

	public String getSemester() {
		return semester;
	}

	public String getHours() {
		return hours;
	}
}