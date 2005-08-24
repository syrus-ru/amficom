/*
 * ��������: $Id: ControlsFrame.java,v 1.13 2005/08/24 08:19:58 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
*/

package com.syrus.AMFICOM.client.map.operations;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.ui.MapFrame;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * ���� �������� �� ����� - �������� ������ � 4 ����������
 * <li> ����� ����
 * <li> ����� ��������� �������
 * <lI> ����� �������������� ��������
 * <li> ���������� ������������ �����
 * @version $Revision: 1.13 $
 * @author $Author: krupenn $
 * @module mapviewclient
 */
 public class ControlsFrame extends JInternalFrame 
		implements PropertyChangeListener {
	public static final String	NAME = "controlsFrame";

	BorderLayout borderLayout1 = new BorderLayout();

	/**
	 * ������ ������ �������������� ��������
	 */
	SpatialSearchPanel mapSearchPanel;
	
	/**
	 * ������ ������ ��������� �������
	 */
	AMFICOMSearchPanel searchPanel;
	
	/**
	 * ������ ��������
	 */
	JTabbedPane tabbedPane = new JTabbedPane();

	/**
	 * �������� ���������� - ����� ��� �������������� � ������� ������ ������
	 */	
	protected ApplicationContext aContext;
	
	/**
	 * �� ���������
	 */
	public ControlsFrame(MapFrame mapFrame, ApplicationContext aContext) {
		setContext(aContext);
		this.mapSearchPanel = new SpatialSearchPanel(mapFrame);
		this.searchPanel = new AMFICOMSearchPanel(mapFrame);
		jbInit();
	}

	public void setContext(ApplicationContext aContext) {
		Dispatcher disp;
		if(this.aContext != null) {
			disp = this.aContext.getDispatcher();
			if(disp != null) {
				disp.removePropertyChangeListener(MapEvent.MAP_EVENT_TYPE, this);
			}
		}
		this.aContext = aContext;
		if(aContext == null)
			return;
		disp = this.aContext.getDispatcher();
		if(disp != null) {
			disp.addPropertyChangeListener(MapEvent.MAP_EVENT_TYPE, this);
		}
	}

	public void propertyChange(PropertyChangeEvent pce) {
		if(pce.getPropertyName().equals(MapEvent.MAP_EVENT_TYPE)) {
			MapEvent mapEvent = (MapEvent )pce;
			String mapEventType = mapEvent.getMapEventType();

			if(mapEventType.equals(MapEvent.MAP_VIEW_SELECTED))
				this.searchPanel.setMapView((MapView )pce.getNewValue());
			if(mapEventType.equals(MapEvent.MAP_VIEW_DESELECTED))
				this.searchPanel.setMapView(null);
			if(mapEventType.equals(MapEvent.MAP_FRAME_SHOWN)) {
				try {
					setMapFrame((MapFrame )pce.getNewValue());
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public void setMapFrame(MapFrame mapFrame) {
		this.searchPanel.setMapFrame(mapFrame);
		this.mapSearchPanel.setMapFrame(mapFrame);
	}

	private void jbInit() {
		setClosable(true);
		setResizable(true);
		setMaximizable(false);
		setIconifiable(false);

		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));

		this.setTitle("yf [th? yf [th!");
		this.getContentPane().setLayout(this.borderLayout1);
		this.setSize(new Dimension(370, 629));
		
		this.tabbedPane.addTab(
				"", 
				new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/search.gif")
					.getScaledInstance(16, 16, Image.SCALE_SMOOTH)),
					this.searchPanel);
		this.tabbedPane.addTab(
				"", 
				new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/map_search.gif")
					.getScaledInstance(16, 16, Image.SCALE_SMOOTH)),
					this.mapSearchPanel);
		
		this.getContentPane().add(this.tabbedPane, BorderLayout.CENTER);
	}
}
