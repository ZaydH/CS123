package cs123A;

import java.util.ArrayList;

public class GAChromosomePopulation {

	private static int NUMBER_OF_POPULATION_CHROMOSOMES = 1000;
	private static int NUMBER_OF_GENERATIONS = 100;
	private ArrayList<GAChromosome> populationMembers;
	
	/**
	 * Constructor for the GAChromosomePopulation class.  
	 * It creates an empty array of population members.
	 */
	private GAChromosomePopulation(){
		populationMembers = new ArrayList<GAChromosome>();
	}
	
	
	/**
	 * Static constructor to create a random genetic algorithm population.
	 * 
	 * @return Randomly generated genetic algorithm population.
	 */
	public static GAChromosomePopulation createRandomPopulation(){
		//---- Creates the set of population members
		GAChromosomePopulation chromosomePopulation = new GAChromosomePopulation();
		
		//---- Create the random population 
		while(chromosomePopulation.populationMembers.size() < NUMBER_OF_POPULATION_CHROMOSOMES){
			chromosomePopulation.populationMembers.add(GAChromosome.createRandomChromosome());
		}
		
		//---- Return the randomly generated chromosome population.
		return chromosomePopulation;
	}
	
}
