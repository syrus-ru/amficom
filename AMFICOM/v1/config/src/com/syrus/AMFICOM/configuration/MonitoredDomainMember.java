/*
 * $Id: MonitoredDomainMember.java,v 1.10 2005/03/04 13:11:58 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Collection;

/**
 * @version $Revision: 1.10 $, $Date: 2005/03/04 13:11:58 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public interface MonitoredDomainMember {

	public Collection getMonitoredElementIds();
}
