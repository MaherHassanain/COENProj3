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
        System.out.println(", memory manager"  + " Started");
        synchronized (this) {
            try {
                while (!finished) {
                    //System.out.println( "memory manager waiting");
                    this.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void pause(){
        System.out.println(", memory manager"  + " Paused");
        this.suspend();
    }
    public void finish(){
        System.out.println( ", memory manager"  + " Finished ");
        this.stop();
    }
    public void printMemSize(){
        System.out.println("Memory Size: " + memorySize);
    }
    public void store(int id, int value,int time){
        //int varid = Integer.parseInt(id);
        int varid = id;
        //if list is less than the memory size just add it directly
        if(pages.size() < this.memorySize) {

            //System.out.println("Pages Size" + pages);
            physicalMemory[pages.size()][0] = varid;
            physicalMemory[pages.size()][1] = value;
            pages.add(new PageEntry(varid, pages.size(), time));
            System.out.println("clock: store" + varid + " " + value + " " +time);
        }
        int pagenum = findPageNumber(id);
        if(pagenum < 0){
            pages.add(new PageEntry(varid,pages.size(),time));
            System.out.println("clock: store" + varid + " " + value+ " " +time);
            write(id + " " + pages.size()+ " " + time);
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
        Writer writer = null;

        try {
            writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream("filename.txt"), "utf-8"));
            writer.write(text);
        } catch (IOException ex) {
            // Report
        } finally {
            try {writer.close();} catch (Exception ex) {/*ignore*/}
        }
    }
}
