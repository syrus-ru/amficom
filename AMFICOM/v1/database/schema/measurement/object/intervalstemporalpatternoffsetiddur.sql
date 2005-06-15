-- $Id: intervalstemporalpatternoffsetiddur.sql,v 1.5 2005/06/15 09:40:35 bass Exp $

CREATE TABLE ITmpPattrnOffsetDur (
 inter_temp_patt_id NUMBER(19) NOT NULL,
-- 
 offset NUMBER(19) NOT NULL,
 temp_pattern_id NUMBER(19) NOT NULL,
 duration VARCHAR2(32 CHAR) NOT NULL,
--
 CONSTRAINT itp_id_fk FOREIGN KEY (inter_temp_patt_id)
  REFERENCES ITempPattern (id) ON DELETE CASCADE
);
