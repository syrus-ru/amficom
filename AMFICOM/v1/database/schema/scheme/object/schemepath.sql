-- $Id: schemepath.sql,v 1.1 2005/02/21 08:30:18 bass Exp $

CREATE TABLE "SchemePath" (
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
        CONSTRAINT schemepath_pk PRIMARY KEY(id),
--
	CONSTRAINT schemepath_creator_fk FOREIGN KEY(creator_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT schemepath_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES "User"(id) ON DELETE CASCADE
);

COMMENT ON TABLE "SchemePath" IS '$Id: schemepath.sql,v 1.1 2005/02/21 08:30:18 bass Exp $';

CREATE SEQUENCE "SchemePath_Seq" ORDER;
