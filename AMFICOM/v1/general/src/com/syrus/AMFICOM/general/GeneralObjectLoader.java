/*
 * $Id: GeneralObjectLoader.java,v 1.6 2005/02/11 15:35:16 arseniy Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * ������-����������� �����.
 * ������: �������.
 */

package com.syrus.AMFICOM.general;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * @version $Revision: 1.6 $, $Date: 2005/02/11 15:35:16 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public interface GeneralObjectLoader {

	ParameterType loadParameterType(Identifier id) throws DatabaseException, CommunicationException;

	CharacteristicType loadCharacteristicType(Identifier id) throws DatabaseException, CommunicationException;

	Characteristic loadCharacteristic(Identifier id) throws DatabaseException, CommunicationException;


	List loadParameterTypes(Collection ids) throws DatabaseException, CommunicationException;

	List loadCharacteristicTypes(Collection ids) throws DatabaseException, CommunicationException;

	List loadCharacteristics(Collection ids) throws DatabaseException, CommunicationException;


    /* Load Configuration StorableObject but argument ids */

	List loadParameterTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

	List loadCharacteristicTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;

	List loadCharacteristicsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException, CommunicationException;


	void saveParameterType(ParameterType parameterType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveCharacteristicType(CharacteristicType characteristicType, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveCharacteristic(Characteristic characteristic, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;


	void saveParameterTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveCharacteristicTypes(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;

	void saveCharacteristics(Collection list, boolean force) throws VersionCollisionException, DatabaseException, CommunicationException;


	Set refresh(Set storableObjects) throws CommunicationException, DatabaseException;
	

	void delete(Identifier id) throws CommunicationException, DatabaseException;

	void delete(Collection objects) throws CommunicationException, DatabaseException, IllegalDataException;

}
