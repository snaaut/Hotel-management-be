package com.datn.hotelmanagement.entity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.sql.Date;
@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "Review")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    int star;
    String review;
    @ManyToOne
    @JoinColumn(name = "user_id")
     Account user;
    @ManyToOne
    @JoinColumn(name = "room_id")
     Room room;
    String reviewAt;
}
