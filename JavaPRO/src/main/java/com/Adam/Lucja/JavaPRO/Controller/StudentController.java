package com.Adam.Lucja.JavaPRO.Controller;

import com.Adam.Lucja.JavaPRO.DTO.Request.AuthRequest;
import com.Adam.Lucja.JavaPRO.DTO.Request.StudentRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.AuthResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.StudentResponse;
import com.Adam.Lucja.JavaPRO.Service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studenty/")
public class StudentController {
    @Autowired
    private AuthController authController;

    @Autowired
    private StudentService studentService;

    @GetMapping
    public ResponseEntity<List<StudentResponse>> getAllStudenty(){
        return ResponseEntity.ok(studentService.getAllStudenty());
    }
    @GetMapping("{id}")
    public ResponseEntity<StudentResponse> getStudent(@PathVariable("id") Long id){
        return ResponseEntity.ok(studentService.getStudent(id));
    }
    @PostMapping
    public ResponseEntity<?> createStudent(@RequestBody StudentRequest studentRequest){
        return authController.registerStudent(studentRequest);
    }
    @PutMapping("{id}")
    public ResponseEntity<StudentResponse> updateStudent(@RequestBody StudentRequest studentRequest, @PathVariable("id") Long id) {
        return ResponseEntity.ok(studentService.updateStudent(studentRequest, id));
    }
    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteStudent (@PathVariable Long id){
        studentService.deleteStudent(id);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/login")
    public ResponseEntity<?> studentLogin(@RequestBody AuthRequest authRequest){
        return authController.getToken(authRequest);
    }
}
