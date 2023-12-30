package com.example.LibraryManagement;

import com.example.LibraryManagement.Model.Admin;
import com.example.LibraryManagement.Security.SecuredUser;
import com.example.LibraryManagement.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LibraryManagementApplication{

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementApplication.class, args);
	}
//
//	@Autowired
//	AdminService adminService;
//	@Override
//	public void run(String... args) throws Exception {
//		Admin admin = Admin.builder()
//				.name("ADMIN")
//				.email("admin@lib.com")
//				.securedUser(
//						SecuredUser.builder()
//								.username("Super_Admin")
//								.password("super_admin@123")
//								.build()
//				)
//				.build();
//
//		adminService.create(admin);
//}
}
