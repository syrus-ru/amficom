/*-
 * $Id: ActionType.java,v 1.21 2005/08/28 15:15:25 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.util.Codeable;

/**
 * @version $Revision: 1.21 $, $Date: 2005/08/28 15:15:25 $
 * @author $Author: arseniy $
 * @module measurement
 */
public interface ActionType extends TransferableObject, Codeable {

	String getCodename();

	String getDescription();
}
