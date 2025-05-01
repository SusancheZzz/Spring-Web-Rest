package com.rntgroup.api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment_in_department_info", schema = "departments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentPaymentEntity {

  @Id
  @Column(name = "department_id")
  private Long id;

  @Column(name = "common_payment")
  private Integer payment;
}
