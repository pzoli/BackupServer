package hu.infokristaly.backupservice.service;

import java.io.File;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import hu.infokristaly.backupservice.model.FileInfo;
import hu.infokristaly.backupservice.model.FolderInfo;
import hu.infokristaly.backupservice.repository.FileInfoRepository;

@Service
public class FileInfoService {

	@Autowired
	private FileInfoRepository fileInfoRepository;
	
	@Value("${userBackup.path}")
	private String userBackupPath;

	public static FolderInfo getRootFolderInfo(FolderInfo f) {
		FolderInfo result = f;
		while (result.getParentFolder() != null) {
			result = result.getParentFolder();
		}
		return result;
	}

	public static String getFilePath(FileInfo fileInfo) {
		StringBuilder sb = new StringBuilder();
		FolderInfo folderInfo = fileInfo.getFolderInfo();
		do {
			sb.insert(0, File.separator + folderInfo.getFolderName());
			folderInfo = folderInfo.getParentFolder();
		} while (folderInfo != null);
		return sb.toString();
	}

	public static String getFolderPath(FolderInfo folderInfo) {
		StringBuilder sb = new StringBuilder();
		do {
			sb.insert(0, File.separator + folderInfo.getFolderName());
			folderInfo = folderInfo.getParentFolder();
		} while (folderInfo != null);
		return sb.toString();
	}

	public static String getFileNameWithPath(FileInfo fileInfo) {
		return getFilePath(fileInfo) + File.separator + fileInfo.getFileName();
	}
	public Optional<FileInfo> findById(Long id) {
		return fileInfoRepository.findById(id);
	}
	
	public List<FileInfo> findByName(String name) {
		return fileInfoRepository.findByName(name);
	}

	public FileInfo save(FileInfo fileInfo) {
		fileInfoRepository.save(fileInfo);
		return fileInfo;
	}
	
	public void delete(FileInfo fileInfo) {
		fileInfoRepository.delete(fileInfo);
	}

	public boolean deleteById(Long id) {
		Optional<FileInfo> fileInfo = findById(id);
		
		String filePath = FolderInfoService.getFileNameWithPath(fileInfo.get());
		File f = new File(userBackupPath + File.separator + filePath);
		boolean result = f.exists();
		if (result) {
			result = f.delete();
			if (result) {
				fileInfoRepository.deleteById(id);
			}
		}
		return result;
	}

	public Iterable<FileInfo> findAll() {
		return fileInfoRepository.findAll();
	}

	public List<FileInfo> findByParentFolderId(Long id) {
		return fileInfoRepository.findByParentFolderInfoId(id);
	}
	
	public FileInfo findByParentFolderIdAndName(Long id, String fileName) {
		FileInfo result = fileInfoRepository.findByParentFolderIdAndName(id,fileName);
		return result;
	}

}
