/*
 * $Id: DatabaseString.java,v 1.2 2004/10/27 12:20:17 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.util.database;


/**
 * @version $Revision: 1.2 $, $Date: 2004/10/27 12:20:17 $
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
		return (string != null) ? string.replaceAll("('){2}", "$1") : null;
	}
}
