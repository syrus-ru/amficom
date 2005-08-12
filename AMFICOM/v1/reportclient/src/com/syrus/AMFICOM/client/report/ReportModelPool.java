/*
 * $Id: ReportModelPool.java,v 1.1 2005/08/12 10:23:10 peskovsky Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.util.HashMap;
import java.util.Map;

import com.syrus.util.Log;

/**
 * Ïףכ הכÿ למהוכוי מעק¸עמג.
 * @author $Author: peskovsky $
 * @version $Revision: 1.1 $, $Date: 2005/08/12 10:23:10 $
 * @module reportclient_v1
 */
public class ReportModelPool {
	private static Map<String,ReportModel> pool =
		new HashMap<String,ReportModel>();
	
	public static ReportModel getModel(String modelClassName)
	{
		ReportModel result = ReportModelPool.pool.get(modelClassName);
		try {
			result = (ReportModel) Class.forName(modelClassName).newInstance();
			pool.put(modelClassName,result);
		} catch (Exception e) {
			Log.errorMessage("ReportBuilder.build | Can't create model for name " + modelClassName);
			Log.errorException(e);
		}
		
		return result;
	}
}
