-- 상태 ENUM
CREATE TYPE friend_status AS ENUM ('PENDING','ACCEPTED','DECLINED','CANCELED');

-- 단일 테이블(요청 + 친구관계)
CREATE TABLE friendships (
                             id            UUID PRIMARY KEY,
                             requester_id  UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE, -- 보낸 쪽
                             addressee_id  UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE, -- 받은 쪽
                             status        friend_status NOT NULL DEFAULT 'PENDING',
                             requested_at  TIMESTAMPTZ NOT NULL DEFAULT now(),
                             responded_at  TIMESTAMPTZ
);



