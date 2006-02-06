-- $Id: schemepath.sql,v 1.7 2005/09/25 17:52:41 bass Exp $

CREATE TABLE SchemePath (
	id NUMBER(19) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id NOT NULL,
	modifier_id NOT NULL,
	version NUMBER(19) NOT NULL,
--
	name VARCHAR2(32 CHAR) NOT NULL,
	description VARCHAR2(256 CHAR),
--
	transmission_path_id,
	prnt_schm_mntrng_sltn_id NOT NULL,
--
        CONSTRAINT schemepath_pk PRIMARY KEY(id),
--
	CONSTRAINT schemepath_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT schemepath_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT schmpth_transmission_path_fk FOREIGN KEY(transmission_path_id)
		REFERENCES TransmissionPath(id) ON DELETE SET NULL,
	CONSTRAINT schmpth_prnt_schm_mntng_sln_fk FOREIGN KEY(prnt_schm_mntrng_sltn_id)
		REFERENCES SchemeMonitoringSolution(id) ON DELETE CASCADE
);

COMMENT ON TABLE SchemePath IS '$Id: schemepath.sql,v 1.7 2005/09/25 17:52:41 bass Exp $';

CREATE SEQUENCE SchemePath_Seq ORDER;
