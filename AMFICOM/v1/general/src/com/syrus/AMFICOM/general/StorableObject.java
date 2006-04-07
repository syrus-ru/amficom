/*-
 * $Id: StorableObject.java,v 1.148.2.5 2006/04/04 09:01:40 arseniy Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.general;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECTS_NOT_OF_THE_SAME_ENTITY;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECTS_NOT_OF_THE_SAME_GROUP;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_STATE_ILLEGAL;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.StorableObjectVersion.ILLEGAL_VERSION;
import static com.syrus.AMFICOM.general.StorableObjectVersion.INITIAL_VERSION;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.SOURCE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;

import java.io.Serializable;
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
import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.bugs.Crutch134;
import com.syrus.AMFICOM.general.corba.IdlCreateObjectException;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.general.corba.IdlStorableObjectHelper;
import com.syrus.AMFICOM.general.xml.XmlIdentifier;
import com.syrus.util.Log;
import com.syrus.util.Wrapper;
import com.syrus.util.LRUMap.Retainable;
import com.syrus.util.transport.idl.IdlTransferableObjectExt;

/**
 * @version $Revision: 1.148.2.5 $, $Date: 2006/04/04 09:01:40 $
 * @author $Author: arseniy $
 * @author Tashoyan Arseniy Feliksovich
 * @module general
 */
public abstract class StorableObject implements Identifiable, Retainable, Serializable {
	private static final long serialVersionUID = 3904998894075738999L;

	protected Identifier id;
	protected Date created;
	protected Identifier creatorId;
	protected Date modified;
	protected Identifier modifierId;
	protected StorableObjectVersion version;

	private boolean changed;

	private boolean deleted;

	private Date savedModified;
	private Identifier savedModifierId;
	private StorableObjectVersion savedVersion;

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected StorableObject(/*IdlStorableObject*/) {
		this.changed = false;
		this.deleted = false;
	}

