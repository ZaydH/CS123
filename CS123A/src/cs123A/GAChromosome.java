package cs123A;

import java.util.Random;

public class GAChromosome {

	//---- Stores the gain and offset information for the genetic algorithm chromosome.
	private int[] gainVector;
	private int offset;
	
	private static Random randomGenerator;
	
	
	/**
	 * Constructor for Genetic Algorithm Chromosome. 
	 */
	private GAChromosome(){
		gainVector = new int[Patient.NUMBER_OF_FEATURES - 2];
		offset = 0;
		
		//--- Create the random number generator if it does not exist. 
		if(randomGenerator == null)
			randomGenerator = new Random();
	}
	
	/**
	 * Only a static constructor for Genetic Algorithm Chromosome.
	 * It creates a random gain vector and offset.
	 * 
	 * @return Genetic Algorithm Chromosome.
	 */
	public static GAChromosome createRandomChromosome(){
		//---- Creates an empty chromosome.
		GAChromosome randomChromosome = new GAChromosome();
		
		//---- Get a random value for 
		randomChromosome.offset = randomGenerator.nextInt();
		
		//---- Iterate through the gain vector settings
		for(int i = 0; i < randomChromosome.gainVector.length; i++)
			randomChromosome.gainVector[i] = randomGenerator.nextInt();
		
		//---- Return the created random chromosome.
		return randomChromosome;
	}
	
	
	/**
	 * Accessor for the chromosome's offset.
	 * 
	 * @return  Integer value of the offset.
	 */
	public int getOffset(){
		return offset;
	}
	

	/**
	 * Accessor for the chromosome's gain vector.
	 * 
	 * @return Copy of the chromosome's gain vector.
	 */
	public int[] getGainVector(){
		
		//---- Create a copy of the gain vector for use in calculations.
		int[] outputGainVector = new int[gainVector.length];
		
		//---- Copy over the gain vector.
		for(int i = 0; i < gainVector.length; i++)
			outputGainVector[i] = gainVector[i];
		
		return outputGainVector;
	}	
	
}
