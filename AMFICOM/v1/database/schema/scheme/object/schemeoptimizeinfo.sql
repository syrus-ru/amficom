-- $Id: schemeoptimizeinfo.sql,v 1.8 2005/06/15 17:03:10 bass Exp $

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
	CONSTRAINT schmoptimizeinfo_pk PRIMARY KEY(id),
--
	CONSTRAINT schmoptimizeinfo_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT schmoptimizeinfo_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT schmoptimizeinfo_prnt_schm_fk FOREIGN KEY(parent_scheme_id)
		REFERENCES Scheme(id) ON DELETE CASCADE
);

COMMENT ON TABLE SchemeOptimizeInfo IS '$Id: schemeoptimizeinfo.sql,v 1.8 2005/06/15 17:03:10 bass Exp $';

CREATE SEQUENCE SchemeOptimizeInfo_Seq ORDER;


CREATE TABLE SchemeOptimizeInfoRtu (
	id NUMBER(19) NOT NULL,
--
	name VARCHAR2(128 CHAR) NOT NULL,
	price_usd NUMBER(10) NOT NULL,
	range_db NUMBER(5, 2) NOT NULL,
	scheme_optimize_info_id NOT NULL,
--
	CONSTRAINT schemeoptimizeinfortu_pk PRIMARY KEY(id),
--
	CONSTRAINT schemeoptimizeinfortu_uk UNIQUE(
		name,
		price_usd,
		range_db,
		scheme_optimize_info_id
	),
--
	CONSTRAINT schemeoptimizeinfortu_fk FOREIGN KEY(scheme_optimize_info_id)
		REFERENCES SchemeOptimizeInfo(id) ON DELETE CASCADE
);

COMMENT ON TABLE SchemeOptimizeInfoRtu IS '$Id: schemeoptimizeinfo.sql,v 1.8 2005/06/15 17:03:10 bass Exp $';
COMMENT ON COLUMN SchemeOptimizeInfoRtu.price_usd IS 'RTU price in US dollars.';
COMMENT ON COLUMN SchemeOptimizeInfoRtu.range_db IS 'RTU range in decibels, from 0.00 to 128.00 db.';

CREATE SEQUENCE SchemeOptimizeInfoRtu_Seq ORDER;


CREATE TABLE SchemeOptimizeInfoSwitch (
	id NUMBER(19) NOT NULL,
--
	name VARCHAR2(128 CHAR) NOT NULL,
	price_usd NUMBER(10) NOT NULL,
	no_of_ports NUMBER(3) NOT NULL,
	scheme_optimize_info_id NOT NULL,
--
	CONSTRAINT schemeoptimizeinfoswitch_pk PRIMARY KEY(id),
--
	CONSTRAINT schemeoptimizeinfoswitch_uk UNIQUE(
		name,
		price_usd,
		no_of_ports,
		scheme_optimize_info_id
	),
--
	CONSTRAINT schemeoptimizeinfoswitch_fk FOREIGN KEY(scheme_optimize_info_id)
		REFERENCES SchemeOptimizeInfo(id) ON DELETE CASCADE
);

COMMENT ON TABLE SchemeOptimizeInfoSwitch IS '$Id: schemeoptimizeinfo.sql,v 1.8 2005/06/15 17:03:10 bass Exp $';
COMMENT ON COLUMN SchemeOptimizeInfoSwitch.price_usd IS 'Optical switch price in US dollars.';
COMMENT ON COLUMN SchemeOptimizeInfoSwitch.no_of_ports IS 'Number of ports in this optical switch. Up to 256.';

CREATE SEQUENCE SchemeOptimizeInfoSwitch_Seq ORDER;
