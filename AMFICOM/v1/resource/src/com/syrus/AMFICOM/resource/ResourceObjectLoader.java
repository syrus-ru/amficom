/*
* $Id: ResourceObjectLoader.java,v 1.10 2005/04/12 08:14:28 bass Exp $
*
* Copyright ¿ 2004 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.resource;

import java.util.Set;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectCondition;



/**
 * @version $Revision: 1.10 $, $Date: 2005/04/12 08:14:28 $
 * @author $Author: bass $
 * @module resource_v1
 */
public interface ResourceObjectLoader {
	StorableObject loadImageResource(Identifier id) throws ApplicationException;

	Set loadImageResources(Set ids) throws ApplicationException;

	Set loadImageResourcesButIds (StorableObjectCondition condition, Set ids) throws ApplicationException;
	
	Set refresh(Set storableObjects) throws ApplicationException;
	
	void saveImageResource(AbstractImageResource abstractImageResource, boolean force) throws ApplicationException;

	void saveImageResources(Set list, boolean force) throws ApplicationException;
	
	void delete(Identifier id) throws IllegalDataException;

	void delete(final Set identifiables) throws IllegalDataException;

}
