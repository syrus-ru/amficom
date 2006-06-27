// NMI's Java Code Viewer 5.1 © 1997-2001 B. Lemaire
// http://njcv.htmlplanet.com - info@njcv.htmlplanet.com

// Copy registered to Evaluation Copy                                   

// Source File Name:   WorkRequestDatabase.java

package com.gnnettest.questfiber.work;

import com.gnnettest.questfiber.util.*;
import java.io.PrintStream;
import java.sql.*;
import java.util.*;

// Referenced classes of package com.gnnettest.questfiber.work:
//            Constraints, DatabaseException, OTDRAcquisitionConstraints, OTDRAcquisitionConstraintsDatabase, 
//            OTDRAnalysisConstraints, OTDRAnalysisConstraintsDatabase, OTDRValidationConstraints, OTDRValidationConstraintsDatabase, 
//            OracleDatabaseErrorInterpreter, RequestImplementation, WorkReport, WorkReportDatabase, 
//            WorkRequest, WorkRequestImplementation, WorkRequestSearchConstraints

public class WorkRequestDatabase {

    private static Connection Conn;
    private String sIdentity;
    private static LinkedList WorkRequestIdentityList;
    private static OracleDatabaseErrorInterpreter dbErrorInterpreter = new OracleDatabaseErrorInterpreter();
    private static String selectAttributes = "Identity, acqConstraintsIdentity, acquisitionType, analysisConstraintsIdentity,  moduleIdentity, monitoredElementIdentity, port, scheduleType, schedule, status, statusString, task, userIdentity, validationConstraintsIdentity, " + DatabaseDate.fromDatabaseQuery("RequestDate") + ", pendingStatus" + ", serverName"/*-------*/ + ", AmficomMeasurementId"/*-------*/;

    public WorkRequestDatabase() {
        Conn = ServerDbConnection.getConnection();
    }

    public void alwaysWrite(WorkRequestImplementation workrequestimplementation) throws DatabaseException, Exception {
        try {
            create(workrequestimplementation);
        }
        catch(SQLException sqlexception) {
            if(dbErrorInterpreter.recordExists(sqlexception))
                save(workrequestimplementation);
        }
    }

    public void alwaysWriteWithContents(WorkRequestImplementation workrequestimplementation) throws DatabaseException, Exception {
        alwaysWrite(workrequestimplementation);
        if(workrequestimplementation.getAcquisitionConstraints() != null) {
            OTDRAcquisitionConstraintsDatabase otdracquisitionconstraintsdatabase = new OTDRAcquisitionConstraintsDatabase();
            otdracquisitionconstraintsdatabase.createIfNonexistent((OTDRAcquisitionConstraints)workrequestimplementation.getAcquisitionConstraints());
        }
        if(workrequestimplementation.getAnalysisConstraints() != null) {
            OTDRAnalysisConstraintsDatabase otdranalysisconstraintsdatabase = new OTDRAnalysisConstraintsDatabase();
            otdranalysisconstraintsdatabase.createIfNonexistent((OTDRAnalysisConstraints)workrequestimplementation.getAnalysisConstraints());
        }
        if(workrequestimplementation.getValidationConstraints() != null) {
            OTDRValidationConstraintsDatabase otdrvalidationconstraintsdatabase = new OTDRValidationConstraintsDatabase();
            otdrvalidationconstraintsdatabase.createIfNonexistent((OTDRValidationConstraints)workrequestimplementation.getValidationConstraints());
        }
    }

    public static WorkRequestImplementation[] constrainedSearch(WorkRequestSearchConstraints workrequestsearchconstraints) {
        WorkRequestImplementation aworkrequestimplementation[] = null;
        LinkedList linkedlist = constrainedSearchList(workrequestsearchconstraints);
        if(linkedlist.size() > 0) {
            aworkrequestimplementation = new WorkRequestImplementation[linkedlist.size()];
            aworkrequestimplementation = (WorkRequestImplementation[])linkedlist.toArray(aworkrequestimplementation);
        }
        return aworkrequestimplementation;
    }

