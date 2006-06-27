// NMI's Java Code Viewer 5.1 © 1997-2001 B. Lemaire
// http://njcv.htmlplanet.com - info@njcv.htmlplanet.com

// Copy registered to Evaluation Copy

// Source File Name:   DomainManagerRMIServant.java

package com.gnnettest.questfiber.rmi;

import com.gnnettest.questfiber.*;
import com.gnnettest.questfiber.domain.*;
import com.gnnettest.questfiber.path.*;
import com.gnnettest.questfiber.probe.*;
import com.gnnettest.questfiber.problem.ProblemManager;
import com.gnnettest.questfiber.problem.ProblemManagerImplementation;
import com.gnnettest.questfiber.serial.SerialPortManager;
import com.gnnettest.questfiber.starprobe.*;
import com.gnnettest.questfiber.util.*;
import com.gnnettest.questfiber.work.*;
import java.io.PrintStream;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

// Referenced classes of package com.gnnettest.questfiber.rmi:
//            AccessValidation, CableAttributesRMIServant, CableAttributesRMIServantRepository, DomainRMIServant,
//            DomainRMIServantRepository, MonitoredElementRMIServant, MonitoredElementRMIServantRepository, MonitoredPathRMIServant,
//            MonitoredPathRMIServantRepository, ProbeRMIServant, ProbeRMIServantRepository, ProblemManagerRMIServant,
//            ProcessInfoManagerRMIServant, QuestProbeRMIServant, StarProbeLookup, StarProbeRMIServant,
//            UserRMIServant, UserRMIServantRepository, WorkManagerRMIServant, SerialPortManagerRMIServant

