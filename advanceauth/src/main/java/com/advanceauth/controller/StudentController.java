package com.advanceauth.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.advanceauth.entity.Student;
import com.advanceauth.response.ApiResponse;
import com.advanceauth.service.StudentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

	private final StudentService studentService;

	@PostMapping("/create")
	public ApiResponse addStudent(@RequestBody Student student) {
		return studentService.createStudent(student);
	}
}
