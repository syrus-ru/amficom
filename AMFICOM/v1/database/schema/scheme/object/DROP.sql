-- $Id: DROP.sql,v 1.4 2005/02/10 08:47:55 bass Exp $

-- 09. SchemeCablePort
PROMPT 09. Dropping table "SchemeCablePort"...;
DROP TABLE "SchemeCablePort";
DROP SEQUENCE "SchemeCablePort_Seq";

-- 08. SchemePort
PROMPT 08. Dropping table "SchemePort"...;
DROP TABLE "SchemePort";
DROP SEQUENCE "SchemePort_Seq";

-- 07. SchemeDevice
PROMPT 07. Dropping table "SchemeDevice"...;
DROP TABLE "SchemeDevice";
DROP SEQUENCE "SchemeDevice_Seq";

-- 06. SchemeMonitoringSolution
PROMPT 06. Dropping table "SchemeMonitoringSolution"...;
ALTER TABLE "Scheme" DROP CONSTRAINT scheme_schm_monitoring_sltn_fk;
DROP TABLE "SchemeMonitoringSolution";
DROP SEQUENCE "SchemeMonitoringSolution_Seq";

-- 05. SchemeOptimizeInfo
PROMPT 05. Dropping table "SchemeOptimizeInfo"...;
DROP TABLE "SchemeOptimizeInfoSwitch";
DROP SEQUENCE "SchemeOptimizeInfoSwitch_Seq";
DROP TABLE "SchemeOptimizeInfoRtu";
DROP SEQUENCE "SchemeOptimizeInfoRtu_Seq";
DROP TABLE "SchemeOptimizeInfo";
DROP SEQUENCE "SchemeOptimizeInfo_Seq";

-- 04. SchemeElement
PROMPT 04. Dropping table "SchemeElement"...;
ALTER TABLE "Scheme" DROP CONSTRAINT scheme_prnt_scheme_element_fk;
DROP TABLE "SchemeElement";
DROP SEQUENCE "SchemeElement_Seq";

-- 03. Scheme
PROMPT 03. Dropping table "Scheme"...;
DROP TABLE "Scheme";
DROP SEQUENCE "Scheme_Seq";

-- 02. SchemeProtoElement
PROMPT 02. Dropping table "SchemeProtoElement"...;
DROP TABLE "SchemeProtoElement";
DROP SEQUENCE "SchemeProtoElement_Seq";

-- 01. SchemeProtoGroup
PROMPT 01. Dropping table "SchemeProtoGroup"...;
DROP TABLE "SchemeProtoGroup";
DROP SEQUENCE "SchemeProtoGroup_Seq";
