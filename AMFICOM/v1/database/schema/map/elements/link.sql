CREATE TABLE MapPhysicalLinkElement
(
	id VARCHAR2(32),
	name VERCHAR(32),
--
	description VARCHAR2(256),
	map_proto_id VARCHAR(32),
	start_node_id VARCHAR(32),
	end_node_id VARCHAR(32),
	dimensionX NUMBER(3),
	dimensionY NUMBER(3),
	leftToRight NUMBER(1),
	topToBottom NUMBER(1),
	city VARCHAR(256),
	street VARCHAR(256),
	building VARCHAR(256),
--
	CONSTRAINT mnle_pk PRIMARY KEY (id),
	CONSTRAINT mse_proto_fk FOREIGN KEY (map_proto_id)
		REFERENCES MapLinkProtoElement (id) ON DELETE CASCADE
);

CREATE SEQUENCE MapPhysicalLinkElement_seq ORDER;

