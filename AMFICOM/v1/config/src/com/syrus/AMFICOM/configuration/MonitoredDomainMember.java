/*
 * $Id: MonitoredDomainMember.java,v 1.11 2005/03/05 21:37:24 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collection;

/**
 * @version $Revision: 1.11 $, $Date: 2005/03/05 21:37:24 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public interface MonitoredDomainMember {

	Collection getMonitoredElementIds();
}
