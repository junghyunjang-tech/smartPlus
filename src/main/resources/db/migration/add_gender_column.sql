-- TB_MEMBER 테이블에 성별 컬럼 추가
-- 작성일: 2026-01-20
-- 설명: 회원가입 시 성별 정보를 저장하기 위한 컬럼 추가

ALTER TABLE TB_MEMBER ADD COLUMN GENDER VARCHAR(1);

COMMENT ON COLUMN TB_MEMBER.GENDER IS '성별 (M:남성, F:여성)';
