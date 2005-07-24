-- $Id: schemeoptimizeinfortu.sql,v 1.1 2005/07/24 15:42:19 bass Exp $

CREATE TABLE SchemeOptimizeInfoRtu (
	id NUMBER(19) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id NOT NULL,
	modifier_id NOT NULL,
	version NUMBER(19) NOT NULL,
--
	name VARCHAR2(128 CHAR) NOT NULL,
	price_usd NUMBER(10) NOT NULL,
	range_db BINARY_FLOAT NOT NULL,
	parent_scheme_optimize_info_id NOT NULL,
--
	CONSTRAINT schmoptmzinfrtu_pk PRIMARY KEY(id),
--
	CONSTRAINT schmoptmzinfrtu_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT schmoptmzinfrtu_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT schmoptmzinfrtu_uk UNIQUE(
		name,
		price_usd,
		range_db,
		parent_scheme_optimize_info_id
	),
--
	CONSTRAINT schmoptmzinfrtu_parent_fk FOREIGN KEY(parent_scheme_optimize_info_id)
		REFERENCES SchemeOptimizeInfo(id) ON DELETE CASCADE
);

COMMENT ON TABLE SchemeOptimizeInfoRtu IS '$Id: schemeoptimizeinfortu.sql,v 1.1 2005/07/24 15:42:19 bass Exp $';
COMMENT ON COLUMN SchemeOptimizeInfoRtu.price_usd IS 'RTU price in US dollars.';
COMMENT ON COLUMN SchemeOptimizeInfoRtu.range_db IS 'RTU range in decibels, from 0.00 to 128.00 db.';

CREATE SEQUENCE SchemeOptimizeInfoRtu_Seq ORDER;
