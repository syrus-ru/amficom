/*
 * $Id: CMServerTestCase.java,v 1.1 2004/09/21 08:18:50 bob Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Научно-технический центр.
 * Проект: АМФИКОМ.
 */

package com.syrus.AMFICOM.cmserver.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

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
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.general.CreateObjectException;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.AMFICOMRemoteException;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.corba.JavaSoftORBUtil;

/**
 * @version $Revision: 1.1 $, $Date: 2004/09/21 08:18:50 $
 * @author $Author: bob $
 * @module module
 */
public class CMServerTestCase extends TestCase {

	private static CMServer				server;
	private static AccessIdentifier_Transferable	accessIdentifier_Transferable;

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

			System.out.println("server reference have got : " + server.toString());

			accessIdentifier_Transferable = new AccessIdentifier_Transferable();

			Identifier id = new Identifier("Null_0");
			accessIdentifier_Transferable.domain_id = (Identifier_Transferable) id.getTransferable();
			accessIdentifier_Transferable.user_id = (Identifier_Transferable) id.getTransferable();
			accessIdentifier_Transferable.session_id = (Identifier_Transferable) id.getTransferable();

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

	public void testRetriveDomain() throws AMFICOMRemoteException, CreateObjectException {

		long time0 = System.currentTimeMillis();
		Domain_Transferable[] domain_Transferables = server.transmitDomains(new Identifier_Transferable[0],
											accessIdentifier_Transferable);
		long time1 = System.currentTimeMillis();
		System.out.println("retrieve " + domain_Transferables.length + " item(s) for " + (time1 - time0)
				+ " ms");
		for (int i = 0; i < domain_Transferables.length; i++) {
			Domain domain = new Domain(domain_Transferables[i]);
		}
		long time2 = System.currentTimeMillis();
		Domain_Transferable[] domain_Transferables2 = server.transmitDomains(new Identifier_Transferable[0],
											accessIdentifier_Transferable);
		long time3 = System.currentTimeMillis();
		System.out.println("retrieve " + domain_Transferables2.length + " item(s) for " + (time3 - time2)
				+ " ms");

	}

}
