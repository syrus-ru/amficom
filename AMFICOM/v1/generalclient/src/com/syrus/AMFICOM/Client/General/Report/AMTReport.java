/*
 * $Id: AMTReport.java,v 1.4 2004/09/14 14:44:18 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.Report;

import java.util.Hashtable;

/**
 * @version $Revision: 1.4 $, $Date: 2004/09/14 14:44:18 $
 * @author $Author: bass $
 * @module generalclient_v1
 */
public class AMTReport {
	public Hashtable data = new Hashtable();

	public void addRecord(String teTitle, Object teData) {
		data.put(teTitle, teData);
	}
}
