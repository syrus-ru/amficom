-- $Id: schemepath.sql,v 1.3 2005/06/09 14:40:12 max Exp $

CREATE TABLE SchemePath (
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
        CONSTRAINT schemepath_pk PRIMARY KEY(id),
--
	CONSTRAINT schemepath_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT schemepath_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE
);

COMMENT ON TABLE SchemePath IS '$Id: schemepath.sql,v 1.3 2005/06/09 14:40:12 max Exp $';

CREATE SEQUENCE SchemePath_Seq ORDER;
