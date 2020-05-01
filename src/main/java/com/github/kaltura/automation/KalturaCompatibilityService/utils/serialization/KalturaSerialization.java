package com.github.kaltura.automation.KalturaCompatibilityService.utils.serialization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kaltura.automation.KalturaCompatibilityService.model.KalturaXml;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.net.URL;

/**
 * @author andrey.dodon - 30/04/2020
 */
public abstract class KalturaSerialization {

    private ObjectMapper objectMapper;
    private Jaxb2Marshaller jaxb2Marshaller;


    public String writeValueAsString(Object value) {
        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public KalturaXml getKalturaXmlObject(URL url) throws IOException {
        return (KalturaXml) jaxb2Marshaller.unmarshal(new StreamSource(url.openStream()));
    }

    @Autowired
    public final void setJaxb2Marshaller(Jaxb2Marshaller jaxb2Marshaller) {
        this.jaxb2Marshaller = jaxb2Marshaller;
    }

    @Autowired
    public final void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }


}
