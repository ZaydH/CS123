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
				//----- Add next patient to the data set.
				trainingDataSet.addPatient(fileline);
			}
			
			//----- After building the training set, build the verification data set.
			while((fileline = dataFileIn.nextLine()) != null){
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
