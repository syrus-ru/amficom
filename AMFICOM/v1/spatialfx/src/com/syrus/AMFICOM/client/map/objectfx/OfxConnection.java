/**
 * $Id: OfxConnection.java,v 1.7 2005/02/25 13:58:59 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.Client.Map.ObjectFX;

import com.ofx.base.SxEnvironment;
import com.ofx.component.swing.JMapViewer;

import com.syrus.AMFICOM.Client.General.Model.Environment;
import com.syrus.AMFICOM.Client.Map.MapConnection;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;

import java.util.List;

/**
 * Реализация соединения с хранилищем данных в формате SpatialFX.
 * @version $Revision: 1.7 $, $Date: 2005/02/25 13:58:59 $
 * @author $Author: krupenn $
 * @module spatialfx_v1
 */
public class OfxConnection extends MapConnection
{
	protected String dbUserName = "user";
	protected String dbPassword = "";

	protected String dataBaseURL = null;
	protected String dataBasePath = null;
	protected String dataBaseView = null;

	protected static final String OFX_DATABASE_PREFIX = "file://localhost/";

	protected JMapViewer jMapViewer = null;
	
	protected static boolean dbset = false;

	public OfxConnection()
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"constructor call", 
				getClass().getName(), 
				"OfxConnection()");
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
		this.dataBaseURL = url;
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
		return this.dataBaseURL;
	}

	public boolean connect() throws MapConnectionException
	{
		if(this.dataBaseURL == null
			|| this.dataBaseView == null
			|| this.dataBasePath == null)
				throw new MapConnectionException("insufficient data for map connection");
	
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"connect()");
		
		String sessionName = OFX_DATABASE_PREFIX + this.dataBasePath;
/*		
        SxProperties.singleton().setProperty("ofx.userName", this.dbUserName);
        SxProperties.singleton().setProperty("ofx.password", this.dbPassword);
        SxProperties.singleton().setProperty("ofx.domainDimension", "3");
        com.ofx.query.SxQueryInterface qsi = SxEnvironment.singleton().getQuery();
        Vector sessionProperties = 
			SxEnvironment.singleton().getProperties().getQuerySessionOpenProperties();
        String dbType = (String)sessionProperties.elementAt(0);
        String dbURL = (String)sessionProperties.elementAt(3);
        String jdbcDriverClass = (String)sessionProperties.elementAt(4);
        String sdoAdminUserName = (String)sessionProperties.elementAt(5);

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

        boolean result = false;
        try 
		{
            result = qsi.openSession(
					dbType, 
					sessionName, 
					this.dbUserName, 
					this.dbPassword, 
					dbURL, 
					jdbcDriverClass, 
					sdoAdminUserName);
        }
        catch(Exception ex) 
		{
            System.out.println("Exception occurred opening database named: " 
					+ sessionName + " exception: " + ex);
			throw new MapConnectionException(
				"Exception occurred opening database named: " + sessionName,
				ex);
        }
        if(!result)
		{
            System.out.println("Unable to open; Check log file for details.");
			throw new MapConnectionException("Unable to open; Check log file for details.");
		}
        else
        if(!qsi.doesSpatialSchemaExist()) 
		{
            qsi.close();
            System.out.println("Unable to open data tables; they do not exist.");
			throw new MapConnectionException("Unable to open data tables; they do not exist.");
		}
*/
		try
		{
			this.jMapViewer = new JMapViewer();

			if(!dbset)
				this.jMapViewer.setDBName(sessionName);
			this.jMapViewer.setMapName(this.dataBaseView);
		}
		catch(Exception e)
		{
			throw new MapConnectionException(e);
		}

		return true;
	}
	
	public boolean release() throws MapConnectionException
	{
		Environment.log(
				Environment.LOG_LEVEL_FINER, 
				"method call", 
				getClass().getName(), 
				"release()");

		try
		{
			SxEnvironment.singleton().getQuery().close();
			this.jMapViewer.closeSession();
			com.ofx.service.SxServiceFactory.shutdown();
		}
		catch (Exception e)
		{
			throw new MapConnectionException(e);
		}
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.Client.Map.MapConnection#getAvailableViews()
	 */
	public List getAvailableViews() throws MapDataException {
		try 
		{
		return this.jMapViewer.getAvailableMaps();
		} 
		catch (Exception ex) 
		{
			throw new MapDataException(ex);
		} 
	}


	public JMapViewer getJMapViewer()
	{
		return this.jMapViewer;
	}
}
