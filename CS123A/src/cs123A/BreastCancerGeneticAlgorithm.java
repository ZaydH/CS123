package cs123A;

import java.io.FileInputStream;
import java.util.Scanner;

public class BreastCancerGeneticAlgorithm {

	private static int NUMBER_OF_GENERATIONS = 100;
	private static int PREVIOUS_GENERATION_CARRY_OVER_SIZE = 10;
	private GAChromosome bestSolution;
	private BreastCancerDataSet trainingDataSet;			//---- Used to train the linear classifier.
	private BreastCancerDataSet verificationDataSet;  		//---- Used to measure the quality of the training set results.
	private GAChromosomePopulation chromosomePopulation;	//---- Set of chromosomes used in the genetic algorithm.
	
	public static void main(String[] args) {
		
		//---- Initialize the genetic algorithm.
		BreastCancerGeneticAlgorithm geneticAlgorithm = new BreastCancerGeneticAlgorithm();
		
		//---- Run the genetic algorithm.
		geneticAlgorithm.run();
		
		//---- Print the results.
		geneticAlgorithm.printResults();

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
		
		//---- Create the genetic algorithm chromosome population from a random generated solution set.
		chromosomePopulation = new GAChromosomePopulation();
		chromosomePopulation.createRandomPopulation();
		
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
	
	/**
	 * 
	 * @return
	 */
	public void run(){
		
		GAChromosome[] bestChromosomes;
		GAChromosome parent1, parent2, child;
		int generationNumber, i;
		
		//FIX_ME Write the code to run the genetic algorithm.
		for(generationNumber = 0; generationNumber < NUMBER_OF_GENERATIONS; generationNumber++){
			
			//---- Build a new chromosome population.
			GAChromosomePopulation newPopulation = new GAChromosomePopulation();
			
			//---- Get the specified number of best chromosomes from this generation.
			bestChromosomes = chromosomePopulation.getBestChromosomes(PREVIOUS_GENERATION_CARRY_OVER_SIZE);
			for(i = 0; i < PREVIOUS_GENERATION_CARRY_OVER_SIZE; i++)
				newPopulation.addChromosome(bestChromosomes[i]);
			
			//---- Keep building the chromosome population until it reaches the specified size.
			while(newPopulation.getPopulationSize() < GAChromosomePopulation.MAXIMUM_POPULATION_SIZE){
				
				//---- Select two parents for crossover
				parent1 = chromosomePopulation.performTournamentSelection();
				parent2 = chromosomePopulation.performTournamentSelection();
				
				//---- Crossover parent chromosomes to form the child.
				child = parent1.crossover(parent2, 2);
				
				//---- FIXME add bitwise mutation.
				newPopulation.addChromosome(child);
			}
			
			//--- Replace the existing population with the new population.
			chromosomePopulation = newPopulation;
			
		}
		
		//----- Extract the best chromosome from the final solution.
		bestChromosomes = chromosomePopulation.getBestChromosomes(1);
		//----- Get the best chromosome. 
		bestSolution = bestChromosomes[0];
		
	}
	
	/**
	 * 
	 */
	public void printResults(){
		
		int chromosomeScore = verificationDataSet.getChromosomeScoreForPopulation(bestSolution);
		
		//---- Print a basic results summary.
		System.out.println("The score for the best solution is: " + Integer.toString(chromosomeScore));
		System.out.println("The maximum possible score is: " + Integer.toString(verificationDataSet.getDataSetSize()));
		
	}
	
}
