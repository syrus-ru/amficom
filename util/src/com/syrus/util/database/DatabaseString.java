/*
 * $Id: DatabaseString.java,v 1.4 2004/12/10 15:10:33 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.util.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.syrus.AMFICOM.general.Identifier;


/**
 * @version $Revision: 1.4 $, $Date: 2004/12/10 15:10:33 $
 * @author $Author: bob $
 * @module util
 */
public class DatabaseString {
	
	private DatabaseString(){
		// empty
	}
	
	/**
	 * @deprecated use {@link #toQuerySubString(String, int)}
	 * @param string
	 * @return escape strings for sql query
	 * @since j2sdk 1.4
	 */
	public static String toQuerySubString(String string){
		return (string != null) ? string.replaceAll("(')", "$1$1") : "";
	}
	
	/**
	 * @param string 
	 * @param length maximum length to this string
	 * @return escape strings for sql query
	 * @since j2sdk 1.4
	 */
	public static String toQuerySubString(String string, int length){
		return (string != null) ? (
				(string.length() > length) ? string.substring(0, length) : string
						).replaceAll("(')", "$1$1") : "";
	}
	
	public static void setString(PreparedStatement preparedStatement, int parameterIndex, String string, int length)
		throws SQLException {
		preparedStatement.setString(parameterIndex, 
			(string != null) ? ((string.length() > length) ? string.substring(0, length) : string) : ""	);
	}
	
	/**
	 * @param string
	 * @return sql query without escape chars 
 	 * @since j2sdk 1.4
	 */
	public static String fromQuerySubString(String string){
		// we mustn't unescape from substring because of this work done in sql driver
		// return (string != null) ? string.replaceAll("(')\\1", "$1") : null;
		return string;
	}
}
