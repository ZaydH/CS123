package cs123A;

import java.util.ArrayList;

public class BreastCancerDataSet {

	public final static int TRAINING_DATA_SET_SIZE = 200;
	ArrayList<Patient> setOfPatients;
	
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
	
	
}
