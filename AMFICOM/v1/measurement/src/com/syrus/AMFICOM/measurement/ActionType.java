/*-
 * $Id: ActionType.java,v 1.19 2005/08/25 20:13:56 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.TransferableObject;

/**
 * @version $Revision: 1.19 $, $Date: 2005/08/25 20:13:56 $
 * @author $Author: arseniy $
 * @module measurement
 */
public interface ActionType extends TransferableObject {

	int getCode();

	String getCodename();

	String getDescription();
}
