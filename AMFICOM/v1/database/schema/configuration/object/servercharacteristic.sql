CREATE TABLE ServerCharacteristic (
 server_id Identifier NOT NULL,
 characteristic_type_id Identifier NOT NULL,
 value VARCHAR2(256),
--
 CONSTRAINT serverchc_server_fk (server_id)
  REFERENCES Server(id) ON DELETE CASCADE,
 CONSTRAINT serverchc_chctype_fk (characteristic_type_id)
  REFERENCES CharacteristicType(id) ON DELETE CASCADE
);
