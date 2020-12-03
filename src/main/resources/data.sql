INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(1, '뿌리기', 'sprinkling', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(2, '받기', 'receiving', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(3, '생성자', 'creator', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(4, '수신자', 'receiver', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(5, '조회자', 'viewer', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(6, '토큰 ', 'token', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(7, '대화방', 'room', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(8, '금액', 'money', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(9, '뿌린', 'sprinkled', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(10, '받은', 'received', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(11, '인원', 'people', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(12, '수', 'count', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(13, 'ID', 'id', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(14, '시간', 'time', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(15, '양', 'amount', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(16, '버전', 'version', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(17, '최대', 'max', NULL);
INSERT INTO WORDS(WORD_ID, LOGICAL_NAME, PHYSICAL_NAME, COMMENT) VALUES(18, '무작위', 'random', NULL);

INSERT INTO TERMS(TERM_ID, LOGICAL_NAME, PHYSICAL_NAME, TYPE, COMMENT) VALUES(1, 'ID', 'id', 'BIGINT', NULL);
INSERT INTO TERMS(TERM_ID, LOGICAL_NAME, PHYSICAL_NAME, TYPE, COMMENT) VALUES(2, '대화방 ID', 'room_id', 'VARCHAR2(255)', NULL);
INSERT INTO TERMS(TERM_ID, LOGICAL_NAME, PHYSICAL_NAME, TYPE, COMMENT) VALUES(3, '생성자 ID', 'creator_id', 'BIGINT', NULL);
INSERT INTO TERMS(TERM_ID, LOGICAL_NAME, PHYSICAL_NAME, TYPE, COMMENT) VALUES(4, '인원수', 'people_count', 'INT', NULL);
INSERT INTO TERMS(TERM_ID, LOGICAL_NAME, PHYSICAL_NAME, TYPE, COMMENT) VALUES(5, '뿌린 금액', 'sprinkled_money', 'BIGINT', NULL);
INSERT INTO TERMS(TERM_ID, LOGICAL_NAME, PHYSICAL_NAME, TYPE, COMMENT) VALUES(6, '토큰', 'token', 'VARCHAR2(3)', NULL);
INSERT INTO TERMS(TERM_ID, LOGICAL_NAME, PHYSICAL_NAME, TYPE, COMMENT) VALUES(7, '뿌린 시간', 'sprinkled_time', 'DATETIME', NULL);
INSERT INTO TERMS(TERM_ID, LOGICAL_NAME, PHYSICAL_NAME, TYPE, COMMENT) VALUES(8, '수신자 ID', 'receiver_id', 'BIGINT', NULL);
INSERT INTO TERMS(TERM_ID, LOGICAL_NAME, PHYSICAL_NAME, TYPE, COMMENT) VALUES(9, '받은 금액', 'received_money', 'BIGINT', NULL);
INSERT INTO TERMS(TERM_ID, LOGICAL_NAME, PHYSICAL_NAME, TYPE, COMMENT) VALUES(10, '받은 시간', 'received_time', 'DATETIME', NULL);
INSERT INTO TERMS(TERM_ID, LOGICAL_NAME, PHYSICAL_NAME, TYPE, COMMENT) VALUES(11, '버전', 'version', 'INT', NULL);
INSERT INTO TERMS(TERM_ID, LOGICAL_NAME, PHYSICAL_NAME, TYPE, COMMENT) VALUES(12, '최대 무작위 금액', 'max_random_money', 'BIGINT', NULL);
INSERT INTO TERMS(TERM_ID, LOGICAL_NAME, PHYSICAL_NAME, TYPE, COMMENT) VALUES(13, '양', 'amount', 'BIGINT', NULL);