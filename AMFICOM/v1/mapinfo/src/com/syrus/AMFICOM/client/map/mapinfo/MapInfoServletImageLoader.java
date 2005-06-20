/**
 * $Id: MapInfoServletImageLoader.java,v 1.1.2.1 2005/06/20 15:31:23 peskovsky Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.ServletCommandNames;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.DoublePoint;
import com.syrus.AMFICOM.map.TopologicalImageQuery;

public class MapInfoServletImageLoader implements MapImageLoader
{
	private String uriString = null;

	private byte[] imageBuffer = null;
	
	private final MapInfoConnection connection;
	
	public MapInfoServletImageLoader(MapInfoConnection connection)
			throws MapConnectionException
	{
		this.connection = connection;

		this.uriString = this.connection.getURL();
		
		Dimension maximumImageSize = Toolkit.getDefaultToolkit().getScreenSize();
		int dataSize = maximumImageSize.width * maximumImageSize.height * 2;

		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
				" TIC - loadingthread - setImage - allocating for image buffer " + dataSize + " bytes of memory");
		
		this.imageBuffer = new byte[dataSize];
		
		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
				" TIC - loadingthread - setImage - memory allocated");
	}
	
	/**
	 * Посылает запрос на сервер на остановку рендеринга.
	 */
	public void stopRendering()
	{
		String requestString = new String(
				this.uriString +
				"?" + ServletCommandNames.COMMAND_NAME + "=" + ServletCommandNames.CN_CANCEL_RENDERING +
				"&" + ServletCommandNames.USER_ID +
				LoginManager.getSessionKey().hashCode());

		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
				" TIC - loadingthread - stopping rendering at server");
		
		try
		{
			URI mapServerURI = new URI(requestString);
			URL mapServerURL = new URL(mapServerURI.toASCIIString());
			URLConnection urlConnection = mapServerURL.openConnection();

            String connectionResult = null;
            while (connectionResult == null)
            {
                connectionResult = urlConnection.getHeaderField(ServletCommandNames.STATUS_FIELD_NAME);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }   

			if (!connectionResult.equals(ServletCommandNames.STATUS_SUCCESS))
			{
				System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
						" Server returned: " + connectionResult);
				
				return;
			}
