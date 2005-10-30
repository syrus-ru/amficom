/*-
 * $Id: XmlComplementorRegistry.java,v 1.13 2005/10/30 14:49:07 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static java.util.logging.Level.INFO;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.syrus.AMFICOM.general.XmlComplementor.ComplementationMode;
import com.syrus.AMFICOM.general.xml.XmlStorableObject;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.13 $, $Date: 2005/10/30 14:49:07 $
 * @module general
 */
public final class XmlComplementorRegistry {
	private static final Map<Short, List<XmlComplementor>> REGISTRY = new HashMap<Short, List<XmlComplementor>>();

	private static final Set<String> QUIET_CLASS_NAMES = new HashSet<String>();

	private XmlComplementorRegistry() {
		assert false;
	}

	/**
	 * @param entityCode
	 * @param complementor
	 */
	public static void registerComplementor(final short entityCode,
			final XmlComplementor complementor) {
		registerComplementor(new Short(entityCode), complementor);
	}

	/**
	 * @param entityCode
	 * @param complementor
	 */
	public static void registerComplementor(final Short entityCode,
			final XmlComplementor complementor) {
		assert entityCode != null : NON_NULL_EXPECTED;
		assert ObjectEntities.isEntityCodeValid(entityCode.shortValue());
		assert complementor != null : NON_NULL_EXPECTED;
		List<XmlComplementor> complementors = REGISTRY.get(entityCode);
		if (complementors == null) {
			complementors = Collections.synchronizedList(new LinkedList<XmlComplementor>());
			REGISTRY.put(entityCode, complementors);
		}
		if (!complementors.contains(complementor)) {
			complementors.add(complementor);
		}
	}

	/**
	 * @param storableObject
	 * @param entityCode
	 * @param importType
	 * @param mode
	 * @throws CreateObjectException
	 * @throws UpdateObjectException
	 */
	public static void complementStorableObject(
			final XmlStorableObject storableObject,
			final short entityCode,
			final String importType,
			final ComplementationMode mode)
	throws CreateObjectException, UpdateObjectException {
		assert storableObject != null : NON_NULL_EXPECTED;
		final List<XmlComplementor> complementors = REGISTRY.get(new Short(entityCode));
		if (complementors == null || complementors.isEmpty()) {
			final String className = storableObject.getClass().getName();
			if (!QUIET_CLASS_NAMES.contains(className)) {
				QUIET_CLASS_NAMES.add(className);
				Log.debugMessage("no complementor(s) found fot type: "
						+ className + " (this message will only be issued once)",
						INFO);
			}
			return;
		}
		synchronized (complementors) {
			for (final XmlComplementor complementor : complementors) {
				complementor.complementStorableObject(storableObject, importType, mode);
			}
		}
	}
}
