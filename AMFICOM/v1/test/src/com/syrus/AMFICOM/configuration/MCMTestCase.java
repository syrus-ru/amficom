/*
 * $Id: MCMTestCase.java,v 1.1.1.1 2004/09/10 06:50:56 cvsadmin Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package test.com.syrus.AMFICOM.configuration;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.ConfigurationDatabaseContext;
import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.AMFICOM.configuration.MCMDatabase;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;

/**
 * @version $Revision: 1.1.1.1 $, $Date: 2004/09/10 06:50:56 $
 * @author $Author: cvsadmin $
 * @module tools
 */
public class MCMTestCase extends ConfigureTestCase {

	public MCMTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = MCMTestCase.class;
		junit.awtui.TestRunner.run(clazz);
//		junit.swingui.TestRunner.run(clazz);
//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(MCMTestCase.class);
	}

	public void testRetriveAll() throws ObjectNotFoundException, RetrieveObjectException, IllegalDataException {
		MCMDatabase database = (MCMDatabase) ConfigurationDatabaseContext.getMCMDatabase();

		List list = database.retrieveByIds(null, null);
		for (Iterator it = list.iterator(); it.hasNext();) {
			MCM mcm = (MCM) it.next();
			MCM mcm2 = new MCM(mcm.getId());
			assertEquals(mcm.getId(), mcm2.getId());
		}
	}

}