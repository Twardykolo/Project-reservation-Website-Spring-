package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.DTO.Request.TematRequest;
import com.Adam.Lucja.JavaPRO.DTO.Response.TematResponse;
import com.Adam.Lucja.JavaPRO.Entity.Temat;
import com.Adam.Lucja.JavaPRO.Repository.TematRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public TematResponse getTemat(Long id){
        return new TematResponse(tematRepository.findById(id).get());
    }

    public TematResponse createTemat(TematRequest tematRequest){
        Temat temat = Temat.builder()
                .name(tematRequest.getName())
                .description(tematRequest.getDescription())
                .isReserved(false).build();
        Temat savedTemat = tematRepository.save(temat);
        TematResponse zwrotka = new TematResponse(savedTemat);
        return zwrotka;
    }

    public TematResponse updateTemat(Long id,TematRequest tematRequest) {
        Temat temat = tematRepository.findById(id).get();
        temat.setName(tematRequest.getName());
        temat.setDescription(tematRequest.getDescription());
        temat.setIsReserved(tematRequest.getIsReserved().get());
        Temat savedTemat = tematRepository.save(temat);
        return new TematResponse(savedTemat);
    }

    public void deleteTemat(Long id) {
        Temat temat = tematRepository.findById(id).get();
        tematRepository.delete(temat);
    }
}
