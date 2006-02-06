/*-
 * $Id: ActionType.java,v 1.26.2.1 2006/02/06 14:46:30 arseniy Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.measurement;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.util.transport.idl.IdlTransferableObject;

/**
 * @version $Revision: 1.26.2.1 $, $Date: 2006/02/06 14:46:30 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module measurement
 */
public interface ActionType<T extends IDLEntity> extends IdlTransferableObject<T> {

	String getCodename();

	String getDescription();
}
