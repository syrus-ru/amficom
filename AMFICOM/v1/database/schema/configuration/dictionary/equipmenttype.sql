-- $Id: equipmenttype.sql,v 1.1 2005/09/29 09:04:00 arseniy Exp $

CREATE TABLE EquipmentType (
 code NUMBER(2, 0),
 codename VARCHAR2(32 CHAR) NOT NULL,
--
 CONSTRAINT eqptyp_pk PRIMARY KEY (code),
 CONSTRAINT eqptyp_uniq UNIQUE (codename)
);

