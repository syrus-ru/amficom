CREATE TABLE ImportUIDMap (
 import_kind VARCHAR2(32) NOT NULL,
 foreign_uid VARCHAR2(128) NOT NULL,
 id NUMBER(19) NOT NULL,
--
 CONSTRAINT ium_pk PRIMARY KEY (import_kind, foreign_uid),
 CONSTRAINT ium_uniq UNIQUE (import_kind, foreign_uid, id)
);
