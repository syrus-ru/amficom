-- $Id: schemelink.sql,v 1.1 2005/02/18 16:45:56 bass Exp $

CREATE TABLE "SchemeLink" (
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
	physical_length NUMBER NOT NULL,
	optical_length NUMBER NOT NULL,
	link_type_id VARCHAR2(32 CHAR),
	link_id VARCHAR2(32 CHAR),
--
	CONSTRAINT schmlnk_pk PRIMARY KEY(id),
--
	CONSTRAINT schmlnk_creator_fk FOREIGN KEY(creator_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT schmlnk_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
--
	CONSTRAINT schmlnk_lnk_tp_fk FOREIGN KEY(link_type_id)
		REFERENCES LinkType(id) ON DELETE CASCADE,
	CONSTRAINT schmlnk_lnk_fk FOREIGN KEY(link_id)
		REFERENCES Link(id) ON DELETE CASCADE
);

CREATE SEQUENCE "SchemeLink_Seq" ORDER;
