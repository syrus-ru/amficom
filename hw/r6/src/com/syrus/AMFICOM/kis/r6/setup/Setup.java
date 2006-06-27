package com.syrus.AMFICOM.kis.r6.setup;

import java.rmi.Remote;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import com.gnnettest.questfiber.util.QuestFiberApplication;
import com.gnnettest.questfiber.domain.DomainManager;
import com.gnnettest.questfiber.domain.Password;
import com.gnnettest.questfiber.domain.Domain;
import com.gnnettest.questfiber.domain.MonitoredElement;
import com.gnnettest.questfiber.domain.User;
import com.gnnettest.questfiber.domain.AccessLevel;
import com.gnnettest.questfiber.probe.Probe;
import com.gnnettest.questfiber.probe.ProbeTypes;
import com.gnnettest.questfiber.probe.ProbeComTypes;
import com.gnnettest.questfiber.probe.ProbeServiceTypes;
import com.gnnettest.questfiber.probe.ProbeImplementation;
import com.gnnettest.questfiber.probe.OTDRModule;
import com.gnnettest.questfiber.probe.OTAUModule;
import com.gnnettest.questfiber.probe.OTAUTypes;
import com.gnnettest.questfiber.work.ModuleTypes;

public class Setup {
	private static final String hostname = "rtu-1";
	private static final int port = 17770;
	private static final String admin_user_name = "administrator";
	private static final String admin_password_string = "admin";
	private static final String amficom_domain_name = "amficom";
	private static final String amficom_user_name = "amficom";
	private static final String amficom_password_string = "rakom";
	private static final String rtu_name = "rtu-1";
	private static final String rtu_host_name = "rtu-1";
	private static final String otdr_name = "otdr-1";
	private static final String otau_name = "otau-1";
	private static final int otau_ports_number = 24;
	private static final int otau_input_port = 1;
	private static final int otdr_output_port = 1;
	private static final String[] fiber_names = {"fiber-1", "fiber-2"};

	public Setup() {
	}

	public static void main(String[] args) {
//Initial test for foolish conditions
		if (fiber_names.length > otau_ports_number) {
			System.out.println("ERROR: Number of fibers supplied > number of ports on OTAU");
			System.exit(0);
		}

//Create QuestFiberApplication -- for normal operation of Log
		QuestFiberApplication qpapp = new QuestFiberApplication("amficom", true);

//Connect and get DomainManager remote reference
    String s = "com.gnnettest.questfiber.domain.DomainManager";
    Remote remote = null;
    try {
        Registry registry = LocateRegistry.getRegistry(hostname, port);
        remote = registry.lookup(s);
        System.out.println("RTUSession.open | connection with RTU established");
    }
    catch(Exception e) {
        System.out.println(e);
    }
    DomainManager domainManager = (DomainManager)remote;

		try {
//Login as administrator user
			Password adminPassword = new Password(new String(admin_password_string));
			User adminUser = domainManager.getUser(admin_user_name, adminPassword);

//Create user for AMFICOM and set access levels
			User amficomUser = domainManager.createUser(adminUser, amficom_user_name, new Password(amficom_password_string));
			amficomUser.setAccessLevel(null, adminUser, AccessLevel.OPERATOR);

//Create domain for AMFICOM
			Domain amficomDomain = domainManager.createDomain(adminUser, amficom_domain_name);

//Set access level for amficom user in amficom domain
			amficomDomain.addUser(adminUser, amficomUser, AccessLevel.OPERATOR);

//Create probe - RTU
			Probe rtuProbe = domainManager.createProbe(adminUser, rtu_name, ProbeTypes.QUESTPROBE - 1);
			rtuProbe.setCommunication(amficomDomain, adminUser, ProbeComTypes.NETWORK);
			rtuProbe.setHostInformation(amficomDomain, adminUser, rtu_host_name);
			rtuProbe.setSoftwareVersion(amficomDomain, adminUser, "Unknown");
			rtuProbe.setAvailableSerialPorts(amficomDomain, adminUser, 0);
			rtuProbe.setUseCountryAndAreaCodeFlag(amficomDomain, adminUser, false);
			rtuProbe.setSearchStringFlag(amficomDomain, adminUser, false);
			rtuProbe.setCommunicationCheckInterval(amficomDomain, adminUser, ProbeImplementation.DEFAULT_COMMUNICATION_CHECK_INTERVAL);
			rtuProbe.setConnectionType(amficomDomain, adminUser, ProbeImplementation.CONNECTION_TYPE_AS_NEEDED);
			rtuProbe.setServiceStatus(amficomDomain, adminUser, ProbeServiceTypes.IN_SERVICE);

//Create OTDR
			OTDRModule otdrModule = (OTDRModule)rtuProbe.createModule(amficomDomain, adminUser, otdr_name, ModuleTypes.OTDR);
			otdrModule.setOpticsModel(amficomDomain, adminUser, "QP1640");

//Create OTAU
			OTAUModule otauModule = (OTAUModule)rtuProbe.createModule(amficomDomain, adminUser, otau_name, ModuleTypes.OTAU);
			otauModule.setNumberOfPorts(amficomDomain, adminUser, 24);
			otauModule.setCommunicationAddress(amficomDomain, adminUser, 1);
			otauModule.setNumberOfInputPorts(amficomDomain, adminUser, 1);
			otauModule.setIsRemote(amficomDomain, adminUser, false);
			otauModule.setOTAUType(amficomDomain, adminUser, new OTAUTypes(OTAUTypes.TYPE_II));
			otauModule.setSerialPort(amficomDomain, adminUser, "COM2");

//Attach OTAU to OTDR
			otauModule.attachInputModule(amficomDomain, adminUser, otau_input_port, otdrModule, otdr_output_port);
			otdrModule.attachModule(amficomDomain, adminUser, otauModule, otdr_output_port);

//Create fibers
			MonitoredElement[] monitoredElements = new MonitoredElement[fiber_names.length];
			int attach_port;
			for (int i = 0; i < monitoredElements.length; i++) {
				monitoredElements[i] = domainManager.createMonitoredElement(adminUser, fiber_names[i]);
				attach_port = i + 1;
				monitoredElements[i].attachModule(amficomDomain, adminUser, otauModule, attach_port);
			}

//Move all stuff to AMFICOM domain
			amficomDomain.addElement(adminUser, rtuProbe);
			for (int i = 0; i < monitoredElements.length; i++)
				amficomDomain.addElement(adminUser, monitoredElements[i]);
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}