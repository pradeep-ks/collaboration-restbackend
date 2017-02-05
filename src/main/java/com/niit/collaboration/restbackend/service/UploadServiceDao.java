package com.niit.collaboration.restbackend.service;

import java.io.InputStream;

import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;

public interface UploadServiceDao {

	Response upload(InputStream inStream, FormDataContentDisposition fileDetails);
	
}
