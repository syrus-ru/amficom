/*
 * $Id: CMServerTestCase.java,v 1.9 2004/09/23 08:28:01 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import junit.extensions.TestSetup;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.omg.CORBA.ORB;
import org.omg.CORBA.UserException;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.cmserver.corba.CMServerHelper;
import com.syrus.AMFICOM.configuration.ConfigurationStorableObjectPool;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.configuration.corba.MonitoredElement_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.corba.AnalysisType_Transferable;
import com.syrus.AMFICOM.measurement.corba.EvaluationType_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.MeasurementType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.ParameterType_Transferable;
import com.syrus.AMFICOM.measurement.corba.Result_Transferable;
import com.syrus.AMFICOM.measurement.corba.Set_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.corba.JavaSoftORBUtil;

/**
 * @version $Revision: 1.9 $, $Date: 2004/09/23 08:28:01 $
 * @author $Author: bob $
 * @module module
 */
public class CMServerTestCase extends TestCase {

	private static CMServer				server;
	private static AccessIdentifier_Transferable	accessIdentifier_Transferable;

	private static AccessIdentifier_Transferable[]	accessIdentifier_Transferables	= new AccessIdentifier_Transferable[3];

	public CMServerTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = CMServerTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(CMServerTestCase.class);
	}

	private static Test suiteWrapper(Class clazz) {
		TestSuite suite = new TestSuite(clazz);
		TestSetup wrapper = new TestSetup(suite) {

			protected void setUp() {
				oneTimeSetUp();
			}

			protected void tearDown() {
				oneTimeTearDown();
			}
		};
		return wrapper;
	}

	static void oneTimeSetUp() {
		try {
			ORB orb = JavaSoftORBUtil.getInstance().getORB();

			POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
			rootPOA.the_POAManager().activate();
			NamingContextExt rootNamingCtx = NamingContextExtHelper.narrow(orb
					.resolve_initial_references("NameService"));

			final String hostName = InetAddress.getLocalHost().getCanonicalHostName()
					.replaceAll("\\.", "_");

			server = CMServerHelper.narrow(rootNamingCtx.resolve_str(hostName + "/CMServer"));

			// initialize pool
			MeasurementStorableObjectPool.init(new ClientMeasurementObjectLoader(server), 200);
			ConfigurationStorableObjectPool.init(new ClientConfigurationObjectLoader(server), 200);

			System.out.println("server reference have got : \n" + server.toString());

			accessIdentifier_Transferable = new AccessIdentifier_Transferable();

			for (int i = 0; i < accessIdentifier_Transferables.length; i++) {
				accessIdentifier_Transferables[i] = new AccessIdentifier_Transferable();
			}

			Identifier id = new Identifier("Null_0");

			Identifier domainId = new Identifier("Domain_19");
			accessIdentifier_Transferable.domain_id = (Identifier_Transferable) domainId.getTransferable();
			accessIdentifier_Transferable.user_id = (Identifier_Transferable) id.getTransferable();
			accessIdentifier_Transferable.session_id = (Identifier_Transferable) id.getTransferable();

			ClientMeasurementObjectLoader.setAccessIdentifierTransferable(accessIdentifier_Transferable);
			ClientConfigurationObjectLoader.setAccessIdentifierTransferable(accessIdentifier_Transferable);

		} catch (UserException ue) {
			ue.printStackTrace();
			fail(ue.getMessage());
		} catch (UnknownHostException uhe) {
			uhe.printStackTrace();
			fail(uhe.getMessage());
		}
	}

	static void oneTimeTearDown() {
	}

	public void _testTransmitDomainX() throws AMFICOMRemoteException, CreateObjectException {

		//      Checking method transmitDomains(null , acc)
		System.out.println("Checking method transmitDomainX");
		Identifier_Transferable identifier_Transferables[] = new Identifier_Transferable[0];
		long time0 = System.currentTimeMillis();
		Domain_Transferable[] domain_Transferables = server.transmitDomains(identifier_Transferables,
											accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("1. transmit " + domain_Transferables.length + " identifier_Transferabl(es) for "
				+ (time1 - time0) + " ms");

		//      Checking method transmitDomain(Id_Trans , acc)
		Domain_Transferable domain_Transferable = domain_Transferables[0];
		Identifier_Transferable id_Tf = domain_Transferable.id;
		long time2 = System.currentTimeMillis();
		Domain_Transferable domain_Transferable2 = server.transmitDomain(id_Tf, accessIdentifier_Transferable);
		long time3 = System.currentTimeMillis();
		System.out.println("2. transmit " + "Domain_Transferables" + "  for " + (time3 - time2) + " ms");
		//      Checking method transmitDomain(Id_Trans[] , acc)
		Identifier_Transferable[] identifier_Transferables2 = new Identifier_Transferable[2];
		identifier_Transferables2[0] = id_Tf;
		identifier_Transferables2[1] = id_Tf;
		long time4 = System.currentTimeMillis();
		Domain_Transferable[] domain_Transferables3 = server.transmitDomains(identifier_Transferables2,
											accessIdentifier_Transferable);
		long time5 = System.currentTimeMillis();
		System.out.println("3. transmit " + identifier_Transferables2.length + "  for " + (time5 - time4)
				+ " ms");
	}

	public void _testTransmitAnalysisTypeX() throws AMFICOMRemoteException {
		//      Checking method transmitAnalysisTypes(null , acc)
		System.out.println("Checking method transmitAnalysisTypeX");
		Identifier_Transferable identifier_Transferables[] = new Identifier_Transferable[0];
		AnalysisType_Transferable[] analysisType_Transferables;
		long time0 = System.currentTimeMillis();
		analysisType_Transferables = server.transmitAnalysisTypes(identifier_Transferables,
										accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("1. transmit " + analysisType_Transferables.length
				+ " identifier_Transferabl(es) for " + (time1 - time0) + " ms");

		//      Checking method transmitAnalysisType(Id_Trans , acc)
		AnalysisType_Transferable analysisType_Transferable = analysisType_Transferables[0];
		Identifier_Transferable id_Tf = analysisType_Transferable.id;
		long time2 = System.currentTimeMillis();
		AnalysisType_Transferable analysisType_Transferable2 = server
				.transmitAnalysisType(id_Tf, accessIdentifier_Transferable);
		long time3 = System.currentTimeMillis();
		System.out.println("2. transmit " + "AnalysisType_Transferables" + "  for " + (time3 - time2) + " ms");
		//      Checking method transmitAnalysisType(Id_Trans[] , acc)
		Identifier_Transferable[] identifier_Transferables2 = new Identifier_Transferable[2];
		identifier_Transferables2[0] = id_Tf;
		identifier_Transferables2[1] = id_Tf;
		long time4 = System.currentTimeMillis();
		AnalysisType_Transferable[] analysisType_Transferables3 = server
				.transmitAnalysisTypes(identifier_Transferables2, accessIdentifier_Transferable);
		long time5 = System.currentTimeMillis();
		System.out.println("3. transmit " + identifier_Transferables2.length + "  for " + (time5 - time4)
				+ " ms");
	}

	public void _testTransmitEvaluationTypeX() throws AMFICOMRemoteException {
		//      Checking method transmitEvaluationTypes(null , acc)
		System.out.println("Checking method transmitEvaluationTypeX");
		Identifier_Transferable identifier_Transferables[] = new Identifier_Transferable[0];
		long time0 = System.currentTimeMillis();
		EvaluationType_Transferable[] evaluationType_Transferables = server
				.transmitEvaluationTypes(identifier_Transferables, accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("1. transmit " + evaluationType_Transferables.length
				+ " identifier_Transferabl(es) for " + (time1 - time0) + " ms");

		//      Checking method transmitEvaluationType(Id_Trans , acc)
		EvaluationType_Transferable EvaluationType_Transferable = evaluationType_Transferables[0];
		Identifier_Transferable id_Tf = EvaluationType_Transferable.id;
		long time2 = System.currentTimeMillis();
		EvaluationType_Transferable evaluationType_Transferable2 = server
				.transmitEvaluationType(id_Tf, accessIdentifier_Transferable);
		long time3 = System.currentTimeMillis();
		System.out
				.println("2. transmit " + "EvaluationType_Transferables" + "  for " + (time3 - time2)
						+ " ms");
		//      Checking method transmitEvaluationType(Id_Trans[] , acc)
		Identifier_Transferable[] identifier_Transferables2 = new Identifier_Transferable[2];
		identifier_Transferables2[0] = id_Tf;
		identifier_Transferables2[1] = id_Tf;
		long time4 = System.currentTimeMillis();
		EvaluationType_Transferable[] evaluationType_Transferables3 = server
				.transmitEvaluationTypes(identifier_Transferables2, accessIdentifier_Transferable);
		long time5 = System.currentTimeMillis();
		System.out.println("3. transmit " + identifier_Transferables2.length + "  for " + (time5 - time4)
				+ " ms");
	}

	public void _testTransmitMeasurementTypeX() throws AMFICOMRemoteException {
		//      Checking method transmitMeasurementTypes(null , acc)
		System.out.println("Checking method transmitMeasurementTypeX");
		Identifier_Transferable identifier_Transferables[] = new Identifier_Transferable[0];
		long time0 = System.currentTimeMillis();
		MeasurementType_Transferable[] measurementType_Transferables = server
				.transmitMeasurementTypes(identifier_Transferables, accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("1. transmit " + measurementType_Transferables.length
				+ " identifier_Transferabl(es) for " + (time1 - time0) + " ms");

		//      Checking method transmitMeasurementType(Id_Trans , acc)
		MeasurementType_Transferable MeasurementType_Transferable = measurementType_Transferables[0];
		Identifier_Transferable id_Tf = MeasurementType_Transferable.id;
		long time2 = System.currentTimeMillis();
		MeasurementType_Transferable measurementType_Transferable2 = server
				.transmitMeasurementType(id_Tf, accessIdentifier_Transferable);
		long time3 = System.currentTimeMillis();
		System.out.println("2. transmit " + "MeasurementType_Transferables" + "  for " + (time3 - time2)
				+ " ms");
		//      Checking method transmitMeasurementType(Id_Trans[] , acc)
		Identifier_Transferable[] identifier_Transferables2 = new Identifier_Transferable[2];
		identifier_Transferables2[0] = id_Tf;
		identifier_Transferables2[1] = id_Tf;
		long time4 = System.currentTimeMillis();
		MeasurementType_Transferable[] measurementType_Transferables3 = server
				.transmitMeasurementTypes(identifier_Transferables2, accessIdentifier_Transferable);
		long time5 = System.currentTimeMillis();
		System.out.println("3. transmit " + identifier_Transferables2.length + "  for " + (time5 - time4)
				+ " ms");
	}

	public void testTransmitMeasurementX() throws AMFICOMRemoteException {
		//      Checking method transmitMeasurements(null , acc)
		System.out.println("Checking method transmitMeasurementX");
		Identifier_Transferable identifier_Transferables[] = new Identifier_Transferable[0];
		long time0 = System.currentTimeMillis();
		Measurement_Transferable[] measurement_Transferables = server
				.transmitMeasurements(identifier_Transferables, accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("1. transmit " + measurement_Transferables.length
				+ " identifier_Transferabl(es) for " + (time1 - time0) + " ms");

		//      Checking method transmitMeasurement(Id_Trans , acc)
		Measurement_Transferable measurement_Transferable = measurement_Transferables[0];
		Identifier_Transferable id_Tf = measurement_Transferable.id;
		long time2 = System.currentTimeMillis();
		Measurement_Transferable measurement_Transferable2 = server
				.transmitMeasurement(id_Tf, accessIdentifier_Transferable);
		long time3 = System.currentTimeMillis();
		System.out.println("2. transmit " + "Measurement_Transferables" + "  for " + (time3 - time2) + " ms");
		//      Checking method transmitMeasurement(Id_Trans[] , acc)
		Identifier_Transferable[] identifier_Transferables2 = new Identifier_Transferable[2];
		identifier_Transferables2[0] = id_Tf;
		identifier_Transferables2[1] = id_Tf;
		long time4 = System.currentTimeMillis();
		Measurement_Transferable[] measurement_Transferables3 = server
				.transmitMeasurements(identifier_Transferables2, accessIdentifier_Transferable);
		long time5 = System.currentTimeMillis();
		System.out.println("3. transmit " + identifier_Transferables2.length + "  for " + (time5 - time4)
				+ " ms");
	}

	public void _testTransmitMonitoredElementX() throws AMFICOMRemoteException {
		//      Checking method transmitMonitoredElements(null , acc)
		System.out.println("Checking method transmitMonitoredElementX");
		Identifier_Transferable identifier_Transferables[] = new Identifier_Transferable[0];
		long time0 = System.currentTimeMillis();
		MonitoredElement_Transferable[] monitoredElement_Transferables = server
				.transmitMonitoredElements(identifier_Transferables, accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("1. transmit " + monitoredElement_Transferables.length
				+ " identifier_Transferabl(es) for " + (time1 - time0) + " ms");

		//      Checking method transmitMonitoredElement(Id_Trans , acc)
		MonitoredElement_Transferable monitoredElement_Transferable = monitoredElement_Transferables[0];
		Identifier_Transferable id_Tf = monitoredElement_Transferable.id;
		long time2 = System.currentTimeMillis();
		MonitoredElement_Transferable monitoredElement_Transferable2 = server
				.transmitMonitoredElement(id_Tf, accessIdentifier_Transferable);
		long time3 = System.currentTimeMillis();
		System.out.println("2. transmit " + "MonitoredElement_Transferables" + "  for " + (time3 - time2)
				+ " ms");
		//      Checking method transmitMonitoredElement(Id_Trans[] , acc)
		Identifier_Transferable[] identifier_Transferables2 = new Identifier_Transferable[2];
		identifier_Transferables2[0] = id_Tf;
		identifier_Transferables2[1] = id_Tf;
		long time4 = System.currentTimeMillis();
		MonitoredElement_Transferable[] monitoredElement_Transferables3 = server
				.transmitMonitoredElements(identifier_Transferables2, accessIdentifier_Transferable);
		long time5 = System.currentTimeMillis();
		System.out.println("3. transmit " + identifier_Transferables2.length + "  for " + (time5 - time4)
				+ " ms");
	}

	public void _testTransmitParameterTypeX() throws AMFICOMRemoteException {
		//      Checking method transmitParameterTypes(null , acc)
		System.out.println("Checking method transmitParameterTypeX");
		Identifier_Transferable identifier_Transferables[] = new Identifier_Transferable[0];
		long time0 = System.currentTimeMillis();
		ParameterType_Transferable[] parameterType_Transferables = server
				.transmitParameterTypes(identifier_Transferables, accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("1. transmit " + parameterType_Transferables.length
				+ " identifier_Transferabl(es) for " + (time1 - time0) + " ms");

		//      Checking method transmitParameterType(Id_Trans , acc)
		ParameterType_Transferable parameterType_Transferable = parameterType_Transferables[0];
		Identifier_Transferable id_Tf = parameterType_Transferable.id;
		long time2 = System.currentTimeMillis();
		ParameterType_Transferable ParameterType_Transferable2 = server
				.transmitParameterType(id_Tf, accessIdentifier_Transferable);
		long time3 = System.currentTimeMillis();
		System.out.println("2. transmit " + "ParameterType_Transferables" + "  for " + (time3 - time2) + " ms");
		//      Checking method transmitParameterType(Id_Trans[] , acc)
		Identifier_Transferable[] identifier_Transferables2 = new Identifier_Transferable[2];
		identifier_Transferables2[0] = id_Tf;
		identifier_Transferables2[1] = id_Tf;
		long time4 = System.currentTimeMillis();
		ParameterType_Transferable[] parameterType_Transferables3 = server
				.transmitParameterTypes(identifier_Transferables2, accessIdentifier_Transferable);
		long time5 = System.currentTimeMillis();
		System.out.println("3. transmit " + identifier_Transferables2.length + "  for " + (time5 - time4)
				+ " ms");
	}

	public void _testTransmitResultX() throws AMFICOMRemoteException {
		//      Checking method transmitResults(null , acc)
		System.out.println("Checking method transmitResultX");
		Identifier_Transferable identifier_Transferables[] = new Identifier_Transferable[0];
		long time0 = System.currentTimeMillis();
		Result_Transferable[] result_Transferables = server.transmitResults(identifier_Transferables,
											accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("1. transmit " + result_Transferables.length + " identifier_Transferabl(es) for "
				+ (time1 - time0) + " ms");

		//      Checking method transmitResult(Id_Trans , acc)
		if (result_Transferables.length > 0) {
			Result_Transferable result_Transferable = result_Transferables[0];
			Identifier_Transferable id_Tf = result_Transferable.id;
			long time2 = System.currentTimeMillis();
			Result_Transferable result_Transferable2 = server
					.transmitResult(id_Tf, accessIdentifier_Transferable);
			long time3 = System.currentTimeMillis();
			System.out
					.println("2. transmit " + "Result_Transferables" + "  for " + (time3 - time2)
							+ " ms");
			//      Checking method transmitResult(Id_Trans[] , acc)
			Identifier_Transferable[] identifier_Transferables2 = new Identifier_Transferable[2];
			identifier_Transferables2[0] = id_Tf;
			identifier_Transferables2[1] = id_Tf;
			long time4 = System.currentTimeMillis();
			Result_Transferable[] result_Transferables3 = server
					.transmitResults(identifier_Transferables2, accessIdentifier_Transferable);
			long time5 = System.currentTimeMillis();
			System.out.println("3. transmit " + identifier_Transferables2.length + "  for "
					+ (time5 - time4) + " ms");
		}
	}

	public void _testTransmitTestX() throws AMFICOMRemoteException {
		//      Checking method transmitTests(null , acc)
		System.out.println("Checking method transmitTestX");
		Identifier_Transferable identifier_Transferables[] = new Identifier_Transferable[0];
		long time0 = System.currentTimeMillis();
		Test_Transferable[] Test_Transferables = server.transmitTests(identifier_Transferables,
										accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("1. transmit " + Test_Transferables.length + " identifier_Transferabl(es) for "
				+ (time1 - time0) + " ms");

		//      Checking method transmitTest(Id_Trans , acc)
		Test_Transferable test_Transferable = Test_Transferables[0];
		Identifier_Transferable id_Tf = test_Transferable.id;
		long time2 = System.currentTimeMillis();
		Test_Transferable test_Transferable2 = server.transmitTest(id_Tf, accessIdentifier_Transferable);
		long time3 = System.currentTimeMillis();
		System.out.println("2. transmit " + "Test_Transferables" + "  for " + (time3 - time2) + " ms");
		//      Checking method transmitTest(Id_Trans[] , acc)
		Identifier_Transferable[] identifier_Transferables2 = new Identifier_Transferable[2];
		identifier_Transferables2[0] = id_Tf;
		identifier_Transferables2[1] = id_Tf;
		long time4 = System.currentTimeMillis();
		Test_Transferable[] test_Transferables3 = server.transmitTests(identifier_Transferables2,
										accessIdentifier_Transferable);
		long time5 = System.currentTimeMillis();
		System.out.println("3. transmit " + identifier_Transferables2.length + "  for " + (time5 - time4)
				+ " ms");
	}

	public void _testTransmitSetX() throws AMFICOMRemoteException {
		//      Checking method transmitSets(null , acc)
		System.out.println("Checking method transmitSetX");
		Identifier_Transferable identifier_Transferables[] = new Identifier_Transferable[0];
		long time0 = System.currentTimeMillis();
		Set_Transferable[] set_Transferables = server.transmitSets(identifier_Transferables,
										accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("1. transmit " + set_Transferables.length + " identifier_Transferabl(es) for "
				+ (time1 - time0) + " ms");

		//      Checking method transmitSet(Id_Trans , acc)
		Set_Transferable set_Transferable = set_Transferables[0];
		Identifier_Transferable id_Tf = set_Transferable.id;
		long time2 = System.currentTimeMillis();
		Set_Transferable set_Transferable2 = server.transmitSet(id_Tf, accessIdentifier_Transferable);
		long time3 = System.currentTimeMillis();
		System.out.println("2. transmit " + "Set_Transferables" + "  for " + (time3 - time2) + " ms");
		//      Checking method transmitSet(Id_Trans[] , acc)
		Identifier_Transferable[] identifier_Transferables2 = new Identifier_Transferable[2];
		identifier_Transferables2[0] = id_Tf;
		identifier_Transferables2[1] = id_Tf;
		long time4 = System.currentTimeMillis();
		Set_Transferable[] set_Transferables3 = server.transmitSets(identifier_Transferables2,
										accessIdentifier_Transferable);
		long time5 = System.currentTimeMillis();
		System.out.println("3. transmit " + identifier_Transferables2.length + "  for " + (time5 - time4)
				+ " ms");
	}

	public void _testTransmitMeasurementSetup() throws AMFICOMRemoteException {

		long time0 = System.currentTimeMillis();
		MeasurementSetup_Transferable[] measurementSetup_Transferables = server
				.transmitMeasurementSetups(new Identifier_Transferable[0],
								accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("transmit " + measurementSetup_Transferables.length + " measurement setup(s) for "
				+ (time1 - time0) + " ms");
	}

	public void _testTransmitResults() throws AMFICOMRemoteException {
		long time0 = System.currentTimeMillis();
		Result_Transferable[] result_Transferables = server.transmitResults(new Identifier_Transferable[0],
											accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("transmit " + result_Transferables.length + " result(s) for " + (time1 - time0)
				+ " ms");
	}

	public void _testTransmitTestsAndMeasurements() throws AMFICOMRemoteException, CreateObjectException {

		long time0 = System.currentTimeMillis();
		// 2 month ago
		long diff = 1000L * 60L * 60L * 24L * 31L * 2L;
		Date start = new Date(System.currentTimeMillis() - diff);
		Date end = new Date(System.currentTimeMillis());
		
		System.out.println("start:" + start.toString());
		System.out.println("end:" + end.toString());

		Test_Transferable[] test_Transferables = server.transmitTestsByTime(start.getTime(), end.getTime(),
											accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("transmit " + test_Transferables.length + " test(s) for " + (time1 - time0) + " ms");

		Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[test_Transferables.length];
		for (int i = 0; i < identifier_Transferables.length; i++) {
			com.syrus.AMFICOM.measurement.Test test = new com.syrus.AMFICOM.measurement.Test(
														test_Transferables[i]);
			identifier_Transferables[i] = test_Transferables[i].id;
			System.out.println("test " + test.getId().toString() + " status :" + test.getStatus().value());
		}
		long time2 = System.currentTimeMillis();
		Measurement_Transferable[] measurement_Transferables = server
				.transmitMeasurementForTests(identifier_Transferables, accessIdentifier_Transferable);
		long time3 = System.currentTimeMillis();
		System.out.println("transmit " + measurement_Transferables.length + " measuremen(s) for "
				+ (time3 - time2) + " ms");

	}

}
