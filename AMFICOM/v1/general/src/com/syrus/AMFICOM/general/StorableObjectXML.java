/*
 * $Id: StorableObjectXML.java,v 1.17 2005/02/15 07:11:18 bob Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
 * @version $Revision: 1.17 $, $Date: 2005/02/15 07:11:18 $
 * @author $Author: bob $
 * @module general_v1
 */
public class StorableObjectXML {

	private static final String		CLASSNAME	= "className";

	private StorableObjectXMLDriver	driver;

	public StorableObjectXML(final StorableObjectXMLDriver driver) {
		this.driver = driver;
	}

	private StorableObjectWrapper getWrapper(final short entityCode) throws IllegalDataException {
		StorableObjectWrapper wrapper = null;
		String className = ObjectGroupEntities.getPackageName(entityCode) + "."
				+ ObjectEntities.codeToString(entityCode) + "Wrapper";
		try {
			Class clazz = Class.forName(className);
			Method method = clazz.getMethod("getInstance", new Class[0]);
			wrapper = (StorableObjectWrapper) method.invoke(null, new Object[0]);

		} catch (ClassNotFoundException e) {
			throw new IllegalDataException("StorableObjectXML.getWrapper | Class " + className //$NON-NLS-1$
					+ " not found on the classpath - " + e.getMessage());
		} catch (SecurityException e) {
			throw new IllegalDataException("StorableObjectXML.getWrapper | Caught " + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new IllegalDataException("StorableObjectXML.getWrapper | Class " + className //$NON-NLS-1$
					+ " haven't getInstance static method - " + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new IllegalDataException("StorableObjectXML.getWrapper | Caught " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new IllegalDataException("StorableObjectXML.getWrapper | Caught " + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new IllegalDataException("StorableObjectXML.getWrapper | Caught " + e.getMessage());
		}
		return wrapper;
	}

	public StorableObject retrieve(final Identifier identifier) throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		Map objectMap = this.driver.getObjectMap(identifier);
		short entityCode = identifier.getMajor();
		StorableObject storableObject = getStorableObject(identifier, (String) objectMap.get(CLASSNAME));
		StorableObjectWrapper wrapper = this.getWrapper(entityCode);
		storableObject.setAttributes((Date) objectMap.get(StorableObjectWrapper.COLUMN_CREATED), (Date) objectMap
				.get(StorableObjectWrapper.COLUMN_MODIFIED), (Identifier) objectMap
				.get(StorableObjectWrapper.COLUMN_CREATOR_ID), (Identifier) objectMap
				.get(StorableObjectWrapper.COLUMN_MODIFIER_ID), ((Long) objectMap
				.get(StorableObjectWrapper.COLUMN_VERSION)).longValue());
		objectMap.remove(CLASSNAME);
		objectMap.remove(StorableObjectWrapper.COLUMN_ID);
		objectMap.remove(StorableObjectWrapper.COLUMN_CREATED);
		objectMap.remove(StorableObjectWrapper.COLUMN_MODIFIED);
		objectMap.remove(StorableObjectWrapper.COLUMN_CREATOR_ID);
		objectMap.remove(StorableObjectWrapper.COLUMN_MODIFIER_ID);
		objectMap.remove(StorableObjectWrapper.COLUMN_VERSION);
		for (Iterator it = objectMap.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			wrapper.setValue(storableObject, key, objectMap.get(key));
		}
		return storableObject;
	}

	public List retrieveByCondition(Collection ids,
									StorableObjectCondition condition) throws RetrieveObjectException,
			IllegalDataException {
		List list = null;
		List identifiers = this.driver.getIdentifiers(condition.getEntityCode().shortValue());
		for (Iterator it = identifiers.iterator(); it.hasNext();) {
			Identifier id = (Identifier) it.next();
			if (ids == null || !ids.contains(id)) {
				try {
					StorableObject storableObject = retrieve(id);
					if (condition.isConditionTrue(storableObject)) {
						if (list == null)
							list = new LinkedList();
						list.add(storableObject);
					}

				} catch (ObjectNotFoundException e) {
					String msg = "StorableObjectXML.retrieveByCondition | object " + id.getIdentifierString()
							+ " not found";
					throw new RetrieveObjectException(msg, e);
				} catch (ApplicationException e) {
					String msg = "StorableObjectXML.retrieveByCondition | caught  " + e.getMessage() + " during check "
							+ id.getIdentifierString() + " for condition ";
					throw new RetrieveObjectException(msg, e);
				}
			}
		}
		if (list == null)
			list = Collections.EMPTY_LIST;
		return list;
	}

	public void updateObject(	final StorableObject storableObject,
								boolean force,
								final Identifier modifierId) throws IllegalDataException, VersionCollisionException,
			UpdateObjectException {
		StorableObjectWrapper wrapper = this.getWrapper(storableObject.getId().getMajor());
		List keys = wrapper.getKeys();
		Map objectMap = new HashMap();
		for (Iterator it = keys.iterator(); it.hasNext();) {
			String key = (String) it.next();
			objectMap.put(key, wrapper.getValue(storableObject, key));
		}
		Identifier id = storableObject.getId();
		{
			String className = storableObject.getClass().getName();
			String shortClassName = className.substring(className.lastIndexOf('.') + 1);
			/* put short class name when id is not unambiguously define entity */
			if (!shortClassName.equals(ObjectEntities.codeToString(id.getMajor())))
				objectMap.put(CLASSNAME, shortClassName);
		}
		try {
			Map xmlObjectMap = this.driver.getObjectMap(id);
			long version = ((Long) xmlObjectMap.get(StorableObjectWrapper.COLUMN_VERSION)).longValue();
			if (force || version == storableObject.getVersion()) {
				storableObject.version = version + 1;
				storableObject.modified = new Date(System.currentTimeMillis());
				storableObject.modifierId = modifierId;
			} else {
				throw new VersionCollisionException("StorableObjectXML.updateObject | version collision, id='"
						+ id.getIdentifierString() + '\'', storableObject.version, version);
			}

			this.driver.deleteObject(id);
		} catch (ObjectNotFoundException e) {
			// object not found , ok
		} catch (RetrieveObjectException e) {
			// any problems
			this.driver.deleteObject(id);
		} catch (IllegalDataException e) {
			// any problems
			this.driver.deleteObject(id);
		}
		objectMap.put(StorableObjectWrapper.COLUMN_ID, id);
		objectMap.put(StorableObjectWrapper.COLUMN_CREATED, storableObject.getCreated());
		objectMap.put(StorableObjectWrapper.COLUMN_MODIFIED, storableObject.getModified());
		objectMap.put(StorableObjectWrapper.COLUMN_CREATOR_ID, storableObject.getCreatorId());
		objectMap.put(StorableObjectWrapper.COLUMN_MODIFIER_ID, storableObject.getModifierId());
		objectMap.put(StorableObjectWrapper.COLUMN_VERSION, new Long(storableObject.getVersion()));

		this.driver.putObjectMap(storableObject.getId(), objectMap);
	}

	private StorableObject getStorableObject(	final Identifier identifier,
												String className) throws IllegalDataException {
		short entityCode = identifier.getMajor();
		String clazzName;
		if (className == null)
			clazzName = ObjectGroupEntities.getPackageName(entityCode) + '.' + ObjectEntities.codeToString(entityCode);
		else
			clazzName = ObjectGroupEntities.getPackageName(entityCode) + '.'
					+ className.substring(className.lastIndexOf('.') + 1);
		try {
			Class clazz = Class.forName(clazzName);
			Constructor[] constructors = clazz.getDeclaredConstructors();
			for (int i = 0; i < constructors.length; i++) {
				Class[] parameterTypes = constructors[i].getParameterTypes();
				if (parameterTypes.length >= 2 && parameterTypes[0].equals(Identifier.class)
						&& parameterTypes[1].equals(Identifier.class)) {
					Constructor constructor = constructors[i];
					constructor.setAccessible(true);
					Object[] initArgs = new Object[parameterTypes.length];
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
						return (StorableObject) constructor.newInstance(initArgs);
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
			throw new IllegalDataException("StorableObjectXML.getStorableObject | Class " + clazzName //$NON-NLS-1$
					+ " not found on the classpath - " + e.getMessage());
		}
		throw new IllegalDataException(
										"StorableObjectXML.getStorableObject | there is no constuctor(Identifier, Identifier, ...)");
	}

	public void flush() {
		this.driver.writeXmlFile();
	}

	public void delete(final Identifier id) throws IllegalDataException {
		this.driver.deleteObject(id);
	}
}
