package org.example.dto.tm;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerTable {
    private String customerId;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;
}
