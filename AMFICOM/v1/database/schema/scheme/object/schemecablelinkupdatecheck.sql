-- $Id: schemecablelinkupdatecheck.sql,v 1.1 2005/02/18 16:45:56 bass Exp $

CREATE TRIGGER "SchemeCableLinkUpdateCheck"
	BEFORE INSERT OR UPDATE OF cable_link_type_id, cable_link_id
	ON "SchemeCableLink"
	FOR EACH ROW
	WHEN ((old.cable_link_type_id != new.cable_link_type_id) OR (old.cable_link_id != new.cable_link_id))
BEGIN
	DBMS_OUTPUT.PUT_LINE('SchemeCableLinkUpdateCheck BEGIN');
	DBMS_OUTPUT.PUT_LINE('SchemeCableLinkUpdateCheck END');
END;
/
