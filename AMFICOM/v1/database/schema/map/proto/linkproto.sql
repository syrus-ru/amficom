CREATE TABLE MapLinkProtoElement
(
	id VARCHAR2(32),
	name VERCHAR(32),
	modified DATE NOT NULL,
--
	description VARCHAR2(256),
--
	CONSTRAINT mlpe_pk PRIMARY KEY (id)
);

CREATE SEQUENCE MapLinkProtoElement_seq ORDER;

