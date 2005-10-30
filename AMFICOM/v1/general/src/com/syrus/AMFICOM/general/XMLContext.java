/*-
* $Id: XMLContext.java,v 1.5 2005/10/30 15:20:42 bass Exp $
*
* Copyright ¿ 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import gnu.trove.TShortObjectHashMap;

import com.syrus.util.Log;


/**
 * @version $Revision: 1.5 $, $Date: 2005/10/30 15:20:42 $
 * @author $Author: bass $
 * @author Vladimir Dolzhenko
 * @module general
 */
public final class XMLContext {

	private static final TShortObjectHashMap ENTITY_CODE_XML_MAP = new TShortObjectHashMap();

	private XMLContext() {
		assert false;
	}

	public static void registerXML(final AbstractStorableObjectXML<?> xml) {
		assert xml != null: ErrorMessages.NON_NULL_EXPECTED;
		final short entityCode = xml.getEntityCode();
		final String entity = '\'' + ObjectEntities.codeToString(entityCode) + "' (" + entityCode + ')';
		assert !ENTITY_CODE_XML_MAP.containsKey(entityCode) :
				"XML Handler for type: "
				+ entity + " already registered";
		assert Log.debugMessage("XML Handler for type: "
				+ entity + " registered",
				Log.DEBUGLEVEL10);
		ENTITY_CODE_XML_MAP.put(entityCode, xml);
	}

	public static <T extends StorableObject> AbstractStorableObjectXML<T> getXMLHandler(final Short entityCode) {
		return getXMLHandler(entityCode.shortValue());
	}

	@SuppressWarnings("unchecked")
	public static <T extends StorableObject> AbstractStorableObjectXML<T> getXMLHandler(final short entityCode ) {
		final AbstractStorableObjectXML handler = (AbstractStorableObjectXML) ENTITY_CODE_XML_MAP.get(entityCode);
		assert handler != null : "XML Handler for " + ObjectEntities.codeToString(entityCode) + " isn't registered.";
		return handler;
	}
	
}

