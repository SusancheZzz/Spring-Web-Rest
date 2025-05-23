package com.rntgroup.impl.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "department_snapshot", schema = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DepartmentSnapshotEntity {

  @Id
  private Long id;

  private String name;

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DepartmentSnapshotEntity that = (DepartmentSnapshotEntity) o;
    return Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(name);
  }
}