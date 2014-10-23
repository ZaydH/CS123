package cs123A;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class BreastCancerGeneticAlgorithm {

	private static int NUMBER_OF_GENERATIONS = 1000;
	private static int PREVIOUS_GENERATION_CARRY_OVER_SIZE = 5;
	private static int NUMBER_CROSSOVER_POINTS = 3;
	private static int NUMBER_RANDOM_RESTARTS = 5;
	private GAChromosome bestSolution;
	private BreastCancerDataSet trainingDataSet;			//---- Used to train the linear classifier.
	private BreastCancerDataSet verificationDataSet;  		//---- Used to measure the quality of the training set results.
	private GAChromosomePopulation chromosomePopulation;	//---- Set of chromosomes used in the genetic algorithm.
	private Random rand;
	
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
		
		//--- Initialize a random number generator.
		rand = new Random();
		
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
		
		ArrayList<String> allLines = new ArrayList<String>(); 
		
		//---- Open the data set file and parse it.
		try{
			Scanner dataFileIn = new Scanner( new FileInputStream(filename) );
			
			//----- After building the training set, build the verification data set.
			while( dataFileIn.hasNext() ){
				
				//---- Get the next line.
				fileline = dataFileIn.nextLine();
				
				//---- Ignore the incomplete dataset elements.
				if(fileline.contains("?")) continue;
				
				//----- If valid data element, add it to the verification set.
				allLines.add(fileline);
			}
			
			//---- Close the input stream.
			dataFileIn.close();
		}
		catch(Exception e){
			assert(false);
		}
		
		//---- Build the training set randomly.
		int randomIndex;
		String replacementString;
		while(trainingDataSet.getDataSetSize() < BreastCancerDataSet.TRAINING_DATA_SET_SIZE){
			
			//---- Get a random index.
			randomIndex = rand.nextInt(allLines.size());
			
			//----- Add next patient to the data set.
			trainingDataSet.addPatient(allLines.get(randomIndex));
			//---- Get the last element
			replacementString = allLines.get(allLines.size()-1);
			allLines.set(randomIndex, replacementString); //---- Replace the removed index.
			
			//---- Remove the last index.
			allLines.remove(allLines.size()-1);
		}
		
		//---- Use the remaining chromosomes in the verification set.
		while(allLines.size() > 0){
			verificationDataSet.addPatient(allLines.remove(0));
		}
		
		
	}
	
	/**
	 * 
	 */
	public void run(){
		
		GAChromosome[] bestChromosomes;
		GAChromosome parent1, parent2, child;
		int generationNumber, i, restartNumber;
		GAChromosome tempBestSolution;
		
		//----- Run the genetic algorithm with the specified number of restarts.
		for(restartNumber = 0; restartNumber < NUMBER_RANDOM_RESTARTS; restartNumber++){
		
			//---- Create the genetic algorithm chromosome population from a random generated solution set.
			chromosomePopulation = new GAChromosomePopulation();
			chromosomePopulation.createRandomPopulation();
			
			//FIX_ME Write the code to run the genetic algorithm.
			for(generationNumber = 0; generationNumber < NUMBER_OF_GENERATIONS; generationNumber++){
				
				//---- Score population members.
				chromosomePopulation.scorePopulationMembers(trainingDataSet);
				
				//---- Build a new chromosome population.
				GAChromosomePopulation newPopulation = new GAChromosomePopulation();
				
				//---- Get the specified number of best chromosomes from this generation.
				bestChromosomes = chromosomePopulation.getBestChromosomes(PREVIOUS_GENERATION_CARRY_OVER_SIZE);
				for(i = 0; i < PREVIOUS_GENERATION_CARRY_OVER_SIZE; i++)
					newPopulation.addChromosome(bestChromosomes[i]);
				
				//---- Keep building the chromosome population until it reaches the specified size.
				while(newPopulation.getPopulationSize() < GAChromosomePopulation.MAXIMUM_POPULATION_SIZE){
					
					//---- Select two parents for crossover
					parent1 = chromosomePopulation.performTournamentSelection(20);
					parent2 = chromosomePopulation.performTournamentSelection(20);
					
					//---- Crossover parent chromosomes to form the child.
					child = parent1.crossover(parent2, NUMBER_CROSSOVER_POINTS);
					
					//---- Perform mutation.
					child.mutate();
					
					//---- FIXME add bitwise mutation.
					newPopulation.addChromosome(child);
				}	
				
				//--- Replace the existing population with the new population.
				chromosomePopulation = newPopulation;
				
			}
			
			//----- Extract the best chromosome from the final solution.
			bestChromosomes = chromosomePopulation.getBestChromosomes(1);
			//----- Get the best chromosome. 
			tempBestSolution = bestChromosomes[0];
			
			//---- Overwrite the best solution if appropriate by score or because no best solution found yet.
			if(bestSolution == null || tempBestSolution.getScore() > bestSolution.getScore())
				bestSolution = tempBestSolution;
			
			System.out.println("After "+ Integer.toString(restartNumber+1) + " run, the percent correct on the training set is: " 
							   + String.format("%2.2f",bestSolution.getScore() * 100.0 / trainingDataSet.getDataSetSize()));
		}
		
		//---- Print a basic results summary.
		System.out.println("On the training set, the score for the best solution is: " + Integer.toString(bestSolution.getScore()));
		System.out.println("The maximum possible score is: " + Integer.toString(trainingDataSet.getDataSetSize()));
		System.out.println("The percent correct is: " +  String.format("%2.2f",bestSolution.getScore() * 100.0 / trainingDataSet.getDataSetSize()));		
		
		//---- Print the gain vector.
		System.out.println("\nThe linear function weights are:");
		System.out.println(bestSolution.toString());
		
	}
	
	/**
	 * 
	 */
	public void printResults(){
		
		int chromosomeScore = verificationDataSet.getChromosomeScoreForPopulation(bestSolution);
		
		//---- Print a basic results summary.
		System.out.println("On the verification set, the score for the best solution is: " + Integer.toString(chromosomeScore));
		System.out.println("The maximum possible score is: " + Integer.toString(verificationDataSet.getDataSetSize()));
		System.out.println("The percent correct is: " + String.format("%2.2f",chromosomeScore * 100.0 / verificationDataSet.getDataSetSize()));
		System.out.println("The percentage of malignant tumors correctly categorized is: " 
							+  String.format("%2.2f",verificationDataSet.getMaligancyAccuracyForPopulation(bestSolution)) + "%.");
		
	}
	
	
	public GAChromosome getBestSolution(){
		return bestSolution;
	}
	
}
