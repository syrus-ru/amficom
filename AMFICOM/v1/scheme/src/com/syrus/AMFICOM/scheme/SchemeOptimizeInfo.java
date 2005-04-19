/*-
 * $Id: SchemeOptimizeInfo.java,v 1.15 2005/04/19 17:45:16 bass Exp $
 *
 * Copyright ¿ 2004-2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

import org.omg.CORBA.portable.IDLEntity;

import com.syrus.AMFICOM.general.AbstractCloneableStorableObject;
import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Describable;
import com.syrus.AMFICOM.general.ErrorMessages;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.LinkedIdsCondition;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo_Transferable;
import com.syrus.util.Log;

/**
 * #05 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.15 $, $Date: 2005/04/19 17:45:16 $
 * @module scheme_v1
 */
public final class SchemeOptimizeInfo extends AbstractCloneableStorableObject
		implements Describable {
	private static final long serialVersionUID = 3761127137155232822L;

	private String description;

	private int iterations;

	private double lenMargin;

	private double mutationDegree;

	private double mutationRate;

	private String name;

	private double nodesCutProb;

	private double nodesSpliceProb;

	private int optimizationMode;

	private Identifier parentSchemeId;

	private double price;

	private double rtuCreateProb;

	private double rtuDeleteProb;

	private double survivorRate;

	private double waveLength;

	private SchemeOptimizeInfoDatabase schemeOptimizeInfoDatabase;

	/**
	 * @param id
	 * @throws RetrieveObjectException
	 * @throws ObjectNotFoundException
	 */
	SchemeOptimizeInfo(final Identifier id) throws RetrieveObjectException, ObjectNotFoundException {
		super(id);
	
		this.schemeOptimizeInfoDatabase = SchemeDatabaseContext.getSchemeOptimizeInfoDatabase();
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
	 */
	SchemeOptimizeInfo(Identifier id, Date created,
			Date modified, Identifier creatorId,
			Identifier modifierId, long version) {
		super(id, created, modified, creatorId, modifierId, version);
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 */
	SchemeOptimizeInfo(final SchemeOptimizeInfo_Transferable transferable) throws CreateObjectException {
		this.schemeOptimizeInfoDatabase = SchemeDatabaseContext.getSchemeOptimizeInfoDatabase();
		fromTransferable(transferable);
	}

	public static SchemeOptimizeInfo createInstance(
			final Identifier creatorId)
			throws CreateObjectException {
		assert creatorId != null;
		try {
			final Date created = new Date();
			final SchemeOptimizeInfo schemeOptimizeInfo = new SchemeOptimizeInfo(
					IdentifierPool
							.getGeneratedIdentifier(ObjectEntities.SCHEME_OPTIMIZE_INFO_ENTITY_CODE),
					created, created, creatorId, creatorId,
					0L);
			schemeOptimizeInfo.changed = true;
			return schemeOptimizeInfo;
		} catch (final IdentifierGenerationException ige) {
			throw new CreateObjectException(
					"SchemeOptimizeInfo.createInstance | cannot generate identifier ", ige); //$NON-NLS-1$
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

	public Object clone() {
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
	public Set getDependencies() {
		throw new UnsupportedOperationException();
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
		throw new UnsupportedOperationException();
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

	public Set getSchemeMonitoringSolutions() {
		try {
			return Collections.unmodifiableSet(SchemeStorableObjectPool.getStorableObjectsByCondition(new LinkedIdsCondition(this.id, ObjectEntities.SCHEME_MONITORING_SOLUTION_ENTITY_CODE), true));
		} catch (final ApplicationException ae) {
			Log.debugException(ae, Log.SEVERE);
			return Collections.EMPTY_SET;
		}
	}

	public Set getSchemeOptimizeInfoRtus() {
		throw new UnsupportedOperationException();
	}

	public Set getSchemeOptimizeInfoSwitches() {
		throw new UnsupportedOperationException();
	}

	public double getSurvivorRate() {
		return this.survivorRate;
	}

	/**
	 * @see com.syrus.AMFICOM.general.TransferableObject#getTransferable()
	 */
	public IDLEntity getTransferable() {
		throw new UnsupportedOperationException();
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
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
		if (this.description.equals(description))
			return;
		this.description = description;
		this.changed = true;
	}

	public void setIterations(final int iterations) {
		if (this.iterations == iterations)
			return;
		this.iterations = iterations;
		this.changed = true;
	}

	public void setLenMargin(final double lenMargin) {
		if (this.lenMargin == lenMargin)
			return;
		this.lenMargin = lenMargin;
		this.changed = true;
	}

	public void setMutationDegree(final double mutationDegree) {
		if (this.mutationDegree == mutationDegree)
			return;
		this.mutationDegree = mutationDegree;
		this.changed = true;
	}

	public void setMutationRate(final double mutationRate) {
		if (this.mutationRate == mutationRate)
			return;
		this.mutationRate = mutationRate;
		this.changed = true;
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
		this.changed = true;
	}

	public void setNodesCutProb(final double nodesCutProb) {
		if (this.nodesCutProb == nodesCutProb)
			return;
		this.nodesCutProb = nodesCutProb;
		this.changed = true;
	}

	public void setNodesSpliceProb(final double nodesSpliceProb) {
		if (this.nodesSpliceProb == nodesSpliceProb)
			return;
		this.nodesSpliceProb = nodesSpliceProb;
		this.changed = true;
	}

	public void setOptimizationMode(final int optimizationMode) {
		if (this.optimizationMode == optimizationMode)
			return;
		this.optimizationMode = optimizationMode;
		this.changed = true;
	}

	/**
	 * @param parentScheme
	 */
	public void setParentScheme(final Scheme parentScheme) {
		throw new UnsupportedOperationException();
	}

	public void setPrice(double price) {
		if (this.price == price)
			return;
		this.price = price;
		this.changed = true;
	}

	public void setRtuCreateProb(final double rtuCreateProb) {
		if (this.rtuCreateProb == rtuCreateProb)
			return;
		this.rtuCreateProb = rtuCreateProb;
		this.changed = true;
	}

	public void setRtuDeleteProb(final double rtuDeleteProb) {
		if (this.rtuDeleteProb == rtuDeleteProb)
			return;
		this.rtuDeleteProb = rtuDeleteProb;
		this.changed = true;
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
		this.changed = true;
	}

	public void setWaveLength(double waveLength) {
		if (this.waveLength == waveLength)
			return;
		this.waveLength = waveLength;
		this.changed = true;
	}

	/**
	 * @param transferable
	 * @throws CreateObjectException
	 * @see com.syrus.AMFICOM.general.StorableObject#fromTransferable(IDLEntity)
	 */
	protected void fromTransferable(final IDLEntity transferable) throws CreateObjectException {
		throw new UnsupportedOperationException();
	}
}
