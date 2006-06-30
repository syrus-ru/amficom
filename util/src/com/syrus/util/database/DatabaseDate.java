/*-
 * $Id: DatabaseDate.java,v 1.22 2006/06/30 15:02:35 bass Exp $
 *
 * Copyright ¿ 2004-2006 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.util.database;

import static java.util.logging.Level.SEVERE;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.syrus.util.Log;

/**
 * @author $Author: bass $
 * @version $Revision: 1.22 $, $Date: 2006/06/30 15:02:35 $
 * @author Tashoyan Arseniy Feliksovich
 * @module util
 */
public class DatabaseDate {

	public static final DateFormat SDF = new SimpleDateFormat("yyyyMMdd HHmmss");

	private DatabaseDate() {
		assert false;
	}

	/**
	 * Returns {@code null} only if database record was {@code null} (see
	 * {@link ResultSet#wasNull()}). In case database record is non-{@code
	 * null} but unparseable, this method reports a {@link ParseException}
	 * and returns a new {@link Date} object.
	 *
	 * <p>WARNING: prior to migration to
	 * {@link ResultSet#getTimestamp(String)} database schemas
	 * should be updated in order to reference TIMESTAMP, not DATE. This
	 * is not an easy task and requires a tool for schema alteration
	 * to be written.</p>
	 *
	 * <p>Currently, blind migration would result in an
	 * {@code IllegalArgumentException} at {@link java.sql.Timestamp#valueOf(String)}.</p>
	 *
	 * @see <a href = "http://ararat.science.syrus.ru/cgi-bin/bugzilla/show_bug.cgi?id=525">bug #525</a> 
	 */
	public static Date fromQuerySubString(final ResultSet resultSet, final String column) throws SQLException {
		try {
			final String dateStr = resultSet.getString(column);
			if (resultSet.wasNull()) {
				return null;
			}
			assert dateStr != null;
			return SDF.parse(dateStr);
		} catch (final ParseException pe) {
			Log.debugMessage(pe, SEVERE);
			Log.errorMessage(pe);
			return new Date();
		}
	}

	public static String toQuerySubString(final String column) {
		final String subString = "TO_CHAR(" + column + ", 'YYYYMMDD HH24MISS') " + column;
		return subString;
	}

	/**
	 * <p>Whenever <em>this</em> is used (in favour of {@link
	 * #toQuerySubString(String)}}, a subsequent call to {@link
	 * ResultSet#getTimestamp(String)} will be successful.</p>
	 *
	 * <p>{@code SSxFF}, unlike {@code SS.FF}, will select seconds and
	 * milliseconds separated with the discriminator default for the
	 * current locale, i.&nbsp;e. it will be &apos;.&apos; for {@code
	 * AMERICA} or &apos;,&apos; for {@code RUSSIA} and {@code CIS}.</p>
	 *
	 * @param column
	 */
	public static String toTimestampQuerySubString(final String column) {
		return "TO_CHAR(" + column + ", 'YYYY-MM-DD HH24:MI:SS.FF') AS " + column;
	}

	public static String toUpdateSubString(final Date date) {
		if (date != null) {
			final String subString = "TO_DATE('" + SDF.format(date) + "', 'YYYYMMDD HH24MISS')";
			return subString;
		}
		return null;
	}
}
