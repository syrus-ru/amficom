/*
 * $Id: AMTReport.java,v 1.3 2004/09/14 12:38:13 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.Report;

import java.util.Hashtable;

/**
 * @version $Revision: 1.3 $, $Date: 2004/09/14 12:38:13 $
 * @author $Author: bass $
 */
public class AMTReport {
	public Hashtable data = new Hashtable();

	public void addRecord(String teTitle, Object teData) {
		data.put(teTitle, teData);
	}
}
