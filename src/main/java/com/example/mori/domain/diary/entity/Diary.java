package com.example.mori.domain.diary.entity;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.example.mori.domain.user.entity.User;
import com.example.mori.global.enumType.DiaryVisibility;
import com.example.mori.global.enumType.DailyMood;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "diaries")
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
/** JPA delete() 호출 시 실제 삭제 대신 deleted_at만 채움 */
@SQLDelete(sql = "UPDATE diaries SET deleted_at = now() WHERE id = ?")
/** 조회 시 삭제된 행 자동 제외 */
@Where(clause = "deleted_at IS NULL")
public class Diary {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	@Column(columnDefinition = "uuid")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(name = "entry_date", nullable = false)
	private LocalDate entryDate;

	@Column(nullable = false, columnDefinition = "text")
	private String title;

	@Column(nullable=false, columnDefinition = "text")
	private String content;

	@Enumerated(EnumType.STRING)
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	@Column(columnDefinition = "diary_mood")
	private DailyMood mood;

	@Enumerated(EnumType.STRING)
	@JdbcTypeCode(SqlTypes.NAMED_ENUM)
	@Column(nullable=false, columnDefinition = "diary_visibility")
	private DiaryVisibility visibility = DiaryVisibility.PRIVATE;

	@Column(name="is_edited", nullable=false)
	private boolean edited = false;

	@CreatedDate
	@Column(name="created_at", nullable=false)
	private Instant createdAt;

	@LastModifiedDate
	@Column(name="updated_at", nullable=false)
	private Instant updatedAt;

	@Column(name="deleted_at")
	private Instant deletedAt;


	// getter

	public UUID getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public LocalDate getEntryDate() {
		return entryDate;
	}

	public String getTitle() {
		return title;
	}

	public String getContent() {
		return content;
	}

	public DailyMood getMood() {
		return mood;
	}

	public DiaryVisibility getVisibility() {
		return visibility;
	}

	public boolean isEdited() {
		return edited;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public Instant getDeletedAt() {
		return deletedAt;
	}

	//setter
	public void setId(UUID id) {
		this.id = id;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public void setEntryDate(LocalDate entryDate) {
		this.entryDate = entryDate;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setMood(DailyMood mood) {
		this.mood = mood;
	}

	public void setVisibility(DiaryVisibility visibility) {
		this.visibility = visibility;
	}

	public void setEdited(boolean edited) {
		this.edited = edited;
	}
}
