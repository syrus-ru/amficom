/*-
 * $Id: LocalXmlIdentifierPool.java,v 1.20 2005/10/30 15:20:42 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static java.util.logging.Level.INFO;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Map.Entry;

import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;

/**
 * @author Andrew ``Bass'' Shcheglov
 * @author $Author: bass $
 * @version $Revision: 1.20 $, $Date: 2005/10/30 15:20:42 $
 * @module general
 */
public final class LocalXmlIdentifierPool {
	private static final Map<Key, String> FORWARD_MAP = new HashMap<Key, String>();

	private static final Map<XmlKey, Identifier> REVERSE_MAP = new HashMap<XmlKey, Identifier>();
	
	private static final Set<Key> KEYS_TO_DELETE = new HashSet<Key>();
	private static final Set<XmlKey> XML_KEYS_TO_DELETE = new HashSet<XmlKey>();
	private static final Set<String> PREFETCHED_IMPORT_TYPES = new HashSet<String>();

	enum KeyState {
		NEW,
		UP_TO_DATE
	}

	private LocalXmlIdentifierPool() {
		assert false;
	}

	/**
	 * @param id
	 * @param xmlId
	 * @param importType
	 */
	static void put(final Identifier id, final String xmlId,
			final String importType) {
		put(id, xmlId, importType, KeyState.NEW);
	}

	/**
	 * @param id
	 * @param xmlId
	 * @param importType
	 * @param state
	 */
	static void put(final Identifier id, final String xmlId,
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

		final String oldXmlId = FORWARD_MAP.put(new Key(id, importType, state), xmlId);
		final Identifier oldId = REVERSE_MAP.put(new XmlKey(xmlId, importType, state), id);
		
		if (oldXmlId != null) {
			final String xmlIdStringValue = xmlId;
			final String oldXmlIdStringValue = oldXmlId;
			if (!xmlIdStringValue.equals(oldXmlIdStringValue)) {
				throw new IllegalStateException(
						"Such situations are currently unsupported: oldXmlId = String(``"
						+ oldXmlIdStringValue
						+ "''); xmlId = String(``"
						+ xmlIdStringValue + "'')");
			}
		} else if (oldId != null && !id.equals(oldId)) {
			throw new IllegalStateException(
					"Such situations are currently unsupported: oldId = Identifier(``"
					+ oldId.getIdentifierString()
					+ "''); id = Identifier(``"
					+ id.getIdentifierString() + "'')"); 
		}
		
//		When working with a large amount of objects ( ~ 26k ) this code
//		results in significant performance degradation blia (w/o any cunt).	
//		assert Log.debugMessage("FORWARD_MAP " + FORWARD_MAP.values(), Log.DEBUGLEVEL09);
//		assert Log.debugMessage("REVERSE_MAP " + REVERSE_MAP.values(), Log.DEBUGLEVEL09);

	}

	/**
	 * @param id
	 * @param importType
	 */
	static String get(final Identifier id, final String importType) {
		if (id == null) {
			throw new NullPointerException("id is null");
		} else if (importType == null || importType.length() == 0) {
			throw new NullPointerException("importType is either null or empty");
		}

		final String xmlId = FORWARD_MAP.get(new Key(id, importType));
		if (xmlId == null) {
			throw new NoSuchElementException(
					"No forward mapping found for id = Identifier(``"
					+ id.getIdentifierString()
					+ "''), importType = ``"
					+ importType + "''");
		}
		
		assert Log.debugMessage("id:" + id 
			+ ", importType:" + importType
			+ ", xmlId:" + xmlId, Log.DEBUGLEVEL09);
		
		return xmlId;
	}

