CREATE TABLE Port (
 id VARCHAR2(32),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2(32) NOT NULL,
 modifier_id VARCHAR2(32) NOT NULL,
--
 domain_id VARCHAR2(32),
--
 type_id VARCHAR2(32) NOT NULL,
--
 description VARCHAR2(256),
--
 sort NUMBER(2) NOT NULL,
 port_id VARCHAR2(32),
 cable_port_id VARCHAR2(32),
 --
);

CREATE SEQUENCE port_seq ORDER;

