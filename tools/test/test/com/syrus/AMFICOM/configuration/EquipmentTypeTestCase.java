/*
 * $Id: EquipmentTypeTestCase.java,v 1.1 2004/08/13 14:13:04 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */
package test.com.syrus.AMFICOM.configuration;

import java.util.Iterator;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.configuration.EquipmentType;
import com.syrus.AMFICOM.configuration.EquipmentTypeDatabase;
import com.syrus.AMFICOM.configuration.corba.EquipmentType_Transferable;
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
public class EquipmentTypeTestCase extends ConfigureTestCase {

	public EquipmentTypeTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		junit.awtui.TestRunner.run(EquipmentTypeTestCase.class);
		//		junit.swingui.TestRunner.run(TransmissionPathTestCase.class);
		//		junit.textui.TestRunner.run(TransmissionPathTestCase.class);
	}

	public static Test suite() {
		return _suite(EquipmentTypeTestCase.class);
	}
	
	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, ObjectNotFoundException, RetrieveObjectException {
		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.EQUIPMENTTYPE_ENTITY_CODE);
		EquipmentType eqType = EquipmentType.createInstance(id, ConfigureTestCase.creatorId, "testCaseEqType",
													"portType created by EquipmentTypeTestCase");
		EquipmentType eqType2 = new EquipmentType((EquipmentType_Transferable) eqType.getTransferable());
		
		EquipmentType eqType3 = new EquipmentType(eqType2.getId());		
			
		//EquipmentTypeDatabase.delete(eqType);

	}

		public void testRetriveAll() throws 
	 RetrieveObjectException, ObjectNotFoundException{
			List list = EquipmentTypeDatabase.retrieveAll();
			for(Iterator it=list.iterator();it.hasNext();){
				EquipmentType eqType = (EquipmentType)it.next();
				EquipmentType eqType2 = new EquipmentType(eqType.getId());

			}
		}

}