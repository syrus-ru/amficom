-- $Id: schemelinkupdatecheck.sql,v 1.1 2005/02/18 16:45:56 bass Exp $

CREATE TRIGGER "SchemeLinkUpdateCheck"
	BEFORE INSERT OR UPDATE OF link_type_id, link_id
	ON "SchemeLink"
	FOR EACH ROW
	WHEN ((old.link_type_id != new.link_type_id) OR (old.link_id != new.link_id))
BEGIN
	DBMS_OUTPUT.PUT_LINE('SchemeLinkUpdateCheck BEGIN');
	DBMS_OUTPUT.PUT_LINE('SchemeLinkUpdateCheck END');
END;
/
