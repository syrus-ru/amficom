-- $Id: DROP.sql,v 1.12 2006/03/23 16:12:24 bass Exp $

-- 17. PathElement
PROMPT 17. Dropping table PathElement...;
ALTER TABLE LineMismatchEvent DROP CONSTRAINT lm_event_affected_path_lmnt_fk;
ALTER TABLE ReflectogramMismatchEvent DROP CONSTRAINT rm_event_anchor1_fk;
ALTER TABLE ReflectogramMismatchEvent DROP CONSTRAINT rm_event_anchor2_fk;
DROP TABLE PathElement;
DROP SEQUENCE PathElement_Seq;

-- 16. SchemePath
PROMPT 16. Dropping table SchemePath...;
DROP TABLE SchemePath;
DROP SEQUENCE SchemePath_Seq;

-- 15. CableChannelingItem
PROMPT 15. Dropping table CableChannelingItem...;
DROP TABLE CableChannelingItem;
DROP SEQUENCE CableChannelingItem_Seq;

-- 14. SchemeCableThread
PROMPT 14. Dropping table SchemeCableThread...;
DROP TABLE SchemeCableThread;
DROP SEQUENCE SchemeCableThread_Seq;

-- 13. SchemeCableLink
PROMPT 13. Dropping table SchemeCableLink...;
DROP TABLE SchemeCableLink;
DROP SEQUENCE SchemeCableLink_Seq;

-- 12. SchemeLink
PROMPT 12. Dropping table SchemeLink...;
DROP TABLE SchemeLink;
DROP SEQUENCE SchemeLink_Seq;

-- 11. SchemeCablePort
PROMPT 11. Dropping table SchemeCablePort...;
DROP TABLE SchemeCablePort;
DROP SEQUENCE SchemeCablePort_Seq;

-- 10. SchemePort
PROMPT 10. Dropping table SchemePort...;
DROP TABLE SchemePort;
DROP SEQUENCE SchemePort_Seq;

-- 09. SchemeDevice
PROMPT 09. Dropping table SchemeDevice...;
DROP TABLE SchemeDevice;
DROP SEQUENCE SchemeDevice_Seq;

-- 08. SchemeMonitoringSolution
PROMPT 08. Dropping table SchemeMonitoringSolution...;
DROP TABLE SchemeMonitoringSolution;
DROP SEQUENCE SchemeMonitoringSolution_Seq;

-- 07. SchemeOptimizeInfoRtu
PROMPT 07. Dropping table SchemeOptimizeInfoRtu...;
DROP TABLE SchemeOptimizeInfoRtu;
DROP SEQUENCE SchemeOptimizeInfoRtu_Seq;

-- 06. SchemeOptimizeInfoSwitch
PROMPT 06. Dropping table SchemeOptimizeInfoSwitch...;
DROP TABLE SchemeOptimizeInfoSwitch;
DROP SEQUENCE SchemeOptimizeInfoSwitch_Seq;

-- 05. SchemeOptimizeInfo
PROMPT 05. Dropping table SchemeOptimizeInfo...;
DROP TABLE SchemeOptimizeInfo;
DROP SEQUENCE SchemeOptimizeInfo_Seq;

-- 04. SchemeElement
PROMPT 04. Dropping table SchemeElement...;
ALTER TABLE Scheme DROP CONSTRAINT scheme_prnt_scheme_element_fk;
DROP TABLE SchemeElement;
DROP SEQUENCE SchemeElement_Seq;

-- 03. Scheme
PROMPT 03. Dropping table Scheme...;
DROP TABLE Scheme;
DROP SEQUENCE Scheme_Seq;

-- 02. SchemeProtoElement
PROMPT 02. Dropping table SchemeProtoElement...;
DROP TABLE SchemeProtoElement;
DROP SEQUENCE SchemeProtoElement_Seq;

-- 01. SchemeProtoGroup
PROMPT 01. Dropping table SchemeProtoGroup...;
DROP TABLE SchemeProtoGroup;
DROP SEQUENCE SchemeProtoGroup_Seq;
