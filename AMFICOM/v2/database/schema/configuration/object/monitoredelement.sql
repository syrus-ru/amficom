CREATE TABLE MonitoredElement (
 id NUMBER(20, 0),
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id NUMBER(20, 0) NOT NULL,
 modifier_id NUMBER(20, 0) NOT NULL,
 domain_id NUMBER(20, 0),

 kis_id NUMBER(20) NOT NULL,
 local_address VARCHAR2(64) NOT NULL,

 CONSTRAINT me_pk PRIMARY KEY (id) ENABLE,
 CONSTRAINT me_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT me_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE ENABLE,
 CONSTRAINT me_domain_fk FOREIGN KEY (domain_id)
  REFERENCES Domain (id) ON DELETE CASCADE ENABLE,

 CONSTRAINT me_kis_fk FOREIGN KEY (kis_id)
  REFERENCES Kis (id) ON DELETE CASCADE ENABLE
);

CREATE SEQUENCE monitoredelement_seq ORDER;
