/*-
 * $Id: StorableElementWrapper.java,v 1.2 2005/10/25 19:53:08 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.report;

import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author Maxim Selivanov
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/25 19:53:08 $
 * @module report
 */

public abstract class StorableElementWrapper<T extends StorableElement<T>> extends StorableObjectWrapper<T> {
	public static final String COLUMN_LOCATION_X = "location_x";
	public static final String COLUMN_LOCATION_Y = "location_y";
	public static final String COLUMN_SIZE_WIDTH = "width";
	public static final String COLUMN_SIZE_HEIGHT = "height";
	public static final String COLUMN_REPORT_TEMPLATE_ID = "report_template_id";	
}
