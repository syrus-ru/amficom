/*-
 * $Id: SchemeOptimizeInfo.java,v 1.37 2005/07/07 15:52:10 bass Exp $
 *
 * Copyright � 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.ORB;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseContext;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifiable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.StorableObjectPool;
import com.syrus.AMFICOM.general.corba.IdlStorableObject;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfo;
import com.syrus.AMFICOM.scheme.corba.IdlSchemeOptimizeInfoHelper;
import com.syrus.util.Log;

/**
 * #05 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.37 $, $Date: 2005/07/07 15:52:10 $
 * @module scheme_v1
 */
public final class SchemeOptimizeInfo extends AbstractCloneableStorableObject
		implements Describable {
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

	private Identifier parentSchemeId;

	private SchemeOptimizeInfoDatabase schemeOptimizeInfoDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeOptimizeInfo(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
	
		this.schemeOptimizeInfoDatabase = (SchemeOptimizeInfoDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEMEOPTIMIZEINFO_CODE);
		try {
			this.schemeOptimizeInfoDatabase.retrieve(this);
		} catch (final IllegalDataException ide) {
			throw new RetrieveObjectException(ide.getMessage(), ide);
		}
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
	SchemeOptimizeInfo(final Identifier id, final Date created,
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final int optimizationMode, final int iterations,
			final double price, final double waveLength,
			final double lenMargin, final double mutationRate,
			final double mutationDegree,
			final double rtuDeleteProb, final double rtuCreateProb,
			final double nodesSpliceProb,
			final double nodesCutProb, final double survivorRate,
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
		this.schemeOptimizeInfoDatabase = (SchemeOptimizeInfoDatabase) DatabaseContext.getDatabase(ObjectEntities.SCHEMEOPTIMIZEINFO_CODE);
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
	public static SchemeOptimizeInfo createInstance(
			final Identifier creatorId, final String name,
			final String description, final int optimizationMode,
			final int iterations, final double price,
			final double waveLength, final double lenMargin,
			final double mutationRate, final double mutationDegree,
			final double rtuDeleteProb, final double rtuCreateProb,
			final double nodesSpliceProb,
			final double nodesCutProb, final double survivorRate,
			final Scheme parentScheme) throws CreateObjectException {
		assert creatorId != null && !creatorId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;
		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentScheme != null: ErrorMessages.NON_NULL_EXPECTED;

		try {
			final Date created = new Date();
			final SchemeOptimizeInfo schemeOptimizeInfo = new SchemeOptimizeInfo(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEMEOPTIMIZEINFO_CODE),
					created, created, creatorId, creatorId,
					0L, name, description,
					optimizationMode, iterations, price,
					waveLength, lenMargin, mutationRate,
					mutationDegree, rtuDeleteProb,
					rtuCreateProb, nodesSpliceProb,
					nodesCutProb, survivorRate,
					parentScheme);
			schemeOptimizeInfo.markAsChanged();
			return schemeOptimizeInfo;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeOptimizeInfo.createInstance | cannot generate identifier ", ige);
		}
	}

	public void addSchemeMonitoringSolution(final SchemeMonitoringSolution schemeMonitoringSolution) {
		assert schemeMonitoringSolution != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeMonitoringSolution.setParentSchemeOptimizeInfo(this);
	}

	public void addSchemeOptimizeInfoRtu(final SchemeOptimizeInfoRtu schemeOptimizeInfoRtu) {
		assert schemeOptimizeInfoRtu != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeOptimizeInfoRtu.setParentSchemeOptimizeInfo(this);
	}

	public void addSchemeOptimizeInfoSwitch(final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch) {
		assert schemeOptimizeInfoSwitch != null: ErrorMessages.NON_NULL_EXPECTED;
		schemeOptimizeInfoSwitch.setParentSchemeOptimizeInfo(this);
	}

	@Override
	public SchemeOptimizeInfo clone() {
		final SchemeOptimizeInfo schemeOptimizeInfo = (SchemeOptimizeInfo) super
				.clone();
		/**
		 * @todo Update the newly created object.
		 */
		return schemeOptimizeInfo;
	}

	/**
	 * @see com.syrus.AMFICOM.general.StorableObject#getDependencies()
	 */
	@Override
	public Set<Identifiable> getDependencies() {
		assert this.parentSchemeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		final Set<Identifiable> dependencies = new HashSet<Identifiable>();
		dependencies.add(this.parentSchemeId);
		dependencies.remove(null);
		dependencies.remove(Identifier.VOID_IDENTIFIER);
		return Collections.unmodifiableSet(dependencies);
	}

	/**
	 * @see Describable#getDescription()
	 */
	public String getDescription() {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
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
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
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

	public Scheme getParentScheme() {
		assert this.parentSchemeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;

		try {
			return (Scheme) StorableObjectPool.getStorableObject(this.parentSchemeId, true);
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
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

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 */
	public Set getSchemeMonitoringSolutions() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEMEMONITORINGSOLUTION_CODE), true, true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 */
	public Set getSchemeOptimizeInfoRtus() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, ObjectEntities.SCHEMEOPTIMIZEINFORTU_CODE), true, true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	/**
	 * @todo parameter breakOnLoadError to StorableObjectPool.getStorableObjectsByCondition
	 */
	public Set getSchemeOptimizeInfoSwitches() {
		try {
			return Collections.unmodifiableSet(StorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(super.id, ObjectEntities.SCHEMEOPTIMIZEINFOSWITCH_CODE), true, true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
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
				this.version, this.name,
				this.description, this.optimizationMode,
				this.iterations, this.price, this.waveLength,
				this.lenMargin, this.mutationRate,
				this.mutationDegree, this.rtuDeleteProb,
				this.rtuCreateProb, this.nodesSpliceProb,
				this.nodesCutProb, this.survivorRate,
				this.parentSchemeId.getTransferable());
	}

	public double getWaveLength() {
		return this.waveLength;
	}

	public void removeSchemeMonitoringSolution(final SchemeMonitoringSolution schemeMonitoringSolution) {
		assert schemeMonitoringSolution != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeMonitoringSolutions().contains(schemeMonitoringSolution): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeMonitoringSolution.setParentSchemeOptimizeInfo(null);
	}

	public void removeSchemeOptimizeInfoRtu(final SchemeOptimizeInfoRtu schemeOptimizeInfoRtu) {
		assert schemeOptimizeInfoRtu != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeOptimizeInfoRtus().contains(schemeOptimizeInfoRtu): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
		schemeOptimizeInfoRtu.setParentSchemeOptimizeInfo(null);
	}

	public void removeSchemeOptimizeInfoSwitch(final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch) {
		assert schemeOptimizeInfoSwitch != null: ErrorMessages.NON_NULL_EXPECTED;
		assert getSchemeOptimizeInfoSwitches().contains(schemeOptimizeInfoSwitch): ErrorMessages.REMOVAL_OF_AN_ABSENT_PROHIBITED;
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
			final Date modified, final Identifier creatorId,
			final Identifier modifierId, final long version,
			final String name, final String description,
			final int optimizationMode, final int iterations,
			final double price, final double waveLength,
			final double lenMargin, final double mutationRate,
			final double mutationDegree,
			final double rtuDeleteProb, final double rtuCreateProb,
			final double nodesSpliceProb,
			final double nodesCutProb, final double survivorRate,
			final Identifier parentSchemeId) {
		super.setAttributes(created, modified, creatorId, modifierId, version);

		assert name != null && name.length() != 0: ErrorMessages.NON_EMPTY_EXPECTED;
		assert description != null: ErrorMessages.NON_NULL_EXPECTED;
		assert parentSchemeId != null && !parentSchemeId.isVoid(): ErrorMessages.NON_VOID_EXPECTED;

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
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
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
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
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
	 * @param parentScheme
	 */
	public void setParentScheme(final Scheme parentScheme) {
		assert this.parentSchemeId != null: ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert !this.parentSchemeId.isVoid(): ErrorMessages.EXACTLY_ONE_PARENT_REQUIRED;
		if (parentScheme == null) {
			Log.debugMessage(ErrorMessages.OBJECT_WILL_DELETE_ITSELF_FROM_POOL, Log.WARNING);
			StorableObjectPool.delete(super.id);
			return;
		}
		final Identifier newParentSchemeId = parentScheme.getId();
		if (this.parentSchemeId.equals(newParentSchemeId))
			return;
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

	public void setSchemeMonitoringSolutions(final Set schemeMonitoringSolutions) {
		assert schemeMonitoringSolutions != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeMonitoringSolutionIterator = getSchemeMonitoringSolutions().iterator(); oldSchemeMonitoringSolutionIterator.hasNext();) {
			final SchemeMonitoringSolution oldSchemeMonitoringSolution = (SchemeMonitoringSolution) oldSchemeMonitoringSolutionIterator.next();
			/*
			 * Check is made to prevent SchemeMonitoringSolutions from
			 * permanently losing their parents.
			 */
			assert !schemeMonitoringSolutions.contains(oldSchemeMonitoringSolution);
			removeSchemeMonitoringSolution(oldSchemeMonitoringSolution);
		}
		for (final Iterator schemeMonitoringSolutionIterator = schemeMonitoringSolutions.iterator(); schemeMonitoringSolutionIterator.hasNext();)
			addSchemeMonitoringSolution((SchemeMonitoringSolution) schemeMonitoringSolutionIterator.next());
	}

	public void setSchemeOptimizeInfoRtus(final Set schemeOptimizeInfoRtus) {
		assert schemeOptimizeInfoRtus != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeOptimizeInfoRtuIterator = getSchemeOptimizeInfoRtus().iterator(); oldSchemeOptimizeInfoRtuIterator.hasNext();) {
			final SchemeOptimizeInfoRtu oldSchemeOptimizeInfoRtu = (SchemeOptimizeInfoRtu) oldSchemeOptimizeInfoRtuIterator.next();
			/*
			 * Check is made to prevent SchemeOptimizeInfoRtus from
			 * permanently losing their parents.
			 */
			assert !schemeOptimizeInfoRtus.contains(oldSchemeOptimizeInfoRtu);
			removeSchemeOptimizeInfoRtu(oldSchemeOptimizeInfoRtu);
		}
		for (final Iterator schemeOptimizeInfoRtuIterator = schemeOptimizeInfoRtus.iterator(); schemeOptimizeInfoRtuIterator.hasNext();)
			addSchemeOptimizeInfoRtu((SchemeOptimizeInfoRtu) schemeOptimizeInfoRtuIterator.next());
	}

	public void setSchemeOptimizeInfoSwitches(final Set schemeOptimizeInfoSwitches) {
		assert schemeOptimizeInfoSwitches != null: ErrorMessages.NON_NULL_EXPECTED;
		for (final Iterator oldSchemeOptimizeInfoSwitchIterator = getSchemeOptimizeInfoSwitches().iterator(); oldSchemeOptimizeInfoSwitchIterator.hasNext();) {
			final SchemeOptimizeInfoSwitch oldSchemeOptimizeInfoSwitch = (SchemeOptimizeInfoSwitch) oldSchemeOptimizeInfoSwitchIterator.next();
			/*
			 * Check is made to prevent SchemeOptimizeInfoSwitches from
			 * permanently losing their parents.
			 */
			assert !schemeOptimizeInfoSwitches.contains(oldSchemeOptimizeInfoSwitch);
			removeSchemeOptimizeInfoSwitch(oldSchemeOptimizeInfoSwitch);
		}
		for (final Iterator schemeOptimizeInfoSwitchIterator = schemeOptimizeInfoSwitches.iterator(); schemeOptimizeInfoSwitchIterator.hasNext();)
			addSchemeOptimizeInfoSwitch((SchemeOptimizeInfoSwitch) schemeOptimizeInfoSwitchIterator.next());
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
	protected void fromTransferable(final IdlStorableObject transferable) {
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
