/*
 * $Id: TestSet.java,v 1.1 2005/02/22 14:09:11 arseniy Exp $
 * 
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package com.syrus.AMFICOM.measurement;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import junit.framework.Test;

import com.syrus.AMFICOM.general.ApplicationException;
import com.syrus.AMFICOM.general.IdentifierPool;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.StorableObjectDatabase;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.corba.Parameter_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2005/02/22 14:09:11 $
 * @author $Author: arseniy $
 * @module config_v1
 */
public class TestSet extends CommonMeasurementTest {

	public TestSet(String name) {
		super(name);
	}

	public static Test suite() {
		return suiteWrapper(TestSet.class);
	}

	public void testRename() throws ApplicationException, SQLException {
		SetDatabase setDatabase = (SetDatabase) MeasurementDatabaseContext.setDatabase;
		Collection sets = setDatabase.retrieveAll();
		System.out.println("Retrieved " + sets.size() + " sets");

		for (Iterator it = sets.iterator(); it.hasNext();) {
			this.save((Set) it.next(), setDatabase);
		}
	}

	private void save(Set set, SetDatabase setDatabase) throws ApplicationException {
		Set_Transferable st = (Set_Transferable) set.getTransferable();
		String id1Str = "Set_" + Long.toString(set.getId().getMinor());
		st.header.id = new Identifier_Transferable(id1Str);

		Parameter_Transferable[] pts = st.parameters;
		for (int i = 0; i < pts.length; i++) {
			pts[i].id = (Identifier_Transferable) IdentifierPool.getGeneratedIdentifier(ObjectEntities.SETPARAMETER_ENTITY_CODE).getTransferable();
		}

		Set set1 = new Set(st);
		setDatabase.update(set1, set.getModifierId(), StorableObjectDatabase.UPDATE_CHECK);
	}
}
