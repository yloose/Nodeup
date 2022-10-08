package de.yloose.nodeup.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import de.yloose.nodeup.backend.models.DatasinkEntity;

public interface DatasinkRepository extends JpaRepository<DatasinkEntity, UUID> {
	
}
