-- $Id: cablechannelingitem.sql,v 1.4 2005/06/09 14:40:11 max Exp $

CREATE TABLE CableChannelingItem (
	id NUMBER(19) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id NUMBER(19) NOT NULL,
	modifier_id NUMBER(19) NOT NULL,
	version NUMBER(19) NOT NULL,
--
        CONSTRAINT cablchannelingitem_pk PRIMARY KEY(id),
--
	CONSTRAINT cablchannelingitem_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT cablchannelingitem_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE
);

COMMENT ON TABLE CableChannelingItem IS '$Id: cablechannelingitem.sql,v 1.4 2005/06/09 14:40:11 max Exp $';

CREATE SEQUENCE CableChannelingItem_Seq ORDER;
