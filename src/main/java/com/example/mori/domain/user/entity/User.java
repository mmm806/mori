package com.example.mori.domain.user.entity;

import java.time.Instant;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.mori.global.enumType.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(columnDefinition = "uuid")
	private UUID id;

	@Column(nullable = false, unique = true, length = 255)
	private String email;

	@Column(name = "pw_hash", nullable = false, length = 255)
	private String pwHash;

	@Column(nullable = false, unique = true, length = 50)
	private String nickname;

	@Column(name = "avatar_url", length = 500)
	private String avatarUrl;

	@Enumerated(EnumType.STRING)
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	@Column(name = "role", nullable = false, columnDefinition = "user_role")
	private UserRole role = UserRole.USER;


	@Column(name = "created_at", nullable = false, updatable = false)
	@CreatedDate
	private Instant createdAt;

	@Column(name = "updated_at", nullable = false)
	@LastModifiedDate
	private Instant updatedAt;



	protected User() {}
	private User(String email, String pwHash, String nickname, String avatarUrl) {
		this.email = email.toLowerCase();
		this.pwHash = pwHash;
		this.nickname = nickname;
		this.avatarUrl = avatarUrl;
	}

	public static User create(String email, String pwHash, String nickname, String avatarUrl) {
		return new User(email, pwHash, nickname, avatarUrl);
	}


	// getter
	public UUID getId() { return id; }
	public String getEmail() { return email; }
	public String getNickname() { return nickname; }
	public String getPwHash() { return pwHash; }
	public String getAvatarUrl() { return avatarUrl; }
	public UserRole getRole() { return role; }
	public Instant getCreatedAt() { return createdAt; }
	public Instant getUpdatedAt() { return updatedAt; }

	//setter
	public void setNickname(String nickname) { this.nickname = nickname; }
	public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
	public void setRole(UserRole role) { this.role = role; }

	public void updateProfile(String nickname, String avatarUrl) {
		if (nickname != null) this.nickname = nickname;
		if (avatarUrl != null) this.avatarUrl = avatarUrl;
	}

	public void changePasswordHash(String newHash) { this.pwHash = newHash; }
}
