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

-- ============================================
-- MLOps 파이프라인 테이블
-- ============================================

-- 모델 버전 레지스트리
CREATE TABLE IF NOT EXISTS model_versions (
    version_id          BIGSERIAL PRIMARY KEY,
    version_tag         VARCHAR(50) NOT NULL UNIQUE,
    artifacts_path      TEXT NOT NULL,
    status              VARCHAR(20) NOT NULL DEFAULT 'staged',
    window_size         INTEGER NOT NULL DEFAULT 10,
    global_threshold    FLOAT,
    training_loss       FLOAT,
    val_loss            FLOAT,
    training_epochs     INTEGER,
    description         TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT NOW(),
    activated_at        TIMESTAMP,
    archived_at         TIMESTAMP
);

-- 데이터 드리프트 감지 이력
CREATE TABLE IF NOT EXISTS drift_logs (
    drift_id            BIGSERIAL PRIMARY KEY,
    detected_at         TIMESTAMP NOT NULL DEFAULT NOW(),
    drift_type          VARCHAR(30) NOT NULL,
    severity            VARCHAR(10) NOT NULL,
    details             JSONB NOT NULL,
    model_version_id    BIGINT REFERENCES model_versions(version_id),
    acknowledged        BOOLEAN NOT NULL DEFAULT FALSE,
    acknowledged_by     VARCHAR(100),
    acknowledged_at     TIMESTAMP
);

-- 재학습 작업
CREATE TABLE IF NOT EXISTS retrain_jobs (
    job_id              BIGSERIAL PRIMARY KEY,
    status              VARCHAR(20) NOT NULL DEFAULT 'pending',
    requested_at        TIMESTAMP NOT NULL DEFAULT NOW(),
    requested_by        VARCHAR(100),
    drift_log_id        BIGINT REFERENCES drift_logs(drift_id),
    approved_by         VARCHAR(100),
    approved_at         TIMESTAMP,
    started_at          TIMESTAMP,
    completed_at        TIMESTAMP,
    base_model_id       BIGINT REFERENCES model_versions(version_id),
    new_model_id        BIGINT REFERENCES model_versions(version_id),
    config              JSONB,
    result              JSONB,
    error_message       TEXT
);

-- 모델 성능 추적
CREATE TABLE IF NOT EXISTS model_performance (
    perf_id             BIGSERIAL PRIMARY KEY,
    model_version_id    BIGINT REFERENCES model_versions(version_id),
    recorded_at         TIMESTAMP NOT NULL DEFAULT NOW(),
    avg_reconstruction_error FLOAT,
    anomaly_rate        FLOAT,
    avg_inference_ms    FLOAT,
    sample_count        INTEGER,
    details             JSONB
);