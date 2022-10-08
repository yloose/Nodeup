package de.yloose.nodeup.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import de.yloose.nodeup.backend.models.NodeEntity;
import de.yloose.nodeup.backend.models.WeatherDatapoint;

public interface WeatherdataRepository extends JpaRepository<WeatherDatapoint, UUID> {
	
	List<WeatherDatapoint> findByNode(NodeEntity node);
	
	Optional<WeatherDatapoint> findFirstByNodeOrderByTimestampDesc(NodeEntity node);
	
	List<WeatherDatapoint> findAllByNodeOrderByTimestampDesc(NodeEntity node);
}
