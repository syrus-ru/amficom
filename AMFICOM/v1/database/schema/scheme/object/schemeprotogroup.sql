-- $Id: schemeprotogroup.sql,v 1.3 2005/02/10 11:09:26 max Exp $

CREATE TABLE "SchemeProtoGroup" (
	id VARCHAR2(32 CHAR) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id VARCHAR2(32 CHAR) NOT NULL,
	modifier_id VARCHAR2(32 CHAR) NOT NULL,
	version NUMBER(19) NOT NULL,
--
	name VARCHAR2(32 CHAR) NOT NULL,
	description VARCHAR2(256 CHAR),
--
	symbol_id VARCHAR2(32 CHAR),
	parent_scheme_proto_group_id VARCHAR2(32 CHAR),
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
