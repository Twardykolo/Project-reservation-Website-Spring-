package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.DTO.Response.TematResponse;
import com.Adam.Lucja.JavaPRO.Entity.Temat;
import com.Adam.Lucja.JavaPRO.Repository.TematRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TematService {
    @Autowired
    private TematRepository tematRepository;

    public List<TematResponse> getAllTematy(){
        List<Temat> tematy =tematRepository.findAll();
        List<TematResponse> wynikFunkcji = new ArrayList<>();
        for (Temat temat: tematy) {
            wynikFunkcji.add(new TematResponse(temat));
        }
        return wynikFunkcji;
    }
}
