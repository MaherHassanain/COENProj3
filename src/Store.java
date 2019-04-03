
public class Store extends Commands{

	private int varID;
	private int value;
	
	public Store(String com,int id1,int val, int id) {
		super(com, id);
		this.varID = id1;
		this.value = val;
	}
	
	public void printStoredCommands(){
			
	        String st = Integer.toString(this.varID);
	        String td = Integer.toString(this.value);
	        System.out.println("Command " + super.getComID() +",  Order # " + super.getID() + ", var: "+ st + ", Value " + td);
	    }
	
	
	public String getStoredID() {
		return super.getComID();
	}

	public int getVarID() {
		return varID;
	}
	
	public int getValue() {
		return value;
	}
}
