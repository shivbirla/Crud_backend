package com.nt.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.jpa.boot.internal.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nt.exception.ResourceNotFoundException;
import com.nt.model.Employee;
import com.nt.model.FileDB;
import com.nt.repository.EmployeeRepository;
import com.nt.repository.FileDBRepository;
import com.nt.service.IEmpoyeeService;
import com.sun.org.apache.xpath.internal.operations.Bool;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/")
public class EmployeeController {
	

	@Autowired
	private IEmpoyeeService empService;

	@Autowired
	private EmployeeRepository empRepo;
	
	@Autowired
	 private FileDBRepository fileRepo;
	
	FileDB filed =null;

	@GetMapping("/employees")
	public List<Employee> getAllEmployee() {
		return empService.getAllEmployee();
	}

	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee emp) {
		
		Employee createEmployee = empService.createEmployee(emp);
		System.out.println(createEmployee);
		return createEmployee;
	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
		Employee employee = empRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee Not Found :: " + id));
		System.out.println(employee);
		return ResponseEntity.ok(employee);
	}

	@PutMapping("/employees/{id}")
	public ResponseEntity<Employee> updateEmployee(@PathVariable Long id, @RequestBody Employee employeeDetails) {
		Employee employee = empRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee Not found " + id));

		employee.setId(employeeDetails.getId());
		employee.setFirstName(employeeDetails.getFirstName());
		employee.setLastName(employeeDetails.getLastName());
		employee.setEmailId(employeeDetails.getEmailId());
		employee.setGender(employeeDetails.getGender());
		employee.setHobbies(employeeDetails.getHobbies());

		Employee updateEmployee = empRepo.save(employee);
		
		return ResponseEntity.ok(updateEmployee);
	}

	@DeleteMapping("/employees/{id}")
	public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long id) {
		Employee employee = empRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee not found " + id));
		empRepo.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("delete", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/employees/formdata")
	public ResponseEntity<FileDB> uploadFile(@RequestParam("file") MultipartFile file) throws IOException{
		
		 filed 	= new FileDB();
		filed.setName(file.getOriginalFilename());
		filed.setData(file.getBytes());
		filed.setType(file.getContentType());
				
         FileDB save = fileRepo.save(filed);
         System.out.println(save.toString());
	 	return  ResponseEntity.ok(save);
		
	}

	}

