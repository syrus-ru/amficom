package com.syrus.util.corba;

import org.omg.CORBA.ORB;
import org.omg.CORBA.ORBPackage.InvalidName;
import org.omg.PortableServer.POA;
import org.omg.PortableServer.POAHelper;
import org.omg.PortableServer.Servant;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NameComponent;
import java.util.Properties;
import com.syrus.util.ApplicationProperties;

public class CORBAServer /*extends Thread */{
	public static final String DEFAULT_ORB_INITIAL_HOST = "127.0.0.1";
	public static final int DEFAULT_ORB_INITIAL_PORT = 1050;

	private ORB orb;
	private POA poa;
	private NamingContextExt namingContext;

	public CORBAServer() throws Exception {
		Properties properties = System.getProperties();
		String host = ApplicationProperties.getString("ORBInitialHost", DEFAULT_ORB_INITIAL_HOST);
		int port = ApplicationProperties.getInt("ORBInitialPort", DEFAULT_ORB_INITIAL_PORT);
		System.out.println("host: " + host + ", port: " + Integer.parseInt(Integer.toString(port)));
		properties.setProperty("org.omg.CORBA.ORBInitialHost", host);
		properties.setProperty("org.omg.CORBA.ORBInitialPort", Integer.toString(port));
		String[] args = null;
		this.orb = ORB.init(args, properties);

		this.initPOA();
		this.initNamingContext();

		this.poa.the_POAManager().activate();
	}

	public void activateServant(Servant servant, String name) throws Exception {
		NameComponent[] nameComponents = this.namingContext.to_name(name);
		this.poa.activate_object(servant);
		this.namingContext.rebind(nameComponents, this.poa.servant_to_reference(servant));
	}

	public org.omg.CORBA.Object resolveReference(String name) throws Exception {
		try {
			return this.namingContext.resolve_str(name);
		}
		catch (org.omg.CosNaming.NamingContextPackage.NotFound nf) {
			throw new Exception("Name '" + name + "' not found; " + nf.why);
		}
	}

	public ORB getORB() {
		return this.orb;
	}

	public void run() {
		this.orb.run();
	}

	public void shutdown() throws Exception {
		this.poa.the_POAManager().deactivate(false, false);
		this.poa.destroy(false, false);
		this.orb.shutdown(true);
	}

	private void initPOA() throws Exception {
		try {
			this.poa = POAHelper.narrow(this.orb.resolve_initial_references("RootPOA"));
		}
		catch (InvalidName in) {
			throw new Exception("Cannot resolve RootPOA -- invalid name: " + in.getMessage());
		}
	}

	private void initNamingContext() throws Exception {
		try {
			this.namingContext = NamingContextExtHelper.narrow(this.orb.resolve_initial_references("NameService"));
		}
		catch (InvalidName in) {
			throw new Exception("Cannot resolve NameService -- invalid name: " + in.getMessage());
		}
	}
}
