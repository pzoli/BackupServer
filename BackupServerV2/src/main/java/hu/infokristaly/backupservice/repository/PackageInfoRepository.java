package hu.infokristaly.backupservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import hu.infokristaly.backupservice.model.PackageInfo;

public interface PackageInfoRepository extends CrudRepository<PackageInfo, Long> {

	@Query("select p from PackageInfo p order by p.folderName")
	Iterable<PackageInfo> findAllOrdered();

}
