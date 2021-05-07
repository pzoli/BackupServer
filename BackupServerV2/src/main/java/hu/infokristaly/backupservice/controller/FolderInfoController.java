package hu.infokristaly.backupservice.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.infokristaly.backupservice.model.FolderInfo;
import hu.infokristaly.backupservice.service.FolderInfoService;

@RestController
@RequestMapping("/folderinfo")
public class FolderInfoController {

	@Autowired
	private FolderInfoService folderInfoService;
	
	@PostMapping("/save")
	public FolderInfo save(@RequestBody FolderInfo folderInfo) {
		FolderInfo existingFolderInfo = folderInfoService.findByParentIdAndName(folderInfo);
		if (existingFolderInfo != null) {
			return existingFolderInfo;
		}
		return folderInfoService.save(folderInfo);
	}
	
	@GetMapping("/{id}")
	public FolderInfo findById(@PathVariable Long id) {
		Optional<FolderInfo> result = folderInfoService.findById(id);
		return result.isPresent() ? result.get() : null;
	}
	
	@GetMapping("/findAllFolder")
	public List<FolderInfo> findAll(){
		List<FolderInfo> result = new LinkedList<FolderInfo>();
		folderInfoService.findAll().forEach(f -> result.add(f));
		return result;
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		folderInfoService.deleteById(id);
	}
	
	@GetMapping("/findByParentFolderId/{id}")
	public List<FolderInfo> findByParentFolder(@PathVariable Long id) {
		List<FolderInfo> result = new LinkedList<FolderInfo>();
		folderInfoService.findByParentFolder(id).forEach(f -> result.add(f));
		return result;
	}
	
	@PostMapping("/findByParentIdAndName")
	public FolderInfo findByParentIdAndName(@RequestBody FolderInfo folderInfo) {
		FolderInfo result = folderInfoService.findByParentIdAndName(folderInfo);
		return result;
	}
}
