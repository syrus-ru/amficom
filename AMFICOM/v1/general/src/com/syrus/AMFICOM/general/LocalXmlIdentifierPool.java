/*-
 * $Id: LocalXmlIdentifierPool.java,v 1.3 2005/09/10 17:07:53 max Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.HashCodeGenerator;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: max $
 * @version $Revision: 1.3 $, $Date: 2005/09/10 17:07:53 $
 * @module general
 */
public final class LocalXmlIdentifierPool {
	private static final Map<Key, XmlIdentifier> FORWARD_MAP = new HashMap<Key, XmlIdentifier>();

	private static final Map<XmlKey, Identifier> REVERSE_MAP = new HashMap<XmlKey, Identifier>();

	private enum KeyState { NEW, UP_TO_DATE, DELETED }
	
	private LocalXmlIdentifierPool() {
		assert false;
	}

	/**
	 * @param id
	 * @param xmlId
	 * @param importType
	 */
	static void put(final Identifier id, final XmlIdentifier xmlId, final String importType) {
		if (id == null) {
			throw new NullPointerException("id is null");
		} else if (xmlId == null) {
			throw new NullPointerException("xmlId is null");
		} else if (importType == null) {
			throw new NullPointerException("importType is null");
		}

		FORWARD_MAP.put(new Key(id, importType), xmlId);
		REVERSE_MAP.put(new XmlKey(xmlId, importType), id);
	}

	/**
	 * @param id
	 * @param importType
	 */
	static XmlIdentifier get(final Identifier id, final String importType) {
		if (id == null) {
			throw new NullPointerException("id is null");
		} else if (importType == null) {
			throw new NullPointerException("importType is null");
		}

		final XmlIdentifier xmlId = FORWARD_MAP.get(new Key(id, importType));
		if (xmlId == null) {
			throw new NoSuchElementException(
					"No forward mapping found for id = Identifier(``"
					+ id.getIdentifierString()
					+ "''), importType = ``"
					+ importType + "''");
		}
		return xmlId;
	}

	/**
	 * @param xmlId
	 * @param importType
	 */
	static Identifier get(final XmlIdentifier xmlId, final String importType) {
		if (xmlId == null) {
			throw new NullPointerException("xmlId is null");
		} else if (importType == null) {
			throw new NullPointerException("importType is null");
		}

		final Identifier id = REVERSE_MAP.get(new XmlKey(xmlId, importType));
		if (id == null) {
			throw new NoSuchElementException(
					"No reverse mapping found for xmlId = XmlIdentifier(``"
					+ xmlId.getStringValue()
					+ "''), importType = ``"
					+ importType + "''");
		}
		return id;
	}

	/**
	 * @param id
	 * @param importType
	 */
	static boolean contains(final Identifier id, final String importType) {
		if (id == null) {
			throw new NullPointerException("id is null");
		} else if (importType == null) {
			throw new NullPointerException("importType is null");
		}

		prefetch(importType);
		
		final XmlIdentifier xmlId = FORWARD_MAP.get(new Key(id, importType));
		if (xmlId == null) {
			if (REVERSE_MAP.containsValue(id)) {
				throw new IllegalStateException(
						"Forward map contains no key while reverse map contains a value: id = Identifier(``"
						+ id.getIdentifierString()
						+ "''), importType = ``"
						+ importType + "''");
			}
			return false;
		}
		final Identifier id2 = REVERSE_MAP.get(new XmlKey(xmlId, importType));
		if (!id2.equals(id)) {
			throw new IllegalStateException(
					"Both forward and reverse mappings are present, but they do not match: id = Identifier(``"
					+ id.getIdentifierString()
					+ "''), id2 = Identifier(``"
					+ id2.getIdentifierString()
					+ "''), xmlId = XmlIdentifier(``"
					+ xmlId.getStringValue()
					+ "''), importType = ``"
					+ importType + "''");
		}
		return true;
	}

