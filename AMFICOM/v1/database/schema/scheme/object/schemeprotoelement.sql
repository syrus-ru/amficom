-- $Id: schemeprotoelement.sql,v 1.10 2005/09/29 13:05:10 bass Exp $

CREATE TABLE SchemeProtoElement (
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
	label VARCHAR2(64 CHAR),
	proto_equipment_id,
	symbol_id,
	ugo_cell_id,
	scheme_cell_id,
	parent_scheme_proto_group_id,
	parent_scheme_proto_element_id,
--
	CONSTRAINT schemeprotoelement_pk PRIMARY KEY(id),
--
	CONSTRAINT schemeprotoelement_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotoelement_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT schemeprotoelement_pteqpmnt_fk FOREIGN KEY(proto_equipment_id)
		REFERENCES ProtoEquipment(id) ON DELETE SET NULL,
	CONSTRAINT schemeprotoelement_symbol_fk FOREIGN KEY(symbol_id)
		REFERENCES ImageResource(id) ON DELETE SET NULL,
	CONSTRAINT schemeprotoelement_schmcell_fk FOREIGN KEY(scheme_cell_id)
		REFERENCES ImageResource(id) ON DELETE SET NULL,
	CONSTRAINT schemeprotoelement_ugocell_fk FOREIGN KEY(ugo_cell_id)
		REFERENCES ImageResource(id) ON DELETE SET NULL,
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

COMMENT ON TABLE SchemeProtoElement IS '$Id: schemeprotoelement.sql,v 1.10 2005/09/29 13:05:10 bass Exp $';

CREATE SEQUENCE SchemeProtoElement_Seq ORDER;
