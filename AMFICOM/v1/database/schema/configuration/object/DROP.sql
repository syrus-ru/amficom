-- $Id: DROP.sql,v 1.17.2.1 2006/02/14 09:48:52 arseniy Exp $

PROMPT 06. Dropping table TransmissionPath...;
DROP TABLE TransmissionPath;
DROP SEQUENCE TransmissionPath_seq;

PROMPT 05. Dropping table Port...;
DROP TABLE Port;
DROP SEQUENCE Port_seq;

PROMPT 04. Dropping table Equipment...;
DROP TABLE Equipment;
DROP SEQUENCE Equipment_seq;

PROMPT 03. Dropping table ProtoEquipment...;
DROP TABLE ProtoEquipment;
DROP SEQUENCE ProtoEquipment_seq;

PROMPT 02. Dropping table CableLink...;
DROP TABLE CableLink;
DROP SEQUENCE CableLink_Seq;

PROMPT 01. Dropping table Link...;
DROP TABLE Link;
DROP SEQUENCE Link_Seq;
