package com.syrus.AMFICOM.Client.Map;

import java.util.*;

import com.ofx.base.*;
import com.ofx.repository.*;

public class MapManager 
{
	public MapManager()
	{
	}

    static public boolean openSession(
		String sessionName,
		String dbUserName,
		String dbPassword)
	{
        SxProperties.singleton().setProperty("ofx.userName", dbUserName);
        SxProperties.singleton().setProperty("ofx.password", dbPassword);
        com.ofx.query.SxQueryInterface qsi = SxEnvironment.singleton().getQuery();
        Vector theProperties = SxEnvironment.singleton().getProperties().getQuerySessionOpenProperties();
        String dbType = (String)theProperties.elementAt(0);
        String dbURL = (String)theProperties.elementAt(3);
        String jdbcDriverClass = (String)theProperties.elementAt(4);
        String sdoAdminUserName = (String)theProperties.elementAt(5);
/*
        if(OmUtil.isAFileDatabase(dbType)) 
		{
            URL url = null;
            try 
			{
                url = new URL(sessionName);
            }
            catch(MalformedURLException _ex) 
			{
                System.out.println("URL specification is invalid.");
                return false;
            }
            File file = new File(url.getFile());
            if(!file.exists()) 
			{
                System.out.println("Unable to open; SpatialFX data not present.");
                return false;
            }
        }
*/
        boolean result = false;
        try 
		{
            result = qsi.openSession(dbType, sessionName, dbUserName, dbPassword, dbURL, jdbcDriverClass, sdoAdminUserName);
        }
        catch(Exception ex) 
		{
            System.out.println("Exception occurred opening database named: " + sessionName + " exception: " + ex);
        }
        if(!result)
            System.out.println("Unable to open; Check log file for details.");
        else
        if(!qsi.doesSpatialSchemaExist()) 
		{
            qsi.close();
            System.out.println("Unable to open data tables; they do not exist.");
			return false;
		}
		return true;
    }
	
}