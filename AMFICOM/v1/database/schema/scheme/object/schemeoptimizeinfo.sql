-- $Id: schemeoptimizeinfo.sql,v 1.1 2005/02/08 14:08:58 bass Exp $

CREATE TABLE "SchemeOptimizeInfo" (
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
	optimization_mode NUMBER(10) NOT NULL,
	iterations NUMBER(10) NOT NULL,
	price NUMBER NOT NULL,
	wave_length NUMBER NOT NULL,
	len_margin NUMBER NOT NULL,
	mutation_rate NUMBER NOT NULL,
	mutation_degree NUMBER NOT NULL,
	rtu_delete_prob NUMBER NOT NULL,
	rtu_create_prob NUMBER NOT NULL,
	nodes_splice_prob NUMBER NOT NULL,
	nodes_cut_prob NUMBER NOT NULL,
	survivor_rate NUMBER NOT NULL,
	scheme_id VARCHAR2(32 CHAR) NOT NULL,
--
	CONSTRAINT schemeoptimizeinfo_pk PRIMARY KEY(id),
--
	CONSTRAINT schemeoptimizeinfo_creator_fk FOREIGN KEY(creator_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
	CONSTRAINT schemeoptimizeinfo_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES "User"(id) ON DELETE CASCADE,
--
	CONSTRAINT schemeoptimizeinfo_scheme_fk FOREIGN KEY(scheme_id)
		REFERENCES "Scheme"(id) ON DELETE CASCADE
);

CREATE SEQUENCE "SchemeOptimizeInfo_Seq" ORDER;


CREATE TABLE "SchemeOptimizeInfoRtu" (
	id VARCHAR2(32 CHAR) NOT NULL,
--
	name VARCHAR2(128 CHAR) NOT NULL,
	price_usd NUMBER(10) NOT NULL,
	range_db NUMBER(5, 2) NOT NULL,
	scheme_optimize_info_id VARCHAR2(32 CHAR) NOT NULL,
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
		REFERENCES "SchemeOptimizeInfo"(id) ON DELETE CASCADE
);

COMMENT ON COLUMN "SchemeOptimizeInfoRtu".price_usd IS 'RTU price in US dollars.';
COMMENT ON COLUMN "SchemeOptimizeInfoRtu".range_db IS 'RTU range in decibels, from 0.00 to 128.00 db.';

CREATE SEQUENCE "SchemeOptimizeInfoRtu_Seq" ORDER;


CREATE TABLE "SchemeOptimizeInfoSwitch" (
	id VARCHAR2(32 CHAR) NOT NULL,
--
	name VARCHAR2(128 CHAR) NOT NULL,
	price_usd NUMBER(10) NOT NULL,
	no_of_ports NUMBER(3) NOT NULL,
	scheme_optimize_info_id VARCHAR2(32 CHAR) NOT NULL,
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
		REFERENCES "SchemeOptimizeInfo"(id) ON DELETE CASCADE
);

COMMENT ON COLUMN "SchemeOptimizeInfoSwitch".price_usd IS 'Optical switch price in US dollars.';
COMMENT ON COLUMN "SchemeOptimizeInfoSwitch".no_of_ports IS 'Number of ports in this optical switch. Up to 256.';

CREATE SEQUENCE "SchemeOptimizeInfoSwitch_Seq" ORDER;
