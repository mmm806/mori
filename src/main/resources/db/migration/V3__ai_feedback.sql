DROP TABLE IF EXISTS ai_feedbacks;
CREATE TABLE ai_feedbacks (
                              id              UUID PRIMARY KEY,
                              diary_id        UUID NOT NULL REFERENCES diaries(id) ON DELETE CASCADE,
                              user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                              provider        VARCHAR(30) DEFAULT 'OPENAI',
                              model           VARCHAR(80),
                              request_id      VARCHAR(120),                 -- 외부 호출 추적용(옵션)
                              content         TEXT NOT NULL,                -- 생성된 피드백 전문
                              prompt_tokens   INT,
                              completion_tokens INT,
                              total_tokens    INT,
                              created_at      TIMESTAMPTZ NOT NULL DEFAULT now()
);

