/*
 * $Id: ResultTestCase.java,v 1.2 2005/02/04 14:21:34 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package test.com.syrus.AMFICOM.measurement;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.measurement.AbstractMesurementTestCase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.ResultDatabase;

/**
 * @version $Revision: 1.2 $, $Date: 2005/02/04 14:21:34 $
 * @author $Author: bob $
 * @module tools
 */
public class ResultTestCase extends AbstractMesurementTestCase {

	public ResultTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = ResultTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(ResultTestCase.class);
	}


	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException, IllegalDataException {
		ResultDatabase resultDatabase = (ResultDatabase) MeasurementDatabaseContext.getResultDatabase();
		List list = resultDatabase.retrieveButIds(null);
		for (Iterator it = list.iterator(); it.hasNext();) {
			Result result = (Result) it.next();
			Result result2 = new Result(result.getId());
			assertEquals(result.getId(), result2.getId());
		}
	}

}