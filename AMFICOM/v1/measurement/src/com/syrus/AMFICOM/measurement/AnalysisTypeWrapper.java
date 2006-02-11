/*-
 * $Id: AnalysisTypeWrapper.java,v 1.19.2.1 2006/02/11 18:40:45 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;


/**
 * @version $Revision: 1.19.2.1 $, $Date: 2006/02/11 18:40:45 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class AnalysisTypeWrapper extends ActionTypeWrapper<AnalysisType> {

	private static AnalysisTypeWrapper instance;

	private AnalysisTypeWrapper() {
		super(new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION });
	}

	public static AnalysisTypeWrapper getInstance() {
		if (instance == null) {
			instance = new AnalysisTypeWrapper();
		}
		return instance;
	}

}
