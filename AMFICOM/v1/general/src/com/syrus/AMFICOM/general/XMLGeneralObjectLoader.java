/*
 * $Id: XMLGeneralObjectLoader.java,v 1.10 2005/02/15 07:11:32 bob Exp $
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
 * @version $Revision: 1.10 $, $Date: 2005/02/15 07:11:32 $
 * @author $Author: bob $
 * @module general_v1
 */
public final class XMLGeneralObjectLoader implements GeneralObjectLoader {

	private StorableObjectXML	generalXML;

	public XMLGeneralObjectLoader(final File path) {
		StorableObjectXMLDriver driver = new StorableObjectXMLDriver(path, "general");
		this.generalXML = new StorableObjectXML(driver);
	}

	private StorableObject loadStorableObject(Identifier id) throws CommunicationException {
		try {
			return this.generalXML.retrieve(id);
		} catch (ObjectNotFoundException e) {
			throw new CommunicationException("XMLGeneralObjectLoader.load" + ObjectEntities.codeToString(id.getMajor())
					+ " | caught " + e.getMessage(), e);
		} catch (RetrieveObjectException e) {
			throw new CommunicationException("XMLGeneralObjectLoader.load" + ObjectEntities.codeToString(id.getMajor())
					+ " | caught " + e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLGeneralObjectLoader.load" + ObjectEntities.codeToString(id.getMajor())
					+ " | caught " + e.getMessage(), e);
		}
	}

	public ParameterType loadParameterType(Identifier id) throws DatabaseException, CommunicationException {
		return (ParameterType) this.loadStorableObject(id);
	}

	public CharacteristicType loadCharacteristicType(Identifier id) throws DatabaseException, CommunicationException {
		return (CharacteristicType) this.loadStorableObject(id);
	}

	public Characteristic loadCharacteristic(Identifier id) throws DatabaseException, CommunicationException {
		return (Characteristic) this.loadStorableObject(id);
	}

	public Collection loadParameterTypes(Collection ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadCharacteristicTypes(Collection ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public Collection loadCharacteristics(Collection ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	private Collection loadStorableObjectButIds(StorableObjectCondition condition, Collection ids) throws CommunicationException {
		try {
			return this.generalXML.retrieveByCondition(ids, condition);
		} catch (RetrieveObjectException e) {
			throw new CommunicationException("XMLGeneralObjectLoader.loadParameterTypesButIds | caught "
					+ e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLGeneralObjectLoader.loadParameterTypesButIds | caught "
					+ e.getMessage(), e);
		}

	}

	public Collection loadParameterTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadCharacteristicTypesButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public Collection loadCharacteristicsButIds(StorableObjectCondition condition, Collection ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	private void saveStorableObject(StorableObject storableObject, boolean force) throws CommunicationException {
		Identifier id = storableObject.getId();
		Identifier modifierId = SessionContext.getAccessIdentity().getUserId();
		try {
			this.generalXML.updateObject(storableObject, force, modifierId);
		} catch (UpdateObjectException e) {
			throw new CommunicationException("XMLGeneralObjectLoader.save" + ObjectEntities.codeToString(id.getMajor())
					+ " | caught " + e.getMessage(), e);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLGeneralObjectLoader.save" + ObjectEntities.codeToString(id.getMajor())
					+ " | caught " + e.getMessage(), e);
		} catch (VersionCollisionException e) {
			throw new CommunicationException("XMLGeneralObjectLoader.save" + ObjectEntities.codeToString(id.getMajor())
					+ " | caught " + e.getMessage(), e);
		}

	}

	public void saveParameterType(ParameterType parameterType, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(parameterType, force);
		this.generalXML.flush();
	}

	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(characteristicType, force);
		this.generalXML.flush();
	}

	public void saveCharacteristic(Characteristic characteristic, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(characteristic, force);
		this.generalXML.flush();
	}

	private void saveStorableObjects(Collection storableObjects, boolean force) throws CommunicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject, force);
		}
		this.generalXML.flush();
	}

	public void saveParameterTypes(Collection collection, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(collection, force);
	}

	public void saveCharacteristicTypes(Collection collection, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(collection, force);
	}

	public void saveCharacteristics(Collection collection, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(collection, force);
	}

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		// TODO Auto-generated method stub
		return Collections.EMPTY_SET;
	}

	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		try {
			this.generalXML.delete(id);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLGeneralObjectLoader.delete | caught " + e.getMessage(), e);
		}
		this.generalXML.flush();
	}

	public void delete(Collection collection) throws CommunicationException, DatabaseException {
		try {
			for (Iterator it = collection.iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				this.generalXML.delete(id);
			}
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLGeneralObjectLoader.delete | caught " + e.getMessage(), e);
		}
		this.generalXML.flush();
	}

}
