package cs123A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GAChromosomePopulation {

	public static int MAXIMUM_POPULATION_SIZE = 1000;
	private boolean isSorted;
	private ArrayList<GAChromosome> populationMembers;
	private Random rand;
	
	/**
	 * Constructor for the GAChromosomePopulation class.  
	 * It creates an empty array of population members.
	 */
	public GAChromosomePopulation(){
		populationMembers = new ArrayList<GAChromosome>();
		isSorted = false;
		if(rand == null)
			rand = new Random();
	}
	
	
	/**
	 * Static constructor to create a random genetic algorithm population.
	 * 
	 * @return Randomly generated genetic algorithm population.
	 */
	public void createRandomPopulation(){
		
		//---- Create the random population 
		while(populationMembers.size() < MAXIMUM_POPULATION_SIZE){
			populationMembers.add(GAChromosome.createRandomChromosome());
		}
	}
	
	/**
	 * Accessor for the size of this population.
	 * 
	 * @return Size of this population.
	 */
	public int getPopulationSize(){
		return populationMembers.size();
	}
	
	
	/**
	 * Run tournament selection to select a parent chromosome. 
	 * This function uses a default tournament size of 5.
	 * 
	 * @return GA Chromosome selected via tournament selection.
	 */
	public GAChromosome performTournamentSelection(){
		int defaultTournamentSize = 5;
		//---- Return the selected chromosome.
		return performTournamentSelection(defaultTournamentSize);
	}
	
	/**
	 * Run tournament selection to select a parent chromosome.
	 * 
	 * @param tournamentSize Number of chromosomes in the tournament.
	 * @return GA Chromosome selected via tournament selection.
	 */
	public GAChromosome performTournamentSelection(int tournamentSize){
		
		GAChromosome parentChromosome, tournamentChromosome;
		int i, tournamentIndex;
		parentChromosome = null; //---- Default initialization for parent.
		
		//---- Run the tournament 
		for(i = 0; i < tournamentSize; i++){
			
			//---- Select the index of the n
			tournamentIndex = rand.nextInt(populationMembers.size());
			//---- Extract the tournament chromosome
			tournamentChromosome = populationMembers.get(tournamentIndex);
			if(parentChromosome == null || tournamentChromosome.getScore() > parentChromosome.getScore())
				parentChromosome = tournamentChromosome;
		}
		
		
		return parentChromosome;
	}
	
	
	/**
	 * Returns the best chromosomes from the population.
	 * 
	 * @param numbChromosomes Number of chromosomes to return.
	 * @return	Array of the best chromosomes of length numbChromosomes.
	 */
	public GAChromosome[] getBestChromosomes(int numbChromosomes){
		//---- If the list of population members is not sorted, then sort it. 
		if(!isSorted){
			Collections.sort(populationMembers);
			isSorted = true; //--- Mark the population sorted.
		}
		
		//---- Build an array to store the n best chromosomes.
		GAChromosome[] bestChromosomes = new GAChromosome[numbChromosomes];
		
		//---- Copy the best chromosomes.
		for(int i = 0; i < numbChromosomes; i++){
			bestChromosomes[i] = populationMembers.get(i);
		}
		
		//---- Return the array of the best chromosomes.
		return bestChromosomes;
	}
	
	/**
	 * Adds a new chromosome to this population.
	 * 
	 * @param newChromosome  Add a new chromsome to this population.
	 */
	public void addChromosome(GAChromosome newChromosome){
		//---- Ensure not exceeding the maximum population size.
		assert(populationMembers.size() < MAXIMUM_POPULATION_SIZE);
		
		//---- Adds a new chromosome to the population.
		populationMembers.add(newChromosome);
		
		//---- Since a new population member was added, mark this no longer sorted.
		isSorted = false;
	}
	
	
	/**
	 * Scores the chromosome population based off the passed in dataset.
	 * 
	 * @param dataSet Breast Cancer Data Set by Which the Population will be Scored.
	 */
	public void scorePopulationMembers(BreastCancerDataSet dataSet){
	
		GAChromosome tempChromosome;
		int chromosomeScore;
		
		//---- Iterate through all population members and generate their score.
		for(int i = 0; i < populationMembers.size(); i++){
			
			//---- Get the current chromosome.
			tempChromosome = populationMembers.get(i);
			//---- Get the score for that chromosome.
			chromosomeScore = dataSet.getChromosomeScoreForPopulation(tempChromosome, 0);
			//---- Update the chromosome's score.
			tempChromosome.setScore(chromosomeScore);
			
		}
		
		//----- Since the population was rescored, mark it unsorted.
		isSorted = false;
		
	}
	
}
