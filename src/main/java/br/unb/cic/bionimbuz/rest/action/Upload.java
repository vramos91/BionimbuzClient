package br.unb.cic.bionimbuz.rest.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;

import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataOutput;

import br.unb.cic.bionimbuz.configuration.Configuration;
import br.unb.cic.bionimbuz.configuration.ConfigurationLoader;
import br.unb.cic.bionimbuz.rest.request.RequestInfo;
import br.unb.cic.bionimbuz.rest.request.UploadRequest;
import br.unb.cic.bionimbuz.rest.response.UploadResponse;

public class Upload extends Action {
	private static final String REST_UPLOAD_URL = "/rest/upload";
	private UploadRequest uploadRequest;
	private WebTarget target;
	private Configuration config;
	
	@Override
	public void setup(Client client, RequestInfo requestInfo) {
		System.out.println("Upload REST client initialized...");
		
		this.config = ConfigurationLoader.readConfiguration();
		this.target = client.target(config.getBionimbuzAddress());
		this.uploadRequest = (UploadRequest) requestInfo;
	}

	@Override
	public void prepareTarget() {
		target = target.path(REST_UPLOAD_URL);
			
		System.out.println("Dispatching request to URI: " + target.getUri());
	}

	@Override
	public UploadResponse execute() {		
	    MultipartFormDataOutput multipart = new MultipartFormDataOutput();
	    
	    try {
			multipart.addFormData("file", new FileInputStream(new File(config.getUploadedFilesDirectory() + uploadRequest.getFileInfo().getName())), MediaType.MULTIPART_FORM_DATA_TYPE);
			multipart.addFormData("file_info", uploadRequest.getFileInfo(), MediaType.APPLICATION_JSON_TYPE);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	    GenericEntity<MultipartFormDataOutput> entity = new GenericEntity<MultipartFormDataOutput>(multipart) {};

	    UploadResponse response = target.request().post(Entity.entity(entity, MediaType.MULTIPART_FORM_DATA), UploadResponse.class);
	    
	    return response;
	}
}