    public static LinkedList constrainedSearchList(WorkRequestSearchConstraints workrequestsearchconstraints) {
        java.sql.Date date = new java.sql.Date(0xffffe4b714a82000L);
        java.sql.Date date1 = new java.sql.Date(0xb5e620f48000L);
        if(workrequestsearchconstraints.timeInterval.lowerBound > date.getTime())
            date = new java.sql.Date(workrequestsearchconstraints.timeInterval.lowerBound);
        if(workrequestsearchconstraints.timeInterval.upperBound < date1.getTime())
            date1 = new java.sql.Date(workrequestsearchconstraints.timeInterval.upperBound);
        String s = "SELECT Identity FROM WorkRequest WHERE";
        s = s.concat(" RequestDate BETWEEN " + DatabaseDate.toDatabaseDate(date) + " AND " + DatabaseDate.toDatabaseDate(date1));
        if(workrequestsearchconstraints.scheduleType != 0)
            s = s.concat(" AND scheduleType = " + workrequestsearchconstraints.scheduleType);
        if(workrequestsearchconstraints.status != 0)
            s = s.concat(" AND status = " + workrequestsearchconstraints.status);
        if(workrequestsearchconstraints.task != 0)
            s = s.concat(" AND task = " + workrequestsearchconstraints.task);
        if(workrequestsearchconstraints.serverName != null)
            s = s.concat(" AND servername = '" + workrequestsearchconstraints.serverName + "'");
        LinkedList linkedlist = new LinkedList();
        Statement statement = null;
        try {
            Connection connection = ServerDbConnection.getConnection();
            statement = connection.createStatement();
            Log.systemLog.logDebugMessage("Trying: " + s, 200);
            ResultSet resultset = statement.executeQuery(s);
            LinkedList linkedlist1 = new LinkedList();
            while(resultset.next())  {
                String s1 = resultset.getString("Identity");
                Object obj = null;
                if(s1 != null) {
                    Identity identity = new Identity(s1);
                    linkedlist1.add(identity);
                }
            }
            WorkRequestDatabase workrequestdatabase = new WorkRequestDatabase();
            WorkReportDatabase workreportdatabase = new WorkReportDatabase();
            WorkRequestImplementation workrequestimplementation;
            for(Iterator iterator = linkedlist1.iterator(); iterator.hasNext(); linkedlist.add(workrequestimplementation)) {
                workrequestimplementation = new WorkRequestImplementation();
                Identity identity1 = (Identity)iterator.next();
                workrequestimplementation.setIdentity(identity1);
                try {
                    workrequestdatabase.load(workrequestimplementation);
                    Vector vector = workreportdatabase.loadWorkReportVector(identity1);
                    for(int i = 0; vector != null && i < vector.size(); i++)
                        workrequestimplementation.addWorkReport((WorkReport)vector.get(i));

                }
                catch(Exception exception1) {
                    Log.systemLog.logErrorException(exception1);
                }
            }

        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("WorkRequestDatabase: Error (17) ", 200);
            Log.systemLog.logErrorException(sqlexception);
        }
        finally {
            try {
                statement.close();
            }
            catch(SQLException sqlexception1) {
                Log.systemLog.logDebugMessage("WorkRequestDatabase: Error (18) ", 200);
                Log.systemLog.logErrorException(sqlexception1);
            }
            statement = null;
        }
        return linkedlist;
    }

