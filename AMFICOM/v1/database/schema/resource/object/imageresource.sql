-- $Id: imageresource.sql,v 1.10 2005/06/15 07:50:19 bass Exp $

CREATE TABLE ImageResource (
	id NUMBER(19) NOT NULL,
--
	created DATE NOT NULL,
	modified DATE NOT NULL,
	creator_id NUMBER(19) NOT NULL,
	modifier_id NUMBER(19) NOT NULL,
	version NUMBER(19) NOT NULL,
--
	codename VARCHAR2(256),
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

COMMENT ON TABLE ImageResource IS '$Id: imageresource.sql,v 1.10 2005/06/15 07:50:19 bass Exp $';

CREATE SEQUENCE ImageResource_Seq ORDER;
