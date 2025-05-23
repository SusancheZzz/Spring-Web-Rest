package com.rntgroup.impl.repository;

import com.rntgroup.impl.entity.DepartmentSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentSnapshotRepository extends
  JpaRepository<DepartmentSnapshotEntity, Long> {

}