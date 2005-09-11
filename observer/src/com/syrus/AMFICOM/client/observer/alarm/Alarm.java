/**
 * $Id: Alarm.java,v 1.1 2005/09/11 17:39:24 krupenn Exp $
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
