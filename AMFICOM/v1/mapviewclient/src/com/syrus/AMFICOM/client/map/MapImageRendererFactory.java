/**
 * $Id: MapImageRendererFactory.java,v 1.3 2005/06/22 08:43:46 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.syrus.util.Log;

/**
 * @version $Revision: 1.3 $, $Date: 2005/06/22 08:43:46 $
 * @author $Author: krupenn $
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
		Log.debugMessage("method call MapImageRendererFactory.create()", Log.FINER);

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
			// MapImageRenderer renderer = (MapImageRenderer
			// )Class.forName(className).newInstance();
		} catch (ClassNotFoundException cnfe) {
			cnfe.printStackTrace();
			throw new MapDataException(
					"MapImageRendererFactory.create() throws ClassNotFoundException");
		} catch (InstantiationException ie) {
			ie.printStackTrace();
			throw new MapDataException(
					"MapImageRendererFactory.create() throws InstantiationException");
		} catch (IllegalAccessException iae) {
			iae.printStackTrace();
			throw new MapDataException(
					"MapImageRendererFactory.create() throws IllegalAccessException");
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
			throw new MapDataException(
					"MapImageRendererFactory.create() throws IllegalArgumentException");
		} catch (InvocationTargetException ite) {
			ite.printStackTrace();
			throw new MapDataException(
					"MapImageRendererFactory.create() throws InvocationTargetException");
		}
		throw new MapDataException(
				"MapImageRendererFactory.create() cannot find constructor with arguments (MapImageLoader) for class "
						+ className);
	}
}
