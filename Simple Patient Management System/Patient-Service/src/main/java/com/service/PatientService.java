package com.service;

import java.util.List;

import org.springframework.stereotype.Service;
import com.model.Patient;


public interface PatientService {
	
	public List<Patient> getAllPatients();
	
	public boolean addPatient(Patient pt);
	
	public Patient getPatientById(long id);
	
	public boolean updatePatient(Patient pt, long id);
	
	public boolean deletePatientById(long id);
	
	public boolean deletePatients();
	
}