    public static WorkRequestImplementation[] constrainedSearchOld(WorkRequestSearchConstraints workrequestsearchconstraints) {
        java.sql.Date date = new java.sql.Date(0xffffe4b714a82000L);
        java.sql.Date date1 = new java.sql.Date(0xb5e620f48000L);
        if(workrequestsearchconstraints.timeInterval.lowerBound > date.getTime())
            date = new java.sql.Date(workrequestsearchconstraints.timeInterval.lowerBound);
        if(workrequestsearchconstraints.timeInterval.upperBound < date1.getTime())
            date1 = new java.sql.Date(workrequestsearchconstraints.timeInterval.upperBound);
        String s = "SELECT " + selectAttributes + " FROM WorkRequest WHERE";
        s = s.concat(" RequestDate BETWEEN " + DatabaseDate.toDatabaseDate(date) + " AND " + DatabaseDate.toDatabaseDate(date1));
        if(workrequestsearchconstraints.scheduleType != 0)
            s = s.concat(" AND scheduleType = " + workrequestsearchconstraints.scheduleType);
        if(workrequestsearchconstraints.status != 0)
            s = s.concat(" AND status = " + workrequestsearchconstraints.status);
        if(workrequestsearchconstraints.task != 0)
            s = s.concat(" AND task = " + workrequestsearchconstraints.task);
        WorkRequestImplementation aworkrequestimplementation[] = null;
        LinkedList linkedlist = new LinkedList();
        Statement statement = null;
        try {
            Connection connection = ServerDbConnection.getConnection();
            statement = connection.createStatement();
            Log.systemLog.logDebugMessage("Trying: " + s, 200);
            WorkRequestImplementation workrequestimplementation;
            for(ResultSet resultset = statement.executeQuery(s); resultset.next(); linkedlist.add(workrequestimplementation)) {
                String s1 = resultset.getString("Identity");
                Identity identity = null;
                if(s1 != null)
                    identity = new Identity(s1);
                String s2 = resultset.getString("acqConstraintsIdentity");
                Identity identity1 = null;
                if(s2 != null)
                    identity1 = new Identity(s2);
                String s3 = resultset.getString("analysisConstraintsIdentity");
                Identity identity2 = null;
                if(s3 != null)
                    identity2 = new Identity(s3);
                String s4 = resultset.getString("moduleIdentity");
                Identity identity3 = null;
                if(s4 != null)
                    identity3 = new Identity(s4);
                String s5 = resultset.getString("monitoredElementIdentity");
                Identity identity4 = null;
                if(s5 != null)
                    identity4 = new Identity(s5);
                String s6 = resultset.getString("userIdentity");
                Identity identity5 = null;
                if(s6 != null)
                    identity5 = new Identity(s6);
                String s7 = resultset.getString("validationConstraintsIdentity");
                Identity identity6 = null;
                if(s7 != null)
                    identity6 = new Identity(s7);
                workrequestimplementation = new WorkRequestImplementation();
                workrequestimplementation.setIdentity(identity);
                workrequestimplementation.setAttributes(resultset.getInt("task"), identity3, identity4, resultset.getInt("port"), identity1, resultset.getInt("acquisitionType"), resultset.getInt("scheduleType"), null, resultset.getInt("status"), identity5, resultset.getString("statusString"), identity2, identity6, DatabaseDate.fromDatabaseDate(resultset, "RequestDate"), resultset.getInt("pendingStatus"), resultset.getString("ServerName"), resultset.getString("AmficomMeasurementID"));
            }

            aworkrequestimplementation = new WorkRequestImplementation[linkedlist.size()];
            aworkrequestimplementation = (WorkRequestImplementation[])linkedlist.toArray(aworkrequestimplementation);
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("WorkRequestDatabase: Error (17) ", 200);
            Log.systemLog.logErrorException(sqlexception);
        }
        finally {
            try {
                statement.close();
            }
            catch(SQLException sqlexception1) {
                Log.systemLog.logDebugMessage("WorkRequestDatabase: Error (18) ", 200);
                Log.systemLog.logErrorException(sqlexception1);
            }
            statement = null;
        }
        return aworkrequestimplementation;
    }

