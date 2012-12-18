package pl.cyfronet.coin.portlet.datamanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.portlet.PortletURL;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import pl.cyfronet.coin.portlet.lobcder.LobcderClient;
import pl.cyfronet.coin.portlet.lobcder.LobcderEntry;
import pl.cyfronet.coin.portlet.lobcder.LobcderException;

@Controller
@RequestMapping("VIEW")
public class DataManagerPortlet {
	private final Logger log = LoggerFactory.getLogger(DataManagerPortlet.class);
	
	private static final String MODEL_BEAN_LOBCDER_PATH = "path";
	
	private static final String PARAM_ACTION = "action";
	
	private static final String ACTION_UPLOAD_FILE = "uploadFile";
	
	@Autowired private LobcderClient lobcderClient;
	
	@RequestMapping
	public String doView(@RequestParam(value = MODEL_BEAN_LOBCDER_PATH, defaultValue = "/", required = false) String path, Model model) {
		log.debug("Generating data portlet main view for location [{}]", path);
		model.addAttribute(MODEL_BEAN_LOBCDER_PATH, path);
		
		return "dataManager/main";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_UPLOAD_FILE)
	public String doViewUploadFile(@RequestParam("path") String path, Model model) {
		model.addAttribute(MODEL_BEAN_LOBCDER_PATH, path);
		
		return "dataManager/fileUpload";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_UPLOAD_FILE)
	public void doActionUploadFile(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) throws LobcderException, IOException {
		log.info("Uploading file [{}] to path [{}]", file.getName(), path);
		
		if(!file.isEmpty()) {
			lobcderClient.put(path, file.getOriginalFilename(), file.getInputStream());
		}
	}
	
	@ResourceMapping("fileList")
	public void fileList(@RequestParam(MODEL_BEAN_LOBCDER_PATH) String path, ResourceResponse response) throws LobcderException {
		//somehow the path parameter is filled with multiple values
		if(path.contains(",")) {
			log.warn("Path contains multiple values: " + path);
			path = path.split(",")[0];
		}
		
		StringBuilder builder = new StringBuilder();
		List<String> files = new ArrayList<String>();
		List<LobcderEntry> entries = lobcderClient.list(path);
		sortLobcderEntries(entries);
		
		//if we are looking at a subdirectory let's generate a back item
		if(!path.equals("/")) {
			int lastButOneSlashIndex = path.substring(0, path.length() - 1).lastIndexOf("/");
			String backValue = null;
			
			if(lastButOneSlashIndex == -1) {
				backValue = "/";
			} else {
				backValue = path.substring(0, lastButOneSlashIndex);
			}

			PortletURL url = response.createRenderURL();
			url.setParameter("path", backValue);
			files.add(path + "..|" + url.toString() + "|");
		}
		
		for(LobcderEntry entry : entries) {
			if(!entry.isDirectory()) {
				ResourceURL url = response.createResourceURL();
				url.setResourceID("getFile");
				url.setParameter("fileName", entry.getName());
				files.add(entry.getName() + "|" + url.toString() + "|" + entry.getBytes() / 1024 + " kB");
			} else {
				PortletURL url = response.createRenderURL();
				url.setParameter("path", entry.getName());
				files.add(entry.getName() + "|" + url.toString() + "|");
			}
		}
		
		for(String file : files) {
			builder.append(file).append(";");
		}
		
		if(builder.length() > 0) {
			builder.deleteCharAt(builder.length() - 1);
		}
		
		try {
			response.getWriter().write(builder.toString());
		} catch (IOException e) {
			log.warn("Could not list [{}] location", path);
		}
	}

	@ResourceMapping("getFile")
	public void getFile(@RequestParam("fileName") String fileName, ResourceResponse response) {
//		String path = fileLocation + "/" + fileName;
//		String contentType = new MimetypesFileTypeMap().getContentType(fileName);
//		log.debug("Serving file [{}] with content type [{}]", path, contentType);
//		File file = new File(path);
//		
//		if(file.exists() && file.isFile()) {
//			response.addProperty("Content-Disposition", "Attachment;Filename=\"" + fileName + "\"");
//			response.addProperty("Pragma", "public");
//			response.addProperty("Cache-Control", "must-revalidate");
//			response.setContentLength((int) file.length());
//			response.setContentType(contentType);
//			
//			try {
//				FileCopyUtils.copy(new FileInputStream(file), response.getPortletOutputStream());
//			} catch (Exception e) {
//				log.warn("Could not serve file [{}]", path);
//			}
//		} else {
//			log.warn("File [{}] does not exist", path);
//		}
	}
	
	private void sortLobcderEntries(List<LobcderEntry> entries) {
		Collections.sort(entries, new Comparator<LobcderEntry>() {
			@Override
			public int compare(LobcderEntry le1, LobcderEntry le2) {
				if(le1.isDirectory() == le2.isDirectory()) {
					return le1.getName().compareTo(le2.getName());
				} else {
					if(le1.isDirectory()) {
						return -1;
					} else {
						return 1;
					}
				}
			}
		});
	}
}