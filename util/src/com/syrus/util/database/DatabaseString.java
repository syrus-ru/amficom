/*
 * $Id: DatabaseString.java,v 1.1 2004/10/27 11:13:57 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.util.database;


/**
 * @version $Revision: 1.1 $, $Date: 2004/10/27 11:13:57 $
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
		return string.replaceAll("(')", "\\\\$1");
	}
	
	/**
	 * @param string
	 * @return sql query without escape chars 
 	 * @since j2sdk 1.4
	 */
	public static String fromQuerySubString(String string){
		return string.replaceAll("(\\\\)", "");
	}
}
