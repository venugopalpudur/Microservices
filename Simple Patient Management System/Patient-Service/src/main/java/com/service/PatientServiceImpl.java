package com.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.model.Patient;
import com.repository.PatientRepository;

@Service
public class PatientServiceImpl implements PatientService{

	@Autowired
	private PatientRepository repo;
	
	public List<Patient> getAllPatients() {
		return repo.findAll();
	}
	
	public boolean addPatient(Patient pt) {
		if(repo.save(pt) != null) {
			return true;
		}
		return false;
	}

	@Override
	public Patient getPatientById(long id) {
		if(repo.findById(id).isPresent()) {
			return (Patient)repo.findById(id).get();
		}
		return null;
	}

	@Override
	public boolean updatePatient(Patient pt, long id) {
		if(repo.findById(id).isPresent()) {
			Patient updatePatient = repo.findById(id).get();
			updatePatient.setFirstName(pt.getFirstName());
			updatePatient.setLastName(pt.getLastName()); 
			updatePatient.setServices(pt.getServices());
			if(repo.save(updatePatient) != null) {
				return true;
			}
			return false;
		}
		return false;
	}

	@Override
	public boolean deletePatientById(long id) {
		if(repo.findById(id).isPresent()) {
			repo.deleteById(id);
			return true;
		}
		return false;
	}

	@Override
	public boolean deletePatients() {
		if(!repo.findAll().isEmpty()) {
			repo.deleteAll();
			return true;
		}
		return false;
	}
}
