--
-- $Id: domains.sql,v 1.1 2004/10/14 08:31:53 bass Exp $
--
-- Dependencies:
--	amficom.imageresources;
--	amficom.users;
--	mcm.
--

CREATE TABLE amficom.domains (
	id VARCHAR2(64) NOT NULL,
	name VARCHAR2(64) NOT NULL,
--
	image_id VARCHAR2(64),
--
	created_by VARCHAR2(64) NOT NULL,
	modified_by VARCHAR2(64) NOT NULL,
	owner_id VARCHAR2(64) NOT NULL,
-- Shouldn't these be foreign keys?
	domain_id VARCHAR2(64),
	object_permission_id VARCHAR2(64),
--
	created DATE NOT NULL,
	modified DATE NOT NULL,
	codename VARCHAR2(128),
	description VARCHAR2(1024),
--
	CONSTRAINT domain_pk PRIMARY KEY (id),
	CONSTRAINT domain_name_uk UNIQUE (name),
--
	CONSTRAINT domain_img_fk FOREIGN KEY (image_id)
		REFERENCES amficom.imageresources (id),
--
	CONSTRAINT domain_creusr_fk FOREIGN KEY (created_by)
		REFERENCES amficom.users (id),
	CONSTRAINT domain_modusr_fk FOREIGN KEY (modified_by)
		REFERENCES amficom.users (id),
	CONSTRAINT domain_usr_fk FOREIGN KEY (owner_id)
		REFERENCES amficom.users (id)
);

GRANT REFERENCES (name) ON amficom.domains TO mcm;
