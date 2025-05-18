package com.rntgroup.impl.repository;

import com.rntgroup.impl.entity.EmployeeEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

  @Query(
    nativeQuery = true,
    value = """
        SELECT e.*
        FROM employees.employee e JOIN departments.department_leaders dl
        ON e.id = dl.employee_id
        WHERE dl.department_id = :departmentId
      """
  )
  Optional<EmployeeEntity> findLeaderInDepartment(@Param("departmentId") Long departmentId);

  @Query(
    nativeQuery = true,
    value = """
        SELECT count(dl.*)
        FROM departments.department_leaders dl
        WHERE dl.employee_id = :leaderId
      """
  )
  long getCountDepartmentsOfLeaderById(@Param("leaderId") Long leaderId);


  @Query(
    value = "SELECT e FROM EmployeeEntity e WHERE e.id = :id AND e.quiteDate IS NULL"
  )
  Optional<EmployeeEntity> findEmployeeById(@Param("id") Long id);

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

  @Modifying
  @Query("""
      UPDATE EmployeeEntity e SET e.isLeader = :isLeader WHERE e.id = :id
    """)
  void updateIsLeaderById(@Param("isLeader") Boolean isLeader, @Param("id") Long id);
}
