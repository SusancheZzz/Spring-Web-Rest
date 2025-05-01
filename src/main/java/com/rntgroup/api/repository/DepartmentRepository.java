package com.rntgroup.api.repository;

import com.rntgroup.api.entity.DepartmentEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository("departmentRepository")
public interface DepartmentRepository extends JpaRepository<DepartmentEntity, Long> {

  Optional<DepartmentEntity> findByName(String name);

  @Query(
    value = """
      INSERT INTO departments.parent_child_departments (parent_department_id, child_department_id) 
      VALUES (:departmentId, :childId)
    """,
    nativeQuery = true
  )
  @Modifying
  void addChildDepartment(@Param("departmentId") Long departmentId, @Param("childId") Long childId);

  @Query(value = "SELECT d FROM DepartmentEntity d WHERE d.isMain = true")
  DepartmentEntity findMainDepartment();

  @Query(
    value = """
      SELECT d FROM DepartmentEntity d JOIN d.childDepartment cd WHERE cd.id = :childId
      """
  )
  DepartmentEntity findDepartmentWithChildId(@Param("childId") Long childId);
}
