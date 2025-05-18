package com.rntgroup.impl.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "employee", schema = "employees")
@Getter
@Setter
@ToString(exclude = {"quiteDate"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String surname;

  private String name;

  private String patronymic;

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
    String patronymic, Gender gender,
    LocalDate birthday, String phoneNumber,
    LocalDate employmentDate, Integer payment,
    String position, Boolean isLeader) {
    this.surname = surname;
    this.name = name;
    this.patronymic = patronymic;
    this.gender = gender;
    this.birthday = birthday;
    this.phoneNumber = phoneNumber;
    this.employmentDate = employmentDate;
    this.position = position;
    this.isLeader = isLeader;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EmployeeEntity entity = (EmployeeEntity) o;
    return Objects.equals(surname, entity.surname) && Objects.equals(name,
      entity.name) && Objects.equals(patronymic, entity.patronymic)
      && gender == entity.gender && Objects.equals(birthday, entity.birthday)
      && Objects.equals(phoneNumber, entity.phoneNumber) && Objects.equals(
      departmentId, entity.departmentId) && Objects.equals(employmentDate,
      entity.employmentDate) && Objects.equals(quiteDate, entity.quiteDate)
      && Objects.equals(position, entity.position) && Objects.equals(payment,
      entity.payment) && Objects.equals(isLeader, entity.isLeader);
  }

  @Override
  public int hashCode() {
    return Objects.hash(surname, name, patronymic, gender, birthday, phoneNumber, departmentId,
      employmentDate, quiteDate, position, payment, isLeader);
  }
}
