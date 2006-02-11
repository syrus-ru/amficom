/*-
 * $Id: ModelingTypeWrapper.java,v 1.16.2.1 2006/02/11 18:40:46 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;


/**
 * @version $Revision: 1.16.2.1 $, $Date: 2006/02/11 18:40:46 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public final class ModelingTypeWrapper extends ActionTypeWrapper<ModelingType> {

	private static ModelingTypeWrapper instance;

	private ModelingTypeWrapper() {
		super(new String[] { COLUMN_CODENAME, COLUMN_DESCRIPTION });
	}

	public static ModelingTypeWrapper getInstance() {
		if (instance == null) {
			instance = new ModelingTypeWrapper();
		}
		return instance;
	}

}
