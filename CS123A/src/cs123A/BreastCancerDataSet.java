package cs123A;

import java.util.ArrayList;

public class BreastCancerDataSet {

	public final static int TRAINING_DATA_SET_SIZE = 200;
	ArrayList<Patient> setOfPatients;
	
	/**
	 * Constructor to build datasets of breast cancer dataset.
	 */
	public BreastCancerDataSet(){
		//----- Create the patient array
		setOfPatients = new ArrayList<Patient>();
	}
	
	
	/**
	 * Adds a new patient to the training set.
	 * 
	 * @param features A comma separated list of the features that is parsed by the patient.
	 */
	public void addPatient(String features){
		Patient newPatient = new Patient(features);
		setOfPatients.add(newPatient);
	}
	
	
	/**
	 * Accessor for number of patients in the dataset.
	 * 
	 * @return Number of patients in the data set.
	 */
	public int getDataSetSize(){
		return setOfPatients.size();
	}
	
	
	/**
	 * Determines the score of a given chromosome and a population.
	 * 
	 * @param chromosome Chromosome whose population score will be calculated.
	 * @return Score for the chromosome which is the number of correct identifications versus incorrect identifications.
	 */
	public int getChromosomeScoreForPopulation(GAChromosome chromosome){
		
		int chromosomeScore = 0;
		int index;
		long patientScore;
		Patient patient;
		
		//---- Iterate through the 
		for(index = 0; index < setOfPatients.size(); index++){
			//---- Get the patient
			patient = setOfPatients.get(index);
			//---- Determine the score for that patient
			patientScore = patient.calculateLinearFunction(chromosome);
			//---- If the patient is correctly categorized, give it a positive score.
			if((patientScore > 0 && patient.isMalignant()) || (patientScore < 0 && !patient.isMalignant()))
				chromosomeScore++;
			//---- Patient is miscategorized so give it a negative score.
			else
				chromosomeScore--;	
			
		}
		
		//---- Give the chromosome score.
		return chromosomeScore;
		
	}
	
	
}
