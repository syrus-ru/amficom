package com.syrus.AMFICOM.kis.r6;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.Remote;
import com.gnnettest.questfiber.domain.DomainManager;
import com.gnnettest.questfiber.domain.User;
import com.gnnettest.questfiber.domain.Password;
import com.gnnettest.questfiber.domain.MonitoredElement;
import com.gnnettest.questfiber.domain.Domain;
import com.gnnettest.questfiber.work.ModuleTypes;
import com.gnnettest.questfiber.probe.Module;
import com.gnnettest.questfiber.probe.OTDRModule;
import com.gnnettest.questfiber.probe.OTAUModule;
import com.gnnettest.questfiber.probe.Probe;
import com.gnnettest.questfiber.util.QuestFiberApplication;
import com.gnnettest.questfiber.rmi.ServerEventPipe;
import com.syrus.AMFICOM.Dispatcher;

public class Session {
    public static DomainManager dommgr;
    public static User user = null;
    public static Domain userdomain = null;
    public static MonitoredElement[] monelems = null;
    public static OTDRModule otdr = null;
    public static OTAUModule[] otaus = null;
    public static Dispatcher dispatcher;
    public static ServerEventPipe eventPipe;

		private static String hostname_sav;
		private static int port_sav;
		private static String username_sav;
		private static String pswdstring_sav;

  public static void open(String hostname, int port, String username, String pswdstring) throws Exception {
		hostname_sav = hostname;
		port_sav = port;
		username_sav = username;
		pswdstring_sav = pswdstring;

    QuestFiberApplication qpapplication = new QuestFiberApplication("amficom", true);

  //Connect ang get domainmanager
    String s = "com.gnnettest.questfiber.domain.DomainManager";
    Remote remote = null;
    try {
        Registry registry = LocateRegistry.getRegistry(hostname, port);
        remote = registry.lookup(s);
        System.out.println("RTUSession.open | connection with RTU established");
    }
    catch(Exception e) {
        System.out.println(e);
        throw e;
    }
    dommgr = (DomainManager)remote;
//Get user
    Password pswd = new Password(new String(pswdstring));
    user = dommgr.getUser(username, pswd);
//Get userdomain
    Domain[] userdomains = dommgr.getUserDomains(user);
    if (userdomains.length >1)
      throw new Exception("User " + user.getUserName(userdomains[0], user) + "has more then !ONE! domain");
    userdomain = userdomains[0];
//Get monitored elements
    monelems = userdomain.getMonitoredElements(user);
//How many RTUs are there in  userdomain?
    if (userdomain.getProbes(user).length>1)
      throw new Exception("User " + user.getUserName(userdomains[0], user) + "has more then !ONE! RTU");
//Get OTDR and OTAUs
    Probe probe = userdomain.getProbes(user)[0];
    Module[] modules = probe.getModules(userdomain, user, ModuleTypes.OTDR);
    if (modules.length > 1)
      throw new Exception("User " + user.getUserName(userdomains[0], user) + "has more then !ONE! OTDR");
    if (modules[0] instanceof OTDRModule) 
      otdr = (OTDRModule)modules[0];
          
    modules = probe.getModules(userdomain, user, ModuleTypes.OTAU);
    if (modules.length > 0) {
      otaus = new OTAUModule[modules.length];
      for (int i = 0; i < otaus.length; i++) 
        if (modules[i] instanceof OTAUModule)
          otaus[i] = (OTAUModule)modules[i];
    }
    else
      throw new Exception("User " + user.getUserName(userdomains[0], user) + "has NONE OTAU");

//Create eventPipe from RTU
    eventPipe = new ServerEventPipe();
    dommgr.addServerListener(eventPipe, user);
  }

	public static void reopen() {
		try {
			open(hostname_sav, port_sav, username_sav, pswdstring_sav);
		}
		catch (Exception e) {
			System.out.println("Session.reopen | Exception: " + e.getMessage());
			e.printStackTrace();
		}
	}
}