	/**
	 * Constructor used by clients (via {@code createInstance(...)} methods)
	 * or SQL database drivers (directly, see {@link
	 * StorableObjectDatabase#updateEntityFromResultSet(StorableObject, java.sql.ResultSet)}).
	 *
	 * @param id
	 * @param created my be {@code null}. In this case, current date will be used.
	 * @param modified my be {@code null}. In this case, current date will be used.
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @see StorableObjectDatabase#updateEntityFromResultSet(StorableObject, java.sql.ResultSet)
	 */
	protected StorableObject(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version) {
		this.id = id;
		this.created = new Date(created == null ? System.currentTimeMillis() : created.getTime());
		this.modified = new Date(modified == null ? System.currentTimeMillis() : modified.getTime());
		this.creatorId = creatorId;
		this.modifierId = modifierId;
		this.version = version;

		this.changed = false;
		this.deleted = false;

		this.savedModified = null;
		this.savedModifierId = null;
		this.savedVersion = ILLEGAL_VERSION;
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
				created, // Not a bug, though Date objects are mutable:
				created, // two separate objects are created in underlying ctor.
				creatorId,
				creatorId,
				INITIAL_VERSION);
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 *
	 * <p>
	 * Non-synchronized.
	 * Non-overriding.
	 * Non-overridable.
	 * </p>
	 *
	 * @param transferable
	 */
	protected final void fromIdlTransferable(final IdlStorableObject transferable) {
		this.id = Identifier.valueOf(transferable.id);
		this.created = new Date(transferable.created);
		this.modified = new Date(transferable.modified);
		this.creatorId = Identifier.valueOf(transferable.creatorId);
		this.modifierId = Identifier.valueOf(transferable.modifierId);
		this.version = StorableObjectVersion.valueOf(transferable.version);

		this.changed = false;

		this.savedModified = null;
		this.savedModifierId = null;
		this.savedVersion = ILLEGAL_VERSION;		
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 *
	 * <p>This method <em>may</em> return {@code true} even if the bean is invalid; however,
	 * since it&apos;s being primarily used in assertions, its behaviour is quite correct when
	 * assertions enabled: an {@link AssertionError} is thrown upon bean invalidity. Thus,
	 * {@code boolean} return type better serves for assertion chaining opportunity, and the
	 * value returned may not reflect bean&apos;s effective validity state (and implementation
	 * may eventually be rewritten to <em>always</em> return {@code true}).</p>
	 *
	 * @return {@code true} if the bean is valid; otherwise, depending on the implementation,
	 *         may <em>either</em> return {@code false} <em>or</em> throw an
	 *         {@link AssertionError} (assertions enabled) or silently return {@code true}
	 *         (assertions disabled).
	 * @throws AssertionError if the bean is invalid (optional).
	 */
	protected boolean isValid() {
		return !(this.id == null || this.created == null || this.modified == null || this.creatorId == null || this.modifierId == null);
	}

	public final Date getCreated() {
		return (Date) this.created.clone();
	}

	public final Identifier getCreatorId() {
		return this.creatorId;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 * @return <code>Set</code> of {@link Identifiable} - dependencies of this object.
	 */
	public final Set<Identifiable> getDependencies() {
		assert this.isValid() : OBJECT_STATE_ILLEGAL;

		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.addAll(this.getDependenciesTmpl());
		dependencies.add(this.creatorId);
		dependencies.add(this.modifierId);

		/*NOTE: this.equals(this.id)*/
		dependencies.remove(this.id);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);

		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * Must be overridden by descendants.
	 */
	protected abstract Set<Identifiable> getDependenciesTmpl();

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	protected IdlStorableObject getIdlTransferable(final ORB orb) {
		return IdlStorableObjectHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue());
	}

	/**
	 * @see Identifiable#getId()
	 */
	public final Identifier getId() {
		return this.id;
	}

	public final Date getModified() {
		return (Date) this.modified.clone();
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

	protected final void setUpdated(final Identifier modifierId) {
		this.savedModified = this.modified;
		this.savedModifierId = this.modifierId;
		this.savedVersion = this.version;

		this.modified = new Date(System.currentTimeMillis());
		this.modifierId = modifierId;
		this.version = this.version.increment();
		this.changed = false;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected final void cleanupUpdate() {
		this.savedModified = null;
		this.savedModifierId = null;
		this.savedVersion = ILLEGAL_VERSION;
	}

	/**
	 * <p><b>Clients must never explicitly call this method.</b></p>
	 */
	protected final void rollbackUpdate() {
		if (this.savedModified == null || this.savedModifierId == null || this.savedVersion == ILLEGAL_VERSION) {
			Log.errorMessage("Cannot rollback update of object: '" + this.id + "', entity: '" + ObjectEntities.codeToString(this.id.getMajor())
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
		this.created = new Date(created.getTime());
		this.modified = new Date(modified.getTime());
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
	protected StorableObject clone() throws CloneNotSupportedException {
		final StorableObject clone = (StorableObject) super.clone();
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
		clone.version = INITIAL_VERSION;

		clone.changed = false;
		clone.markAsChanged();

		clone.deleted = false;

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
		assert storableObjects != null : NON_NULL_EXPECTED;

		final IdlStorableObject[] transferables = new IdlStorableObject[storableObjects.size()];
		int i = 0;
		synchronized (storableObjects) {
			for (final StorableObject storableObject : storableObjects) {
				if (!(storableObject instanceof IdlTransferableObjectExt)) {
					Log.debugMessage(storableObject.getClass().getName() + " doesn't support IDL import/export", SEVERE);
					continue;
				}
				IdlTransferableObjectExt<?> pureJava = (IdlTransferableObjectExt) storableObject;
				final IDLEntity transferable = pureJava.getIdlTransferable(orb);
				if (!(transferable instanceof IdlStorableObject)) {
					Log.debugMessage(transferable.getClass().getName() + " isn't castable to IdlStorableObject", SEVERE);
					continue;
				}
				transferables[i++] = (IdlStorableObject) transferable;
			}
		}
		return transferables;
	}

	@SuppressWarnings("unchecked")
	public static final <T extends StorableObject> Set<T> fromTransferables(final IdlStorableObject[] storableObjectsT) {
		assert storableObjectsT != null : NON_NULL_EXPECTED;

		final Set<T> storableObjects = new HashSet<T>();
		for (final IdlStorableObject idlStorableObject : storableObjectsT) {
			try {
				storableObjects.add((T) idlStorableObject.getNative());
			} catch (final IdlCreateObjectException coe) {
				Log.errorMessage(coe.detailMessage);
				Log.errorMessage(coe);
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

		if (identifiables.size() <= 1) {
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
		if (length <= 1) {
			return true;
		}

		short entityCode = VOID_IDENTIFIER.getMajor();
		int i = 0;
		for (; i < length; i++) {
			final Identifier id = Identifier.valueOf(ids[i]);
			if (id.isVoid()) {
				continue;
			}
			entityCode = id.getMajor();
			break;
		}
		for (; i < length; i++) {
			final Identifier id = Identifier.valueOf(ids[i]);
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

		if (identifiables.size() <= 1) {
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
		assert identifiables != null && !identifiables.isEmpty() : NON_EMPTY_EXPECTED;
		assert hasSingleTypeEntities(identifiables) : OBJECTS_NOT_OF_THE_SAME_ENTITY;

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
	public String toString() {
		return '{' + this.id.toString()
				+ "; changed: " + this.isChanged()  + '}';
	}

	protected abstract StorableObjectWrapper<?> getWrapper();

	public final Object getValue(final String key) {
		@SuppressWarnings("unchecked")
		final StorableObjectWrapper wrapper = this.getWrapper();
		@SuppressWarnings("unchecked")
		final Object value = wrapper.getValue(this, key);
		return value;
	}

	/**
	 * @see #getEntityCodeOfIdentifiables(Set)
	 */
	public static final short getEntityCodeOfIdentifiables(final IdlIdentifier[] ids) {
		assert ids != null && ids.length != 0;
		assert hasSingleTypeEntities(ids) : OBJECTS_NOT_OF_THE_SAME_ENTITY;

		for (final IdlIdentifier idlIdentifier : ids) {
			final Identifier id = Identifier.valueOf(idlIdentifier);
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
		assert hasSingleGroupEntities(identifiables) : OBJECTS_NOT_OF_THE_SAME_GROUP;

		return ObjectGroupEntities.getGroupCode(identifiables.iterator().next().getId().getMajor());
	}

	public static <T extends StorableObject, V> Map<Identifier, V> createValuesMap(final Set<T> storableObjects, final String key) {
		assert storableObjects != null : NON_NULL_EXPECTED;
		assert !storableObjects.isEmpty() : NON_EMPTY_EXPECTED;
		assert hasSingleTypeEntities(storableObjects) :  OBJECTS_NOT_OF_THE_SAME_ENTITY;

		final Wrapper storableObjectWrapper = storableObjects.iterator().next().getWrapper();
		final Map<Identifier, V> valuesMap = new HashMap<Identifier, V>();
		for (final T storableObject : storableObjects) {
			final V value = (V) storableObjectWrapper.getValue(storableObject, key);
			valuesMap.put(storableObject.getId(), value);
		}

		return valuesMap;
	}

	/*-********************************************************************
	 * Caching sub-framework.                                             *
	 **********************************************************************/

	private static final String KEY = "amficom.sof.cache.on.modification";

	private static final String DEFAULT_VALUE = "false";

	static {
		Log.debugMessage(KEY + '=' + System.getProperty(KEY, DEFAULT_VALUE), INFO);
	}

	protected static boolean buildCacheOnModification() {
		return Boolean.parseBoolean(System.getProperty(KEY, DEFAULT_VALUE));
	}

	/**
	 * <p>This class shouldn&apos;t have been declared static since it&apos;s
	 * not referenced within a static context anywhere, unless an ugly
	 * fucking bug in javac (up to version 1.5.0_05):</p>
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
	 * @author $Author: arseniy $
	 * @version $Revision: 1.148.2.5 $, $Date: 2006/04/04 09:01:40 $
	 * @module general
	 */
	@Crutch134(notes = "This class should be made final.")
	protected static class StorableObjectContainerWrappee<T extends StorableObject> {
		private static final long serialVersionUID = -1264974065379428032L;

		private boolean cacheBuilt = false;

		private StorableObjectCondition condition;

		private Set<Identifier> containeeIds;

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
				this.containeeIds.add(containee.getId());
			} else if (buildCacheOnModification()) {
				this.ensureCacheBuilt(usePool);

				this.containeeIds.add(containee.getId());
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
				this.containeeIds.remove(containee);
			} else if (buildCacheOnModification()) {
				this.ensureCacheBuilt(usePool);

				this.containeeIds.remove(containee);
			}
		}

		/**
		 * <p>Returns a set of {@code StorableObject}s corresponding to cached ids, removing
		 * from ids those conforming to deleted objects.</p>
		 * 
		 * <p>This call returns a new {@code Set} instance on every invocation. The set
		 * returned is mutable; however, any modifications made to it do not get reflected
		 * in cache&apos;s internal state.</p>
		 *
		 * @param usePool
		 * @throws ApplicationException
		 */
		@Crutch134(notes = "Remove the final modifier: containing class itself should be final.")
		public final Set<T> getContainees(final boolean usePool)
		throws ApplicationException {
			this.ensureCacheBuilt(usePool);
			synchronized (this.containeeIds) {
				final Set<T> containees = new HashSet<T>(StorableObjectPool.<T>getStorableObjects(this.containeeIds, this.useLoader()));
				this.containeeIds.retainAll(containees);
				return containees;
			}
		}

		/**
		 * @param usePool
		 * @throws ApplicationException
		 */
		private void ensureCacheBuilt(final boolean usePool)
		throws ApplicationException {
			synchronized (this) {
				if (!this.cacheBuilt || usePool) {
					if (this.containeeIds == null) {
						this.containeeIds = Collections.synchronizedSet(new HashSet<Identifier>());
					} else {
						this.containeeIds.clear();
					}
					this.containeeIds.addAll(StorableObjectPool.getIdentifiersByCondition(this.condition, this.useLoader()));
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
	 * @author $Author: arseniy $
	 * @version $Revision: 1.148.2.5 $, $Date: 2006/04/04 09:01:40 $
	 * @module general
	 */
	@Retention(SOURCE)
	@Target(METHOD)
	protected static @interface ParameterizationPending {
		String[] value();
	}

	/**
	 * @see com.syrus.util.LRUMap.Retainable#retain()
	 */
	public boolean retain() {
		return this.isChanged();
	}
}
