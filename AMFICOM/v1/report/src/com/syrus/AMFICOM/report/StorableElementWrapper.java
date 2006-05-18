/*-
 * $Id: StorableElementWrapper.java,v 1.3 2006/03/13 13:53:57 bass Exp $
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
 * @version $Revision: 1.3 $, $Date: 2006/03/13 13:53:57 $
 * @module report
 */

public abstract class StorableElementWrapper<T extends StorableElement> extends StorableObjectWrapper<T> {
	public static final String COLUMN_LOCATION_X = "location_x";
	public static final String COLUMN_LOCATION_Y = "location_y";
	public static final String COLUMN_SIZE_WIDTH = "width";
	public static final String COLUMN_SIZE_HEIGHT = "height";
	public static final String COLUMN_REPORT_TEMPLATE_ID = "report_template_id";	
}
