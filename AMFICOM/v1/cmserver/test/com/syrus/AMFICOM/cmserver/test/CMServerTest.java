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
import com.syrus.AMFICOM.general.ObjectEntities;
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

			Identifier testId = new Identifier(server.getGeneratedIdentifier(ObjectEntities.ANALYSISTYPE_ENTITY_CODE));
            
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
			
			Identifier_Transferable[] identifier_Transferable = new Identifier_Transferable[domain_Transferables2.length];
			for (int i = 0; i < domain_Transferables2.length; i++) {
				identifier_Transferable[i] = domain_Transferables2[i].id;			
			}
			
			long time4 = System.currentTimeMillis();
			Domain_Transferable[] domain_Transferables3 = server.transmitDomains(identifier_Transferable,
												accessIdentifier_Transferable);
			long time5 = System.currentTimeMillis();
			System.out.println("retrieve " + domain_Transferables3.length + " item(s) by ids for " + (time5 - time4)
					+ " ms");
			
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
