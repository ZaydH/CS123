package cs123A;

import java.io.FileInputStream;
import java.util.Random;
import java.util.Scanner;

public class BreastCancerGeneticAlgorithm {

	private static final int NUMBER_OF_GENERATIONS = 1000;
	private static final int PREVIOUS_GENERATION_CARRY_OVER_SIZE = 5;
	private static final int NUMBER_CROSSOVER_POINTS = 3;
	private static final int NUMBER_RANDOM_RESTARTS = 5;
	private static int numberTimesToRunProgram = 1;
	private static boolean balanceMaligantPatients = false;
	private static int maligancyBiasFactor = 1;
	private GAChromosome bestSolution;
	private BreastCancerDataSet trainingDataSet;			//---- Used to train the linear classifier.
	private BreastCancerDataSet verificationDataSet;  		//---- Used to measure the quality of the training set results.
	private GAChromosomePopulation chromosomePopulation;	//---- Set of chromosomes used in the genetic algorithm.
	private Random rand;
	
	public static void main(String[] args) {
		
		//---- Parse the input arguments and ensure they are valid.
		boolean inputArgumentsValid = parseInputArguments(args);
		if(!inputArgumentsValid)
			return;
		
		for(int i = 0; i < numberTimesToRunProgram; i++){
			
			//---- Print the number of times the algorithm is running
			if(numberTimesToRunProgram > 1)
				System.out.println("Genetic Algorithm Execution #" + i 
							   	   + " of " + numberTimesToRunProgram + ".");
			
			//---- Initialize the genetic algorithm.
			BreastCancerGeneticAlgorithm geneticAlgorithm = new BreastCancerGeneticAlgorithm();
			
			//---- Run the genetic algorithm.
			geneticAlgorithm.run();
			
			//---- Print the results.
			geneticAlgorithm.printResults();
		}

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
	 * Parser for the breast cancer data set.
	 * 
	 * @param filename Name of the breast cancer data set file.
	 */
	private void parseDataSetFile(String filename){
		
		String fileline;
		BreastCancerDataSet benignPatients = new BreastCancerDataSet();
		BreastCancerDataSet malignantPatients = new BreastCancerDataSet();
		Patient tempPatient;
		
		//---- Open the data set file and parse it.
		try{
			Scanner dataFileIn = new Scanner( new FileInputStream(filename) );
			
			//----- After building the training set, build the verification data set.
			while( dataFileIn.hasNext() ){
				
				//---- Get the next line.
				fileline = dataFileIn.nextLine();
				
				//---- Ignore the incomplete data set elements.
				if(fileline.contains("?")) continue;
				
				//----- If valid data element, add it to the verification set.
				tempPatient = new Patient(fileline);
				if(tempPatient.isMalignant())
					malignantPatients.addPatient(tempPatient);
				else
					benignPatients.addPatient(tempPatient);
					
			}
			
			//---- Close the input stream.
			dataFileIn.close();
		}
		catch(Exception e){
			assert(false);
		}
		
		//----- Separate the source data into the training and verification datasets.
		if(!balanceMaligantPatients){
			
			//---- Merge the two data sets into one.
			verificationDataSet = BreastCancerDataSet.mergeDataSets(benignPatients, malignantPatients);
			//---- Remove the Training Data Set from the verification data set.
			trainingDataSet = verificationDataSet.removeRandomSubset(BreastCancerDataSet.getTrainingDataSetSize());
			
		}
		else{
			
			//--- Extract the percentage of the population that will be in the training set.
			double percentTraining = 1.0* BreastCancerDataSet.getTrainingDataSetSize();
			percentTraining /= (malignantPatients.getDataSetSize() + benignPatients.getDataSetSize());
			
			//---- Get the malignant patients for training set.
			int numbMalignantInTrainingSet = (int)Math.round(percentTraining * malignantPatients.getDataSetSize() );
			trainingDataSet = malignantPatients.removeRandomSubset( numbMalignantInTrainingSet );
			
			//---- Get the Benign patients for the training set.
			int numbBenignInTrainingSet =  BreastCancerDataSet.getTrainingDataSetSize() - numbMalignantInTrainingSet;
			BreastCancerDataSet tempDataSet = benignPatients.removeRandomSubset( numbBenignInTrainingSet );
			//---- Construct the full training set.
			trainingDataSet = BreastCancerDataSet.mergeDataSets(trainingDataSet, tempDataSet)
			
			
			//---- Merge the two remaining objects in benign and malignant data set into the verification set.
			verificationDataSet = BreastCancerDataSet.mergeDataSets(benignPatients, malignantPatients);
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
				chromosomePopulation.scorePopulationMembers(trainingDataSet, maligancyBiasFactor);
				
				//---- Build a new chromosome population.
				GAChromosomePopulation newPopulation = new GAChromosomePopulation();
				
				//---- Get the specified number of best chromosomes from this generation.
				bestChromosomes = chromosomePopulation.getBestChromosomes(PREVIOUS_GENERATION_CARRY_OVER_SIZE);
				for(i = 0; i < PREVIOUS_GENERATION_CARRY_OVER_SIZE; i++)
					newPopulation.addChromosome(bestChromosomes[i]);
				
				//---- Keep building the chromosome population until it reaches the specified size.
				while(newPopulation.getPopulationSize() < GAChromosomePopulation.getMaximumPopulationSize()){
					
					//---- Select two parents for crossover
					parent1 = chromosomePopulation.performTournamentSelection(20);
					parent2 = chromosomePopulation.performTournamentSelection(20);
					
					//---- Crossover parent chromosomes to form the child.
					child = parent1.crossover(parent2, NUMBER_CROSSOVER_POINTS);
					
					//---- Perform mutation.
					child.mutate();
					
					//---- Add the modified chromosome to the new population.
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
	
	/**
	 * 
	 * 
	 * @return GAChromosome that has the best score from the current population.
	 */
	public GAChromosome getBestSolution(){
		return bestSolution;
	}
	
	
	
	
	
	/**
	 * 
	 * -BalancePopulations - Flag to enable the balance malignant patients 
	 * proportionally between the training and verification datasets.
	 * 
	 * -n - Indicator for the number of times to run the entire algorithm.
	 * This is followed by an integer number.
	 * 
	 * -TrainingDataSetSize - Indicator for the number of the size of the training dataset.
	 * This is followed by an integer number.
	 * 
	 * -MaxPopSize Flag to change the maximum population size of the algorithm.
	 * This is followed by an integer number.
	 * 
	 * @param args Command line input arguments.
	 * 
	 * @return True of the input arguments were successfully parsed, false otherwise.
	 */
	private static boolean parseInputArguments(String[] args){
		
		
		//----- Parse the input flags.  Each flag has a 
		for(int i = 0; i < args.length; i++){
			switch(args[i]){
			
			
			//---------------------------------------------------//
			//    Enable the balancing of Malignant Patients     //
			//---------------------------------------------------//
			case "-BalancePopulations":
				
				balanceMaligantPatients = true;
			
			//---------------------------------------------------//
			//     Parse the size of the training data set       //
			//---------------------------------------------------//
			case "-TrainingDataSetSize":
			
				i++;
				//---- Ensure no overflow.
				if(i == args.length) return printInvalidInputNumberInputArguments();
				try{
					BreastCancerDataSet.setTrainingDataSetSize(Integer.parseInt(args[i]));
					///----- Check the training data set size is valid.
					if(BreastCancerDataSet.getTrainingDataSetSize() < 1
							|| BreastCancerDataSet.getTrainingDataSetSize() > BreastCancerDataSet.MAXIMUM_TRAINING_DATA_SET_SIZE){
						System.out.println("Error: The training data set size is invalid. Exiting...");
						return false;
					}
				}
				catch(Exception e){
					System.out.println("Error: The training data set size is invalid. Exiting...");
					return false;
				}
		
			//---------------------------------------------------//
			//           Parse the Malignant Penalty             //
			//---------------------------------------------------//
			case "-MaligantPenalty":
				
				i++;
				//---- Ensure no overflow.
				if(i == args.length) return printInvalidInputNumberInputArguments();
				try{
					maligancyBiasFactor = Integer.parseInt(args[i]);
					if(maligancyBiasFactor < 1){
						System.out.println("Error: The maligancy bias factor must be greater than 1. Exiting...");
						return false;
					}
				}
				catch(Exception e){
					System.out.println("Error: The maligancy bias factor is invalid. Exiting...");
					return false;
				}
			
			//---------------------------------------------------//
			//        Parse the maximum population size.         //
			//---------------------------------------------------//
			case "-MaxPopSize":
				
				i++;
				//---- Ensure no overflow.
				if(i == args.length) return printInvalidInputNumberInputArguments();
				try{
					GAChromosomePopulation.setMaximumPopulationSize(Integer.parseInt(args[i]));
					if(GAChromosomePopulation.getMaximumPopulationSize() < 1){
						System.out.println("Error: The maximum population size is invalid. Exiting...");
						return false;
					}
				}
				catch(Exception e){
					System.out.println("Error: The maximum population size is invalid. Exiting...");
					return false;
				}
			
			
			//---------------------------------------------------//
			//   Parse the number of times to run the program.   //
			//---------------------------------------------------//
			case "-n":
				i++;
				//---- Ensure no overflow.
				if(i == args.length) return printInvalidInputNumberInputArguments();
				try{
					numberTimesToRunProgram = Integer.parseInt(args[i]);
					if(numberTimesToRunProgram < 1){
						System.out.println("Error: The number of times the program is run is invalid. Exiting...");
						return false;
					}
				}
				catch(Exception e){
					System.out.println("Error: The number of times the program is run is invalid. Exiting...");
					return false;
				}
			
			//--------------------------------------------------//
			//  Unable to parse input argument so return exit.  //
			//--------------------------------------------------//
			default:
				System.out.println("Error: Invalid input argument \"" + args[i] + "\".");
				return false;
			}
		}
		
		//----- All arguments successfully parsed so return true.
		return true;
	}
	/**
	 * Error message used to print an invalid number of input arguments.
	 * 
	 * @return false to indicate the parse failed.
	 */
	private static boolean printInvalidInputNumberInputArguments(){
		System.out.println("Error: The number of input arguments is invalid. Exiting...");
		return false;
	}
	
}
