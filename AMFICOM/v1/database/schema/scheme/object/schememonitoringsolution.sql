-- $Id: schememonitoringsolution.sql,v 1.7 2005/06/15 11:38:53 bass Exp $

CREATE TABLE SchemeMonitoringSolution (
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
	price_usd NUMBER(10) NOT NULL,
	active NUMBER(1) NOT NULL,
	scheme_optimize_info_id,
--
	CONSTRAINT schememonitoringsolution_pk PRIMARY KEY(id),
--
	CONSTRAINT schmmonitoringsltn_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT schmmonitoringsltn_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT schmmonitoringsltn_schmptnf_fk FOREIGN KEY(scheme_optimize_info_id)
		REFERENCES SchemeOptimizeInfo(id) ON DELETE CASCADE,
	CONSTRAINT schmmonitoringsltn_active_chk CHECK
		(active = 0
		OR active = 1)
);

COMMENT ON TABLE SchemeMonitoringSolution IS '$Id: schememonitoringsolution.sql,v 1.7 2005/06/15 11:38:53 bass Exp $';
COMMENT ON COLUMN SchemeMonitoringSolution.price_usd IS 'Cost of this solution in US dollars.';

CREATE SEQUENCE SchemeMonitoringSolution_Seq ORDER;
