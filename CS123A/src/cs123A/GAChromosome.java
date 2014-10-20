package cs123A;

import java.util.Arrays;
import java.util.Random;

public class GAChromosome implements Comparable<GAChromosome> {

	//---- Stores the gain and offset information for the genetic algorithm chromosome.
	private int[] gainVector;
	private int offset;
	private int chromosomeScore;
	
	private static Random randomGenerator;
	
	
	/**
	 * Constructor for Genetic Algorithm Chromosome. 
	 */
	private GAChromosome(){
		//---- Create the gain and offset vectors for the population.
		gainVector = new int[Patient.numberDataElementsPerPatient()];
		offset = 0;
		
		//--- Create the random number generator if it does not exist. 
		if(randomGenerator == null)
			randomGenerator = new Random();
		
		//---- Give a chromosome the minimum score by default as a check.
		chromosomeScore = Integer.MIN_VALUE;
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
	
	/**
	 * Performs n-point crossover for two chromosomes.
	 * 
	 * @param otherChromosome Other Chromosome that will be crossed over.
	 * @return Crossed over Genetic Algorithm Chromosome.
	 */
	public GAChromosome crossover(GAChromosome otherChromosome, int numb_points){
		
		//---- Extract the chromosome data for the two parent chromosomes.
		int[] implicitGAGainVector = this.getGainVector();
		int[] otherGAGainVector = otherChromosome.getGainVector();
		
		//---- Create the data vectors
		int[] implicitDataVector = new int[implicitGAGainVector.length + 1];
		int[] otherDataVector = new int[implicitGAGainVector.length + 1];
		int[] childDataVector = new int[implicitGAGainVector.length + 1];
		
		//---- Create the data vectors for the two parents and the child.
		int i, lastIndex;
		//---- Copy the gain components first.
		for(i = 0; i < otherGAGainVector.length; i++){
			implicitDataVector[i] = implicitGAGainVector[i];
			otherDataVector[i] = otherGAGainVector[i];
			childDataVector[i] = 0;
		}
		//---- Copy the offset last.
		lastIndex = implicitGAGainVector.length - 1;
		implicitDataVector[lastIndex] = this.getOffset();	
		otherDataVector[lastIndex] = otherChromosome.getOffset();
		childDataVector[lastIndex] = 0;
		
		//----- Build the crossover bit location list.  This is the specific bit in the data vector
		//----- where crossover will occur.
		int[] crossover_loc = new int[numb_points]; 
		for(i = 0; i < numb_points; i++){
			crossover_loc[i] = randomGenerator.nextInt(childDataVector.length*32);
		}
		//---- Sort the crossover locations.
		Arrays.sort(crossover_loc);
		
		//---- At first use the implicit parameter for boolean.
		boolean useImplicitInCrossover = true;
		int byteIndex = 0;
		int crossoverIndex = 0;
		while(byteIndex < childDataVector.length){
			
			//---- This number does not contain a crossover point so copy it entirely.
			if(crossoverIndex == crossover_loc.length || byteIndex < crossover_loc[crossoverIndex] / 32){
				if(useImplicitInCrossover)
					childDataVector[byteIndex] = implicitDataVector[byteIndex];
				else
					childDataVector[byteIndex] = otherDataVector[byteIndex];
				//---- Increment the byte index.
				byteIndex++;
			}
			else{
				//---- Always copy MSB first.
				int previousCrossoverBitLocation = 31;
				int crossoverBitLocation;
				int referenceWord = 0;
				int dataWord = 0;
				//---- Continue crossing over until the end of the word is reached
				while(crossoverIndex < crossover_loc.length && byteIndex == crossover_loc[crossoverIndex] / 32){
					
					//---- Extract the reference byte.
					if(useImplicitInCrossover)
						referenceWord = implicitDataVector[byteIndex];
					else
						referenceWord = otherDataVector[byteIndex];	
					
					//----- Extract the crossover bit location.
					crossoverBitLocation = 31 - crossover_loc[crossoverIndex] % 32;
				
					//----- Update the word with the crossed over data vector.
					dataWord |= extractIntegerBits(referenceWord, previousCrossoverBitLocation, crossoverBitLocation);
					previousCrossoverBitLocation = crossoverBitLocation;
					
					//---- Go to the next crossover point.
					crossoverIndex++;
					//---- Switch the value of the boolean flag on which data vector to use.
					useImplicitInCrossover = !useImplicitInCrossover;
				}
				
				//---- Extract the remanent of the word.
				if(useImplicitInCrossover)
					referenceWord = implicitDataVector[byteIndex];
				else
					referenceWord = otherDataVector[byteIndex];	
				//----- Update the word with the crossed over data vector.
				dataWord |= extractIntegerBits(referenceWord, previousCrossoverBitLocation, -1);
				
				//---- Store the crossed over data word.
				childDataVector[byteIndex] = dataWord;
								
				//---- Increment the byte index.
				byteIndex++;
			}
			

		}
		
		//---- Create a new child chromosome.
		GAChromosome newChromosome = new GAChromosome();
		//---- Extract the gain vector
		int[] childGainVector = Arrays.copyOfRange(childDataVector, 0, childDataVector.length - 1);
		//---- Extract the offset
		int childOffset = childDataVector[childDataVector.length - 1];
		newChromosome.updateChromosomeParameters(childGainVector, childOffset);
		//---- Return the new child.
		return newChromosome;
		
	}
	
	
	/**
	 * Extracts a subset of bits from a word.  Used in bitwise crossover.
	 * 
	 * @param dataWord			Word whose bits are being excluded.
	 * @param maxBitPosition	Bit number of highest significant bit to keep (inclusive)
	 * @param minBitPosition	Bit number of highest position bit to exclude after starting bit keeping (exclusive)
	 * @return					Word[maxBitPosition:minBitPosition) as an integer.
	 */
	static private int extractIntegerBits(int dataWord, int maxBitPosition, int minBitPosition){
		
		//---- Handle the case where the whole word was to be extracted.
		if(maxBitPosition - minBitPosition == 32)
			return dataWord;
		//---- Return the case where the two positions are equal and not both zero.
		else if(maxBitPosition == minBitPosition)
			return 0;
		
		//---- Shift the word over by the number of bits in the min position.
		dataWord = dataWord >>> (minBitPosition + 1);
		
		//---- Remove any preceding bits.
		dataWord %= Math.pow(2, maxBitPosition-minBitPosition);
		
		//---- Reshift the word back.
		dataWord = dataWord << (minBitPosition + 1);
				
		return dataWord;
	}
	
	
	/**
	 * 
	 * @param newGainVector Gain vector that will overwrite the chromosome's existing gain vector.
	 * @param newOffset  Offset scalar that will overwrite the chromosome's existing offset scalar.
	 */
	private void updateChromosomeParameters(int[] newGainVector, int newOffset){
		
		int i;
		
		//--- Iterate through the gain vector and update its values.
		for(i = 0; i < gainVector.length; i++)
			gainVector[i] = newGainVector[i];
		
		//--- Update the offset.
		offset = newOffset;
	}
	
	
	/**
	 * Sorting function for GA Chromosomes.  It sorts them in descending order based off the chromosome score.
	 */
	@Override
	public int compareTo(GAChromosome other){
		//----- Ensure the object is of the same class.
		if(other.getClass() != this.getClass()) return 0;
		
		//---- Cast the other object to a GAChromsome
		GAChromosome otherCasted = (GAChromosome)other;
		
		//---- Chromosomes with higher scores go first in the sort.
		if(this.chromosomeScore > otherCasted.chromosomeScore)
			return -1;
		else
			return 1;
		
	}
	
	
	/**
	 * Returns the score of the chromosome.
	 * 
	 * @param newScore New score for the chromosome.
	 */
	public int getScore(){
		return chromosomeScore;
	}	
	
	/**
	 * Updates the score of a chromosome.
	 * 
	 * @param newScore New score for the chromosome.
	 */
	public void setScore(int newScore){
		chromosomeScore = newScore;
	}
	
}
