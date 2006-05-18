/*-
 * $Id: ActionType.java,v 1.27 2006/03/27 10:10:07 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.util.transport.idl.IdlTransferableObject;

/**
 * @version $Revision: 1.27 $, $Date: 2006/03/27 10:10:07 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public interface ActionType<T extends IDLEntity> extends IdlTransferableObject<T> {

	String getCodename();

	String getDescription();

	/**
	 * @see Enum#ordinal()
	 */
	int ordinal();
}
