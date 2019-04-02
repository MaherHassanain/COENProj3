import java.io.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class Main {
	
	static int mainMemorySize = 0;
    static ArrayList<Processes> procList = new ArrayList<Processes>();
    static ArrayList<Store> storeComList = new ArrayList<Store>();
    static ArrayList<Release> releaseComList = new ArrayList<Release>();
    static ArrayList<Lookup> lookupComList = new ArrayList<Lookup>();

	public static void readProcesses(String filename) {
		
		try {
			 File file = new File(filename);
	         FileReader fr = new FileReader(file);
	         BufferedReader br = new BufferedReader(fr);
	         
	         Processes proc;
	         String st;
	         int linecount = 0;
	         
	         while ((st = br.readLine()) != null) {
	        	 if(linecount == 0) {
	        		 int processCount = Integer.parseInt(st);
	        	 } else {
	        		 String[] parts = st.split(" "); // split the line in two
	                 String part1 = parts[0]; // ready time
	                 String part2 = parts[1]; // duration time
	                 String procID = Integer.toString(linecount);
	                 proc = new Processes(procID,Integer.parseInt(part1),Integer.parseInt(part2));
	                 procList.add(proc);
	        	 }
	        	 //System.out.println(st);
	             linecount++;
	         }
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
	}
	
	public static void readMemSize(String filename) {
		
		try {
			
			 File file = new File(filename);
	         FileReader fr = new FileReader(file);
	         BufferedReader br = new BufferedReader(fr);   
	         String st;
	         while ((st = br.readLine()) != null) {	        	 
	        		 mainMemorySize = Integer.parseInt(st);
	        		 //System.out.println(mainMemorySize);
	         }
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public static void readCommands(String filename) {
		
		try {
			
			File file = new File(filename);
			FileReader fr = new FileReader(file);
			BufferedReader br = new BufferedReader(fr);  
			
			Commands com;
	        String st;
	        int linecount = 0;
	        int id = 1;
	        while ((st = br.readLine()) != null) {
	        	String[] parts = st.split(" "); // split the line in three
	        	String part1 = parts[0]; // command
	        	//System.out.println(part1);
	        	if(part1.contentEquals("Store")) {
	        		String part2 = parts[1]; // command ID
	                String part3 = parts[2]; // command Value
	                //System.out.println(part2);
	                //System.out.println(part3);
	                com = new Commands(part1, id);
	                Store store = new Store(part1,Integer.parseInt(part2),Integer.parseInt(part3), id);
	                storeComList.add(store); 
	                id++;
	        	} else if (part1.contentEquals("Release")){
	        		String part2 = parts[1]; // command ID
	        		Release rel = new Release(part1,id,Integer.parseInt(part2));
	        		releaseComList.add(rel); 
	        		id++;
	        	} else {
	        		String part2 = parts[1]; // command ID
	        		Lookup lu = new Lookup(part1,id,Integer.parseInt(part2));
	        		lookupComList.add(lu); 
	        		id++;
	        	}
                
	        }
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	public static void printProccesses(){
        if(procList.size() < 1){
            System.out.println("No Processes found");
            return;
        }
        
        procList.forEach(proc -> {
            proc.getProcID();
            proc.getStartTime();
            proc.getTimeDuration();
            proc.printProcesses();
        });
    }
	
	public static void printStoredCommands(){
        if(storeComList.size() < 1){
            System.out.println("No Store commands found");
            return;
        }
        
        storeComList.forEach(store -> {
        	store.getStoredID();
        	store.getVarID();
        	store.getValue();
        	store.printStoredCommands();
        });
    }
	
	public static void printReleaseCommands(){
        if(releaseComList.size() < 1){
            System.out.println("No Store commands found");
            return;
        }
        
        releaseComList.forEach(rel -> {
        	rel.getReleaseID();
        	rel.getVarID();
        	rel.printReleaseCommands();
        });
    }
	
	public static void printLookupCommands(){
        if(lookupComList.size() < 1){
            System.out.println("No Store commands found");
            return;
        }
        
        lookupComList.forEach(lu -> {
        	lu.getLookupID();
        	lu.getVarID();
        	lu.printLookupCommands();
        });
    }
	
	
	public static void main(String[] args)  {
		if(args != null){
	           readProcesses("processes.txt");
	           readMemSize("memconfig.txt");
	           readCommands("commands.txt");
	        }
		printProccesses();
		printStoredCommands();
		printReleaseCommands();
		printLookupCommands();
		System.out.println("Number of pages for main memory: " + mainMemorySize);
		//System.out.println("Hello World");
//		VMM vmm = new VMM(5);
//		vmm.put(0,3);
//		vmm.put(1,7);
//		vmm.put(3,12);
//		System.out.println(vmm.get(1));
//		System.out.println(vmm.get(3));
//		vmm.delete(0);
//		vmm.delete(3);
//		vmm.printArray();
    }
}
