CREATE TABLE Domain (
 id NUMBER(20, 0),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(20, 0) NOT NULL,
 modifier_id NUMBER(20, 0) NOT NULL,
 domain_id NUMBER(20, 0),
 name VARCHAR2(64) NOT NULL,
 description VARCHAR2(256),
 owner_id NUMBER(20, 0),
 permission_attributes_id NUMBER(20, 0) NOT NULL,
 CONSTRAINT domain_pk PRIMARY KEY (id),
 CONSTRAINT domain_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT domain_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT domain_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT domain_owner_fk FOREIGN KEY (owner_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT domain_pa_fk FOREIGN KEY (permission_attributes_id)
  REFERENCES PermissionAttributes (id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE domain_seq ORDER;