    public void create(WorkRequestImplementation workrequestimplementation) throws NullPointerException, Exception {
        Object obj = null;
        Object obj1 = null;
        Identity identity = workrequestimplementation.getIdentity();
        if(identity == null)
            throw new NullPointerException("WorkRequest Identity is null");
        identity = workrequestimplementation.getModuleIdentity();
        if(identity == null)
            throw new NullPointerException("Module Identity is null");
        identity = workrequestimplementation.getAcquisitionConstraintsIdentity();
        if(identity == null)
            throw new NullPointerException("AcquisitionConstraints Identity is null");
        identity = workrequestimplementation.getMonitoredElementIdentity();
        if(identity == null)
            throw new NullPointerException("MonitoredElement Identity is null");
        Statement statement = null;
        try {
            statement = Conn.createStatement();
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("WorkRequestDatabase: Error (7) ", 200);
            Log.systemLog.logErrorException(sqlexception);
            throw new DatabaseException("Unable to create Statement Ojbect for Database access.");
        }
        String s = "INSERT INTO WorkRequest (Identity, Task, ModuleIdentity,  Port,AcqConstraintsIdentity, AcquisitionType, ScheduleType, Status, UserIdentity, RequestDate, PendingStatus";
        if(workrequestimplementation.getMonitoredElementIdentity() != null)
            s = s + ", MonitoredElementIdentity";
        if(workrequestimplementation.getSchedule() != null)
            s = s + ", Schedule";
        if(workrequestimplementation.getStatusString() != null)
            s = s + ", StatusString";
        if(workrequestimplementation.getAnalysisConstraints() != null)
            s = s + ", AnalysisConstraintsIdentity";
        if(workrequestimplementation.getValidationConstraints() != null)
            s = s + ", ValidationConstraintsIdentity";
        if(workrequestimplementation.getServerName() != null)
            s = s + ", ServerName";
//+++++++++++++++++++++++++++++++++++++++
        if(workrequestimplementation.getAmficomMeasurementId() != null)
            s = s + ", AmficomMeasurementId";
//+++++++++++++++++++++++++++++++++++++++
        s = s + ") VALUES ('";
        s = s + workrequestimplementation.getIdentity() + "'," + workrequestimplementation.getTask() + ",'" + workrequestimplementation.getModuleIdentity() + "'," + workrequestimplementation.getPort() + ",'" + workrequestimplementation.getAcquisitionConstraintsIdentity() + "'," + workrequestimplementation.getAcquisitionType() + "," + workrequestimplementation.getScheduleType() + "," + workrequestimplementation.getStatus() + ",'" + workrequestimplementation.getUserIdentity() + "'" + "," + DatabaseDate.toDatabaseDate(workrequestimplementation.getDate()) + "," + workrequestimplementation.getPendingStatus();
        if(workrequestimplementation.getMonitoredElementIdentity() != null)
            s = s + ",'" + workrequestimplementation.getMonitoredElementIdentity() + "'";
        if(workrequestimplementation.getSchedule() != null)
            s = s + ",'" + workrequestimplementation.getSchedule() + "'";
        if(workrequestimplementation.getStatusString() != null)
            s = s + ",'" + workrequestimplementation.getStatusString() + "'";
        if(workrequestimplementation.getAnalysisConstraints() != null)
            s = s + ",'" + workrequestimplementation.getAnalysisConstraints().getIdentity() + "'";
        if(workrequestimplementation.getValidationConstraints() != null)
            s = s + ",'" + workrequestimplementation.getValidationConstraints().getIdentity() + "'";
        if(workrequestimplementation.getServerName() != null)
            s = s + ",'" + workrequestimplementation.getServerName() + "'";
//+++++++++++++++++++++++++++++++++++++++
        if(workrequestimplementation.getAmficomMeasurementId() != null)
            s = s + ",'" + workrequestimplementation.getAmficomMeasurementId() + "'";
//+++++++++++++++++++++++++++++++++++++++
        s = s + ")";
        System.out.println("Trying: " + s);
        Log.systemLog.logDebugMessage("Trying: " + s, 200);
        try {
            int i = statement.executeUpdate(s);
            statement.close();
            statement = null;
        }
        catch(SQLException sqlexception1) {
            throw sqlexception1;
        }
        finally {
            try {
                if(statement != null)
                    statement.close();
                statement = null;
            }
            catch(SQLException _ex) { }
        }
    }

