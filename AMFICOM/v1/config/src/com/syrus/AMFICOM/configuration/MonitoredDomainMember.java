/*
 * $Id: MonitoredDomainMember.java,v 1.13 2005/07/24 14:55:43 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.13 $, $Date: 2005/07/24 14:55:43 $
 * @author $Author: arseniy $
 * @module config_v1
 */

public interface MonitoredDomainMember {

	Set<Identifier> getMonitoredElementIds();
}
