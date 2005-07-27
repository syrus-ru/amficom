/*
 * $Id: MonitoredDomainMember.java,v 1.14 2005/07/27 15:59:22 bass Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.14 $, $Date: 2005/07/27 15:59:22 $
 * @author $Author: bass $
 * @module config
 */

public interface MonitoredDomainMember {

	Set<Identifier> getMonitoredElementIds();
}
