-- $Id: schemecablethread.sql,v 1.3 2005/06/09 14:40:12 max Exp $

CREATE TABLE SchemeCableThread (
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
        CONSTRAINT schemecablethread_pk PRIMARY KEY(id),
--
	CONSTRAINT schemecablethread_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT schemecablethread_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE
);

COMMENT ON TABLE SchemeCableThread IS '$Id: schemecablethread.sql,v 1.3 2005/06/09 14:40:12 max Exp $';

CREATE SEQUENCE SchemeCableThread_Seq ORDER;
