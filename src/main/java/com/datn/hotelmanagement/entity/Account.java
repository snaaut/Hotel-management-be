package com.datn.hotelmanagement.entity;

import java.io.Serial;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;


@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "account")
public class Account extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String userName;
    String password;
    String email;
    String fullName;
    String phoneNumber;
    String address;
    Date lastLogin;
    String status;
    String role;
    String city;
    String country;
}
