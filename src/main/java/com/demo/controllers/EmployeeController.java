/**
 * 
 */
package com.demo.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.model.Employee;
import com.demo.repository.EmployeeRepository;

@RestController
public class EmployeeController {

	// Save the uploaded file to this folder
	private static String UPLOADED_FOLDER = "D://temp//";

	@Autowired
	EmployeeRepository employeeRepository;

	@RequestMapping("/create")
	public ResponseEntity<?> create(Employee employee) {
		employee.setCreationDate(new Date());
		try {
			employee = employeeRepository.save(employee);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(employee, HttpStatus.OK);
	}

	@RequestMapping("/read")
	public ResponseEntity<?> read(@RequestParam String employeeId) {
		Employee employee = new Employee();
		try {
			employee = employeeRepository.findOne(employeeId);
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(employee, HttpStatus.OK);
	}

	/**
	 * GET /update --> Update a booking record and save it in the database.
	 */
	@RequestMapping("/update")
	public ResponseEntity<?> update(Employee employee) {
		try{
			String employeeId = employee.getId();
			String employeeFirstname = employee.getEmployeeFirstName();
			String employeeLastname = employee.getEmployeeLastName();
			String employeeDepartment =  employee.getEmployeeDepartment();
			employee = employeeRepository.findOne(employeeId);
			employee.setEmployeeFirstName(employeeFirstname);
			employee.setEmployeeLastName(employeeLastname);
			employee.setEmployeeDepartment(employeeDepartment);
			employee = employeeRepository.save(employee);
		}catch(Exception ex){
			return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * GET /delete --> Delete a booking from the database.
	 */
	@RequestMapping("/delete")
	public ResponseEntity<?> delete(@RequestParam String employeeId) {
		try{
			employeeRepository.delete(employeeId);
		}catch(Exception ex){
			return new ResponseEntity<>(false,HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(true,HttpStatus.OK);
		
	}

	/**
	 * GET /read --> Read all booking from the database.
	 */
	@RequestMapping(path = "/read-all", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> readAll() {
		List<Employee> employees = new ArrayList<Employee>();
		try {
			employees = employeeRepository.findAll();
		} catch (Exception ex) {
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>(employees, HttpStatus.OK);
	}

	

	public void readFromExcelFile(String path) {
		try {
			FileInputStream file = new FileInputStream(new File(path));

			// Create Workbook instance holding reference to .xlsx file
			XSSFWorkbook workbook = new XSSFWorkbook(file);

			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);

			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			Row row = null;
			row = rowIterator.next();
			while (rowIterator.hasNext()) {
				row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				int i = 0;
				Employee employee = new Employee();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();
					if (i == 0) {
						employee.setEmployeeFirstName(cell.getStringCellValue());
					} else if (i == 1) {
						employee.setEmployeeLastName(cell.getStringCellValue());
					} else if (i == 2) {
						employee.setEmployeeDepartment(cell.getStringCellValue());
					}
					i++;
				}
				create(employee);
				System.out.println("");
			}
			file.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@GetMapping("/uploadStatus")
	public String uploadStatus() {
		return "uploadStatus";
	}

	public void createEmployeeXls(String downloadPath) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet("Employee Data");
		List<Employee> employees = employeeRepository.findAll();
		Map<String, Object[]> data = new TreeMap<String, Object[]>();
		data.put("1", new Object[] { "FIRSTNAME", "LASTNAME", "DEPARMENT" });
		int i = 2;
		for (Employee employee : employees) {
			data.put(String.valueOf(i), new Object[] { employee.getEmployeeFirstName(), employee.getEmployeeLastName(),
					employee.getEmployeeDepartment() });
			i++;
		}
		// Iterate over data and write to sheet
		Set<String> keyset = data.keySet();
		int rownum = 0;
		for (String key : keyset) {
			Row row = sheet.createRow(rownum++);
			Object[] objArr = data.get(key);
			int cellnum = 0;
			for (Object obj : objArr) {
				Cell cell = row.createCell(cellnum++);
				if (obj instanceof String)
					cell.setCellValue((String) obj);
				else if (obj instanceof Integer)
					cell.setCellValue((Integer) obj);
			}
		}
		try {
			// Write the workbook in file system
			FileOutputStream out = new FileOutputStream(new File(downloadPath));
			workbook.write(out);
			out.close();
			System.out.println("howtodoinjava_demo.xlsx written successfully on disk.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping("/downloadExcel")
	public void downloadExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String downloadPath = "D:\\temp\\download\\employee.xlsx";
		createEmployeeXls(downloadPath);
		FileInputStream inputStream = new FileInputStream(new File(downloadPath));
		response.setHeader("Content-Disposition", "attachment; filename=\"testExcel.xlsx\"");
		response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
		ServletOutputStream outputStream = response.getOutputStream();
		IOUtils.copy(inputStream, outputStream);
		outputStream.close();
		inputStream.close();
	}

}
