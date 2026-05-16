package com.be_nlu_echo.entity;

import com.be_nlu_echo.enums.AuthProvider;
import com.be_nlu_echo.enums.UserStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_users_student_code", columnNames = "student_code"),
                @UniqueConstraint(name = "uq_users_email", columnNames = "email")
        }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "student_code", length = 20)
    private String studentCode;

    @Column(name = "full_name", nullable = false, length = 100)
    private String fullName;

    @Column(name = "email", nullable = false, length = 120)
    private String email;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "avatar_url", columnDefinition = "TEXT")
    private String avatarUrl;

    @Column(name = "faculty", length = 100)
    private String faculty;

    @Column(name = "bio", length = 255)
    private String bio;

    @Column(name = "is_email_verified", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean emailVerified;

    @Column(name = "default_ghost_mode", nullable = false, columnDefinition = "TINYINT(1)")
    private boolean defaultGhostMode;

    @Enumerated(EnumType.STRING)
    @Column(name = "auth_provider", nullable = false, columnDefinition = "ENUM('LOCAL','GOOGLE')")
    private AuthProvider authProvider;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('ACTIVE','BLOCKED','DELETED')")
    private UserStatus status;



    @Builder.Default
    @Column(name = "total_xp", nullable = false)
    private Integer totalXp = 0;

    @Builder.Default
    @Column(name = "coins", nullable = false)
    private Integer coins = 0;

    @Builder.Default
    @Column(name = "streak_days", nullable = false)
    private Integer streakDays = 0;

    @Column(name = "last_active_date")
    private LocalDate lastActiveDate;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<RefreshToken> refreshTokens = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<EmailVerification> emailVerifications = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<PasswordResetToken> passwordResetTokens = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Echo> echoes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Comment> comments = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<EchoLike> likes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "follower")
    private List<Follow> followingRelations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "following")
    private List<Follow> followerRelations = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<EchoUnlock> echoUnlocks = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<Notification> notifications = new ArrayList<>();

    public void addXp(int amount) {
        if (amount <= 0) {
            return;
        }

        if (this.totalXp == null) {
            this.totalXp = 0;
        }

        this.totalXp += amount;
    }

    public void addCoins(int amount) {
        if (amount <= 0) {
            return;
        }

        if (this.coins == null) {
            this.coins = 0;
        }

        this.coins += amount;
    }

    public void updateStreak(LocalDate today) {
        if (today == null) {
            today = LocalDate.now();
        }

        if (this.lastActiveDate == null) {
            this.streakDays = 1;
            this.lastActiveDate = today;
            return;
        }

        if (this.lastActiveDate.isEqual(today)) {
            return;
        }

        if (this.lastActiveDate.plusDays(1).isEqual(today)) {
            this.streakDays = this.streakDays == null ? 1 : this.streakDays + 1;
        } else {
            this.streakDays = 1;
        }

        this.lastActiveDate = today;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", studentCode='" + studentCode + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", avatarUrl='" + avatarUrl + '\'' +
                ", faculty='" + faculty + '\'' +
                ", bio='" + bio + '\'' +
                ", emailVerified=" + emailVerified +
                ", defaultGhostMode=" + defaultGhostMode +
                ", authProvider=" + authProvider +
                ", status=" + status +
                ", totalXp=" + totalXp +
                ", coins=" + coins +
                ", streakDays=" + streakDays +
                ", lastActiveDate=" + lastActiveDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}