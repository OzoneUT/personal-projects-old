/* A contact object stores information for one specific entry created for
 * the address book. The information stored can always be changed from
 * the main program. The information stored will be the following:
 * Name, a collection of phone numbers with label, a collection of emails with 
 * labels, physical address, notes, nickname, relationship. 
 */
public class Contact implements Comparable{
	
	private String name;
	private String nickname;
	private String relationship;
	private String phoneNumber;
	private String email;
	private String address;
	private String notes;
	
	public Contact() {
		name = "";
		nickname = "";
		relationship = "";
		phoneNumber = "";
		email = "";
		address = "";
		notes = "";
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String newName) {
		name = newName;
	}
	
	public String getNickname() {
		return nickname;
	}
	
	public void setNickname(String newNickname) {
		nickname = newNickname;
	}
	
	public String getRelationship() {
		return relationship;
	}
	
	public void setRelationship(String newRel) {
		relationship = newRel;
	}
	
	public String getPhoneNumber() {
		return phoneNumber;
	}
	
	public void setPhoneNumber(String num) {
		phoneNumber = num;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String newEmail) {
		email = newEmail;
	}
	
	public String getAddress() {
		return address;
	}
	
	public void setAddress(String newAddress) {
		address = newAddress;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String newNotes) {
		notes = newNotes;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("\nName=");
		sb.append(name);
		sb.append("\nNickname=");
		sb.append(nickname);
		sb.append("\nRelationship=");
		sb.append(relationship);
		sb.append("\nNumber=");
		sb.append(phoneNumber);
		sb.append("\nEmail=");
		sb.append(email);
		sb.append("\nAddress=");
		sb.append(address);
		sb.append("\nNotes=");
		sb.append(notes);
		return sb.toString();
	}
	
	public String display() {
		StringBuilder sb = new StringBuilder();
		sb.append("\n");
		sb.append(name);
		sb.append("\nNickname: ");
		sb.append(nickname);
		sb.append("\nRelationship: ");
		sb.append(relationship);
		sb.append("\nNumber: ");
		sb.append(phoneNumber);
		sb.append("\nEmail: ");
		sb.append(email);
		sb.append("\nAddress: ");
		sb.append(address);
		sb.append("\nNotes: ");
		sb.append(notes);
		return sb.toString();
	}

	@Override
	public int compareTo(Object o) {
		int val = this.name.compareTo(((Contact)o).name);
		if (val == 0)
			val = this.nickname.compareTo(((Contact)o).nickname);
		return val;
	}
}
