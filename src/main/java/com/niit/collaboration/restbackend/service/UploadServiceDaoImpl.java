package com.niit.collaboration.restbackend.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

@Path("/profile")
public class UploadServiceDaoImpl implements UploadServiceDao {

	private static final String IMG_LOCATION = "/home/pkumar/workspace/images/";
	private static final String IMG_PREFIX = "Pic_";
	
	@Context
	private HttpServletRequest request;
	
	@POST
	@Path("/uploadImg")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response upload(@FormDataParam("file") InputStream inStream, @FormDataParam("file") FormDataContentDisposition fileDetails) {
		HttpSession session = request.getSession();
		System.out.println(session.toString());
		long userId = (Long) session.getAttribute("loggedInUserId");
		
		String loc = IMG_LOCATION + fileDetails.getFileName();
		System.out.println(loc);
		File oldFileName = new File(loc);
		String extension = getExtension(fileDetails.getFileName());
		
		File newFileName = new File(IMG_LOCATION + IMG_PREFIX + userId + "." + extension);
		System.out.println(newFileName.getAbsoluteFile());
		oldFileName.renameTo(newFileName);
		try {
			OutputStream outStream = new FileOutputStream(new File(loc));
			
			int read = 0;
			byte[] buffer = new byte[1048576];
			while ((read = inStream.read(buffer)) != -1) {
				outStream.write(buffer, 0, read);
			}
			
			outStream.flush();
			outStream.close();
		} catch (IOException ex) {
			ex.printStackTrace(System.err);
		}
		String output = "File Uploaded To: " + loc;
		return Response.ok(output).build();
	}

	private String getExtension(String fileName) {
		int dotIndex = fileName.lastIndexOf('.');
		return fileName.substring(dotIndex + 1);
	}
}
