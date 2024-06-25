package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "customer")
@Table(name = "customer")
public class CustomerEntity {
    @Id
    private String customerId;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
}
