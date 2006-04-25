-- $Id: DROP.sql,v 1.10 2006/04/25 07:50:07 arseniy Exp $

PROMPT 19. Dropping table MapCollector...;
DROP TABLE MapCollector;

PROMPT 18. Dropping table MapMark...;
DROP TABLE MapMark;

PROMPT 17. Dropping table MapExtNodeLink...;
DROP TABLE MapExtNodeLink;

PROMPT 16. Dropping table MapMapLibraryLink...;
DROP TABLE MapMapLibraryLink;

PROMPT 15. Dropping table MapMapLink...;
DROP TABLE MapMapLink;

PROMPT 14. Dropping table MapPhysicalLink...;
DROP TABLE MapPhysicalLink;

PROMPT 13. Dropping table MapNodeLink...;
DROP TABLE MapNodeLink;

PROMPT 12. Dropping table MapTopologicalNode...;
DROP TABLE MapTopologicalNode;

PROMPT 11. Dropping table MapSiteNode...;
DROP TABLE MapSiteNode;

PROMPT 10. Dropping table Map...;
DROP TABLE Map;
DROP SEQUENCE Map_seq;

PROMPT 09. Dropping table CollPhLink...;
DROP TABLE CollPhLink;

PROMPT 08. Dropping table Collector...;
DROP TABLE Collector;
DROP SEQUENCE Collector_seq;

PROMPT 07. Dropping table Mark...;
DROP TABLE Mark;
DROP SEQUENCE Mark_seq;

PROMPT 06. Dropping table NodeLink...;
DROP TABLE NodeLink;
DROP SEQUENCE NodeLink_seq;

PROMPT 05. Dropping table PhysicalLinkPipeBlock...;
DROP TABLE PhysicalLinkPipeBlock;

PROMPT 04. Dropping table PhysicalLink...;
DROP TABLE PhysicalLink;
DROP SEQUENCE PhysicalLink_seq;

PROMPT 03. Dropping table PipeBlock...;
DROP TABLE PipeBlock;
DROP SEQUENCE PipeBlock_seq;

PROMPT 02. Dropping table TopologicalNode...;
DROP TABLE TopologicalNode;
DROP SEQUENCE TopologicalNode_seq;

PROMPT 01. Dropping table SiteNode...;
DROP TABLE SiteNode;
DROP SEQUENCE SiteNode_seq;
