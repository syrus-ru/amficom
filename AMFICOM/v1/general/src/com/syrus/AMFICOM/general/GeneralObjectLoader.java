/*
 * $Id: GeneralObjectLoader.java,v 1.16 2005/05/01 17:27:56 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.16 $, $Date: 2005/05/01 17:27:56 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public interface GeneralObjectLoader {

	/* Load multiple objects*/

	Set loadParameterTypes(Set ids) throws ApplicationException;

	Set loadCharacteristicTypes(Set ids) throws ApplicationException;

	Set loadCharacteristics(Set ids) throws ApplicationException;



	/* Load multiple objects but ids*/

	Set loadParameterTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadCharacteristicTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadCharacteristicsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;



	/* Save multiple objects*/

	void saveParameterTypes(Set objects, boolean force) throws ApplicationException;

	void saveCharacteristicTypes(Set objects, boolean force) throws ApplicationException;

	void saveCharacteristics(Set objects, boolean force) throws ApplicationException;



	/*	Refresh*/

	Set refresh(Set objects) throws ApplicationException;
	


	/*	Delete*/

	void delete(final Set identifiables);

}
