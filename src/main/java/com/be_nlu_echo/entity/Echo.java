package com.be_nlu_echo.entity;

import com.be_nlu_echo.enums.EchoStatus;
import com.be_nlu_echo.enums.EchoType;
import com.be_nlu_echo.enums.Visibility;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "echoes")
public class Echo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title", length = 120)
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "echo_type", nullable = false, columnDefinition = "ENUM('TEXT','IMAGE','AUDIO','MIXED')")
    private EchoType echoType;

    @Column(name = "latitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @Column(name = "longitude", nullable = false, precision = 10, scale = 7)
    private BigDecimal longitude;

    @Column(name = "gps_accuracy", precision = 6, scale = 2)
    private BigDecimal gpsAccuracy;

    @Column(name = "location_name", length = 150)
    private String locationName;

    @Column(name = "unlock_radius_m", nullable = false)
    private Integer unlockRadiusM;

    @Column(name = "is_anonymous", nullable = false)
    private boolean anonymous;

    @Column(name = "is_capsule", nullable = false)
    private boolean capsule;

    @Column(name = "unlock_at")
    private LocalDateTime unlockAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility", nullable = false, columnDefinition = "ENUM('PUBLIC','FOLLOWERS','PRIVATE')")
    private Visibility visibility;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('ACTIVE','HIDDEN','DELETED')")
    private EchoStatus status;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount;

    @Column(name = "comment_count", nullable = false)
    private Integer commentCount;

    @Column(name = "unlock_count", nullable = false)
    private Integer unlockCount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "echo")
    private List<EchoMedia> mediaList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "echo")
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "echo")
    private List<EchoLike> likes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "echo")
    private List<EchoUnlock> unlocks = new ArrayList<>();
}