	/**
	 * @param xmlId
	 * @param importType
	 */
	static Identifier get(final String xmlId, final String importType) {
		if (xmlId == null) {
			throw new NullPointerException("xmlId is null");
		} else if (importType == null || importType.length() == 0) {
			throw new NullPointerException("importType is either null or empty");
		}

		final Identifier id = REVERSE_MAP.get(new XmlKey(xmlId, importType));
		if (id == null) {
			throw new NoSuchElementException(
					"No reverse mapping found for xmlId = String(``"
					+ xmlId
					+ "''), importType = ``"
					+ importType + "''");
		}
		
		assert Log.debugMessage("xmlId:" + xmlId 
			+ ", importType:" + importType
			+ ", id:" + id , Log.DEBUGLEVEL09);
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

		final String xmlId = FORWARD_MAP.get(new Key(id, importType));
		if (xmlId == null) {
			final long nanos = System.nanoTime();
			for (final Entry<XmlKey, Identifier> entry : REVERSE_MAP.entrySet()) {
				if (entry.getKey().getImportType().equals(importType)
						&& entry.getValue().equals(id)) {
					throw new IllegalStateException(
							"Forward map contains no key while reverse map contains a value: id = Identifier(``"
							+ id.getIdentifierString()
							+ "''), importType = ``"
							+ importType + "''");
				}
			}
			registerDebugOverhead(System.nanoTime() - nanos);

			return false;
		}
		final Identifier id2 = REVERSE_MAP.get(new XmlKey(xmlId, importType));
		if (!id2.equals(id)) {
			throw new IllegalStateException(
					"Both forward and reverse mappings are present, but they do not match: id = Identifier(``"
					+ id.getIdentifierString()
					+ "''), id2 = Identifier(``"
					+ id2.getIdentifierString()
					+ "''), xmlId = String(``"
					+ xmlId
					+ "''), importType = ``"
					+ importType + "''");
		}
		return true;
	}

