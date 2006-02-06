-- $Id: schemedevice.sql,v 1.6 2005/06/15 17:03:10 bass Exp $

CREATE TABLE SchemeDevice (
	id NUMBER(19) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id NOT NULL,
	modifier_id NOT NULL,
	version NUMBER(19) NOT NULL,
--
	name VARCHAR2(32 CHAR) NOT NULL,
	description VARCHAR2(256 CHAR),
--
	parent_scheme_proto_element_id,
	parent_scheme_element_id,
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

COMMENT ON TABLE SchemeDevice IS '$Id: schemedevice.sql,v 1.6 2005/06/15 17:03:10 bass Exp $';

CREATE SEQUENCE SchemeDevice_Seq ORDER;
