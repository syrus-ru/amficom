/*-
 * $Id: ActionType.java,v 1.22 2005/09/14 18:35:57 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.util.Codeable;

/**
 * @version $Revision: 1.22 $, $Date: 2005/09/14 18:35:57 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public interface ActionType extends TransferableObject, Codeable {

	String getCodename();

	String getDescription();
}
