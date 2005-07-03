-- $Id: cablechannelingitem.sql,v 1.5 2005/06/15 11:28:47 bass Exp $

CREATE TABLE CableChannelingItem (
	id NUMBER(19) NOT NULL,
--
	created TIMESTAMP NOT NULL,
	modified TIMESTAMP NOT NULL,
	creator_id NOT NULL,
	modifier_id NOT NULL,
	version NUMBER(19) NOT NULL,
--
	start_spare NUMBER(19) NOT NULL,
	end_spare NUMBER(19) NOT NULL,
	row_x NUMBER(10) NOT NULL,
	place_y NUMBER(10) NOT NULL,
	sequential_number NUMBER(10) NOT NULL,
	physical_link_id,
	start_site_node_id NOT NULL,
	end_site_node_id NOT NULL,
	parent_scheme_cable_link_id NOT NULL,
--
        CONSTRAINT cblchnnlngtm_pk PRIMARY KEY(id),
--
	CONSTRAINT cblchnnlngtm_creator_fk FOREIGN KEY(creator_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
	CONSTRAINT cblchnnlngtm_modifier_fk FOREIGN KEY(modifier_id)
		REFERENCES SystemUser(id) ON DELETE CASCADE,
--
	CONSTRAINT cblchnnlngtm_physical_link_fk FOREIGN KEY(physical_link_id)
		REFERENCES PhysicalLink(id) ON DELETE CASCADE,
	CONSTRAINT cblchnnlngtm_strt_site_node_fk FOREIGN KEY(start_site_node_id)
		REFERENCES SiteNode(id) ON DELETE CASCADE,
	CONSTRAINT cblchnnlngtm_end_site_node_fk FOREIGN KEY(end_site_node_id)
		REFERENCES SiteNode(id) ON DELETE CASCADE,
	CONSTRAINT cblchnnlngtm_prntschmcbllnk_fk FOREIGN KEY(parent_scheme_cable_link_id)
		REFERENCES SchemeCableLink(id) ON DELETE CASCADE
);

COMMENT ON TABLE CableChannelingItem IS '$Id: cablechannelingitem.sql,v 1.5 2005/06/15 11:28:47 bass Exp $';

CREATE SEQUENCE CableChannelingItem_Seq ORDER;
