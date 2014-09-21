package cs123A;

import java.io.FileInputStream;
import java.util.Scanner;

public class BreastCancerGeneticAlgorithm {

	private BreastCancerDataSet trainingDataSet;
	private BreastCancerDataSet verificationDataSet;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BreastCancerGeneticAlgorithm geneticAlgorithm = new BreastCancerGeneticAlgorithm();
		
	}

	
	/**
	 * Constructor for Breast Cancer Genetic Algorithm using default data set file location.
	 */
	public BreastCancerGeneticAlgorithm(){
		this("breast-cancer-wisconsin.data.txt");
	}
	
	
	/**
	 * Constructor for the breast cancer genetic algorithm that allows users to specify a dataset file.
	 * 
	 * @param breastCancerDataSetFile Path to the data set file.
	 */
	public BreastCancerGeneticAlgorithm(String breastCancerDataSetFile){
		
		//---- Initialize the training and verification data sets.
		trainingDataSet = new BreastCancerDataSet();
		verificationDataSet = new BreastCancerDataSet();
		
		//---- Parse the dataset file.
		this.parseDataSetFile(breastCancerDataSetFile);
		
	}
	
	/**
	 * Parser for the breast cancer dataset.
	 * 
	 * @param filename Name of the breast cancer dataset file.
	 */
	private void parseDataSetFile(String filename){
		
		String fileline;
		
		//---- Open the data set file and parse it.
		try{
			Scanner dataFileIn = new Scanner( new FileInputStream(filename) );
			
			//----- Extract the training data set size first.
			int patientCount = 0;
			while(patientCount < BreastCancerDataSet.TRAINING_DATA_SET_SIZE){
				fileline = dataFileIn.nextLine();//---- Get the file line.
				
				//---- Ignore the incomplete dataset elements.
				if(fileline.contains("?")) continue;
				
				//----- Add next patient to the data set.
				trainingDataSet.addPatient(fileline);
				patientCount++; //--- Increment the number of patients in the dataset.
			}
			
			//----- After building the training set, build the verification data set.
			while( dataFileIn.hasNext() ){
				
				//---- Get the next line.
				fileline = dataFileIn.nextLine();
				
				//---- Ignore the incomplete dataset elements.
				if(fileline.contains("?")) continue;
				
				//----- If valid data element, add it to the verification set.
				verificationDataSet.addPatient(fileline);
			}
			
			//---- Close the input stream.
			dataFileIn.close();
		}
		catch(Exception e){
			assert(false);
		}
		
	}
	
	
}
