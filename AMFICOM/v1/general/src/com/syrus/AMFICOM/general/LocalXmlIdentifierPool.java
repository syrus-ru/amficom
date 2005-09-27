/*-
 * $Id: LocalXmlIdentifierPool.java,v 1.10 2005/09/27 10:58:30 bass Exp $
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
 * @author $Author: bass $
 * @version $Revision: 1.10 $, $Date: 2005/09/27 10:58:30 $
 * @module general
 */
public final class LocalXmlIdentifierPool {
	private static final Map<Key, XmlIdentifier> FORWARD_MAP = new HashMap<Key, XmlIdentifier>();

	private static final Map<XmlKey, Identifier> REVERSE_MAP = new HashMap<XmlKey, Identifier>();
	
	private static final Set<Identifier> IDS_TO_DELETE = new HashSet<Identifier>();
	/**
	 * Unlike pure Java identifiers, XML ones are tied to importType.
	 */
	private static final Set<XmlKey> XML_IDS_TO_DELETE = new HashSet<XmlKey>();
	private static final Set<String> PREFETCHED_IMPORT_TYPES = new HashSet<String>();

	private enum KeyState { NEW, UP_TO_DATE}
	
	private LocalXmlIdentifierPool() {
		assert false;
	}

	/**
	 * @param id
	 * @param xmlId
	 * @param importType
	 */
	static void put(final Identifier id, final XmlIdentifier xmlId,
			final String importType) {
		put(id, xmlId, importType, KeyState.NEW);
	}

	/**
	 * @param id
	 * @param xmlId
	 * @param importType
	 * @param state
	 */
	private static void put(final Identifier id, final XmlIdentifier xmlId,
			final String importType, final KeyState state) {
		if (id == null) {
			throw new NullPointerException("id is null");
		} else if (xmlId == null) {
			throw new NullPointerException("xmlId is null");
		} else if (importType == null || importType.length() == 0) {
			throw new NullPointerException("importType is either null or empty");
		} else if (state == null) {
			throw new NullPointerException("state is null");
		}

		final XmlIdentifier oldXmlId = FORWARD_MAP.put(new Key(id, importType, state), xmlId);
		final Identifier oldId = REVERSE_MAP.put(new XmlKey(xmlId, importType, state), id);

		if (oldXmlId != null) {
			final String xmlIdStringValue = xmlId.getStringValue();
			final String oldXmlIdStringValue = oldXmlId.getStringValue();
			if (!xmlIdStringValue.equals(oldXmlIdStringValue)) {
				throw new IllegalStateException(
						"Such situations are currently unsupported: oldXmlId = XmlIdentifier(``"
						+ oldXmlIdStringValue
						+ "''); xmlId = XmlIdentifier(``"
						+ xmlIdStringValue + "'')");
			}
		} else if (oldId != null && !id.equals(oldId)) {
			throw new IllegalStateException(
					"Such situations are currently unsupported: oldId = Identifier(``"
					+ oldId.getIdentifierString()
					+ "''); id = Identifier(``"
					+ id.getIdentifierString() + "'')"); 
		}
	}

	/**
	 * @param id
	 * @param importType
	 */
	static XmlIdentifier get(final Identifier id, final String importType) {
		if (id == null) {
			throw new NullPointerException("id is null");
		} else if (importType == null || importType.length() == 0) {
			throw new NullPointerException("importType is either null or empty");
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
		} else if (importType == null || importType.length() == 0) {
			throw new NullPointerException("importType is either null or empty");
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
		} else if (importType == null || importType.length() == 0) {
			throw new NullPointerException("importType is either null or empty");
		}

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
		} else if (importType == null || importType.length() == 0) {
			throw new NullPointerException("importType is either null or empty");
		}

