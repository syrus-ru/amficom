/**
 * $Id: MapInfoServletImageLoader.java,v 1.8 2005/08/29 12:13:34 peskovsky Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
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

public class MapInfoServletImageLoader implements MapImageLoader {
	private String uriString = null;

	private byte[] imageBuffer = null;

	private final MapInfoConnection connection;

	public MapInfoServletImageLoader(final MapInfoConnection connection) {
		this.connection = connection;

		this.uriString = this.connection.getURL();

		final Dimension maximumImageSize = Toolkit.getDefaultToolkit().getScreenSize();
		final int dataSize = maximumImageSize.width * maximumImageSize.height * 2;

		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
				+ " TIC - loadingthread - setImage - allocating for image buffer " + dataSize + " bytes of memory");

		this.imageBuffer = new byte[dataSize];

		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
				+ " TIC - loadingthread - setImage - memory allocated");
	}

	/**
	 * �������� ������ �� ������ �� ��������� ����������.
	 */
	public void stopRendering() {
		final String requestString = new String(this.uriString + "?" + ServletCommandNames.COMMAND_NAME + "="
				+ ServletCommandNames.CN_CANCEL_RENDERING + "&" + ServletCommandNames.USER_ID + LoginManager.getSessionKey().hashCode());

		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
				+ " TIC - loadingthread - stopping rendering at server");

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
					e.printStackTrace();
				}
			}

			if (!connectionResult.equals(ServletCommandNames.STATUS_SUCCESS)) {
				System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
						+ " Server returned: " + connectionResult);

				return;
			}
			// if(connection.getInputStream() == null)
			// return;

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * �������� ������ �� ��������� ����������� �� �������
	 * 
	 * @throws MapConnectionException
	 */
	public Image renderMapImage(final TopologicalImageQuery query) throws MapConnectionException {
		System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
				+ " TIC - loadingthread - starting rendering at server");

		try {
			final String requestString = this.uriString + this.createRenderCommandString(query);
			final URI mapServerURI = new URI(requestString);
			final URL mapServerURL = new URL(mapServerURI.toASCIIString());
			final URLConnection urlConnection = mapServerURL.openConnection();

			if (urlConnection.getInputStream() == null) {
				return null;
			}

			final ObjectInputStream ois = new ObjectInputStream(urlConnection.getInputStream());

			System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
					+ " TIC - loadingthread - getServerMapImage - got data at ObjectInputStream");

			try {
				ois.readFully(this.imageBuffer);
			} catch (EOFException eofExc) {
				//Nothing
			}

			System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
					+ " TIC - loadingthread - getServerMapImage - Read array from stream");

			final String connectionResult = urlConnection.getHeaderField(ServletCommandNames.STATUS_FIELD_NAME);
			if (!connectionResult.equals(ServletCommandNames.STATUS_SUCCESS)) {
				System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
						+ " Server returned: " + connectionResult);

				return null;
			}

			ois.close();

			final Image imageReceived = Toolkit.getDefaultToolkit().createImage(this.imageBuffer);

			System.out.println(MapPropertiesManager.getLogDateFormat().format(new Date(System.currentTimeMillis()))
					+ " TIC - loadingthread - getServerMapImage - Image created");

			return imageReceived;

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * ������ �� �������� ������� ������ ���������� ��� HTTP ������� � �������
	 * ���������� ��� ���������� ����������� ������
	 * 
	 * @param query
	 *        ������
	 * @return ������ ���������� ��� HTTP ������� � ������� ����������
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

			// ��������� ���� ������� �� ����, ����� �� ��� ������ ������, ����� �� ��
			// ��� ������� �������� �� �������
			// � ���� �� ���������� ������� ��� �������� �������
			final boolean toShowLayerObjects = spL.isVisible() && spL.isVisibleAtScale(query.getTopoScale());

			// �� �� ����� ��� ��������
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
	public MapConnection getMapConnection() throws MapConnectionException {
		return this.connection;
	}

	public List<SpatialObject> findSpatialObjects(SpatialLayer layer, String searchText) throws MapConnectionException, MapDataException {
		// TODO ��������� ���������� �� �������
		final List<SpatialObject> searchResultsList = new ArrayList<SpatialObject>();
		return searchResultsList;
	}

	public List<SpatialObject> findSpatialObjects(SpatialLayer layer, Double bounds) throws MapConnectionException, MapDataException {
		// TODO ��������� ���������� �� �������
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