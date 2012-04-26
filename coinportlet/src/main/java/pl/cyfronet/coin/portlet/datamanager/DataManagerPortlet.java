package pl.cyfronet.coin.portlet.datamanager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

@Controller
@RequestMapping("VIEW")
public class DataManagerPortlet {
	private final Logger log = LoggerFactory.getLogger(DataManagerPortlet.class);
	
	private static final String PARAM_ACTION = "action";
	
	private static final String ACTION_UPLOAD_FILE = "uploadFile";

	@Value("${data.management.file.location}")
	private String fileLocation;
	
	@RequestMapping
	public String doView() {
		log.debug("Generating data portlet main view for location [{}]", fileLocation);
		
		return "dataManager/main";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_UPLOAD_FILE)
	public String doViewUploadFile() {
		return "dataManager/fileUpload";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_UPLOAD_FILE)
	public void doActionUploadFile(@RequestParam("file") MultipartFile file) {
		if(!file.isEmpty()) {
			File target = new File(fileLocation + "/" + file.getOriginalFilename());
			log.info("Saving file {} in LOBCDER under location {}", file.getOriginalFilename(), target.getAbsolutePath());
			
			try {
				file.transferTo(target);
			} catch (Exception e) {
				log.error("Could not save file {} in LOBCDER", file.getOriginalFilename());
			}
		}
	}
	
	@ResourceMapping("fileList")
	public void fileList(ResourceResponse response) {
		File location = new File(fileLocation);
		StringBuilder builder = new StringBuilder();
		
		if(location.isDirectory()) {
			List<String> files = new ArrayList<String>();
			
			for(File file : location.listFiles()) {
				if(file.isFile()) {
					ResourceURL url = response.createResourceURL();
					url.setResourceID("getFile");
					url.setParameter("fileName", file.getName());
					files.add(file.getName() + "|" + url.toString() + "|" + file.length() / 1024 + " kB");
				}
			}
			
			Collections.sort(files);
			
			for(String file : files) {
				builder.append(file).append(";");
			}
			
			if(builder.length() > 0) {
				builder.deleteCharAt(builder.length() - 1);
			}
		} else {
			log.warn("Location [{}] is not a browsable directory", fileLocation);
		}
		
		try {
			response.getWriter().write(builder.toString());
		} catch (IOException e) {
			log.warn("Could not list [{}] location", fileLocation);
		}
	}
	
	@ResourceMapping("getFile")
	public void getFile(@RequestParam("fileName") String fileName, ResourceResponse response) {
		String path = fileLocation + "/" + fileName;
		log.debug("Serving file [{}]", path);
		File file = new File(path);
		
		if(file.exists() && file.isFile()) {
			response.addProperty("Content-Disposition", "Attachment;Filename=\"" + fileName + "\"");
			response.addProperty("Pragma", "public");
			response.addProperty("Cache-Control", "must-revalidate");
			response.setContentLength((int) file.length());
			
			try {
				FileCopyUtils.copy(new FileInputStream(file), response.getPortletOutputStream());
			} catch (Exception e) {
				log.warn("Could not serve file [{}]", path);
			}
		} else {
			log.warn("File [{}] does not exist", path);
		}
	}
}