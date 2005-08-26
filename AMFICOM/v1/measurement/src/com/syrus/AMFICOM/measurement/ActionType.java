/*-
 * $Id: ActionType.java,v 1.20 2005/08/26 18:13:31 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.Codeable;
import com.syrus.AMFICOM.general.TransferableObject;

/**
 * @version $Revision: 1.20 $, $Date: 2005/08/26 18:13:31 $
 * @author $Author: arseniy $
 * @module measurement
 */
public interface ActionType extends TransferableObject, Codeable {

	String getCodename();

	String getDescription();
}
