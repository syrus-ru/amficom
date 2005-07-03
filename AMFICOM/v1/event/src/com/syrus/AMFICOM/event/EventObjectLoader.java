/*
 * $Id: EventObjectLoader.java,v 1.12 2005/06/22 19:24:02 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.event;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;

/**
 * @version $Revision: 1.12 $, $Date: 2005/06/22 19:24:02 $
 * @author $Author: arseniy $
 * @module event_v1
 */

public interface EventObjectLoader {

	/* Load multiple objects*/

	Set loadEventTypes(final Set<Identifier> ids) throws ApplicationException;

	Set loadEvents(final Set<Identifier> ids) throws ApplicationException;

	Set loadEventSources(final Set<Identifier> ids) throws ApplicationException;



	/* Load multiple objects but ids*/

	Set loadEventTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadEventsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadEventSourcesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;



	/* Save multiple objects*/

	void saveEventTypes(final Set<EventType> objects, final boolean force) throws ApplicationException;

	void saveEvents(final Set<Event> objects, final boolean force) throws ApplicationException;

	void saveEventSources(final Set<EventSource> objects, final boolean force) throws ApplicationException;



	Set refresh(final Set<? extends StorableObject> storableObjects) throws ApplicationException;



	void delete(final Set<? extends Identifiable> objects);

}
