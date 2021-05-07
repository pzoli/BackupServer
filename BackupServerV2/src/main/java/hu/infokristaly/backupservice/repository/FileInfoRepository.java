package hu.infokristaly.backupservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import hu.infokristaly.backupservice.model.FileInfo;

@Repository
public interface FileInfoRepository extends CrudRepository<FileInfo, Long> {

	@Query("Select f from FileInfo f where fileName like :fileName")
	public List<FileInfo> findByName(@Param("fileName") String name);

	@Query("Select f from FileInfo f where folderInfo.id = :id order by f.fileName")
	List<FileInfo> findByParentFolderInfoId(Long id);

	@Query("Select f from FileInfo f where f.folderInfo.id = :id and f.fileName = :fileName")
	public FileInfo findByParentFolderIdAndName(Long id, String fileName); 

}
