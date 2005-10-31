/*
 * $Id: ReportModelPool.java,v 1.6 2005/10/31 12:29:57 bass Exp $
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
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/10/31 12:29:57 $
 * @module reportclient
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
				Log.errorMessage("Can't create model for name " + modelClassName);
				Log.errorMessage(e);
				throw new CreateModelException (modelClassName);			
			}
		}
		return result;
	}
}
