package com.example.mori.domain.user.entity;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(
	name="user_stats",
	uniqueConstraints=@UniqueConstraint(name="uk_user_stats_user_id", columnNames="user_id")
)
@EntityListeners(AuditingEntityListener.class)
public class UserStats {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(columnDefinition = "uuid")
	private UUID id;

	@Column(name="user_id", columnDefinition="uuid", nullable=false)
	private UUID userId;

	@Column(name="total_entries", nullable=false)
	private int totalEntries = 0;

	@Column(name="current_streak", nullable=false)
	private int currentStreak = 0;

	@Column(name="longest_streak", nullable=false)
	private int longestStreak = 0;

	@LastModifiedDate
	@Column(name="updated_at", nullable=false)
	private Instant updatedAt;


	public UUID getId(){ return id; }
	public UUID getUserId(){ return userId; }
	public int getTotalEntries(){ return totalEntries; }
	public int getCurrentStreak(){ return currentStreak; }
	public int getLongestStreak(){ return longestStreak; }
	public Instant getUpdatedAt(){ return updatedAt; }

	protected UserStats() {}

	private UserStats(UUID userId) { this.userId = userId; }

	public static UserStats init(UUID userId) { return new UserStats(userId); }
}