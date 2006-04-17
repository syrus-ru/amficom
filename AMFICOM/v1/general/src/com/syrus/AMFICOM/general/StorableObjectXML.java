/*
 * $Id: StorableObjectXML.java,v 1.47.4.1 2006/04/17 14:24:39 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.util.Log;

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
 * @version $Revision: 1.47.4.1 $, $Date: 2006/04/17 14:24:39 $
 * @author $Author: bass $
 * @module general
 */
public class StorableObjectXML {

	private static final String		CLASSNAME	= "className";

	private StorableObjectXMLDriver	driver;

	public StorableObjectXML(final StorableObjectXMLDriver driver) {
		this.driver = driver;
	}

	public <T extends StorableObject> T retrieve(final Identifier identifier)
			throws IllegalDataException,
				ObjectNotFoundException,
				RetrieveObjectException {
		final Map<String, Object> objectMap = this.driver.getObjectMap(identifier);
		final short entityCode = identifier.getMajor();
		AbstractStorableObjectXML<T> handler = XMLContext.getXMLHandler(entityCode);
		T storableObject = handler.getStorableObject(objectMap);
		return storableObject;
	}

	public <T extends StorableObject> Set<T> retrieveButIdsByCondition(final Set<Identifier> ids,
			final StorableObjectCondition condition) throws RetrieveObjectException, IllegalDataException {
		Set<T> set = null;
		final Set<Identifier> identifiers = this.reflectXMLCondition(condition).getIdsByCondition();
		Log.debugMessage("Identifiers:" + Identifier.toString(identifiers), Log.DEBUGLEVEL10);
		identifiers.removeAll(ids);
		Log.debugMessage("Cleaned identifiers:" + Identifier.toString(identifiers), Log.DEBUGLEVEL10);
		for (final Identifier id : identifiers) {
			try {
				final T storableObject = this.<T> retrieve(id);
				if (condition.isConditionTrue(storableObject)) {
					if (set == null) {
						set = new HashSet<T>();
					}
					set.add(storableObject);
				}

			} catch (ObjectNotFoundException e) {
				final String msg = "StorableObjectXML.retrieveButIdsByCondition | object " + id.getIdentifierString() + " not found";
				throw new RetrieveObjectException(msg, e);
			} catch (ApplicationException e) {
				final String msg = "StorableObjectXML.retrieveButIdsByCondition | caught  "
						+ e.getMessage()
						+ " during check "
						+ id.getIdentifierString()
						+ " for condition ";
				throw new RetrieveObjectException(msg, e);
			}
		}
		if (set == null)
			set = Collections.emptySet();
		return set;
	}

	Set<Identifier> getIdentifiers(final short entityCode) throws IllegalDataException {
		return this.driver.getIdentifiers(entityCode);
	}

	public void updateObject(final StorableObject storableObject) throws IllegalDataException {
		final StorableObjectWrapper<StorableObject> wrapper = StorableObjectWrapper.getWrapper(storableObject.getId().getMajor());
		final AbstractStorableObjectXML<StorableObject> handler = XMLContext.getXMLHandler(storableObject.getId().getMajor());
		final List<String> keys = handler.getKeys();
		final Map<String, Object> objectMap = new HashMap<String, Object>();
		for (final String key : keys) {
			objectMap.put(key, wrapper.getValue(storableObject, key));
		}
		final Identifier id = storableObject.getId();
		final String className = storableObject.getClass().getSimpleName();
		/* put short class name when id is not unambiguously define entity */
		if (!className.equals(ObjectEntities.codeToString(id.getMajor()))) {
			objectMap.put(CLASSNAME, className);
		}

		this.driver.deleteObject(id);

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

	private <T extends StorableObjectCondition> XMLStorableObjectCondition<T> reflectXMLCondition(final StorableObjectCondition condition) {
		XMLStorableObjectCondition<T> xmlStorableObjectCondition = null;
		final String className = condition.getClass().getName();
		final int lastPoint = className.lastIndexOf('.');
		final String dbClassName = className.substring(0, lastPoint + 1) + "XML" + className.substring(lastPoint + 1);
		try {
			final Class<?> clazz = Class.forName(dbClassName);
			final Constructor<?> constructor = clazz.getDeclaredConstructor(new Class[] { condition.getClass(),
					StorableObjectXMLDriver.class });
			constructor.setAccessible(true);
			xmlStorableObjectCondition = (XMLStorableObjectCondition) constructor.newInstance(new Object[] { condition, this.driver });
		} catch (ClassNotFoundException e) {
			Log.errorMessage(e);
		} catch (SecurityException e) {
			Log.errorMessage(e);
		} catch (NoSuchMethodException e) {
			Log.errorMessage(e);
		} catch (IllegalArgumentException e) {
			Log.errorMessage(e);
		} catch (InstantiationException e) {
			Log.errorMessage(e);
		} catch (IllegalAccessException e) {
			Log.errorMessage(e);
		} catch (InvocationTargetException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null) {
					assert false;
				}
				else {
					assert false : message;
				}
			} else {
				Log.errorMessage(e);
			}
		}
		return xmlStorableObjectCondition;
	}

	public void flush() {
		this.driver.writeXmlFile();
	}

	public void delete(final Identifier id) {
		this.driver.deleteObject(id);
	}
}
