-- $Id: permittionattributes.sql,v 1.3 2005/10/20 11:14:06 arseniy Exp $

CREATE TABLE PermissionAttributes(
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 domain_id NOT NULL,
 parent_id NUMBER(19) NOT NULL,
 module_code VARCHAR2(64 CHAR) NOT NULL,
 permission_mask NUMBER(12),
 deny_mask NUMBER(12),
--
 CONSTRAINT permatt_pk PRIMARY KEY (id),
 CONSTRAINT permatt_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT permatt_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT permatt_domain_fk FOREIGN KEY (domain_id)
   REFERENCES Domain (id) ON DELETE CASCADE   
);

CREATE SEQUENCE PermissionAttributes_Seq ORDER;
