/*
 * $Id: MonitoredDomainMember.java,v 1.12 2005/04/01 07:57:28 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Set;

/**
 * @version $Revision: 1.12 $, $Date: 2005/04/01 07:57:28 $
 * @author $Author: bob $
 * @module config_v1
 */

public interface MonitoredDomainMember {

	Set getMonitoredElementIds();
}
