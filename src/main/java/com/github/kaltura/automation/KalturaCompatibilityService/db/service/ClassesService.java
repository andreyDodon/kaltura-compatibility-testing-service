package com.github.kaltura.automation.KalturaCompatibilityService.db.service;

import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Classes;
import com.github.kaltura.automation.KalturaCompatibilityService.db.repository.ClassesRepository;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.interfaces.IClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author andrey.dodon - 22/04/2020
 */
@Service
public class ClassesService implements IClassService {

    @Autowired
    ClassesRepository classesRepository;

    public List<Classes> findAll() {
        List<Classes> classes = new ArrayList<>();
        classesRepository.findAll().forEach(classes::add);
        classes.sort(Comparator.comparing(Classes::getName));
        return classes;
    }


    public Long count() {
        return classesRepository.count();
    }

    public Classes getClassById(Long id) {
        return classesRepository.findById(id).get();
    }

    public void saveOrUpdate(Classes classes) {
        classesRepository.save(classes);
    }

    public void deleteAll(){
        classesRepository.deleteAll();
    }

    public void delete(Long id) {
        classesRepository.deleteById(id);
    }

    @Override
    public List<String> findByName(String name) {
        return classesRepository.findByName(name);
    }

}
