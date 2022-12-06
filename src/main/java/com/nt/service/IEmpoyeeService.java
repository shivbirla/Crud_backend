package com.nt.service;
import java.util.List;

import com.nt.model.Employee;

public interface IEmpoyeeService {

	public List<Employee> getAllEmployee();

	public Employee createEmployee(Employee emp);

}
