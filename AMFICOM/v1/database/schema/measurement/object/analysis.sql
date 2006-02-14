-- $Id: analysis.sql,v 1.13.2.1 2006/02/14 09:58:40 arseniy Exp $

CREATE TABLE Analysis (
 id NUMBER(19),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NOT NULL,
 modifier_id NOT NULL,
 version NUMBER(19) NOT NULL,
--
 type_id NOT NULL,
 monitored_element_id NOT NULL,
 action_template_id NOT NULL,
 name VARCHAR2(128 CHAR),
 start_time DATE NOT NULL,
 duration NUMBER(20) NOT NULL,
 status NUMBER(2, 0) NOT NULL,
 measurement_id,
--
 CONSTRAINT a_pk PRIMARY KEY (id),
 CONSTRAINT a_creator_fk FOREIGN KEY (creator_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
 CONSTRAINT a_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES SystemUser (id) ON DELETE CASCADE,
--
 CONSTRAINT a_at_fk FOREIGN KEY (type_id)
  REFERENCES AnalysisType (id) ON DELETE CASCADE,
 CONSTRAINT a_me_fk FOREIGN KEY (monitored_element_id)
  REFERENCES MonitoredElement (id) ON DELETE CASCADE,
 CONSTRAINT a_actmpl_fk FOREIGN KEY (action_template_id)
  REFERENCES ActionTemplate (id) ON DELETE CASCADE,
 CONSTRAINT a_m_fk FOREIGN KEY (measurement_id)
  REFERENCES Measurement (id) ON DELETE CASCADE
);

CREATE SEQUENCE Analysis_seq ORDER;
