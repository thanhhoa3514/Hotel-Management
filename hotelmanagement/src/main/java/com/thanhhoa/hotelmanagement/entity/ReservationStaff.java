package com.thanhhoa.hotelmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservation_staff", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "reservation_id", "staff_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationStaff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "staff_id", nullable = false)
    private Staff staff;
}
