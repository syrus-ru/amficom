/*
 * $Id: XMLGeneralObjectLoader.java,v 1.12 2005/02/24 16:17:56 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @version $Revision: 1.12 $, $Date: 2005/02/24 16:17:56 $
 * @author $Author: bob $
 * @module general_v1
 */
public final class XMLGeneralObjectLoader implements GeneralObjectLoader {

	private StorableObjectXML	generalXML;

	public XMLGeneralObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "general");
		this.generalXML = new StorableObjectXML(driver);
	}

	private StorableObject loadStorableObject(Identifier id) throws ApplicationException{
		return this.generalXML.retrieve(id);
	}

	public ParameterType loadParameterType(Identifier id) throws ApplicationException {
		return (ParameterType) this.loadStorableObject(id);
	}

	public CharacteristicType loadCharacteristicType(Identifier id) throws ApplicationException {
		return (CharacteristicType) this.loadStorableObject(id);
	}

	public Characteristic loadCharacteristic(Identifier id) throws ApplicationException {
		return (Characteristic) this.loadStorableObject(id);
	}

	public Collection loadParameterTypes(Collection ids) throws ApplicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadCharacteristicTypes(Collection ids) throws ApplicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadCharacteristics(Collection ids) throws ApplicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	private Collection loadStorableObjectButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.generalXML.retrieveByCondition(ids, condition);
	}

	public Collection loadParameterTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadCharacteristicTypesButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadCharacteristicsButIds(StorableObjectCondition condition, Collection ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	private void saveStorableObject(StorableObject storableObject, boolean force) throws ApplicationException {
		Identifier modifierId = SessionContext.getAccessIdentity().getUserId();
		this.generalXML.updateObject(storableObject, force, modifierId);
	}

	public void saveParameterType(ParameterType parameterType, boolean force) throws ApplicationException {
		this.saveStorableObject(parameterType, force);
		this.generalXML.flush();
	}

	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force)
			throws ApplicationException {
		this.saveStorableObject(characteristicType, force);
		this.generalXML.flush();
	}

	public void saveCharacteristic(Characteristic characteristic, boolean force) throws ApplicationException {
		this.saveStorableObject(characteristic, force);
		this.generalXML.flush();
	}

	private void saveStorableObjects(Collection storableObjects, boolean force) throws ApplicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, force);
		}
		this.generalXML.flush();
	}

	public void saveParameterTypes(Collection collection, boolean force) throws ApplicationException {
		this.saveStorableObjects(collection, force);
	}

	public void saveCharacteristicTypes(Collection collection, boolean force) throws ApplicationException {
		this.saveStorableObjects(collection, force);
	}

	public void saveCharacteristics(Collection collection, boolean force) throws ApplicationException {
		this.saveStorableObjects(collection, force);
	}

	public Set refresh(Set storableObjects) throws ApplicationException {
		// TODO Auto-generated method stub
		return Collections.EMPTY_SET;
	}

	public void delete(Identifier id) throws IllegalDataException {
		this.generalXML.delete(id);
		this.generalXML.flush();
	}

	public void delete(Collection collection) throws IllegalDataException {
		for (Iterator it = collection.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			this.generalXML.delete(id);
		}
		this.generalXML.flush();
	}

}
