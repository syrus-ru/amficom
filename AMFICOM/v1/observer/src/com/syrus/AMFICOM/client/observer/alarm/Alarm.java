/*-
 * $Id: Alarm.java,v 1.2 2005/10/26 15:32:30 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.observer.alarm;

import java.util.Collections;
import java.util.Set;

import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectWrapper;

/**
 * @author krupenn
 * @author $Author: bass $
 * @version $Revision: 1.2 $, $Date: 2005/10/26 15:32:30 $
 * @module observer
 */
public final class Alarm extends StorableObject<Alarm> {
	private static final long serialVersionUID = -8320740053940064854L;

	@Override
	public Set<Identifiable> getDependencies() {
		return Collections.emptySet();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected StorableObjectWrapper<Alarm> getWrapper() {
		throw new UnsupportedOperationException();
	}
}
