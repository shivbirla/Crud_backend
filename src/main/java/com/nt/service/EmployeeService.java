package com.nt.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nt.exception.ResourceNotFoundException;
import com.nt.model.Employee;
import com.nt.repository.EmployeeRepository;

@Service
public class EmployeeService implements IEmpoyeeService {

	@Autowired
	private EmployeeRepository empRepo;

	@Override
	public List<Employee> getAllEmployee() {
		List<Employee> allEmp = empRepo.findAll();
		return allEmp;
	}

	@Override
	public Employee createEmployee(Employee emp) {
		Employee save = empRepo.save(emp);
		return save;
	}

}
