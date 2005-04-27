/*
 * $Id: XMLGeneralObjectLoader.java,v 1.1 2005/04/27 13:38:20 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @version $Revision: 1.1 $, $Date: 2005/04/27 13:38:20 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public final class XMLGeneralObjectLoader extends XMLObjectLoader implements GeneralObjectLoader {

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

	public Set loadParameterTypes(Set ids) throws ApplicationException {
		Set set = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			set.add(this.loadStorableObject(id));
		}
		return set;
	}

	public Set loadCharacteristicTypes(Set ids) throws ApplicationException {
		Set set = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			set.add(this.loadStorableObject(id));
		}
		return set;
	}

	public Set loadCharacteristics(Set ids) throws ApplicationException {
		Set set = new HashSet(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			set.add(this.loadStorableObject(id));
		}
		return set;
	}

	private Set loadStorableObjectButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.generalXML.retrieveByCondition(ids, condition);
	}

	public Set loadParameterTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set loadCharacteristicTypesButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Set loadCharacteristicsButIds(StorableObjectCondition condition, Set ids) throws ApplicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	private void saveStorableObject(StorableObject storableObject, boolean force) throws ApplicationException {
		this.generalXML.updateObject(storableObject, force);
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

	private void saveStorableObjects(Set storableObjects, boolean force) throws ApplicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, force);
		}
		this.generalXML.flush();
	}

	public void saveParameterTypes(Set collection, boolean force) throws ApplicationException {
		this.saveStorableObjects(collection, force);
	}

	public void saveCharacteristicTypes(Set collection, boolean force) throws ApplicationException {
		this.saveStorableObjects(collection, force);
	}

	public void saveCharacteristics(Set collection, boolean force) throws ApplicationException {
		this.saveStorableObjects(collection, force);
	}

	public Set refresh(Set storableObjects) throws ApplicationException {
		return Collections.EMPTY_SET;
	}

	public void delete(Identifier id) {
		this.generalXML.delete(id);
		this.generalXML.flush();
	}

	public void delete(final Set identifiables) {
		for (Iterator it = identifiables.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			this.generalXML.delete(id);
		}
		this.generalXML.flush();
	}

}
