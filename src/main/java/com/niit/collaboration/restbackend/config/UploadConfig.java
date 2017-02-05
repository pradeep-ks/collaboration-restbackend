package com.niit.collaboration.restbackend.config;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

import com.niit.collaboration.restbackend.service.UploadServiceDaoImpl;

@ApplicationPath("/rest")
public class UploadConfig extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> s = new HashSet<Class<?>>();
		s.add(UploadServiceDaoImpl.class);
		s.add(MultiPartFeature.class);
		return s;
	}
/*
	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put("jersey.config.server.provider.classnames", "org.glassfish.jersey.media.multipart.MultiPartFeature");
		return super.getProperties();
	}
*/
}
