CREATE TABLE EquipmentCharacteristic (
 equipment_id Identifier NOT NULL,
 characteristic_type_id Identifier NOT NULL,
 value VARCHAR2(256),
--
 CONSTRAINT eqpchc_eqp_fk (eqp_id)
  REFERENCES Equipment(id) ON DELETE CASCADE,
 CONSTRAINT eqpchc_chctype_fk (characteristic_type_id)
  REFERENCES CharacteristicType(id) ON DELETE CASCADE
);
