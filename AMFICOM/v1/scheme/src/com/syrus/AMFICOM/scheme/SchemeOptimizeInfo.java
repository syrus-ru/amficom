/*-
 * $Id: SchemeOptimizeInfo.java,v 1.67 2005/10/01 15:13:19 bass Exp $
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
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ReverseDependencyContainer;
import com.syrus.AMFICOM.general.StorableObject;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.StorableObjectVersion;
import com.syrus.AMFICOM.general.XmlBeansTransferable;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfo;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfoHelper;
import com.syrus.AMFICOM.scheme.xml.XmlSchemeOptimizeInfo;
import com.syrus.util.Log;

/**
 * #05 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.67 $, $Date: 2005/10/01 15:13:19 $
 * @module scheme
 */
public final class SchemeOptimizeInfo extends StorableObject
		implements Describable, ReverseDependencyContainer,
		XmlBeansTransferable<XmlSchemeOptimizeInfo> {
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
			final Scheme parentScheme) throws CreateObjectException {
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
					StorableObjectVersion.createInitial(),
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
			schemeOptimizeInfo.markAsChanged();
			return schemeOptimizeInfo;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException("SchemeOptimizeInfo.createInstance | cannot generate identifier ", ige);
		}
	}

	public void addSchemeMonitoringSolution(final SchemeMonitoringSolution schemeMonitoringSolution) {
		assert schemeMonitoringSolution != null: NON_NULL_EXPECTED;
		schemeMonitoringSolution.setParentSchemeOptimizeInfo(this);
	}

	public void addSchemeOptimizeInfoRtu(final SchemeOptimizeInfoRtu schemeOptimizeInfoRtu) {
		assert schemeOptimizeInfoRtu != null: NON_NULL_EXPECTED;
		schemeOptimizeInfoRtu.setParentSchemeOptimizeInfo(this);
	}

	public void addSchemeOptimizeInfoSwitch(final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch) {
		assert schemeOptimizeInfoSwitch != null: NON_NULL_EXPECTED;
		schemeOptimizeInfoSwitch.setParentSchemeOptimizeInfo(this);
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
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeOptimizeInfoSwitches0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeOptimizeInfoRtus0()) {
			reverseDependencies.addAll(reverseDependencyContainer.getReverseDependencies(usePool));
		}
		for (final ReverseDependencyContainer reverseDependencyContainer : this.getSchemeMonitoringSolutions0()) {
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
			Log.debugException(ae, SEVERE);
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

	public Set<SchemeMonitoringSolution> getSchemeMonitoringSolutions() {
		try {
			return Collections.unmodifiableSet(this.getSchemeMonitoringSolutions0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	private Set<SchemeMonitoringSolution> getSchemeMonitoringSolutions0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, SCHEMEMONITORINGSOLUTION_CODE), true);
	}

	public Set<SchemeOptimizeInfoRtu> getSchemeOptimizeInfoRtus() {
		try {
			return Collections.unmodifiableSet(this.getSchemeOptimizeInfoRtus0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	private Set<SchemeOptimizeInfoRtu> getSchemeOptimizeInfoRtus0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, SCHEMEOPTIMIZEINFORTU_CODE), true);
	}

	public Set<SchemeOptimizeInfoSwitch> getSchemeOptimizeInfoSwitches() {
		try {
			return Collections.unmodifiableSet(this.getSchemeOptimizeInfoSwitches0());
		} catch (final ApplicationException ae) {
			Log.debugException(ae, SEVERE);
			return Collections.emptySet();
		}
	}

	private Set<SchemeOptimizeInfoSwitch> getSchemeOptimizeInfoSwitches0() throws ApplicationException {
		return StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, SCHEMEOPTIMIZEINFOSWITCH_CODE), true);
	}

	public double getSurvivorRate() {
		return this.survivorRate;
	}

	/**
	 * @param orb
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable(org.omg.CORBA.ORB)
	 */
	@Override
	public IdlSchemeOptimizeInfo getTransferable(final ORB orb) {
		return IdlSchemeOptimizeInfoHelper.init(orb,
				this.id.getTransferable(),
				this.created.getTime(),
				this.modified.getTime(),
				this.creatorId.getTransferable(),
				this.modifierId.getTransferable(),
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
				this.parentSchemeId.getTransferable());
	}

	/**
	 * @param schemeOptimizeInfo
	 * @param importType
	 * @param usePool
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#getXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String, boolean)
	 */
	public void getXmlTransferable(
			final XmlSchemeOptimizeInfo schemeOptimizeInfo,
			final String importType,
			final boolean usePool)
	throws ApplicationException {
		throw new UnsupportedOperationException();
	}

	public double getWaveLength() {
		return this.waveLength;
	}

	public void removeSchemeMonitoringSolution(final SchemeMonitoringSolution schemeMonitoringSolution) {
		assert schemeMonitoringSolution != null: NON_NULL_EXPECTED;
		assert schemeMonitoringSolution.getParentSchemeOptimizeInfoId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeMonitoringSolution.setParentSchemeOptimizeInfo(null);
	}

	public void removeSchemeOptimizeInfoRtu(final SchemeOptimizeInfoRtu schemeOptimizeInfoRtu) {
		assert schemeOptimizeInfoRtu != null: NON_NULL_EXPECTED;
		assert schemeOptimizeInfoRtu.getParentSchemeOptimizeInfoId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeOptimizeInfoRtu.setParentSchemeOptimizeInfo(null);
	}

	public void removeSchemeOptimizeInfoSwitch(final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch) {
		assert schemeOptimizeInfoSwitch != null: NON_NULL_EXPECTED;
		assert schemeOptimizeInfoSwitch.getParentSchemeOptimizeInfoId().equals(this) : REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeOptimizeInfoSwitch.setParentSchemeOptimizeInfo(null);
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
	 * @param parentSchemeId
	 */
	void setParentSchemeId(final Identifier parentSchemeId) {
		assert this.parentSchemeId != null : OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeId.isVoid() : EXACTLY_ONE_PARENT_REQUIRED;

		assert parentSchemeId != null : NON_NULL_EXPECTED;
		final boolean parentSchemeIdVoid = parentSchemeId.isVoid();
		assert parentSchemeIdVoid || parentSchemeId.getMajor() == SCHEME_CODE;

		if (parentSchemeIdVoid) {
			Log.debugMessage(OBJECT_WILL_DELETE_ITSELF_FROM_POOL, WARNING);
			StorableObjectPool.delete(super.id);
			return;
		}
		if (this.parentSchemeId.equals(parentSchemeId)) {
			return;
		}
		this.parentSchemeId = parentSchemeId;
		super.markAsChanged();
	}

	/**
	 * A wrapper around {@link #setParentSchemeId(Identifier)}.
	 *
	 * @param parentScheme
	 */
	public void setParentScheme(final Scheme parentScheme) {
		this.setParentSchemeId(Identifier.possiblyVoid(parentScheme));
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

	public void setSchemeMonitoringSolutions(final Set<SchemeMonitoringSolution> schemeMonitoringSolutions) throws ApplicationException {
		assert schemeMonitoringSolutions != null: NON_NULL_EXPECTED;
		final Set<SchemeMonitoringSolution> oldSchemeMonitoringSolutions = this.getSchemeMonitoringSolutions0();
		/*
		 * Check is made to prevent SchemeMonitoringSolutions from
		 * permanently losing their parents.
		 */
		oldSchemeMonitoringSolutions.removeAll(schemeMonitoringSolutions);
		for (final SchemeMonitoringSolution oldSchemeMonitoringSolution : oldSchemeMonitoringSolutions) {
			this.removeSchemeMonitoringSolution(oldSchemeMonitoringSolution);
		}
		for (final SchemeMonitoringSolution schemeMonitoringSolution : schemeMonitoringSolutions) {
			this.addSchemeMonitoringSolution(schemeMonitoringSolution);
		}
	}

	public void setSchemeOptimizeInfoRtus(final Set<SchemeOptimizeInfoRtu> schemeOptimizeInfoRtus) throws ApplicationException {
		assert schemeOptimizeInfoRtus != null: NON_NULL_EXPECTED;
		final Set<SchemeOptimizeInfoRtu> oldSchemeOptimizeInfoRtus = this.getSchemeOptimizeInfoRtus0();
		/*
		 * Check is made to prevent SchemeOptimizeInfoRtus from
		 * permanently losing their parents.
		 */
		oldSchemeOptimizeInfoRtus.removeAll(schemeOptimizeInfoRtus);
		for (final SchemeOptimizeInfoRtu oldSchemeOptimizeInfoRtu : oldSchemeOptimizeInfoRtus) {
			this.removeSchemeOptimizeInfoRtu(oldSchemeOptimizeInfoRtu);
		}
		for (final SchemeOptimizeInfoRtu schemeOptimizeInfoRtu : schemeOptimizeInfoRtus) {
			this.addSchemeOptimizeInfoRtu(schemeOptimizeInfoRtu);
		}
	}

	public void setSchemeOptimizeInfoSwitches(final Set<SchemeOptimizeInfoSwitch> schemeOptimizeInfoSwitches) throws ApplicationException {
		assert schemeOptimizeInfoSwitches != null: NON_NULL_EXPECTED;
		final Set<SchemeOptimizeInfoSwitch> oldSchemeOptimizeInfoSwitches = this.getSchemeOptimizeInfoSwitches0();
		/*
		 * Check is made to prevent SchemeOptimizeInfoSwitches from
		 * permanently losing their parents.
		 */
		oldSchemeOptimizeInfoSwitches.removeAll(schemeOptimizeInfoSwitches);
		for (final SchemeOptimizeInfoSwitch oldSchemeOptimizeInfoSwitch : oldSchemeOptimizeInfoSwitches) {
			this.removeSchemeOptimizeInfoSwitch(oldSchemeOptimizeInfoSwitch);
		}
		for (final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch : schemeOptimizeInfoSwitches) {
			this.addSchemeOptimizeInfoSwitch(schemeOptimizeInfoSwitch);
		}
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
	 * @throws ApplicationException
	 * @see XmlBeansTransferable#fromXmlTransferable(com.syrus.AMFICOM.general.xml.XmlStorableObject, String)
	 */
	public void fromXmlTransferable(
			final XmlSchemeOptimizeInfo xmlSchemeOptimizeInfo,
			final String importType)
	throws ApplicationException {
		throw new UnsupportedOperationException();
	}
}
