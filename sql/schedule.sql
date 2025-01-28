
-- 일정 테이블
CREATE TABLE to_do (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    date DATE,
    registered_date DATETIME,
    modified_date DATETIME,
    work TEXT
);

-- 유저 테이블
CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) UNIQUE NOT NULL ,
    password INT NOT NULL ,
    name CHAR(10) UNICODE NOT NULL ,
    to_do_id BIGINT,
    FOREIGN KEY (to_do_id) REFERENCES to_do(id)
);

-- 스케줄 테이블
CREATE TABLE schedule (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT,
    to_do_id BIGINT,
    date DATE,
    FOREIGN KEY (user_id) REFERENCES user(id),
    FOREIGN KEY (to_do_id) REFERENCES to_do(id)
);
