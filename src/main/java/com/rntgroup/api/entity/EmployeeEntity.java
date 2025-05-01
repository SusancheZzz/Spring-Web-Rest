package com.rntgroup.api.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "employee", schema = "employees")
@Getter
@Setter
@ToString(exclude = {"quiteDate"})
@EqualsAndHashCode(exclude = {"id", "quiteDate"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String surname;

  private String name;

  private String fatherName;

  @Enumerated(value = EnumType.STRING)
  private Gender gender;

  private LocalDate birthday;

  private String phoneNumber;

  private Long departmentId;

  private LocalDate employmentDate;

  @Builder.Default
  private LocalDate quiteDate = null;

  private String position;

  private Integer payment;

  private Boolean isLeader;

  public EmployeeEntity(String surname, String name,
    String fatherName, Gender gender,
    LocalDate birthday, String phoneNumber,
    LocalDate employmentDate, Integer payment,
    String position, Boolean isLeader) {
    this.surname = surname;
    this.name = name;
    this.fatherName = fatherName;
    this.gender = gender;
    this.birthday = birthday;
    this.phoneNumber = phoneNumber;
    this.employmentDate = employmentDate;
    this.position = position;
    this.isLeader = isLeader;
  }
}
