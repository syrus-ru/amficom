/*
 * $Id: AnalysisTypeTestCase.java,v 1.4 2005/02/04 14:21:34 bob Exp $
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
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.measurement.AbstractMesurementTestCase;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;

/**
 * @version $Revision: 1.4 $, $Date: 2005/02/04 14:21:34 $
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

	public void _testCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, ObjectNotFoundException, RetrieveObjectException {

		AnalysisTypeDatabase analysisTypeDatabase = (AnalysisTypeDatabase) MeasurementDatabaseContext
				.getAnalysisTypeDatabase();
		List list = analysisTypeDatabase.retrieveAll();
		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
		List inParameterTypes = new LinkedList();
		List criteriaParameterTypes = new LinkedList();
		List etalonParameterTypes = new LinkedList();
		List outParameterTypes = new LinkedList();
		AnalysisType anType = AnalysisType.createInstance(id, AbstractMesurementTestCase.creatorId, "codeName:"
				+ id.getIdentifierString(), "analysisType created by AnalysisTypeTestCase",
									inParameterTypes, criteriaParameterTypes,
									etalonParameterTypes, outParameterTypes);
		AnalysisType anType2 = AnalysisType.getInstance((AnalysisType_Transferable) anType.getTransferable());

		assertEquals(anType.getId(), anType2.getId());

		AnalysisType anType3 = new AnalysisType(anType2.getId());

		assertEquals(anType2.getId(), anType3.getId());

		if (!list.isEmpty())
			analysisTypeDatabase.delete(anType);

	}

	public void testMultyCreation() throws IdentifierGenerationException, IllegalObjectEntityException,
			CreateObjectException, IllegalDataException, RetrieveObjectException {

		AnalysisTypeDatabase analysisTypeDatabase = (AnalysisTypeDatabase) MeasurementDatabaseContext
				.getAnalysisTypeDatabase();
		List list = analysisTypeDatabase.retrieveAll();
		Identifier id = IdentifierGenerator.generateIdentifier(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
		List inParameterTypes = new LinkedList();
		List criteriaParameterTypes = new LinkedList();
		List etalonParameterTypes = new LinkedList();
		List outParameterTypes = new LinkedList();
		
		List analysisTypes = new LinkedList();
		AnalysisType anType1 = AnalysisType.createInstance(id, AbstractMesurementTestCase.creatorId, "codeName:"
				+ id.getIdentifierString(), "analysisType 1 created by testMultyCreation",
									inParameterTypes, criteriaParameterTypes,
									etalonParameterTypes, outParameterTypes);
		
		id = IdentifierGenerator.generateIdentifier(ObjectEntities.ANALYSISTYPE_ENTITY_CODE);
		
		AnalysisType anType2 = AnalysisType.createInstance(id, AbstractMesurementTestCase.creatorId, "codeName:"
															+ id.getIdentifierString(), "analysisType 2 created by testMultyCreation",
																				inParameterTypes, criteriaParameterTypes,
																				etalonParameterTypes, outParameterTypes);
		
		analysisTypes.add(anType1);
		analysisTypes.add(anType2);
		
		analysisTypeDatabase.insert(analysisTypes);
		
		if (!list.isEmpty()){
			for (Iterator it = analysisTypes.iterator(); it.hasNext();) {
				AnalysisType analysisType = (AnalysisType) it.next();
				analysisTypeDatabase.delete(analysisType);
			}
			
		}

	}

	public void _testRetriveAll() throws IllegalDataException, RetrieveObjectException, ObjectNotFoundException {
		AnalysisTypeDatabase analysisTypeDatabase = (AnalysisTypeDatabase) MeasurementDatabaseContext
				.getAnalysisTypeDatabase();

		List list = analysisTypeDatabase.retrieveAll();
		List idsList = new LinkedList();
		for (Iterator it = list.iterator(); it.hasNext();) {
			AnalysisType anType = (AnalysisType) it.next();
			AnalysisType anType2 = new AnalysisType(anType.getId());
			assertEquals(anType.getId(), anType2.getId());
			idsList.add(anType.getId());
		}
		List analysisList = analysisTypeDatabase.retrieveByIds(idsList, null);
		for (Iterator it = analysisList.iterator(); it.hasNext();) {
			AnalysisType anType = (AnalysisType) it.next();
			AnalysisType anType2 = new AnalysisType(anType.getId());
			assertEquals(anType.getId(), anType2.getId());
		}
	}

}