/*
 * $Id: StringFieldConditionImpl.java,v 1.1 2005/01/24 13:33:31 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.resource;

import com.syrus.AMFICOM.general.*;
import com.syrus.AMFICOM.general.corba.StringFieldSort;
import com.syrus.util.Log;

/**
 * @author $Author: bob $
 * @version $Revision: 1.1 $, $Date: 2005/01/24 13:33:31 $
 * @module resource_v1
 */
final class StringFieldConditionImpl extends com.syrus.AMFICOM.general.StringFieldCondition {
	private static final String STRING_FIELD_CONDITION_INIT = "StringFieldConditionImpl.<init>() | "; //$NON-NLS-1$

	private static final String STRING_FIELD_CONDITION_IS_CONDITION_TRUE = "StringFieldConditionImpl.isConditionTrue() | "; //$NON-NLS-1$

	private StringFieldConditionImpl(final String string, final Short entityCode, final StringFieldSort sort) {
		super();
		this.string = string;
		this.entityCode = entityCode;
		this.sort = sort.value();
		Log.debugMessage(STRING_FIELD_CONDITION_INIT
			+ "Created new instance of ResourceStringFieldCondition", //$NON-NLS-1$
			Log.FINEST);
	}

	/**
	 * Two sorts for this condition are valid:
	 * {@link StringFieldSort#STRINGSORT_BASE} and
	 * {@link StringFieldSort#STRINGSORT_INTEGER}. In the first case,
	 * objects, depending on their type, are looked for by
	 * {@link BitmapImageResource#codename} or
	 * {@link FileImageResource#fileName}. In the second one, objects are
	 * looked for by their
	 * {@link com.syrus.AMFICOM.resource.corba.ImageResource_TransferablePackage.ImageResourceDataPackage.ImageResourceSort sort}
	 * property.
	 * 
	 * @param object
	 * @throws ApplicationException
	 * @see com.syrus.AMFICOM.general.StorableObjectCondition#isConditionTrue(Object)
	 */
	public boolean isConditionTrue(final Object object) throws ApplicationException {
		boolean condition = false;
		if (object instanceof AbstractBitmapImageResource) {
			final AbstractBitmapImageResource imageResource = (AbstractBitmapImageResource) object;
			switch (this.sort) {
				case StringFieldSort._STRINGSORT_BASE:
					if (imageResource.getCodename().equals(this.string))
						condition = true;
					break;
			}
		} else if (object instanceof AbstractImageResource) {
			final AbstractImageResource imageResource = (AbstractImageResource) object;
			switch (this.sort) {
				case StringFieldSort._STRINGSORT_INTEGER:
					try {
						if (imageResource.getSort().value() == Integer.parseInt(this.string))
							condition = true;
					} catch (final NumberFormatException nfe) {
						Log.debugMessage(STRING_FIELD_CONDITION_IS_CONDITION_TRUE
							+ "This instance of ResourceStringFieldCondition was constructed improperly: " //$NON-NLS-1$
							+ this 
							+ "; evaluation result is always false...", //$NON-NLS-1$
							Log.WARNING);
					}
					break;
			}
		}
		return condition;
	}
}
