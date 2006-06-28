/**
 * $Id: MapInfoServletImageLoader.java,v 1.9 2006/06/22 11:47:14 stas Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 */
package com.syrus.AMFICOM.client.map.mapinfo;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.Rectangle2D.Double;
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
import java.util.logging.Level;

import com.syrus.AMFICOM.client.map.MapConnection;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapImageLoader;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.ServletCommandNames;
import com.syrus.AMFICOM.client.map.SpatialLayer;
import com.syrus.AMFICOM.client.map.SpatialObject;
import com.syrus.AMFICOM.general.LoginManager;
import com.syrus.AMFICOM.map.TopologicalImageQuery;
import com.syrus.util.Log;

public class MapInfoServletImageLoader implements MapImageLoader {
	private String uriString = null;

	private byte[] imageBuffer = null;

	private final MapInfoConnection connection;

	public MapInfoServletImageLoader(final MapInfoConnection connection) {
		this.connection = connection;

		this.uriString = this.connection.getURL();

		final Dimension maximumImageSize = Toolkit.getDefaultToolkit().getScreenSize();
		final int dataSize = maximumImageSize.width * maximumImageSize.height * 2;

		Log.debugMessage(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
				+ " TIC - loadingthread - setImage - allocating for image buffer " + dataSize + " bytes of memory", Log.DEBUGLEVEL09);

		this.imageBuffer = new byte[dataSize];

		Log.debugMessage(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
				+ " TIC - loadingthread - setImage - memory allocated", Log.DEBUGLEVEL09);
	}