	/**
	 * @param xmlId
	 * @param importType
	 */
	static boolean contains(final String xmlId, final String importType) {
		if (xmlId == null) {
			throw new NullPointerException("xmlId is null");
		} else if (importType == null || importType.length() == 0) {
			throw new NullPointerException("importType is either null or empty");
		}

		final Identifier id = REVERSE_MAP.get(new XmlKey(xmlId, importType));
		final String xmlIdStringValue = xmlId;
		if (id == null) {
			final long nanos = System.nanoTime();
			for (final Entry<Key, String> entry : FORWARD_MAP.entrySet()) {
				final Key key = entry.getKey();
				final String value = entry.getValue();
				if (key.getImportType().equals(importType)
						&& value.equals(xmlIdStringValue)) {
					throw new IllegalStateException(
							"Reverse map contains no key while forward map contains a valuexmlId = String(``"
							+ xmlIdStringValue
							+ "''), importType = ``"
							+ importType + "''");
				}
			}
			registerDebugOverhead(System.nanoTime() - nanos);

			return false;
		}
		final String xmlId2 = FORWARD_MAP.get(new Key(id, importType));
		final String xmlId2StringValue = xmlId2;
		if (!xmlId2StringValue.equals(xmlIdStringValue)) {
			throw new IllegalStateException(
					"Both forward and reverse mappings are present, but they do not match: xmlId = String(``"
					+ xmlIdStringValue
					+ "''), xmlId2 = String(``"
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
		final String xmlIdStringValue = xmlId.getStringValue();
		final XmlKey xmlKey = new XmlKey(xmlIdStringValue, importType);
		final Identifier id = REVERSE_MAP.remove(xmlKey);
		final Key key = new Key(id, importType);
		final String xmlId2 = FORWARD_MAP.remove(key);
		final String xmlId2StringValue = xmlId2;
		if (!xmlId2StringValue.equals(xmlIdStringValue)) {
			throw new IllegalStateException(
					"Both forward and reverse mappings are present, but they do not match: xmlId = String(``"
					+ xmlIdStringValue
					+ "''), xmlId2 = String(``"
					+ xmlId2StringValue
					+ "''), id = Identifier(``"
					+ id.getIdentifierString()
					+ "''), importType = ``"
					+ importType + "''");
		}
		KEYS_TO_DELETE.add(key);
	}

	/**
	 * @param id
	 * @param importType
	 */
	public static void remove(final Identifier id, final String importType) {
		final Key key = new Key(id, importType);
		final String xmlId = FORWARD_MAP.remove(key);
		final XmlKey xmlKey = new XmlKey(xmlId, importType);
		final Identifier id2 = REVERSE_MAP.remove(xmlKey);
		if (!id2.equals(id)) {
			throw new IllegalStateException(
					"Both forward and reverse mappings are present, but they do not match: id = Identifier(``"
					+ id.getIdentifierString()
					+ "''), id2 = Identifier(``"
					+ id2.getIdentifierString()
					+ "''), xmlId = String(``"
					+ xmlId
					+ "''), importType = ``"
					+ importType + "''");
		}
		XML_KEYS_TO_DELETE.add(xmlKey);
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

			try {
				XmlIdentifierDatabase.retrievePrefetchedMap(importType);
			} catch (final RetrieveObjectException roe) {
				assert Log.errorMessage(roe);
				return;
			}
			PREFETCHED_IMPORT_TYPES.add(importType);
		}
	}

	public static void flush() {
		final Map<Key, String> keysToCreate = new HashMap<Key, String>();
		for (final Key key : FORWARD_MAP.keySet()) {
			final KeyState state = key.getState();
			if (state.equals(KeyState.NEW)) {
				keysToCreate.put(key, FORWARD_MAP.get(key));
				key.setState(KeyState.UP_TO_DATE);
			}
		}
		
		for (final XmlKey xmlKey : REVERSE_MAP.keySet()) {
			final KeyState state = xmlKey.getState();
			if (state.equals(KeyState.NEW)) {
				keysToCreate.put(new Key(REVERSE_MAP.get(xmlKey), xmlKey.getImportType()), xmlKey.getXmlId());
				xmlKey.setState(KeyState.UP_TO_DATE);
			}
		}

		/*
		 * Since certain pairs in (KEYS_TO_DELETE or XML_KEYS_TO_DELETE)
		 * and keysToCreate may correspond to the same PK or UK values,
		 * we need to perform a deletion first and only then insert new
		 * records into the database (bug #150).
		 */
		XmlIdentifierDatabase.removeKeys(KEYS_TO_DELETE);
		KEYS_TO_DELETE.clear();
		XmlIdentifierDatabase.removeXmlKeys(XML_KEYS_TO_DELETE);
		XML_KEYS_TO_DELETE.clear();

		try {
			XmlIdentifierDatabase.insertKeys(keysToCreate);
		} catch (final CreateObjectException coe) {
			assert Log.errorMessage(coe);
		}
	}

	static volatile long totalDebugOverheadNanos;

	static {
		Runtime.getRuntime().addShutdownHook(new Thread("LocalXmlIdentifierPool -- debug overhead meter") {
			@Override
			public void run() {
				assert Log.debugMessage("LocalXmlIdentifierPool | additional sanity checks took "
								+ totalDebugOverheadNanos
								+ " nanosecond(s) in total",
						INFO);
			}
		});
	}

	private static void registerDebugOverhead(final long nanos) {
		totalDebugOverheadNanos += nanos;
	}

	/**
	 * @author Maxim Selivanov
	 * @author $Author: bass $
	 * @version $Revision: 1.20 $, $Date: 2005/10/30 15:20:42 $
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
	 * @version $Revision: 1.20 $, $Date: 2005/10/30 15:20:42 $
	 * @module general
	 */
	static final class Key extends State {
		private Identifier id;

		private String importType;

		private int hashCode = 0;
		
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
			if (this.hashCode == 0) {
				this.hashCode = 37 * 17 + this.id.hashCode();
				this.hashCode = 37 * this.hashCode + this.importType.hashCode();
			}
			return this.hashCode;
		}
	}
	
	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.20 $, $Date: 2005/10/30 15:20:42 $
	 * @module general
	 */
	static final class XmlKey extends State {
		private String xmlId;

		private String importType;

		private int hashCode = 0;
		
		String getXmlId() {
			return this.xmlId;
		}
		
		String getImportType() {
			return this.importType;
		}

		/**
		 * @param xmlId
		 * @param importType
		 */
		XmlKey(final String xmlId, final String importType) {
			this(xmlId, importType, KeyState.NEW);
		}

		/**
		 * @param xmlId
		 * @param importType
		 * @param state
		 */
		XmlKey(final String xmlId, final String importType, final KeyState state) {
			if (xmlId == null) {
				throw new NullPointerException("id is null");
			} else if (importType == null || importType.length() == 0) {
				throw new NullPointerException("importType is either null or empty");
			}

			this.xmlId = xmlId;
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
				return this.xmlId.equals(that.xmlId)
						&& this.importType.equals(that.importType);
			}
			return false;
		}
		
		/**
		 * @see Object#hashCode()
		 */
		@Override
		public int hashCode() {
			if (this.hashCode == 0) {
				this.hashCode = 37 * 17 + this.xmlId.hashCode();
				this.hashCode = 37 * this.hashCode + this.importType.hashCode();
			}
			return this.hashCode;
		}
	}
}
