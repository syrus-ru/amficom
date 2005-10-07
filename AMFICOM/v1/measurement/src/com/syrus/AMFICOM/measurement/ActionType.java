/*-
 * $Id: ActionType.java,v 1.24 2005/10/07 10:04:19 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.util.Codeable;
import com.syrus.util.TransferableObject;

/**
 * @version $Revision: 1.24 $, $Date: 2005/10/07 10:04:19 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public interface ActionType<T extends IDLEntity> extends TransferableObject<T>, Codeable {

	String getCodename();

	String getDescription();
}
