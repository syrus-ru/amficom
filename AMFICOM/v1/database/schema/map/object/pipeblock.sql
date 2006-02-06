-- $Id: pipeblock.sql,v 1.1 2005/10/16 11:07:30 max Exp $

CREATE TABLE PipeBlock (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 pipe_number NUMBER(10) NOT NULL,
 dimension_x NUMBER(10) NOT NULL,
 dimension_y NUMBER(10) NOT NULL,
 left_to_right NUMBER(1) NOT NULL,
 top_to_bottom NUMBER(1) NOT NULL,
 horizontal_vertical NUMBER(1) NOT NULL,
--
 CONSTRAINT pipeblock_pk PRIMARY KEY (id),
 CONSTRAINT pipeblock_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT pipeblock_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE
);

COMMENT ON TABLE PipeBlock IS '$Id: pipeblock.sql,v 1.1 2005/10/16 11:07:30 max Exp $';

CREATE SEQUENCE PipeBlock_Seq ORDER;