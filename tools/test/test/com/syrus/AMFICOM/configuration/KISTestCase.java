/*
 * $Id: KISTestCase.java,v 1.1 2004/08/13 14:13:04 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package test.com.syrus.AMFICOM.configuration;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.MCM;
import com.syrus.AMFICOM.configuration.corba.KIS_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/13 14:13:04 $
 * @author $Author: bob $
 * @module tools
 */
public class KISTestCase extends ConfigureTestCase {	
	
	public KISTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		junit.awtui.TestRunner.run(KISTestCase.class);
		//		junit.swingui.TestRunner.run(TransmissionPathTestCase.class);
		//		junit.textui.TestRunner.run(TransmissionPathTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException {
		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.KIS_ENTITY_CODE);

		MCM mcm = new MCM(new Identifier ("MCM_2"));
		KIS kis = KIS.createInstance(id, ConfigureTestCase.creatorId, ConfigureTestCase.domainId, "testCaseKIS",
										"kis  created by KISTestCase ",mcm.getId());

		KIS kis2 = new KIS((KIS_Transferable) kis.getTransferable());
		KIS kis3 = new KIS(kis2.getId());
//		KISDatabase.delete(kis);

	}	

	public static Test suite() {
		return _suite(KISTestCase.class);
	}
}