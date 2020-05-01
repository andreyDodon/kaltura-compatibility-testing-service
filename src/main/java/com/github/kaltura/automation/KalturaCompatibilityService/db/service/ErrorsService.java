package com.github.kaltura.automation.KalturaCompatibilityService.db.service;

import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Errors;
import com.github.kaltura.automation.KalturaCompatibilityService.db.repository.ErrorsRepository;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.interfaces.IErrorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author andrey.dodon - 01/05/2020
 */
@Service
public class ErrorsService implements IErrorService {

    @Autowired
    private ErrorsRepository errorsRepository;


    public List<Errors> findAll() {
        List<Errors> errors = new ArrayList<>();
        errorsRepository.findAll().forEach(errors::add);
        errors.sort(Comparator.comparing(Errors::getName));
        return errors;
    }


    public Long count() {
        return errorsRepository.count();
    }

    public Errors getErrorsById(Long id) {
        return errorsRepository.findById(id).orElse(null);
    }

    public void saveOrUpdate(Errors errors) {
        errorsRepository.save(errors);
    }

    public void deleteAll() {
        errorsRepository.deleteAll();
    }

    public void delete(Long id) {
        errorsRepository.deleteById(id);
    }


    @Override
    public List<String> findByName(String name) {
        return errorsRepository.findByName(name);
    }
}