    public void createIfNonexistent(WorkRequestImplementation workrequestimplementation) throws DatabaseException, Exception {
        try {
            create(workrequestimplementation);
        }
        catch(SQLException sqlexception) {
            if(dbErrorInterpreter.recordExists(sqlexception))
                Log.systemLog.logDebugMessage("Record exists with identity = " + workrequestimplementation.getIdentity(), 200);
        }
    }

    public void createWithContents(WorkRequestImplementation workrequestimplementation) throws DatabaseException, Exception {
        createIfNonexistent(workrequestimplementation);
        if(workrequestimplementation.getAcquisitionConstraints() != null) {
            OTDRAcquisitionConstraintsDatabase otdracquisitionconstraintsdatabase = new OTDRAcquisitionConstraintsDatabase();
            otdracquisitionconstraintsdatabase.createIfNonexistent((OTDRAcquisitionConstraints)workrequestimplementation.getAcquisitionConstraints());
        }
        if(workrequestimplementation.getAnalysisConstraints() != null) {
            OTDRAnalysisConstraintsDatabase otdranalysisconstraintsdatabase = new OTDRAnalysisConstraintsDatabase();
            otdranalysisconstraintsdatabase.createIfNonexistent((OTDRAnalysisConstraints)workrequestimplementation.getAnalysisConstraints());
        }
        if(workrequestimplementation.getValidationConstraints() != null) {
            OTDRValidationConstraintsDatabase otdrvalidationconstraintsdatabase = new OTDRValidationConstraintsDatabase();
            otdrvalidationconstraintsdatabase.createIfNonexistent((OTDRValidationConstraints)workrequestimplementation.getValidationConstraints());
        }
    }

    public void delete(WorkRequestImplementation workrequestimplementation) throws NullPointerException, Exception {
        Identity identity = workrequestimplementation.getIdentity();
        if(identity == null)
            throw new NullPointerException("WorkRequest Identity is null");
        identity = workrequestimplementation.getModuleIdentity();
        if(identity == null)
            throw new NullPointerException("Module Identity is null");
        identity = workrequestimplementation.getAcquisitionConstraintsIdentity();
        if(identity == null)
            throw new NullPointerException("AcquisitionConstraints Identity is null");
        identity = workrequestimplementation.getMonitoredElementIdentity();
        if(identity == null)
            throw new NullPointerException("MonitoredElement Identity is null");
        Statement statement = null;
        try {
            statement = Conn.createStatement();
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("WorkRequestDatabase: Error (9) ", 200);
            Log.systemLog.logErrorException(sqlexception);
            throw new DatabaseException("Unable to connect to Database.");
        }
        String s = "DELETE FROM WorkRequest WHERE Identity = '" + workrequestimplementation.getIdentity().toString() + "'";
        try {
            Log.systemLog.logDebugMessage("Trying: " + s, 200);
            int i = statement.executeUpdate(s);
            statement.close();
            statement = null;
        }
        catch(SQLException sqlexception1) {
            Log.systemLog.logDebugMessage("WorkRequestDatabase: Error (10) ", 200);
            Log.systemLog.logErrorException(sqlexception1);
            throw new DatabaseException("Delete of WorkRequest from Database failed.");
        }
        finally {
            try {
                if(statement != null)
                    statement.close();
                statement = null;
            }
            catch(SQLException _ex) { }
        }
    }

    public static int getScheduleType(Identity identity) {
        Statement statement = null;
        int i = 0;
        try {
            Connection connection = ServerDbConnection.getConnection();
            statement = connection.createStatement();
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("WorkRequestDatabase: Error (13) ", 200);
            Log.systemLog.logErrorException(sqlexception);
        }
        String s = "SELECT ScheduleType FROM WorkRequest Where MonitoredElementIdentity = '" + identity.toString() + "'";
        try {
            ResultSet resultset = statement.executeQuery(s);
            if(resultset.next())
                i = resultset.getInt("ScheduleType");
            statement.close();
            statement = null;
        }
        catch(SQLException sqlexception1) {
            Log.systemLog.logDebugMessage("WorkRequestDatabase: Error (14) ", 200);
            Log.systemLog.logErrorException(sqlexception1);
        }
        return i;
    }

