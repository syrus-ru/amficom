/*
 * $Id: XMLGeneralObjectLoader.java,v 1.4 2005/01/27 13:21:32 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @version $Revision: 1.4 $, $Date: 2005/01/27 13:21:32 $
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

	public List loadParameterTypes(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadCharacteristicTypes(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	public List loadCharacteristics(List ids) throws DatabaseException, CommunicationException {
		List list = new ArrayList(ids.size());
		for (Iterator it = ids.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			list.add(this.loadStorableObject(id));
		}
		return list;
	}

	private List loadStorableObjectButIds(StorableObjectCondition condition, List ids) throws CommunicationException {
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

	public List loadParameterTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadCharacteristicTypesButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	public List loadCharacteristicsButIds(StorableObjectCondition condition, List ids) throws DatabaseException,
			CommunicationException {
		return this.loadStorableObjectButIds(condition, ids);
	}

	private void saveStorableObject(StorableObject storableObject) throws CommunicationException {
		Identifier id = storableObject.getId();
		try {
			this.generalXML.updateObject(storableObject);
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
		this.saveStorableObject(parameterType);
		this.generalXML.flush();
	}

	public void saveCharacteristicType(CharacteristicType characteristicType, boolean force)
			throws VersionCollisionException, DatabaseException, CommunicationException {
		this.saveStorableObject(characteristicType);
		this.generalXML.flush();
	}

	public void saveCharacteristic(Characteristic characteristic, boolean force) throws VersionCollisionException,
			DatabaseException, CommunicationException {
		this.saveStorableObject(characteristic);
		this.generalXML.flush();
	}

	private void saveStorableObjects(List storableObjects) throws CommunicationException {
		for (Iterator it = storableObjects.iterator(); it.hasNext();) {
			StorableObject storableObject = (StorableObject) it.next();
			this.saveStorableObject(storableObject);
		}
		this.generalXML.flush();
	}

	public void saveParameterTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveCharacteristicTypes(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public void saveCharacteristics(List list, boolean force) throws VersionCollisionException, DatabaseException,
			CommunicationException {
		this.saveStorableObjects(list);
	}

	public Set refresh(Set storableObjects) throws CommunicationException, DatabaseException {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(Identifier id) throws CommunicationException, DatabaseException {
		try {
			this.generalXML.delete(id);
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLGeneralObjectLoader.delete | caught " + e.getMessage(), e);
		}
		this.generalXML.flush();
	}

	public void delete(List ids) throws CommunicationException, DatabaseException {
		try {
			for (Iterator it = ids.iterator(); it.hasNext();) {
				Identifier id = (Identifier) it.next();
				this.generalXML.delete(id);
			}
		} catch (IllegalDataException e) {
			throw new CommunicationException("XMLGeneralObjectLoader.delete | caught " + e.getMessage(), e);
		}
		this.generalXML.flush();
	}

}
