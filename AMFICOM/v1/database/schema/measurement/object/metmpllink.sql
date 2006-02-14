-- $Id: metmpllink.sql,v 1.1.2.1 2006/02/14 09:56:32 arseniy Exp $

CREATE TABLE METmplLink (
 monitored_element_id NOT NULL,
 action_template_id NOT NULL,
--
 CONSTRAINT metmpl_uniq UNIQUE (monitored_element_id, action_template_id),
 CONSTRAINT metmpl_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement(id) ON DELETE CASCADE,
 CONSTRAINT metmpl_actmpl_fk FOREIGN KEY (action_template_id)
  REFERENCES ActionTemplate (id) ON DELETE CASCADE
);
