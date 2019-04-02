import java.util.Arrays;

public class VMM {

	private int variable_ID;
	private double value;
	private int size;
	double memoryArray[];
	
	public VMM(int size) {
		this.size = size;
		memoryArray = new double[size];
	}
	
	public void put(int id, double val) {
		int counter = 0;
		if(id > memoryArray.length-1) {
			System.out.println("Requested input greater than memory capacity");
			return;
		}else {
			
		for(int i = 0; i < memoryArray.length; i++) {
			if(memoryArray[i] == 0) {
				counter++; // counter has value for # of empty index slots. The value is basically the index (0 means all full)
			} 
		}
		
		if(counter == 0) {
			// print in output text file
		} else {
			if(memoryArray[id] == 0) {
				memoryArray[id] = val;
			} else {
				// print in output file
			}
		}
		
	  }
	}
	
	public double get(int id) {
		double temp = 0;
		for (int i = 0; i < memoryArray.length; i++) {
			if(i == id) {
				//found in array
				temp = memoryArray[i]; 
			} else {
				// search for output file
			}
		}
		return temp;
	}
	
	public void delete(int id) {
		for (int i = 0; i < memoryArray.length; i++) {
			if(i== id) {
				//delete this value
				memoryArray[i] = 0;
			} else {
				//search in output file
			}
		}
	}
	
	public void printArray() {
		
		System.out.println(Arrays.toString(memoryArray));
	}
}
