/*-
 * $Id: XmlComplementorRegistry.java,v 1.4 2005/09/04 12:18:31 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.syrus.AMFICOM.general.xml.XmlStorableObject;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.4 $, $Date: 2005/09/04 12:18:31 $
 * @module general
 */
public final class XmlComplementorRegistry {
	private static final Map<Short, List<XmlComplementor>> REGISTRY = new HashMap<Short, List<XmlComplementor>>();

	private XmlComplementorRegistry() {
		assert false;
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
			complementors = new LinkedList<XmlComplementor>();
			REGISTRY.put(entityCode, complementors);
		}
		if (!complementors.contains(complementor)) {
			complementors.add(complementor);
		}
	}

	/**
	 * @param storableObject
	 * @param importType
	 * @throws CreateObjectException
	 * @throws RetrieveObjectException
	 * @throws UpdateObjectException
	 */
	public static void complementStorableObject(
			final XmlStorableObject storableObject,
			final String importType)
	throws CreateObjectException, RetrieveObjectException,
			UpdateObjectException {
		assert storableObject != null : NON_NULL_EXPECTED;
		final Identifier id = Identifier.fromXmlTransferable(storableObject.getId(), importType);
		assert id != null : NON_NULL_EXPECTED;
		assert !id.isVoid() : NON_VOID_EXPECTED;
		final List<XmlComplementor> complementors = REGISTRY.get(new Short(id.getMajor()));
		if (complementors == null || complementors.isEmpty()) {
			throw new UpdateObjectException("XmlComplementorRegistry.complementStorableObject() | no complementor(s) found to complement the object");
		}
		for (final XmlComplementor complementor : complementors) {
			complementor.complementStorableObject(storableObject, importType);
		}
	}
}
