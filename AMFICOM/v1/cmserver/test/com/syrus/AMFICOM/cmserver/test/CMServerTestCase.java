/*
 * $Id: CMServerTestCase.java,v 1.5 2004/09/22 08:08:06 bob Exp $
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
import com.syrus.AMFICOM.configuration.DatabaseConfigurationObjectLoader;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.AMFICOM.measurement.DatabaseMeasurementObjectLoader;
import com.syrus.AMFICOM.measurement.MeasurementStorableObjectPool;
import com.syrus.AMFICOM.measurement.corba.MeasurementSetup_Transferable;
import com.syrus.AMFICOM.measurement.corba.Measurement_Transferable;
import com.syrus.AMFICOM.measurement.corba.Test_Transferable;
import com.syrus.util.corba.JavaSoftORBUtil;

/**
 * @version $Revision: 1.5 $, $Date: 2004/09/22 08:08:06 $
 * @author $Author: bob $
 * @module module
 */
public class CMServerTestCase extends TestCase {

	private static CMServer				server;
	private static AccessIdentifier_Transferable	accessIdentifier_Transferable;
	private static AccessIdentifier_Transferable[]  accessIdentifier_Transferables = new AccessIdentifier_Transferable[3];
	
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

	public void testTransmitDomain() throws AMFICOMRemoteException, CreateObjectException {

		long time0 = System.currentTimeMillis();
		Domain_Transferable[] domain_Transferables = server.transmitDomains(new Identifier_Transferable[0],
											accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("transmit " + domain_Transferables.length + " domain(s) for " + (time1 - time0)
				+ " ms");

		for (int i = 0; i < domain_Transferables.length; i++) {
			Domain domain = new Domain(domain_Transferables[i]);
		}
		long time2 = System.currentTimeMillis();
		Domain_Transferable[] domain_Transferables2 = server.transmitDomains(new Identifier_Transferable[0],
											accessIdentifier_Transferable);
		long time3 = System.currentTimeMillis();
		System.out.println("transmit " + domain_Transferables2.length + " domain(s) for " + (time3 - time2)
				+ " ms");

		Identifier_Transferable[] identifier_Transferable = new Identifier_Transferable[domain_Transferables2.length];
		for (int i = 0; i < domain_Transferables2.length; i++) {
			identifier_Transferable[i] = domain_Transferables2[i].id;
		}

		long time4 = System.currentTimeMillis();
		Domain_Transferable[] domain_Transferables3 = server.transmitDomains(identifier_Transferable,
											accessIdentifier_Transferable);
		long time5 = System.currentTimeMillis();
		System.out.println("transmit " + domain_Transferables3.length + " domain(s) by ids for "
				+ (time5 - time4) + " ms");
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

	public void _testTransmitTestsAndMeasurements() throws AMFICOMRemoteException, CreateObjectException {
		long time0 = System.currentTimeMillis();
		// 2 month ago
		Date start = new Date(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 31 * 2);
		Date end = new Date(System.currentTimeMillis());
		Test_Transferable[] test_Transferables = server.transmitTestsByTime(start.getTime(), end.getTime(),
											accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("transmit " + test_Transferables.length + " test(s) for " + (time1 - time0) + " ms");

		Identifier_Transferable[] identifier_Transferables = new Identifier_Transferable[test_Transferables.length];
		for (int i = 0; i < identifier_Transferables.length; i++) {
			com.syrus.AMFICOM.measurement.Test test = new com.syrus.AMFICOM.measurement.Test(
														test_Transferables[i]);
			identifier_Transferables[i] = test_Transferables[i].id;
			System.out.println("test " + test.getId().toString() + " status :" + test.getStatus());
		}
		long time2 = System.currentTimeMillis();
		Measurement_Transferable[] measurement_Transferables = server
				.transmitMeasurementForTests(identifier_Transferables, accessIdentifier_Transferable);
		long time3 = System.currentTimeMillis();
		System.out.println("transmit " + measurement_Transferables.length + " measuremen(s) for "
				+ (time3 - time2) + " ms");

	}

}
