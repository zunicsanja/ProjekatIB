package ib.project;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import ib.project.rest.FileUploadController;

@Configuration
@EnableAutoConfiguration
@ComponentScan({"ib.project","ib.project.rest"})
public class FileUploadApplication {
	public static void main(String[] args) {
		new File(FileUploadController.uploadDirectory).mkdir();
		SpringApplication.run(FileUploadApplication.class, args);
	}
}
