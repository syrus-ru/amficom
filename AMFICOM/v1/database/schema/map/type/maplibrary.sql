CREATE TABLE MapLibrary (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 name VARCHAR2(64 CHAR),
 codename VARCHAR2(32 CHAR),
 description VARCHAR2(256 CHAR),
 parentmaplibrary_id,
--
 CONSTRAINT map_lib_pk PRIMARY KEY (id),
 CONSTRAINT map_lib_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT map_lib_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT par_map_lib_fk FOREIGN KEY (parentmaplibrary_id)
  REFERENCES MapLibrary (id) ON DELETE CASCADE
);
CREATE SEQUENCE MapLibrary_Seq;