/**
 * $Id: MapMouseMotionListener.java,v 1.25 2005/07/15 14:59:11 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.logging.Level;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.map.MapPropertiesManager;
import com.syrus.AMFICOM.client.map.MapState;
import com.syrus.AMFICOM.client.map.NetMapViewer;
import com.syrus.AMFICOM.client.map.strategy.MapStrategy;
import com.syrus.AMFICOM.client.map.strategy.MapStrategyManager;
import com.syrus.AMFICOM.map.MapElement;
import com.syrus.util.Log;

/**
 * ���������� ����������� ���� � ���� �����. ��� ��������� ��������� ���������
 * ����������� �������� ���� operationMode. ���� �������� � ��������� ���,
 * �� ��������� ������� ���������� �������� ��������� �������� �����
 * (����������� ������� MapStrategy)
 * 
 * @version $Revision: 1.25 $, $Date: 2005/07/15 14:59:11 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapMouseMotionListener implements MouseMotionListener
{
	NetMapViewer netMapViewer;
	
	private final double navigateAreaSize = MapPropertiesManager.getNavigateAreaSize();

	public MapMouseMotionListener(NetMapViewer netMapViewer)
	{
		this.netMapViewer = netMapViewer;
	}

	public void mouseDragged(MouseEvent me)
	{
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();

//		System.out.println("Dragged to (" + me.getPoint().x + ", " + me.getPoint().y + ")");

		logicalNetLayer.setCurrentPoint(me.getPoint());
		MapState mapState = logicalNetLayer.getMapState();
		
		mapState.setMouseMode(MapState.MOUSE_DRAGGED);
		if ( logicalNetLayer.getMapView() != null)
		{
			logicalNetLayer.setEndPoint(me.getPoint());
			try {
				this.netMapViewer.showLatLong(me.getPoint());
			} catch(MapConnectionException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			} catch(MapDataException e3) {
				// TODO Auto-generated catch block
				e3.printStackTrace();
			}

			//������������ ������� �� ������ ������������
			switch (mapState.getOperationMode())
			{
				case MapState.MEASURE_DISTANCE:
					logicalNetLayer.sendMapEvent(MapEvent.NEED_REPAINT);
					break;
				case MapState.MOVE_HAND:
					try {
						//���� ���������� ����� ������
						this.netMapViewer.handDragged(me);
					} catch(MapConnectionException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					} catch(MapDataException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}
					break;
				case MapState.MOVE_TO_CENTER:
					break;
				case MapState.NAVIGATE:
					break;
				case MapState.ZOOM_TO_RECT:
					logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.NEED_REPAINT));
					break;
				case MapState.NODELINK_SIZE_EDIT:
					break;
				case MapState.MOVE_FIXDIST:
					// fall through
				case MapState.NO_OPERATION:
					try {
						MapElement mapElement = logicalNetLayer.getCurrentMapElement();
						MapStrategy strategy = MapStrategyManager.getStrategy(mapElement);
						if(strategy != null)
						{
							strategy.setNetMapViewer(this.netMapViewer);
							strategy.doContextChanges(me);
						}
//						logicalNetLayer.sendMapSelectedEvent(mapElement);
						logicalNetLayer.sendMapEvent(MapEvent.NEED_REPAINT);
					} catch(MapConnectionException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch(MapDataException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					break;
				default:
					try
					{
						System.out.println("unknown map operation: " + mapState.getOperationMode());
						throw new Exception("dummy");
					}
					catch(Exception e)
					{
						Log.debugMessage("MapMouseMotionListener::mouseDragged | current execution point with call stack:", Level.FINER);
						Log.debugException(e, Level.SEVERE);
					}
					break;
			}//switch (mapState.getOperationMode()
		}
		mapState.setMouseMode(MapState.MOUSE_NONE);
	}

	public void mouseMoved(MouseEvent me)
	{
		LogicalNetLayer logicalNetLayer = this.netMapViewer.getLogicalNetLayer();
		logicalNetLayer.setCurrentPoint(me.getPoint());
		
		MapState mapState = logicalNetLayer.getMapState();

		mapState.setMouseMode( MapState.MOUSE_MOVED);
		try {
			//������� �������� ������ � �������
			this.netMapViewer.showLatLong( me.getPoint());
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//������������ ������� �� ������ ������������
		if (mapState.getOperationMode() == MapState.MOVE_HAND)
		{		
			try {
				//���� ���������� ����� ������
				this.netMapViewer.handMoved(me);
			} catch(MapConnectionException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			} catch(MapDataException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
		if(mapState.getActionMode() == MapState.NULL_ACTION_MODE)
			if(MapPropertiesManager.isTopologicalImageCache()) {
				try {
					//���� ���������� ����� ������
					this.netMapViewer.mouseMoved(me);
				} catch(MapConnectionException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch(MapDataException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
			}
		
		if(mapState.getActionMode() == MapState.NULL_ACTION_MODE)
		if(MapPropertiesManager.isDescreteNavigation()) {
			Dimension imageSize = this.netMapViewer.getVisualComponent().getSize();
			int mouseX = me.getPoint().x;
			int mouseY = me.getPoint().y;

			int cursorX =
				(mouseX < imageSize.width * this.navigateAreaSize) 
				? 0
				: (mouseX < imageSize.width * (1 - this.navigateAreaSize)) 
				? 1
				:2;
			int cursorY =
				(mouseY < imageSize.height * this.navigateAreaSize) 
				? 0
				: (mouseY < imageSize.height * (1 - this.navigateAreaSize)) 
				? 1
				: 2;

			this.netMapViewer.setCursor(cursors[cursorX][cursorY]);
		}

		mapState.setMouseMode(MapState.MOUSE_NONE);
	}

	private static Cursor[][] cursors = new Cursor[3][3];

//	.getScaledInstance(
//			MapMouseMotionListener.IMG_SIZE,
//			MapMouseMotionListener.IMG_SIZE,
//			Image.SCALE_SMOOTH)
	private static final String NORTHWEST = "northwest";
	static {
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Point hotSpot = new Point(0, 0);
		cursors[0][0] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gonorthwest.gif"),
				hotSpot,
				NORTHWEST);
		cursors[0][1] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gowest.gif"),
				hotSpot,
				NORTHWEST);
		cursors[0][2] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gosouthwest.gif"),
				hotSpot,
				NORTHWEST);
		cursors[1][0] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gonorth.gif"),
				hotSpot,
				NORTHWEST);
		cursors[1][1] = Cursor.getDefaultCursor();
		cursors[1][2] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gosouth.gif"),
				hotSpot,
				NORTHWEST);
		cursors[2][0] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gonortheast.gif"),
				hotSpot,
				NORTHWEST);
		cursors[2][1] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/goeast.gif"),
				hotSpot,
				NORTHWEST);		
		cursors[2][2] = toolkit.createCustomCursor(
				toolkit.createImage("images/cursors/gosoutheast.gif"),
				hotSpot,
				NORTHWEST);
	}
}
