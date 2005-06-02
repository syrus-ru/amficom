/*
 * $Id: ResultTestCase.java,v 1.2 2005/06/02 14:31:02 arseniy Exp $
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