package com.example.demoproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RestController;
import com.example.demoproject.service.PdfFieldMapper;

import java.nio.file.Files;
import java.nio.file.Paths;


@RestController
@SpringBootApplication
public class DemoApplication {
	

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/hello")
	public void sayHello() throws Exception {
 		String json = new String(Files.readAllBytes(Paths.get("input.json")));
        new PdfFieldMapper()
            .dryRun(true)
            .mapJsonToPdf("config.yaml", json, "template.pdf", "output.pdf");
    }
}




