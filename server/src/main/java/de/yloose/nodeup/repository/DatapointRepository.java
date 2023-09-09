package de.yloose.nodeup.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import de.yloose.nodeup.models.NodeEntity;
import de.yloose.nodeup.models.DatapointEntity;

public interface DatapointRepository extends JpaRepository<DatapointEntity, UUID> {
		
	List<DatapointEntity> findByNode(NodeEntity node);
	
	Optional<DatapointEntity> findFirstByNodeOrderByTimestampDesc(NodeEntity node);
	
	List<DatapointEntity> findAllByNodeOrderByTimestampDesc(NodeEntity node);
}