    public static int getSurveillanceStatus(Identity identity) {
        Statement statement = null;
        int i = -1;
        try {
            Connection connection = ServerDbConnection.getConnection();
            statement = connection.createStatement();
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("WorkRequestDatabase: Error (15) ", 200);
            Log.systemLog.logErrorException(sqlexception);
        }
        String s = "SELECT Status FROM WorkRequest Where MonitoredElementIdentity = '" + identity.toString() + "'" + " and ScheduleType = " + 2 + " order by RequestDate";
        try {
            Log.systemLog.logDebugMessage("Trying: " + s, 200);
            ResultSet resultset = statement.executeQuery(s);
            int j;
            for(i = 0; resultset.next() && i != 2; Log.systemLog.logDebugMessage("getSurveillanceStatus new status: " + j + " surveillance status: " + i, 200)) {
                j = resultset.getInt("Status");
                if(i == 0 || j == 2 || j < i)
                    i = j;
            }

            Log.systemLog.logDebugMessage("getSurveillanceStatus returning status: " + i, 200);
            statement.close();
            statement = null;
        }
        catch(SQLException sqlexception1) {
            Log.systemLog.logErrorMessage("WorkRequestDatabase: Error (16) ");
            Log.systemLog.logErrorException(sqlexception1);
        }
        return i;
    }

    public void load(WorkRequestImplementation workrequestimplementation) throws Exception {
        Statement statement = null;
        Identity identity = null;
        Identity identity1 = null;
        Object obj = null;
        try {
            statement = Conn.createStatement();
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("WorkRequestDatabase: Error (3) ", 200);
            Log.systemLog.logErrorException(sqlexception);
        }
        String s = "SELECT " + selectAttributes + " FROM WorkRequest WHERE Identity = '" + workrequestimplementation.getIdentity().toString() + "'";
        Log.systemLog.logDebugMessage("Trying: " + s, 200);
//----------
				ResultSet resultset = null;
//----------
        try {
            resultset = statement.executeQuery(s);
            if(resultset.next()) {
                if(resultset.getString("AnalysisConstraintsIdentity") != null)
                    identity = new Identity(resultset.getString("AnalysisConstraintsIdentity"));
                if(resultset.getString("ValidationConstraintsIdentity") != null)
                    identity1 = new Identity(resultset.getString("ValidationConstraintsIdentity"));
                workrequestimplementation.setAttributes(resultset.getInt("Task"), new Identity(resultset.getString("ModuleIdentity")), new Identity(resultset.getString("MonitoredElementIdentity")), resultset.getInt("Port"), new Identity(resultset.getString("AcqConstraintsIdentity")), resultset.getInt("AcquisitionType"), resultset.getInt("ScheduleType"), null, resultset.getInt("Status"), new Identity(resultset.getString("UserIdentity")), resultset.getString("StatusString"), identity, 
                              identity1, DatabaseDate.fromDatabaseDate(resultset, "RequestDate"), resultset.getInt("PendingStatus"), resultset.getString("ServerName"), resultset.getString("AmficomMeasurementId"));
            } else {
                throw new Exception("WorkRequest does not exist");
            }
            OTDRAcquisitionConstraints otdracquisitionconstraints = new OTDRAcquisitionConstraints(workrequestimplementation.getAcquisitionConstraintsIdentity());
            OTDRAcquisitionConstraintsDatabase otdracquisitionconstraintsdatabase = new OTDRAcquisitionConstraintsDatabase();
            otdracquisitionconstraintsdatabase.load(otdracquisitionconstraints);
            workrequestimplementation.setAcquisitionConstraints(otdracquisitionconstraints);
            if(identity != null)
                try {
                    OTDRAnalysisConstraintsDatabase otdranalysisconstraintsdatabase = new OTDRAnalysisConstraintsDatabase();
                    OTDRAnalysisConstraints otdranalysisconstraints = new OTDRAnalysisConstraints(identity);
                    otdranalysisconstraintsdatabase.load(otdranalysisconstraints);
                    workrequestimplementation.setAnalysisConstraints(otdranalysisconstraints);
                }
                catch(Exception exception) {
                    Log.systemLog.logErrorMessage("WorkRequestDatabase: Error (4) exception loading analysis constraints");
                    Log.systemLog.logErrorException(exception);
                }
            if(identity1 != null)
                try {
                    OTDRValidationConstraintsDatabase otdrvalidationconstraintsdatabase = new OTDRValidationConstraintsDatabase();
                    OTDRValidationConstraints otdrvalidationconstraints = new OTDRValidationConstraints(identity1);
                    otdrvalidationconstraintsdatabase.load(otdrvalidationconstraints);
                    workrequestimplementation.setValidationConstraints(otdrvalidationconstraints);
                    loadOTDRConfirmationConstraints(workrequestimplementation, otdrvalidationconstraints.getBaselineCharacterization());
                }
                catch(Exception exception1) {
                    Log.systemLog.logErrorMessage("WorkRequestDatabase: Error (5) exception loading validation constraints");
                    Log.systemLog.logErrorException(exception1);
                }
            WorkReportDatabase workreportdatabase = new WorkReportDatabase();
            workrequestimplementation.workReports = workreportdatabase.loadWorkReportVector(workrequestimplementation.getIdentity());
        }
        catch(SQLException sqlexception1) {
            Log.systemLog.logErrorMessage("WorkRequestDatabase: Error (6) exception loading work report vector");
            Log.systemLog.logErrorException(sqlexception1);
        }
//----------
				finally {
            try {
                if(statement != null)
                    statement.close();
                statement = null;
                if(resultset != null) {
                    resultset.close();
                    resultset = null;
                }
            }
            catch(SQLException _ex) { }
        }
//----------
    }