//		if(connection.getInputStream() == null)
//			return;
			
		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Посылает запрос на рендеринг изображения на сервере
	 * @throws MapConnectionException 
	 */
	public Image renderMapImage(TopologicalImageQuery query) throws MapConnectionException
	{
		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
			" TIC - loadingthread - starting rendering at server");

		try
		{
			String requestString = this.uriString + this.createRenderCommandString(query);			
			URI mapServerURI = new URI(requestString);
			URL mapServerURL = new URL(mapServerURI.toASCIIString());
			URLConnection urlConnection = mapServerURL.openConnection();

      if(urlConnection.getInputStream() == null)
          return null;

			ObjectInputStream ois = new ObjectInputStream(urlConnection.getInputStream());

			System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
					" TIC - loadingthread - getServerMapImage - got data at ObjectInputStream");

			try
			{
				ois.readFully(this.imageBuffer);
			}
			catch(EOFException eofExc)
			{
			}
			
			System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
					" TIC - loadingthread - getServerMapImage - Read array from stream");

			String connectionResult = urlConnection.getHeaderField(ServletCommandNames.STATUS_FIELD_NAME);
            if (!connectionResult.equals(ServletCommandNames.STATUS_SUCCESS))
            {
                System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
                        " Server returned: " + connectionResult);
                
                return null;
            }
            
			ois.close();

			Image imageReceived = Toolkit.getDefaultToolkit().createImage(this.imageBuffer);

			System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
					" TIC - loadingthread - getServerMapImage - Image created");
			
			return imageReceived;

		} catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MapDataException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	/**
	 * Создаёт по текущему запросу строку параметров для HTTP запроса к серверу
	 * топографии для рендеринга графических данных
	 * @param query Запрос
	 * @return Строка параметров для HTTP запроса к серверу
	 * топографии 
	 * @throws MapConnectionException 
	 */
	private String createRenderCommandString(TopologicalImageQuery query)
		throws MapDataException, MapConnectionException
	{
		String result = "";
		result += "?" + ServletCommandNames.USER_ID + "="
		    + LoginManager.getSessionKey().hashCode();
		
		result += "&" + ServletCommandNames.COMMAND_NAME + "="
		+ ServletCommandNames.CN_START_RENDER_IMAGE;
		
		result += "&" + ServletCommandNames.PAR_WIDTH + "="	+ query.getMapImageWidth();
		result += "&" + ServletCommandNames.PAR_HEIGHT + "=" + query.getMapImageHeight();
		result += "&" + ServletCommandNames.PAR_CENTER_X + "=" + query.getTopoCenterX();
		result += "&" + ServletCommandNames.PAR_CENTER_Y + "=" + query.getTopoCenterY();
		result += "&" + ServletCommandNames.PAR_ZOOM_FACTOR + "=" + query.getTopoScale();

		int index = 0;
		
		Iterator layersIt = this.connection.getLayers().iterator();
		for(; layersIt.hasNext();)
		{
			SpatialLayer spL = (SpatialLayer )layersIt.next();
			
			//Видимость слоя зависит от того, хочет ли его видеть клиент, виден ли он при текущем масштабе на сервере
			//и надо ли отображать объекты для текущего запроса
			boolean toShowLayerObjects =	spL.isVisible()
				&& spL.isVisibleAtScale(query.getTopoScale());

			//то же самое для надписей
			boolean toShowLayerLabels =	spL.isLabelVisible()
				&& spL.isVisibleAtScale(query.getTopoScale());
			
			result += "&" + ServletCommandNames.PAR_LAYER_VISIBLE + index + "="	+ (toShowLayerObjects ? 1 : 0);
			result += "&" + ServletCommandNames.PAR_LAYER_LABELS_VISIBLE + index + "=" + (toShowLayerLabels ? 1 : 0);

			index++;
		}

		return result;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapImageLoader#getMapConnection()
	 */
	public MapConnection getMapConnection() throws MapConnectionException
	{
		return this.connection;
	}

	/* (non-Javadoc)
	 * @see com.syrus.AMFICOM.client.map.MapImageLoader#findSpatialObjects(java.lang.String)
	 */
	public List findSpatialObjects(String searchText) throws MapConnectionException, MapDataException
	{
		List resultList = new ArrayList();

		try
		{
			String requestString = this.uriString + this.createSearchCommandString(searchText);			
			URI mapServerURI = new URI(requestString);
			URL mapServerURL = new URL(mapServerURI.toASCIIString());
			URLConnection urlConnection = mapServerURL.openConnection();

			System.out.println("MIServletImageLoader - searchText - Conection opened");

			if(urlConnection.getInputStream() == null)
				return resultList;
			
			ObjectInputStream ois = new ObjectInputStream(urlConnection.getInputStream());

			System.out.println("MIFLNL - searchText - ObjectInputStream exists");

			//reading possible error from server
			try
			{
				Object readObject = ois.readObject();
				if(readObject instanceof String)
				{
//						Environment.log(
//								Environment.LOG_LEVEL_FINER,
//								(String )readObject);
					return resultList;
				}
			}
			catch(IOException optExc)
			{
			}

			//reading names and centers
			try
			{
				for(;;)
				{
					double xCoord  = ois.readDouble();
					double yCoord  = ois.readDouble();				
					String featureName = (String)ois.readObject();
					
					resultList.add(new MapInfoSpatialObject(
							new DoublePoint(xCoord,yCoord),
							featureName));
				}
			}
			catch(EOFException eofExc)
			{
			}
			System.out.println("MIFLNL - searchText - Spatial objects read");

			ois.close();
			System.out.println("MIFLNL - searchText - Stream closed");
		}

		catch(Exception exc)
		{
			exc.printStackTrace();
		}

		return resultList;
	}
	
	private String createSearchCommandString(String nameToSearch)
	{
		String result = "";

		result += "?" + ServletCommandNames.COMMAND_NAME + "="
			+ ServletCommandNames.CN_SEARCH_NAME;
		
		result += "&" + ServletCommandNames.PAR_NAME_TO_SEARCH + "=" + nameToSearch;

		return result;
	}
}