package com.Adam.Lucja.JavaPRO.Controller;

import com.Adam.Lucja.JavaPRO.DTO.Request.TematRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.TematResponse;
import com.Adam.Lucja.JavaPRO.Service.TematService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tematy/")
public class TematController {
    @Autowired
    private TematService tematService;

    @GetMapping
    public ResponseEntity<List<TematResponse>> getAllTematy(){
        return ResponseEntity.ok(tematService.getAllTematy());
    }

    @PostMapping
    public ResponseEntity<TematResponse> createTemat(@RequestBody TematRequest tematRequest){
        return ResponseEntity.ok(tematService.createTemat(tematRequest));
    }
}
