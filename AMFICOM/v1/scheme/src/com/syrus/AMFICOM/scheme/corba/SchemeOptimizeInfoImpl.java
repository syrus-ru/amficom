/*
 * $Id: SchemeOptimizeInfoImpl.java,v 1.3 2004/11/30 07:54:42 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.*;
import com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfoPackage.OptimizationMode;
import com.syrus.util.logging.ErrorHandler;
import java.util.*;

/**
 * @author $Author: bass $
 * @version $Revision: 1.3 $, $Date: 2004/11/30 07:54:42 $
 * @module schemecommon_v1
 */
final class SchemeOptimizeInfoImpl extends SchemeOptimizeInfo implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	SchemeOptimizeInfoImpl() {
	}
	
	public SchemeOptimizeInfo cloneInstance() {
		try {
			return (SchemeOptimizeInfo) this.clone();
		} catch (CloneNotSupportedException cnse) {
			ERROR_HANDLER.error(cnse);
			return null;
		}
	}

	public long created() {
		throw new UnsupportedOperationException();
	}

	public String description() {
		throw new UnsupportedOperationException();
	}

	public void description(String description) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see java.util.JavaUtilIStorableObject#getCreated()
	 */
	public Date getCreated() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.ComSyrusAmficomGeneralIStorableObject#getCreatorId()
	 */
	public com.syrus.AMFICOM.general.Identifier getCreatorId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see java.util.JavaUtilIStorableObject#getDependencies()
	 */
	public List getDependencies() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.corba.IStorableObject#getHeaderTransferable()
	 */
	public StorableObject_Transferable getHeaderTransferable() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.ComSyrusAmficomGeneralIStorableObject#getId()
	 */
	public com.syrus.AMFICOM.general.Identifier getId() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see java.util.JavaUtilIStorableObject#getModified()
	 */
	public Date getModified() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.ComSyrusAmficomGeneralIStorableObject#getModifierId()
	 */
	public com.syrus.AMFICOM.general.Identifier getModifierId() {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return
	 * @see com.syrus.AMFICOM.general.corba.IStorableObject#isChanged()
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

	public long modified() {
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

	public long version() {
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
		throw new UnsupportedOperationException();
	}
}
