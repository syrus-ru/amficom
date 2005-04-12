/*
 * $Id: GeneralObjectLoader.java,v 1.12 2005/04/12 16:17:56 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.12 $, $Date: 2005/04/12 16:17:56 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public interface GeneralObjectLoader {

	ParameterType loadParameterType(Identifier id) throws ApplicationException;

	CharacteristicType loadCharacteristicType(Identifier id) throws ApplicationException;

	Characteristic loadCharacteristic(Identifier id) throws ApplicationException;


	Set loadParameterTypes(Set ids) throws ApplicationException;

	Set loadCharacteristicTypes(Set ids) throws ApplicationException;

	Set loadCharacteristics(Set ids) throws ApplicationException;


    /* Load Configuration StorableObject but argument ids */

	Set loadParameterTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadCharacteristicTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;

	Set loadCharacteristicsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException;


	void saveParameterType(ParameterType parameterType, boolean force) throws ApplicationException;

	void saveCharacteristicType(CharacteristicType characteristicType, boolean force) throws ApplicationException;

	void saveCharacteristic(Characteristic characteristic, boolean force) throws ApplicationException;


	void saveParameterTypes(Set list, boolean force) throws ApplicationException;

	void saveCharacteristicTypes(Set list, boolean force) throws ApplicationException;

	void saveCharacteristics(Set list, boolean force) throws ApplicationException;


	Set refresh(Set storableObjects) throws ApplicationException;
	

	void delete(Identifier id);

	void delete(final Set identifiables);

}
