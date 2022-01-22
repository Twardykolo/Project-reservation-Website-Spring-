package com.Adam.Lucja.JavaPRO.Service;

import com.Adam.Lucja.JavaPRO.DTO.Response.ProjektResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.StudentResponse;
import com.Adam.Lucja.JavaPRO.DTO.Response.TematResponse;
import com.Adam.Lucja.JavaPRO.Entity.Projekt;
import com.Adam.Lucja.JavaPRO.Entity.Projekt2Student;
import com.Adam.Lucja.JavaPRO.Entity.Student;
import com.Adam.Lucja.JavaPRO.Repository.Projekt2StudentRepository;
import com.Adam.Lucja.JavaPRO.Repository.ProjektRepository;
import com.Adam.Lucja.JavaPRO.Repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class Projekt2StudentService {
    @Autowired
    private ProjektRepository projektRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private Projekt2StudentRepository projekt2StudentRepository;

    public List<ProjektResponse> getProjektByStudentId(Long id){
        List<Projekt2Student> listaProjektow2Student = projekt2StudentRepository.findAllByStudentId(id);
        List<ProjektResponse> wynikFunkcji = new ArrayList<>();
        for(Projekt2Student projekt2Student: listaProjektow2Student){
            wynikFunkcji.add(new ProjektResponse(projekt2Student.getProjekt()));
        }
        return wynikFunkcji;
    }
    public List<StudentResponse> getStudenciByProjektId(Long id){
        List<Projekt2Student> listaProjektow2Student = projekt2StudentRepository.findAllByProjektId(id);
        List<StudentResponse> wynikFunkcji = new ArrayList<>();
        for(Projekt2Student projekt2Student: listaProjektow2Student){
            wynikFunkcji.add(new StudentResponse(projekt2Student.getStudent()));
        }
        return wynikFunkcji;
    }

}
