/*
 * $Id: AnalysisTypeTestCase.java,v 1.1 2004/08/26 14:09:38 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package test.com.syrus.AMFICOM.measurement;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Test;

import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;

/**
 * @version $Revision: 1.1 $, $Date: 2004/08/26 14:09:38 $
 * @author $Author: bob $
 * @module tools
 */
public class AnalysisTypeTestCase extends AbstractMesurementTestCase {

	public AnalysisTypeTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = AnalysisTypeTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(AnalysisTypeTestCase.class);
	}

	public void testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, ObjectNotFoundException, RetrieveObjectException {


		List list = AnalysisTypeDatabase.retrieveAll();
		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
		List inParameterTypes = new LinkedList();
		List criteriaParameterTypes= new LinkedList();
		List etalonParameterTypes= new LinkedList();
		List outParameterTypes= new LinkedList();
		AnalysisType anType = AnalysisType.createInstance(id, AbstractMesurementTestCase.creatorId,
									"codeName:"+id.getIdentifierString(),
									"analysisType created by AnalysisTypeTestCase",
									inParameterTypes , criteriaParameterTypes, etalonParameterTypes, outParameterTypes);
		AnalysisType anType2 = new AnalysisType((AnalysisType_Transferable) anType.getTransferable());

		assertEquals(anType.getId(), anType2.getId());

		AnalysisType anType3 = new AnalysisType(anType2.getId());

		assertEquals(anType2.getId(), anType3.getId());

		if (!list.isEmpty())
			AnalysisTypeDatabase.delete(anType);

	}

	public void testRetriveAll() throws RetrieveObjectException, ObjectNotFoundException {
		List list = AnalysisTypeDatabase.retrieveAll();
		List idsList = new LinkedList();
		for (Iterator it = list.iterator(); it.hasNext();) {
			AnalysisType anType = (AnalysisType) it.next();			
			AnalysisType anType2 = new AnalysisType(anType.getId());
			assertEquals(anType.getId(), anType2.getId());
			idsList.add(anType.getId());
		}
		List analysisList =AnalysisTypeDatabase.retrieveByIds(idsList);
		for(Iterator it=analysisList.iterator();it.hasNext();){
			AnalysisType anType = (AnalysisType) it.next();			
			AnalysisType anType2 = new AnalysisType(anType.getId());
			assertEquals(anType.getId(), anType2.getId());
		}
	}

}