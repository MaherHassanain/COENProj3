
public class Commands {

	private String commandID;
	private int ID;
	
	public Commands(String com, int id) {
		this.commandID = com;
		this.ID = id;
	}
	
	public String getComID() {
		return commandID;
	}
	
	public int getID() {
		return ID++;
	}
}