/*
 * $Id: StorableObjectXML.java,v 1.2 2005/01/25 06:10:48 bob Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @version $Revision: 1.2 $, $Date: 2005/01/25 06:10:48 $
 * @author $Author: bob $
 * @module general_v1
 */
public abstract class StorableObjectXML {

	private StorableObjectXMLDriver	driver;

	public StorableObjectXML(final StorableObjectXMLDriver driver) {
		this.driver = driver;
	}

	protected abstract Class getStorableObjectClass(final short entityCode);

	protected abstract Wrapper getWrapper(final short entityCode);

	public StorableObject retrieve(final Identifier identifier) throws IllegalDataException, ObjectNotFoundException,
			RetrieveObjectException {
		Map objectMap = this.driver.getObjectMap(identifier);
		short entityCode = identifier.getMajor();
		StorableObject storableObject = getStorableObject(this.getStorableObjectClass(entityCode), identifier);
		Wrapper wrapper = this.getWrapper(entityCode);
		storableObject.setAttributes(new Date(Long.parseLong((String) objectMap
				.get(StorableObjectDatabase.COLUMN_CREATED))), new Date(Long.parseLong((String) objectMap
				.get(StorableObjectDatabase.COLUMN_MODIFIED))), new Identifier((String) objectMap
				.get(StorableObjectDatabase.COLUMN_CREATOR_ID)), new Identifier((String) objectMap
				.get(StorableObjectDatabase.COLUMN_MODIFIER_ID)));
		for (Iterator it = objectMap.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			wrapper.setValue(storableObject, key, objectMap.get(key));
		}
		return storableObject;
	}

	public void updateObject(final StorableObject storableObject) throws IllegalDataException, VersionCollisionException,
			UpdateObjectException {
		Wrapper wrapper = this.getWrapper(storableObject.getId().getMajor());
		List keys = wrapper.getKeys();
		Map objectMap = new HashMap();
		for (Iterator it = keys.iterator(); it.hasNext();) {
			String key = (String) it.next();
			objectMap.put(key, wrapper.getValue(storableObject, key));
		}
		this.driver.putObjectMap(storableObject.getId(), objectMap);
	}

	private StorableObject getStorableObject(final Class clazz, final Identifier identifier) throws IllegalDataException {
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
					throw new IllegalDataException("StorableObjectXML.getStorableObject | Caught an IllegalArgumentException");
				} catch (InstantiationException e) {
					throw new IllegalDataException("StorableObjectXML.getStorableObject | Caught an InstantiationException");
				} catch (IllegalAccessException e) {
					throw new IllegalDataException("StorableObjectXML.getStorableObject | Caught an IllegalAccessException");
				} catch (InvocationTargetException e) {
					throw new IllegalDataException("StorableObjectXML.getStorableObject | Caught an InvocationTargetException");
				}
			}
		}
		throw new IllegalDataException("StorableObjectXML.getStorableObject | there is no constuctor(Identifier, Identifier, ...)");
	}
	
	public void flush() {
		this.driver.writeXmlFile();
	}
	
	public void delete(final Identifier id) throws IllegalDataException {
		this.driver.deleteObject(id);
	}
}
