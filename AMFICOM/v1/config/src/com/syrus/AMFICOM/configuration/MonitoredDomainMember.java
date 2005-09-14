/*
 * $Id: MonitoredDomainMember.java,v 1.15 2005/09/14 18:42:07 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.configuration;

import java.util.Set;

import com.syrus.AMFICOM.general.Identifier;

/**
 * @version $Revision: 1.15 $, $Date: 2005/09/14 18:42:07 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module config
 */

public interface MonitoredDomainMember {

	Set<Identifier> getMonitoredElementIds();
}
