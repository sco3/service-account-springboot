CREATE DATABASE IF NOT EXISTS service_accounts;

CREATE TABLE IF NOT EXISTS service_accounts.users
(
    user_id          UInt64,
    service_account  String,
    created_at       DateTime DEFAULT now()
)
ENGINE = MergeTree()
ORDER BY (user_id, service_account)
SETTINGS index_granularity = 8192;

ALTER TABLE service_accounts.users 
ADD INDEX idx_service_account service_account TYPE bloom_filter GRANULARITY 1;

-- Rebuild the index (takes time)
ALTER TABLE service_accounts.users MATERIALIZE INDEX idx_service_account;

CREATE TABLE IF NOT EXISTS service_accounts.history
(
    time_id          Date,
    user_id          UInt64,
    service_account  String,
    total_requests   UInt64,
    error_count      UInt64,
    avg_latency_ms   Float64,
    data_volume_mb   Float64,
    success_rate     Float64,
    p99_latency_ms   Float64
)
ENGINE = MergeTree()
PARTITION BY toYYYYMM(time_id)
ORDER BY (user_id, service_account, time_id)
SETTINGS index_granularity = 8192;

CREATE INDEX IF NOT EXISTS idx_history_user_id ON service_accounts.history (user_id) TYPE minmax GRANULARITY 4;
CREATE INDEX IF NOT EXISTS idx_history_service_account ON service_accounts.history (service_account) TYPE minmax GRANULARITY 4;
