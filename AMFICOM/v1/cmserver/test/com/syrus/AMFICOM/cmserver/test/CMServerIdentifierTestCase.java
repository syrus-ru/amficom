/*
 * $Id: CMServerIdentifierTestCase.java,v 1.1 2004/09/24 09:38:55 bob Exp $
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
import org.omg.CORBA.ORBPackage.InvalidName;
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
import com.syrus.AMFICOM.general.CORBAServer;
import com.syrus.AMFICOM.general.CommunicationException;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.ObjectEntities;
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
import com.syrus.util.ClientLRUMap;
import com.syrus.util.corba.JavaSoftORBUtil;

/**
 * @version $Revision: 1.1 $, $Date: 2004/09/24 09:38:55 $
 * @author $Author: bob $
 * @module module
 */
public class CMServerIdentifierTestCase extends TestCase {

	private static CMServer				server;
	private static AccessIdentifier_Transferable	accessIdentifier_Transferable;

	private static AccessIdentifier_Transferable[]	accessIdentifier_Transferables	= new AccessIdentifier_Transferable[3];

	public CMServerIdentifierTestCase(String name) {
		super(name);
	}

	public static void main(java.lang.String[] args) {
		Class clazz = CMServerIdentifierTestCase.class;
		junit.awtui.TestRunner.run(clazz);
		//		junit.swingui.TestRunner.run(clazz);
		//		junit.textui.TestRunner.run(clazz);
	}

	public static Test suite() {
		return suiteWrapper(CMServerIdentifierTestCase.class);
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

			if (server == null)
				fail("server reference is null");

			//			CORBAServer corbaServer = new CORBAServer();
			//			
			//			server =
			// CMServerHelper.narrow(corbaServer.resolveReference("CMServer"));

			// initialize pool
			MeasurementStorableObjectPool.init(new ClientMeasurementObjectLoader(server),
								ClientLRUMap.class, 200);
			ConfigurationStorableObjectPool.init(new ClientConfigurationObjectLoader(server),
								ClientLRUMap.class, 200);
			IdentifierPool.init(server, 2);

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

		}
		//		catch (CommunicationException e) {
		//			e.printStackTrace();
		//			fail(e.getMessage());
		//		}
		catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	static void oneTimeTearDown() {
	}

	public void testGenerateIdentifiers() throws AMFICOMRemoteException {
		for (int i = 0; i < 5; i++) {
			Identifier id = IdentifierPool.generateId(ObjectEntities.TEST_ENTITY_CODE);
			System.out.println(id.toString());
		}

	}

}
