/*
 * $Id: ClientMeasurementObjectLoader.java,v 1.2 2004/09/22 08:08:14 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver.test;

import java.util.List;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.DatabaseException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.RetrieveObjectException;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.Analysis;
import com.syrus.AMFICOM.measurement.AnalysisType;
import com.syrus.AMFICOM.measurement.Evaluation;
import com.syrus.AMFICOM.measurement.EvaluationType;
import com.syrus.AMFICOM.measurement.Measurement;
import com.syrus.AMFICOM.measurement.MeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementSetup;
import com.syrus.AMFICOM.measurement.MeasurementType;
import com.syrus.AMFICOM.measurement.ParameterType;
import com.syrus.AMFICOM.measurement.Result;
import com.syrus.AMFICOM.measurement.Set;
import com.syrus.AMFICOM.measurement.TemporalPattern;
import com.syrus.AMFICOM.measurement.Test;

/**
 * @version $Revision: 1.2 $, $Date: 2004/09/22 08:08:14 $
 * @author $Author: bob $
 * @module mcm_v1
 */

public final class ClientMeasurementObjectLoader implements MeasurementObjectLoader {

	private CMServer				server;

	private static AccessIdentifier_Transferable	accessIdentifierTransferable;

	public ClientMeasurementObjectLoader(CMServer server) {
		this.server = server;
	}

	public static void setAccessIdentifierTransferable(AccessIdentifier_Transferable accessIdentifier_Transferable) {
		accessIdentifierTransferable = accessIdentifier_Transferable;
	}

	public ParameterType loadParameterType(Identifier id) throws RetrieveObjectException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public MeasurementType loadMeasurementType(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		throw new UnsupportedOperationException();
	}

	public AnalysisType loadAnalysisType(Identifier id) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public EvaluationType loadEvaluationType(Identifier id) throws RetrieveObjectException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public Set loadSet(Identifier id) throws RetrieveObjectException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public MeasurementSetup loadMeasurementSetup(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		throw new UnsupportedOperationException();
	}

	public Measurement loadMeasurement(Identifier id) throws DatabaseException {
		throw new UnsupportedOperationException();
	}

	public Analysis loadAnalysis(Identifier id) throws DatabaseException {
		throw new UnsupportedOperationException();
	}

	public Evaluation loadEvaluation(Identifier id) throws DatabaseException {
		throw new UnsupportedOperationException();
	}

	public Test loadTest(Identifier id) throws DatabaseException {
		throw new UnsupportedOperationException();
	}

	public Result loadResult(Identifier id) throws DatabaseException {
		throw new UnsupportedOperationException();
	}

	public TemporalPattern loadTemporalPattern(Identifier id) throws RetrieveObjectException,
			CommunicationException {
		try {
			return new TemporalPattern(this.server.transmitTemporalPattern((Identifier_Transferable) id
					.getTransferable(), accessIdentifierTransferable));
		} catch (CreateObjectException e) {
			String msg = "ClientMeasurementObjectLoader.loadTemporalPattern | new TemporalPattern("
					+ id.toString() + ")";
			throw new RetrieveObjectException(msg, e);
		} catch (AMFICOMRemoteException e) {
			String msg = "ClientMeasurementObjectLoader.loadTemporalPattern | server.transmitTemporalPattern("
					+ id.toString() + ")";
			throw new CommunicationException(msg, e);
		}
	}

	public List loadAnalyses(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadAnalysisTypes(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadEvaluations(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadEvaluationTypes(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadMeasurements(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadMeasurementSetups(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadMeasurementTypes(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadParameterTypes(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadResults(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadSets(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadTemporalPatterns(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}

	public List loadTests(List ids) throws DatabaseException, CommunicationException {
		throw new UnsupportedOperationException();
	}
}
