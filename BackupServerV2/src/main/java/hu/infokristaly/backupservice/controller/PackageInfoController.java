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
import hu.infokristaly.backupservice.model.PackageInfo;
import hu.infokristaly.backupservice.service.FolderInfoService;
import hu.infokristaly.backupservice.service.PackageInfoService;

@RestController
@RequestMapping("/packageinfo")
public class PackageInfoController {

	@Autowired
	private PackageInfoService packageInfoService;
	
	@PostMapping("/save")
	public PackageInfo save(@RequestBody PackageInfo packageInfo) {
		return packageInfoService.save(packageInfo);
	}
	
	@GetMapping("/{id}")
	public PackageInfo findById(@PathVariable Long id) {
		Optional<PackageInfo> result = packageInfoService.findById(id);
		return result.isPresent() ? result.get() : null;
	}
	
	@GetMapping("/findAllPackage")
	public List<PackageInfo> findAll(){
		List<PackageInfo> result = new LinkedList<PackageInfo>();
		packageInfoService.findAllOrdered().forEach(f -> result.add(f));
		return result;
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		PackageInfo packageInfo = new PackageInfo(id);
		packageInfoService.delete(packageInfo);
	}
}
