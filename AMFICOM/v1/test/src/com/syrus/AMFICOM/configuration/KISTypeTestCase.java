/*
 * $Id: KISTypeTestCase.java,v 1.1 2004/11/02 12:26:18 cvsadmin Exp $
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
import com.syrus.AMFICOM.configuration.KISType;
import com.syrus.AMFICOM.configuration.KISTypeDatabase;
import com.syrus.AMFICOM.configuration.corba.KISType_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;

/**
 * @version $Revision: 1.1 $, $Date: 2004/11/02 12:26:18 $
 * @author $Author: cvsadmin $
 * @module tools
 */
public class KISTypeTestCase extends ConfigureTestCase {

	public KISTypeTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = KISTypeTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(KISTypeTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, ObjectNotFoundException, RetrieveObjectException {
		KISTypeDatabase kisTypeDatabase = (KISTypeDatabase) ConfigurationDatabaseContext
				.getKISTypeDatabase();
		List list = kisTypeDatabase.retrieveAll();
		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.KISTYPE_ENTITY_CODE);
		KISType kisType = KISType.createInstance(id, ConfigureTestCase.creatorId, "testCaseKISType",
									"kisType created by KISTypeTestCase", "kisType" );
		KISType kisType2 = KISType.getInstance((KISType_Transferable) kisType.getTransferable());

		assertEquals(kisType.getId(), kisType2.getId());

		KISType kisType3 = new KISType(kisType2.getId());

		assertEquals(kisType2.getId(), kisType3.getId());

		if (list.size() > 3)
			kisTypeDatabase.delete(kisType);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		KISTypeDatabase kisTypeDatabase = (KISTypeDatabase) ConfigurationDatabaseContext
				.getKISTypeDatabase();
		List list = kisTypeDatabase.retrieveAll();
		for (Iterator it = list.iterator(); it.hasNext();) {
			KISType kisType = (KISType) it.next();
			KISType kisType2 = new KISType(kisType.getId());
			assertEquals(kisType.getId(), kisType2.getId());
		}
	}

}