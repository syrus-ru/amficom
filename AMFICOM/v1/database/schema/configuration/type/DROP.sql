-- $Id: DROP.sql,v 1.12.2.1 2006/02/14 09:48:52 arseniy Exp $

PROMPT 06. Dropping table TransmissionPathType...;
DROP TABLE TransmissionPathType;
DROP SEQUENCE TransmissionPathType_seq;

PROMPT 05. Dropping table CableThreadType...;
DROP TABLE CableThreadType;
DROP SEQUENCE CableThreadType_seq;

PROMPT 04. Dropping table CableLinkType...;
DROP TABLE CableLinkType;
DROP SEQUENCE CableLinkType_seq;

PROMPT 03. Dropping table LinkType...;
DROP TABLE LinkType;
DROP SEQUENCE LinkType_seq;

PROMPT 02. Dropping table PortType...;
DROP TABLE PortType;
DROP SEQUENCE PortType_seq;

PROMPT 01. Dropping table EquipmentType...;
DROP TABLE EquipmentType;
DROP SEQUENCE EquipmentType_seq;
