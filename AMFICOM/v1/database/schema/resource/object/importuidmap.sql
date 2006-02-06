CREATE TABLE ImportUIDMap (
 import_kind VARCHAR2(32) NOT NULL,
 foreign_uid VARCHAR2(128) NOT NULL,
 id NUMBER(19) NOT NULL,
--
 CONSTRAINT ium_pk PRIMARY KEY (foreign_uid, import_kind),
 CONSTRAINT ium_uniq UNIQUE (id, import_kind)
);
