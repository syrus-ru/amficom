/*
 * $Id: GeneralObjectLoader.java,v 1.3 2005/02/08 09:14:37 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.List;
import java.util.Set;

/**
 * @version $Revision: 1.3 $, $Date: 2005/02/08 09:14:37 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public interface GeneralObjectLoader {

	ParameterType loadParameterType(Identifier id) throws DatabaseException, CommunicationException;

	CharacteristicType loadCharacteristicType(Identifier id) throws DatabaseException, CommunicationException;

	Characteristic loadCharacteristic(Identifier id) throws DatabaseException, CommunicationException;


	List loadParameterTypes(List ids) throws DatabaseException, CommunicationException;

	List loadCharacteristicTypes(List ids) throws DatabaseException, CommunicationException;

	List loadCharacteristics(List ids) throws DatabaseException, CommunicationException;


    /* Load Configuration StorableObject but argument ids */

	List loadParameterTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadCharacteristicTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;

	List loadCharacteristicsButIds(StorableObjectCondition condition, List ids) throws DatabaseException, CommunicationException;


	void saveParameterType(ParameterType parameterType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveCharacteristicType(CharacteristicType characteristicType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveCharacteristic(Characteristic characteristic, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;


	void saveParameterTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveCharacteristicTypes(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveCharacteristics(List list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;


	Set refresh(Set storableObjects) throws CommunicationException, DatabaseException;


	void delete(Identifier id) throws CommunicationException, DatabaseException;

	void delete(List objects) throws CommunicationException, DatabaseException, IllegalDataException;

}
