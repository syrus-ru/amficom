CREATE TABLE MapSiteElement
(
	id VARCHAR2(32),
	name VERCHAR(32),
	modified DATE NOT NULL,
--
	description VARCHAR2(256),
	symbol_id VARCHAR(32),
	longitude NUMBER(7, 4),
	latitude NUMBER(7, 4),
	map_proto_id VARCHAR(32),
	city VARCHAR(256),
	street VARCHAR(256),
	building VARCHAR(256),
--
	CONSTRAINT mse_pk PRIMARY KEY (id),
	CONSTRAINT mse_image_fk FOREIGN KEY (symbol_id)
		REFERENCES ImageResources (id) ON DELETE CASCADE,
	CONSTRAINT mse_proto_fk FOREIGN KEY (map_proto_id)
		REFERENCES MapNodeProtoElement (id) ON DELETE CASCADE
);

CREATE SEQUENCE MapSiteElement_seq ORDER;

