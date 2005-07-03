-- $Id: schemecablethread.sql,v 1.4 2005/06/15 11:28:47 bass Exp $

CREATE TABLE SchemeCableThread (
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
	cable_thread_type_id NOT NULL,
	link_id,
--
	source_scheme_port_id,
	target_scheme_port_id,
	parent_scheme_cable_link_id NOT NULL,
--
        CONSTRAINT schmcblthrd_pk PRIMARY KEY(id),
--
	CONSTRAINT schmcblthrd_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT schmcblthrd_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT schmcblthrd_cbl_thrd_tp_fk FOREIGN KEY(cable_thread_type_id)
		REFERENCES CableThreadType(id) ON DELETE CASCADE,
	CONSTRAINT schmcblthrd_lnk_fk FOREIGN KEY(link_id)
		REFERENCES Link(id) ON DELETE CASCADE,
--
	CONSTRAINT schmcblthrd_src_schm_prt_fk FOREIGN KEY(source_scheme_port_id)
		REFERENCES SchemePort(id) ON DELETE CASCADE,
	CONSTRAINT schmcblthrd_trgt_schm_prt_fk FOREIGN KEY(target_scheme_port_id)
		REFERENCES SchemePort(id) ON DELETE CASCADE,
	CONSTRAINT schmcblthrd_prntschmcbllnk_fk FOREIGN KEY(parent_scheme_cable_link_id)
		REFERENCES SchemeCableLink(id) ON DELETE CASCADE
);

COMMENT ON TABLE SchemeCableThread IS '$Id: schemecablethread.sql,v 1.4 2005/06/15 11:28:47 bass Exp $';

CREATE SEQUENCE SchemeCableThread_Seq ORDER;
