-- $Id: CREATE.sql,v 1.6 2005/02/21 08:30:18 bass Exp $

-- 01. SchemeProtoGroup
PROMPT 01. Creating table "SchemeProtoGroup"...;
@@schemeprotogroup;

-- 02. SchemeProtoElement
PROMPT 02. Creating table "SchemeProtoElement"...;
@@schemeprotoelement;

-- 03. Scheme
PROMPT 03. Creating table "Scheme"...;
@@scheme;

-- 04. SchemeElement
PROMPT 04. Creating table "SchemeElement"...;
@@schemeelement;

-- 05. SchemeOptimizeInfo
PROMPT 05. Creating table "SchemeOptimizeInfo"...;
@@schemeoptimizeinfo;

-- 06. SchemeMonitoringSolution
PROMPT 06. Creating table "SchemeMonitoringSolution"...;
@@schememonitoringsolution;

-- 07. SchemeDevice
PROMPT 07. Creating table "SchemeDevice"...;
@@schemedevice;

-- 08. SchemePort
PROMPT 08. Creating table "SchemePort"...;
@@schemeport;

-- 09. SchemeCablePort
PROMPT 09. Creating table "SchemeCablePort"...;
@@schemecableport;

-- 10. SchemeLink
PROMPT 10. Creating table "SchemeLink"...;
@@schemelink;
@@schemelinkupdatecheck;

-- 11. SchemeCableLink
PROMPT 11. Creating table "SchemeCableLink"...;
@@schemecablelink;
@@schemecablelinkupdatecheck;
@@linktypenatureupdatecheck;

-- 12. SchemeCableThread
PROMPT 12. Creating table "SchemeCableThread"...;
@@schemecablethread;

-- 13. CableChannelingItem
PROMPT 13. Creating table "CableChannelingItem"...;
@@cablechannelingitem;

-- 14. SchemePath
PROMPT 14. Creating table "SchemePath"...;
@@schemepath;

-- 15. PathElement
PROMPT 15. Creating table "PathElement"...;
@@pathelement;
