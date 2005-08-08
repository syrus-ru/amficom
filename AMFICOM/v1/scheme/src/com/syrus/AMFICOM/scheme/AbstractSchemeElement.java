/*-
 * $Id: AbstractSchemeElement.java,v 1.45 2005/08/08 14:25:23 arseniy Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.Characteristic;
import com.syrus.AMFICOM.general.Characterizable;
import com.syrus.AMFICOM.general.CharacterizableDelegate;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlIdentifier;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.util.Log;

/**
 * This class is never used directly, it was provided just in order for source
 * generated from IDL files to compile cleanly. Use other implementations of
 * {@link AbstractSchemeElement}instead.
 *
 * @author $Author: arseniy $
 * @version $Revision: 1.45 $, $Date: 2005/08/08 14:25:23 $
 * @module scheme
 */
public abstract class AbstractSchemeElement
		extends AbstractCloneableStorableObject
		implements Describable, Characterizable, ReverseDependencyContainer {
	static final long serialVersionUID = 4644766113809681630L;

	private String name;

	private String description;

	/**
	 * @todo It may be necessary to allow accessor and modifier be
	 *       overridden by descendants to add extra checks as SchemeElement
	 *       and SchemeLink may be enclosed not by Scheme only.
	 */
	Identifier parentSchemeId;

	private transient CharacterizableDelegate characterizableDelegate;

	/**
	 * @param id
	 */
	AbstractSchemeElement(final Identifier id) {
		super(id);
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param parentScheme
	 */
	AbstractSchemeElement(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, 
			final StorableObjectVersion version, final String name,
			final String description, final Scheme parentScheme) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.parentSchemeId = Identifier.possiblyVoid(parentScheme);
	}

	/**
	 * Will transmute to the constructor from the corresponding
	 * transferable.
	 */
	AbstractSchemeElement() {
		// super();
	}

	/**
	 * @see Characterizable#getCharacteristics()
	 */
	public Set<Characteristic> getCharacteristics(final boolean usePool) throws ApplicationException {
		if (this.characterizableDelegate == null) {
			this.characterizableDelegate = new CharacterizableDelegate(this.id);
		}
		return this.characterizableDelegate.getCharacteristics(usePool);
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.parentSchemeId != null: OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.parentSchemeId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public final String getDescription() {
		assert this.description != null: OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public final String getName() {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	Identifier getParentSchemeId() {
		assert this.parentSchemeId != null : OBJECT_NOT_INITIALIZED;
		assert this.parentSchemeId.isVoid() || this.parentSchemeId.getMajor() == SCHEME_CODE;
		return this.parentSchemeId;
	}

	/**
	 * <p>A wrapper around {@link #getParentSchemeId()}.</p>
	 *
	 * <p>Returns <code>Scheme</code> parent to this <code>SchemeLink</code>
	 * or <code>SchemeCableLlink</code> or <code>SchemeElement</code>.
	 * Descendants almost always need to override
	 * {@link #getParentSchemeId()}.</p>
	 */
	public final Scheme getParentScheme() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeId(), true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return null;
		}
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public final void setDescription(final String description) {
		assert this.description != null : OBJECT_NOT_INITIALIZED;
		assert description != null : NON_NULL_EXPECTED;
		if (this.description.equals(description)) {
			return;
		}
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public final void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		if (this.name.equals(name)) {
			return;
		}
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * Descendants almost always need to override this.
	 *
	 * @see #parentSchemeId
	 */
	public void setParentScheme(final Scheme parentScheme) {
		if (parentScheme == null) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(this.id);
			return;
		}
		final Identifier newParentSchemeId = parentScheme.getId();
		if (this.parentSchemeId.equals(newParentSchemeId)) {
			return;
		}
		this.parentSchemeId = newParentSchemeId;
		super.markAsChanged();
	}

	/**
	 * @param header
	 * @param name1
	 * @param description1
	 * @param parentSchemeId1
	 * @throws CreateObjectException
	 */
	void fromTransferable(final IdlStorableObject header,
			final String name1, final String description1,
			final IdlIdentifier parentSchemeId1)
			throws CreateObjectException {
		try {
			super.fromTransferable(header);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
		this.name = name1;
		this.description = description1;
		this.parentSchemeId = new Identifier(parentSchemeId1);
	}

	final void setAttributes(final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version, final String name,
			final String description,
			final Identifier parentSchemeId) {
		synchronized (this) {
			super.setAttributes(created, modified, creatorId, modifierId, version);
	
			assert name != null && name.length() != 0: NON_EMPTY_EXPECTED;
			assert description != null: NON_NULL_EXPECTED;
			assert parentSchemeId != null: NON_NULL_EXPECTED;
	
			this.name = name;
			this.description = description;
			this.parentSchemeId = parentSchemeId;
		}
	}

	/*-********************************************************************
	 * Non-model members.                                                 *
	 **********************************************************************/

	/**
	 * Shouldn&apos;t be declared {@code transient} since the GUI often uses
	 * drag&apos;n&apos;drop.
	 *
	 * @todo Check whether this attribute is necessary.
	 */
	private boolean alarmed;

	/**
	 * Transient attribute
	 */
	public final boolean isAlarmed() {
		return this.alarmed;
	}

	/**
	 * Transient attribute
	 */
	public final void setAlarmed(final boolean alarmed) {
		this.alarmed = alarmed;
	}
}
