/*-
 * $Id: SchemeOptimizeInfo.java,v 1.6 2005/04/01 13:59:07 bass Exp $
 *
 * Copyright ¿ 2005 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme;

import com.syrus.AMFICOM.general.*;
import java.util.*;

/**
 * #05 in hierarchy.
 *
 * @author $Author: bass $
 * @version $Revision: 1.6 $, $Date: 2005/04/01 13:59:07 $
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

	/**
	 * @param id
	 */
	protected SchemeOptimizeInfo(Identifier id) {
		super(id);
	}

	/**
	 * @param id
	 * @param created
	 * @param modified
	 * @param creatorId
	 * @param modifierId
	 * @param version
	 */
	protected SchemeOptimizeInfo(Identifier id, Date created,
			Date modified, Identifier creatorId,
			Identifier modifierId, long version) {
		super(id, created, modified, creatorId, modifierId, version);
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
		} catch (final IllegalObjectEntityException ioee) {
			throw new CreateObjectException(
					"SchemeOptimizeInfo.createInstance | cannot generate identifier ", ioee); //$NON-NLS-1$
		}
	}

	public void addSchemeMonitoringSolution(final SchemeMonitoringSolution schemeMonitoringSolution) {
		throw new UnsupportedOperationException();
	}

	public void addSchemeOptimizeInfoRtu(final SchemeOptimizeInfoRtu schemeOptimizeInfoRtu) {
		throw new UnsupportedOperationException();
	}

	public void addSchemeOptimizeInfoSwitch(final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch) {
		throw new UnsupportedOperationException();
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
	 * @see StorableObject#getDependencies()
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
	 * @see Namable#getName()
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
		throw new UnsupportedOperationException();
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
	 * @see TransferableObject#getTransferable()
	 */
	public Object getTransferable() {
		throw new UnsupportedOperationException();
	}

	public double getWaveLength() {
		return this.waveLength;
	}

	public void removeSchemeMonitoringSolution(final SchemeMonitoringSolution schemeMonitoringSolution) {
		throw new UnsupportedOperationException();
	}

	public void removeSchemeOptimizeInfoRtu(final SchemeOptimizeInfoRtu schemeOptimizeInfoRtu) {
		throw new UnsupportedOperationException();
	}

	public void removeSchemeOptimizeInfoSwitch(final SchemeOptimizeInfoSwitch schemeOptimizeInfoSwitch) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see Describable#setDescription(String)
	 */
	public void setDescription(final String description) {
		assert this.description != null : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert description != null : ErrorMessages.NON_NULL_EXPECTED;
		if (description.equals(this.description))
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
	 * @see Namable#setName(String)
	 */
	public void setName(final String name) {
		assert this.name != null && this.name.length() != 0 : ErrorMessages.OBJECT_NOT_INITIALIZED;
		assert name != null && name.length() != 0 : ErrorMessages.NON_EMPTY_EXPECTED;
		if (name.equals(this.name))
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
		throw new UnsupportedOperationException();
	}

	public void setSchemeOptimizeInfoRtus(final Set schemeOptimizeInfoRtus) {
		throw new UnsupportedOperationException();
	}

	public void setSchemeOptimizeInfoSwitches(final Set schemeOptimizeInfoSwitches) {
		throw new UnsupportedOperationException();
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
}
