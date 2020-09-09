import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.TreeSet;

public class Manager {
	
	// constants for menu options
	final static String ADD = "add";
	final static String REMOVE = "remove";
	final static String EDIT = "edit";
	final static String DISPLAY = "display";
	final static String EXIT = "exit";
	
	public static void main(String[] args) {
		System.out.println("This program helps in managing an address book.");
		System.out.println("It can perform tasks such as adding a contact, removing a contact, and editing one or more aspects of an existing entry.");
		Scanner sc = new Scanner(System.in);
		TreeSet<Contact> directory = new TreeSet<>();
		File dirFile = new File("directory.txt");
		
		init(directory, dirFile);
		while (true) {
			System.out.println("Choose from the following options: \n");
			System.out.println("ADD additional contacts");
			System.out.println("REMOVE contacts");
			System.out.println("EDIT a contact's information");
			System.out.println("DISPLAY the entire address book");
			System.out.println("Save and EXIT\n ");
			
			String menuOpt = sc.nextLine().toLowerCase();
			switch (menuOpt) {
				case ADD: addContact(sc, directory);
				break;
				case REMOVE: removeContact(sc, directory);
				break;
				case EDIT: editContact(sc, directory);
				break;
				case DISPLAY: System.out.println(directory.size() + " contact(s) are stored:");
				showNames(directory, true);
				System.out.println("\nEnd of address book.");
				break;
				case EXIT: System.out.println("Saving and exiting...");
				save(directory);
				return;
				default: System.out.println("Invalid command!");
				break;
			}
		}
	}
	
	private static void init(TreeSet<Contact> directory, File dirFile) {
		//if a previous directory.txt exists, load from it and delete the file
		if (dirFile.isFile()) {
			System.out.println("Opening a saved address book...\n");
			try {
				Scanner sc = new Scanner(dirFile);
				Scanner lineSc;
				Contact current = null;
				while (sc.hasNextLine()) {
					String line = sc.nextLine();
					lineSc = new Scanner(line);
					lineSc.useDelimiter("=");
					if (lineSc.hasNext()) {
						String fieldToken = lineSc.next();
						lineSc.skip("="); // skips the delimiter symbol after token
						if (fieldToken.equals("Name")) {
							if (current != null) {
								directory.add(current);
							}
							current = new Contact();
							current.setName(lineSc.nextLine());
						} else {
							String fieldData = lineSc.nextLine();
							switch (fieldToken) {
								case "Nickname": current.setNickname(fieldData);
								break;
								case "Relationship": current.setRelationship(fieldData);
								break;
								case "Number": current.setPhoneNumber(fieldData);
								break;
								case "Email": current.setEmail(fieldData);
								break;
								case "Address": current.setAddress(fieldData);
								break;
								case "Notes": current.setNotes(fieldData);
								break;
								default: System.out.println("Warning: " + current.getName() + " had unexpected data.");
								break;
							}
						}
					}
				}
				// add last contact since no new names will be found
				directory.add(current);
				sc.close();
			} catch (FileNotFoundException e) {
				System.out.println("UNEXPECTED: File not found");
			}
		}
	}

	private static void editContact(Scanner sc, TreeSet<Contact> directory) {
		showNames(directory, false);
		System.out.println("Which contact would you like to edit?");
		String editAns = sc.nextLine();
		Contact toEdit = findContact(directory, editAns);
		if (toEdit == null) {
			System.out.println("This contact does not exist. Returning to Main Menu...");
			return;
		}
		// if here, contact was found
		System.out.println(toEdit.toString());
		System.out.print("\nWhat would you like to edit? ");
		editAns = sc.nextLine().toLowerCase();
		switch (editAns) {
			case "name": System.out.print("Change name to: ");
			toEdit.setName(sc.nextLine());
			System.out.println("Success.");
			break;
			case "nickname": System.out.print("Change nickname to: ");
			toEdit.setNickname(sc.nextLine());
			System.out.println("Success.");
			break;
			case "relationship": System.out.print("Change relationship to: ");
			toEdit.setRelationship(sc.nextLine());
			System.out.println("Success.");
			break;
			case "number": System.out.print("Change phone number to: ");
			toEdit.setPhoneNumber(sc.nextLine());
			System.out.println("Success.");
			break;
			case "email": System.out.print("Change email address to: ");
			toEdit.setEmail(sc.nextLine());
			System.out.println("Success.");
			break;
			case "address": System.out.println("Change address to: ");
			toEdit.setAddress(sc.nextLine());
			System.out.println("Success.");
			break;
			case "notes": notesHelper(sc, toEdit);
			break;
			default: System.out.println("Error: Field not found! Nothing was changed.");
			break;
		}
	}
	
