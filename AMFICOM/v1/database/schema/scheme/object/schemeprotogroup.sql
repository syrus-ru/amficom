-- $Id: schemeprotogroup.sql,v 1.1 2005/02/04 07:37:57 bass Exp $

CREATE TABLE "SchemeProtoGroup" (
	id VARCHAR2(32) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id VARCHAR2(32) NOT NULL,
	modifier_id VARCHAR2(32) NOT NULL,
--
	name VARCHAR2(32) NOT NULL,
	description VARCHAR2(256),
--
	symbol_id VARCHAR2(32),
	parent_scheme_proto_group_id VARCHAR2(32),
--
	CONSTRAINT schemeprotogroup_pk PRIMARY KEY(id),
--
	CONSTRAINT schemeprotogroup_creator_fk FOREIGN KEY(creator_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotogroup_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
--
	CONSTRAINT schemeprotogroup_symbol_fk FOREIGN KEY(symbol_id)
		REFERENCES "ImageResource"(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotogroup_prnt_spg_fk FOREIGN KEY(parent_scheme_proto_group_id)
		REFERENCES "SchemeProtoGroup"(id) ON DELETE CASCADE
);

CREATE SEQUENCE "SchemeProtoGroup_Seq" ORDER;
