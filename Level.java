
import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

public class Level{
	
	//For Counting number of lines in given csv file. It is used by readFromCSV method.
	private static int numberOfLines(String filename) throws Exception{
		int nofsetup = 0;
			BufferedReader lineCounter = new BufferedReader(new FileReader(filename));
			while (lineCounter.readLine() != null) {
				nofsetup++;
			}
			lineCounter.close();
		return nofsetup;
	}
	
	//For reading data from a csv file.
	private static Data[] readFromCSV(String filename, double knownRL) throws Exception{
		Data[] data = new Data[numberOfLines(filename)+1];
		int i= 0;
		data[i]= new Data (0,0,knownRL);
		BufferedReader fileReader = new BufferedReader(new FileReader(filename));
		String line = "";
		while ((line = fileReader.readLine()) != null) {
			i++;
			String[] column = line.split(",");
			data[i] = new Data(Double.parseDouble(column[0]),Double.parseDouble(column[1]),data[i-1].getReducedLevel());
		}
		return data;
	}
	
	//For reading data from user.
	private static Data[] readFromUser(double knownRL){
		Scanner input = new Scanner(System.in);
		System.out.print("Enter Number of Setup: ");
		int nofsetup = input.nextInt();
		Data[] data = new Data[nofsetup+1];
		data[0] = new Data(0,0,knownRL);
		for(int i = 1; i < data.length ; i++){
			System.out.print("Enter backsight for setup "+i+": ");
			double backsight = input.nextDouble();
			System.out.print("Enter foresight for setup "+i+": ");	
			double foresight = input.nextDouble();
			data[i] = new Data(backsight, foresight, data[i-1].getReducedLevel());  
		}
		return data;
	}
	
	//For Writing result to a csv file
	private static void writeToCSV(String filename,Data[] data) throws Exception{
	
		FileWriter fileWrite = new FileWriter(filename+".csv");
		BufferedWriter bufferedWrite = new BufferedWriter(fileWrite);
		for (int i=0; i < data.length ; i++) {
			bufferedWrite.write(data[i].toCSVString());
		}
		bufferedWrite.close();
	}
	
	public static void main (String[] args){
		
		Scanner input = new Scanner(System.in);
		System.out.print("Enter known RL: ");
		int knownRL = input.nextInt();
		input.nextLine();
		boolean loopOrNot = true;
		Data[] data = new Data[0];
		while (loopOrNot){
			//Knowing the method of data entry that user want to use.
			System.out.print("How do you want to enter backsight and foresight Data? Manually or From File (M/F) ");
			String check = input.nextLine();
			if (check.equals("F")||check.equals("f")){
				System.out.print("Enter Filename for Backsight and Foresight data: ");
				String filename = input.nextLine();
				try{
					data = readFromCSV(filename,knownRL);
					loopOrNot = false;
				}catch(Exception e){
					System.out.println("Error in CsvFileReader !!!");
					loopOrNot = true;
				}
			}else if (check.equals("M")||check.equals("m")){
				data = readFromUser(knownRL);
				loopOrNot = false;
			}else{
				System.out.println("Invalid Choice");
				loopOrNot = true;
			}
		}
		//Displaying the information in table form.
		System.out.println("Setup \t Backsight \t Foresight \t Rise/fall \t RL of Foresight Station");
		for(int i = 1; i < data.length ; i++)
			System.out.println(i+"\t  "+data[i].getBackSight()+"\t\t  "+data[i].getForesight()+"\t\t "+data[i].getHeightDifference()+"\t\t  "+data[i].getReducedLevel());
		//Do loop is used as loopOrNot is false at first but the loop needz to run once
		do{
			//Asking user if they wish to save result.
			System.out.print("Do you wish to save result? Enter Y to save or Any other key to exit ");
			String ask = input.nextLine();
			if (ask.equals("Y")||ask.equals("y")){
				System.out.print("Enter filename of output file: ");
				String filename = input.nextLine();
				try{
					writeToCSV(filename,data);
					System.out.println("Data sucessfully written to file "+filename+".csv");
					loopOrNot = false;
				}catch (Exception e){
					System.out.println("Could not write data to file "+filename+".csv");
					loopOrNot = true;
				}
			}else{
				loopOrNot=false;
			}
		}while (loopOrNot);
		
	}
}

class Data {
	private double backsight;
	private double foresight;
	private double reducedLevel;
	private double heightDifference = 0;
	Data(double backsight,double foresight,double reducedLevel){
		this.backsight = backsight;
		this.foresight = foresight;
		this.reducedLevel = reducedLevel;
		this.heightDifference = backsight - foresight;
		this.reducedLevel = reducedLevel + heightDifference;
	}
	public double getBackSight(){
		return backsight;
	}
	public double getForesight(){
		return foresight;
	}
	public double getHeightDifference(){
		return heightDifference;
	}
	public double getReducedLevel(){
		return reducedLevel;
	}
	
	//This returns row to be written to a csv file.
	public String toCSVString() {
		String row = backsight+","+ foresight + ","+ heightDifference +","+reducedLevel+"\n";
		return row;
	} 
}