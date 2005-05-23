/*-
 * $Id: StorableObjectFactory.java,v 1.1 2005/05/23 16:18:42 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import org.omg.CORBA.portable.IDLEntity;

/**
 * As soon as migration to valuetypes is complete, this class and its
 * descendants will be merged with valuetyped implementations.
 *
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2005/05/23 16:18:42 $
 * @module general_v1
 */
public abstract class StorableObjectFactory {
	protected abstract StorableObject newInstance(final IDLEntity transferable) throws CreateObjectException;
}
