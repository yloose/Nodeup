package de.yloose.nodeup.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import de.yloose.nodeup.models.NodeEntity;
import de.yloose.nodeup.models.WeatherDatapoint;

public interface WeatherdataRepository extends JpaRepository<WeatherDatapoint, UUID> {
	
	List<WeatherDatapoint> findByNode(NodeEntity node);
	
	Optional<WeatherDatapoint> findFirstByNodeOrderByTimestampDesc(NodeEntity node);
	
	List<WeatherDatapoint> findAllByNodeOrderByTimestampDesc(NodeEntity node);
}
