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
		idNumber = Long.getLong(features[index++]);
		
		//---- Get clump thickness 
		clumpThickness = Integer.getInteger(features[index++]);
		assert( clumpThickness >= 1 && clumpThickness <= 10 );
		
		//---- Get uniformity of cell size
		cellSizeUniformity = Integer.getInteger(features[index++]);
		assert( cellSizeUniformity >= 1 && cellSizeUniformity <= 10 );
		
		//---- Get uniformity of cell shape
		cellShapeUniformity = Integer.getInteger(features[index++]);
		assert( cellShapeUniformity >= 1 && cellShapeUniformity <= 10 );
		
		//---- Get uniformity of cell shape
		marginalAdhesion = Integer.getInteger(features[index++]);
		assert( marginalAdhesion >= 1 && marginalAdhesion <= 10 );
		
		//---- Get single epithelial cell size
		singleEpithelialCellSize = Integer.getInteger(features[index++]);
		assert( singleEpithelialCellSize >= 1 && singleEpithelialCellSize <= 10 );
		
		//---- Get bare nucleoli
		bareNucleoli = Integer.getInteger(features[index++]);
		assert( bareNucleoli >= 1 && bareNucleoli <= 10 );
		
		//---- Get bland chromatin rating.
		blandChromatin = Integer.getInteger(features[index++]);
		assert( blandChromatin >= 1 && blandChromatin <= 10 );
		
		//---- Get uniformity of cell shape
		normalNucleoli = Integer.getInteger(features[index++]);
		assert( normalNucleoli >= 1 && normalNucleoli <= 10 );
		
		//---- Get mitoses rating.
		mitoses = Integer.getInteger(features[index++]);
		assert( mitoses >= 1 && mitoses <= 10 );	
		
		//---- Check if the patient tumor is malignant or benign
		if( Integer.getInteger(features[index]) == BENIGN_INDICATOR ) malignant = false;
		else if( Integer.getInteger(features[index]) == MALIGNANT_INDICATOR ) malignant = true;
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
		linearFunctionWeight += gainVector[index++] * clumpThickness;
		linearFunctionWeight += gainVector[index++] * cellSizeUniformity;
		linearFunctionWeight += gainVector[index++] * cellShapeUniformity;
		linearFunctionWeight += gainVector[index++] * marginalAdhesion;
		linearFunctionWeight += gainVector[index++] * singleEpithelialCellSize;
		linearFunctionWeight += gainVector[index++] * bareNucleoli;
		linearFunctionWeight += gainVector[index++] * blandChromatin;
		linearFunctionWeight += gainVector[index++] * normalNucleoli;
		linearFunctionWeight += gainVector[index++] * mitoses;		

		//---- Correct for whether the tumor is malignant or not.
		if(malignant) return linearFunctionWeight;
		else return -1 * linearFunctionWeight; 				
	}
	
	
	public boolean isMalignant(){
		return malignant;
	}
	
}
