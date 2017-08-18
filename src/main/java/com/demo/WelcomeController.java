package com.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.demo.model.Employee;
import com.demo.repository.EmployeeRepository;

@Controller
public class WelcomeController {
	private static String UPLOADED_FOLDER = "D://temp//";

	@Autowired
	EmployeeRepository employeeRepository;

	@RequestMapping("/")
	public String welcome(Map<String, Object> model) {
		return "welcome";
	}

	/* Excel upload */
	@PostMapping("/upload")
	public String singleFileUpload(@RequestParam("file") MultipartFile file, Model model) {
		if (file.isEmpty()) {
            model.addAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }
		try {
			byte[] bytes = file.getBytes();
			Path path = Paths.get(UPLOADED_FOLDER + file.getOriginalFilename());
			Files.write(path, bytes);
			readFromExcelFile(UPLOADED_FOLDER + file.getOriginalFilename());
			model.addAttribute("message","You successfully uploaded '" + file.getOriginalFilename() + "'");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "welcome";
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

	public void create(Employee employee) {
		employee.setCreationDate(new Date());
		employee = employeeRepository.save(employee);

	}

}