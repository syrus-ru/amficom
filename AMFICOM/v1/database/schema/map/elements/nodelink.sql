CREATE TABLE MapNodeLinkElement
(
	id VARCHAR2(32),
	name VERCHAR(32),
--
	physical_link_id VARCHAR(32),
	start_node_id VARCHAR(32),
	end_node_id VARCHAR(32),
	length NUMBER(8, 2),
--
	CONSTRAINT mnle_pk PRIMARY KEY (id),
	CONSTRAINT mnle_mple_fk FOREIGN KEY (physical_link_id)
		REFERENCES MapPhysicalLinkElement (id) ON DELETE CASCADE
);

CREATE SEQUENCE MapNodeLinkElement_seq ORDER;

