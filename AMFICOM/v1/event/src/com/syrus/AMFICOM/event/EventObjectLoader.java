/*
 * $Id: EventObjectLoader.java,v 1.11 2005/05/01 16:49:02 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.11 $, $Date: 2005/05/01 16:49:02 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public interface EventObjectLoader {

	/* Load multiple objects*/

	Set loadEventTypes(Set ids) throws ApplicationException;

	Set loadEvents(Set ids) throws ApplicationException;

	Set loadEventSources(Set ids) throws ApplicationException;



	/* Load multiple objects but ids*/

	Set loadEventTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadEventsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadEventSourcesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;



	/* Save multiple objects*/

	void saveEventTypes(Set collection, boolean force) throws ApplicationException;

	void saveEvents(Set collection, boolean force) throws ApplicationException;

	void saveEventSources(Set collection, boolean force) throws ApplicationException;



	java.util.Set refresh(java.util.Set storableObjects) throws ApplicationException;



	void delete(final Set objects);

}
