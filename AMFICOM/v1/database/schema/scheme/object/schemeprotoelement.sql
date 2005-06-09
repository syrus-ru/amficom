-- $Id: schemeprotoelement.sql,v 1.7 2005/06/09 14:40:12 max Exp $

CREATE TABLE SchemeProtoElement (
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
	label VARCHAR2(64 CHAR),
	equipment_type_id NUMBER(19),
	symbol_id NUMBER(19),
	ugo_cell_id NUMBER(19),
	scheme_cell_id NUMBER(19),
	parent_scheme_proto_group_id NUMBER(19),
	parent_scheme_proto_element_id NUMBER(19),
--
	CONSTRAINT schemeprotoelement_pk PRIMARY KEY(id),
--
	CONSTRAINT schemeprotoelement_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotoelement_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT schemeprotoelement_eqpmnttp_fk FOREIGN KEY(equipment_type_id)
		REFERENCES EquipmentType(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotoelement_symbol_fk FOREIGN KEY(symbol_id)
		REFERENCES ImageResource(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotoelement_schmcell_fk FOREIGN KEY(scheme_cell_id)
		REFERENCES ImageResource(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotoelement_ugocell_fk FOREIGN KEY(ugo_cell_id)
		REFERENCES ImageResource(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotoelement_prnt_spg_fk FOREIGN KEY(parent_scheme_proto_group_id)
		REFERENCES SchemeProtoGroup(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotoelement_prnt_spe_fk FOREIGN KEY(parent_scheme_proto_element_id)
		REFERENCES SchemeProtoElement(id) ON DELETE CASCADE,
--
	-- Boolean XOR: only one of parent_scheme_proto_group_id and
	-- parent_scheme_proto_element_id may be defined, and only one may be
	-- null.
	CONSTRAINT schemeprotoelement_prnt_chk CHECK
		((parent_scheme_proto_group_id iS NULL
		AND parent_scheme_proto_element_id iS NOT NULL)
		OR (parent_scheme_proto_group_id iS NOT NULL
		AND parent_scheme_proto_element_id iS NULL))
);

COMMENT ON TABLE SchemeProtoElement IS '$Id: schemeprotoelement.sql,v 1.7 2005/06/09 14:40:12 max Exp $';

CREATE SEQUENCE SchemeProtoElement_Seq ORDER;
