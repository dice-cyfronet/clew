package pl.cyfronet.coin.portlet.datamanager;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletURL;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import pl.cyfronet.coin.portlet.lobcder.LobcderClient;
import pl.cyfronet.coin.portlet.lobcder.LobcderEntry;
import pl.cyfronet.coin.portlet.lobcder.LobcderException;
import pl.cyfronet.coin.portlet.lobcder.LobcderInputStream;
import pl.cyfronet.coin.portlet.lobcder.LobcderRestClient;
import pl.cyfronet.coin.portlet.lobcder.LobcderRestMetadata;
import pl.cyfronet.coin.portlet.lobcder.LobcderRestMetadataPermissions;
import pl.cyfronet.coin.portlet.lobcder.LobcderSearchCriteria;
import pl.cyfronet.coin.portlet.lobcder.LobcderWebDavMetadata;
import pl.cyfronet.coin.portlet.metadata.Metadata;
import pl.cyfronet.coin.portlet.portal.Portal;

@Controller
@RequestMapping("VIEW")
@SessionAttributes({DataManagerPortlet.MODEL_BEAN_SEARCH_REQUEST, DataManagerPortlet.MODEL_BEAN_MODE,
	DataManagerPortlet.MODEL_BEAN_FOUND_ENTRIES, DataManagerPortlet.MODEL_BEAN_PARENT_PATHS})
public class DataManagerPortlet {
	private final Logger log = LoggerFactory.getLogger(DataManagerPortlet.class);
	
	static final String MODEL_BEAN_LOBCDER_PATH = "path";
	static final String MODEL_BEAN_CREATE_DIRECTORY_REQUEST = "createDirectoryRequest";
	static final String MODEL_BEAN_LOBCDER_PARENT_PATH = "parentPath";
	static final String MODEL_BEAN_METADATA = "metadata";
	static final String MODEL_BEAN_SEARCH_REQUEST = "searchRequest";
	static final String MODEL_BEAN_FOUND_ENTRIES = "foundEntries";
	static final String MODEL_BEAN_PARENT_PATHS = "parentPaths";
	static final String MODEL_BEAN_MODE = "mode";
	
	private static final String PARAM_ACTION = "action";
	private static final String PARAM_MODE_VIEW = "view";
	private static final String PARAM_MODE_SEARCH = "search";
	
	private static final String ACTION_UPLOAD_FILE = "uploadFile";
	private static final String ACTION_CREATE_DIRECTORY = "createDirectory";
	private static final String ACTION_DELETE_RESOURCE = "deleteResource";
	private static final String ACTION_METADATA = "metadata";
	private static final String ACTION_UPDATE_METADATA = "updateMetadata";
	private static final String ACTION_SEARCH = "search";
	
	@Autowired private LobcderClient lobcderClient;
	@Autowired private Validator validator;
	@Autowired private Portal portal;
	@Autowired private LobcderRestClient lobcderRestClient;
	
