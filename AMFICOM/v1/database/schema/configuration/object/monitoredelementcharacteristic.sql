CREATE TABLE MonitoredElementCharacteristic (
 monitored_element_id Identifier NOT NULL,
 characteristic_type_id Identifier NOT NULL,
 value VARCHAR2(256),
--
 CONSTRAINT mechc_me_fk (me_id)
  REFERENCES MonitoredElement(id) ON DELETE CASCADE,
 CONSTRAINT mechc_chctype_fk (characteristic_type_id)
  REFERENCES CharacteristicType(id) ON DELETE CASCADE
);
