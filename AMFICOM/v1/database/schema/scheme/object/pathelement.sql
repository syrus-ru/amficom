-- $Id: pathelement.sql,v 1.1 2005/02/21 08:30:18 bass Exp $

CREATE TABLE "PathElement" (
	id VARCHAR2(32 CHAR) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id VARCHAR2(32 CHAR) NOT NULL,
	modifier_id VARCHAR2(32 CHAR) NOT NULL,
	version NUMBER(19) NOT NULL,
--
        CONSTRAINT pathelement_pk PRIMARY KEY(id),
--
	CONSTRAINT pathelement_creator_fk FOREIGN KEY(creator_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT pathelement_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES "User"(id) ON DELETE CASCADE
);

COMMENT ON TABLE "PathElement" IS '$Id: pathelement.sql,v 1.1 2005/02/21 08:30:18 bass Exp $';

CREATE SEQUENCE "PathElement_Seq" ORDER;
