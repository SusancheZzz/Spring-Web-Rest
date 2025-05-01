package com.rntgroup.api.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NamedEntityGraph(
  name = "department.childDepartment",
  attributeNodes = @NamedAttributeNode("childDepartment")
)
@Entity
@Table(name = "department", schema = "departments")
@Getter
@Setter
@EqualsAndHashCode(exclude = {"id"})
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private LocalDate createdAt;

  private Boolean isMain = false;

  @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REFRESH})
  @JoinTable(
    schema = "departments",
    name = "parent_child_departments",
    joinColumns = @JoinColumn(name = "parent_department_id")
  )
  private List<DepartmentEntity> childDepartment = new ArrayList<>();

  public DepartmentEntity(String name, LocalDate createdAt) {
    this.name = name;
    this.createdAt = createdAt;
  }

  public void addChildDepartment(DepartmentEntity departmentEntity) {
    childDepartment.add(departmentEntity);
  }

  public void addAllChildDepartments(List<DepartmentEntity> departmentEntities) {
    childDepartment.addAll(departmentEntities);
  }

  public DepartmentEntity removeChildDepartment(int index) {
    var removingDepartment = childDepartment.get(index);
    childDepartment.remove(index);

    return removingDepartment;
  }

  public DepartmentEntity removeChildDepartment(DepartmentEntity departmentEntity) {
    childDepartment.remove(departmentEntity);
    return departmentEntity;
  }
}
