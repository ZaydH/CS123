package cs123A;

public class Patient {

	//---- Stores the number of 
	private final static int NUMBER_OF_FEATURES = 11;
	private final static int BENIGN_INDICATOR = 2;
	private final static int MALIGNANT_INDICATOR = 4;
	
	@SuppressWarnings("unused")
	private long idNumber;
	private int clumpThickness;
	private int cellSizeUniformity;
	private int cellShapeUniformity;
	private int marginalAdhesion;
	private int singleEpithelialCellSize;
	private int bareNucleoli;
	private int blandChromatin;
	private int normalNucleoli;
	private int mitoses;
	private boolean malignant;
	
	
	
	/**
	 * Constructor for the patient data set.
	 * 
	 * @param features String of features from the input file.
	 */
	public Patient(String features){
		//---- Split the input string based off the comma then use array constructor.
		this(features.split(","));
	}
	
	
	/**
	 * Constructor for the patient data set.
	 * 
	 * @param features Array of strings from input data set file.
	 */
	public Patient(String[] features){
		
		//----- Ensure the number of features in the file line matches the expected features for a patient.
		assert(features.length == NUMBER_OF_FEATURES);
		
		
		int index = 0; //---- Allow for easy index insertion
		//---- Extract ID number
		idNumber = Long.valueOf(features[index++]);
		
		//---- Get clump thickness 
		assert( Integer.valueOf(features[index]) >= 1 && Integer.valueOf(features[index]) <= 10 );
		clumpThickness = Integer.valueOf(features[index++]);
		
		//---- Get uniformity of cell size
		assert( Integer.valueOf(features[index]) >= 1 && Integer.valueOf(features[index]) <= 10 );
		cellSizeUniformity = Integer.valueOf(features[index++]);
		
		//---- Get uniformity of cell shape
		assert( Integer.valueOf(features[index]) >= 1 && Integer.valueOf(features[index]) <= 10 );
		cellShapeUniformity = Integer.valueOf(features[index++]);
		
		//---- Get uniformity of cell shape
		assert( Integer.valueOf(features[index]) >= 1 && Integer.valueOf(features[index]) <= 10 );		
		marginalAdhesion = Integer.valueOf(features[index++]);
		
		//---- Get single epithelial cell size
		assert( Integer.valueOf(features[index]) >= 1 && Integer.valueOf(features[index]) <= 10 );		
		singleEpithelialCellSize = Integer.valueOf(features[index++]);
		
		//---- Get bare nucleoli
		assert( Integer.valueOf(features[index]) >= 1 && Integer.valueOf(features[index]) <= 10 );		
		bareNucleoli = Integer.valueOf(features[index++]);
		
		//---- Get bland chromatin rating.
		assert( Integer.valueOf(features[index]) >= 1 && Integer.valueOf(features[index]) <= 10 );		
		blandChromatin = Integer.valueOf(features[index++]);
		
		//---- Get uniformity of cell shape
		assert( Integer.valueOf(features[index]) >= 1 && Integer.valueOf(features[index]) <= 10 );		
		normalNucleoli = Integer.valueOf(features[index++]);
		
		//---- Get mitoses rating.
		assert( Integer.valueOf(features[index]) >= 1 && Integer.valueOf(features[index]) <= 10 );		
		mitoses = Integer.valueOf(features[index++]);
		
		//---- Check if the patient tumor is malignant or benign
		if( Integer.valueOf(features[index]) == BENIGN_INDICATOR ) malignant = false;
		else if( Integer.valueOf(features[index]) == MALIGNANT_INDICATOR ) malignant = true;
		else assert(false);
		index++; //---- Increment index count.
		
	}
	
	
	/**
	 * Calculates linear weight for the linear function.  It is:
	 * 
	 * Weight = yi ( mT * x + b)
	 * 
	 * where mT is the transpose of the gain vector, b is the offset, and yi
	 * is a correction factor that is 1 if the tumor is malignant and -1 if it is benign.
	 * 
	 * @param gainVector Array of integers that serve as weight for gain for each feature
	 * @param offset	 Offset correction factor for this linear function
	 * @return
	 */
	public long calculateLinearFunction(int[] gainVector, int offset){
		
		//----- Include the offset correction.
		long linearFunctionWeight = offset;

		//----- Calculate the linear parameters for each feature.
		int index = 0;
		linearFunctionWeight += (long)gainVector[index++] * clumpThickness;
		linearFunctionWeight += (long)gainVector[index++] * cellSizeUniformity;
		linearFunctionWeight += (long)gainVector[index++] * cellShapeUniformity;
		linearFunctionWeight += (long)gainVector[index++] * marginalAdhesion;
		linearFunctionWeight += (long)gainVector[index++] * singleEpithelialCellSize;
		linearFunctionWeight += (long)gainVector[index++] * bareNucleoli;
		linearFunctionWeight += (long)gainVector[index++] * blandChromatin;
		linearFunctionWeight += (long)gainVector[index++] * normalNucleoli;
		linearFunctionWeight += (long)gainVector[index++] * mitoses;		

		//---- Correct for whether the tumor is malignant or not.
		if(malignant) return linearFunctionWeight;
		else return -1 * linearFunctionWeight; 				
	}
	
	
	public boolean isMalignant(){
		return malignant;
	}
	
}
