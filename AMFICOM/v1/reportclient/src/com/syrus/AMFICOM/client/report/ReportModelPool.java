/*
 * $Id: ReportModelPool.java,v 1.2 2005/09/16 13:26:29 peskovsky Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/09/16 13:26:29 $
 * @module reportclient_v1
 */
public class ReportModelPool {
	private static Map<String,ReportModel> pool =
		new HashMap<String,ReportModel>();
	
	public static ReportModel getModel(String modelClassName) throws CreateModelException {
		ReportModel result = ReportModelPool.pool.get(modelClassName);
		if (result == null) {
			try {
				result = (ReportModel) Class.forName(modelClassName).newInstance();
				pool.put(modelClassName,result);
			} catch (Exception e) {
				Log.errorMessage("ReportBuilder.build | Can't create model for name " + modelClassName);
				Log.errorException(e);
				throw new CreateModelException (modelClassName);			
			}
		}
		return result;
	}
}
