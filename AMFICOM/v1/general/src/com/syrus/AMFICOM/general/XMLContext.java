/*-
* $Id: XMLContext.java,v 1.3 2005/09/25 11:29:16 arseniy Exp $
*
* Copyright � 2005 Syrus Systems.
* Dept. of Science & Technology.
* Project: AMFICOM.
*/

package com.syrus.AMFICOM.general;

import gnu.trove.TShortObjectHashMap;

import com.syrus.util.Log;


/**
 * @version $Revision: 1.3 $, $Date: 2005/09/25 11:29:16 $
 * @author $Author: arseniy $
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
				"XMLContext.registerXML() | XML Handler for type: "
				+ entity + " already registered";
		Log.debugMessage("XMLContext.registerXML() | XML Handler for type: "
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

