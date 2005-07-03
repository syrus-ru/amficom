-- $Id: schemeprotogroup.sql,v 1.7 2005/06/15 17:03:10 bass Exp $

CREATE TABLE SchemeProtoGroup (
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
	symbol_id,
	parent_scheme_proto_group_id,
--
	CONSTRAINT schemeprotogroup_pk PRIMARY KEY(id),
--
	CONSTRAINT schemeprotogroup_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotogroup_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT schemeprotogroup_symbol_fk FOREIGN KEY(symbol_id)
		REFERENCES ImageResource(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotogroup_prnt_spg_fk FOREIGN KEY(parent_scheme_proto_group_id)
		REFERENCES SchemeProtoGroup(id) ON DELETE CASCADE
);

COMMENT ON TABLE SchemeProtoGroup IS '$Id: schemeprotogroup.sql,v 1.7 2005/06/15 17:03:10 bass Exp $';

CREATE SEQUENCE SchemeProtoGroup_Seq ORDER;
