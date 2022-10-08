package de.yloose.nodeup.backend.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import de.yloose.nodeup.backend.models.NodeEntity;

public interface NodeRepository extends JpaRepository<NodeEntity, UUID> {
	Optional<NodeEntity> findByMac(String mac);
}
