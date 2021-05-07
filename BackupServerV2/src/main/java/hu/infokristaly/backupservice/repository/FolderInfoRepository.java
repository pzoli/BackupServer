package hu.infokristaly.backupservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import hu.infokristaly.backupservice.model.FolderInfo;

@Repository
public interface FolderInfoRepository extends CrudRepository<FolderInfo, Long> {

	@Query("select f from FolderInfo f where f.parentFolder = :parentFolder order by f.folderName")
	List<FolderInfo> findByParentFolder(FolderInfo parentFolder);

	@Query("select f from FolderInfo f where f.parentFolder = :parentFolder and f.folderName = :folderName")
	FolderInfo findByParentIdAndName(FolderInfo parentFolder, String folderName); 

}
