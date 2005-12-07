/*-
 * $Id: ActionType.java,v 1.26 2005/12/07 17:17:16 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.util.Codeable;
import com.syrus.util.transport.idl.IdlTransferableObject;

/**
 * @version $Revision: 1.26 $, $Date: 2005/12/07 17:17:16 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public interface ActionType<T extends IDLEntity> extends IdlTransferableObject<T>, Codeable {

	String getCodename();

	String getDescription();
}