		final Identifier id = REVERSE_MAP.get(new XmlKey(xmlId, importType));
		final String xmlIdStringValue = xmlId.getStringValue();
		if (id == null) {
			if (FORWARD_MAP.containsValue(xmlId)) {
				throw new IllegalStateException(
						"Reverse map contains no key while forward map contains a valuexmlId = XmlIdentifier(``"
						+ xmlIdStringValue
						+ "''), importType = ``"
						+ importType + "''");
			}
			return false;
		}
		final XmlIdentifier xmlId2 = FORWARD_MAP.get(new Key(id, importType));
		final String xmlId2StringValue = xmlId2.getStringValue();
		if (!xmlId2StringValue.equals(xmlIdStringValue)) {
			throw new IllegalStateException(
					"Both forward and reverse mappings are present, but they do not match: xmlId = XmlIdentifier(``"
					+ xmlIdStringValue
					+ "''), xmlId2 = XmlIdentifier(``"
					+ xmlId2StringValue
					+ "''), id = Identifier(``"
					+ id.getIdentifierString()
					+ "''), importType = ``"
					+ importType + "''");
		}
		return true;
	}

	/**
	 * @param xmlId
	 * @param importType
	 */
	public static void remove(final XmlIdentifier xmlId, final String importType) {
		final XmlKey xmlKey = new XmlKey(xmlId, importType);
		final Identifier id = REVERSE_MAP.remove(xmlKey);
		final Key key = new Key(id, importType);
		final XmlIdentifier xmlId2 = FORWARD_MAP.remove(key);
		final String xmlIdStringValue = xmlId.getStringValue();
		final String xmlId2StringValue = xmlId2.getStringValue();
		if (!xmlId2StringValue.equals(xmlIdStringValue)) {
			throw new IllegalStateException(
					"Both forward and reverse mappings are present, but they do not match: xmlId = XmlIdentifier(``"
					+ xmlIdStringValue
					+ "''), xmlId2 = XmlIdentifier(``"
					+ xmlId2StringValue
					+ "''), id = Identifier(``"
					+ id.getIdentifierString()
					+ "''), importType = ``"
					+ importType + "''");
		}
		IDS_TO_DELETE.add(id);
	}

	/**
	 * @param id
	 * @param importType
	 */
	public static void remove(final Identifier id, final String importType) {
		final Key key = new Key(id, importType);
		final XmlIdentifier xmlId = FORWARD_MAP.remove(key);
		final XmlKey xmlKey = new XmlKey(xmlId, importType);
		final Identifier id2 = REVERSE_MAP.remove(xmlKey);
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
		XML_IDS_TO_DELETE.add(xmlKey);
	}

	/**
	 * @param importType
	 */
	static void prefetch(final String importType) {
		if (importType == null || importType.length() == 0) {
			throw new NullPointerException("importType is either null or empty");
		}

		if (!PREFETCHED_IMPORT_TYPES.contains(importType)) {
			for (final Key key : FORWARD_MAP.keySet()) {
				if (key.getImportType().equals(importType)) {
					throw new IllegalStateException(
							"Unable to perform a prefetch for importType = ``"
							+ importType 
							+ "'' since FORWARD_MAP already contains mappings for it");
				}
			}
			for (final XmlKey xmlKey : REVERSE_MAP.keySet()) {
				if (xmlKey.getImportType().equals(importType)) {
					throw new IllegalStateException(
							"Unable to perform a prefetch for importType = ``"
							+ importType 
							+ "'' since REVERSE_MAP already contains mappings for it");
				}
			}

			final Map<Identifier, XmlIdentifier> idXmlIdMap;
			try {
				idXmlIdMap = XmlIdentifierDatabase.retrievePrefetchedMap(importType);
			} catch (final RetrieveObjectException roe) {
				Log.errorException(roe);
				return;
			}
			for (final Identifier id : idXmlIdMap.keySet()) {
				final XmlIdentifier xmlId =  idXmlIdMap.get(id);
				put(id, xmlId, importType, KeyState.UP_TO_DATE);
			}
			PREFETCHED_IMPORT_TYPES.add(importType);
		}
	}

	public static void flush() {
		
		final Map<Key, XmlIdentifier> keysToCreate = new HashMap<Key, XmlIdentifier>();
		for (final Key key : FORWARD_MAP.keySet()) {
			KeyState state = key.getState();
			if (state.equals(KeyState.NEW)) {
				keysToCreate.put(key, FORWARD_MAP.get(key));
			}
		}
		
		for (final XmlKey xmlKey : REVERSE_MAP.keySet()) {
			final KeyState state = xmlKey.getState();
			if (state.equals(KeyState.NEW)) {
				keysToCreate.put(new Key(REVERSE_MAP.get(xmlKey), xmlKey.getImportType()), xmlKey.getXmlId());
			}
		}
		
		try {
			XmlIdentifierDatabase.insertKeys(keysToCreate);
		} catch (final CreateObjectException coe) {
			Log.errorException(coe);
		}
		XmlIdentifierDatabase.removeIds(IDS_TO_DELETE);
		XmlIdentifierDatabase.removeXmlIds(XML_IDS_TO_DELETE);
	}
	
	/**
	 * @author Maxim Selivanov
	 * @author $Author: bass $
	 * @version $Revision: 1.10 $, $Date: 2005/09/27 10:58:30 $
	 * @module general
	 */
	private abstract static class State {
		protected KeyState state;
		
		protected KeyState getState() {
			return this.state;
		}
		
		protected void setState(KeyState state) {
			this.state = state;
		}
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.10 $, $Date: 2005/09/27 10:58:30 $
	 * @module general
	 */
	static final class Key extends State {
		private Identifier id;

		private String importType;

		private HashCodeGenerator hashCodeGenerator;
		
		Identifier getId() {
			return this.id;
		}

		String getImportType() {
			return this.importType;
		}
		
		/**
		 * @param id
		 * @param importType
		 */
		Key(final Identifier id, final String importType) {
			this(id, importType, KeyState.NEW);
		}
		
		/**
		 * @param id
		 * @param importType
		 * @param state
		 */
		Key(final Identifier id, final String importType, final KeyState state) {
			if (id == null) {
				throw new NullPointerException("id is null");
			} else if (importType == null || importType.length() == 0) {
				throw new NullPointerException("importType is either null or empty");
			}

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
	
	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.10 $, $Date: 2005/09/27 10:58:30 $
	 * @module general
	 */
	static class XmlKey extends State{
		private XmlIdentifier id;

		private String importType;

		private HashCodeGenerator hashCodeGenerator;
		
		XmlIdentifier getXmlId() {
			return this.id;
		}
		
		String getImportType() {
			return this.importType;
		}

		/**
		 * @param id
		 * @param importType
		 */
		XmlKey(final XmlIdentifier id, final String importType) {
			this(id, importType, KeyState.NEW);
		}

		/**
		 * @param id
		 * @param importType
		 * @param state
		 */
		XmlKey(final XmlIdentifier id, final String importType, final KeyState state) {
			if (id == null) {
				throw new NullPointerException("id is null");
			} else if (importType == null || importType.length() == 0) {
				throw new NullPointerException("importType is either null or empty");
			}

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
