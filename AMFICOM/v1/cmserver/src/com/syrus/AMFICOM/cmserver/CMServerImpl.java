/*
 * $Id: CMServerImpl.java,v 1.1 2004/09/10 14:35:30 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.cmserver.corba.CMServerOperations;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.IdentifierGenerationException;
import com.syrus.AMFICOM.general.IdentifierGenerator;
import com.syrus.AMFICOM.general.IllegalDataException;
import com.syrus.AMFICOM.general.IllegalObjectEntityException;
import com.syrus.AMFICOM.general.ObjectEntities;
import com.syrus.AMFICOM.general.ObjectNotFoundException;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.CompletionStatus;
import com.syrus.AMFICOM.general.corba.ErrorCode;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.AnalysisTypeDatabase;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.EvaluationTypeDatabase;
import com.syrus.AMFICOM.measurement.MeasurementDatabaseContext;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementSetupDatabase;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.MeasurementTypeDatabase;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.SetDatabase;
import com.syrus.AMFICOM.measurement.Test;
import com.syrus.AMFICOM.measurement.TestDatabase;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.Log;

/**
 * @version $Revision: 1.1 $, $Date: 2004/09/10 14:35:30 $
 * @author $Author: bob $
 * @module cmserver_v1
 */
public class CMServerImpl implements CMServerOperations {

	public void receiveTests(Test_Transferable[] test_Transferables) throws AMFICOMRemoteException {
		Log.debugMessage("Received " + test_Transferables.length + " tests", Log.DEBUGLEVEL07);
		List testList = new ArrayList(test_Transferables.length);
		try {

			for (int i = 0; i < test_Transferables.length; i++) {
				Test test = new Test(test_Transferables[i]);
				testList.add(test);
			}

			TestDatabase testDatabase = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();
			testDatabase.insert(testList);

		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException();
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException();
		}
	}

	public void receiveSets(Set_Transferable[] set_Transferables) throws AMFICOMRemoteException {
		Log.debugMessage("Received " + set_Transferables.length + " sets", Log.DEBUGLEVEL07);
		List setList = new ArrayList(set_Transferables.length);
		try {

			for (int i = 0; i < set_Transferables.length; i++) {
				Set set = new Set(set_Transferables[i]);
				setList.add(set);
			}

			SetDatabase setDatabase = (SetDatabase) MeasurementDatabaseContext.getSetDatabase();
			setDatabase.insert(setList);

		} catch (CreateObjectException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException();
		} catch (IllegalDataException e) {
			Log.errorException(e);
			throw new AMFICOMRemoteException();
		}
	}

