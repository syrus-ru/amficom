/**
 * $Id: Alarm.java,v 1.1 2005/10/26 13:51:56 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.observer.alarm;

import java.util.Set;

import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.StorableObject;

public class Alarm extends StorableObject {

	@Override
	public Set<Identifiable> getDependencies() {
		return null;
	}

}
