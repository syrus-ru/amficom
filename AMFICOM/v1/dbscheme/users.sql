--
-- $Id: users.sql,v 1.1 2004/10/14 08:31:54 bass Exp $
--
-- Dependencies:
--	mcm.
--

CREATE TABLE amficom.users (
	id VARCHAR2(32) NOT NULL,
	login VARCHAR2(32) NOT NULL,
	name VARCHAR2(64) NOT NULL,
-- Shouldn't these be foreign keys?
	operational_id VARCHAR2(64),
	operator_id VARCHAR2(64),
	organization_id VARCHAR2(64),
	subscriber_id VARCHAR2(64),
--
	type VARCHAR2(64) NOT NULL,
--
	CONSTRAINT usr_pk PRIMARY KEY (id),
	CONSTRAINT usr_login_uk UNIQUE (login),
	CONSTRAINT usr_name_uk UNIQUE (name)
);

GRANT REFERENCES (login) ON amficom.users TO mcm;
GRANT REFERENCES (name) ON amficom.users TO mcm;
