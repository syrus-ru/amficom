/*-
 * $Id: SchemeOptimizeInfo.java,v 1.83 2005/12/07 17:17:20 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import static com.syrus.AMFICOM.general.ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_EMPTY_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_NULL_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.NON_VOID_EXPECTED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_NOT_INITIALIZED;
import static com.syrus.AMFICOM.general.ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL;
import static com.syrus.AMFICOM.general.ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
import static com.syrus.AMFICOM.general.Identifier.VOID_IDENTIFIER;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEMEOPTIMIZEINFO_CODE;
import static com.syrus.AMFICOM.general.ObjectEntities.SCHEME_CODE;
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
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfo;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfoHelper;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeOptimizeInfo;
import com.syrus.util.Log;
import com.syrus.util.transport.xml.XmlConversionException;
import com.syrus.util.transport.xml.XmlTransferableObject;

/**
 * #05 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.83 $, $Date: 2005/12/07 17:17:20 $
 * @module scheme
 */
public final class SchemeOptimizeInfo extends StorableObject<SchemeOptimizeInfo>
		implements Describable, ReverseDependencyContainer,
		XmlTransferableObject<XmlSchemeOptimizeInfo> {
	private static final long serialVersionUID = 3761127137155232822L;

	private String name;

	private String description;

	private int optimizationMode;

	private int iterations;

	private double price;

	private double waveLength;

	private double lenMargin;

	private double mutationRate;

	private double mutationDegree;

	private double rtuDeleteProb;

	private double rtuCreateProb;

	private double nodesSpliceProb;

	private double nodesCutProb;

	private double survivorRate;

	Identifier parentSchemeId;

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param optimizationMode
	 * @param iterations
	 * @param price
	 * @param waveLength
	 * @param lenMargin
	 * @param mutationRate
	 * @param mutationDegree
	 * @param rtuDeleteProb
	 * @param rtuCreateProb
	 * @param nodesSpliceProb
	 * @param nodesCutProb
	 * @param survivorRate
	 * @param parentScheme
	 */
	SchemeOptimizeInfo(final Identifier id,
			final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final int optimizationMode,
			final int iterations,
			final double price,
			final double waveLength,
			final double lenMargin,
			final double mutationRate,
			final double mutationDegree,
			final double rtuDeleteProb,
			final double rtuCreateProb,
			final double nodesSpliceProb,
			final double nodesCutProb,
			final double survivorRate,
			final Scheme parentScheme) {
		super(id, created, modified, creatorId, modifierId, version);
		this.name = name;
		this.description = description;
		this.optimizationMode = optimizationMode;
		this.iterations = iterations;
		this.price = price;
		this.waveLength = waveLength;
		this.lenMargin = lenMargin;
		this.mutationRate = mutationRate;
		this.mutationDegree = mutationDegree;
		this.rtuDeleteProb = rtuDeleteProb;
		this.rtuCreateProb = rtuCreateProb;
		this.nodesSpliceProb = nodesSpliceProb;
		this.nodesCutProb = nodesCutProb;
		this.survivorRate = survivorRate;
		this.parentSchemeId = Identifier.possiblyVoid(parentScheme);
	}

	/**
	 * @param transferable
	 */
	public SchemeOptimizeInfo(final IdlSchemeOptimizeInfo transferable) {
		fromTransferable(transferable);
	}

	/**
	 * A shorthand for
	 * {@link #createInstance(Identifier, String, String, int, int, double, double, double, double, double, double, double, double, double, double, Scheme)}.
	 *
	 * @param creatorId
	 * @param name
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	public static SchemeOptimizeInfo createInstance(
			final Identifier creatorId, final String name,
			final Scheme parentScheme) throws CreateObjectException {
		return createInstance(creatorId, name, "", 0, 0, 0, 0, 0, 0, 0,
				0, 0, 0, 0, 0, parentScheme);
	}

	/**
	 * @param creatorId
	 * @param name
	 * @param description
	 * @param optimizationMode
	 * @param iterations
	 * @param price
	 * @param waveLength
	 * @param lenMargin
	 * @param mutationRate
	 * @param mutationDegree
	 * @param rtuDeleteProb
	 * @param rtuCreateProb
	 * @param nodesSpliceProb
	 * @param nodesCutProb
	 * @param survivorRate
	 * @param parentScheme
	 * @throws CreateObjectException
	 */
	@ParameterizationPending(value = {"final boolean usePool"})
	public static SchemeOptimizeInfo createInstance(final Identifier creatorId,
			final String name,
			final String description,
			final int optimizationMode,
			final int iterations,
			final double price,
			final double waveLength,
			final double lenMargin,
			final double mutationRate,
			final double mutationDegree,
			final double rtuDeleteProb,
			final double rtuCreateProb,
			final double nodesSpliceProb,
			final double nodesCutProb,
			final double survivorRate,
			final Scheme parentScheme)
	throws CreateObjectException {
		final boolean usePool = false;

		assert creatorId != null && !creatorId.isVoid() : NON_VOID_EXPECTED;
		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert parentScheme != null : NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeOptimizeInfo schemeOptimizeInfo = new SchemeOptimizeInfo(IdentifierPool.getGeneratedIdentifier(SCHEMEOPTIMIZEINFO_CODE),
					created,
					created,
					creatorId,
					creatorId,
					StorableObjectVersion.INITIAL_VERSION,
					name,
					description,
					optimizationMode,
					iterations,
					price,
					waveLength,
					lenMargin,
					mutationRate,
					mutationDegree,
					rtuDeleteProb,
					rtuCreateProb,
					nodesSpliceProb,
					nodesCutProb,
					survivorRate,
					parentScheme);
			parentScheme.getSchemeOptimizeInfoContainerWrappee().addToCache(schemeOptimizeInfo, usePool);

			schemeOptimizeInfo.markAsChanged();
			return schemeOptimizeInfo;
		} catch (final CreateObjectException coe) {
			throw coe;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeOptimizeInfo.createInstance | cannot generate identifier ", ige);
		} catch (final ApplicationException ae) {
			throw new CreateObjectException(ae);
		}
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
	 * @see com.syrus.AMFICOM.general.ReverseDependencyContainer#getReverseDependencies(boolean)
	 */
	public Set<Identifiable> getReverseDependencies(final boolean usePool) throws ApplicationException {
		final Set<Identifiable> reverseDependencies = new HashSet<Identifiable>();
		reverseDependencies.add(super.id);
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeOptimizeInfoSwitches0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeOptimizeInfoRtus0(usePool)) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeMonitoringSolutions0(usePool)) {
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

	public int getIterations() {
		return this.iterations;
	}

	public double getLenMargin() {
		return this.lenMargin;
	}

	public double getMutationDegree() {
		return this.mutationDegree;
	}

	public double getMutationRate() {
		return this.mutationRate;
	}

	/**
	 * @see com.syrus.AMFICOM.general.Namable#getName()
	 */
	public String getName() {
		assert this.name != null && this.name.length() != 0 : OBJECT_NOT_INITIALIZED;
		return this.name;
	}

	public double getNodesCutProb() {
		return this.nodesCutProb;
	}

	public double getNodesSpliceProb() {
		return this.nodesSpliceProb;
	}

	public int getOptimizationMode() {
		return this.optimizationMode;
	}

	Identifier getParentSchemeId() {
		assert this.parentSchemeId != null: OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;
		assert this.parentSchemeId.getMajor() == SCHEME_CODE;
		return this.parentSchemeId;
	}

	/**
	 * A wrapper around {@link #getParentSchemeId()}.
	 */
	public Scheme getParentScheme() {
		try {
			return StorableObjectPool.getStorableObject(this.getParentSchemeId(), true);
		} catch (final ApplicationException ae) {
			Log.debugMessage(ae, SEVERE);
			return null;
		}
	}

	public double getPrice() {
		return this.price;
	}

	public double getRtuCreateProb() {
		return this.rtuCreateProb;
	}

	public double getRtuDeleteProb() {
		return this.rtuDeleteProb;
	}

	public double getSurvivorRate() {
		return this.survivorRate;
	}

	/**
	 * @param orb
	 * @see com.syrus.util.transport.idl.IdlTransferableObject#getIdlTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeOptimizeInfo getIdlTransferable(final ORB orb) {
		return IdlSchemeOptimizeInfoHelper.init(orb,
				this.id.getIdlTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getIdlTransferable(),
				this.modifierId.getIdlTransferable(),
				this.version.longValue(),
				this.name,
				this.description,
				this.optimizationMode,
				this.iterations,
				this.price,
				this.waveLength,
				this.lenMargin,
				this.mutationRate,
				this.mutationDegree,
				this.rtuDeleteProb,
				this.rtuCreateProb,
				this.nodesSpliceProb,
				this.nodesCutProb,
				this.survivorRate,
				this.parentSchemeId.getIdlTransferable());
	}

	/**
	 * @param schemeOptimizeInfo
	 * @param importType
	 * @param usePool
	 * @throws XmlConversionException
	 * @see com.syrus.util.transport.xml.XmlTransferableObject#getXmlTransferable(org.apache.xmlbeans.XmlObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlSchemeOptimizeInfo schemeOptimizeInfo,
			final String importType,
			final boolean usePool)
	throws XmlConversionException {
		throw new UnsupportedOperationException();
	}

	public double getWaveLength() {
		return this.waveLength;
	}

	/**
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 * @param name
	 * @param description
	 * @param optimizationMode
	 * @param iterations
	 * @param price
	 * @param waveLength
	 * @param lenMargin
	 * @param mutationRate
	 * @param mutationDegree
	 * @param rtuDeleteProb
	 * @param rtuCreateProb
	 * @param nodesSpliceProb
	 * @param nodesCutProb
	 * @param survivorRate
	 * @param parentSchemeId
	 */
	synchronized void setAttributes(final Date created,
			final Date modified,
			final Identifier creatorId,
			final Identifier modifierId,
			final StorableObjectVersion version,
			final String name,
			final String description,
			final int optimizationMode,
			final int iterations,
			final double price,
			final double waveLength,
			final double lenMargin,
			final double mutationRate,
			final double mutationDegree,
			final double rtuDeleteProb,
			final double rtuCreateProb,
			final double nodesSpliceProb,
			final double nodesCutProb,
			final double survivorRate,
			final Identifier parentSchemeId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert name != null && name.length() != 0 : NON_EMPTY_EXPECTED;
		assert description != null : NON_NULL_EXPECTED;
		assert parentSchemeId != null && !parentSchemeId.isVoid() : NON_VOID_EXPECTED;

		this.name = name;
		this.description = description;
		this.optimizationMode = optimizationMode;
		this.iterations = iterations;
		this.price = price;
		this.waveLength = waveLength;
		this.lenMargin = lenMargin;
		this.mutationRate = mutationRate;
		this.mutationDegree = mutationDegree;
		this.rtuDeleteProb = rtuDeleteProb;
		this.rtuCreateProb = rtuCreateProb;
		this.nodesSpliceProb = nodesSpliceProb;
		this.nodesCutProb = nodesCutProb;
		this.survivorRate = survivorRate;
		this.parentSchemeId = parentSchemeId;
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

	public void setIterations(final int iterations) {
		if (this.iterations == iterations)
			return;
		this.iterations = iterations;
		super.markAsChanged();
	}

	public void setLenMargin(final double lenMargin) {
		if (this.lenMargin == lenMargin)
			return;
		this.lenMargin = lenMargin;
		super.markAsChanged();
	}

	public void setMutationDegree(final double mutationDegree) {
		if (this.mutationDegree == mutationDegree)
			return;
		this.mutationDegree = mutationDegree;
		super.markAsChanged();
	}

	public void setMutationRate(final double mutationRate) {
		if (this.mutationRate == mutationRate)
			return;
		this.mutationRate = mutationRate;
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

	public void setNodesCutProb(final double nodesCutProb) {
		if (this.nodesCutProb == nodesCutProb)
			return;
		this.nodesCutProb = nodesCutProb;
		super.markAsChanged();
	}

	public void setNodesSpliceProb(final double nodesSpliceProb) {
		if (this.nodesSpliceProb == nodesSpliceProb)
			return;
		this.nodesSpliceProb = nodesSpliceProb;
		super.markAsChanged();
	}

	public void setOptimizationMode(final int optimizationMode) {
		if (this.optimizationMode == optimizationMode)
			return;
		this.optimizationMode = optimizationMode;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setParentScheme(Scheme, boolean)}.
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

		if (this.parentSchemeId.equals(parentSchemeId)) {
			return;
		}

		this.setParentScheme(
				StorableObjectPool.<Scheme>getStorableObject(parentSchemeId, true),
				usePool);
	}

	/**
	 * @param parentScheme
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setParentScheme(final Scheme parentScheme,
			final boolean usePool)
	throws ApplicationException {
		assert this.parentSchemeId != null : OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;

		final Identifier newParentSchemeId = Identifier.possiblyVoid(parentScheme);
		if (this.parentSchemeId.equals(newParentSchemeId)) {
			return;
		}

		this.getParentScheme().getSchemeOptimizeInfoContainerWrappee().removeFromCache(this, usePool);

		if (parentScheme == null) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(this.getReverseDependencies(usePool));
		} else {
			parentScheme.getSchemeOptimizeInfoContainerWrappee().addToCache(this, usePool);
		}

		this.parentSchemeId = newParentSchemeId;
		super.markAsChanged();
	}

	public void setPrice(double price) {
		if (this.price == price)
			return;
		this.price = price;
		super.markAsChanged();
	}

	public void setRtuCreateProb(final double rtuCreateProb) {
		if (this.rtuCreateProb == rtuCreateProb)
			return;
		this.rtuCreateProb = rtuCreateProb;
		super.markAsChanged();
	}

	public void setRtuDeleteProb(final double rtuDeleteProb) {
		if (this.rtuDeleteProb == rtuDeleteProb)
			return;
		this.rtuDeleteProb = rtuDeleteProb;
		super.markAsChanged();
	}

	public void setSurvivorRate(final double survivorRate) {
		if (this.survivorRate == survivorRate)
			return;
		this.survivorRate = survivorRate;
		super.markAsChanged();
	}

	public void setWaveLength(double waveLength) {
		if (this.waveLength == waveLength)
			return;
		this.waveLength = waveLength;
		super.markAsChanged();
	}

	/**
	 * @param transferable
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IdlStorableObject)
	 */
	@Override
	protected void fromTransferable(final IdlStorableObject transferable) {
		synchronized (this) {
			final IdlSchemeOptimizeInfo schemeOptimizeInfo = (IdlSchemeOptimizeInfo) transferable;
			try {
				super.fromTransferable(schemeOptimizeInfo);
			} catch (final ApplicationException ae) {
				/*
				 * Never.
				 */
				assert false;
			}
			this.name = schemeOptimizeInfo.name;
			this.description = schemeOptimizeInfo.description;
			this.optimizationMode = schemeOptimizeInfo.optimizationMode;
			this.iterations = schemeOptimizeInfo.iterations;
			this.price = schemeOptimizeInfo.price;
			this.waveLength = schemeOptimizeInfo.waveLength;
			this.lenMargin = schemeOptimizeInfo.lenMargin;
			this.mutationRate = schemeOptimizeInfo.mutationRate;
			this.mutationDegree = schemeOptimizeInfo.mutationDegree;
			this.rtuDeleteProb = schemeOptimizeInfo.rtuDeleteProb;
			this.rtuCreateProb = schemeOptimizeInfo.rtuCreateProb;
			this.nodesSpliceProb = schemeOptimizeInfo.nodesSpliceProb;
			this.nodesCutProb = schemeOptimizeInfo.nodesCutProb;
			this.survivorRate = schemeOptimizeInfo.survivorRate;
			this.parentSchemeId = new Identifier(schemeOptimizeInfo.parentSchemeId);
		}
	}

	/**
	 * @param xmlSchemeOptimizeInfo
	 * @param importType
	 * @throws XmlConversionException
	 * @see XmlTransferableObject#fromXmlTransferable(org.apache.xmlbeans.XmlObject, String)
	 */
	public void fromXmlTransferable(
			final XmlSchemeOptimizeInfo xmlSchemeOptimizeInfo,
			final String importType)
	throws XmlConversionException {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getWrapper()
	 */
	@Override
	protected SchemeOptimizeInfoWrapper getWrapper() {
		return SchemeOptimizeInfoWrapper.getInstance();
	}

	/*-********************************************************************
	 * Children manipulation: scheme optimizeInfo switches                *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<SchemeOptimizeInfoSwitch> schemeOptimizeInfoSwitchContainerWrappee;

	StorableObjectContainerWrappee<SchemeOptimizeInfoSwitch> getSchemeOptimizeInfoSwitchContainerWrappee() {
		return (this.schemeOptimizeInfoSwitchContainerWrappee == null)
				? this.schemeOptimizeInfoSwitchContainerWrappee = new StorableObjectContainerWrappee<SchemeOptimizeInfoSwitch>(this, SCHEMEOPTIMIZEINFOSWITCH_CODE)
				: this.schemeOptimizeInfoSwitchContainerWrappee;
	}

	/**
	 * @param schemeOptimizeInfoSwitch
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemeOptimizeInfoSwitch(
			final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch,
			final boolean usePool)
	throws ApplicationException {
		assert schemeOptimizeInfoSwitch != null: NON_NULL_EXPECTED;
		schemeOptimizeInfoSwitch.setParentSchemeOptimizeInfo(this, usePool);
	}

	/**
	 * @param schemeOptimizeInfoSwitch
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemeOptimizeInfoSwitch(
			final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch,
			final boolean usePool)
	throws ApplicationException {
		assert schemeOptimizeInfoSwitch != null: NON_NULL_EXPECTED;
		assert schemeOptimizeInfoSwitch.getParentSchemeOptimizeInfoId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeOptimizeInfoSwitch.setParentSchemeOptimizeInfo(null, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemeOptimizeInfoSwitch> getSchemeOptimizeInfoSwitches(
			final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemeOptimizeInfoSwitches0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	private Set<SchemeOptimizeInfoSwitch> getSchemeOptimizeInfoSwitches0(
			final boolean usePool)
	throws ApplicationException {
		return this.getSchemeOptimizeInfoSwitchContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param schemeOptimizeInfoSwitches
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemeOptimizeInfoSwitches(
			final Set<SchemeOptimizeInfoSwitch> schemeOptimizeInfoSwitches,
			final boolean usePool)
	throws ApplicationException {
		assert schemeOptimizeInfoSwitches != null: NON_NULL_EXPECTED;

		final Set<SchemeOptimizeInfoSwitch> oldSchemeOptimizeInfoSwitches = this.getSchemeOptimizeInfoSwitches0(usePool);

		final Set<SchemeOptimizeInfoSwitch> toRemove = new HashSet<SchemeOptimizeInfoSwitch>(oldSchemeOptimizeInfoSwitches);
		toRemove.removeAll(schemeOptimizeInfoSwitches);
		for (final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch : toRemove) {
			this.removeSchemeOptimizeInfoSwitch(schemeOptimizeInfoSwitch, usePool);
		}

		final Set<SchemeOptimizeInfoSwitch> toAdd = new HashSet<SchemeOptimizeInfoSwitch>(schemeOptimizeInfoSwitches);
		toAdd.removeAll(oldSchemeOptimizeInfoSwitches);
		for (final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch : toAdd) {
			this.addSchemeOptimizeInfoSwitch(schemeOptimizeInfoSwitch, usePool);
		}
	}

	/*-********************************************************************
	 * Children manipulation: scheme optimizeInfo RTUs                    *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<SchemeOptimizeInfoRtu> schemeOptimizeInfoRtuContainerWrappee;

	StorableObjectContainerWrappee<SchemeOptimizeInfoRtu> getSchemeOptimizeInfoRtuContainerWrappee() {
		return (this.schemeOptimizeInfoRtuContainerWrappee == null)
				? this.schemeOptimizeInfoRtuContainerWrappee = new StorableObjectContainerWrappee<SchemeOptimizeInfoRtu>(this, SCHEMEOPTIMIZEINFORTU_CODE)
				: this.schemeOptimizeInfoRtuContainerWrappee;
	}

	/**
	 * @param schemeOptimizeInfoRtu
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemeOptimizeInfoRtu(
			final SchemeOptimizeInfoRtu schemeOptimizeInfoRtu,
			final boolean usePool)
	throws ApplicationException {
		assert schemeOptimizeInfoRtu != null: NON_NULL_EXPECTED;
		schemeOptimizeInfoRtu.setParentSchemeOptimizeInfo(this, usePool);
	}

	/**
	 * @param schemeOptimizeInfoRtu
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemeOptimizeInfoRtu(
			final SchemeOptimizeInfoRtu schemeOptimizeInfoRtu,
			final boolean usePool)
	throws ApplicationException {
		assert schemeOptimizeInfoRtu != null: NON_NULL_EXPECTED;
		assert schemeOptimizeInfoRtu.getParentSchemeOptimizeInfoId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeOptimizeInfoRtu.setParentSchemeOptimizeInfo(null, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemeOptimizeInfoRtu> getSchemeOptimizeInfoRtus(
			final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemeOptimizeInfoRtus0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	private Set<SchemeOptimizeInfoRtu> getSchemeOptimizeInfoRtus0(
			final boolean usePool)
	throws ApplicationException {
		return this.getSchemeOptimizeInfoRtuContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param schemeOptimizeInfoRtus
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemeOptimizeInfoRtus(
			final Set<SchemeOptimizeInfoRtu> schemeOptimizeInfoRtus,
			final boolean usePool)
	throws ApplicationException {
		assert schemeOptimizeInfoRtus != null: NON_NULL_EXPECTED;

		final Set<SchemeOptimizeInfoRtu> oldSchemeOptimizeInfoRtus = this.getSchemeOptimizeInfoRtus0(usePool);

		final Set<SchemeOptimizeInfoRtu> toRemove = new HashSet<SchemeOptimizeInfoRtu>(oldSchemeOptimizeInfoRtus);
		toRemove.removeAll(schemeOptimizeInfoRtus);
		for (final SchemeOptimizeInfoRtu schemeOptimizeInfoRtu : toRemove) {
			this.removeSchemeOptimizeInfoRtu(schemeOptimizeInfoRtu, usePool);
		}

		final Set<SchemeOptimizeInfoRtu> toAdd = new HashSet<SchemeOptimizeInfoRtu>(schemeOptimizeInfoRtus);
		toAdd.removeAll(oldSchemeOptimizeInfoRtus);
		for (final SchemeOptimizeInfoRtu schemeOptimizeInfoRtu : toAdd) {
			this.addSchemeOptimizeInfoRtu(schemeOptimizeInfoRtu, usePool);
		}
	}

	/*-********************************************************************
	 * Children manipulation: scheme monitoringSolutions                  *
	 **********************************************************************/

	private transient StorableObjectContainerWrappee<SchemeMonitoringSolution> schemeMonitoringSolutionContainerWrappee;

	StorableObjectContainerWrappee<SchemeMonitoringSolution> getSchemeMonitoringSolutionContainerWrappee() {
		return (this.schemeMonitoringSolutionContainerWrappee == null)
				? this.schemeMonitoringSolutionContainerWrappee = new StorableObjectContainerWrappee<SchemeMonitoringSolution>(this, SCHEMEMONITORINGSOLUTION_CODE)
				: this.schemeMonitoringSolutionContainerWrappee;
	}

	/**
	 * @param schemeMonitoringSolution
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void addSchemeMonitoringSolution(
			final SchemeMonitoringSolution schemeMonitoringSolution,
			final boolean usePool)
	throws ApplicationException {
		assert schemeMonitoringSolution != null: NON_NULL_EXPECTED;
		schemeMonitoringSolution.setParentSchemeOptimizeInfo(this, usePool);
	}

	/**
	 * @param schemeMonitoringSolution
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void removeSchemeMonitoringSolution(
			final SchemeMonitoringSolution schemeMonitoringSolution,
			final boolean usePool)
	throws ApplicationException {
		assert schemeMonitoringSolution != null: NON_NULL_EXPECTED;
		assert schemeMonitoringSolution.getParentSchemeOptimizeInfoId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeMonitoringSolution.setParentSchemeOptimizeInfo(null, usePool);
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	public Set<SchemeMonitoringSolution> getSchemeMonitoringSolutions(
			final boolean usePool)
	throws ApplicationException {
		return Collections.unmodifiableSet(this.getSchemeMonitoringSolutions0(usePool));
	}

	/**
	 * @param usePool
	 * @throws ApplicationException
	 */
	private Set<SchemeMonitoringSolution> getSchemeMonitoringSolutions0(
			final boolean usePool)
	throws ApplicationException {
		return this.getSchemeMonitoringSolutionContainerWrappee().getContainees(usePool);
	}

	/**
	 * @param schemeMonitoringSolutions
	 * @param usePool
	 * @throws ApplicationException
	 */
	public void setSchemeMonitoringSolutions(
			final Set<SchemeMonitoringSolution> schemeMonitoringSolutions,
			final boolean usePool)
	throws ApplicationException {
		assert schemeMonitoringSolutions != null: NON_NULL_EXPECTED;

		final Set<SchemeMonitoringSolution> oldSchemeMonitoringSolutions = this.getSchemeMonitoringSolutions0(usePool);

		final Set<SchemeMonitoringSolution> toRemove = new HashSet<SchemeMonitoringSolution>(oldSchemeMonitoringSolutions);
		toRemove.removeAll(schemeMonitoringSolutions);
		for (final SchemeMonitoringSolution schemeMonitoringSolution : toRemove) {
			this.removeSchemeMonitoringSolution(schemeMonitoringSolution, usePool);
		}

		final Set<SchemeMonitoringSolution> toAdd = new HashSet<SchemeMonitoringSolution>(schemeMonitoringSolutions);
		toAdd.removeAll(oldSchemeMonitoringSolutions);
		for (final SchemeMonitoringSolution schemeMonitoringSolution : toAdd) {
			this.addSchemeMonitoringSolution(schemeMonitoringSolution, usePool);
		}
	}
}
