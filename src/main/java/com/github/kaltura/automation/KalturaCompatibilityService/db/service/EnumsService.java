package com.github.kaltura.automation.KalturaCompatibilityService.db.service;

import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Enums;
import com.github.kaltura.automation.KalturaCompatibilityService.db.repository.EnumsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class EnumsService implements IEnumService {

    @Autowired
    EnumsRepository enumsRepository;

    public List<Enums> findAll() {
        List<Enums> enums = new ArrayList<>();
        enumsRepository.findAll().forEach(enums::add);
        enums.sort(Comparator.comparing(Enums::getName));
        return enums;
    }


    public Long count(){
        return enumsRepository.count();
    }

    public Enums getEnumById(Long id) {
        return enumsRepository.findById(id).get();
    }

    public void saveOrUpdate(Enums enums) {
        enumsRepository.save(enums);
    }

    public void delete(Long id) {
        enumsRepository.deleteById(id);
    }

    public void deleteAll(){
        enumsRepository.deleteAll();
    }

    @Override
    public List<Enums> findByName(String name) {
        List<Enums> enums = enumsRepository.findByName(name);
        return enums;
    }
}