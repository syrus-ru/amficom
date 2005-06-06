/**
 * $Id: MapInfoServletMapImageLoader.java,v 1.1.2.2 2005/06/06 12:16:29 peskovsky Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.Client.Map.Mapinfo;

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
import java.util.Date;
import java.util.Iterator;

import javax.swing.ImageIcon;

import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;
import com.syrus.AMFICOM.Client.Map.MapConnectionException;
import com.syrus.AMFICOM.Client.Map.MapDataException;
import com.syrus.AMFICOM.Client.Map.MapImageLoader;
import com.syrus.AMFICOM.Client.Map.MapPropertiesManager;
import com.syrus.AMFICOM.Client.Map.ServletCommandNames;
import com.syrus.AMFICOM.Client.Map.SpatialLayer;
import com.syrus.AMFICOM.Client.Map.TopologicalRequest;
import com.syrus.AMFICOM.general.LoginManager;

public class MapInfoServletMapImageLoader implements MapImageLoader
{
	private LogicalNetLayer logicalNetLayer = null;
	private String uriString = null;
	
	private byte[] imageBuffer = null;
	
	public MapInfoServletMapImageLoader(
			LogicalNetLayer miLayer)
	{
		this.logicalNetLayer = miLayer;
		this.uriString = this.logicalNetLayer.getMapViewer().getConnection().getURL();
		
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
	public void stopRenderingAtServer()
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
			URLConnection connection = mapServerURL.openConnection();

            String connectionResult = null;
            while (connectionResult == null)
            {
                connectionResult = connection.getHeaderField(ServletCommandNames.STATUS_FIELD_NAME);
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
	public ImageIcon renderMapImageAtServer(TopologicalRequest request) throws MapConnectionException
	{
		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis())) +
			" TIC - loadingthread - starting rendering at server");

		try
		{
			String requestString = this.uriString + this.createRenderCommandString(request);			
			URI mapServerURI = new URI(requestString);
			URL mapServerURL = new URL(mapServerURI.toASCIIString());
			URLConnection connection = mapServerURL.openConnection();

            if(connection.getInputStream() == null)
                return null;

			ObjectInputStream ois = new ObjectInputStream(connection.getInputStream());

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

			String connectionResult = connection.getHeaderField(ServletCommandNames.STATUS_FIELD_NAME);
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
			
			return new ImageIcon(imageReceived);

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
	 * @param request Запрос
	 * @return Строка параметров для HTTP запроса к серверу
	 * топографии 
	 * @throws MapConnectionException 
	 */
	private String createRenderCommandString(TopologicalRequest request)
		throws MapDataException, MapConnectionException
	{
		String result = "";
		result += "?" + ServletCommandNames.USER_ID + "="
		    + LoginManager.getSessionKey().hashCode();
		
		result += "&" + ServletCommandNames.COMMAND_NAME + "="
		+ ServletCommandNames.CN_START_RENDER_IMAGE;
		
		Dimension size = this.logicalNetLayer.getMapViewer().getVisualComponent().getSize();
		
		result += "&" + ServletCommandNames.PAR_WIDTH + "="	+ size.width;
		result += "&" + ServletCommandNames.PAR_HEIGHT + "=" + size.height;
		result += "&" + ServletCommandNames.PAR_CENTER_X + "=" + request.getTopoCenter().getX();
		result += "&" + ServletCommandNames.PAR_CENTER_Y + "=" + request.getTopoCenter().getY();
		result += "&" + ServletCommandNames.PAR_ZOOM_FACTOR + "=" + request.getTopoScale();

		int index = 0;
		
		Iterator layersIt = this.logicalNetLayer.getMapViewer().getLayers()
				.iterator();
		for(; layersIt.hasNext();)
		{
			SpatialLayer spL = (SpatialLayer )layersIt.next();
			
			//Видимость слоя зависит от того, хочет ли его видеть клиент, виден ли он при текущем масштабе на сервере
			//и надо ли отображать объекты для текущего запроса
			boolean toShowLayerObjects =	spL.isVisible()
				&& spL.isVisibleAtScale(this.logicalNetLayer.getScale());

			//то же самое для надписей
			boolean toShowLayerLabels =	spL.isLabelVisible()
				&& spL.isVisibleAtScale(this.logicalNetLayer.getScale());
			
			result += "&" + ServletCommandNames.PAR_LAYER_VISIBLE + index + "="	+ (toShowLayerObjects ? 1 : 0);
			result += "&" + ServletCommandNames.PAR_LAYER_LABELS_VISIBLE + index + "=" + (toShowLayerLabels ? 1 : 0);

			index++;
		}

		return result;
	}
}