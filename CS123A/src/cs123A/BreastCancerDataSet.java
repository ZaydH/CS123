package cs123A;

import java.util.ArrayList;

public class BreastCancerDataSet {

	public final static int TRAINING_DATA_SET_SIZE = 200;
	private ArrayList<Patient> setOfPatients;
	
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
	public int getChromosomeScoreForPopulation(GAChromosome chromosome, int malignancyBiasFactor){
		
		int chromosomeScore = 0;
		int index;
		long patientScore;
		Patient patient;
		
		int[] chromosomeGainVector = chromosome.getGainVector();
		int chromosomeOffset = chromosome.getOffset();
		
		//---- Iterate through the 
		for(index = 0; index < setOfPatients.size(); index++){
			//---- Get the patient
			patient = setOfPatients.get(index);
			//---- Determine the score for that patient
			patientScore = patient.calculateLinearFunction(chromosomeGainVector, chromosomeOffset);
			//---- If the patient is correctly categorized, give it a positive score.
			if(patientScore > 0)
				chromosomeScore++;
				if(patient.isMalignant())
					chromosomeScore += malignancyBiasFactor;
			else
				if(patient.isMalignant())
					chromosomeScore -= malignancyBiasFactor;
//			//---- Patient is miscategorized so give it a negative score.
//			else
//				chromosomeScore--;	
			
		}
		
		//---- Give the chromosome score.
		return chromosomeScore;
		
	}
	
	/**
	 * Calculates the accuracy for malignant tumors properly categorized.
	 * 
	 * @param chromosome	Genetic Algorithm Chromosome (Linear function)
	 * @return				Percentage of malignant tumors correctly categorized.
	 */
	public double getMaligancyAccuracyForPopulation(GAChromosome chromosome){
		
		int index;
		int malignancyCorrect = 0;
		int numberOfMalignantPatients = 0;
		long patientScore;
		Patient patient;
		
		
		int[] chromosomeGainVector = chromosome.getGainVector();
		int chromosomeOffset = chromosome.getOffset();
		
		//---- Iterate through the 
		for(index = 0; index < setOfPatients.size(); index++){
			//---- Get the patient
			patient = setOfPatients.get(index);
			if(patient.isMalignant()){
				numberOfMalignantPatients++;
				
				//---- Determine the score for that patient
				patientScore = patient.calculateLinearFunction(chromosomeGainVector, chromosomeOffset);
				//---- If the patient is correctly categorized, give it a positive score.
				if(patientScore > 0)
					malignancyCorrect++;
			}	
		}
		
		//---- Give the chromosome score.
		return malignancyCorrect * 100.0 / numberOfMalignantPatients ;
		
	}
	
	
}
