package com.github.kaltura.automation.KalturaCompatibilityService.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ServiceConfiguration {

    @Bean
    public Jaxb2Marshaller getCastorMarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setPackagesToScan("com.github.kaltura.automation.KalturaCompatibilityService.model");
        Map<String, Object> map = new HashMap<>();
        map.put("jaxb.formatted.output", true);
        jaxb2Marshaller.setMarshallerProperties(map);
        return jaxb2Marshaller;
    }

    @Bean
    public ObjectMapper mapper() {
        return new ObjectMapper();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact();
        contact.setEmail("andrey.dodon@kaltura.com");
        contact.setName("Andrey Dodon");

        OpenAPI openApi = new OpenAPI();

        return openApi
                .components(new Components())
                .info(new Info().title("Kaltura Compatibility testing service").description(
                        "This is a Kaltura Testing infrastructure based on " +
                                "Spring Boot RESTful service using springdoc-openapi and OpenAPI 3.").version("1.1.1")
                        .contact(contact));
    }
}