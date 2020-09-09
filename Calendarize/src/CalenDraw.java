import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CalenDraw extends Component{
	
	// constants and fields for WeekChart generation
	private static final String[] DAYS_OF_THE_WEEK = {"MONDAY", "TUESDAY", 
			"WEDNESDAY", "THURSDAY", "FRIDAY"};
	private static final String[] DAYS_ABBREV = {"M", "T", "W", "H", "F"};
	private static Font rboThin;
	private static Font rboRegular;
	private static Font rboBold;
	private static Font rboLight;
	private JFrame frame;
	
	private static int startTimeRange;
	private static int endTimeRange;
	private static final int CELL_WIDTH = 230;
	private static final int CELL_HEIGHT = 40;
	private static final int START_X = 90;
	private static final int START_Y = 175;
	
	private static CourseListing storedList;
	
	public CourseListing getCourseList() {
		return storedList;
	}
	
	public void paint(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
			     RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		makeEmptyWeek(g2d);
		drawCourseTimes(g2d);
		displayDetails(g2d); 
//		BufferedImage bi = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
//		Graphics2D graphics = bi.createGraphics();
//		frame.print(graphics);
//		graphics.dispose();
	}

	private void displayDetails(Graphics2D g2d) {
		g2d.setFont(rboRegular.deriveFont(30f));
		g2d.setColor(Color.BLACK);
		int y = START_Y - g2d.getFontMetrics().getHeight() - 90;
		g2d.drawString(storedList.getSemester(), START_X - 60, y);
		g2d.drawLine(START_X - 60, y + 7, this.getWidth() - 50, y + 7);
		int bottomOfTable = convTimeToY(endTimeRange) + 25;
		g2d.setFont(rboBold.deriveFont(16f));
		int x = START_X + (5 * CELL_WIDTH / 2) - g2d.getFontMetrics().stringWidth(storedList.getHours()) / 2;
		g2d.drawString(storedList.getHours(), x, bottomOfTable);
		
		// list the classes in a two column format with necessary details and the color
		detailsHelper(bottomOfTable + 40, g2d);
	}

	private void detailsHelper(int initY, Graphics2D g2d) {
		g2d.setFont(rboRegular.deriveFont(14f));
		final int COL_1 = START_X + 2*CELL_WIDTH / 3;
		final int COL_2 = COL_1 + 2 * CELL_WIDTH;
		int halfElem = storedList.size() / 2;
		if (storedList.size() % 2 == 1) {
			halfElem++;
		}
		// col 1 is the larger column in all situations...
		populateColumn(0, halfElem, g2d, COL_1, initY);
		populateColumn(halfElem, storedList.size(), g2d, COL_2, initY);
	}

	private void populateColumn(int start, int end, Graphics2D g2d, int col, int initY) {
		Point drawPos = new Point(col, initY);
		for (int i = start; i < end; i++) {
			Course c = storedList.get(i);
			g2d.setColor(c.getColor());
			g2d.fillRect(drawPos.x, drawPos.y - 13, 14, 14);
			drawPos.x += 20;
			g2d.setColor(Color.BLACK);
			g2d.drawString(c.getCode() + " (" + c.getUnique() + ") " + c.getTitle(), drawPos.x, drawPos.y);
			drawPos.x = col;
			drawPos.y += 20;
		}
		System.out.println(drawPos.y);
	}

	private void drawCourseTimes(Graphics2D g2d) {
		// iterate through each course
		for (int i = 0; i < storedList.size(); i++) {
			Course curr = storedList.get(i);
			Color currColor = curr.getColor();
			String currName = curr.getCode();
			HashMap<String, Integer> startTimesWeekly = curr.getStartTimesWeekly();
			HashMap<String, Integer> endTimesWeekly = curr.getEndTimesWeekly();
			// iterate through each startTime for a given week
			for (String day : startTimesWeekly.keySet()) {
				int currStartTime = startTimesWeekly.get(day);
				int currEndTime = endTimesWeekly.get(day);
				String location = getLocation(day, curr);
				//System.out.println(currName + " " + day + " " + location + " " + " Starts: " + currStartTime + " Ends: " + currEndTime);
				// getXpos from day of the week
				int xPos = getXPos(day);
				
				// getYpos from currStartTime
				int yPosStart = convTimeToY(currStartTime);
				// getYpos from currEndTime
				int yPosEnd = convTimeToY(currEndTime);
				
				// draw the course
				g2d.setColor(currColor);
				g2d.fillRect(xPos, yPosStart, CELL_WIDTH, yPosEnd - yPosStart);
				drawLabel(g2d, xPos, yPosStart, yPosEnd, currName + " - " + location);
			}
		}
	}

	private void drawLabel(Graphics2D g2d, int xPos, int yPosStart, int yPosEnd, String l) {
		String label = fixLabel(l);
		g2d.setColor(Color.WHITE);
		g2d.setFont(rboBold.deriveFont(16f));
		int strWidth = g2d.getFontMetrics().stringWidth(label);
		int offset = (CELL_WIDTH - strWidth) / 2;
		g2d.drawString(label, xPos + offset, 6 + yPosStart + ((yPosEnd - yPosStart) / 2));
	}

	private String fixLabel(String l) {
		int endIdx = -1;
		for (int i = l.length() - 1; i >= 0; i--) {
			if (l.charAt(i) == ' ' || l.charAt(i) == '-') {
				endIdx = i;
			} else {
				break;
			}
		}
		if (endIdx == -1) {
			return l;
		} else {
			return l.substring(0, endIdx);
		}
	}

	// converts a given military time to its corresponding y position
	private int convTimeToY(int currStartTime) {
		int yPos = START_Y;
		int minutes = currStartTime % 100;
		int evenHour = currStartTime / 100;
		yPos += (evenHour - (startTimeRange / 100)) * CELL_HEIGHT;
		Double minutesToY = CELL_HEIGHT * (minutes / 60.0);
		yPos += minutesToY.intValue();
		return yPos;
	}

	// get the x position of the pen given the day of the week
	private int getXPos(String day) {
		int result = 0;
		for (int i = 0; i < DAYS_ABBREV.length; i++) {
			if (day.equals(DAYS_ABBREV[i])) {
				result = START_X + (i * CELL_WIDTH);
				break;
			}
		}
		return result;
	}

	// returns a string of the building and room given the day of the course
	private String getLocation(String day, Course curr) {
		String result = "";
		int idx = 0;
		boolean found = false;
		while (idx < curr.getDays().size() && !found) {
			String dayGroup = curr.getDays().get(idx);
			if (dayGroup.contains(day)) {
				found = true;
				idx--;
			}
			idx++;
		}
		if (curr.getBldgs().size() > 0 && curr.getRooms().size() > 0) {
			result = curr.getBldgs().get(idx) + " " + curr.getRooms().get(idx);
		} else {
			result = "";
		}
		return result;
	}

	// draws a skeleton schedule with default times
	private void makeEmptyWeek(Graphics2D g2d) {
		Point drawPos = new Point(START_X, START_Y);
		final Color WHITE_GRAY = new Color(245,245,245);
		int rowsNum = (endTimeRange - startTimeRange) / 100;
		for (int row = 0; row < rowsNum; row++) {
			for (int col = 0; col < 5; col++) {
				if (row % 2 == 0)
					g2d.setColor(Color.WHITE);
				else
					g2d.setColor(WHITE_GRAY);
				g2d.fillRect(drawPos.x, drawPos.y, CELL_WIDTH, CELL_HEIGHT);
				g2d.setColor(Color.LIGHT_GRAY);
				g2d.drawRect(drawPos.x, drawPos.y, CELL_WIDTH, CELL_HEIGHT);
				drawPos.x = drawPos.x + CELL_WIDTH;
			}
			drawPos.x = START_X;
			drawPos.y = drawPos.y + CELL_HEIGHT;
		}
		writeDays(g2d, drawPos);
		writeTimeRange(g2d, drawPos);
	}

	// correctly format every hour in the time range inclusive if necessary, 
	// then write to image
	// Assuming military time is at least of length 3/4 (e.g. 0030 may break 
	// the code, but is not expected to appear in a class schedule)
	private void writeTimeRange(Graphics2D g2d, Point drawPos) {
		drawPos.move(START_X - 55, START_Y + 5);
		g2d.setFont(rboLight.deriveFont(13f));
		DateTimeFormatter milFormatter = DateTimeFormatter.ofPattern("HHmm");
		DateTimeFormatter stdFormatter = DateTimeFormatter.ofPattern("hh:mm a");
		for (int milTime = startTimeRange; milTime <= endTimeRange; milTime += 100) {
			String milTimeFmt = milTime + "";
			if (milTimeFmt.length() == 3) {
				milTimeFmt = '0' + milTimeFmt;
			}
			LocalTime time = LocalTime.parse(milTimeFmt, milFormatter);
			String formattedTime = time.format(stdFormatter);
			if (formattedTime.charAt(0) == '0') {
				formattedTime = formattedTime.substring(1);
				// formattedTime = " " + formattedTime;
			}
			int offset = 0;
			if (formattedTime.length() == 8) {
				offset = 4;
			}
			formattedTime = String.format("%8s", formattedTime);
			g2d.drawString(formattedTime, drawPos.x - offset, drawPos.y);
			drawPos.y += CELL_HEIGHT;
		}
	}

	// write out each day of the week above the table, centering above each
	// column using a calculated offset
	private void writeDays(Graphics2D g2d, Point drawPos) {
		drawPos.move(START_X, START_Y - 30);
		g2d.setColor(Color.BLACK);
		g2d.setFont(rboThin.deriveFont(30f));
		for (int i = 0; i < 5; i++) {
			String day = DAYS_OF_THE_WEEK[i];
			drawPos.x = START_X + (i * CELL_WIDTH);
			int strWidth = g2d.getFontMetrics().stringWidth(day);
			int offset = (CELL_WIDTH - strWidth) / 2;
			g2d.drawString(day, drawPos.x + offset, drawPos.y);
		}
	}

	public void init(CourseListing list) {
		frame = new JFrame("Calendarize - Your Courses");
		registerFonts();
		storedList = list;
		determineTimeRange();
		frame.setSize(1300, 900);
		frame.add("Center", new CalenDraw());
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
	}

	// appropriately change the time range if earliestTime is before the 
	// startTime or latestTime is after the endTime
	// also, round starting time down and ending time up
	private static void determineTimeRange() {
		startTimeRange = 800;
		endTimeRange = 1700;
		int[] extrema = storedList.getExtrema();
		if (extrema[0] < startTimeRange) {
			startTimeRange = extrema[0];
		}
		if (extrema[1] > endTimeRange) {
			endTimeRange = extrema[1];
		}
		startTimeRange -= startTimeRange % 100;
		if (endTimeRange % 100 != 0) {
			endTimeRange += 100 - (startTimeRange % 100);
		}
	}

	private static void registerFonts() {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		try {
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts\\Roboto-Thin.ttf")));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts\\Roboto-Regular.ttf")));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts\\Roboto-Light.ttf")));
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("fonts\\Roboto-Bold.ttf")));

		} catch (FontFormatException | IOException e) {
			System.out.println("Fatal error: font file missing");
			e.printStackTrace();
		}
		Font[] fonts = ge.getAllFonts();
		int numReg = 0;
		for (int i = fonts.length - 1; i >= 0; i--) {
			Font font = fonts[i];
			String fontName = font.getFontName();
			switch (fontName) {
			case "Roboto": rboRegular = font;
			numReg++;
			break;
			case "Roboto Light": rboLight = font;
			numReg++;
			break;
			case "Roboto Bold": rboBold = font;
			numReg++;
			break;
			case "Roboto Thin": rboThin = font;
			numReg++;
			break;
			}
			if (numReg == 4) {
				break;
			}
		}
		
	}
	
}