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
import hu.infokristaly.backupservice.repository.FolderInfoRepository;

@Service
public class FolderInfoService {

	@Autowired
	private FolderInfoRepository folderInfoRepository;

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

	public FolderInfo save(FolderInfo folderInfo) {
		return folderInfoRepository.save(folderInfo);
	}

	public Optional<FolderInfo> findById(Long id) {
		return folderInfoRepository.findById(id);
	}

	public void delete(FolderInfo folderInfo) {
		folderInfoRepository.delete(folderInfo);
	}

	public Iterable<FolderInfo> findAll() {
		return folderInfoRepository.findAll();
	}

	public Iterable<FolderInfo> findByParentFolder(Long id) {
		FolderInfo parentFolder = new FolderInfo(id);
		return folderInfoRepository.findByParentFolder(parentFolder);
	}

	public List<FolderInfo> findByParentId(Long id) {
		Optional<FolderInfo> parentFolder = folderInfoRepository.findById(id);
		return parentFolder.isPresent() ? folderInfoRepository.findByParentFolder(parentFolder.get()) : null;
	}

	public FolderInfo findByParentIdAndName(FolderInfo folderInfo) {
		FolderInfo result = folderInfoRepository.findByParentIdAndName(folderInfo.getParentFolder(),folderInfo.getFolderName());
		return result;
	}

	public void deleteById(Long id) {
		Optional<FolderInfo> folder = findById(id);
		String folderPath = getFolderPath(folder.get());

		List<FolderInfo> folders = findByParentId(id);
		for (FolderInfo f : folders) {
			deleteById(f.getId());
		}
		List<FileInfo> files = fileInfoRepository.findByParentFolderInfoId(id);
		for (FileInfo entry : files) {
			File file = new File(userBackupPath + File.separator + getFileNameWithPath(entry));
			file.delete();
			fileInfoRepository.delete(entry);
		}

		folderPath = userBackupPath + File.separator + folderPath;
		File folderFile = new File(folderPath);
		folderFile.delete();
		folderInfoRepository.delete(folder.get());
	}

}
