import java.util.*;
import java.util.concurrent.Semaphore;


public class Scheduler extends Thread{

    private ArrayList<Processes> procList;
    private Semaphore sem;
    private MemoryManager memory;
    private static ArrayList<Commands> comList;
    private int memsize;


    public Scheduler(ArrayList<Processes> pl,int msize, Semaphore sema){
        this.procList = pl;
        this.sem = sema;
        this.memsize = msize;
    }

    public void run(){

        // start memory manager
        memory = MemoryManager.getInstance();
        // set memory size;
        memory.setMemorySize(memsize);
        memory.setPhysicalMemory();
        memory.start();

        boolean wait =true;
        int total = procList.size();
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        while(Clock.clock < 10000){

            try {
                sem.acquire();
                for(int i = 0;i<total;i++){
                    Clock.ready[i] = true;  // set ready flags for all proceses to true
                }
                sem.release();

                stopProcess();      //stop processes
                startProcess();     // start processes
                updateReady();      // update ready flags based on if thread is alive

                wait = true;
                while(wait){    // loop until all processes are ready
                    boolean containsFalse = false;
                    for(int i=0;i<total;i++){
                        if(Clock.ready[i] == false){
                            containsFalse = true;
                        }
                    }
                    if(!containsFalse){
                        wait = false;
                    }
                }

                    sem.acquire();
                    Clock.clock += 50;      // increment clock
                    sem.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

        }

        memory.memoryDump();
        memory.setFinished(true);

    }
    public void startProcess(){

        for(int i=0;i<procList.size();i++){
            int time = procList.get(i).getStartTime();
            if(Clock.clock == time){
                try {
                    sem.acquire();
                    //Clock.ready[Integer.parseInt(procList.get(i).getProcID()) -1] = false;
                    sem.release();
                    procList.get(i).setCommandList(comList);
                    procList.get(i).start();
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }

    }
    public void stopProcess(){

        for(int i=0;i<procList.size();i++){
            int stime = procList.get(i).getStartTime();
            int dtime = procList.get(i).getTimeDuration();
            if(Clock.clock >= stime + dtime){
                procList.get(i).finish();
                //procList.get(i).join();
                try {
                    Thread.sleep(1);
                    //procList.get(i).join();
                    sem.acquire();
                    Clock.ready[Integer.parseInt(procList.get(i).getProcID())-1] = true;
                    procList.remove(i);
                    i--;
                    sem.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    public void updateReady(){
        for(int i=0;i<procList.size();i++){
            if(procList.get(i).isAlive()){
                try {
                    sem.acquire();
                    Clock.ready[Integer.parseInt(procList.get(i).getProcID())-1] = false;
                    sem.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }



    public void setCommandList(ArrayList<Commands> commandList) {
        this.comList = commandList;
    }
}

