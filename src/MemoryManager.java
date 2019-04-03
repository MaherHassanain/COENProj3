import java.util.ArrayList;
import java.io.*;

public class MemoryManager extends Thread {

    private int memorySize;
    private boolean finished;
    private static MemoryManager instance;
    private int[][] physicalMemory;
    private ArrayList<PageEntry> pages;



    private MemoryManager(){

        this.memorySize = 2;
        this.finished = false;
        this.physicalMemory = new int[2][2];
        this.pages = new ArrayList<PageEntry>();
    }

    public static MemoryManager getInstance(){
        if (instance == null)
        {
            //synchronized block to remove overhead
            synchronized (MemoryManager.class)
           {
                if(instance==null)
                {
                    // if instance is null, initialize
                    instance = new MemoryManager();
                }

            }
        }
        return instance;
    }
    public void setMemorySize(int size){
        this.memorySize = size;
    }
    public void setPhysicalMemory(){
        this.physicalMemory = new int[memorySize][2];
    }
    public void setFinished(boolean fin){
        this.finished = fin;
    }

    public void run(){
        System.out.println("clock: " + Clock.clock + ", Memory Manager"  + " Started");

        while (!finished) {

        }
    }

    public void pause(){
        System.out.println(", memory manager"  + " Paused");
        //this.suspend();
    }
    public void finish(){
        System.out.println( ", memory manager"  + " Finished ");
        //this.stop();
    }
    public void printMemSize(){
        System.out.println("Memory Size: " + memorySize);
    }
    public void store(int id, int value,int pid){
        //int varid = Integer.parseInt(id);
        int varid = id;
        //if list is less than the memory size just add it directly
        int pagenum = findPageNumber(id);
        if(pages.size() < this.memorySize) {
            //System.out.println("Pages Size" + pages);
            physicalMemory[pages.size()][0] = varid;
            physicalMemory[pages.size()][1] = value;
            pages.add(new PageEntry(varid, pages.size(), Clock.clock));
            System.out.println("clock: " + Clock.clock +", Process " + pid + " Store: Variable " + varid + ", Value: " + value);
        }else if(pagenum < 0){
            pages.add(new PageEntry(varid,pages.size(),Clock.clock));
            System.out.println("clock: " + Clock.clock +", Process " + pid + " Store: Variable " + varid + ", Value: " + value);
            write(id + " " + value);
        }else{
            //overwrite previous value
            PageEntry tempPage = pages.get(pagenum); // pagenum corresponds to array index
            if(pagenum < this.memorySize){ // then it is in physical memory
                physicalMemory[pagenum][0] = varid;
                physicalMemory[pagenum][1] = value;
                pages.get(pagenum).setLastAccess(Clock.clock);
                System.out.println("clock: " + Clock.clock +", Process " + pid + " Store: Variable " + varid + ", Value: " + value);
            }else{ // the id is on disk so overwrite the disk value
                pages.get(pagenum).setLastAccess(Clock.clock);
                write(id + " " + pages.size());
                System.out.println("clock: " + Clock.clock +", Process " + pid + " Store: Variable " + varid + ", Value: " + value);
            }
        }

    }

    public int findPageNumber(int id){
        //int varid = Integer.parseInt(id);
        int varid = id;
        for(int i=0;i<pages.size();i++){
            if(varid == pages.get(i).getVariableID()){
                return pages.get(i).getPageNumber();
            }
        }

        return -1;
    }

    public void write(String text){
        try{
        FileWriter writer = new FileWriter("filename.txt",true);
        writer.write(text);
        writer.close();
        } catch (IOException ex) {
            // Report
        }
    }
}
