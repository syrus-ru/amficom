/*
 * $Id: EquipmentTestCase.java,v 1.1 2004/08/13 14:13:04 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package test.com.syrus.AMFICOM.configuration;

import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.Equipment;
import com.syrus.AMFICOM.configuration.EquipmentDatabase;
import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.KIS;
import com.syrus.AMFICOM.configuration.KISDatabase;
import com.syrus.AMFICOM.configuration.corba.EquipmentSort;
import com.syrus.AMFICOM.configuration.corba.Equipment_Transferable;
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
public class EquipmentTestCase extends ConfigureTestCase {

	public EquipmentTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		junit.awtui.TestRunner.run(EquipmentTestCase.class);
		//		junit.swingui.TestRunner.run(TransmissionPathTestCase.class);
		//		junit.textui.TestRunner.run(TransmissionPathTestCase.class);
	}

	public static Test suite() {
		return _suite(EquipmentTestCase.class);
	}
	
	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, RetrieveObjectException, ObjectNotFoundException {

		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.EQUIPMENT_ENTITY_CODE);

		List eqTypelist = EquipmentTypeDatabase.retrieveAll();
		
		EquipmentType eqType = (EquipmentType)eqTypelist.get(0);
		
		
		List kislist = KISDatabase.retrieveAll();
		
		KIS kis = (KIS)kislist.get(0);

		Equipment eq = Equipment.createInstance(id, creatorId, domainId, eqType, "testCaseEquipment",
												"equipment created by EquipmentTestCase", new Identifier("Image_1"),
												EquipmentSort._EQUIPMENT_SORT_KIS, kis.getId() );

		Equipment eq2 = new Equipment((Equipment_Transferable) eq.getTransferable());
		Equipment eq3 = new Equipment(eq2.getId());
		
		//EquipmentDatabase.delete(eq);	

	}
}