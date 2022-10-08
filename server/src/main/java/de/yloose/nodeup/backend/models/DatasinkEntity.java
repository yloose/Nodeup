package de.yloose.nodeup.backend.models;

import java.util.Objects;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "datasinks")
public class DatasinkEntity {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;
	
	@Column(unique = true)
	private UUID sinkId;
	
	private String displayName;
	
	public DatasinkEntity() {
		super();
	}
	
	public DatasinkEntity(UUID sinkId, String displayName) {
		super();
		this.sinkId = sinkId;
		this.displayName = displayName;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getSinkId() {
		return sinkId;
	}

	public void setSinkId(UUID sinkId) {
		this.sinkId = sinkId;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(sinkId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DatasinkEntity other = (DatasinkEntity) obj;
		return Objects.equals(sinkId, other.sinkId);
	}
}
