/**
 * $Id: OfxConnection.java,v 1.2 2005/01/12 14:24:07 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/
package com.syrus.AMFICOM.Client.Map.ObjectFX;

import com.ofx.base.SxEnvironment;
import com.ofx.base.SxProperties;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.MapConnection;

import java.util.Vector;

/**
 * Реализация соединения с хранилищем данных в формате SpatialFX
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2005/01/12 14:24:07 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class OfxConnection extends MapConnection
{
	protected String dbUserName = "user";
	protected String dbPassword = "";

	protected String dataBasePath = "";
	protected String dataBaseView = "";

	protected static final String OFX_DATABASE_PREFIX = "file://localhost/";
	
	public OfxConnection()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"OfxConnection()");
	}
	
	public OfxConnection( String dataBasePath, String dataBaseView)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"OfxConnection(" + dataBasePath + ", " + dataBaseView + ")");
		
		this.setPath(dataBasePath);
		this.setView(dataBaseView);
	}

	public void setPath(String path)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setPath(" + path + ")");
		
		this.dataBasePath = path;
	}
	
	public void setView(String name)
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"setView(" + name + ")");

		this.dataBaseView = name;
	}

	public void setURL(String url)
	{
	}

	public String getPath()
	{
		return OFX_DATABASE_PREFIX + this.dataBasePath;
	}
	
	public String getView()
	{
		return this.dataBaseView;
	}

	public String getURL()
	{
		return "";
	}

	public boolean connect()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"connect()");
		
		String sessionName = OFX_DATABASE_PREFIX + dataBasePath;
		
        SxProperties.singleton().setProperty("ofx.userName", dbUserName);
        SxProperties.singleton().setProperty("ofx.password", dbPassword);
        SxProperties.singleton().setProperty("ofx.domainDimension", "3");
        com.ofx.query.SxQueryInterface qsi = SxEnvironment.singleton().getQuery();
        Vector theProperties = 
			SxEnvironment.singleton().getProperties().getQuerySessionOpenProperties();
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
            result = qsi.openSession(
					dbType, 
					sessionName, 
					dbUserName, 
					dbPassword, 
					dbURL, 
					jdbcDriverClass, 
					sdoAdminUserName);
        }
        catch(Exception ex) 
		{
            System.out.println("Exception occurred opening database named: " 
					+ sessionName + " exception: " + ex);
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
	
	public boolean release()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"release()");
		
		return true;
	}
}
