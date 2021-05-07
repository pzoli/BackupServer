package hu.infokristaly.backupservice.model;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@Entity
@JsonTypeInfo(include=JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class PackageInfo extends FolderInfo {
	
	private String comment;

	public PackageInfo() {
		super();
	}
	
	public PackageInfo(Long id) {
		this.setId(id);
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
	
}
