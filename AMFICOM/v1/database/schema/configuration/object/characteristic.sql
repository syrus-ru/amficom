CREATE TABLE Characteristic (
 id Identifier,
 created DATE NOT NULL,
 modified DATE NOT NULL,
 creator_id Identifier NOT NULL,
 modifier_id Identifier NOT NULL,
--
 type_id Identifier NOT NULL,
--
 name VARCHAR2(64) NOT NULL,
 description VARCHAR2(256),
 value VARCHAR2(256),
--
 sort NUMBER(2) NOT NULL,
 server_id Identifier,
 mcm_id Identifier,
 equipment_id Identifier,
 transmission_path_id Identifier,
--
 CONSTRAINT chc_pk (id) PRIMARY_KEY,
CONSTRAINT chc_creator_fk FOREIGN KEY (creator_id)
  REFERENCES Users (id) ON DELETE CASCADE,
 CONSTRAINT chc_modifier_fk FOREIGN KEY (modifier_id)
  REFERENCES Users (id) ON DELETE CASCADE,
--
CONSTRAINT chc_chctype_fk (type_id)
  REFERENCES CharacteristicType(id) ON DELETE CASCADE
--
 CONSTRAINT chc_server_fk FOREGN KEY (server_id)
  REFERENCES Server(id) ON DELETE CASCADE,
 CONSTRAINT chc_mcm_fk FOREGN KEY (mcm_id)
  REFERENCES MCM(id) ON DELETE CASCADE,
 CONSTRAINT chc_eqp_fk FOREGN KEY (equipment_id)
  REFERENCES Equipment(id) ON DELETE CASCADE,
 CONSTRAINT chc_tp_fk FOREGN KEY (transmission_path_id)
  REFERENCES TransmissionPath(id) ON DELETE CASCADE
);

CREATE SEQUENCE characteristic_seq ORDER;

