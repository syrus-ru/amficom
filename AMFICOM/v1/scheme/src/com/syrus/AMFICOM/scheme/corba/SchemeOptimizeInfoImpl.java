/*
 * $Id: SchemeOptimizeInfoImpl.java,v 1.1 2004/11/24 10:03:58 bass Exp $
 *
 * Copyright ¿ 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */

package com.syrus.AMFICOM.scheme.corba;

import com.syrus.AMFICOM.general.corba.Identifier;
import com.syrus.AMFICOM.scheme.corba.SchemeOptimizeInfoPackage.OptimizationMode;
import com.syrus.util.logging.ErrorHandler;

/**
 * @author $Author: bass $
 * @version $Revision: 1.1 $, $Date: 2004/11/24 10:03:58 $
 * @module schemecommon_v1
 */
final class SchemeOptimizeInfoImpl extends SchemeOptimizeInfo implements Cloneable {
	private static final ErrorHandler ERROR_HANDLER = ErrorHandler.getInstance();

	SchemeOptimizeInfoImpl() {
	}
	
	SchemeOptimizeInfoImpl(final Identifier id) {
		this.thisId = id;
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

	public void description(String newDescription) {
		throw new UnsupportedOperationException();
	}

	public Identifier id() {
		throw new UnsupportedOperationException();
	}

	public double iterations() {
		throw new UnsupportedOperationException();
	}

	public double lenMargin() {
		throw new UnsupportedOperationException();
	}

	public long modified() {
		throw new UnsupportedOperationException();
	}

	public double mutationDegree() {
		throw new UnsupportedOperationException();
	}

	public double mutationRate() {
		throw new UnsupportedOperationException();
	}

	public String name() {
		throw new UnsupportedOperationException();
	}

	public void name(String newName) {
		throw new UnsupportedOperationException();
	}

	public double nodesCutProb() {
		throw new UnsupportedOperationException();
	}

	public double nodesSpliceProb() {
		throw new UnsupportedOperationException();
	}

	public OptimizationMode optimizationMode() {
		throw new UnsupportedOperationException();
	}

	public double price() {
		throw new UnsupportedOperationException();
	}

	public String[] reflNames() {
		throw new UnsupportedOperationException();
	}

	public double[] reflPrices() {
		throw new UnsupportedOperationException();
	}

	public double[] reflRanges() {
		throw new UnsupportedOperationException();
	}

	public double rtuCreateProb() {
		throw new UnsupportedOperationException();
	}

	public double rtuDeleteProb() {
		throw new UnsupportedOperationException();
	}

	public Scheme scheme() {
		throw new UnsupportedOperationException();
	}

	public SchemeMonitoringSolution schemeMonitoringSolution() {
		throw new UnsupportedOperationException();
	}

	public double survivorRate() {
		throw new UnsupportedOperationException();
	}

	public String[] switchNames() {
		throw new UnsupportedOperationException();
	}

	public double[] switchNports() {
		throw new UnsupportedOperationException();
	}

	public double[] switchPrices() {
		throw new UnsupportedOperationException();
	}

	public long version() {
		throw new UnsupportedOperationException();
	}

	public double wavelength() {
		throw new UnsupportedOperationException();
	}

	protected Object clone() throws CloneNotSupportedException {
		throw new UnsupportedOperationException();
	}
}
