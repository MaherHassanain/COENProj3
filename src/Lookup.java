
public class Lookup extends Commands{

	private int varID;
	
	public Lookup(String com, int id, int id1) {
		super(com, id);
		this.varID = id1;
	}
	
	public void printLookupCommands(){
		
        String st = Integer.toString(this.varID);
        System.out.println("Command " + super.getComID() + ", Order # " + super.getID() +  ", var : " + st);
    }

	public String getLookupID() {
		return super.getComID();
	}
	
	public int getVarID() {
		return varID;
	}
	

}
