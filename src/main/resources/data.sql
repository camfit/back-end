INSERT INTO member (MEMBER_ID, USERNAME, PASSWORD, REGDATE, GENDER, DATE_OF_BIRTH, PHONE_NUMBER) VALUES (1, 'admin', '$2a$08$lDnHPz7eUkSi6ao14Twuau08mzhWrL4kyZGGU5xfiGALO/Vxd5DOi', NOW(), '남', '20051208', '010-8988-3538');
INSERT INTO member (MEMBER_ID, USERNAME, PASSWORD, REGDATE, GENDER, DATE_OF_BIRTH, PHONE_NUMBER) VALUES (2, 'user', '$2a$08$UkVvwpULis18S19S5pZFn.YHPZt3oaqHZnDwqbCW9pft6uFtkXKDC', NOW(), '남', '20051208', '010-8988-3538');

INSERT INTO AUTHORITY (AUTHORITY_NAME) values ('ROLE_USER');
INSERT INTO AUTHORITY (AUTHORITY_NAME) values ('ROLE_ADMIN');

INSERT INTO MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_NAME) values (1, 'ROLE_USER');
INSERT INTO MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_NAME) values (1, 'ROLE_ADMIN');
INSERT INTO MEMBER_AUTHORITY (MEMBER_ID, AUTHORITY_NAME) values (2, 'ROLE_USER');