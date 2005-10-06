/*-
 * $Id: ActionType.java,v 1.23 2005/10/06 15:19:43 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.TransferableObject;
import com.syrus.util.Codeable;

/**
 * @version $Revision: 1.23 $, $Date: 2005/10/06 15:19:43 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public interface ActionType<T extends IDLEntity> extends TransferableObject<T>, Codeable {

	String getCodename();

	String getDescription();
}
