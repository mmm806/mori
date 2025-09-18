-- ========== ENUMS ==========
-- 유저 권한
CREATE TYPE user_role AS ENUM ('USER','ADMIN');
-- 일기 공개 범위
CREATE TYPE diary_visibility AS ENUM ('PRIVATE', 'PUBLIC');
-- 일기 감정
CREATE TYPE diary_mood AS ENUM ('HAPPY','SAD','ANGRY','NEUTRAL','ANXIOUS','EXCITED');
-- 나무 이벤트 종류
CREATE TYPE tree_event_type AS ENUM ('WRITE_DIARY','EDIT_DIARY','DELETE_DIARY','STREAK_BONUS','LEVEL_UP','MANUAL_ADJUST');

CREATE EXTENSION IF NOT EXISTS "pgcrypto";
-- ========== USERS ==========
-- 회원 계정 및 프로필 기본 정보
CREATE TABLE users (
                       id         UUID PRIMARY KEY,
                       email      VARCHAR(255) NOT NULL UNIQUE,      -- 로그인 ID
                       pw_hash    VARCHAR(255) NOT NULL,             -- 비밀번호 해시
                       nickname   VARCHAR(50)  NOT NULL UNIQUE,      -- 표시명
                       avatar_url VARCHAR(500),                      -- 프로필 이미지(선택)
                       role       user_role   NOT NULL DEFAULT 'USER', -- 유저 권한
                       created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                       updated_at TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- 간단 집계 캐시(앱 로직으로 갱신)
DROP TABLE IF EXISTS user_stats;
CREATE TABLE user_stats (
                            id         UUID PRIMARY KEY,
                            user_id         UUID REFERENCES users(id) ON DELETE CASCADE,
                            total_entries   INT NOT NULL DEFAULT 0, -- 누적 일기수
                            current_streak  INT NOT NULL DEFAULT 0, -- 현재 연속 작성 일수
                            longest_streak  INT NOT NULL DEFAULT 0, -- 최장 연속 작성 일수
                            updated_at      TIMESTAMPTZ NOT NULL DEFAULT now() -- 통계 갱신 시각
);

-- ========== DIARIES ==========
-- 일기 본문. 하루 1개 제한. 소프트 삭제 지원.
CREATE TABLE diaries (
                         id UUID PRIMARY KEY,
                         user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                         title VARCHAR(120) NOT NULL,
                         content TEXT NOT NULL,
                         mood diary_mood,
                         visibility diary_visibility NOT NULL DEFAULT 'PRIVATE',
                         entry_date DATE NOT NULL, -- 달력 기준 날짜
                         is_edited BOOLEAN NOT NULL DEFAULT FALSE,
                         created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                         updated_at TIMESTAMPTZ NOT NULL DEFAULT now(),
                         deleted_at TIMESTAMPTZ
);

CREATE UNIQUE INDEX ux_diary_user_day_not_deleted
    ON diaries(user_id, entry_date) WHERE deleted_at IS NULL;




