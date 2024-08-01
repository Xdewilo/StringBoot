package com.rutaoptimaspring.cargadatosbig;

import com.monitorjbl.xlsx.StreamingReader;
import com.rutaoptimaspring.cargadatosbig.Repository.CustomerRepository;
import com.rutaoptimaspring.cargadatosbig.entity.Customer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.yaml.snakeyaml.reader.StreamReader;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@SpringBootApplication
@RequiredArgsConstructor
public class CargaDatosBigApplication implements CommandLineRunner {

	private final CustomerRepository customerRepository;

	public static void main(String[] args) {
		SpringApplication.run(CargaDatosBigApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		long startTimeReading = System.currentTimeMillis();
		log.info("Reading data from the database...");

		InputStream inputStream = new FileInputStream("src/main/resources/customers.xlsx");
		Workbook workbook = StreamingReader.builder()
				.rowCacheSize(100)    // number of rows to keep in memory (defaults to 10)
				.bufferSize(4096)     // buffer size to use when reading InputStream to file (defaults to 1024)
				.open(inputStream);

		List<Customer> customers = StreamSupport.stream(workbook.getSheetAt(0).spliterator(), false)
				.skip(1)
				.map(row -> {
					Customer customer = new Customer();
					customer.setName(row.getCell(0).getStringCellValue());
					customer.setLastName(row.getCell(1).getStringCellValue());
					customer.setAddress(row.getCell(2).getStringCellValue());
					customer.setEmail(row.getCell(3).getStringCellValue());
					return customer;
				})
				.toList();

		long endTimeReading = System.currentTimeMillis();
		log.info("Reading finished, time"+( endTimeReading - startTimeReading)+"ms");

		long startTimeSaving = System.currentTimeMillis();
		customerRepository.saveAll(customers);
		long endTimeSaving = System.currentTimeMillis();
	}
}
