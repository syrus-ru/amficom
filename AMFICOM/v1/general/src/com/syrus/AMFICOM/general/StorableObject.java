/*-
 * $Id: StorableObject.java,v 1.125 2005/10/30 15:20:43 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.PERSISTENCE_COUNTER_NEGATIVE;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;
import static java.util.logging.Level.INFO;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.bugs.Crutch134;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectHelper;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;
import com.syrus.util.TransferableObject;

/**
 * @version $Revision: 1.125 $, $Date: 2005/10/30 15:20:43 $
 * @author $Author: bass $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public abstract class StorableObject<T extends StorableObject<T>> implements Identifiable,
		TransferableObject<IdlStorableObject> {
	private static final long serialVersionUID = 3904998894075738999L;

	protected Identifier id;
	protected Date created;
	protected Identifier creatorId;
	protected Date modified;
	protected Identifier modifierId;
	protected StorableObjectVersion version;

	private boolean changed;

	private boolean deleted;

	private transient volatile int cachedTimes;

	private Date savedModified;
	private Identifier savedModifierId;
	private StorableObjectVersion savedVersion;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected StorableObject(/*IdlStorableObject*/) {
		this.changed = false;
		this.deleted = false;
		this.cachedTimes = 0;
	}

	/**
	 * Client-side constructor.
	 *
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	protected StorableObject(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version) {
		this.id = id;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;

		this.changed = false;
		this.deleted = false;
		this.cachedTimes = 0;

		this.savedModified = null;
		this.savedModifierId = null;
		this.savedVersion = StorableObjectVersion.ILLEGAL_VERSION;
	}

	/**
	 * Minimalistic constructor used when importing from XML.
	 *
	 * @param id
	 * @param importType
	 * @param entityCode
	 * @param created
	 * @param creatorId
	 * @throws IdentifierGenerationException
	 */
	protected StorableObject(final XmlIdentifier id,
			final String importType,
			final short entityCode,
			final Date created,
			final Identifier creatorId) throws IdentifierGenerationException {
		this(Identifier.fromXmlTransferable(id, importType, entityCode),
				created,
				created,
				creatorId,
				creatorId,
				StorableObjectVersion.createInitial());
	}

	/**
	 *
	 * Will be overridden by descendants.
	 *
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @throws ApplicationException
	 */
	@SuppressWarnings("unused")
	protected synchronized void fromTransferable(final IdlStorableObject transferable) throws ApplicationException {
		this.id = new Identifier(transferable.id);
		this.created = new Date(transferable.created);
		this.modified = new Date(transferable.modified);
		this.creatorId = new Identifier(transferable.creatorId);
		this.modifierId = new Identifier(transferable.modifierId);
		this.version = new StorableObjectVersion(transferable.version);

		this.changed = false;

		this.savedModified = null;
		this.savedModifierId = null;
		this.savedVersion = StorableObjectVersion.ILLEGAL_VERSION;		
	}
	
	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @return <code>true</code> if storable object is valid, <code>false</code> otherwise
	 */
	protected boolean isValid() {
		return !(this.id == null || this.created == null || this.modified == null || this.creatorId == null || this.modifierId == null);
	}

	public final Date getCreated() {
		return this.created;
	}

	public final Identifier getCreatorId() {
		return this.creatorId;
	}

	/**
	 * Will be overridden by descendants.
	 *
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	public abstract Set<Identifiable> getDependencies();

	/**
	 * @param orb
	 * @see com.syrus.util.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	public IdlStorableObject getTransferable(final ORB orb) {
		return IdlStorableObjectHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
				this.version.longValue());
	}

	/**
	 * @see Identifiable#getId()
	 */
	public final Identifier getId() {
		return this.id;
	}

	public final Date getModified() {
		return this.modified;
	}

	public final Identifier getModifierId() {
		return this.modifierId;
	}

	public final StorableObjectVersion getVersion() {
		return this.version;
	}

	/**
	 * Returns <code>true</code> if object was changed locally (with respect
	 * to server).
	 */
	public final boolean isChanged() {
		return this.changed;
	}

	final boolean isDeleted() {
		return this.deleted;
	}

	/**
	 * @return {@code true} if not only pool holds a reference to this
	 *         object, but also some external chache, and the object thus
	 *         should never be squeezed out of the pool. 
	 */
	final boolean isPersistent() {
		assert this.cachedTimes >= 0;
		return this.cachedTimes > 0;
	}

	/**
	 * This method is called in:
	 * 1) all setters of a StorableObject
	 * 2) static method createInstance of StorableObject
	 * i. e., in all methods, which change state of an object.
	 * Subsequent call to StorableObjectPool.flush will save this changed object.
	 *
	 */
	protected final void markAsChanged() {
		if (!this.changed) {
			this.changed = true;
			try {
				StorableObjectPool.putStorableObject(this);
			} catch (final IllegalObjectEntityException ioee) {
				assert false : ioee.getMessage();
			}
		}
	}

	final void markAsDeleted() {
		this.deleted = true;
	}

	/**
	 * Is invoked solely by caching facilities.
	 */
	final void markAsPersistent() {
		if (!this.isPersistent()) {
			try {
				StorableObjectPool.putStorableObject(this);
			} catch (final IllegalObjectEntityException ioee) {
				assert false : ioee.getMessage();
			}
		}
		this.cachedTimes++;
	}

	/**
	 * Is invoked solely by caching facilities.
	 */
	final void cleanupPersistence() {
		assert this.isPersistent() : PERSISTENCE_COUNTER_NEGATIVE + this
				+ "; cached " + (this.cachedTimes - 1) + " time(s)";
		this.cachedTimes--;
	}

	protected final void setUpdated(final Identifier modifierId) {
		this.savedModified = this.modified;
		this.savedModifierId = this.modifierId;
		this.savedVersion = this.version.clone();

		this.modified = new Date(System.currentTimeMillis());
		this.modifierId = modifierId;
		this.version.increment();
		this.changed = false;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected final void cleanupUpdate() {
		this.savedModified = null;
		this.savedModifierId = null;
		this.savedVersion = StorableObjectVersion.ILLEGAL_VERSION;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected final void rollbackUpdate() {
		if (this.savedModified == null || this.savedModifierId == null || this.savedVersion == StorableObjectVersion.ILLEGAL_VERSION) {
			assert Log.errorMessage("Cannot rollback update of object: '" + this.id + "', entity: '" + ObjectEntities.codeToString(this.id.getMajor())
					+ "' -- saved values are in illegal states!");
			return;
		}

		this.modified = this.savedModified;
		this.modifierId = this.savedModifierId;
		this.version = this.savedVersion;
		this.changed = true;

		this.cleanupUpdate();
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 *
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	protected final synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version) {
		assert created != null && modified != null && creatorId != null && modifierId != null;
		this.created = created;
		this.modified = modified;
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;
	}

	/**
	 * @param xmlId
	 * @param importType
	 */
	protected final void insertXmlMapping(final XmlIdentifier xmlId,
			final String importType) {
		LocalXmlIdentifierPool.put(this.id, xmlId.getStringValue(), importType);
	}

	/**
	 * for descendsants that inherit {@link CloneableStorableObject} and
	 * thus <em>do support</em> cloning, {@link #creatorId} and
	 * {@link #modifierId} of a newly created object are set to the same
	 * values as those of the original one. 
	 *
	 * @throws CloneNotSupportedException
	 * @see Object#clone()
	 */
	@Override
	protected T clone() throws CloneNotSupportedException {
		@SuppressWarnings("unchecked")
		final T clone = (T) super.clone();
		try {
			clone.id = IdentifierPool.getGeneratedIdentifier(this.id.getMajor());
		} catch (final IdentifierGenerationException ige) {
			final CloneNotSupportedException cnse = new CloneNotSupportedException();
			cnse.initCause(ige);
			throw cnse;
		}

		final Date cloneCreated = new Date();
		clone.created = cloneCreated;
		clone.modified = cloneCreated;

		/*
		 * Initialize version vith 0L, like for all newly created
		 * objects.
		 */
		clone.version = StorableObjectVersion.createInitial();

		clone.changed = false;
		clone.markAsChanged();

		clone.deleted = false;

		clone.cachedTimes = 0;

		return clone;
	}

	/**
	 * @param that
	 * @see Object#equals(Object)
	 */
	@Override
	public final boolean equals(final Object that) {
		return this.id.equals(that);
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return this.id.hashCode();
	}

	public static final IdlStorableObject[] createTransferables(final Set<? extends StorableObject> storableObjects, final ORB orb) {
		assert storableObjects != null : ErrorMessages.NON_NULL_EXPECTED;

		final IdlStorableObject[] transferables = new IdlStorableObject[storableObjects.size()];
		int i = 0;
		synchronized (storableObjects) {
			for (final StorableObject storableObject : storableObjects) {
				transferables[i++] = storableObject.getTransferable(orb);
			}
		}
		return transferables;
	}

	@SuppressWarnings("unchecked")
	public static final <T extends StorableObject> Set<T> fromTransferables(final IdlStorableObject[] storableObjectsT) {
		assert storableObjectsT != null : ErrorMessages.NON_NULL_EXPECTED;

		final Set<T> storableObjects = new HashSet<T>();
		for (final IdlStorableObject idlStorableObject : storableObjectsT) {
			try {
				storableObjects.add((T) idlStorableObject.getNative());
			} catch (IdlCreateObjectException coe) {
				assert Log.errorMessage(coe);
			}
		}
		return storableObjects;
	}

	public static final Map<Identifier, StorableObjectVersion> createVersionsMap(final Set<? extends StorableObject> storableObjects) {
		final Map<Identifier, StorableObjectVersion> versionsMap = new HashMap<Identifier, StorableObjectVersion>();
		synchronized (storableObjects) {
			for (final StorableObject storableObject : storableObjects) {
				versionsMap.put(storableObject.id, storableObject.version);
			}
		}
		return versionsMap;
	}

	/**
	 * This method should only be invoked during assertion evaluation, and
	 * never in a release system.
	 *
	 * @param identifiables non-null set of identifiables (empty set is ok).
	 * @return <code>true</code> if all entities within this set are of
	 *         the same type, <code>false</code> otherwise.
	 */
	public static final boolean hasSingleTypeEntities(final Set<? extends Identifiable> identifiables) {
		/*
		 * Nested assertions are ok.
		 */
		assert identifiables != null;

		if (identifiables.isEmpty()) {
			return true;
		}

		short entityCode = VOID_IDENTIFIER.getMajor();
		synchronized (identifiables) {
			final Iterator<? extends Identifiable> identifiableIterator = identifiables.iterator();
			while (identifiableIterator.hasNext()) {
				final Identifier id = identifiableIterator.next().getId();
				if (id.isVoid()) {
					continue;
				}
				entityCode = id.getMajor();
				break;
			}
			while (identifiableIterator.hasNext()) {
				final Identifier id = identifiableIterator.next().getId();
				if (id.isVoid()) {
					continue;
				}
				if (entityCode != id.getMajor()) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @see #hasSingleTypeEntities(Set)
	 */
	public static final boolean hasSingleTypeEntities(final IdlIdentifier[] ids) {
		assert ids != null;

		final int length = ids.length;
		if (length == 0) {
			return true;
		}

		short entityCode = VOID_IDENTIFIER.getMajor();
		int i = 0;
		for (; i < length; i++) {
			final Identifier id = new Identifier(ids[i]);
			if (id.isVoid()) {
				continue;
			}
			entityCode = id.getMajor();
			break;
		}
		for (; i < length; i++) {
			final Identifier id = new Identifier(ids[i]);
			if (id.isVoid()) {
				continue;
			}
			if (entityCode != id.getMajor()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * This method should only be invoked during assertion evaluation, and
	 * never in a release system.
	 *
	 * @param identifiables non-null set of identifiables (empty set is ok).
	 * @return <code>true</code> if all entities within this set belong to
	 *         the same group, <code>false</code> otherwise.
	 */
	public static final boolean hasSingleGroupEntities(final Set<? extends Identifiable> identifiables) {
		/*
		 * Nested assertions are ok.
		 */
		assert identifiables != null;

		if (identifiables.isEmpty()) {
			return true;
		}

		synchronized (identifiables) {
			final Iterator<? extends Identifiable> identifiableIterator = identifiables.iterator();
			final short groupCode = ObjectGroupEntities.getGroupCode(identifiableIterator.next().getId().getMajor());
			while (identifiableIterator.hasNext()) {
				if (groupCode != ObjectGroupEntities.getGroupCode(identifiableIterator.next().getId().getMajor())) {
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * This method checks:
	 * 1) for non-null and non-empty set,
	 * 2) for the same entity code of all elements in set (@see #hasSingleTypeEntities(Set)).
	 *
	 * @param identifiables non-null, non-empty set of storable objects or
	 *        identifiers of the same type.
	 * @return common type of identifiables supplied as
	 *         <code>short</code>.
	 */
	public static final short getEntityCodeOfIdentifiables(final Set<? extends Identifiable> identifiables) {
		assert identifiables != null && !identifiables.isEmpty() : ErrorMessages.NON_EMPTY_EXPECTED;
		assert hasSingleTypeEntities(identifiables) : ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;

		synchronized (identifiables) {
			for (final Identifiable identifiable : identifiables) {
				final Identifier id = identifiable.getId();
				if (id.isVoid()) {
					continue;
				}
				return id.getMajor();
			}
		}
		return VOID_IDENTIFIER.getMajor();
	}
	
	@Override
	public final String toString() {
		return '{' + this.id.toString()
				+ "; changed: " + this.isChanged()
				+ "; persistent: " + this.isPersistent() + '}';
	}

	protected abstract StorableObjectWrapper<T> getWrapper();

	@SuppressWarnings("unchecked")
	public final Object getValue(final String key) {
		return this.getWrapper().getValue((T) this, key);
	}

	/**
	 * @see #getEntityCodeOfIdentifiables(Set)
	 */
	public static final short getEntityCodeOfIdentifiables(final IdlIdentifier[] ids) {
		assert ids != null && ids.length != 0;
		assert hasSingleTypeEntities(ids) : ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;

		for (final IdlIdentifier idlIdentifier : ids) {
			final Identifier id = new Identifier(idlIdentifier);
			if (id.isVoid()) {
				continue;
			}
			return id.getMajor();
		}
		return VOID_IDENTIFIER.getMajor();
	}

	/**
	 * Code that invokes this method, should preliminarily call
	 * {@link #hasSingleGroupEntities(Set)} with the same parameter and
	 * ensure that return value is <code>true</code>, e.g.:
	 *
	 * <pre>
	 * assert hasSingleGroupEntities(identifiables) : &quot;Identifiables of different group should be treated separately...&quot;;
	 * </pre>
	 * @param identifiables non-null, non-empty set of storable objects or
	 *        identifiers of the same type.
	 * @return common group of identifiables supplied as
	 *         <code>short</code>.
	 */
	public static final short getGroupCodeOfIdentifiables(final Set<? extends Identifiable> identifiables) {
		assert identifiables != null && !identifiables.isEmpty();
		assert hasSingleGroupEntities(identifiables) : ErrorMessages.OBJECTS_NOT_OF_THE_SAME_GROUP;

		return ObjectGroupEntities.getGroupCode(identifiables.iterator().next().getId().getMajor());
	}

	/*-********************************************************************
	 * Caching sub-framework.                                             *
	 **********************************************************************/

	private static final String KEY = "amficom.sof.cache.on.modification";

	private static final String DEFAULT_VALUE = "false";

	static {
		assert Log.debugMessage(KEY + '=' + System.getProperty(KEY, DEFAULT_VALUE), INFO);
	}

	protected static boolean buildCacheOnModification() {
		return Boolean.parseBoolean(System.getProperty(KEY, DEFAULT_VALUE));
	}

	/**
	 * This class shouldn&apos;t have been declared static since it&apos;s
	 * not referenced within a static context anywhere, unless an ugly
	 * fucking bug in javac (up to version 1.5.0_05):
	 *
	 * <pre>java.lang.NullPointerException
	 * at com.sun.tools.javac.code.Types$IsSameTypeFcn.visitClassType(Types.java:652)
	 * at com.sun.tools.javac.code.Type$ClassType.accept(Type.java:473)
	 * at com.sun.tools.javac.code.Types$IsSameTypeFcn.isSameType(Types.java:601)
	 * at com.sun.tools.javac.code.Types$IsSameTypeFcn.visitClassType(Types.java:669)
	 * at com.sun.tools.javac.code.Type$ClassType.accept(Type.java:473)
	 * at com.sun.tools.javac.code.Types$IsSameTypeFcn.isSameType(Types.java:601)
	 * at com.sun.tools.javac.code.Types.isSameType(Types.java:591)
	 * at com.sun.tools.javac.code.Types.covariantReturnType(Types.java:2640)
	 * at com.sun.tools.javac.code.Types.resultSubtype(Types.java:2602)
	 * at com.sun.tools.javac.code.Types.returnTypeSubstitutable(Types.java:2610)
	 * at com.sun.tools.javac.code.Symbol$MethodSymbol.overrides(Symbol.java:821)
	 * at com.sun.tools.javac.code.Symbol$MethodSymbol.implementation(Symbol.java:873)
	 * at com.sun.tools.javac.comp.Check.firstUndef(Check.java:1354)
	 * at com.sun.tools.javac.comp.Check.firstUndef(Check.java:1367)
	 * at com.sun.tools.javac.comp.Check.checkAllDefined(Check.java:1319)
	 * at com.sun.tools.javac.comp.Attr.attribClassBody(Attr.java:2436)
	 * at com.sun.tools.javac.comp.Attr.attribClass(Attr.java:2406)
	 * at com.sun.tools.javac.comp.Attr.attribClass(Attr.java:2355)
	 * at com.sun.tools.javac.main.JavaCompiler.compile(JavaCompiler.java:444)
	 * at com.sun.tools.javac.main.Main.compile(Main.java:592)
	 * at com.sun.tools.javac.main.Main.compile(Main.java:544)
	 * at com.sun.tools.javac.Main.compile(Main.java:67)
	 * at com.sun.tools.javac.Main.main(Main.java:52)</pre>
	 *
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.125 $, $Date: 2005/10/30 15:20:43 $
	 * @module general
	 */
	@Crutch134(notes = "This class should be made final.")
	protected static class StorableObjectContainerWrappee<T extends StorableObject> {
		private static final long serialVersionUID = -1264974065379428032L;

		private boolean cacheBuilt = false;

		private StorableObjectCondition condition;

		private Set<T> containees;

		/**
		 * @param wrapper
		 * @param entityCode
		 */
		public StorableObjectContainerWrappee(final StorableObject wrapper,
				final short entityCode) {
			this.condition = new LinkedIdsCondition(wrapper.getId(), entityCode);
		}

		/**
		 * @param containee
		 * @throws ApplicationException
		 */
		@Crutch134(notes = "Remove the final modifier: containing class itself should be final.")
		public final void addToCache(final T containee, final boolean usePool)
		throws ApplicationException {
			if (containee.isDeleted()) {
				return;
			}

			if (this.cacheBuilt) {
				if (!this.containees.contains(containee)) {
					containee.markAsPersistent();
					this.containees.add(containee);
				}
			} else if (buildCacheOnModification()) {
				this.ensureCacheBuilt(usePool);

				if (!this.containees.contains(containee)) {
					containee.markAsPersistent();
					this.containees.add(containee);
				}
			}
		}

		/**
		 * @param containee
		 * @param usePool
		 * @throws ApplicationException
		 */
		@Crutch134(notes = "Remove the final modifier: containing class itself should be final.")
		public final void removeFromCache(final T containee, final boolean usePool)
		throws ApplicationException {
			if (this.cacheBuilt) {
				if (this.containees.contains(containee)) {
					containee.cleanupPersistence();
					this.containees.remove(containee);
				}
			} else if (buildCacheOnModification()) {
				this.ensureCacheBuilt(usePool);

				if (this.containees.contains(containee)) {
					containee.cleanupPersistence();
					this.containees.remove(containee);
				}
			}
		}

		/**
		 * @param usePool
		 * @throws ApplicationException
		 */
		@Crutch134(notes = "Remove the final modifier: containing class itself should be final.")
		public final Set<T> getContainees(final boolean usePool)
		throws ApplicationException {
			this.ensureCacheBuilt(usePool);
			synchronized (this.containees) {
				for (final Iterator<T> containeeIterator = this.containees.iterator(); containeeIterator.hasNext();) {
					if (containeeIterator.next().isDeleted()) {
						containeeIterator.remove();
					}
				}
			}
			return this.containees;
		}

		/**
		 * @param usePool
		 * @throws ApplicationException
		 */
		private void ensureCacheBuilt(final boolean usePool)
		throws ApplicationException {
			synchronized (this) {
				if (!this.cacheBuilt || usePool) {
					if (this.containees == null) {
						this.containees = Collections.synchronizedSet(new HashSet<T>());
					} else {
						synchronized (this.containees) {
							for (final Iterator<T> containeeIterator = this.containees.iterator(); containeeIterator.hasNext();) {
								containeeIterator.next().cleanupPersistence();
								containeeIterator.remove();
							}
						}
					}
					for (final T containee : StorableObjectPool.<T>getStorableObjectsByCondition(this.condition, this.useLoader())) {
						containee.markAsPersistent();
						this.containees.add(containee);
					}
					this.cacheBuilt = true;
				}
			}
		}

		@Crutch134(notes = "Inline")
		protected boolean useLoader() {
			return true;
		}
	}

	/**
	 * @author Andrew ``Bass'' Shcheglov
	 * @author $Author: bass $
	 * @version $Revision: 1.125 $, $Date: 2005/10/30 15:20:43 $
	 * @module general
	 */
	@Retention(SOURCE)
	@Target(METHOD)
	protected static @interface ParameterizationPending {
		String[] value();
	}
}
