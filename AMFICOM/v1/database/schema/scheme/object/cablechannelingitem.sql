-- $Id: cablechannelingitem.sql,v 1.2 2005/04/22 14:45:49 bass Exp $

CREATE TABLE "CableChannelingItem" (
	id VARCHAR2(32 CHAR) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id VARCHAR2(32 CHAR) NOT NULL,
	modifier_id VARCHAR2(32 CHAR) NOT NULL,
	version NUMBER(19) NOT NULL,
--
        CONSTRAINT cablchannelingitem_pk PRIMARY KEY(id),
--
	CONSTRAINT cablchannelingitem_creator_fk FOREIGN KEY(creator_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT cablchannelingitem_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES "User"(id) ON DELETE CASCADE
);

COMMENT ON TABLE "CableChannelingItem" IS '$Id: cablechannelingitem.sql,v 1.2 2005/04/22 14:45:49 bass Exp $';

CREATE SEQUENCE "CableChannelingItem_Seq" ORDER;
