package org.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "supplier")
@Table(name = "supplier")
public class SupplierEntity {
    @Id
    private String supplierId;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
}
