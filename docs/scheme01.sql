CREATE TABLE championship (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    start_date DATE NOT NULL,
    last_date DATE NOT NULL
);

-- 팀은 전역 단위에서 유일하게 관리
CREATE TABLE team (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);

-- 경기 (championship 내에 속함)
CREATE TABLE match (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    championship_id BIGINT NOT NULL,
    title VARCHAR(500) NOT NULL,
    video_id VARCHAR(100) NOT NULL,
    match_date DATE NOT NULL,
    medium_url VARCHAR(500),
    medium_width INT,
    medium_height INT,
    high_url VARCHAR(500),
    high_width INT,
    high_height INT,
    standard_url VARCHAR(500),
    standard_width INT,
    standard_height INT,
    FOREIGN KEY (championship_id) REFERENCES championship(id) ON DELETE CASCADE,
    INDEX idx_match_championship (championship_id),
    INDEX idx_match_date (match_date)
);

-- 경기 참가 팀 (승/패 포함)
CREATE TABLE match_team (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    match_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    result ENUM('WIN','LOSE') NOT NULL,
    FOREIGN KEY (match_id) REFERENCES match(id) ON DELETE CASCADE,
    FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE CASCADE,
    UNIQUE KEY uniq_match_team (match_id, team_id),
    INDEX idx_team_result (team_id, result)  -- 팀 전적 조회 최적화
);
