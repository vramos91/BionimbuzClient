package br.unb.cic.bionimbuz.rest.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import br.unb.cic.bionimbuz.configuration.ConfigurationRepository;
import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.request.UploadRequest;
import br.unb.cic.bionimbuz.rest.response.UploadResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Upload extends Action {
    private static final Logger LOGGER = LoggerFactory.getLogger(Upload.class);

    private static final String REST_UPLOAD_URL = "/rest/file/upload";

    @Override
    public void setup(Client client, RequestInfo requestInfo) {
        this.target = client.target(appConfiguration.getBionimbuzAddress());
        this.request = (UploadRequest) requestInfo;
    }

    @Override
    public void prepareTarget() {
        target = target.path(REST_UPLOAD_URL);
    }

    @Override
    public UploadResponse execute() {
        logAction(REST_UPLOAD_URL, Upload.class);
        MultipartFormDataOutput multipart = new MultipartFormDataOutput();

        try {
            multipart.addFormData("file", new FileInputStream(new File(ConfigurationRepository.UPLOADED_FILES_PATH
                    + ((UploadRequest) request).getUploadedFileInfo().getName())), MediaType.MULTIPART_FORM_DATA_TYPE);
            multipart.addFormData("file_info", ((UploadRequest) request).getUploadedFileInfo(), MediaType.APPLICATION_JSON_TYPE);
        
        } catch (FileNotFoundException e) {
            LOGGER.error("[Exception - " + e.getMessage() + "]");
            
        }

        GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(multipart) {};

        UploadResponse response = target.request().post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA), UploadResponse.class);

        return response;
    }
}
