CREATE TABLE PermissionAttributes (
 id NUMBER(20, 0),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(20, 0) NOT NULL,
 modifier_id NUMBER(20, 0) NOT NULL,

 name VARCHAR2(64) NOT NULL,
 codename VARCHAR2(32) NOT NULL,
 rwx VARCHAR2(3) NOT NULL,
 deny_message VARCHAR2(512),

 CONSTRAINT pa_pk PRIMARY KEY (id),
 CONSTRAINT pa_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT pa_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE permissionattributes_seq ORDER;