	@RequestMapping
	public String doView(@RequestParam(value = MODEL_BEAN_LOBCDER_PATH, defaultValue = "/", required = false) String path, Model model) {
		log.debug("Generating data portlet main view for location [{}]", path);
		model.addAttribute(MODEL_BEAN_MODE, PARAM_MODE_VIEW);
		model.addAttribute(MODEL_BEAN_LOBCDER_PATH, path);
		
		if(!model.containsAttribute(MODEL_BEAN_CREATE_DIRECTORY_REQUEST)) {
			model.addAttribute(MODEL_BEAN_CREATE_DIRECTORY_REQUEST, new CreateDirectoryRequest());
		}
		
		return "dataManager/main";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_UPLOAD_FILE)
	public String doViewUploadFile(@RequestParam("path") String path, Model model) {
		model.addAttribute(MODEL_BEAN_LOBCDER_PATH, path);
		
		return "dataManager/fileUpload";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_UPLOAD_FILE)
	public void doActionUploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam("path") String path, PortletRequest request, ActionResponse response) throws LobcderException, IOException {
		log.info("Uploading file [{}] to path [{}]", file.getName(), path);
		
		if(!file.isEmpty()) {
			lobcderClient.put(path, file.getOriginalFilename(), file.getInputStream(), portal.getUserToken(request));
		}
		
		response.setRenderParameter(MODEL_BEAN_LOBCDER_PATH, path);
	}
	
	@ResourceMapping("fileList")
	public void fileList(@RequestParam(MODEL_BEAN_LOBCDER_PATH) String path, ResourceRequest request, ResourceResponse response) throws LobcderException {
		//somehow the path parameter is filled with multiple values
		if(path.contains(",")) {
			log.warn("Path contains multiple values: " + path);
			path = path.split(",")[0];
		}
		
		StringBuilder builder = new StringBuilder();
		List<String> files = new ArrayList<String>();
		
		String token = portal.getUserToken(request);
		log.trace("Listing LOBCDER for path {} and token {}", path, token);
		
		List<LobcderEntry> entries = lobcderClient.list(path, token);
		sortLobcderEntries(entries);
		
		//if we are looking at a subdirectory let's generate a back item
		if(!path.equals("/")) {
			String backValue = getParentDirectory(path);

			PortletURL url = response.createRenderURL();
			url.setParameter(MODEL_BEAN_LOBCDER_PATH, backValue);
			files.add(path + "..|" + url.toString() + "|||");
		}
		
		for(LobcderEntry entry : entries) {
			PortletURL deleteUrl = response.createActionURL();
			deleteUrl.setParameter(PARAM_ACTION, ACTION_DELETE_RESOURCE);
			deleteUrl.setParameter(MODEL_BEAN_LOBCDER_PATH, entry.getName());
			
			PortletURL metadataUrl = response.createRenderURL();
			metadataUrl.setParameter(PARAM_ACTION, ACTION_METADATA);
			metadataUrl.setParameter(MODEL_BEAN_LOBCDER_PATH, entry.getName());
			metadataUrl.setParameter(MODEL_BEAN_LOBCDER_PARENT_PATH, path);
			
			if(!entry.isDirectory()) {
				ResourceURL url = response.createResourceURL();
				url.setResourceID("getFile");
				url.setParameter("filePath", entry.getName());
				url.setParameter("size", "" + entry.getBytes());
				files.add(entry.getName() + "|" + url.toString() + "|" + entry.getBytes() / 1024 + " kB|" +
						deleteUrl.toString() + "|" + metadataUrl.toString());
			} else {
				PortletURL url = response.createRenderURL();
				url.setParameter("path", entry.getName());
				files.add(entry.getName() + "|" + url.toString() + "||" +
						deleteUrl.toString() + "|" + metadataUrl.toString());
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
	public void getFile(@RequestParam("filePath") String filePath,
			@RequestParam("size") int size, ResourceRequest request, ResourceResponse response) throws IOException, LobcderException {
		String contentType = new MimetypesFileTypeMap().getContentType(filePath);
		response.addProperty("Content-Disposition", "Attachment;Filename=\"" + filePath.substring(filePath.lastIndexOf("/") + 1) + "\"");
		response.addProperty("Pragma", "public");
		response.addProperty("Cache-Control", "must-revalidate");
		response.setContentLength(size);
		response.setContentType(contentType);
		
		LobcderInputStream lobcderInputStream = lobcderClient.get(filePath, portal.getUserToken(request));
		FileCopyUtils.copy(lobcderInputStream.getInputStream(), response.getPortletOutputStream());
		lobcderInputStream.close();
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_CREATE_DIRECTORY)
	public void doActionCreateDirectory(@RequestParam(MODEL_BEAN_LOBCDER_PATH) String parentDirectory,
			@ModelAttribute(MODEL_BEAN_CREATE_DIRECTORY_REQUEST) CreateDirectoryRequest createDirectoryRequest,
			BindingResult errors, Model model, PortletRequest request, ActionResponse response) throws LobcderException {
		log.debug("Directory creation request submitted for path [{}] and bean [{}]", parentDirectory,  createDirectoryRequest.toString());
		validator.validate(createDirectoryRequest, errors);
		
		if(!errors.hasErrors()) {
			lobcderClient.createDirectory(parentDirectory, createDirectoryRequest.getDirectoryName(), portal.getUserToken(request));
			model.addAttribute(MODEL_BEAN_CREATE_DIRECTORY_REQUEST, new CreateDirectoryRequest());
			log.info("Directory [{}] in [{}] successfully created", createDirectoryRequest.getDirectoryName(), parentDirectory);
			response.setRenderParameter(MODEL_BEAN_LOBCDER_PATH, parentDirectory);
		}
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_DELETE_RESOURCE)
	public void doActionDeleteLobcderEntry(@RequestParam(MODEL_BEAN_LOBCDER_PATH) String path,
			PortletRequest request, ActionResponse response, Model model) throws LobcderException {
		log.debug("LOBCDER delete request processing for path [{}]", path);
		lobcderClient.delete(path, portal.getUserToken(request));
		
		if(model.asMap().get(MODEL_BEAN_MODE).equals(PARAM_MODE_VIEW)) {
			response.setRenderParameter(MODEL_BEAN_LOBCDER_PATH, getParentDirectory(path));
		} else {
			response.setRenderParameter(PARAM_ACTION, ACTION_SEARCH);
			
			//removing the deleted entry from found items
			List<LobcderEntry> entries = (List<LobcderEntry>) model.asMap().get(MODEL_BEAN_FOUND_ENTRIES);
			
			for(Iterator<LobcderEntry> i = entries.iterator(); i.hasNext(); ) {
				if(i.next().getName().equals(path)) {
					i.remove();
					break;
				}
			}
		}
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_METADATA)
	public String doViewMetadata(@RequestParam(MODEL_BEAN_LOBCDER_PATH) String path,
			@RequestParam(MODEL_BEAN_LOBCDER_PARENT_PATH) String parentPath, Model model,
			PortletRequest request) throws LobcderException {
		model.addAttribute(MODEL_BEAN_LOBCDER_PATH, path);
		model.addAttribute(MODEL_BEAN_LOBCDER_PARENT_PATH, parentPath);
		
		if(!model.containsAttribute(MODEL_BEAN_METADATA)) {
			model.addAttribute(MODEL_BEAN_METADATA, getMetadata(path, portal.getUserToken(request)));
		}
		
		return "dataManager/metadata";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_UPDATE_METADATA)
	public void doActionUpdateMetadata(@RequestParam(MODEL_BEAN_LOBCDER_PATH) String path,
			@RequestParam(MODEL_BEAN_LOBCDER_PARENT_PATH) String parentPath,
			@ModelAttribute(MODEL_BEAN_METADATA) Metadata metadata, BindingResult errors,
			PortletRequest request, ActionResponse response) throws LobcderException {
		log.debug("Updating metadata with the following bean: {}", metadata);
		
		String token = portal.getUserToken(request);
		lobcderClient.updateMetadata(path, metadata.getLobcderWebDavMetadata(), token);
		
		LobcderRestMetadata lobcderRestMetadata = new LobcderRestMetadata();
		lobcderRestMetadata.setUid(metadata.getUid());
		lobcderRestMetadata.setPermissions(new LobcderRestMetadataPermissions());
		lobcderRestMetadata.getPermissions().setOwner(metadata.getOwner());
		lobcderRestMetadata.getPermissions().setReadGroups(
				Arrays.asList(metadata.getReadPermissions().split(",")));
		lobcderRestMetadata.getPermissions().setWriteGroups(
				Arrays.asList(metadata.getWritePermissions().split(",")));
		lobcderRestClient.updateMetadata(lobcderRestMetadata, token);
		response.setRenderParameter(PARAM_ACTION, ACTION_METADATA);
		response.setRenderParameter(MODEL_BEAN_LOBCDER_PATH, path);
		response.setRenderParameter(MODEL_BEAN_LOBCDER_PARENT_PATH, parentPath);
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_SEARCH)
	public String doViewSearch(Model model) {
		if(!model.containsAttribute(MODEL_BEAN_SEARCH_REQUEST) ||
				model.asMap().get(MODEL_BEAN_MODE).equals(PARAM_MODE_VIEW)) {
			model.addAttribute(MODEL_BEAN_SEARCH_REQUEST, new SearchRequest());
			model.asMap().remove(MODEL_BEAN_FOUND_ENTRIES);
		}
		
		model.addAttribute(MODEL_BEAN_MODE, PARAM_MODE_SEARCH);
		
		return "dataManager/search";
	}
	
	@RequestMapping(params = PARAM_ACTION + "=" + ACTION_SEARCH)
	public void doActionSearch(@ModelAttribute(MODEL_BEAN_SEARCH_REQUEST) SearchRequest searchRequest,
			BindingResult errors, PortletRequest request, ActionResponse response, Model model) {
		log.debug("Processing LOBCDER search request for {}", searchRequest);
		LobcderSearchCriteria lsc = new LobcderSearchCriteria();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
		
		if(!searchRequest.getStartModificationDate().isEmpty()) {
			try {
				lsc.setModificationStartMillis(sdf.parse(searchRequest.getStartModificationDate()).getTime() / 1000);
			} catch (ParseException e) {
				//ignoring
			}
		}
		
		if(!searchRequest.getStopModificationDate().isEmpty()) {
			try {
				lsc.setModificationStopMillis(sdf.parse(searchRequest.getStopModificationDate()).getTime() / 1000);
			} catch (ParseException e) {
				//ignoring
			}
		}
		
		if(!searchRequest.getStartCreationDate().isEmpty()) {
			try {
				lsc.setCreationStartMillis(sdf.parse(searchRequest.getStartCreationDate()).getTime() / 1000);
			} catch (ParseException e) {
				//ignoring
			}
		}
		
		if(!searchRequest.getStopCreationDate().isEmpty()) {
			try {
				lsc.setCreationStopMillis(sdf.parse(searchRequest.getStopCreationDate()).getTime() / 1000);
			} catch (ParseException e) {
				//ignoring
			}
		}
			
		lsc.setName(searchRequest.getName());
		
		List<LobcderEntry> foundItems = lobcderRestClient.search(lsc, portal.getUserToken(request));
		Collections.sort(foundItems, new Comparator<LobcderEntry>() {
			@Override
			public int compare(LobcderEntry e1, LobcderEntry e2) {
				return e1.getName().compareTo(e2.getName());
			}
		});
		model.addAttribute(MODEL_BEAN_FOUND_ENTRIES, foundItems);
		model.addAttribute(MODEL_BEAN_PARENT_PATHS, createParentPaths(foundItems));
		response.setRenderParameter(PARAM_ACTION, ACTION_SEARCH);
	}

	@ExceptionHandler(Exception.class)
	public String handleExceptions(Exception e) {
		log.error("Unexpected exception occurred", e);
		
		return "fatal/error";
	}
	
	private Map<String, String> createParentPaths(List<LobcderEntry> entries) {
		Map<String, String> result = new HashMap<>();
		
		for(LobcderEntry entry : entries) {
			if(entry.getName().contains("/")) {
				result.put(entry.getName(), "/" + entry.getName().substring(
						entry.getName().lastIndexOf("/") + 1));
			} else {
				result.put(entry.getName(), "/");
			}
		}
		
		return result;
	}
	
	private Object getMetadata(String path, String securityToken) throws LobcderException {
		log.debug("Fetching LOBCDER metadata for path {}", path);
		
		LobcderWebDavMetadata lobcderWebDavMetadata = lobcderClient.getMetadata(path, securityToken);
		LobcderRestMetadata lobcderRestMetadata = lobcderRestClient.getMetadata(path, securityToken);
		
		Metadata metadata = new Metadata();
		metadata.setLobcderWebDavMetadata(lobcderWebDavMetadata);
		metadata.setOwner(lobcderRestMetadata.getPermissions().getOwner());
		
		if(lobcderRestMetadata.getPermissions().getReadGroups() != null) {
			metadata.setReadPermissions(StringUtils.arrayToDelimitedString(lobcderRestMetadata.getPermissions().getReadGroups().toArray(), ","));
		}
		
		if(lobcderRestMetadata.getPermissions().getWriteGroups() != null) {
			metadata.setWritePermissions(StringUtils.arrayToDelimitedString(lobcderRestMetadata.getPermissions().getWriteGroups().toArray(), ","));
		}
		
		metadata.setUid(lobcderRestMetadata.getUid());
		
		return metadata;
	}

	private String getParentDirectory(String path) {
		int lastButOneSlashIndex = path.substring(0, path.length() - 1).lastIndexOf("/");
		String backValue = null;
		
		if(lastButOneSlashIndex == -1) {
			backValue = "/";
		} else {
			backValue = path.substring(0, lastButOneSlashIndex + 1);
		}
		
		return backValue;
	}
	
	private void sortLobcderEntries(List<LobcderEntry> entries) {
		Collections.sort(entries, new Comparator<LobcderEntry>() {
			@Override
			public int compare(LobcderEntry le1, LobcderEntry le2) {
				if(le1.isDirectory() == le2.isDirectory()) {
					return le1.getName().compareToIgnoreCase(le2.getName());
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