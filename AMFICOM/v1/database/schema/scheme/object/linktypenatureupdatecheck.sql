-- $Id: linktypenatureupdatecheck.sql,v 1.1 2005/02/18 11:12:27 bass Exp $

CREATE TRIGGER "LinkTypeNatureUpdateCheck"
	BEFORE UPDATE OF id, nature
	ON LinkType
	FOR EACH ROW
	WHEN ((old.id != new.id) OR (old.nature != new.nature))
DECLARE
	nature NUMBER(1) NOT NULL := 0;
	cnt NUMBER NOT NULL := 0;
	errMsg0 CONSTANT VARCHAR2(1 CHAR) NOT NULL := '.';
	errMsg1 CONSTANT VARCHAR2(5 CHAR) NOT NULL := 'Cable';
	errMsg2 CONSTANT VARCHAR2(8 CHAR) NOT NULL := 'LinkType';
	errMsg3 CONSTANT VARCHAR2(9 CHAR) NOT NULL := ' Link(s) ';
	errMsg4 CONSTANT VARCHAR2(11 CHAR) NOT NULL := ', nature = ';
	errMsg5 CONSTANT VARCHAR2(14 CHAR) NOT NULL := 'LinkType(id = ';
	errMsg6 CONSTANT VARCHAR2(14 CHAR) NOT NULL := ' CableLink(s) ';
	errMsg7 CONSTANT VARCHAR2(15 CHAR) NOT NULL := '): there''s(re) ';
	errMsg8 CONSTANT VARCHAR2(15 CHAR) NOT NULL := ' SchemeLink(s) ';
	errMsg9 CONSTANT VARCHAR2(17 CHAR) NOT NULL := 'referencing this ';
	errMsgA CONSTANT VARCHAR2(19 CHAR) NOT NULL := '): unknown nature: ';
	errMsgB CONSTANT VARCHAR2(20 CHAR) NOT NULL := ' SchemeCableLink(s) ';
	errMsgC CONSTANT VARCHAR2(29 CHAR) NOT NULL := 'LinkTypeNatureUpdateCheck END';
	errMsgD CONSTANT VARCHAR2(31 CHAR) NOT NULL := 'LinkTypeNatureUpdateCheck BEGIN';
	errMsgE CONSTANT VARCHAR2(43 CHAR) NOT NULL := '; should be 0. Fix the above problems first';
	errMsgF CONSTANT VARCHAR2(44 CHAR) NOT NULL := '; can''t change its id or convert it into a ';
	errMsgG CONSTANT VARCHAR2(42 CHAR) NOT NULL := 'LinkTypeNatureUpdateCheck sanity check END';
	errMsgH CONSTANT VARCHAR2(44 CHAR) NOT NULL := 'LinkTypeNatureUpdateCheck sanity check BEGIN';
BEGIN
	DBMS_OUTPUT.PUT_LINE(errMsgD);
	IF (:old.nature = 0) THEN
		DBMS_OUTPUT.PUT_LINE(errMsgH);
		SELECT COUNT(*) INTO cnt FROM Link WHERE type_id = :old.id AND nature = 1;
		IF (cnt >= 1) THEN
			RAISE_APPLICATION_ERROR(num => -20000, msg => errMsg5
				|| :old.id || errMsg4 || :old.nature || errMsg7
				|| cnt || errMsg6 || errMsg9 || errMsg2
				|| errMsgE || errMsg0);
		END IF;
		SELECT COUNT(*) INTO cnt FROM "SchemeCableLink" WHERE cable_link_type_id = :old.id;
		IF (cnt >= 1) THEN
			RAISE_APPLICATION_ERROR(num => -20000, msg => errMsg5
				|| :old.id || errMsg4 || :old.nature || errMsg7
				|| cnt || errMsgB || errMsg9 || errMsg2
				|| errMsgE || errMsg0);
		END IF;
		DBMS_OUTPUT.PUT_LINE(errMsgG);
		SELECT COUNT(*) INTO cnt FROM Link WHERE type_id = :old.id AND nature = 0;
		IF (cnt >= 1) THEN
			RAISE_APPLICATION_ERROR(num => -20000, msg => errMsg5
				|| :old.id || errMsg4 || :old.nature || errMsg7
				|| cnt || errMsg3 || errMsg9 || errMsg2
				|| errMsgF || errMsg1 || errMsg2 || errMsg0);
		END IF;
		SELECT COUNT(*) INTO cnt FROM "SchemeLink" WHERE link_type_id = :old.id;
		IF (cnt >= 1) THEN
			RAISE_APPLICATION_ERROR(num => -20000, msg => errMsg5
				|| :old.id || errMsg4 || :old.nature || errMsg7
				|| cnt || errMsg8 || errMsg9 || errMsg2
				|| errMsgF || errMsg1 || errMsg2 || errMsg0);
		END IF;
	ELSE
		IF (:old.nature = 1) THEN
			DBMS_OUTPUT.PUT_LINE(errMsgH);
			SELECT COUNT(*) INTO cnt FROM Link WHERE type_id = :old.id AND nature = 0;
			IF (cnt >= 1) THEN
				RAISE_APPLICATION_ERROR(num => -20000, msg =>
					errMsg1 || errMsg5 || :old.id || errMsg4
					|| :old.nature || errMsg7 || cnt
					|| errMsg3 || errMsg9 || errMsg1
					|| errMsg2 || errMsgE || errMsg0);
			END IF;
			SELECT COUNT(*) INTO cnt FROM "SchemeLink" WHERE link_type_id = :old.id;
			IF (cnt >= 1) THEN
				RAISE_APPLICATION_ERROR(num => -20000, msg =>
					errMsg1 || errMsg5 || :old.id || errMsg4
					|| :old.nature || errMsg7 || cnt
					|| errMsg8 || errMsg9 || errMsg1
					|| errMsg2 || errMsgE || errMsg0);
			END IF;
			DBMS_OUTPUT.PUT_LINE(errMsgG);
			SELECT COUNT(*) INTO cnt FROM Link WHERE type_id = :old.id AND nature = 1;
			IF (cnt >= 1) THEN
				RAISE_APPLICATION_ERROR(num => -20000, msg =>
					errMsg1 || errMsg5 || :old.id || errMsg4
					|| :old.nature || errMsg7 || cnt
					|| errMsg6 || errMsg9 || errMsg1
					|| errMsg2 || errMsgF || errMsg2
					|| errMsg0);
			END IF;
			SELECT COUNT(*) INTO cnt FROM "SchemeCableLink" WHERE cable_link_type_id = :old.id;
			IF (cnt >= 1) THEN
				RAISE_APPLICATION_ERROR(num => -20000, msg =>
					errMsg1 || errMsg5 || :old.id || errMsg4
					|| :old.nature || errMsg7 || cnt
					|| errMsgB || errMsg9 || errMsg1
					|| errMsg2 || errMsgF || errMsg2
					|| errMsg0);
			END IF;
		ELSE
			RAISE_APPLICATION_ERROR(num => -20000, msg => errMsg5
				|| :old.id || errMsgA || :old.nature
				|| errMsg0);
		END IF;
	END IF;
	DBMS_OUTPUT.PUT_LINE(errMsgC);
END;
/
