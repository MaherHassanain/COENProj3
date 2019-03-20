
public class Processes {

	private String procID;
	private int startTime;
	private int timeDuration;
	
	public Processes(String pid ,int st, int td) {
		this.procID = pid;
		this.startTime = st;
		this.timeDuration = td;
	}
	
	public void printProcesses(){
		
        String st = Integer.toString(this.startTime);
        String td = Integer.toString(this.timeDuration);
        System.out.println("Process " + this.procID + ", Start Time:" + st + ", Duration Time:" + td);
    }
	
	public String getProcID() {
		return procID;
	}
	
	public int getStartTime() {
		return startTime;
	}
	
	public int getTimeDuration() {
		return timeDuration;
	}
}
