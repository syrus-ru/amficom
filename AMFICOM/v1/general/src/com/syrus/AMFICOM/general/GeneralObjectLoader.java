/*
 * $Id: GeneralObjectLoader.java,v 1.13 2005/04/22 14:44:26 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.13 $, $Date: 2005/04/22 14:44:26 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public interface GeneralObjectLoader {

	/* Load single object*/

	ParameterType loadParameterType(Identifier id) throws ApplicationException;

	CharacteristicType loadCharacteristicType(Identifier id) throws ApplicationException;

	Characteristic loadCharacteristic(Identifier id) throws ApplicationException;



	/* Load multiple objects*/

	Set loadParameterTypes(Set ids) throws ApplicationException;

	Set loadCharacteristicTypes(Set ids) throws ApplicationException;

	Set loadCharacteristics(Set ids) throws ApplicationException;



	/* Load multiple objects but ids*/

	Set loadParameterTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadCharacteristicTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadCharacteristicsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;



	/* Save single object*/

	void saveParameterType(ParameterType parameterType, boolean force) throws ApplicationException;

	void saveCharacteristicType(CharacteristicType characteristicType, boolean force) throws ApplicationException;

	void saveCharacteristic(Characteristic characteristic, boolean force) throws ApplicationException;



	/* Save multiple object*/

	void saveParameterTypes(Set objects, boolean force) throws ApplicationException;

	void saveCharacteristicTypes(Set objects, boolean force) throws ApplicationException;

	void saveCharacteristics(Set objects, boolean force) throws ApplicationException;


	Set refresh(Set objects) throws ApplicationException;
	

	void delete(Identifier id);

	void delete(final Set identifiables);

}
