// NMI's Java Code Viewer 5.1 © 1997-2001 B. Lemaire
// http://njcv.htmlplanet.com - info@njcv.htmlplanet.com

// Copy registered to Evaluation Copy                                   

// Source File Name:   SystemDefaultDatabase.java

package com.gnnettest.questfiber.work;

import com.gnnettest.questfiber.util.*;
import java.sql.*;
import java.util.LinkedList;

// Referenced classes of package com.gnnettest.questfiber.work:
//            Constraints, DatabaseException, OTDRAcquisitionConstraints, OTDRAcquisitionConstraintsDatabase, 
//            OTDRAnalysisConstraints, OTDRAnalysisConstraintsDatabase, OTDRValidationConstraints, OTDRValidationConstraintsDatabase

public class SystemDefaultDatabase {

    private String sIdentity;
    private static Connection Conn;

    public SystemDefaultDatabase() {
        Conn = ServerDbConnection.getConnection();
    }

    private void create(Identity identity, int i) throws NullPointerException, Exception {
        Statement statement = null;
        if(identity == null)
            throw new NullPointerException("Identity is null");
        try {
            statement = Conn.createStatement();
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("SystemDefaultDatabase Error: (11)", 200);
            Log.systemLog.logErrorException(sqlexception);
            throw new DatabaseException("Unable to create Statement Ojbect for Database access.");
        }
        String s = "INSERT INTO SystemDefaults (Identity, Type) VALUES ('" + identity.toString() + "'," + Integer.toString(i) + ")";
        Log.systemLog.logDebugMessage("Trying: " + s, 200);
        int j;
        try {
            j = statement.executeUpdate(s);
        }
        catch(SQLException sqlexception1) {
            Log.systemLog.logDebugMessage("SystemDefaultDatabase Error: (13)", 200);
            Log.systemLog.logErrorException(sqlexception1);
            throw new DatabaseException("Unable to create SystemDefaults entry in Database.");
        }
        finally {
            if(statement != null)
                statement.close();
            statement = null;
        }
    }

    public OTDRAcquisitionConstraints getOTDRAcquisitionConstraints() throws Exception {
        OTDRAcquisitionConstraints otdracquisitionconstraints = null;
        Statement statement = null;
        try {
            statement = Conn.createStatement();
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("SystemDefaultDatabase Error: (3)", 200);
            Log.systemLog.logErrorException(sqlexception);
        }
        OTDRAcquisitionConstraintsDatabase otdracquisitionconstraintsdatabase = new OTDRAcquisitionConstraintsDatabase();
        String s = "SELECT Identity FROM OTDRAcquisitionConstraints WHERE Identity IN (SELECT Identity FROM SystemDefaults WHERE Type = 0)";
//------------
				ResultSet resultset = null;
//------------
        try {
            Log.systemLog.logDebugMessage("Trying: " + s, 200);
            resultset = statement.executeQuery(s);
            if(resultset.next()) {
                otdracquisitionconstraints = new OTDRAcquisitionConstraints(new Identity(resultset.getString("Identity")));
                otdracquisitionconstraintsdatabase.load(otdracquisitionconstraints);
            } else {
                otdracquisitionconstraints = new OTDRAcquisitionConstraints();
                otdracquisitionconstraints.createIdentity();
                otdracquisitionconstraintsdatabase.create(otdracquisitionconstraints);
                create(otdracquisitionconstraints.getIdentity(), 0);
            }
        }
        catch(SQLException sqlexception1) {
            Log.systemLog.logDebugMessage("SystemDefaultDatabase Error: (4)", 200);
            Log.systemLog.logErrorException(sqlexception1);
        }
//------------
				finally {
            try {
                if(resultset != null)
                    resultset.close();
                resultset = null;
                if(statement != null)
                    statement.close();
                statement = null;
            }
            catch(SQLException _ex) { }
        }
//------------
        return otdracquisitionconstraints;
    }

    public OTDRAcquisitionConstraints getOTDRAcquisitionSurveillanceConstraints() throws Exception {
        OTDRAcquisitionConstraints otdracquisitionconstraints = null;
        Statement statement = null;
        try {
            statement = Conn.createStatement();
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("SystemDefaultDatabase Error: (1)", 200);
            Log.systemLog.logErrorException(sqlexception);
        }
        OTDRAcquisitionConstraintsDatabase otdracquisitionconstraintsdatabase = new OTDRAcquisitionConstraintsDatabase();
        String s = "SELECT Identity FROM OTDRAcquisitionConstraints WHERE Identity IN (SELECT Identity FROM SystemDefaults WHERE Type = 1)";
        Log.systemLog.logDebugMessage("Trying: " + s, 200);
        ResultSet resultset = null;
        try {
            resultset = statement.executeQuery(s);
            if(resultset.next()) {
                otdracquisitionconstraints = new OTDRAcquisitionConstraints(new Identity(resultset.getString("Identity")));
                otdracquisitionconstraintsdatabase.load(otdracquisitionconstraints);
            } else {
                otdracquisitionconstraints = new OTDRAcquisitionConstraints();
                otdracquisitionconstraints.createIdentity();
                otdracquisitionconstraintsdatabase.create(otdracquisitionconstraints);
                create(otdracquisitionconstraints.getIdentity(), 1);
            }
        }
        catch(SQLException sqlexception1) {
            Log.systemLog.logDebugMessage("SystemDefaultDatabase Error: (6)", 200);
            Log.systemLog.logErrorException(sqlexception1);
        }
        finally {
            try {
                if(resultset != null)
                    resultset.close();
                resultset = null;
                if(statement != null)
                    statement.close();
                statement = null;
            }
            catch(SQLException _ex) { }
        }
        return otdracquisitionconstraints;
    }

