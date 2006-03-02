/*
 * $Id: ModelingWrapper.java,v 1.14.2.3 2006/03/02 16:10:42 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.measurement;


/**
 * @version $Revision: 1.14.2.3 $, $Date: 2006/03/02 16:10:42 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ModelingWrapper extends ActionWrapper<Modeling, ModelingResultParameter> {

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
