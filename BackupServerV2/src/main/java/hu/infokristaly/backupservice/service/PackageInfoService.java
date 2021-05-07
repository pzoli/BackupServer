package hu.infokristaly.backupservice.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.infokristaly.backupservice.model.PackageInfo;
import hu.infokristaly.backupservice.repository.PackageInfoRepository;

@Service
public class PackageInfoService {

	@Autowired
	private PackageInfoRepository packageInfoRepository;
	
	public PackageInfo save(PackageInfo packageInfo) {
		return packageInfoRepository.save(packageInfo);
	}

	public void delete(PackageInfo packageInfo) {
		packageInfoRepository.delete(packageInfo);
	}

	public Iterable<PackageInfo> findAll() {
		return packageInfoRepository.findAll();
	}

	public Iterable<PackageInfo> findAllOrdered() {
		return packageInfoRepository.findAllOrdered();
	}

	public Optional<PackageInfo> findById(Long id) {
		return packageInfoRepository.findById(id);
	}
}
