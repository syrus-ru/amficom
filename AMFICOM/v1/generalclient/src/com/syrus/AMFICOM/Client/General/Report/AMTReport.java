/*
 * $Id: AMTReport.java,v 1.5 2004/11/15 14:30:21 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.Client.General.Report;

import java.util.HashMap;
import java.util.Map;

/**
 * @version $Revision: 1.5 $, $Date: 2004/11/15 14:30:21 $
 * @author $Author: peskovsky $
 * @module generalclient_v1
 */
public class AMTReport {
	public Map data = new HashMap();

	public void addRecord(String teTitle, Object teData) {
		data.put(teTitle, teData);
	}
}
