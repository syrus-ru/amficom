/*-
 * $Id: SchemeMonitoringSolution.java,v 1.89 2005/12/07 17:17:20 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.ACTION_WILL_RESULT_IN_NOTHING;
import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_BADLY_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFO_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEPATH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
import static java.util.logging.Level.INFO;
import static java.util.logging.Level.SEVERE;
import static java.util.logging.Level.WARNING;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeMonitoringSolution;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeMonitoringSolutionHelper;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeMonitoringSolution;
import com.syrus.util.Log;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * #08 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.89 $, $Date: 2005/12/07 17:17:20 $
 * @module scheme
 */
public final class SchemeMonitoringSolution
		extends StorableObject<SchemeMonitoringSolution>
		implements Describable, ReverseDependencyContainer,
		XmlTransferableObject<XmlSchemeMonitoringSolution> {
	private static final long serialVersionUID = 3906364939487949361L;

	private String name;

	private String description;

	private int price;

	private boolean active;

	Identifier parentSchemeId;

	/**
	 * May be void, as <code>SchemeMonitoringSolution</code> may be used
	 * just as a storage for {@link SchemePath}s. However, in this case we
	 * do need information about the scheme, so only one of
	 * {@code parentSchemeOptimizeInfoId} and {@link #parentSchemeId} can be
	 * void at the same time.
	 */
	Identifier parentSchemeOptimizeInfoId;

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param price
	 * @param active
	 * @param parentScheme
	 * @param parentSchemeOptimizeInfo
	 */
	SchemeMonitoringSolution(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final int price,
			final boolean active,
			final Scheme parentScheme,
			final SchemeOptimizeInfo parentSchemeOptimizeInfo) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.price = price;
		this.active = active;

		assert parentScheme == null || parentSchemeOptimizeInfo == null : EXACTLY_ONE_PARENT_REQUIRED;
		this.parentSchemeId = Identifier.possiblyVoid(parentScheme);
		this.parentSchemeOptimizeInfoId = Identifier.possiblyVoid(parentSchemeOptimizeInfo);
	}

	/**
	 * @param transferable
	 */
	public SchemeMonitoringSolution(final IdlSchemeMonitoringSolution transferable) {
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, int, SchemeOptimizeInfo)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentSchemeOptimizeInfo
	 * @throws CreateObjectException
	 */
	public static SchemeMonitoringSolution createInstance(
			final Identifier creatorId, final String name,
			final SchemeOptimizeInfo parentSchemeOptimizeInfo)
	throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, parentSchemeOptimizeInfo);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, int, Scheme)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeMonitoringSolution createInstance(
			final Identifier creatorId, final String name,
			final Scheme parentScheme)
	throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, parentScheme);
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param price
	 * @param parentSchemeOptimizeInfo
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemeMonitoringSolution createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final int price,
			final SchemeOptimizeInfo parentSchemeOptimizeInfo) throws CreateObjectException {
		final boolean usePool = false;

		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert parentSchemeOptimizeInfo != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final boolean active = parentSchemeOptimizeInfo.getParentScheme().getSchemeMonitoringSolutionsRecursively(usePool).isEmpty();
			final SchemeMonitoringSolution schemeMonitoringSolution = new SchemeMonitoringSolution(IdentifierPool.getGeneratedIdentifier(SCHEMEMONITORINGSOLUTION_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					name,
					description,
					price,
					active,
					null,
					parentSchemeOptimizeInfo);
			parentSchemeOptimizeInfo.getSchemeMonitoringSolutionContainerWrappee().addToCache(schemeMonitoringSolution, usePool);

			schemeMonitoringSolution.markAsChanged();
			return schemeMonitoringSolution;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeMonitoringSolution.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param price
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemeMonitoringSolution createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final int price,
			final Scheme parentScheme) throws CreateObjectException {
		final boolean usePool = false;

		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert parentScheme != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final boolean active = parentScheme.getSchemeMonitoringSolutionsRecursively(usePool).isEmpty();
			final SchemeMonitoringSolution schemeMonitoringSolution = new SchemeMonitoringSolution(IdentifierPool.getGeneratedIdentifier(SCHEMEMONITORINGSOLUTION_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					name,
					description,
					price,
					active,
					parentScheme,
					null);
			parentScheme.getSchemeMonitoringSolutionContainerWrappee().addToCache(schemeMonitoringSolution, usePool);

			schemeMonitoringSolution.markAsChanged();
			return schemeMonitoringSolution;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeMonitoringSolution.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.parentSchemeId != null
				&& this.parentSchemeOptimizeInfoId != null: OBJECT_NOT_INITIALIZED;
		assert this.parentSchemeId.isVoid() ^ this.parentSchemeOptimizeInfoId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.parentSchemeId);
		dependencies.add(this.parentSchemeOptimizeInfoId);
		dependencies.remove(null);
		dependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies(boolean)
	 */
	public Set<Identifiable> getReverseDependencies(final boolean usePool) throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.add(super.id);
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemePaths0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		reverseDependencies.remove(null);
		reverseDependencies.remove(VOID_IDENTIFIER);
		return Collections.unmodifiableSet(reverseDependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : OBJECT_NOT_INITIALIZED;
		return this.description;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	Identifier getParentSchemeId() {
		assert this.parentSchemeId != null && this.parentSchemeOptimizeInfoId != null : OBJECT_NOT_INITIALIZED;
		final boolean parentSchemeIdIsVoid = this.parentSchemeId.isVoid();
		assert parentSchemeIdIsVoid ^ this.parentSchemeOptimizeInfoId.isVoid() : OBJECT_BADLY_INITIALIZED;
		assert parentSchemeIdIsVoid || this.parentSchemeId.getMajor() == SCHEME_CODE;
		return this.parentSchemeId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeId()}.
	 */
	public Scheme getParentScheme() {
		try {
			return this.getParentSchemeOptimizeInfoId().isVoid()
					? StorableObjectPool.<Scheme>getStorableObject(this.getParentSchemeId(), true)
					: this.getParentSchemeOptimizeInfo().getParentScheme();
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	Identifier getParentSchemeOptimizeInfoId() {
		assert this.parentSchemeId != null && this.parentSchemeOptimizeInfoId != null : OBJECT_NOT_INITIALIZED;
		final boolean parentSchemeOptimizeInfoIdIsVoid = this.parentSchemeOptimizeInfoId.isVoid();
		assert this.parentSchemeId.isVoid() ^ parentSchemeOptimizeInfoIdIsVoid : OBJECT_BADLY_INITIALIZED;
		assert parentSchemeOptimizeInfoIdIsVoid || this.parentSchemeOptimizeInfoId.getMajor() == SCHEMEOPTIMIZEINFO_CODE;
		return this.parentSchemeOptimizeInfoId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeOptimizeInfoId()}.
	 *
	 * @return <code>SchemeOptimizeInfo</code> parent for this
	 *         <code>SchemeMonitoringSolution</code>, or <code>null</code>
	 *         if none.
	 */
	public SchemeOptimizeInfo getParentSchemeOptimizeInfo() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeOptimizeInfoId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	public int getPrice() {
		return this.price;
	}

	public boolean isActive() {
		return this.active;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeMonitoringSolution getIdlTransferable(final ORB orb) {
		return IdlSchemeMonitoringSolutionHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.price,
				this.active,
				this.parentSchemeId.getIdlTransferable(),
				this.parentSchemeOptimizeInfoId.getIdlTransferable());
	}

	/**
	 * @param schemeMonitoringSolution
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlSchemeMonitoringSolution schemeMonitoringSolution,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param price
	 * @param active
	 * @param parentSchemeId
	 * @param parentSchemeOptimizeInfoId
	 */
	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final int price,
			final boolean active,
			final Identifier parentSchemeId,
			final Identifier parentSchemeOptimizeInfoId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert parentSchemeId != null : NON_NULL_EXPECTED;
		assert parentSchemeOptimizeInfoId != null : NON_NULL_EXPECTED;
		assert parentSchemeId.isVoid() ^ parentSchemeOptimizeInfoId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;

		this.name = name;
		this.description = description;
		this.price = price;
		this.active = active;
		this.parentSchemeId = parentSchemeId;
		this.parentSchemeOptimizeInfoId = parentSchemeOptimizeInfoId;
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : OBJECT_NOT_INITIALIZED;
		assert description != null : NON_NULL_EXPECTED;
		if (this.description.equals(description))
			return;
		this.description = description;
		super.markAsChanged();
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		if (this.name.equals(name))
			return;
		this.name = name;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setParentSchemeOptimizeInfo(SchemeOptimizeInfo, boolean)}.
	 *
	 * @param parentSchemeOptimizeInfoId
	 * @param usePool
	 * @throws ApplicationException
	 */
	void setParentSchemeOptimizeInfoId(
			final Identifier parentSchemeOptimizeInfoId,
			final boolean usePool)
	throws ApplicationException {
		assert parentSchemeOptimizeInfoId != null : NON_NULL_EXPECTED;
		assert parentSchemeOptimizeInfoId.isVoid() || parentSchemeOptimizeInfoId.getMajor() == SCHEMEOPTIMIZEINFO_CODE;
		
		if (this.parentSchemeOptimizeInfoId.equals(parentSchemeOptimizeInfoId)) {
			return;
		}

		this.setParentSchemeOptimizeInfo(
				StorableObjectPool.<SchemeOptimizeInfo>getStorableObject(parentSchemeOptimizeInfoId, true),
				usePool);
	}

	/**
	 * @param parentSchemeOptimizeInfo
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setParentSchemeOptimizeInfo(
			final SchemeOptimizeInfo parentSchemeOptimizeInfo,
			final boolean usePool)
	throws ApplicationException {
		assert this.parentSchemeId != null && this.parentSchemeOptimizeInfoId != null : OBJECT_NOT_INITIALIZED;
		final boolean thisParentSchemeOptimizeInfoIdVoid = this.parentSchemeOptimizeInfoId.isVoid();
		assert this.parentSchemeId.isVoid() ^ thisParentSchemeOptimizeInfoIdVoid : OBJECT_BADLY_INITIALIZED;

		final boolean parentSchemeOptimizeInfoNull = (parentSchemeOptimizeInfo == null);

		final Identifier newParentSchemeOptimizeInfoId = Identifier.possiblyVoid(parentSchemeOptimizeInfo);
		if (this.parentSchemeOptimizeInfoId.equals(newParentSchemeOptimizeInfoId)) {
			Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
			return;
		}

		if (thisParentSchemeOptimizeInfoIdVoid) {
			/*
			 * Erasing old object-type value, setting new object
			 * value. At this point, newParentSchemeOptimizeInfoId
			 * is non-void.
			 */
			this.getParentScheme().getSchemeMonitoringSolutionContainerWrappee().removeFromCache(this, usePool);

			this.parentSchemeId = VOID_IDENTIFIER;
		} else {
			/*
			 * At this point, newParentSchemeOptimizeInfoId may
			 * be void.
			 */
			this.getParentSchemeOptimizeInfo().getSchemeMonitoringSolutionContainerWrappee().removeFromCache(this, usePool);

			if (parentSchemeOptimizeInfoNull) {
				/*
				 * Erasing old object value, preserving old object-type
				 * value. This point is not assumed to be reached unless
				 * initial object value has already been set (i. e.
				 * there already is object-type value to preserve).
				 * 
				 * this.parentSchemeOptimizeInfoId will
				 * automatically become a VOID_IDENTIFIER
				 */
				final Scheme parentScheme = this.getParentSchemeOptimizeInfo().getParentScheme();
				parentScheme.getSchemeMonitoringSolutionContainerWrappee().addToCache(this, usePool);

				this.parentSchemeId = parentScheme.getId();
			}
		}

		if (!parentSchemeOptimizeInfoNull) {
			parentSchemeOptimizeInfo.getSchemeMonitoringSolutionContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeOptimizeInfoId = newParentSchemeOptimizeInfoId;
		super.markAsChanged();
	}

	/**
	 * <p>A wrapper around {@link #setParentScheme(Scheme, boolean)}.</p>
	 *
	 * <p>This method features one extra check since <em>if</em>
	 * {@code parentSchemeId} is void, <em>then</em> either this
	 * {@code SchemeMonitoringSolution} or its parent
	 * {@code SchemeOptimizeInfo} will be deleted.</p>
	 *
	 * @param parentSchemeId
	 * @param usePool
	 * @throws ApplicationException
	 */
	void setParentSchemeId(final Identifier parentSchemeId,
			final boolean usePool)
	throws ApplicationException {
		assert parentSchemeId != null : NON_NULL_EXPECTED;
		assert parentSchemeId.isVoid() || parentSchemeId.getMajor() == SCHEME_CODE;

		if (this.parentSchemeId.isVoid()) {
			this.getParentSchemeOptimizeInfo().setParentSchemeId(parentSchemeId, usePool);
		} else {
			if (this.parentSchemeId.equals(parentSchemeId)) {
				return;
			}

			this.setParentScheme(
					StorableObjectPool.<Scheme>getStorableObject(parentSchemeId, true),
					usePool);
		}
	}

	/**
	 * Sets the {@code Scheme}, parent for this
	 * {@code SchemeMonitoringSolution}.
	 * 
	 * <table border = "1" width = "100%">
	 * <tbody>
	 * <tr>
	 * 	<th bgcolor = "#800000"><font color="#ffffff">{@code oldParentScheme}</font></th>
	 * 	<th bgcolor = "#800000"><font color="#ffffff">{@code newParentScheme}</font></th>
	 * 	<th bgcolor = "#800000"><font color="#ffffff">Action taken</font></th>
	 * </tr>
	 * <tr>
	 * 	<td>{@code non-null}</td>
	 * 	<td>{@code non-null}</td>
	 * 	<td>{@code parentScheme} property is changed unless old value and the new one are equal</td></tr>
	 * <tr>
	 * 	<td>{@code non-null}</td>
	 * 	<td>{@code null}</td>
	 * 	<td>No {@code parentSchemeOptimizeInfo} property was present. The object is deleted.</td>
	 * </tr>
	 * <tr>
	 * 	<td>{@code null}</td>
	 * 	<td>{@code non-null}</td>
	 * 	<td>{@code parentSchemeOptimizeInfo} property is present.
	 * 	    Its {@code parentScheme} property is modified.</td>
	 * </tr>
	 * <tr>
	 * 	<td>{@code null}</td>
	 * 	<td>{@code null}</td>
	 * 	<td>{@code parentSchemeOptimizeInfo} property is present.
	 * 	    Its {@code parentScheme} property is modified (set to
	 * 	    {@code null}), resulting in cascading deletion of
	 * 	    {@code parentSchemeOptimizeInfo} with all its <em>reverse
	 * 	    dependencies</em>, including this
	 * 	    {@code SchemeMonitoringSolution}.</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 *
	 * @param parentScheme must be non-{@code null}, otherwise the object
	 *        will be deleted from pool. 
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setParentScheme(final Scheme parentScheme,
			final boolean usePool)
	throws ApplicationException {
		assert this.parentSchemeId != null && this.parentSchemeOptimizeInfoId != null : OBJECT_NOT_INITIALIZED;
		assert this.parentSchemeId.isVoid() ^ this.parentSchemeOptimizeInfoId.isVoid() : OBJECT_BADLY_INITIALIZED;

		if (this.parentSchemeId.isVoid()) {
			this.getParentSchemeOptimizeInfo().setParentScheme(parentScheme, usePool);
		} else {
			final Identifier newParentSchemeId = Identifier.possiblyVoid(parentScheme);
			if (this.parentSchemeId.equals(newParentSchemeId)) {
				Log.debugMessage(ACTION_WILL_RESULT_IN_NOTHING, INFO);
				return;
			}

			this.getParentScheme().getSchemeMonitoringSolutionContainerWrappee().removeFromCache(this, usePool);

			if (parentScheme == null) {
				Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
				StorableObjectPool.delete(this.getReverseDependencies(usePool));
			} else {
				parentScheme.getSchemeMonitoringSolutionContainerWrappee().addToCache(this, usePool);
			}

			this.parentSchemeId = newParentSchemeId;
			super.markAsChanged();
		}
	}

	public void setPrice(final int price) {
		if (this.price == price)
			return;
		this.price = price;
		super.markAsChanged();
	}

	/**
	 * @param active if {@code false}, removes itself from the pool and
	 *        selects an arbitrary {@code SchemeMonitoringSolution} as
	 *        active, thus preventing the parent {@code Scheme} from losing
	 *        the active solution, unless it has no solutions at all upon
	 *        removal of this one. 
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setActive(final boolean active, final boolean usePool)
	throws ApplicationException {
		if (this.active == active)
			return;

		final Scheme parentScheme = this.getParentScheme();
		if (active) {
			final SchemeMonitoringSolution currentSchemeMonitoringSolution =
					parentScheme.getCurrentSchemeMonitoringSolution(usePool);
			currentSchemeMonitoringSolution.active = false;
			currentSchemeMonitoringSolution.markAsChanged();
		} else {
			StorableObjectPool.delete(this.getReverseDependencies(usePool));

			final Set<SchemeMonitoringSolution> schemeMonitoringSolutions =
					parentScheme.getSchemeMonitoringSolutionsRecursively0(usePool);
			schemeMonitoringSolutions.remove(this);
			for (final SchemeMonitoringSolution schemeMonitoringSolution : schemeMonitoringSolutions) {
				schemeMonitoringSolution.active = true;
				schemeMonitoringSolution.markAsChanged();
				break;
			}
		}

		this.active = active;
		super.markAsChanged();
	}

	/**
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) {
		synchronized (this) {
			final IdlSchemeMonitoringSolution schemeMonitoringSolution = (IdlSchemeMonitoringSolution) transferable;
			try {
				super.fromTransferable(schemeMonitoringSolution);
			} catch (final ApplicationException ae) {
				/*
				 * Never.
				 */
				assert false;
			}
			this.name = schemeMonitoringSolution.name;
			this.description = schemeMonitoringSolution.description;
			this.price = schemeMonitoringSolution.priceUsd;
			this.active = schemeMonitoringSolution.active;
			this.parentSchemeId = new Identifier(schemeMonitoringSolution.parentSchemeId);
			this.parentSchemeOptimizeInfoId = new Identifier(schemeMonitoringSolution.parentSchemeOptimizeInfoId);
		}
	}

	/**
	 * @param xmlSchemeMonitoringSolution
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(
			final XmlSchemeMonitoringSolution xmlSchemeMonitoringSolution,
			final String importType)
	throws XmlConversionException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected SchemeMonitoringSolutionWrapper getWrapper() {
		return SchemeMonitoringSolutionWrapper.getInstance();
	}

	/*-********************************************************************
	 * Children manipulation: scheme paths                                *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<SchemePath> schemePathContainerWrappee;

	StorableObjectContainerWrappee<SchemePath> getSchemePathContainerWrappee() {
		return (this.schemePathContainerWrappee == null)
				? this.schemePathContainerWrappee = new StorableObjectContainerWrappee<SchemePath>(this, SCHEMEPATH_CODE)
				: this.schemePathContainerWrappee;
	}

	/**
	 * @param schemePath
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemePath(final SchemePath schemePath,
			final boolean usePool)
	throws ApplicationException {
		assert schemePath != null: NON_NULL_EXPECTED;
		schemePath.setParentSchemeMonitoringSolution(this, usePool);
	}

	/**
	 * @param schemePath
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemePath(final SchemePath schemePath,
			final boolean usePool)
	throws ApplicationException {
		assert schemePath != null: NON_NULL_EXPECTED;
		assert schemePath.getParentSchemeMonitoringSolutionId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemePath.setParentSchemeMonitoringSolution(null, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemePath> getSchemePaths(final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemePaths0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	Set<SchemePath> getSchemePaths0(final boolean usePool)
	throws ApplicationException {
		return this.getSchemePathContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param schemePaths
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemePaths(final Set<SchemePath> schemePaths,
			final boolean usePool)
	throws ApplicationException {
		assert schemePaths != null: NON_NULL_EXPECTED;

		final Set<SchemePath> oldSchemePaths = this.getSchemePaths0(usePool);

		final Set<SchemePath> toRemove = new HashSet<SchemePath>(oldSchemePaths);
		toRemove.removeAll(schemePaths);
		for (final SchemePath schemePath : toRemove) {
			this.removeSchemePath(schemePath, usePool);
		}

		final Set<SchemePath> toAdd = new HashSet<SchemePath>(schemePaths);
		toAdd.removeAll(oldSchemePaths);
		for (final SchemePath schemePath : toAdd) {
			this.addSchemePath(schemePath, usePool);
		}
	}
}