	/**
	 * Посылает запрос на сервер на остановку рендеринга.
	 */
	public void stopRendering() throws MapConnectionException {
		final String requestString = new String(this.uriString + "?" + ServletCommandNames.COMMAND_NAME + "="
				+ ServletCommandNames.CN_CANCEL_RENDERING + "&" + ServletCommandNames.USER_ID + LoginManager.getSessionKey().hashCode());

		Log.debugMessage(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
				+ " TIC - loadingthread - stopping rendering at server", Log.DEBUGLEVEL09);

		try {
			final URI mapServerURI = new URI(requestString);
			final URL mapServerURL = new URL(mapServerURI.toASCIIString());
			final URLConnection urlConnection = mapServerURL.openConnection();

			String connectionResult = null;
			while (connectionResult == null) {
				connectionResult = urlConnection.getHeaderField(ServletCommandNames.STATUS_FIELD_NAME);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Log.errorMessage(e);
				}
			}

			if (!connectionResult.equals(ServletCommandNames.STATUS_SUCCESS)) {
				Log.debugMessage(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
						+ " Server returned: " + connectionResult, Level.FINER);
			}
			// if(connection.getInputStream() == null)
			// return;

		} catch (MalformedURLException e) {
			throw new MapConnectionException(e);
		} catch (URISyntaxException e) {
			throw new MapConnectionException(e);
		} catch (IOException e) {
			throw new MapConnectionException(e);
		}
	}

	/**
	 * Посылает запрос на рендеринг изображения на сервере
	 * 
	 * @throws MapConnectionException
	 */
	public Image renderMapImage(final TopologicalImageQuery query) throws MapConnectionException {
		Log.debugMessage(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
				+ " TIC - loadingthread - starting rendering at server", Log.DEBUGLEVEL09);

		try {
			final String requestString = this.uriString + this.createRenderCommandString(query);
			final URI mapServerURI = new URI(requestString);
			final URL mapServerURL = new URL(mapServerURI.toASCIIString());
			final URLConnection urlConnection = mapServerURL.openConnection();

			if (urlConnection.getInputStream() == null) {
				return null;
			}

			final ObjectInputStream ois = new ObjectInputStream(urlConnection.getInputStream());

			Log.debugMessage(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
					+ " TIC - loadingthread - getServerMapImage - got data at ObjectInputStream", Log.DEBUGLEVEL09);

			try {
				ois.readFully(this.imageBuffer);
			} catch (EOFException eofExc) {
				//Nothing
			}

			Log.debugMessage(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
					+ " TIC - loadingthread - getServerMapImage - Read array from stream", Log.DEBUGLEVEL09);

			final String connectionResult = urlConnection.getHeaderField(ServletCommandNames.STATUS_FIELD_NAME);
			if (!connectionResult.equals(ServletCommandNames.STATUS_SUCCESS)) {
				Log.debugMessage(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
						+ " Server returned: " + connectionResult, Log.DEBUGLEVEL09);

				return null;
			}

			ois.close();

			final Image imageReceived = Toolkit.getDefaultToolkit().createImage(this.imageBuffer);

			Log.debugMessage(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
					+ " TIC - loadingthread - getServerMapImage - Image created", Log.DEBUGLEVEL09);

			return imageReceived;

		} catch (MalformedURLException e) {
			throw new MapConnectionException(e);
		} catch (URISyntaxException e) {
			throw new MapConnectionException(e);
		} catch (IOException e) {
			throw new MapConnectionException(e);
		} catch (MapDataException e) {
			throw new MapConnectionException(e);
		}
	}

	/**
	 * Создаёт по текущему запросу строку параметров для HTTP запроса к серверу
	 * топографии для рендеринга графических данных
	 * 
	 * @param query
	 *        Запрос
	 * @return Строка параметров для HTTP запроса к серверу топографии
	 */
	private String createRenderCommandString(final TopologicalImageQuery query) throws MapDataException {
		String result = "";
		result += "?" + ServletCommandNames.USER_ID + "=" + LoginManager.getSessionKey().hashCode();

		result += "&" + ServletCommandNames.COMMAND_NAME + "=" + ServletCommandNames.CN_START_RENDER_IMAGE;

		result += "&" + ServletCommandNames.PAR_WIDTH + "=" + query.getMapImageWidth();
		result += "&" + ServletCommandNames.PAR_HEIGHT + "=" + query.getMapImageHeight();
		result += "&" + ServletCommandNames.PAR_CENTER_X + "=" + query.getTopoCenterX();
		result += "&" + ServletCommandNames.PAR_CENTER_Y + "=" + query.getTopoCenterY();
		result += "&" + ServletCommandNames.PAR_ZOOM_FACTOR + "=" + query.getTopoScale();

		int index = 0;

		for (final Iterator layersIt = this.connection.getLayers().iterator(); layersIt.hasNext();) {
			final SpatialLayer spL = (SpatialLayer) layersIt.next();

			// Видимость слоя зависит от того, хочет ли его видеть клиент, виден ли он
			// при текущем масштабе на сервере
			// и надо ли отображать объекты для текущего запроса
			final boolean toShowLayerObjects = spL.isVisible() && spL.isVisibleAtScale(query.getTopoScale());

			// то же самое для надписей
			final boolean toShowLayerLabels = spL.isLabelVisible() && spL.isVisibleAtScale(query.getTopoScale());

			result += "&" + ServletCommandNames.PAR_LAYER_VISIBLE + index + "=" + (toShowLayerObjects ? 1 : 0);
			result += "&" + ServletCommandNames.PAR_LAYER_LABELS_VISIBLE + index + "=" + (toShowLayerLabels ? 1 : 0);

			index++;
		}

		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.syrus.AMFICOM.client.map.MapImageLoader#getMapConnection()
	 */
	public MapConnection getMapConnection() {
		return this.connection;
	}

	public List<SpatialObject> findSpatialObjects(SpatialLayer layer, String searchText) {
		// TODO Требуется реализация на сервере
		final List<SpatialObject> searchResultsList = new ArrayList<SpatialObject>();
		return searchResultsList;
	}

	public List<SpatialObject> findSpatialObjects(SpatialLayer layer, Double bounds) {
		// TODO Требуется реализация на сервере
		final List<SpatialObject> searchResultsList = new ArrayList<SpatialObject>();
		return searchResultsList;
	}
	
	private String createSearchCommandString(final String nameToSearch) {
		String result = "";
		result += "?" + ServletCommandNames.COMMAND_NAME + "=" + ServletCommandNames.CN_SEARCH_NAME;
		result += "&" + ServletCommandNames.PAR_NAME_TO_SEARCH + "=" + nameToSearch;
		return result;
	}
}