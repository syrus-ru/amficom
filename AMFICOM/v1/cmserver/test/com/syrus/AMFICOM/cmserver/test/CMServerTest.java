/*
 * CMServerTest.java
 * Created on 20.09.2004 14:37:35
 * 
 */

package com.syrus.AMFICOM.cmserver.test;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.omg.CORBA.ORB;
import org.omg.CORBA.UserException;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;

import com.syrus.AMFICOM.CORBA.General.AMFICOMRemoteException;
import com.syrus.AMFICOM.cmserver.corba.CMServer;
import com.syrus.AMFICOM.cmserver.corba.CMServerHelper;
import com.syrus.AMFICOM.configuration.Domain;
import com.syrus.AMFICOM.configuration.corba.AccessIdentifier_Transferable;
import com.syrus.AMFICOM.configuration.corba.Domain_Transferable;
import com.syrus.AMFICOM.general.Identifier;
import com.syrus.AMFICOM.general.corba.Identifier_Transferable;
import com.syrus.util.Log;
import com.syrus.util.corba.JavaSoftORBUtil;

/**
 * @author Vladimir Dolzhenko
 */
public class CMServerTest {

	private static CMServer	server;

	public static void main(String[] args) {
		try {

			init();
			AccessIdentifier_Transferable accessIdentifier_Transferable = new AccessIdentifier_Transferable();

			Identifier id = new Identifier("Null_0");
			accessIdentifier_Transferable.domain_id = (Identifier_Transferable) id.getTransferable();
			accessIdentifier_Transferable.user_id = (Identifier_Transferable) id.getTransferable();
			accessIdentifier_Transferable.session_id = (Identifier_Transferable) id.getTransferable();

			Domain_Transferable[] domain_Transferables = CMServerTest.server
					.transmitDomains(new Identifier_Transferable[0], accessIdentifier_Transferable);

			for (int i = 0; i < domain_Transferables.length; i++) {
				Domain domain = new Domain(domain_Transferables[i]);
				System.out.println(domain.getId().toString());
			}

		} catch (AMFICOMRemoteException are) {
			System.err.println("AMFICOMRemoteException code:" + are.code + " , " + are.getMessage());
			System.err.println(are);
			System.exit(-1);
		} catch (Exception e) {
			Log.errorException(e);
			System.err.println(e);
			System.exit(-1);
		}
	}

	private static void init() throws UserException, UnknownHostException {
		ORB orb = JavaSoftORBUtil.getInstance().getORB();

		POA rootPOA = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
		rootPOA.the_POAManager().activate();
		NamingContextExt rootNamingCtx = NamingContextExtHelper.narrow(orb
				.resolve_initial_references("NameService"));

		final String hostName = InetAddress.getLocalHost().getCanonicalHostName().replaceAll("\\.", "_");

		CMServerTest.server = CMServerHelper.narrow(rootNamingCtx.resolve_str(hostName + "/CMServer"));

	}
}
