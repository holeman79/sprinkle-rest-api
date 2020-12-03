DROP TABLE IF EXISTS SPRINKLINGS;
CREATE TABLE SPRINKLINGS(
    SPRINKLING_ID INT PRIMARY KEY AUTO_INCREMENT,
    CREATOR_ID INT NOT NULL              COMMENT '생성자 ID',
    PEOPLE_COUNT INT NOT NULL              COMMENT '인원수',
    ROOM_ID VARCHAR(100) NOT NULL          COMMENT '대화방 ID',
    SPRINKLED_MONEY INT NOT NULL              COMMENT '뿌린 금액',
    SPRINKLED_TIME TIMESTAMP NOT NULL              COMMENT '뿌린 시간',
    TOKEN VARCHAR(3) NOT NULL              COMMENT '토큰'
);

DROP TABLE IF EXISTS RECEIVINGS;
CREATE TABLE RECEIVINGS(
    RECEIVING_ID INT PRIMARY KEY AUTO_INCREMENT,
    RECEIVED_MONEY INT NOT NULL              COMMENT '받은 금액',
    RECEIVED_TIME TIMESTAMP               COMMENT '받은시간',
    RECEIVER_ID INT              COMMENT '수신자 ID',
    VERSION     INT NOT NULL                COMMENT '버전',
    SPRINKLING_ID     INT NULL                COMMENT '뿌리기 ID'
);



DROP TABLE IF EXISTS WORDS;
CREATE TABLE WORDS(
    WORD_ID INT PRIMARY KEY AUTO_INCREMENT,
    LOGICAL_NAME VARCHAR(255) NOT NULL              COMMENT '논리명',
    PHYSICAL_NAME VARCHAR(255) NOT NULL              COMMENT '물리명',
    COMMENT VARCHAR(255)                       COMMENT '기타 내용'
);

DROP TABLE IF EXISTS TERMS;
CREATE TABLE TERMS(
    TERM_ID INT PRIMARY KEY AUTO_INCREMENT,
    LOGICAL_NAME VARCHAR(255) NOT NULL              COMMENT '논리명',
    PHYSICAL_NAME VARCHAR(255) NOT NULL              COMMENT '물리명',
    TYPE VARCHAR(255) NOT NULL              COMMENT '타입 정의',
    COMMENT VARCHAR(255)                       COMMENT '기타 내용'
);