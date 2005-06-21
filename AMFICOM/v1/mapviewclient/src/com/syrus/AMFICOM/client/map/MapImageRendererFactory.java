/**
 * $Id: MapImageRendererFactory.java,v 1.2 2005/06/21 12:32:44 peskovsky Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.syrus.AMFICOM.client.model.Environment;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/21 12:32:44 $
 * @author $Author: peskovsky $
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
		Environment.log(Environment.LOG_LEVEL_FINER,
				"method call MapImageRendererFactory.create()");

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