	/**
	 * @param xmlId
	 * @param importType
	 */
	static boolean contains(final XmlIdentifier xmlId, final String importType) {
		if (xmlId == null) {
			throw new NullPointerException("xmlId is null");
		} else if (importType == null) {
			throw new NullPointerException("importType is null");
		}

		prefetch(importType);
		
		final Identifier id = REVERSE_MAP.get(new XmlKey(xmlId, importType));
		if (id == null) {
			if (FORWARD_MAP.containsValue(xmlId)) {
				throw new IllegalStateException(
						"Reverse map contains no key while forward map contains a valuexmlId = XmlIdentifier(``"
						+ xmlId.getStringValue()
						+ "''), importType = ``"
						+ importType + "''");
			}
			return false;
		}
		final XmlIdentifier xmlId2 = FORWARD_MAP.get(new Key(id, importType));
		if (!xmlId2.getStringValue().equals(xmlId.getStringValue())) {
			throw new IllegalStateException(
					"Both forward and reverse mappings are present, but they do not match: xmlId = XmlIdentifier(``"
					+ xmlId.getStringValue()
					+ "''), xmlId2 = XmlIdentifier(``"
					+ xmlId2.getStringValue()
					+ "''), id = Identifier(``"
					+ id.getIdentifierString()
					+ "''), importType = ``"
					+ importType + "''");
		}
		return true;
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: max $
	 * @version $Revision: 1.3 $, $Date: 2005/09/10 17:07:53 $
	 * @module general
	 */
	
	private static abstract class State {
		protected KeyState state;
		
		protected KeyState getState() {
			return this.state;
		}
		
		protected void setState(KeyState state) {
			this.state = state;
		}
	}
	
	protected static class Key extends State {
		private Identifier id;

		private String importType;

		private HashCodeGenerator hashCodeGenerator;
		
		protected Identifier getId() {
			return this.id;
		}
		protected String getImportType() {
			return this.importType;
		}
		
		/**
		 * @param id
		 * @param importType
		 */
		public Key(final Identifier id, final String importType) {
			this.id = id;
			this.importType = importType;
			this.state = KeyState.NEW;
		}
		
		public Key(final Identifier id, final String importType, KeyState state) {
			this.id = id;
			this.importType = importType;
			this.state = state;
		}

		/**
		 * @param obj
		 * @see Object#equals(Object)
		 */
		@Override
		public boolean equals(final Object obj) {
			if (obj instanceof Key) {
				final Key that = (Key) obj;
				return this.id.equals(that.id)
						&& this.importType.equals(that.importType);
			}
			return false;
		}
		
		/**
		 * @see Object#hashCode()
		 */
		@Override
		public int hashCode() {
			if (this.hashCodeGenerator == null) {
				this.hashCodeGenerator = new HashCodeGenerator();
			}
			this.hashCodeGenerator.clear();
			this.hashCodeGenerator.addObject(this.id);
			this.hashCodeGenerator.addObject(this.importType);
			return this.hashCodeGenerator.getResult();
		}
	}
	
	static void remove(XmlIdentifier xmlId, String importType) {
		XmlKey xmlKey = new XmlKey(xmlId, importType);
		Identifier id = REVERSE_MAP.remove(xmlKey);
		xmlKey.setState(KeyState.DELETED);
		REVERSE_MAP.put(xmlKey, id);
		Key key = new Key(id, importType); 
		FORWARD_MAP.remove(key);
		key.setState(KeyState.DELETED);
		FORWARD_MAP.put(key, xmlId);
	}
	
	static void remove(Identifier id, String importType) {
		Key key = new Key(id, importType);
		XmlIdentifier xmlId = FORWARD_MAP.remove(key);
		key.setState(KeyState.DELETED);
		FORWARD_MAP.put(key, xmlId);
		XmlKey xmlKey = new XmlKey(xmlId, importType);
		REVERSE_MAP.remove(xmlKey);
		xmlKey.setState(KeyState.DELETED);
		REVERSE_MAP.put(xmlKey, id);
	}
	
	private static void prefetch(String importType) {
		
		Map<Identifier, XmlIdentifier> idXmlIdMap;
		try {
			idXmlIdMap = XmlIdentifierDatabase.retrievePrefetchedMap(importType);
		} catch (RetrieveObjectException e) {
			Log.errorException(e);
			return;
		}
		for (Identifier id : idXmlIdMap.keySet()) {
			XmlIdentifier xmlId =  idXmlIdMap.get(id);
			FORWARD_MAP.put(new Key(id, importType, KeyState.UP_TO_DATE), xmlId);
			REVERSE_MAP.put(new XmlKey(xmlId, importType, KeyState.UP_TO_DATE), id);
		}
	}
	
	static void flush() {
		Set<Identifier> idsToDelete = new HashSet<Identifier>();
		Map<Key, XmlIdentifier> keysToCreate = new HashMap<Key, XmlIdentifier>();
		for (Key key : FORWARD_MAP.keySet()) {
			KeyState state = key.getState();
			if(state.equals(KeyState.DELETED)) {				
				idsToDelete.add(key.getId());
			} else if(state.equals(KeyState.NEW)) {
				keysToCreate.put(key, FORWARD_MAP.get(key));
			}
		}
		Set<XmlIdentifier> xmlIdToDelete = new HashSet<XmlIdentifier>();
		for (XmlKey xmlKey : REVERSE_MAP.keySet()) {
			KeyState state = xmlKey.getState();
			if(state.equals(KeyState.DELETED)) {				
				xmlIdToDelete.add(xmlKey.getXmlId());
			} else if(state.equals(KeyState.NEW)) {
				keysToCreate.put(new Key(REVERSE_MAP.get(xmlKey), xmlKey.getImportType()), xmlKey.getXmlId());
			}
		}
		
		XmlIdentifierDatabase.removeIds(idsToDelete);
		XmlIdentifierDatabase.removeXmlIds(xmlIdToDelete);
		
		try {
			XmlIdentifierDatabase.insertKeys(keysToCreate);
		} catch (CreateObjectException e) {
			Log.errorException(e);
		}
	}
	
		
	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: max $
	 * @version $Revision: 1.3 $, $Date: 2005/09/10 17:07:53 $
	 * @module general
	 */
	private static class XmlKey extends State{
		private XmlIdentifier id;

		private String importType;

		private HashCodeGenerator hashCodeGenerator;
		
		protected XmlIdentifier getXmlId() {
			return this.id;
		}
		
		protected String getImportType() {
			return this.importType;
		}

		/**
		 * @param id
		 * @param importType
		 */
		private XmlKey(final XmlIdentifier id, final String importType) {
			this.id = id;
			this.importType = importType;
			this.state = KeyState.NEW;
		}
		
		private XmlKey(final XmlIdentifier id, final String importType, KeyState state) {
			this.id = id;
			this.importType = importType;
			this.state = state;
		}
		
		/**
		 * @param obj
		 * @see Object#equals(Object)
		 */
		@Override
		public boolean equals(final Object obj) {
			if (obj instanceof XmlKey) {
				final XmlKey that = (XmlKey) obj;
				return this.id.getStringValue().equals(that.id.getStringValue())
						&& this.importType.equals(that.importType);
			}
			return false;
		}
		
		/**
		 * @see Object#hashCode()
		 */
		@Override
		public int hashCode() {
			if (this.hashCodeGenerator == null) {
				this.hashCodeGenerator = new HashCodeGenerator();
			}
			this.hashCodeGenerator.clear();
			this.hashCodeGenerator.addObject(this.id.getStringValue());
			this.hashCodeGenerator.addObject(this.importType);
			return this.hashCodeGenerator.getResult();
		}
	}
}
