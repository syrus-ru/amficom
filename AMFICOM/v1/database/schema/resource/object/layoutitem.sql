-- $Id: layoutitem.sql,v 1.1 2005/11/14 11:00:39 bob Exp $

CREATE TABLE LayoutItem (
 id NUMBER(19),
--
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 parent_id,
--
 layout_name VARCHAR2(128 CHAR) NOT NULL,
 name VARCHAR2(256 CHAR),
--
 CONSTRAINT layout_item_pk PRIMARY KEY (id),
 CONSTRAINT layout_item_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT layout_item_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT layout_item_layout_item_fk FOREIGN KEY (parent_id)
  REFERENCES LayoutItem (id) ON DELETE CASCADE
);

CREATE SEQUENCE LayoutItem_seq ORDER;