	public AnalysisType_Transferable transmitAnalysisType(Identifier_Transferable identifier_Transferable)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		try {
			AnalysisType analysisType = new AnalysisType(id);
			return (AnalysisType_Transferable) analysisType.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		}
	}

	public EvaluationType_Transferable transmitEvaluationType(Identifier_Transferable identifier_Transferable)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		try {
			EvaluationType evaluationType = new EvaluationType(id);
			return (EvaluationType_Transferable) evaluationType.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		}
	}

	public MeasurementSetup_Transferable transmitMeasurementSetup(Identifier_Transferable identifier_Transferable)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		try {
			MeasurementSetup measurementSetup = new MeasurementSetup(id);
			return (MeasurementSetup_Transferable) measurementSetup.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		}
	}

	public MeasurementType_Transferable transmitMeasurementType(Identifier_Transferable identifier_Transferable)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		try {
			MeasurementType measurementType = new MeasurementType(id);
			return (MeasurementType_Transferable) measurementType.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		}
	}

	public ParameterType_Transferable transmitParameterType(Identifier_Transferable identifier_Transferable)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		try {
			ParameterType parameterType = new ParameterType(id);
			return (ParameterType_Transferable) parameterType.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		}
	}

	public Set_Transferable transmitSet(Identifier_Transferable identifier_Transferable)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		try {
			Set set = new Set(id);
			return (Set_Transferable) set.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		}
	}

	public Test_Transferable transmitTest(Identifier_Transferable identifier_Transferable)
			throws AMFICOMRemoteException {
		Identifier id = new Identifier(identifier_Transferable);
		try {
			Test test = new Test(id);
			return (Test_Transferable) test.getTransferable();
		} catch (ObjectNotFoundException onfe) {
			Log.errorException(onfe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_NOT_FOUND, CompletionStatus.COMPLETED_YES,
								onfe.getMessage());
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		}
	}

	public AnalysisType_Transferable[] transmitAnalysisTypes(Identifier_Transferable[] identifier_Transferables)
			throws AMFICOMRemoteException {
		List idsList = new ArrayList(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			idsList.add(new Identifier(identifier_Transferables[i]));

		AnalysisTypeDatabase database = (AnalysisTypeDatabase) MeasurementDatabaseContext
				.getAnalysisTypeDatabase();

		List list = null;
		try {
			list = database.retrieveByIds(idsList, null);
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, ide.getMessage());
		}

		AnalysisType_Transferable[] transferables = new AnalysisType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			AnalysisType analysisType = (AnalysisType) it.next();
			transferables[i] = (AnalysisType_Transferable) analysisType.getTransferable();
		}
		return transferables;
	}

	public EvaluationType_Transferable[] transmitEvaluationTypes(Identifier_Transferable[] identifier_Transferables)
			throws AMFICOMRemoteException {
		List idsList = new ArrayList(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			idsList.add(new Identifier(identifier_Transferables[i]));

		EvaluationTypeDatabase database = (EvaluationTypeDatabase) MeasurementDatabaseContext
				.getEvaluationTypeDatabase();

		List list = null;
		try {
			list = database.retrieveByIds(idsList, null);
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, ide.getMessage());
		}

		EvaluationType_Transferable[] transferables = new EvaluationType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			EvaluationType evaluationType = (EvaluationType) it.next();
			transferables[i] = (EvaluationType_Transferable) evaluationType.getTransferable();
		}
		return transferables;
	}

	public MeasurementSetup_Transferable[] transmitMeasurementSetups(Identifier_Transferable[] identifier_Transferables)
			throws AMFICOMRemoteException {
		List idsList = new ArrayList(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			idsList.add(new Identifier(identifier_Transferables[i]));

		MeasurementSetupDatabase database = (MeasurementSetupDatabase) MeasurementDatabaseContext
				.getMeasurementSetupDatabase();

		List list = null;
		try {
			list = database.retrieveByIds(idsList, null);
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, ide.getMessage());
		}

		MeasurementSetup_Transferable[] transferables = new MeasurementSetup_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			MeasurementSetup measurementSetup = (MeasurementSetup) it.next();
			transferables[i] = (MeasurementSetup_Transferable) measurementSetup.getTransferable();
		}
		return transferables;
	}

	public MeasurementType_Transferable[] transmitMeasurementTypes(Identifier_Transferable[] identifier_Transferables)
			throws AMFICOMRemoteException {
		List idsList = new ArrayList(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			idsList.add(new Identifier(identifier_Transferables[i]));

		MeasurementTypeDatabase database = (MeasurementTypeDatabase) MeasurementDatabaseContext
				.getMeasurementTypeDatabase();

		List list = null;
		try {
			list = database.retrieveByIds(idsList, null);
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, ide.getMessage());
		}

		MeasurementType_Transferable[] transferables = new MeasurementType_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			MeasurementType measurementType = (MeasurementType) it.next();
			transferables[i] = (MeasurementType_Transferable) measurementType.getTransferable();
		}
		return transferables;
	}

	public Set_Transferable[] transmitSets(Identifier_Transferable[] identifier_Transferables)
			throws AMFICOMRemoteException {
		List idsList = new ArrayList(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			idsList.add(new Identifier(identifier_Transferables[i]));

		SetDatabase database = (SetDatabase) MeasurementDatabaseContext.getSetDatabase();

		List list = null;
		try {
			list = database.retrieveByIds(idsList, null);
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, ide.getMessage());
		}

		Set_Transferable[] transferables = new Set_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			Set set = (Set) it.next();
			transferables[i] = (Set_Transferable) set.getTransferable();
		}
		return transferables;
	}

	public Test_Transferable[] transmitTests(Identifier_Transferable[] identifier_Transferables)
			throws AMFICOMRemoteException {
		List idsList = new ArrayList(identifier_Transferables.length);
		for (int i = 0; i < identifier_Transferables.length; i++)
			idsList.add(new Identifier(identifier_Transferables[i]));

		TestDatabase database = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();

		List list = null;
		try {
			list = database.retrieveByIds(idsList, null);
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} catch (IllegalDataException ide) {
			Log.errorException(ide);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO, ide.getMessage());
		}

		Test_Transferable[] transferables = new Test_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			Test test = (Test) it.next();
			transferables[i] = (Test_Transferable) test.getTransferable();
		}
		return transferables;
	}

	public Test_Transferable[] transmitTestsByTime(long startTime, long endTime) throws AMFICOMRemoteException {
		TestDatabase database = (TestDatabase) MeasurementDatabaseContext.getTestDatabase();

		List list = null;
		try {
			list = database.retrieveByTimeRange(new Date(startTime), new Date(endTime));
		} catch (RetrieveObjectException roe) {
			Log.errorException(roe);
			throw new AMFICOMRemoteException(ErrorCode.ERROR_RETRIEVE, CompletionStatus.COMPLETED_NO, roe
					.getMessage());
		} 

		Test_Transferable[] transferables = new Test_Transferable[list.size()];
		int i = 0;
		for (Iterator it = list.iterator(); it.hasNext(); i++) {
			Test test = (Test) it.next();
			transferables[i] = (Test_Transferable) test.getTransferable();
		}
		return transferables;
	}

	///////////////////////////////////////// Identifier Generator
	// ////////////////////////////////////////////////
	public Identifier_Transferable getGeneratedIdentifier(short entityCode) throws AMFICOMRemoteException {
		try {
			Identifier identifier = IdentifierGenerator.generateIdentifier(entityCode);
			return (Identifier_Transferable) identifier.getTransferable();
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(
								ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO,
								"Illegal object entity: '"
										+ ObjectEntities
												.codeToString(entityCode)
										+ "'");
		} catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(
								ErrorCode.ERROR_RETRIEVE,
								CompletionStatus.COMPLETED_NO,
								"Cannot create major/minor entries of identifier for entity: '"
										+ ObjectEntities
												.codeToString(entityCode)
										+ "' -- " + ige.getMessage());
		}
	}

	public Identifier_Transferable[] getGeneratedIdentifierRange(short entityCode, int size)
			throws AMFICOMRemoteException {
		try {
			Identifier[] identifiers = IdentifierGenerator.generateIdentifierRange(entityCode, size);
			Identifier_Transferable[] identifiersT = new Identifier_Transferable[identifiers.length];
			for (int i = 0; i < identifiersT.length; i++)
				identifiersT[i] = (Identifier_Transferable) identifiers[i].getTransferable();
			return identifiersT;
		} catch (IllegalObjectEntityException ioee) {
			Log.errorException(ioee);
			throw new AMFICOMRemoteException(
								ErrorCode.ERROR_ILLEGAL_OBJECT_ENTITY,
								CompletionStatus.COMPLETED_NO,
								"Illegal object entity: '"
										+ ObjectEntities
												.codeToString(entityCode)
										+ "'");
		} catch (IdentifierGenerationException ige) {
			Log.errorException(ige);
			throw new AMFICOMRemoteException(
								ErrorCode.ERROR_RETRIEVE,
								CompletionStatus.COMPLETED_NO,
								"Cannot create major/minor entries of identifier for entity: '"
										+ ObjectEntities
												.codeToString(entityCode)
										+ "' -- " + ige.getMessage());
		}
	}

}
