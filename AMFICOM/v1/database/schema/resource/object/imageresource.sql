CREATE TABLE imageresource (
 id VARCHAR2(32);
--
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id VARCHAR2 NOT NULL,
 modifier_id VARCHAR2 NOT NULL,
--
 codename VARCHAR2(256),
 image BLOB,
 sort NUMBER(1) NOT NULL,
--
 CONSTRAINT img_pk PRIMARY KEY (id),
 CONSTRAINT img_crt_id_fk FOREIGN KEY (creator_id)
  REFERENCES users (id) ON DELETE CASCADE,
 CONSTRAINT img_mod_id_fk FOREIGN KEY (modifier_id)
  REFERENCES USERS (id) ON DELETE CASCADE,
 CONSTRAINT img_cn_uk UNIQUE (CODENAME)
);
 
