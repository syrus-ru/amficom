-- $Id: schemedevice.sql,v 1.5 2005/06/09 14:40:12 max Exp $

CREATE TABLE SchemeDevice (
	id NUMBER(19) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id NUMBER(19) NOT NULL,
	modifier_id NUMBER(19) NOT NULL,
	version NUMBER(19) NOT NULL,
--
	name VARCHAR2(32 CHAR) NOT NULL,
	description VARCHAR2(256 CHAR),
--
	parent_scheme_proto_element_id NUMBER(19),
	parent_scheme_element_id NUMBER(19),
--
	CONSTRAINT schemdev_pk PRIMARY KEY(id),
--
	CONSTRAINT schemdev_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT schemdev_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT schemdev_prnt_schmprotelmnt_fk FOREIGN KEY(parent_scheme_proto_element_id)
		REFERENCES SchemeProtoElement(id) ON DELETE CASCADE,
	CONSTRAINT schemdev_prnt_schmelmnt_fk FOREIGN KEY(parent_scheme_element_id)
		REFERENCES SchemeElement(id) ON DELETE CASCADE,
--
	-- Boolean XOR: only one of parent_scheme_proto_element_id and
	-- parent_scheme_element_id may be defined, and only one may be null.
	CONSTRAINT schemdev_prnt_chk CHECK
		((parent_scheme_proto_element_id IS NULL
		AND parent_scheme_element_id IS NOT NULL)
		OR (parent_scheme_proto_element_id IS NOT NULL
		AND parent_scheme_element_id IS NULL))
);

COMMENT ON TABLE SchemeDevice IS '$Id: schemedevice.sql,v 1.5 2005/06/09 14:40:12 max Exp $';

CREATE SEQUENCE SchemeDevice_Seq ORDER;
