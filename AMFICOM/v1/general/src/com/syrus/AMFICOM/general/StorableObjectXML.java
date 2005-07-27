/*
 * $Id: StorableObjectXML.java,v 1.28 2005/07/27 13:31:35 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Provide routines with storage StorableObject as XML. StorableObject must be
 * declarated at {@link com.syrus.AMFICOM.general.ObjectEntities}and
 * {@link com.syrus.AMFICOM.general.ObjectGroupEntities}. StorableObject must
 * have protected constuctor with arguments at less Identifier id, Identifier
 * creatorId. There must be a wrapper for the storable object which must be
 * named StorableObjectWrapper and belong to the same package (i.g.
 * {@link com.syrus.AMFICOM.general.CharacteristicWrapper}for
 * {@link com.syrus.AMFICOM.general.Characteristic}) which must have static
 * getInstance method.
 *
 * @version $Revision: 1.28 $, $Date: 2005/07/27 13:31:35 $
 * @author $Author: arseniy $
 * @module general_v1
 */
public class StorableObjectXML {

	private static final String		CLASSNAME	= "className";

	private StorableObjectXMLDriver	driver;

	public StorableObjectXML(final StorableObjectXMLDriver driver) {
		this.driver = driver;
	}

	public <T extends StorableObject> T retrieve(final Identifier identifier) throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		final Map<String, Object> objectMap = this.driver.getObjectMap(identifier);
		final short entityCode = identifier.getMajor();
		final T storableObject = this.getStorableObject(identifier, (String) objectMap.get(CLASSNAME));
		final StorableObjectWrapper wrapper = StorableObjectWrapper.getWrapper(entityCode);
		storableObject.setAttributes((Date) objectMap.get(StorableObjectWrapper.COLUMN_CREATED),
				(Date) objectMap.get(StorableObjectWrapper.COLUMN_MODIFIED),
				(Identifier) objectMap.get(StorableObjectWrapper.COLUMN_CREATOR_ID),
				(Identifier) objectMap.get(StorableObjectWrapper.COLUMN_MODIFIER_ID),
				((StorableObjectVersion) objectMap.get(StorableObjectWrapper.COLUMN_VERSION)));
		objectMap.remove(CLASSNAME);
		objectMap.remove(StorableObjectWrapper.COLUMN_ID);
		objectMap.remove(StorableObjectWrapper.COLUMN_CREATED);
		objectMap.remove(StorableObjectWrapper.COLUMN_MODIFIED);
		objectMap.remove(StorableObjectWrapper.COLUMN_CREATOR_ID);
		objectMap.remove(StorableObjectWrapper.COLUMN_MODIFIER_ID);
		objectMap.remove(StorableObjectWrapper.COLUMN_VERSION);
		for (final String key : objectMap.keySet()) {
			wrapper.setValue(storableObject, key, objectMap.get(key));
		}
		return storableObject;
	}

	public <T extends StorableObject> Set<T> retrieveByCondition(final Set<Identifier> ids, final StorableObjectCondition condition)
			throws RetrieveObjectException,
				IllegalDataException {
		Set<T> set = null;
		final Set<Identifier> identifiers = this.driver.getIdentifiers(condition.getEntityCode().shortValue());
		for (final Identifier id : identifiers) {
			if (ids == null || !ids.contains(id)) {
				try {
					final T storableObject = this.retrieve(id);
					if (condition.isConditionTrue(storableObject)) {
						if (set == null)
							set = new HashSet<T>();
						set.add(storableObject);
					}

				} catch (ObjectNotFoundException e) {
					final String msg = "StorableObjectXML.retrieveByCondition | object " + id.getIdentifierString()
							+ " not found";
					throw new RetrieveObjectException(msg, e);
				} catch (ApplicationException e) {
					final String msg = "StorableObjectXML.retrieveByCondition | caught  " + e.getMessage() + " during check "
							+ id.getIdentifierString() + " for condition ";
					throw new RetrieveObjectException(msg, e);
				}
			}
		}
		if (set == null)
			set = Collections.emptySet();
		return set;
	}

	public void updateObject(final StorableObject storableObject, boolean force)
			throws IllegalDataException, VersionCollisionException,
			UpdateObjectException {
		final StorableObjectWrapper wrapper = StorableObjectWrapper.getWrapper(storableObject.getId().getMajor());
		final List<String> keys = wrapper.getKeys();
		final Map<String, Object> objectMap = new HashMap<String, Object>();
		for (final String key : keys) {
			objectMap.put(key, wrapper.getValue(storableObject, key));
		}
		final Identifier id = storableObject.getId();
		{
			final String className = storableObject.getClass().getName();
			final String shortClassName = className.substring(className.lastIndexOf('.') + 1);
			/* put short class name when id is not unambiguously define entity */
			if (!shortClassName.equals(ObjectEntities.codeToString(id.getMajor())))
				objectMap.put(CLASSNAME, shortClassName);
		}
		
		boolean canBeModified = false;
		try {
			final Map<String, Object> xmlObjectMap = this.driver.getObjectMap(id);
			final StorableObjectVersion version = (StorableObjectVersion) xmlObjectMap.get(StorableObjectWrapper.COLUMN_VERSION);
			if (force || version == storableObject.getVersion()) {
				storableObject.version.increment();	
				canBeModified = true;
			} else {
				throw new VersionCollisionException("StorableObjectXML.updateObject | version collision, id='"
						+ id.getIdentifierString() + '\'', storableObject.version.longValue(), version.longValue());
			}
			this.driver.deleteObject(id);
		} catch (ObjectNotFoundException e) {
			// object not found , ok
			canBeModified = true;
		} catch (RetrieveObjectException e) {
			throw new UpdateObjectException("StorableObjectXML.updateObject | retrieve exception -- " + e.getMessage(),e );
		} catch (IllegalDataException e) {
			throw new UpdateObjectException("StorableObjectXML.updateObject | illegal data exception -- " + e.getMessage(),e );
		}

		if (canBeModified) {			
			storableObject.modified = new Date(System.currentTimeMillis());
		}

		/**
		 * NOTE modifierId remains the same, but version increments.
		 */
		storableObject.setUpdated(storableObject.modifierId);
		objectMap.put(StorableObjectWrapper.COLUMN_ID, id);
		objectMap.put(StorableObjectWrapper.COLUMN_CREATED, storableObject.getCreated());
		objectMap.put(StorableObjectWrapper.COLUMN_MODIFIED, storableObject.getModified());
		objectMap.put(StorableObjectWrapper.COLUMN_CREATOR_ID, storableObject.getCreatorId());
		objectMap.put(StorableObjectWrapper.COLUMN_MODIFIER_ID, storableObject.getModifierId());
		objectMap.put(StorableObjectWrapper.COLUMN_VERSION, storableObject.getVersion());
		this.driver.putObjectMap(storableObject.getId(), objectMap);
		storableObject.cleanupUpdate();
	}

	private <T extends StorableObject> T getStorableObject(final Identifier identifier, final String className) throws IllegalDataException {
		final short entityCode = identifier.getMajor();
		String clazzName;
		if (className == null)
			clazzName = ObjectGroupEntities.getPackageName(entityCode) + '.' + ObjectEntities.codeToString(entityCode);
		else
			clazzName = ObjectGroupEntities.getPackageName(entityCode) + '.'
					+ className.substring(className.lastIndexOf('.') + 1);
		try {
			final Class clazz = Class.forName(clazzName);
			final Constructor[] constructors = clazz.getDeclaredConstructors();
			for (int i = 0; i < constructors.length; i++) {
				Class[] parameterTypes = constructors[i].getParameterTypes();
				if (parameterTypes.length >= 2 && parameterTypes[0].equals(Identifier.class)
						&& parameterTypes[1].equals(Identifier.class)) {
					final Constructor constructor = constructors[i];
					constructor.setAccessible(true);
					final Object[] initArgs = new Object[parameterTypes.length];
					initArgs[0] = identifier;
					for (int j = 1; j < parameterTypes.length; j++) {
						if (parameterTypes[j].equals(boolean.class)) {
							initArgs[j] = Boolean.FALSE;
						} else if (parameterTypes[j].equals(int.class)) {
							initArgs[j] = new Integer(0);
						} else if (parameterTypes[j].equals(short.class)) {
							initArgs[j] = new Short((short) 0);
						} else if (parameterTypes[j].equals(long.class)) {
							initArgs[j] = new Long(0);
						} else if (parameterTypes[j].equals(double.class)) {
							initArgs[j] = new Double(0.0);
						} else if (parameterTypes[j].equals(float.class)) {
							initArgs[j] = new Float(0.0);
						} else if (parameterTypes[j].equals(byte.class)) {
							initArgs[j] = new Byte((byte) 0);
						} else
							initArgs[j] = null;
					}
					try {
						return (T) constructor.newInstance(initArgs);
					} catch (IllegalArgumentException e) {
						throw new IllegalDataException(
														"StorableObjectXML.getStorableObject | Caught an IllegalArgumentException");
					} catch (InstantiationException e) {
						throw new IllegalDataException(
														"StorableObjectXML.getStorableObject | Caught an InstantiationException");
					} catch (IllegalAccessException e) {
						throw new IllegalDataException(
														"StorableObjectXML.getStorableObject | Caught an IllegalAccessException");
					} catch (InvocationTargetException e) {
						throw new IllegalDataException(
														"StorableObjectXML.getStorableObject | Caught an InvocationTargetException");
					}
				}
			}
		} catch (ClassNotFoundException e) {
			throw new IllegalDataException("StorableObjectXML.getStorableObject | Class " + clazzName
					+ " not found on the classpath - " + e.getMessage());
		}
		throw new IllegalDataException("StorableObjectXML.getStorableObject | there is no constuctor(Identifier, Identifier, ...)");
	}

	public void flush() {
		this.driver.writeXmlFile();
	}

	public void delete(final Identifier id) {
		this.driver.deleteObject(id);
	}
}
