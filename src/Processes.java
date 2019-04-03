import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadLocalRandom;

public class Processes extends Thread{

	private String procID;
	private int startTime;
	private int timeDuration;
	private int waitStart;
	private int waitDuration;
	private boolean finished;
	private Semaphore sem;
	private MemoryManager memory;
	private static ArrayList<Store> storeComList = new ArrayList<Store>();


	public Processes(String pid ,int st, int td,Semaphore semaphore) {
		this.procID = pid;
		this.startTime = st;
		this.timeDuration = td;
		this.sem = semaphore;
		this.finished = false;
		this.waitStart = 0;
		this.waitDuration = 0;
		this.memory = MemoryManager.getInstance();

	}
	
	public void printProcesses(){
		
        String st = Integer.toString(this.startTime);
        String td = Integer.toString(this.timeDuration);
        System.out.println("Process " + this.procID + ", Start Time:" + st + ", Duration Time:" + td);
    }

    @Override
	public void run(){

		try {
			System.out.println("clock: " + Clock.clock + ", Process " + this.procID + " Started");

			while (!finished) {

				if (!Clock.ready[Integer.parseInt(this.getProcID()) - 1]) {
					if (Clock.clock >= waitStart + waitDuration) {

							sem.acquire();
							pickNextCommand();
							waitRandomTime();
							sem.release();

					}

					sem.acquire();
					Clock.ready[Integer.parseInt(this.getProcID()) - 1] = true;
					sem.release();

				}

			}
		}catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public void finish(){
		System.out.println("clock: " + Clock.clock + ", Process " + this.procID + " Finished");
		this.finished = true;
		//this.stop();
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

	public void setStoreComList(ArrayList<Store> storeComList) {
		this.storeComList = storeComList;
	}


	public void pickNextCommand(){
		if (storeComList.size() > 0 && Clock.clock != this.startTime) {
			Store st = storeComList.remove(0);
			memory.store(st.getVarID(), st.getValue(), Integer.parseInt(this.procID));
		}

	}
	public void waitRandomTime(){
		int randomInt = ThreadLocalRandom.current().nextInt(10, 1000);
		this.waitStart = Clock.clock;
		this.waitDuration = randomInt;
		int total = waitStart + waitDuration;
		//System.out.println("Random number generated is : " + randomInt+ " wakeup: " + total + " P" + this.procID);
	}
}
