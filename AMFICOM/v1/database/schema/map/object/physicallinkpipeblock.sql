-- $Id: physicallinkpipeblock.sql,v 1.1.2.1 2006/02/14 11:06:59 arseniy Exp $

CREATE TABLE PhysicalLinkPipeBlock (
 physical_link_id,
 pipe_block_id,
--
 CONSTRAINT plpb_physical_link_fk FOREIGN KEY (physical_link_id)
  REFERENCES PhysicalLink (id) ON DELETE CASCADE,
 CONSTRAINT plpb_pipe_block_fk FOREIGN KEY (pipe_block_id)
  REFERENCES PipeBlock (id) ON DELETE CASCADE
);
