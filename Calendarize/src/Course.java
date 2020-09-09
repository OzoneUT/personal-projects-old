import java.awt.Color;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

// this class parses and stores individual course information in a usable way
public class Course {
	
	// fields to store course data
	private String unique;
	private String code;
	private String title;
	private String remarks;
	private Color color;
	
	// for elements with <br> tags, each will be a separate string
	private ArrayList<String> bldgs;
	private ArrayList<String> rooms;
	private ArrayList<String> days;
	private ArrayList<String> times;
	
	// days and times will be consolidated into two maps for organized access
	private HashMap<String, Integer> startTimesWeekly;

	private HashMap<String, Integer> endTimesWeekly;
	private int earliestStart;
	private int latestEnd;
	
	public Course() {
		unique = "";
		code = "";
		title = "";
		bldgs = new ArrayList<>();
		rooms = new ArrayList<>();
		days = new ArrayList<>();
		times = new ArrayList<>();
		remarks = "";
		setColor(Color.lightGray);
		
		startTimesWeekly = new HashMap<>();
		endTimesWeekly = new HashMap<>();
		earliestStart = Integer.MAX_VALUE;
		latestEnd = -1;
	}
	
	public HashMap<String, Integer> getStartTimesWeekly() {
		return startTimesWeekly;
	}
	
	public HashMap<String, Integer> getEndTimesWeekly() {
		return endTimesWeekly;
	}

	public int getLatestEnd() {
		return latestEnd;
	}

	public void setLatestEnd(int latestEnd) {
		this.latestEnd = latestEnd;
	}

	public int getEarliestStart() {
		return earliestStart;
	}

	public void setEarliestStart(int earliestStart) {
		this.earliestStart = earliestStart;
	}

	public String getUnique() {
		return unique;
	}

	public void setUnique(String unique) {
		this.unique = unique;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ArrayList<String> getBldgs() {
		return bldgs;
	}

	public void addBldg(String bldg) {
		Scanner sc = new Scanner(bldg);
		while (sc.hasNext()) {
			bldgs.add(sc.next());
		}
		sc.close();
	}

	public ArrayList<String> getRooms() {
		return rooms;
	}

	public void addRoom(String room) {
		Scanner sc = new Scanner(room);
		while (sc.hasNext()) {
			rooms.add(sc.next());
		}
		sc.close();
	}

	public ArrayList<String> getDays() {
		return days;
	}

	// the extra code ensures any abbreviation for Thursday as TH is stored as
	// H, then rebuilds and adds the 'day' string to list of days
	public void addDay(String day) {
		Scanner sc = new Scanner(day);
		while (sc.hasNext()) {
			String dayGroup = sc.next().toUpperCase();
			char[] dayGroupChar = dayGroup.toCharArray();
			String fixedDayGroup = "";
			for (int i = dayGroupChar.length - 1; i >= 0 ; i--) {
				if (dayGroupChar[i] == 'H') {
					dayGroupChar[i - 1] = '*';
				}
			}
			for (char c : dayGroupChar) {
				if (c != '*') {
					fixedDayGroup += c;
				}
			}
			days.add(fixedDayGroup);
		}
		sc.close();
	}

	public ArrayList<String> getTimes() {
		return times;
	}

	public void addTime(String time) {
		times.add(time);
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}
	
	public void storeTimesWeekly() {
		fixTimeBlocks();
		convertTimes();
		fillWeeklyMaps();
	}
	
	private void fillWeeklyMaps() {
		Scanner sc = new Scanner("");
		for (int dayTimeNum = 0; dayTimeNum < days.size(); dayTimeNum++) {
			// get start and end times separately as integers
			String rangeStr = times.get(dayTimeNum);
			sc = new Scanner(rangeStr);
			sc.useDelimiter("-");
			int startTime = Integer.parseInt(sc.next());
			sc.skip("-");
			int endTime = Integer.parseInt(sc.nextLine());
			
			if (startTime < earliestStart)
				earliestStart = startTime;
			if (endTime > latestEnd)
				latestEnd = endTime;
			
			System.out.println(earliestStart + " " + latestEnd);

			// for all days in the corresponding dayGroup, store the pair
			String dayGroup = days.get(dayTimeNum);
			for (int dayIdx = 0; dayIdx < dayGroup.length(); dayIdx++) {
				String dayOfTheWeek = dayGroup.charAt(dayIdx) + "";
				startTimesWeekly.put(dayOfTheWeek, startTime);
				endTimesWeekly.put(dayOfTheWeek, endTime);
			}
		}
		sc.close();
		// System.out.println(code + ": " + startTimesWeekly.toString() + endTimesWeekly.toString());
	}

	private void convertTimes() {
		Scanner rawSc = new Scanner("");
		// includes every time range now fixed to use with a scanner
		for (int i = 0; i < times.size(); i++) {
			String rawRange = times.get(i);
			rawSc = new Scanner(rawRange);
			rawSc.useDelimiter("-");
			String milStart = formatTime(rawSc.next());
			rawSc.skip("-");
			String milEnd = formatTime(rawSc.nextLine());
			times.set(i, milStart + "-" + milEnd); 
		}
		rawSc.close();
	}

private String formatTime(String t) {
		String timeStr = t.toUpperCase();
		// delete trailing white space
		if (timeStr.charAt(timeStr.length() - 1) == ' ') {
			timeStr = timeStr.substring(0, timeStr.length() - 2);
		}
		// to perserve format hh:mma, add a 0 in front if necessary
		if (timeStr.length() != 7) {
			timeStr = '0' + timeStr;
		}
		// 'h' is clock hour of am and pm (1-12) and 'H' is 0-23; 'a' for am/pm
		DateTimeFormatter stdFormatter = DateTimeFormatter.ofPattern("hh:mma");
		LocalTime time = LocalTime.parse(timeStr, stdFormatter);
		DateTimeFormatter milFormatter = DateTimeFormatter.ofPattern("HHmm");
		String formattedTime = time.format(milFormatter);
		return formattedTime;
	}

	private void fixTimeBlocks() {
		String rawTimes = times.get(0);
		Scanner rawSc = new Scanner(rawTimes);
		times.clear();
		while (rawSc.hasNext()) {
			String tempFixed = rawSc.next();
			if (tempFixed.charAt(tempFixed.length() - 1) == '-') {
				// the next token is a part of the previous string
				tempFixed += rawSc.next();
			}
			// in both cases (space or no space in block), should be fixed
			times.add(tempFixed);
		}
		rawSc.close();
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Unique=" + unique);
		sb.append(" Course code=" + code);
		sb.append(" Title=" + title);
		sb.append(" Bldgs=" + bldgs.toString());
		sb.append(" Room=" + rooms.toString());
		sb.append(" Days=" + days.toString());
		sb.append(" Time=" + times.toString());
		sb.append(" Remarks=" + remarks);
		sb.append(" ColorRBG=" + color.getRGB());
		return sb.toString();
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
}