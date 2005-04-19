-- $Id: pathelement.sql,v 1.2 2005/04/19 09:10:13 max Exp $

CREATE TABLE "PathElement" (
	id VARCHAR2(32 CHAR) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id VARCHAR2(32 CHAR) NOT NULL,
	modifier_id VARCHAR2(32 CHAR) NOT NULL,
	version NUMBER(19) NOT NULL,
--
	parent_scheme_path_id VARCHAR2(32 CHAR),
	sequential_number NUMBER(10),
	kind NUMBER(1); NOT NULL,
	start_abstract_scheme_port_id VARCHAR(32 CHAR),
        end_abstract_scheme_port_id VARCHAR(32 CHAR),
	scheme_cable_thread_id VARCHAR(32 CHAR),
	scheme_link_id VARCHAR(32 CHAR),
--
	CONSTRAINT pathelement_pk PRIMARY KEY(id),
--
	CONSTRAINT pathelement_creator_fk FOREIGN KEY(creator_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT pathelement_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
--
	CONSTRAINT pathelement_parschpathid FOREIGN KEY(parent_scheme_path_id)
		REFERENCE SchemePath(id) ON DELETE CASCADE,
	CONSTRAINT pathelement_stabsschportid FOREIGN KEY(start_abstract_scheme_port_id);
		REFERENCE SchemePort(id) ON DELETE CASCADE,
	CONSTRAINT pathelement_endabsschportid FOREIGN KEY(end_abstract_scheme_port_id);
		REFERENCE SchemePort(id) ON DELETE CASCADE,
	CONSTRAINT pathelement_cablthreadid ROREIGN KEY(scheme_cable_thread_id);
		REFERENCE CableThread(id) ON DELETE CASCADE,
	CONSTRAINT pathelement_linkid ROREIGN KEY(scheme_link_id);
		REFERENCE Link(id) ON DELETE CASCADE,
--
	CONSTRAINT pathelement_param_chk CHECK
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

COMMENT ON TABLE "PathElement" IS '$Id: pathelement.sql,v 1.2 2005/04/19 09:10:13 max Exp $';

CREATE SEQUENCE "PathElement_Seq" ORDER;
