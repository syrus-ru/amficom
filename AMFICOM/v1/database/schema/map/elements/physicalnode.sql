CREATE TABLE MapPhysicalNodeElement
(
	id VARCHAR2(32),
	name VERCHAR(32),
--
	description VARCHAR2(256),
	longitude NUMBER(7, 4),
	latitude NUMBER(7, 4),
	physical_link_id VARCHAR(32),
	active NUMBER(1),
--
	CONSTRAINT mpne_pk PRIMARY KEY (id),
	CONSTRAINT mpne_mple_fk FOREIGN KEY (physical_link_id)
		REFERENCES MapPhysicalLinkElement (id) ON DELETE CASCADE
);

CREATE SEQUENCE MapPhysicalNodeElement_seq ORDER;

