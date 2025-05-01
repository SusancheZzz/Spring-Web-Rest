package com.rntgroup.api.repository;

import com.rntgroup.api.entity.EmployeeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

  @Query(
    nativeQuery = true,
    value = """
      SELECT e.* FROM employees.employee AS e
      WHERE e.department_id = :departmentId AND e.is_leader = true
    """
  )
  Optional<EmployeeEntity> findLeaderInDepartment(@Param("departmentId") Long departmentId);

  @Query(value = "SELECT e FROM EmployeeEntity e WHERE e.id = :id AND e.quiteDate IS NULL")
  Optional<EmployeeEntity> findById(@Param("id") Long id);

  @Query("""
    SELECT e FROM EmployeeEntity e WHERE e.quiteDate IS NULL AND e.departmentId = :departmentId
  """)
  List<EmployeeEntity> findByDepartmentId(@Param("departmentId") Long departmentId);

  Optional<EmployeeEntity> findByPhoneNumber(String phoneNumber);

  @Query(
    value = "SELECT sum(e.payment) FROM employees.employee e WHERE e.department_id = :departmentId",
    nativeQuery = true
  )
  Integer commonPaymentForDepartment(@Param("departmentId") Long departmentId);
}
