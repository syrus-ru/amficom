-- $Id: pathelement.sql,v 1.6 2005/06/09 14:40:11 max Exp $

CREATE TABLE PathElement (
	id NUMBER(19) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id NUMBER(19) NOT NULL,
	modifier_id NUMBER(19) NOT NULL,
	version NUMBER(19) NOT NULL,
--
	sequential_number NUMBER(10) NOT NULL,
	kind NUMBER(1) NOT NULL,
	start_abstract_scheme_port_id NUMBER(19),
        end_abstract_scheme_port_id NUMBER(19),
	scheme_cable_thread_id NUMBER(19),
	scheme_link_id NUMBER(19),
	parent_scheme_path_id NUMBER(19),
--
	CONSTRAINT pathelement_pk PRIMARY KEY(id),
--
	CONSTRAINT pathelement_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT pathelement_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT pathelement_schm_cbl_thread_fk FOREIGN KEY(scheme_cable_thread_id)
		REFERENCES SchemeCableThread(id) ON DELETE CASCADE,
	CONSTRAINT pathelement_scheme_link_fk FOREIGN KEY(scheme_link_id)
		REFERENCES SchemeLink(id) ON DELETE CASCADE,
	CONSTRAINT pathelement_prnt_schm_path_fk FOREIGN KEY(parent_scheme_path_id)
		REFERENCES SchemePath(id) ON DELETE CASCADE,
--
	-- TODO: check for kind value, too.
	CONSTRAINT pathelement_kind_chk CHECK
		((start_abstract_scheme_port_id IS NOT NULL 
		AND end_abstract_scheme_port_id IS NOT NULL
		AND scheme_cable_thread_id IS NULL 
		AND scheme_link_id IS NULL)
		OR (start_abstract_scheme_port_id IS NULL
		AND end_abstract_scheme_port_id IS NULL
		AND scheme_cable_thread_id IS NOT NULL
		AND scheme_link_id IS NULL)
		OR (start_abstract_scheme_port_id IS NULL
		AND end_abstract_scheme_port_id IS NULL
		AND scheme_cable_thread_id IS NULL
		AND scheme_link_id IS NOT NULL))
);

COMMENT ON TABLE PathElement IS '$Id: pathelement.sql,v 1.6 2005/06/09 14:40:11 max Exp $';

CREATE SEQUENCE PathElement_Seq ORDER;
