/*
 * $Id: TemporalPatternTestCase.java,v 1.4 2004/08/31 15:29:13 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package test.com.syrus.AMFICOM.measurement;

import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.TemporalPatternDatabase;
import com.syrus.AMFICOM.measurement.corba.TemporalPattern_Transferable;

/**
 * @version $Revision: 1.4 $, $Date: 2004/08/31 15:29:13 $
 * @author $Author: bob $
 * @module tools
 */
public class TemporalPatternTestCase extends AbstractMesurementTestCase {

	public TemporalPatternTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = TemporalPatternTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static junit.framework.Test suite() {
		return suiteWrapper(TemporalPatternTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException {

		TemporalPatternDatabase temporalPatternDatabase = (TemporalPatternDatabase) MeasurementDatabaseContext
				.getTemporalPatternDatabase();

		List list = temporalPatternDatabase.retrieveAll();

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.TEMPORALPATTERN_ENTITY_CODE);

		String[] strings = new String[1];
		strings[0] = "*/10 * * * *";

		TemporalPattern temporalPattern = TemporalPattern
				.createInstance(id, creatorId, "created by TemporalPatternTestCase", strings);

		TemporalPattern temporalPattern2 = new TemporalPattern((TemporalPattern_Transferable) temporalPattern
				.getTransferable());

		assertEquals(temporalPattern.getId(), temporalPattern2.getId());

		TemporalPattern temporalPattern3 = new TemporalPattern(temporalPattern2.getId());

		assertEquals(temporalPattern2, temporalPattern3);

		if (!list.isEmpty())
			temporalPatternDatabase.delete(temporalPattern);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		TemporalPatternDatabase temporalPatternDatabase = (TemporalPatternDatabase) MeasurementDatabaseContext
				.getTemporalPatternDatabase();
		List list = temporalPatternDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			TemporalPattern temporalPattern = (TemporalPattern) it.next();
			TemporalPattern temporalPattern2 = new TemporalPattern(temporalPattern.getId());
			assertEquals(temporalPattern.getId(), temporalPattern2.getId());
		}
	}

}