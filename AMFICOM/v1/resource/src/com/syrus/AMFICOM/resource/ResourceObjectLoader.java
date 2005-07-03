/*
* $Id: ResourceObjectLoader.java,v 1.14 2005/06/22 19:24:25 arseniy Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resource;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;



/**
 * @version $Revision: 1.14 $, $Date: 2005/06/22 19:24:25 $
 * @author $Author: arseniy $
 * @module resource_v1
 */
public interface ResourceObjectLoader {
	Set loadImageResources(final Set<Identifier> ids) throws ApplicationException;

	Set loadImageResourcesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set refresh(final Set<? extends StorableObject> storableObjects) throws ApplicationException;

	void saveImageResources(final Set<? extends AbstractImageResource> objects, final boolean force) throws ApplicationException;

	void delete(final Set<? extends Identifiable> objects);
}
