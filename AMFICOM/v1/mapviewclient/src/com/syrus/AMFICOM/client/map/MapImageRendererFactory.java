/*-
 * $$Id: MapImageRendererFactory.java,v 1.10 2006/02/15 11:14:00 stas Exp $$
 *
 * Copyright 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.client.map;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.10 $, $Date: 2006/02/15 11:14:00 $
 * @author $Author: stas $
 * @author Andrei Kroupennikov
 * @module mapviewclient
 */
public final class MapImageRendererFactory {
	private MapImageRendererFactory() {
		// empty
	}

	public static MapImageRenderer create(String className,
			MapCoordinatesConverter coordsConverter,
			MapContext mapContext, 
			MapImageLoader loader) throws MapDataException {
		Log.debugMessage("method call MapImageRendererFactory.create()", Level.FINE); //$NON-NLS-1$

		try {
			Class clazz = Class.forName(className);
			Constructor[] constructors = clazz.getDeclaredConstructors();
			for (int i = 0; i < constructors.length; i++) {
				Class[] parameterTypes = constructors[i].getParameterTypes();
				if (parameterTypes.length == 3
						&& parameterTypes[0].equals(MapCoordinatesConverter.class)
						&& parameterTypes[1].equals(MapContext.class)
						&& parameterTypes[2].equals(MapImageLoader.class)) {
					Constructor constructor = constructors[i];
					constructor.setAccessible(true);
					Object[] initArgs = new Object[3];
					initArgs[0] = coordsConverter;
					initArgs[1] = mapContext;
					initArgs[2] = loader;
					return (MapImageRenderer) constructor.newInstance(initArgs);
				}
			}
		} catch (ClassNotFoundException cnfe) {
			Log.errorMessage(cnfe);
			throw new MapDataException(
					"MapImageRendererFactory.create() throws ClassNotFoundException"); //$NON-NLS-1$
		} catch (InstantiationException ie) {
			Log.errorMessage(ie);
			throw new MapDataException(
					"MapImageRendererFactory.create() throws InstantiationException"); //$NON-NLS-1$
		} catch (IllegalAccessException iae) {
			Log.errorMessage(iae);
			throw new MapDataException(
					"MapImageRendererFactory.create() throws IllegalAccessException"); //$NON-NLS-1$
		} catch (IllegalArgumentException iae) {
			Log.errorMessage(iae);
			throw new MapDataException(
					"MapImageRendererFactory.create() throws IllegalArgumentException"); //$NON-NLS-1$
		} catch (InvocationTargetException ite) {
			Log.errorMessage(ite);
			throw new MapDataException(
					"MapImageRendererFactory.create() throws InvocationTargetException"); //$NON-NLS-1$
		}
		throw new MapDataException(
				"MapImageRendererFactory.create() cannot find constructor with arguments (MapImageLoader) for class " //$NON-NLS-1$
						+ className);
	}
}
