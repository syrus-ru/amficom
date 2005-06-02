/*
 * $Id: MeasurementTestCase.java,v 1.2 2005/06/02 14:31:02 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.measurement;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;

/**
 * @version $Revision: 1.2 $, $Date: 2005/06/02 14:31:02 $
 * @author $Author: arseniy $
 * @module tools
 */
public class MeasurementTestCase extends AbstractMesurementTestCase {

	public MeasurementTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = MeasurementTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(MeasurementTestCase.class);
	}


	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException, IllegalDataException {
		MeasurementDatabase measurementDatabase = (MeasurementDatabase) MeasurementDatabaseContext.getMeasurementDatabase();
		List list = measurementDatabase.retrieveButIds(null);
		for (Iterator it = list.iterator(); it.hasNext();) {
			Measurement measurement = (Measurement) it.next();
			Measurement measurement2 = new Measurement(measurement.getId());
			assertEquals(measurement.getId(), measurement2.getId());
		}
	}

}