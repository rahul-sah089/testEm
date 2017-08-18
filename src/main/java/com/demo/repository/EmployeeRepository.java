package com.demo.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

import com.demo.model.Employee;

@Transactional
public interface EmployeeRepository extends MongoRepository<Employee, String> {
	
	  //public Employee findByDeparture(String departure);
}
