/*-
 * $Id: StorableObjectFactory.java,v 1.2 2005/05/25 13:01:03 bass Exp $
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
 * @version $Revision: 1.2 $, $Date: 2005/05/25 13:01:03 $
 * @module general_v1
 */
public abstract class StorableObjectFactory {
	protected abstract StorableObject newInstance(final IDLEntity transferable) throws CreateObjectException;

	protected abstract Identifier getId(final IDLEntity transferable);
}
