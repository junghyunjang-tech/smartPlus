-- password is 'password' (BCrypt)
INSERT INTO TB_MEMBER (MEMBER_ID, PASSWORD, NAME, ROLE, USE_YN, BIRTH_DATE)
VALUES ('user', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRkgVduVvzNBQwOb0k6xvFe72.q', '테스트유저', 'ROLE_USER', 'Y', '20000101');
