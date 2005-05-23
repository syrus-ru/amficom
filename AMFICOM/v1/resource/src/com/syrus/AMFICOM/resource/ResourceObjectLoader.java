/*
* $Id: ResourceObjectLoader.java,v 1.13 2005/05/23 13:51:17 bass Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resource;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.StorableObjectCondition;



/**
 * @version $Revision: 1.13 $, $Date: 2005/05/23 13:51:17 $
 * @author $Author: bass $
 * @module resource_v1
 */
public interface ResourceObjectLoader {
	Set loadImageResources(final Set ids) throws ApplicationException;

	Set loadImageResourcesButIds(final StorableObjectCondition condition, final Set ids) throws ApplicationException;

	Set refresh(final Set storableObjects) throws ApplicationException;

	void saveImageResources(final Set objects, final boolean force) throws ApplicationException;

	void delete(final Set identifiables);
}
