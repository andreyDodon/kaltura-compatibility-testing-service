package com.github.kaltura.automation.KalturaCompatibilityService.db.service;

import com.github.kaltura.automation.KalturaCompatibilityService.db.model.Yellow;
import com.github.kaltura.automation.KalturaCompatibilityService.db.repository.YellowRepository;
import com.github.kaltura.automation.KalturaCompatibilityService.db.service.interfaces.IYellowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author andrey.dodon - 05/06/2020
 */
@Service
public class YellowService implements IYellowService {

    @Autowired
    private YellowRepository yellowRepository;


    public List<Yellow> findAll() {
        List<Yellow> yellows = new ArrayList<>();
        yellowRepository.findAll().forEach(yellows::add);
        yellows.sort(Comparator.comparing(Yellow::getName));
        return yellows;
    }


    @Override
    public List<String> findByName(String name) {
        return yellowRepository.findByName(name);
    }

    public Long count() {
        return yellowRepository.count();
    }

    public Yellow getYellowById(Long id) {
        return yellowRepository.findById(id).orElse(null);
    }

    public void saveOrUpdate(Yellow yellow) {
        yellowRepository.saveAndFlush(yellow);
    }

    public void deleteAll() {
        yellowRepository.deleteAll();
    }

    public void delete(Long id) {
        yellowRepository.deleteById(id);
    }
}
