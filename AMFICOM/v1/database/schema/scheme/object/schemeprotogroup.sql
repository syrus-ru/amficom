-- $Id: schemeprotogroup.sql,v 1.8 2005/09/25 17:52:41 bass Exp $

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
		REFERENCES ImageResource(id) ON DELETE SET NULL,
	CONSTRAINT schemeprotogroup_prnt_spg_fk FOREIGN KEY(parent_scheme_proto_group_id)
		REFERENCES SchemeProtoGroup(id) ON DELETE SET NULL
);

COMMENT ON TABLE SchemeProtoGroup IS '$Id: schemeprotogroup.sql,v 1.8 2005/09/25 17:52:41 bass Exp $';

CREATE SEQUENCE SchemeProtoGroup_Seq ORDER;
