/**
 * $Id: OfxConnection.java,v 1.14 2005/09/07 15:53:36 arseniy Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */

package com.syrus.AMFICOM.client.map.objectfx;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;
import java.util.logging.Level;

import com.ofx.base.SxEnvironment;
import com.ofx.component.swing.JMapViewer;
import com.ofx.mapViewer.SxMapLayer;
import com.ofx.mapViewer.SxMapLayerInterface;
import com.ofx.mapViewer.SxMapViewer;
import com.ofx.mapViewer.SxMarkerLayer;
import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapContext;
import com.syrus.AMFICOM.client.map.MapCoordinatesConverter;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.util.Log;

/**
 * Реализация соединения с хранилищем данных в формате SpatialFX.
 * @version $Revision: 1.14 $, $Date: 2005/09/07 15:53:36 $
 * @author $Author: arseniy $
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

	/**
	 * Объект-слой, содержищий объекты топографической схемы
	 */
	SxMapLayer spatialLayer = null;

	/**
	 * Ссылка на компонент, отображающий карту
	 */
	SxMapViewer sxMapViewer = null;

	// TODO this is really bad!!
	private OfxNetMapViewer ofxLayerPainter;
	
	public OfxConnection()
	{
		Log.debugMessage(getClass().getName() + "::" + "OfxConnection()" + " | " + "constructor call", Level.FINER);
	}
	
	@Override
	public void setPath(String path)
	{
		Log.debugMessage(getClass().getName() + "::" + "setPath(" + path + ")" + " | " + "method call", Level.FINER);
		
		this.dataBasePath = path;
	}
	
	@Override
	public void setView(String name)
	{
		Log.debugMessage(getClass().getName() + "::" + "setView(" + name + ")" + " | " + "method call", Level.FINER);

		this.dataBaseView = name;
	}

	@Override
	public void setURL(String url)
	{
		this.dataBaseURL = url;
	}

	@Override
	public String getPath()
	{
		return OFX_DATABASE_PREFIX + this.dataBasePath;
	}
	
	@Override
	public String getView()
	{
		return this.dataBaseView;
	}

	@Override
	public String getURL()
	{
		return this.dataBaseURL;
	}

	@Override
	public boolean connect() throws MapConnectionException
	{
		if(this.dataBaseURL == null
			|| this.dataBaseView == null
			|| this.dataBasePath == null)
				throw new MapConnectionException("insufficient data for map connection");
	
		Log.debugMessage(getClass().getName() + "::" + "connect()" + " | " + "method call", Level.FINER);
		
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

			this.sxMapViewer = this.jMapViewer.getSxMapViewer();
			
			this.spatialLayer = new AMFICOMSxMapLayer(this);

			this.sxMapViewer.addLayer( "Network layer", this.spatialLayer);

			try 
			{
				SxMarkerLayer markerLayer = (SxMarkerLayer) 
						this.sxMapViewer.getLayer(SxMapLayerInterface.MARKER);
				markerLayer.listenForMapEvents( false );
				markerLayer.setEnabled(false);
			} 
			catch (Exception ex) 
			{
					ex.printStackTrace();
			} 
			
			this.sxMapViewer.removeNamedLayer("OFX LOGO");
			this.sxMapViewer.removeNamedLayer("OFX COPYRIGHT");

		}
		catch(Exception e)
		{
			throw new MapConnectionException(e);
		}

		return true;
	}
	
	@Override
	public boolean release() throws MapConnectionException
	{
		Log.debugMessage(getClass().getName() + "::" + "release()" + " | " + "method call", Level.FINER);

		this.spatialLayer.release();
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
	@Override
	public List<String> getAvailableViews() throws MapDataException {
		try {
			return this.jMapViewer.getAvailableMaps();
		} catch (Exception ex) {
			throw new MapDataException(ex);
		}
	}

	@Override
	public List<SpatialLayer> getLayers() throws MapDataException {
		List<SpatialLayer> returnList = new LinkedList<SpatialLayer>();

		int sortOrder = 301;// as used in Ofx.JMapLegend
		
		SxMapViewer sxMapViewer1 = this.getJMapViewer().getSxMapViewer();
		
		Vector foregroundClasses = sxMapViewer1.getForegroundClasses(sortOrder);
		for(Iterator it = foregroundClasses.iterator(); it.hasNext();)
		{
			String s = (String )it.next();
			SpatialLayer sl = new OfxSpatialLayer(sxMapViewer1, s);
			returnList.add(sl);
			Vector vector2 = sxMapViewer1.classBinNames(s);
			for(Iterator it2 = vector2.iterator(); it2.hasNext();)
			{
				String s2 = (String )it2.next();
				SpatialLayer sl2 = new OfxSpatialLayer(sxMapViewer1, s2);
				returnList.add(sl2);
			}
		}

		{
		Vector backgroundClasses = sxMapViewer1.getBackgroundClasses();
		for(Iterator it = backgroundClasses.iterator(); it.hasNext();)
		{
			String s = (String )it.next();
			SpatialLayer sl = new OfxSpatialLayer(sxMapViewer1, s);
			returnList.add(sl);
			Vector vector2 = sxMapViewer1.classBinNames(s);
			for(Iterator it2 = vector2.iterator(); it2.hasNext();)
			{
				String s2 = (String )it2.next();
				SpatialLayer sl2 = new OfxSpatialLayer(sxMapViewer1, s2);
				returnList.add(sl2);
			}
		}
		}

		return returnList;
	}

	public JMapViewer getJMapViewer()
	{
		return this.jMapViewer;
	}

	@Override
	public MapImageLoader createImageLoader() {
		return new OfxImageLoader(this);
	}

	@Override
	public MapCoordinatesConverter createCoordinatesConverter() {
		return new OfxCoordinatesConverter(this);
	}

	@Override
	public MapContext createMapContext() {
		return new OfxContext(this);
	}

	/**
	 * @return Returns the sxMapViewer.
	 */
	public SxMapViewer getSxMapViewer() {
		return this.sxMapViewer;
	}

	/**
	 * @return Returns the spatialLayer.
	 */
	public SxMapLayer getSpatialLayer() {
		return this.spatialLayer;
	}

	// TODO this is really bad!!
	public void setOfxLayerPainter(OfxNetMapViewer viewer) {
		this.ofxLayerPainter = viewer;
	}

	// TODO this is really bad!!
	public OfxNetMapViewer getOfxLayerPainter() {
		return this.ofxLayerPainter;
	}
}