	public static void notesHelper(Scanner sc, Contact toEdit) {
		System.out.print("New notes: ");
		String newNotes = sc.nextLine();
		System.out.print("Would you like to ADD to or REPLACE existing notes? ");
		String notesAns = sc.nextLine().toLowerCase();
		if (notesAns.equals("replace")) {
			toEdit.setNotes(newNotes);
			System.out.println("Replaced notes successfully.");
		} else if (notesAns.equals("add")){
			toEdit.setNotes(toEdit.getNotes() + "; " + newNotes);
			System.out.println("Added notes successfully.");
		} else {
			toEdit.setNotes(toEdit.getNotes() + "; " + newNotes);
			System.out.println("Unrecognized command: Added to existing notes as default behavior.");
		}
	}

	private static void removeContact(Scanner sc, TreeSet<Contact> directory) {
		boolean keepRemoving = true;
		showNames(directory, false);
		while (keepRemoving) {
			System.out.print("Which contact should be removed? ");
			String nameToRemove = sc.nextLine();
			Contact toRemove = findContact(directory, nameToRemove);
			if (toRemove != null) {
				directory.remove(toRemove);
				System.out.println(nameToRemove + " has been removed from the address book.");
			} else {
				System.out.println(nameToRemove + " could not be found.");
			}
			System.out.print("Keep removing? (y/n): ");
			String keepRemovingAns = sc.nextLine().toLowerCase();
			if (!keepRemovingAns.equals("y")) {
				keepRemoving = false;
			}
		}
	}

	private static Contact findContact(TreeSet<Contact> directory, String nameToFind) {
		Contact toFind = null;
		for (Contact c : directory) {
			if (c.getName().equals(nameToFind)) {
				toFind = c;
				return toFind;
			}
		}
		return toFind;
	}

	private static void showNames(TreeSet<Contact> directory, boolean verbose) {
		for (Contact c : directory) {
			if (verbose) {
				System.out.println(c.display());
			} else {
				System.out.println(c.getName());	
			}
		}
	}

	private static void addContact(Scanner sc, TreeSet<Contact> directory) {
		System.out.println("\n-----");
		Contact entry = new Contact();
		System.out.print("Name: ");
		entry.setName(sc.nextLine());
		System.out.print("Nickname: ");
		entry.setNickname(sc.nextLine());
		System.out.print("Relationship: ");
		entry.setRelationship(sc.nextLine());
		System.out.print("Phone number: ");
		entry.setPhoneNumber(sc.nextLine());
		System.out.print("Email address: ");
		entry.setEmail(sc.nextLine());
		System.out.print("Address: ");
		entry.setAddress(sc.nextLine());
		System.out.print("Notes: ");
		entry.setNotes(sc.nextLine());
		directory.add(entry);

		System.out.println("\n" + entry.getName() + " was added to the address book.");
	}
	
	private static void save(TreeSet<Contact> directory) {
		if (directory.size() > 0) {
			File newDirFile = new File("directory.txt");
			try {
				newDirFile.createNewFile();
			} catch (IOException e) {
				System.out.println("Failed to create file!");			
			}
			//file is there
			try {
				PrintWriter writer = new PrintWriter(newDirFile);
				for (Contact c : directory) {
					writer.println(c.toString());
				}
				writer.close();
			} catch (FileNotFoundException e) {
				System.out.println("UNEXPECTED ERROR: The file does not exist");
			}
		}
	}
}

