/*
 * $Id: DatabaseString.java,v 1.3 2004/11/11 09:23:15 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.util.database;


/**
 * @version $Revision: 1.3 $, $Date: 2004/11/11 09:23:15 $
 * @author $Author: bob $
 * @module util
 */
public class DatabaseString {
	
	private DatabaseString(){
		// empty
	}
	
	/**
	 * @param string
	 * @return escape strings for sql query
	 * @since j2sdk 1.4
	 */
	public static String toQuerySubString(String string){
		return (string != null) ? string.replaceAll("(')", "$1$1") : "";
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
