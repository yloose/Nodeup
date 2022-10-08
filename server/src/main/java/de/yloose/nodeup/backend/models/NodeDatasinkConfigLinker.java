package de.yloose.nodeup.backend.models;

import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import de.yloose.nodeup.util.HashMapConverter;

@Entity
@Table(name = "DATASINK_CONFIG")
public class NodeDatasinkConfigLinker {

	@Id
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "datasink_id")
	private DatasinkEntity datasink;

	@Convert(converter = HashMapConverter.class)
	private Map<String, Object> datasinkConfig;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public DatasinkEntity getDatasink() {
		return datasink;
	}

	public void setDatasink(DatasinkEntity datasink) {
		this.datasink = datasink;
	}

	public Map<String, Object> getDatasinkConfig() {
		return datasinkConfig;
	}

	public void setDatasinkConfig(Map<String, Object> datasinkConfig) {
		this.datasinkConfig = datasinkConfig;
	}
}