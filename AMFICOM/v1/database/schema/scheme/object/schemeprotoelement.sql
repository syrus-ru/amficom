-- $Id: schemeprotoelement.sql,v 1.2 2005/02/08 14:08:58 bass Exp $

CREATE TABLE "SchemeProtoElement" (
	id VARCHAR2(32 CHAR) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id VARCHAR2(32 CHAR) NOT NULL,
	modifier_id VARCHAR2(32 CHAR) NOT NULL,
--
	name VARCHAR2(32 CHAR) NOT NULL,
	description VARCHAR2(256 CHAR),
--
	label VARCHAR2(64 CHAR),
	equipment_type_id VARCHAR2(32 CHAR),
	symbol_id VARCHAR2(32 CHAR),
	ugo_cell_id VARCHAR2(32 CHAR),
	scheme_cell_id VARCHAR2(32 CHAR),
	parent_scheme_proto_group_id VARCHAR2(32 CHAR),
	parent_scheme_proto_element_id VARCHAR2(32 CHAR),
--
	CONSTRAINT schemeprotoelement_pk PRIMARY KEY(id),
--
	CONSTRAINT schemeprotoelement_creator_fk FOREIGN KEY(creator_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotoelement_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
--
	CONSTRAINT schemeprotoelement_eqpmnttp_fk FOREIGN KEY(equipment_type_id)
		REFERENCES EquipmentType(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotoelement_symbol_fk FOREIGN KEY(symbol_id)
		REFERENCES "ImageResource"(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotoelement_schmcell_fk FOREIGN KEY(scheme_cell_id)
		REFERENCES "ImageResource"(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotoelement_ugocell_fk FOREIGN KEY(ugo_cell_id)
		REFERENCES "ImageResource"(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotoelement_prnt_spg_fk FOREIGN KEY(parent_scheme_proto_group_id)
		REFERENCES "SchemeProtoGroup"(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotoelement_prnt_spe_fk FOREIGN KEY(parent_scheme_proto_element_id)
		REFERENCES "SchemeProtoElement"(id) ON DELETE CASCADE,
	CONSTRAINT schemeprotoelement_prnt_chk CHECK
		((parent_scheme_proto_group_id iS NULL
		AND parent_scheme_proto_element_id iS NOT NULL)
		OR (parent_scheme_proto_group_id iS NOT NULL
		AND parent_scheme_proto_element_id iS NULL))
);

CREATE SEQUENCE "SchemeProtoElement_Seq" ORDER;