    public void loadOTDRConfirmationConstraints(WorkRequestImplementation workrequestimplementation, Identity identity) {
        Statement statement = null;
        try {
            statement = Conn.createStatement();
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logErrorMessage("WorkRequestDatabase: Error (3a) loading OTDR Confirmation Constraints ");
            Log.systemLog.logErrorException(sqlexception);
        }
        String s = "select constraintidentity from measurement where identity=(select measurementidentity from characterization where identity='" + identity + "')";
        ResultSet resultset = null;
        try {
            Log.systemLog.logDebugMessage("Trying: " + s, 200);
            resultset = statement.executeQuery(s);
            if(resultset.next()) {
                Identity identity1 = new Identity(resultset.getString("ConstraintIdentity"));
                OTDRAcquisitionConstraints otdracquisitionconstraints = new OTDRAcquisitionConstraints(identity1);
                OTDRAcquisitionConstraintsDatabase otdracquisitionconstraintsdatabase = new OTDRAcquisitionConstraintsDatabase();
                otdracquisitionconstraintsdatabase.load(otdracquisitionconstraints);
                workrequestimplementation.setConfirmationAcquisitionConstraints(otdracquisitionconstraints);
            }
        }
        catch(Exception exception1) {
            Log.systemLog.logErrorMessage("WorkRequestDatabase: Error (3b) loading OTDR Confirmation Constraints ");
            Log.systemLog.logErrorException(exception1);
        }
        finally {
            try {
                if(statement != null)
                    statement.close();
                statement = null;
                if(resultset != null) {
                    resultset.close();
                    resultset = null;
                }
            }
            catch(SQLException _ex) { }
        }
    }

    public WorkRequestImplementation loadWorkRequestByIdentity(Identity identity) throws Exception {
        WorkRequestImplementation workrequestimplementation = new WorkRequestImplementation();
        workrequestimplementation.setIdentity(identity);
        load(workrequestimplementation);
        return workrequestimplementation;
    }

