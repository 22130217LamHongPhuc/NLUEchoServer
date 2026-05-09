package com.be_nlu_echo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
    name = "echo_unlocks"
)
public class EchoUnlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "echo_id", nullable = false)
    private Echo echo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "unlocked_at", nullable = false)
    private LocalDateTime unlockedAt;

    @Column(name = "unlock_latitude", precision = 10, scale = 7)
    private BigDecimal unlockLatitude;

    @Column(name = "unlock_longitude", precision = 10, scale = 7)
    private BigDecimal unlockLongitude;

    @Column(name = "distance_m", precision = 8, scale = 2)
    private BigDecimal distanceM;
}

