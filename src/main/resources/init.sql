-- ============================================
-- FastAPI 관리 테이블 초기화 스크립트
-- ============================================

-- events
CREATE TABLE IF NOT EXISTS events (
    event_id        BIGSERIAL PRIMARY KEY,
    timestamp       TIMESTAMP NOT NULL,
    anomaly_score   FLOAT,
    is_anomaly      BOOLEAN NOT NULL DEFAULT FALSE,
    fault_type      INTEGER,
    top_sensors     JSONB,
    sensor_values   JSONB,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

-- reports
CREATE TABLE IF NOT EXISTS reports (
    report_id           BIGSERIAL PRIMARY KEY,
    event_id            BIGINT REFERENCES events(event_id),
    fault_summary       TEXT,
    estimated_cause     TEXT,
    sensor_analysis     TEXT,
    similar_cases       TEXT,
    recommended_action  TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

-- inference_logs
CREATE TABLE IF NOT EXISTS inference_logs (
    log_id              BIGSERIAL PRIMARY KEY,
    timestamp           TIMESTAMP NOT NULL,
    sensor_values       JSONB,
    anomaly_score       FLOAT,
    is_anomaly          BOOLEAN NOT NULL DEFAULT FALSE,
    fault_type          INTEGER,
    model_version       VARCHAR(50),
    response_time_ms    INTEGER,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW()
);

-- notification_settings
CREATE TABLE IF NOT EXISTS notification_settings (
    id                  BIGSERIAL PRIMARY KEY,
    slack_webhook_url   TEXT,
    email_recipients    JSONB,
    enabled             BOOLEAN NOT NULL DEFAULT TRUE,
    updated_at          TIMESTAMP NOT NULL DEFAULT NOW()
);