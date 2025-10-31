package com.thanhhoa.hotelmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "reservation_rooms", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "reservation_id", "room_id" })
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;
}
