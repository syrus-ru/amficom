/*
 * $Id: GeneralObjectLoader.java,v 1.9 2005/02/24 14:59:36 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Collection;
import java.util.Set;

/**
 * @version $Revision: 1.9 $, $Date: 2005/02/24 14:59:36 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public interface GeneralObjectLoader {

	ParameterType loadParameterType(Identifier id) throws ApplicationException;

	CharacteristicType loadCharacteristicType(Identifier id) throws ApplicationException;

	Characteristic loadCharacteristic(Identifier id) throws ApplicationException;


	Collection loadParameterTypes(Collection ids) throws ApplicationException;

	Collection loadCharacteristicTypes(Collection ids) throws ApplicationException;

	Collection loadCharacteristics(Collection ids) throws ApplicationException;


    /* Load Configuration StorableObject but argument ids */

	Collection loadParameterTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Collection loadCharacteristicTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;

	Collection loadCharacteristicsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException;


	void saveParameterType(ParameterType parameterType, boolean force) throws ApplicationException;

	void saveCharacteristicType(CharacteristicType characteristicType, boolean force) throws ApplicationException;

	void saveCharacteristic(Characteristic characteristic, boolean force) throws ApplicationException;


	void saveParameterTypes(Collection list, boolean force) throws ApplicationException;

	void saveCharacteristicTypes(Collection list, boolean force) throws ApplicationException;

	void saveCharacteristics(Collection list, boolean force) throws ApplicationException;


	Set refresh(Set storableObjects) throws ApplicationException;
	

	void delete(Identifier id) throws IllegalDataException;

	void delete(Collection objects) throws IllegalDataException;

}
