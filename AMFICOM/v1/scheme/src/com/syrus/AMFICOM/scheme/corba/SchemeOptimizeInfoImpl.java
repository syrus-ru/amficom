/*
 * $Id: SchemeOptimizeInfoImpl.java,v 1.11 2005/03/10 15:06:08 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfoPackage.OptimizationMode;
import com.syrus.util.logging.ErrorHandler;
import java.util.Date;

/**
 * @author $Author: bass $
 * @version $Revision: 1.11 $, $Date: 2005/03/10 15:06:08 $
 * @module scheme_v1
 */
final class SchemeOptimizeInfoImpl extends SchemeOptimizeInfo implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	private static final long serialVersionUID = 4121130333883609142L;

	SchemeOptimizeInfoImpl() {
	}
	
	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	public Date getCreated() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getCreatorId()
	 */
	public Identifier getCreatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getDependencies()
	 */
	public Identifier[] getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	public Identifier getId() {
		throw new UnsupportedOperationException();
	}

	public Date getModified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see StorableObject#getModifierId()
	 */
	public Identifier getModifierId() {
		throw new UnsupportedOperationException();
	}

	public long getVersion() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @see IStorableObject#isChanged()
	 */
	public boolean isChanged() {
		throw new UnsupportedOperationException();
	}

	public double iterations() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newIterations
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#iterations(double)
	 */
	public void iterations(double newIterations) {
		throw new UnsupportedOperationException();
	}

	public double lenMargin() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newLenMargin
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#lenMargin(double)
	 */
	public void lenMargin(double newLenMargin) {
		throw new UnsupportedOperationException();
	}

	public double mutationDegree() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newMutationDegree
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#mutationDegree(double)
	 */
	public void mutationDegree(double newMutationDegree) {
		throw new UnsupportedOperationException();
	}

	public double mutationRate() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newMutationRate
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#mutationRate(double)
	 */
	public void mutationRate(double newMutationRate) {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String name) {
		throw new UnsupportedOperationException();
	}

	public double nodesCutProb() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newNodesCutProb
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#nodesCutProb(double)
	 */
	public void nodesCutProb(double newNodesCutProb) {
		throw new UnsupportedOperationException();
	}

	public double nodesSpliceProb() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newNodesSpliceProb
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#nodesSpliceProb(double)
	 */
	public void nodesSpliceProb(double newNodesSpliceProb) {
		throw new UnsupportedOperationException();
	}

	public OptimizationMode optimizationMode() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newOptimizationMode
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#optimizationMode(com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfoPackage.OptimizationMode)
	 */
	public void optimizationMode(OptimizationMode newOptimizationMode) {
		throw new UnsupportedOperationException();
	}

	public double price() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newPrice
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#price(double)
	 */
	public void price(double newPrice) {
		throw new UnsupportedOperationException();
	}

	public String[] reflNames() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newReflNames
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#reflNames(java.lang.String[])
	 */
	public void reflNames(String[] newReflNames) {
		throw new UnsupportedOperationException();
	}

	public double[] reflPrices() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newReflPrices
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#reflPrices(double[])
	 */
	public void reflPrices(double[] newReflPrices) {
		throw new UnsupportedOperationException();
	}

	public double[] reflRanges() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newReflRanges
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#reflRanges(double[])
	 */
	public void reflRanges(double[] newReflRanges) {
		throw new UnsupportedOperationException();
	}

	public double rtuCreateProb() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newRtuCreateProb
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#rtuCreateProb(double)
	 */
	public void rtuCreateProb(double newRtuCreateProb) {
		throw new UnsupportedOperationException();
	}

	public double rtuDeleteProb() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newRtuDeleteProb
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#rtuDeleteProb(double)
	 */
	public void rtuDeleteProb(double newRtuDeleteProb) {
		throw new UnsupportedOperationException();
	}

	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newScheme
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#scheme(com.syrus.AMFICOM.scheme.corba.Scheme)
	 */
	public void scheme(Scheme newScheme) {
		throw new UnsupportedOperationException();
	}

	public SchemeMonitoringSolution schemeMonitoringSolution() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSchemeMonitoringSolution
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#schemeMonitoringSolution(com.syrus.AMFICOM.scheme.corba.SchemeMonitoringSolution)
	 */
	public void schemeMonitoringSolution(SchemeMonitoringSolution newSchemeMonitoringSolution) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param storableObjectFactory
	 * @param changed
	 * @see IStorableObject#setChanged(StorableObjectFactory, boolean)
	 */
	public void setChanged(final StorableObjectFactory storableObjectFactory, final boolean changed) {
		throw new UnsupportedOperationException();
	}

	public double survivorRate() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSurvivorRate
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#survivorRate(double)
	 */
	public void survivorRate(double newSurvivorRate) {
		throw new UnsupportedOperationException();
	}

	public String[] switchNames() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSwitchNames
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#switchNames(java.lang.String[])
	 */
	public void switchNames(String[] newSwitchNames) {
		throw new UnsupportedOperationException();
	}

	public double[] switchNports() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSwitchNports
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#switchNports(double[])
	 */
	public void switchNports(double[] newSwitchNports) {
		throw new UnsupportedOperationException();
	}

	public double[] switchPrices() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newSwitchPrices
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#switchPrices(double[])
	 */
	public void switchPrices(double[] newSwitchPrices) {
		throw new UnsupportedOperationException();
	}

	public double wavelength() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @param newWavelength
	 * @see com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfo#wavelength(double)
	 */
	public void wavelength(double newWavelength) {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		final SchemeOptimizeInfoImpl schemeOptimizeInfo = (SchemeOptimizeInfoImpl) super.clone(); 
		/**
		 * @todo Update the newly created object.
		 */
		return schemeOptimizeInfo;
	}
}
