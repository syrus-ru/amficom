CREATE TABLE MapNodeProtoElement
(
	id VARCHAR2(32),
	name VERCHAR(32),
	modified DATE NOT NULL,
--
	description VARCHAR2(256),
	symbol_id VARCHAR(32),
	is_topological NOMBER(1)
--
	CONSTRAINT mnpe_pk PRIMARY KEY (id),
	CONSTRAINT mnpe_image_fk FOREIGN KEY (symbol_id)
		REFERENCES ImageResources (id) ON DELETE CASCADE
);

CREATE SEQUENCE MapNodeProtoElement_seq ORDER;

