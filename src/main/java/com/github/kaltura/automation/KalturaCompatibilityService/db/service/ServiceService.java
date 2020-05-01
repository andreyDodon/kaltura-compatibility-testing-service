package com.github.kaltura.automation.KalturaCompatibilityService.db.service;

import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Services;
import com.github.kaltura.automation.KalturaCompatibilityService.db.repository.ServicesRepository;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.interfaces.IServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author andrey.dodon - 27/04/2020
 */
@Service
public class ServiceService implements IServiceService {

    @Autowired
    ServicesRepository servicesRepository;


    public List<Services> findAll() {
        List<Services> services = new ArrayList<>();
        servicesRepository.findAll().forEach(services::add);
        services.sort(Comparator.comparing(Services::getName));
        return services;
    }


    public Long count() {
        return servicesRepository.count();
    }

    public Services getServiceById(Long id) {
        return servicesRepository.findById(id).get();
    }

    public void saveOrUpdate(Services services) {
        servicesRepository.save(services);
    }

    public void deleteAll() {
        servicesRepository.deleteAll();
    }

    public void delete(Long id) {
        servicesRepository.deleteById(id);
    }


    @Override
    public List<String> findByName(String name) {
        return servicesRepository.findByName(name);
    }
}
