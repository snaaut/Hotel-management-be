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
@Table(name = "room")
public class Room extends BaseEntity {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String typeRoom;
    String roomNumber;
    String description;
    @ElementCollection
    List<String> images;
    @ElementCollection
    List<String> facilities;
    int maxCapacity;
    boolean allowPet;
    double pricePerDay;

}
