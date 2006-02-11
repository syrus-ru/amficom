/*
 * $Id: ModelingWrapper.java,v 1.14.2.1 2006/02/11 18:40:46 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;


/**
 * @version $Revision: 1.14.2.1 $, $Date: 2006/02/11 18:40:46 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ModelingWrapper extends ActionWrapper<Modeling> {

	private static ModelingWrapper instance;

	private ModelingWrapper() {
		super(new String[] { COLUMN_TYPE_ID,
				COLUMN_MONITORED_ELEMENT_ID,
				COLUMN_ACTION_TEMPLATE_ID,
				COLUMN_NAME,
				COLUMN_START_TIME,
				COLUMN_DURATION,
				COLUMN_STATUS });
	}

	public static ModelingWrapper getInstance() {
		if (instance == null) {
			instance = new ModelingWrapper();
		}
		return instance;
	}

}
