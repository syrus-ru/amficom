-- $Id: actmplmelink.sql,v 1.1.2.1 2006/02/15 19:41:51 arseniy Exp $

CREATE TABLE AcTmplMELink (
 action_template_id NOT NULL,
 monitored_element_id NOT NULL,
--
 CONSTRAINT aptmplme_uniq UNIQUE (action_template_id, monitored_element_id),
 CONSTRAINT aptmplme_actmpl_fk FOREIGN KEY (action_template_id)
  REFERENCES ActionTemplate (id) ON DELETE CASCADE,
 CONSTRAINT aptmplme_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement(id) ON DELETE CASCADE
);
