package com.advanceauth.service;

import com.advanceauth.entity.Student;
import com.advanceauth.repository.StudentRepository;
import com.advanceauth.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public ApiResponse createStudent(Student student) {
        studentRepository.save(student);
        return new ApiResponse("Student created successfully", true);
    }
}
