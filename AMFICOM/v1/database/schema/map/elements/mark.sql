CREATE TABLE MapMarkElement
(
	id VARCHAR2(32),
	name VERCHAR(32),
--
	description VARCHAR2(256),
	longitude NUMBER(7, 4),
	latitude NUMBER(7, 4),
	physical_link_id VARCHAR(32),
	distance NUMBER(8, 2),
	city VARCHAR(256),
	street VARCHAR(256),
	building VARCHAR(256),
--
	CONSTRAINT mse_pk PRIMARY KEY (id),
	CONSTRAINT mme_mple_fk FOREIGN KEY (physical_link_id)
		REFERENCES MapPhysicalLinkElement (id) ON DELETE CASCADE
);

CREATE SEQUENCE MapMarkElement_seq ORDER;

