import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.io.*;
import java.util.List;

// Memory Manager class is used for the Store,Lookup, and Release API,
// The class is a Singleton so that only one instance can be used by all processes
// it contains the physical memory layout and an array list that simulates a Page Table

public class MemoryManager extends Thread {

    private int memorySize;
    private boolean finished;
    private static MemoryManager instance;
    private int[][] physicalMemory;
    private ArrayList<PageEntry> pages;
    private String path;



    private MemoryManager(){

        this.memorySize = 2;
        this.finished = false;
        this.physicalMemory = new int[2][2];        // By default the size of physical memory is 2x2,
        this.pages = new ArrayList<PageEntry>();    // Use setMemorySize and setPhysicalMemory methods to change size
        this.path = "vm.txt";
    }

    // return the single instance of MM
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

        clearDisk(); // open vm.txt and clear all data

        while (!finished) {
            // not sure if this thread needs to run the whole time, maybe include a ThreadSleep??
        }

    }


    public void printMemSize(){
        System.out.println("Memory Size: " + memorySize);
    }

    public boolean memoryFull(){
        boolean full = true;
        for(int i =0;i<memorySize;i++){
            if(physicalMemory[i][0] == 0){
                full = false;
            }
        }
        return full;
    }
    public boolean diskFull(){
        boolean full = true;
        try {
            FileInputStream fs = new FileInputStream(this.path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            String line;
            String zero = "0 0";
            while((line = br.readLine()) != null) {
                if(line.equals(zero)){
                    full = false;
                }
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return full;
    }
    public void memoryDump(){
        System.out.println("\nMemory Dump\n" );
        for(int i=0;i<memorySize;i++){
            System.out.println("Memory: " + i + ", Variable " + physicalMemory[i][0] + ", Value: " + physicalMemory[i][1]);
        }
        try {
            FileInputStream fs = new FileInputStream(this.path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            String line;
            while((line = br.readLine()) != null) {
                System.out.println("Disk: " + line);
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
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
            //check if memory has free space
            if(!memoryFull()) {
                for(int i = 0;i<memorySize;i++){
                    if(physicalMemory[i][0] == 0){
                        physicalMemory[i][0] = varid;
                        physicalMemory[i][1] = value;
                        pages.get(i).setVariableID(varid);
                        pages.get(i).setLastAccess(Clock.clock);
                        System.out.println("clock: " + Clock.clock + ", Process " + pid + " Store: Variable " + varid + ", Value: " + value);
                        //memoryDump();
                        break;
                    }
                }
            }else if (!diskFull()){ //if disk not full
                int line = findPageNumber(0) - memorySize;
                System.out.println("clock: " + Clock.clock + ", Process " + pid + " Store: Variable " + varid + ", Value: " + value);
                replace(line,id + " " + value);
                pages.get(line + memorySize).setVariableID(id);
                pages.get(line + memorySize).setLastAccess(Clock.clock);
                //memoryDump();
            }else {
                pages.add(new PageEntry(varid, pages.size(), Clock.clock));
                System.out.println("clock: " + Clock.clock + ", Process " + pid + " Store: Variable " + varid + ", Value: " + value);
                write(id + " " + value);
            }
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
                replace(pagenum - memorySize,id + " " + value);
                System.out.println("clock: " + Clock.clock +", Process " + pid + " Store: Variable " + varid + ", Value: " + value);
            }
        }

    }

    public void lookup(int id,int pid){
        int page = findPageNumber(id);
        if(page < 0){ // if does not exist
            System.out.println("clock: " + Clock.clock +", Process " + pid + " Lookup: Variable " + id + ", Value Does Not Exist");
            return;
        }
        if(page < memorySize){ // if in memory
            System.out.println("clock: " + Clock.clock +", Process " + pid + " Lookup: Variable " + id + ", Value: " + getValue(id));
            //update accesstime
        }else{ // else it's on disk
            // find least recently used id
            int LRUid = leastRecentlyUsed();
            // swap(id,LRUid)
            swap(id,LRUid);
            // update access time
            pages.get(findPageNumber(id)).setLastAccess(Clock.clock);
            System.out.println("clock: " + Clock.clock +", Process " + pid + " Lookup: Variable " + id + ", Value: " + getValue(id));
        }
    }

    public void release(int id,int pid){
        int pagenum = findPageNumber(id);
        if(pagenum < 0 ){
            System.out.println("clock: " + Clock.clock +", Process " + pid + " Release: Variable " + id + ", Variable Does Not Exist");
            return;
        }
        if(pagenum < memorySize){
            physicalMemory[pagenum][0] = 0;
            physicalMemory[pagenum][1] = 0;
            pages.get(pagenum).setLastAccess(0);
            pages.get(pagenum).setVariableID(0);
            System.out.println("clock: " + Clock.clock +", Process " + pid + " Release: Variable " + id);
        }else{
            pages.get(pagenum).setLastAccess(0);
            pages.get(pagenum).setVariableID(0);
            replace(pagenum - memorySize, "0 0");
            System.out.println("clock: " + Clock.clock +", Process " + pid + " Release: Variable " + id);
        }
    }

    // swap variable1 with variable2, id1 is on disk, id2 is in memory
    public void swap(int id1, int id2){
        int location1 = findPageNumber(id1);
        int location2 = findPageNumber(id2);
        int value1 = getValue(id1);
        int value2 = getValue(id2);
        PageEntry page1 = pages.get(location1);
        PageEntry page2 = pages.get(location2);

        System.out.println("clock: " + Clock.clock + ", Memory Manager, SWAP: Variable " + id1 + " with Variable " + id2);
        Clock.clock += 50;
        if(location1 >= memorySize) {
            int line = location1 - memorySize;
            //System.out.println("Variable " + id1 + ", page : " + location1 + ", line : " + line);
            String newline = Integer.toString(id2) + " " + Integer.toString(value2);
            replace(line,newline);
            physicalMemory[location2][0] = id1;
            physicalMemory[location2][1] = value1;
            //PageEntry pagetemp = page2;
            page2.setPageNumber(location1);
            page1.setPageNumber(location2);
            pages.set(location2,page1);
            pages.set(location1,page2);
            //page1 = page2;

        }else{
            System.out.println("Variable not swapped, variable not on disk" );
        }


    }

    public int getValue(int id){
        int location = findPageNumber(id);
        if(location < 0){
            return -1;
        }
        if(location < memorySize ){ // then it's in physical memory
            return physicalMemory[location][1];
        }else{ // else on disk
            int line = location - memorySize;
            String diskline = readDisk(line,id);
            String parts[] = diskline.split(" ");
            //System.out.println("part0: " + parts[0]);
            //System.out.println("part1: " + parts[1]);
            return Integer.parseInt(parts[1]);
        }
        //return -1;
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

    public int leastRecentlyUsed(){

        int minPageNum = 0;
        int min = pages.get(minPageNum).getLastAccess();
        for(int i =0;i<memorySize;i++){
            int tempmin = pages.get(i).getLastAccess();
            if(tempmin < min){
                minPageNum = i;
                min = tempmin;
            }
        }

        return pages.get(minPageNum).getVariableID();
    }

    public void clearDisk(){

        BufferedWriter out = null;

        try {
            FileWriter fstream = new FileWriter(this.path, false); // set append to false to clear all data
            out = new BufferedWriter(fstream);
            out.close();
        }
        catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void write(String text){

        PrintWriter pw = null;

        try{
            File file = new File(this.path);
            FileWriter fw = new FileWriter(file, true);
            pw = new PrintWriter(fw);
            pw.println(text);
            pw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String readDisk(int line, int id){

        try {
            FileInputStream fs = new FileInputStream(this.path);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            String diskline;
            for (int i = 0; i < line; ++i) {
               br.readLine();
            }
            diskline = br.readLine();
            //System.out.println("Disk " + diskline);
            return diskline;
        }catch(IOException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public void replace(int linenumber,String newline){
        try {
            List<String> fileContent = new ArrayList<>(Files.readAllLines(Paths.get(this.path), StandardCharsets.UTF_8));

            for (int i = 0; i < fileContent.size(); i++) {
                if (linenumber == i) {
                    fileContent.set(i, newline);
                    break;
                }
            }

            Files.write(Paths.get(this.path), fileContent, StandardCharsets.UTF_8);
        }catch(IOException ex){
            ex.printStackTrace();
        }

    }
}
