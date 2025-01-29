-- 유저 테이블
CREATE TABLE user (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
#     schedule_id BIGINT,
#     FOREIGN KEY (schedule_id) REFERENCES schedule(id),
                      email VARCHAR(100) UNIQUE NOT NULL ,
                      password INT NOT NULL ,
                      name CHAR(10) UNICODE NOT NULL
);

-- 일정 테이블
CREATE TABLE to_do (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES user(id),
#     schedule_id BIGINT,
#     FOREIGN KEY (schedule_id) REFERENCES schedule(id),
    registered_date DATETIME,
    modified_date DATETIME,
    work TEXT
);
