-- $Id: imageresource.sql,v 1.13 2005/07/29 11:54:56 max Exp $

CREATE TABLE ImageResource (
	id NUMBER(19) NOT NULL,
--
	created DATE NOT NULL,
	modified DATE NOT NULL,
	creator_id NOT NULL,
	modifier_id NOT NULL,
	version NUMBER(19) NOT NULL,
--
	codename VARCHAR2(32 CHAR),
	filename VARCHAR2(256 CHAR),
	image BLOB,
	sort NUMBER(1) NOT NULL,
--
	CONSTRAINT imgres_pk PRIMARY KEY(id),
	CONSTRAINT imgres_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT imgres_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT imgres_uniq UNIQUE(codename)
);

COMMENT ON TABLE ImageResource IS '$Id: imageresource.sql,v 1.13 2005/07/29 11:54:56 max Exp $';

CREATE SEQUENCE ImageResource_Seq ORDER;
