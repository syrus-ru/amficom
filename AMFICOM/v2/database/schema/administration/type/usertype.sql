CREATE TABLE Users(
 id NUMBER(20, 0),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(20, 0) NOT NULL,
 modifier_id NUMBER(20, 0) NOT NULL,

 codename VARCHAR2(32) NOT NULL,
 description VARCHAR2(256),

 CONSTRAINT utype_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT utype_uniq UNIQUE (codename) ENABLE,
 CONSTRAINT utype_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT utype_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE users_seq ORDER;