public class DomainManagerRMIServant extends UnicastRemoteObject
    implements DomainManager {

    private static DomainManagerImplementation domainManagerImpl;
    private static ProblemManagerRMIServant problemManager = null;
    private static WorkManagerRMIServant workManager = null;
    private static SerialPortManagerRMIServant serialPortManager = null;
    private static SingleLookup questProbeConnector = new SingleLookup();
    private static SingleLookup questProbeModuleConnector = new SingleLookup();
    private static StarProbeLookup starProbeConnector = new StarProbeLookup();
    private static ProcessInfoManagerRMIServant processInfoManagerRMIServant = null;
    private ServerEventFilter eventFilter;

    public DomainManagerRMIServant(DomainManagerImplementation domainmanagerimplementation) throws RemoteException {
        eventFilter = null;
        domainManagerImpl = domainmanagerimplementation;
        eventFilter = new ServerEventFilter();
        eventFilter.start();
    }

    public void addServerListener(ServerListener serverlistener, User user) throws RemoteException {
        eventFilter.addServerListener(serverlistener, user.getIdentity());
    }

    public void buildRMILayer() {
        loadServerObjects();
    }

    public CableAttributes createCableAttributes(AccessKey accesskey, String s) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 13;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            CableAttributesImplementation cableattributesimplementation = domainManagerImpl.createCableAttributes(s);
            CableAttributesRMIServant cableattributesrmiservant = new CableAttributesRMIServant(cableattributesimplementation, this);
            CableAttributesRMIServantRepository.add(cableattributesimplementation.getIdentity(), cableattributesrmiservant);
            return cableattributesrmiservant;
        }
    }

    public Domain createDomain(AccessKey accesskey, String s) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 15;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            DomainImplementation domainimplementation = domainManagerImpl.createDomain(s);
            DomainRMIServant domainrmiservant = new DomainRMIServant(domainimplementation);
            DomainRMIServantRepository.add(domainimplementation.getIdentity(), domainrmiservant);
            Global.domainManager.fireConfigurationEvent(new CreateConfigurationEvent(domainimplementation, domainimplementation.getIdentity()));
            return domainrmiservant;
        }
    }

    public MonitoredElement createMonitoredElement(AccessKey accesskey, String s) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 13;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            MonitoredElementImplementation monitoredelementimplementation = domainManagerImpl.createMonitoredElement(s);
            MonitoredElementRMIServant monitoredelementrmiservant = new MonitoredElementRMIServant(monitoredelementimplementation, this);
            MonitoredElementRMIServantRepository.add(monitoredelementimplementation.getIdentity(), monitoredelementrmiservant);
            CableAttributes cableattributes = createCableAttributes(accesskey, s);
            monitoredelementrmiservant.setCableAttributes(null, accesskey, cableattributes);
            Global.domainManager.fireConfigurationEvent(new CreateConfigurationEvent(monitoredelementimplementation, monitoredelementimplementation.getIdentity()));
            return monitoredelementrmiservant;
        }
    }

    public MonitoredPath createMonitoredPath(AccessKey accesskey, String s) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 13;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            MonitoredPathImplementation monitoredpathimplementation = domainManagerImpl.createMonitoredPath(s);
            MonitoredPathRMIServant monitoredpathrmiservant = new MonitoredPathRMIServant(monitoredpathimplementation, this);
            MonitoredPathRMIServantRepository.add(monitoredpathimplementation.getIdentity(), monitoredpathrmiservant);
            Global.domainManager.fireConfigurationEvent(new CreateConfigurationEvent(monitoredpathimplementation, monitoredpathimplementation.getIdentity()));
            return monitoredpathrmiservant;
        }
    }

    public Probe createProbe(AccessKey accesskey, String s, int i) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 11;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            ProbeImplementation probeimplementation = domainManagerImpl.createProbe(s, i);
            decorateProbeProxy(probeimplementation.getProbeProxy());
            ProbeRMIServant probermiservant = createProbeRMIServant(probeimplementation);
            Global.domainManager.fireConfigurationEvent(new CreateConfigurationEvent(probeimplementation, probeimplementation.getIdentity()));
            return probermiservant;
        }
    }

    public ProbeRMIServant createProbeRMIServant(ProbeImplementation probeimplementation) {
        Object obj = null;
        try {
            if(probeimplementation instanceof com.gnnettest.questfiber.probe.StarProbeImplementation) {
                obj = new StarProbeRMIServant((com.gnnettest.questfiber.probe.StarProbeImplementation)probeimplementation, this);
                ProbeRMIServantRepository.add(probeimplementation.getIdentity(), ((ProbeRMIServant) (obj)));
            } else
            if(probeimplementation instanceof QuestProbeImplementation) {
                obj = new QuestProbeRMIServant((QuestProbeImplementation)probeimplementation, this);
                ProbeRMIServantRepository.add(probeimplementation.getIdentity(), ((ProbeRMIServant) (obj)));
            }
        }
        catch(RemoteException remoteexception) {
            remoteexception.printStackTrace();
        }
        catch(Exception exception) {
            exception.printStackTrace();
        }
        return ((ProbeRMIServant) (obj));
    }

    public User createUser(AccessKey accesskey, String s, Password password) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 17;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            UserImplementation userimplementation = domainManagerImpl.createUser(s, password);
            UserRMIServant userrmiservant = new UserRMIServant(userimplementation, this);
            UserRMIServantRepository.add(userimplementation.getIdentity(), userrmiservant);
            Global.domainManager.fireConfigurationEvent(new CreateConfigurationEvent(userimplementation, userimplementation.getIdentity()));
            return userrmiservant;
        }
    }

    public void decorateProbeProxy(ProbeProxy probeproxy) {
        if(probeproxy instanceof StarProbeProxy) {
            StarProbeProxy starprobeproxy = (StarProbeProxy)probeproxy;
            StarProbeProxy.setStarProbeLookup(starProbeConnector);
        } else
        if(probeproxy instanceof QuestProbeProxy) {
            QuestProbeProxy questprobeproxy = (QuestProbeProxy)probeproxy;
            questprobeproxy.setQuestProbeLookup(questProbeConnector);
        } else
        if(probeproxy instanceof QuestProbeModuleProxy) {
            QuestProbeModuleProxy questprobemoduleproxy = (QuestProbeModuleProxy)probeproxy;
            questprobemoduleproxy.setQuestProbeModuleProxyLookup(questProbeModuleConnector);
        }
    }

    public void deleteCableAttributes(AccessKey accesskey, CableAttributes cableattributes) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 13;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            CableAttributesImplementation cableattributesimplementation = CableAttributesRepository.getInstance(cableattributes.getIdentity());
            domainManagerImpl.deleteCableAttributes(cableattributesimplementation);
            CableAttributesRMIServantRepository.deleteCableAttributes(cableattributes.getIdentity());
            removeRMIObject(cableattributes);
            return;
        }
    }

    public void deleteDomain(AccessKey accesskey, Domain domain) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 15;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            Identity identity = domain.getIdentity();
            DomainImplementation domainimplementation = DomainRepository.getInstance(identity);
            domainManagerImpl.deleteDomain(domainimplementation);
            DomainRMIServantRepository.deleteDomain(identity);
            removeRMIObject(domain);
            return;
        }
    }

    public void deleteMeasurement(AccessKey accesskey, Measurement measurement) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 13;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            domainManagerImpl.deleteMeasurement((MeasurementImplementation)measurement);
            return;
        }
    }

    public void deleteMonitoredElement(AccessKey accesskey, MonitoredElement monitoredelement) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 13;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            MonitoredElementImplementation monitoredelementimplementation = MonitoredElementRepository.getInstance(monitoredelement.getIdentity());
            domainManagerImpl.deleteMonitoredElement(monitoredelementimplementation);
            MonitoredElementRMIServantRepository.deleteMonitoredElement(monitoredelementimplementation.getIdentity());
            removeRMIObject(monitoredelement);
            return;
        }
    }

    public void deleteMonitoredPath(AccessKey accesskey, MonitoredPath monitoredpath) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 13;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            MonitoredPathImplementation monitoredpathimplementation = MonitoredPathRepository.getInstance(monitoredpath.getIdentity());
            domainManagerImpl.deleteMonitoredPath(monitoredpathimplementation);
            MonitoredPathRMIServantRepository.deleteMonitoredPath(monitoredpathimplementation.getIdentity());
            removeRMIObject(monitoredpath);
            return;
        }
    }

    public void deleteProbe(AccessKey accesskey, Probe probe) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 11;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            ProbeImplementation probeimplementation = ProbeRepository.getInstance(probe.getIdentity());
            domainManagerImpl.deleteProbe(probeimplementation);
            ProbeRMIServantRepository.deleteProbe(probeimplementation.getIdentity());
            removeRMIObject(probe);
            return;
        }
    }

    public void deleteUser(User user, User user1) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 17;
        if(!AccessValidation.validateAccess(user, byte0))
            throw AccessValidation.generateException(byte0);
        Identity identity = user1.getIdentity();
        UserImplementation userimplementation = UserRepository.getInstance(identity);
        UserImplementation userimplementation1 = UserRepository.getInstance(user.getIdentity());
        if(!userimplementation.equals(userimplementation1)) {
            domainManagerImpl.deleteUser(userimplementation);
            UserRMIServantRepository.deleteUser(identity);
            removeRMIObject(user1);
        } else {
            throw new Exception("User can not remove themself");
        }
    }

    public CableAttributes[] getCableAttributes(AccessKey accesskey) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 13;
        if(!AccessValidation.validateAccess(accesskey, byte0))
            throw AccessValidation.generateException(byte0);
        CableAttributesImplementation acableattributesimplementation[] = domainManagerImpl.getCableAttributes();
        CableAttributes acableattributes[] = new CableAttributes[acableattributesimplementation.length];
        for(int i = 0; i < acableattributesimplementation.length; i++)
            acableattributes[i] = CableAttributesRMIServantRepository.getInstance(acableattributesimplementation[i].getIdentity());

        return acableattributes;
    }

    public CableAttributes getCableAttributes(AccessKey accesskey, Identity identity) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 14;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            CableAttributesRMIServant cableattributesrmiservant = CableAttributesRMIServantRepository.getInstance(identity);
            return cableattributesrmiservant;
        }
    }

    public OTDRAcquisitionConstraints getDefaultOTDRAcquisitionConstraints(AccessKey accesskey, int i) throws RemoteException, Exception, AccessDeniedException, NullPointerException {
        byte byte0 = 13;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            OTDRAcquisitionConstraints otdracquisitionconstraints = domainManagerImpl.getDefaultOTDRAcquisitionConstraints(i);
            return otdracquisitionconstraints;
        }
    }

    public OTDRAnalysisConstraints getDefaultOTDRAnalysisConstraints(AccessKey accesskey) throws RemoteException, Exception, AccessDeniedException, NullPointerException {
        byte byte0 = 13;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            OTDRAnalysisConstraints otdranalysisconstraints = domainManagerImpl.getDefaultOTDRAnalysisConstraints();
            return otdranalysisconstraints;
        }
    }

    public OTDRValidationConstraints getDefaultOTDRValidationConstraints(AccessKey accesskey) throws RemoteException, Exception, NullPointerException, AccessDeniedException {
        byte byte0 = 13;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            OTDRValidationConstraints otdrvalidationconstraints = domainManagerImpl.getDefaultOTDRValidationConstraints();
            return otdrvalidationconstraints;
        }
    }

    public Domain getDomain(AccessKey accesskey, Identity identity) throws Exception, AccessDeniedException, RemoteException {
        int i = 1;
        if(!AccessValidation.validateAccess(accesskey, i)) {
            throw AccessValidation.generateException(i);
        } else {
            DomainRMIServant domainrmiservant = DomainRMIServantRepository.getInstance(identity);
            return domainrmiservant;
        }
    }

    public DomainElement getDomainElement(AccessKey accesskey, Identity identity) throws Exception {
        byte byte0 = 0;
        Object obj = null;
        obj = MonitoredElementRMIServantRepository.getInstance(identity);
        if(obj == null) {
            obj = ProbeRMIServantRepository.getInstance(identity);
            if(obj == null)
                obj = DomainRMIServantRepository.getInstance(identity);
        }
        if(obj instanceof MonitoredElementRMIServant)
            byte0 = 14;
        else
        if(obj instanceof ProbeRMIServant)
            byte0 = 12;
        else
            byte0 = 15;
        if(!AccessValidation.validateAccess(accesskey, byte0))
            throw AccessValidation.generateException(byte0);
        else
            return ((DomainElement) (obj));
    }

    public DomainManagerImplementation getDomainManager() {
        return domainManagerImpl;
    }

    public Domain[] getDomains(AccessKey accesskey) throws Exception, AccessDeniedException {
        int i = 1;
        if(!AccessValidation.validateAccess(accesskey, i))
            throw AccessValidation.generateException(i);
        DomainImplementation adomainimplementation[] = domainManagerImpl.getDomains();
        Domain adomain[] = new Domain[adomainimplementation.length];
        for(int j = 0; j < adomainimplementation.length; j++)
            adomain[j] = DomainRMIServantRepository.getInstance(adomainimplementation[j].getIdentity());

        return adomain;
    }

    public Object getInstance(AccessKey accesskey, Identity identity) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 15;
        Object obj = null;
        if(!AccessValidation.validateAccess(accesskey, byte0))
            throw AccessValidation.generateException(byte0);
        obj = ProbeRMIServantRepository.getInstance(identity);
        if(obj == null) {
            obj = MonitoredElementRMIServantRepository.getInstance(identity);
            if(obj == null) {
                obj = MonitoredPathRMIServantRepository.getInstance(identity);
                if(obj == null) {
                    obj = UserRMIServantRepository.getInstance(identity);
                    if(obj == null)
                        obj = DomainRMIServantRepository.getInstance(identity);
                }
            }
        }
        return obj;
    }

    public MonitoredElement getMonitoredElement(AccessKey accesskey, Identity identity) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 14;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            MonitoredElementRMIServant monitoredelementrmiservant = MonitoredElementRMIServantRepository.getInstance(identity);
            return monitoredelementrmiservant;
        }
    }

    public MonitoredElement[] getMonitoredElements(AccessKey accesskey) throws Exception, AccessDeniedException {
        byte byte0 = 11;
        if(!AccessValidation.validateAccess(accesskey, byte0))
            throw AccessValidation.generateException(byte0);
        MonitoredElementImplementation amonitoredelementimplementation[] = domainManagerImpl.getMonitoredElements();
        MonitoredElement amonitoredelement[] = new MonitoredElement[amonitoredelementimplementation.length];
        for(int i = 0; i < amonitoredelementimplementation.length; i++)
            amonitoredelement[i] = MonitoredElementRMIServantRepository.getInstance(amonitoredelementimplementation[i].getIdentity());

        return amonitoredelement;
    }

    public MonitoredPath getMonitoredPath(AccessKey accesskey, Identity identity) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 14;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            MonitoredPathRMIServant monitoredpathrmiservant = MonitoredPathRMIServantRepository.getInstance(identity);
            return monitoredpathrmiservant;
        }
    }

    public MonitoredPath[] getMonitoredPaths(AccessKey accesskey) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 14;
        if(!AccessValidation.validateAccess(accesskey, byte0))
            throw AccessValidation.generateException(byte0);
        MonitoredPathImplementation amonitoredpathimplementation[] = domainManagerImpl.getMonitoredPaths();
        MonitoredPath amonitoredpath[] = new MonitoredPath[amonitoredpathimplementation.length];
        for(int i = 0; i < amonitoredpathimplementation.length; i++)
            amonitoredpath[i] = MonitoredPathRMIServantRepository.getInstance(amonitoredpathimplementation[i].getIdentity());

        return amonitoredpath;
    }

    public String[] getOTDROpticsModelNames(AccessKey accesskey) throws RemoteException {
        String as[] = domainManagerImpl.getOTDROpticsModelNames();
        return as;
    }

    public Probe getProbe(AccessKey accesskey, Identity identity) throws Exception, AccessDeniedException, RemoteException {
        byte byte0 = 12;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            ProbeRMIServant probermiservant = ProbeRMIServantRepository.getInstance(identity);
            return probermiservant;
        }
    }

    public Probe getProbeInstance(AccessKey accesskey, Identity identity) throws Exception, AccessDeniedException {
        byte byte0 = 15;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            ProbeRMIServant probermiservant = ProbeRMIServantRepository.getInstance(identity);
            return probermiservant;
        }
    }

    public Probe[] getProbes(AccessKey accesskey) throws Exception, AccessDeniedException {
        byte byte0 = 15;
        if(!AccessValidation.validateAccess(accesskey, byte0))
            throw AccessValidation.generateException(byte0);
        ProbeImplementation aprobeimplementation[] = domainManagerImpl.getProbes();
        Probe aprobe[] = new Probe[aprobeimplementation.length];
        for(int i = 0; i < aprobeimplementation.length; i++)
            aprobe[i] = ProbeRMIServantRepository.getInstance(aprobeimplementation[i].getIdentity());

        return aprobe;
    }

    public ProblemManager getProblemManager(AccessKey accesskey) throws RemoteException {
        return problemManager;
    }

    public ProcessInfoManager getProcessInfoManager() throws RemoteException, Exception {
        return processInfoManagerRMIServant;
    }

    public SerialPortManager getSerialPortManager(AccessKey accesskey) throws RemoteException {
        return serialPortManager;
    }

    public User getUser(String s, Password password) throws Exception, AccessDeniedException, RemoteException {
        UserRMIServant userrmiservant = null;
        UserImplementation userimplementation = domainManagerImpl.getUser(s, password);
        if(userimplementation.getAccessLevel() != 6) {
            if(userimplementation.getPassWord().equals(password))
                  System.out.println("Password OK");
//                userrmiservant = UserRMIServantRepository.getInstance(userimplementation.getIdentity());
            else
                  System.out.println("Invalid password");
//                throw new Exception("Invalid Password");
            userrmiservant = UserRMIServantRepository.getInstance(userimplementation.getIdentity());
        } else {
            throw new Exception("Access Level = NONE");
        }
        return userrmiservant;
    }

    public Domain[] getUserDomains(User user) throws RemoteException {
        Domain adomain[] = null;
        DomainImplementation adomainimplementation[] = null;
        UserImplementation userimplementation = UserRepository.getInstance(user.getIdentity());
        if(userimplementation instanceof SystemAdministratorImplementation)
            adomainimplementation = domainManagerImpl.getDomains();
        else
            adomainimplementation = domainManagerImpl.getUserDomains(userimplementation);
        adomain = new Domain[adomainimplementation.length];
        for(int i = 0; i < adomainimplementation.length; i++)
            adomain[i] = DomainRMIServantRepository.getInstance(adomainimplementation[i].getIdentity());

        return adomain;
    }

    public User[] getUsers(AccessKey accesskey) throws RemoteException, Exception, AccessDeniedException {
        byte byte0 = 18;
        if(!AccessValidation.validateAccess(accesskey, byte0))
            throw AccessValidation.generateException(byte0);
        UserImplementation auserimplementation[] = domainManagerImpl.getUsers();
        User auser[] = new User[auserimplementation.length];
        for(int i = 0; i < auserimplementation.length; i++)
            auser[i] = UserRMIServantRepository.getInstance(auserimplementation[i].getIdentity());

        return auser;
    }

    public WorkManager getWorkManager() throws RemoteException {
        return workManager;
    }

    public WorkManager getWorkManager(AccessKey accesskey) throws RemoteException {
        return workManager;
    }

    public WorkManagerRMIServant getWorkManagerRMIServant() {
        return workManager;
    }

    private void initializeConfigurationObjects() {
        try {
            com.gnnettest.questfiber.util.ProcessInfoManagerImplementation processinfomanagerimplementation = domainManagerImpl.getProcessInfoManager();
            processInfoManagerRMIServant = new ProcessInfoManagerRMIServant(processinfomanagerimplementation);
        }
        catch(Exception exception) {
            Log.systemLog.logErrorMessage("Failure to initialize configuration objects.");
            Log.systemLog.logErrorException(exception);
        }
    }

    private void loadCableAttributes() throws Exception {
        CableAttributesImplementation acableattributesimplementation[] = CableAttributesRepository.getAll();
        for(int i = 0; i < acableattributesimplementation.length; i++)
            try {
                CableAttributesRMIServant cableattributesrmiservant = new CableAttributesRMIServant(acableattributesimplementation[i], this);
                CableAttributesRMIServantRepository.add(acableattributesimplementation[i].getIdentity(), cableattributesrmiservant);
            }
            catch(RemoteException remoteexception) {
                remoteexception.printStackTrace();
            }

    }

    private void loadDomainObjects() {
        try {
            loadProbes();
            loadCableAttributes();
            loadMonitoredElements();
            loadUsers();
            loadMonitoredPaths();
            loadDomains();
            domainManagerImpl.addConfigurationListener(eventFilter);
        }
        catch(Exception exception) {
            System.err.println(exception);
            exception.printStackTrace();
        }
    }

    private void loadDomains() throws Exception {
        DomainImplementation adomainimplementation[] = DomainRepository.getAll();
        for(int i = 0; i < adomainimplementation.length; i++)
            try {
                DomainRMIServant domainrmiservant = new DomainRMIServant(adomainimplementation[i]);
                DomainRMIServantRepository.add(adomainimplementation[i].getIdentity(), domainrmiservant);
            }
            catch(RemoteException remoteexception) {
                remoteexception.printStackTrace();
            }

    }

    private void loadMonitoredElements() throws Exception {
        MonitoredElementImplementation amonitoredelementimplementation[] = MonitoredElementRepository.getAll();
        for(int i = 0; i < amonitoredelementimplementation.length; i++)
            try {
                MonitoredElementRMIServant monitoredelementrmiservant = new MonitoredElementRMIServant(amonitoredelementimplementation[i], this);
                MonitoredElementRMIServantRepository.add(amonitoredelementimplementation[i].getIdentity(), monitoredelementrmiservant);
            }
            catch(RemoteException remoteexception) {
                remoteexception.printStackTrace();
            }

    }

    private void loadMonitoredPaths() throws Exception {
        MonitoredPathImplementation amonitoredpathimplementation[] = MonitoredPathRepository.getAll();
        for(int i = 0; i < amonitoredpathimplementation.length; i++)
            try {
                MonitoredPathRMIServant monitoredpathrmiservant = new MonitoredPathRMIServant(amonitoredpathimplementation[i], this);
                MonitoredPathRMIServantRepository.add(amonitoredpathimplementation[i].getIdentity(), monitoredpathrmiservant);
            }
            catch(RemoteException remoteexception) {
                remoteexception.printStackTrace();
            }

    }

    private void loadProbes() throws Exception {
        ProbeImplementation aprobeimplementation[] = ProbeRepository.getAll();
        for(int i = 0; i < aprobeimplementation.length; i++) {
            ProbeImplementation probeimplementation = aprobeimplementation[i];
            decorateProbeProxy(probeimplementation.getProbeProxy());
            createProbeRMIServant(probeimplementation);
        }

    }

    private void loadProblemObjects() {
        try {
            ProblemManagerImplementation problemmanagerimplementation = domainManagerImpl.getProblemManager();
            problemManager = new ProblemManagerRMIServant(problemmanagerimplementation, this);
            problemmanagerimplementation.addProblemListener(eventFilter);
        }
        catch(RemoteException remoteexception) {
            remoteexception.printStackTrace();
        }
    }

    private void loadServerObjects() {
        loadDomainObjects();
        loadWorkObjects();
        loadProblemObjects();
        initializeConfigurationObjects();
    }

    public UserPreferences loadSystemUserPreferences(User user, UserPreferences userpreferences) throws RemoteException, Exception, AccessDeniedException {
        if(user == null) {
            throw new NullPointerException("No user provided for validating access");
        } else {
            UserPreferences userpreferences1 = domainManagerImpl.loadSystemUserPreferences(userpreferences);
            return userpreferences1;
        }
    }

    private void loadUsers() throws Exception {
        UserImplementation auserimplementation[] = UserRepository.getAll();
        for(int i = 0; i < auserimplementation.length; i++)
            try {
                UserRMIServant userrmiservant = new UserRMIServant(auserimplementation[i], this);
                UserRMIServantRepository.add(auserimplementation[i].getIdentity(), userrmiservant);
            }
            catch(RemoteException remoteexception) {
                remoteexception.printStackTrace();
            }

    }

    private void loadWorkObjects() {
        try {
            WorkManagerImplementation workmanagerimplementation = domainManagerImpl.getWorkManager();
            workManager = new WorkManagerRMIServant(workmanagerimplementation);
            workmanagerimplementation.addWorkListener(eventFilter);
            starProbeConnector.setWorkManager(workManager);
        }
        catch(RemoteException remoteexception) {
            remoteexception.printStackTrace();
        }
    }

    private void removeRMIObject(Remote remote) throws NoSuchObjectException {
    }

    public void removeServerListener(ServerListener serverlistener, User user) throws RemoteException {
        eventFilter.removeServerListener(serverlistener, user.getIdentity());
    }

    public void setDefaultOTDRAcquisitionConstraints(AccessKey accesskey, OTDRAcquisitionConstraints otdracquisitionconstraints, int i) throws RemoteException, Exception, AccessDeniedException, NullPointerException {
        byte byte0 = 13;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            domainManagerImpl.setDefaultOTDRAcquisitionConstraints(otdracquisitionconstraints, i);
            return;
        }
    }

    public void setDefaultOTDRAnalysisConstraints(AccessKey accesskey, OTDRAnalysisConstraints otdranalysisconstraints) throws RemoteException, Exception, AccessDeniedException, NullPointerException {
        byte byte0 = 13;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            domainManagerImpl.setDefaultOTDRAnalysisConstraints(otdranalysisconstraints);
            return;
        }
    }

    public void setDefaultOTDRValidationConstraints(AccessKey accesskey, OTDRValidationConstraints otdrvalidationconstraints) throws RemoteException, Exception, AccessDeniedException, NullPointerException {
        byte byte0 = 13;
        if(!AccessValidation.validateAccess(accesskey, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            domainManagerImpl.setDefaultOTDRValidationConstraints(otdrvalidationconstraints);
            return;
        }
    }

    public void setSerialPortManager(SerialPortManagerRMIServant serialportmanagerrmiservant) throws RemoteException {
        serialPortManager = serialportmanagerrmiservant;
    }

    public void setSystemUserPreferences(User user, UserPreferences userpreferences) throws RemoteException, Exception, AccessDeniedException {
        byte byte0 = 17;
        if(!AccessValidation.validateAccess(user, byte0)) {
            throw AccessValidation.generateException(byte0);
        } else {
            domainManagerImpl.setSystemUserPreferences(userpreferences);
            return;
        }
    }

}
