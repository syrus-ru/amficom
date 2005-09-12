/*-
 * $Id: LocalXmlIdentifierPool.java,v 1.9 2005/09/12 12:01:15 max Exp $
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
 * @version $Revision: 1.9 $, $Date: 2005/09/12 12:01:15 $
 * @module general
 */
public final class LocalXmlIdentifierPool {
	private static final Map<Key, XmlIdentifier> FORWARD_MAP = new HashMap<Key, XmlIdentifier>();

	private static final Map<XmlKey, Identifier> REVERSE_MAP = new HashMap<XmlKey, Identifier>();
	
	private static final Set<Identifier> IDS_TO_DELETE = new HashSet<Identifier>();
	private static final Set<XmlIdentifier> XML_IDS_TO_DELETE = new HashSet<XmlIdentifier>();
	private static final Set<String> PREFETCHED_SET = new HashSet<String>();

	private enum KeyState { NEW, UP_TO_DATE}
	
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
	 * @param xmlId
	 * @param importType
	 */
	public static void remove(final XmlIdentifier xmlId, final String importType) {
		final XmlKey xmlKey = new XmlKey(xmlId, importType);
		final Identifier id = REVERSE_MAP.remove(xmlKey);
		final Key key = new Key(id, importType);
		FORWARD_MAP.remove(key);		
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
		REVERSE_MAP.remove(xmlKey);
		XML_IDS_TO_DELETE.add(xmlId);
	}

	private static void prefetch(final String importType) {
		if (!PREFETCHED_SET.contains(importType)) {
			final Map<Identifier, XmlIdentifier> idXmlIdMap;
			try {
				idXmlIdMap = XmlIdentifierDatabase.retrievePrefetchedMap(importType);
			} catch (final RetrieveObjectException e) {
				Log.errorException(e);
				return;
			}
			for (final Identifier id : idXmlIdMap.keySet()) {
				final XmlIdentifier xmlId =  idXmlIdMap.get(id);
				FORWARD_MAP.put(new Key(id, importType, KeyState.UP_TO_DATE), xmlId);
				REVERSE_MAP.put(new XmlKey(xmlId, importType, KeyState.UP_TO_DATE), id);
			}
			PREFETCHED_SET.add(importType);
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
		} catch (final CreateObjectException e) {
			Log.errorException(e);
		}
		XmlIdentifierDatabase.removeIds(IDS_TO_DELETE);
		XmlIdentifierDatabase.removeXmlIds(XML_IDS_TO_DELETE);
	}
	
	/**
	 * @author Maxim Selivanov
	 * @author $Author: max $
	 * @version $Revision: 1.9 $, $Date: 2005/09/12 12:01:15 $
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
	 * @author $Author: max $
	 * @version $Revision: 1.9 $, $Date: 2005/09/12 12:01:15 $
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
	 * @author $Author: max $
	 * @version $Revision: 1.9 $, $Date: 2005/09/12 12:01:15 $
	 * @module general
	 */
	private static class XmlKey extends State{
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
		private XmlKey(final XmlIdentifier id, final String importType) {
			this(id, importType, KeyState.NEW);
		}

		/**
		 * @param id
		 * @param importType
		 * @param state
		 */
		private XmlKey(final XmlIdentifier id, final String importType, final KeyState state) {
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
