
public class Release extends Commands{

	private int varID;
	
	public Release(String com, int id, int id1) {
		super(com, id);
		this.varID = id1;
	}
	
	public void printReleaseCommands(){
		
        String st = Integer.toString(this.varID);
        System.out.println("Command " + super.getComID() + ", Order # " + super.getID() +  ", var : " + st);
    }

	public String getReleaseID() {
		return super.getComID();
	}
	
	public int getVarID() {
		return varID;
	}
}
