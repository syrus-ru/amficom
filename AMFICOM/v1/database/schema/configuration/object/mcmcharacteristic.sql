CREATE TABLE MCMCharacteristic (
 mcm_id Identifier NOT NULL,
 characteristic_type_id Identifier NOT NULL,
 value VARCHAR2(256),
--
 CONSTRAINT mcmchc_mcm_fk (mcm_id)
  REFERENCES MCM(id) ON DELETE CASCADE,
 CONSTRAINT mcmchc_chctype_fk (characteristic_type_id)
  REFERENCES CharacteristicType(id) ON DELETE CASCADE
);
