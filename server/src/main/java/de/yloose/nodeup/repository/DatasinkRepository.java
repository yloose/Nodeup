package de.yloose.nodeup.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import de.yloose.nodeup.models.DatasinkEntity;

public interface DatasinkRepository extends JpaRepository<DatasinkEntity, UUID> {
	
}
