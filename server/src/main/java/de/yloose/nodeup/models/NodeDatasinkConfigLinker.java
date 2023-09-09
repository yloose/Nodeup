package de.yloose.nodeup.models;

import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import de.yloose.nodeup.util.HashMapConverter;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

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