    public static Identity[] retrieveIdentities() {
        WorkRequestIdentityList = new LinkedList();
        Statement statement = null;
        try {
            Connection connection = ServerDbConnection.getConnection();
            statement = connection.createStatement();
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("WorkRequestDatabase: Error (1) ", 200);
            Log.systemLog.logErrorException(sqlexception);
        }
        String s = "SELECT Identity FROM WorkRequest";
        Log.systemLog.logDebugMessage("Trying: " + s, 200);
        try {
            String s1;
            for(ResultSet resultset = statement.executeQuery(s); resultset.next(); WorkRequestIdentityList.add(new Identity(s1)))
                s1 = resultset.getString("Identity");

            statement.close();
            statement = null;
        }
        catch(SQLException sqlexception1) {
            Log.systemLog.logDebugMessage("WorkRequestDatabase: Error (2) ", 200);
            Log.systemLog.logErrorException(sqlexception1);
        }
        Identity aidentity[] = new Identity[WorkRequestIdentityList.size()];
        return (Identity[])WorkRequestIdentityList.toArray(aidentity);
    }

    public void save(WorkRequestImplementation workrequestimplementation) throws Exception {
        String s = null;
        String s1 = null;
        String s2 = null;
        String s3 = null;
        Statement statement = null;
        try {
            statement = Conn.createStatement();
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("WorkRequestDatabase: Error (11) ", 200);
            Log.systemLog.logErrorException(sqlexception);
            throw new DatabaseException("Unable to create Statement Ojbect for Database access.");
        }
        if(workrequestimplementation.getAcquisitionConstraints() != null)
            s = new String(" AcqConstraintsIdentity = '" + workrequestimplementation.getAcquisitionConstraintsIdentity().toString() + "'");
        else
            s = new String(" AcqConstraintsIdentity = null");
        if(workrequestimplementation.getAnalysisConstraints() != null)
            s1 = new String(" AnalysisConstraintsIdentity = '" + workrequestimplementation.getAnalysisConstraintsIdentity().toString() + "'");
        else
            s1 = new String(" AnalysisConstraintsIdentity = null");
        if(workrequestimplementation.getValidationConstraints() != null)
            s2 = new String(" ValidationConstraintsIdentity = '" + workrequestimplementation.getValidationConstraintsIdentity().toString() + "'");
        else
            s2 = new String(" ValidationConstraintsIdentity = null");
        if(workrequestimplementation.getServerName() != null)
            s3 = new String(" ServerName = '" + workrequestimplementation.getServerName() + "'");
        else
            s3 = new String(" ServerName = null");
        String s4 = "UPDATE WorkRequest SET Task = " + workrequestimplementation.getTask() + 
              ", ModuleIdentity = '" + workrequestimplementation.getModuleIdentity().toString()
              + "', MonitoredElementIdentity = '" +
              workrequestimplementation.getMonitoredElementIdentity().toString() + "', Port = " +
              workrequestimplementation.getPort() + "," + s + ", AcquisitionType = " +
              workrequestimplementation.getAcquisitionType() + ", ScheduleType = " + 
              workrequestimplementation.getScheduleType() + ", Schedule = '" +
              workrequestimplementation.getSchedule() + "', Status = " +
              workrequestimplementation.getStatus() + ", UserIdentity = '" + 
              workrequestimplementation.getUserIdentity() + "'" + ", StatusString = '" + 
              workrequestimplementation.getStatusString() + "'" + "," + s1 + "," + s2 + "," + 
              s3 + ", RequestDate = " + 
              DatabaseDate.toDatabaseDate(workrequestimplementation.getDate()) + 
              ", PendingStatus = " + workrequestimplementation.getPendingStatus() + 
              " WHERE Identity = '" + workrequestimplementation.getIdentity().toString() + "'";
        System.out.println("save" + s4);
        try {
            int i = statement.executeUpdate(s4);
            statement.close();
            statement = null;
        }
        catch(SQLException sqlexception1) {
            Log.systemLog.logDebugMessage("WorkRequestDatabase: Error (12) ", 200);
            Log.systemLog.logErrorException(sqlexception1);
            throw new DatabaseException("Attempt to update WorkRequest Database failed.");
        }
        finally {
            try {
                if(statement != null)
                    statement.close();
                statement = null;
            }
            catch(SQLException _ex) { }
        }
    }

}
