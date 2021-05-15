package hu.infokristaly.backupservice.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import hu.infokristaly.backupservice.model.FileInfo;
import hu.infokristaly.backupservice.model.FolderInfo;
import hu.infokristaly.backupservice.service.FileInfoService;
import hu.infokristaly.backupservice.service.FolderInfoService;

@RestController
@RequestMapping("/fileinfo")
public class FileInfoController {

	@Value("${userBackup.path}")
	private String userBackupPath;

	@Autowired
	private FileInfoService fileInfoService;

	@Autowired
	private FolderInfoService folderInfoService;

	@GetMapping("/{id}")
	public FileInfo findById(@PathVariable Long id) {
		Optional<FileInfo> result = fileInfoService.findById(id);
		return result.isPresent() ? result.get() : null;
	}

	@GetMapping("/findAllFile")
	public List<FileInfo> findAllFile() {
		List<FileInfo> result = new LinkedList<FileInfo>();
		fileInfoService.findAll().forEach(item -> result.add(item));
		return result;
	}

	@PostMapping("/save")
	public FileInfo save(@RequestBody FileInfo fileInfo) {
		if (fileInfo.getFileName() != null) {
			fileInfoService.save(fileInfo);
		}
		return fileInfo;
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		fileInfoService.deleteById(id);
	}

	@GetMapping("/getFileById/{id}")
	public ResponseEntity<?> getFileById(@PathVariable Long id) {
		Optional<FileInfo> result = fileInfoService.findById(id);
		if (!result.isPresent()) {
			HashMap<String, String> err = new HashMap<String, String>();
			err.put("error", "FileInfo not found!");
			return ResponseEntity.ok().body(err);
		}
		File file = new File(userBackupPath + File.separator + FileInfoService.getFileNameWithPath(result.get()));
		if (!file.exists()) {
			HashMap<String, String> err = new HashMap<String, String>();
			err.put("error", "file not found!");
			return ResponseEntity.ok().body(err);
		}
		FileSystemResource fileSystemResource = new FileSystemResource(file);

		return ResponseEntity.ok().contentType(MediaType.APPLICATION_OCTET_STREAM).body(fileSystemResource);
	}

	@GetMapping("/findByParentFolderId/{id}")
	public List<FileInfo> findByParentId(@PathVariable Long id) {
		List<FileInfo> result = fileInfoService.findByParentFolderId(id);
		return result;
	}

	@GetMapping("/findFileByName")
	public List<FileInfo> findFileByName(@RequestParam String name) {
		return fileInfoService.findByName(name);
	}

	@PostMapping("/findByParentFolderIdAndName")
	public FileInfo find(@RequestBody FileInfo fileInfo) {
		FileInfo existFileInfo = fileInfoService.findByParentFolderIdAndName(fileInfo.getFolderInfo().getId(), fileInfo.getFileName());
		return existFileInfo;
	}

	@RequestMapping(value = "/fileupload", method = { RequestMethod.POST, RequestMethod.PUT }, consumes = { "multipart/form-data;charset=UTF-8" })
	public ResponseEntity<?> fileUpload(@RequestParam("fileInfo") String fileInfoJson, @RequestParam("file") MultipartFile file) {
		FileInfo fileInfo = null;

		ObjectMapper mapper = new ObjectMapper();
		try {
			fileInfo = mapper.readValue(fileInfoJson, FileInfo.class);

			if (fileInfo.getFolderInfo() != null) {
				Optional<FolderInfo> folder = folderInfoService.findById(fileInfo.getFolderInfo().getId());
				if (!folder.isPresent()) {
					HashMap<String, String> err = new HashMap<String, String>();
					err.put("error", "Parent FolderInfo not found");
					return ResponseEntity.accepted().body(err);
				}
				fileInfo.setFolderInfo(folder.get());
			}
			fileInfoService.save(fileInfo);

			String rootPath = userBackupPath + File.separator + FileInfoService.getFilePath(fileInfo);
			File filePath = new File(rootPath);
			filePath.mkdirs();
			try (InputStream in = file.getInputStream(); OutputStream out = new FileOutputStream(rootPath + File.separator + fileInfo.getFileName())) {
				FileCopyUtils.copy(in, out);
			} catch (IOException ex) {
				HashMap<String, String> err = new HashMap<String, String>();
				err.put("error", ex.getLocalizedMessage());
				return ResponseEntity.accepted().body(err);
			}
		} catch (JsonProcessingException e) {
			HashMap<String, String> err = new HashMap<String, String>();
			err.put("error", e.getLocalizedMessage());
			return ResponseEntity.accepted().body(err);
		}
		return ResponseEntity.ok().body(fileInfo);
	}

}