    public OTDRAnalysisConstraints getOTDRAnalysisConstraints() throws Exception {
        OTDRAnalysisConstraints otdranalysisconstraints = null;
        Statement statement = null;
        try {
            statement = Conn.createStatement();
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("SystemDefaultDatabase Error: (7)", 200);
            Log.systemLog.logErrorException(sqlexception);
        }
        OTDRAnalysisConstraintsDatabase otdranalysisconstraintsdatabase = new OTDRAnalysisConstraintsDatabase();
        String s = "SELECT Identity FROM OTDRAnalysisConstraints WHERE Identity IN (SELECT Identity FROM SystemDefaults WHERE Type = 0)";
        Log.systemLog.logDebugMessage("Trying: " + s, 200);
        ResultSet resultset = null;
        try {
            resultset = statement.executeQuery(s);
            if(resultset.next()) {
                otdranalysisconstraints = new OTDRAnalysisConstraints(new Identity(resultset.getString("Identity")));
                otdranalysisconstraintsdatabase.load(otdranalysisconstraints);
            } else {
                otdranalysisconstraints = new OTDRAnalysisConstraints();
                otdranalysisconstraints.createIdentity();
                otdranalysisconstraintsdatabase.create(otdranalysisconstraints);
                create(otdranalysisconstraints.getIdentity(), 0);
            }
        }
        catch(SQLException sqlexception1) {
            Log.systemLog.logDebugMessage("SystemDefaultDatabase Error: (8)", 200);
            Log.systemLog.logErrorException(sqlexception1);
        }
        finally {
            try {
                if(resultset != null)
                    resultset.close();
                resultset = null;
                if(statement != null)
                    statement.close();
                statement = null;
            }
            catch(SQLException _ex) { }
        }
        return otdranalysisconstraints;
    }

    public OTDRValidationConstraints getOTDRValidationConstraints() throws Exception {
        OTDRValidationConstraints otdrvalidationconstraints = null;
        Statement statement = null;
        try {
            statement = Conn.createStatement();
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("SystemDefaultDatabase Error: (9)", 200);
            Log.systemLog.logErrorException(sqlexception);
        }
        OTDRValidationConstraintsDatabase otdrvalidationconstraintsdatabase = new OTDRValidationConstraintsDatabase();
        String s = "SELECT Identity FROM OTDRValidationConstraints WHERE Identity IN (SELECT Identity FROM SystemDefaults WHERE Type = 0)";
        Log.systemLog.logDebugMessage("Trying: " + s, 200);
        ResultSet resultset = null;
        try {
            resultset = statement.executeQuery(s);
            if(resultset.next()) {
                otdrvalidationconstraints = new OTDRValidationConstraints(new Identity(resultset.getString("Identity")));
                otdrvalidationconstraintsdatabase.load(otdrvalidationconstraints);
            } else {
                otdrvalidationconstraints = new OTDRValidationConstraints();
                otdrvalidationconstraints.createIdentity();
                otdrvalidationconstraintsdatabase.create(otdrvalidationconstraints);
                create(otdrvalidationconstraints.getIdentity(), 0);
            }
        }
        catch(SQLException sqlexception1) {
            Log.systemLog.logDebugMessage("SystemDefaultDatabase Error: (10)", 200);
            Log.systemLog.logErrorException(sqlexception1);
        }
        finally {
            try {
                if(resultset != null)
                    resultset.close();
                resultset = null;
                if(statement != null)
                    statement.close();
                statement = null;
            }
            catch(SQLException _ex) { }
        }
        return otdrvalidationconstraints;
    }

    public static Identity[] retrieveIdentities() {
        LinkedList linkedlist = new LinkedList();
        Statement statement = null;
        try {
            Connection connection = ServerDbConnection.getConnection();
            statement = connection.createStatement();
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("SystemDefaultDatabase Error: (1)", 200);
            Log.systemLog.logErrorException(sqlexception);
        }
        String s = "SELECT Identity FROM SystemDefaults";
        ResultSet resultset = null;
        try {
            Log.systemLog.logDebugMessage("Trying: " + s, 200);
            String s1;
            for(resultset = statement.executeQuery(s); resultset.next(); linkedlist.add(new Identity(s1)))
                s1 = resultset.getString("Identity");

        }
        catch(SQLException sqlexception1) {
            Log.systemLog.logDebugMessage("SystemDefaultDatabase Error: (2)", 200);
            Log.systemLog.logErrorException(sqlexception1);
        }
        finally {
            try {
                if(resultset != null)
                    resultset.close();
                resultset = null;
                if(statement != null)
                    statement.close();
                statement = null;
            }
            catch(SQLException _ex) { }
        }
        Identity aidentity[] = new Identity[linkedlist.size()];
        return (Identity[])linkedlist.toArray(aidentity);
    }

    public void save(Identity identity, int i) throws Exception {
        Statement statement = null;
        try {
            statement = Conn.createStatement();
        }
        catch(SQLException sqlexception) {
            Log.systemLog.logDebugMessage("SystemDefaultDatabase Error: (15)", 200);
            Log.systemLog.logErrorException(sqlexception);
            throw new DatabaseException("Unable to connect to Database.");
        }
        String s = "UPDATE SystemDefaults SET Type = " + i + " WHERE Identity = '" + identity.toString() + "'";
        Log.systemLog.logDebugMessage("Trying: " + s, 200);
        int j;
        try {
            j = statement.executeUpdate(s);
        }
        catch(SQLException sqlexception1) {
            Log.systemLog.logDebugMessage("SystemDefaultDatabase Error: (16)", 200);
            Log.systemLog.logErrorException(sqlexception1);
            throw new DatabaseException("Attempt to update SystemDefaults Database failed.");
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
