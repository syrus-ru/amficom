-- $Id: schemeoptimizeinfo.sql,v 1.9 2005/07/24 15:42:19 bass Exp $

CREATE TABLE SchemeOptimizeInfo (
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
	optimization_mode NUMBER(10) NOT NULL,
	iterations NUMBER(10) NOT NULL,
	price BINARY_DOUBLE NOT NULL,
	wave_length BINARY_DOUBLE NOT NULL,
	len_margin BINARY_DOUBLE NOT NULL,
	mutation_rate BINARY_DOUBLE NOT NULL,
	mutation_degree BINARY_DOUBLE NOT NULL,
	rtu_delete_prob BINARY_DOUBLE NOT NULL,
	rtu_create_prob BINARY_DOUBLE NOT NULL,
	nodes_splice_prob BINARY_DOUBLE NOT NULL,
	nodes_cut_prob BINARY_DOUBLE NOT NULL,
	survivor_rate BINARY_DOUBLE NOT NULL,
	parent_scheme_id NOT NULL,
--
	CONSTRAINT schmoptmzinf_pk PRIMARY KEY(id),
--
	CONSTRAINT schmoptmzinf_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT schmoptmzinf_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT schmoptmzinf_prnt_schm_fk FOREIGN KEY(parent_scheme_id)
		REFERENCES Scheme(id) ON DELETE CASCADE
);

COMMENT ON TABLE SchemeOptimizeInfo IS '$Id: schemeoptimizeinfo.sql,v 1.9 2005/07/24 15:42:19 bass Exp $';

CREATE SEQUENCE SchemeOptimizeInfo_Seq ORDER;
