/*
 * $Id: StorableObjectWrapper.java,v 1.6 2005/05/18 11:07:38 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.syrus.util.Wrapper;

/**
 * StorableObjectWrapper provides data from Model (such as StorableObject,
 * ObjectResource) using key set.
 *
 * Model has various fields, accessors for them and many other things, which are
 * represented through controller to viewers using the same interface of
 * interaction.
 *
 * All entities of the same kind use a single StorableObjectWrapper, that's why
 * wrapper's constructor must be private and its instance must be obtained using
 * a static method <code>getInstance()</code>.
 *
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/05/18 11:07:38 $
 * @see <a href =
 *      "http://bass.science.syrus.ru/java/Bitter%20Java.pdf">&laquo;Bitter
 *      Java&raquo; by Bruce A. Tate </a>
 * @module general_v1
 */
public abstract class StorableObjectWrapper implements Wrapper {

	public static final String COLUMN_CREATED = "created";
	public static final String COLUMN_CREATOR_ID = "creator_id";
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_MODIFIED = "modified";
	public static final String COLUMN_MODIFIER_ID = "modifier_id";
	public static final String COLUMN_VERSION = "version";

	public static final String COLUMN_CODENAME = "codename";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_NAME = "name";

	public static final String COLUMN_TYPE_ID = "type_id";

	public static final String COLUMN_CHARACTERISTICS = "characteristics";

	public static final String LINK_COLUMN_PARAMETER_MODE = "parameter_mode";
	public static final String LINK_COLUMN_PARAMETER_TYPE_ID = "parameter_type_id";
	
	public static final String VIEW_NAME = "view_name";

	public static StorableObjectWrapper getWrapper(final short entityCode) throws IllegalDataException {
		StorableObjectWrapper wrapper = null;
		String className = ObjectGroupEntities.getPackageName(entityCode) + "."
				+ ObjectEntities.codeToString(entityCode) + "Wrapper";
		try {
			Class clazz = Class.forName(className);
			Method method = clazz.getMethod("getInstance", new Class[0]);
			wrapper = (StorableObjectWrapper) method.invoke(null, new Object[0]);

		} catch (ClassNotFoundException e) {
			throw new IllegalDataException("StorableObjectWrapper.getWrapper | Class " + className
					+ " not found on the classpath - " + e.getMessage());
		} catch (SecurityException e) {
			throw new IllegalDataException("StorableObjectWrapper.getWrapper | Caught " + e.getMessage());
		} catch (NoSuchMethodException e) {
			throw new IllegalDataException("StorableObjectWrapper.getWrapper | Class " + className
					+ " haven't getInstance static method - " + e.getMessage());
		} catch (IllegalArgumentException e) {
			throw new IllegalDataException("StorableObjectWrapper.getWrapper | Caught " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new IllegalDataException("StorableObjectWrapper.getWrapper | Caught " + e.getMessage());
		} catch (InvocationTargetException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof AssertionError) {
				final String message = cause.getMessage();
				if (message == null)
					assert false;
				else
					assert false: message;
			} else				
				throw new IllegalDataException("StorableObjectWrapper.getWrapper | Caught " + e.getMessage());
		}
		return wrapper;
	}
}
