/*
 * $Id: GeneralObjectLoader.java,v 1.17 2005/06/22 19:21:49 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.general;

import java.util.Set;

/**
 * @version $Revision: 1.17 $, $Date: 2005/06/22 19:21:49 $
 * @author $Author: arseniy $
 * @module general_v1
 */

public interface GeneralObjectLoader {

	/* Load multiple objects*/

	Set loadParameterTypes(final Set<Identifier> ids) throws ApplicationException;

	Set loadCharacteristicTypes(final Set<Identifier> ids) throws ApplicationException;

	Set loadCharacteristics(final Set<Identifier> ids) throws ApplicationException;



	/* Load multiple objects but ids*/

	Set loadParameterTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadCharacteristicTypesButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;

	Set loadCharacteristicsButIds(final StorableObjectCondition condition, final Set<Identifier> ids) throws ApplicationException;



	/* Save multiple objects*/

	void saveParameterTypes(final Set<ParameterType> objects, final boolean force) throws ApplicationException;

	void saveCharacteristicTypes(final Set<CharacteristicType> objects, final boolean force) throws ApplicationException;

	void saveCharacteristics(final Set<Characteristic> objects, final boolean force) throws ApplicationException;



	/*	Refresh*/

	Set refresh(Set<? extends StorableObject> objects) throws ApplicationException;
	


	/*	Delete*/

	void delete(final Set<? extends Identifiable> identifiables);

}
