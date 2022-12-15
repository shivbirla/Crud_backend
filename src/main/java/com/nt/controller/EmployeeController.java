package com.nt.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Resource;
import javax.xml.ws.Response;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.aspectj.util.FileUtil;
import org.hibernate.jpa.boot.internal.Helper;
import org.hibernate.query.criteria.internal.expression.function.SubstringFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import com.nt.model.Converter;
import com.nt.model.Employee;
import com.nt.model.FileDB;
import com.nt.repository.EmployeeRepository;
import com.nt.repository.FileDBRepository;
import com.nt.service.IEmpoyeeService;
import net.bytebuddy.asm.Advice.Return;


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
	
	  @Value("${upload.path}")
	  private String Fpath;
	  
	
	FileDB filed =null;
	Employee employee=null;

	@GetMapping("/employees")
	public List<Employee> getAllEmployee() {
		return empService.getAllEmployee();
	}

	@PostMapping("/employees")
	public Employee createEmployee(@RequestBody Employee emp) {
	
		Employee createEmployee = empService.createEmployee(emp);
		return  new Employee();
	}

	@GetMapping("/employees/{id}")
	public ResponseEntity<Employee> getEmployeeById(@PathVariable Long id) {
		Employee employee = empRepo.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Employee Not Found :: " + id));
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
		employee.getFileData();
		empRepo.delete(employee);
		Map<String, Boolean> response = new HashMap<>();
		response.put("delete", Boolean.TRUE);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/employees/formdata")
	public FileDB uploadFile(@RequestParam("file") MultipartFile file) throws IOException{
		filed = new FileDB();
String path= Fpath+File.separator+file.getOriginalFilename();

String type = "";

int i =  file.getOriginalFilename().lastIndexOf('.');

   type = file.getContentType();
   if (i > 0) {
	    type =  file.getOriginalFilename().substring(i+1);

	} 
     
    String   name=  file.getOriginalFilename().substring(0, file.getOriginalFilename().indexOf("."));
		 name = name.concat(LocalDateTime.now().toString()).replace("-", "").replace( ":" , "" ).concat("."+type);
 
    	InputStream is = file.getInputStream(); 	
    	     byte[] data = new byte[is.available()];
    	     is.read(data);
    	     FileOutputStream fos = new FileOutputStream(Fpath+File.separator+name);
    	
    	     fos.write(data);
    	 	filed = new  FileDB( name, type); 
    	 	
    
  	filed.setName(name);
 	filed.setType(type);

  	return fileRepo.save(filed);
	 	
		
	}

	@GetMapping("/employees/file/{file_id}")
	public Converter getEmployeeFileById(@PathVariable("file_id") Long file_id) throws IOException {
		FileDB filed = fileRepo.findById(file_id).orElseThrow(() -> new ResourceNotFoundException("Employee Not found " + file_id));
		String path= Fpath.concat(filed.getName()) ;		
		
		byte[] imageBytes = FileUtil.readAsByteArray(new File(path));
    
	     String base64EncodedImageBytes = Base64.getEncoder().encodeToString(imageBytes);
        
		return new Converter(base64EncodedImageBytes);
		
	}
	}

