/*
 * ????????: $Id: ControlsFrame.java,v 1.5 2005/05/27 15:14:57 krupenn Exp $
 *
 * Syrus Systems
 * ??????-??????????? ?????
 * ??????: ???????
*/

package com.syrus.AMFICOM.Client.Map.Operations;

import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.MapEditorApplicationModel;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.mapview.MapView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;

/**
 * ???? ???????? ?? ????? - ???????? ?????? ? 4 ??????????
 * <li> ????? ????
 * <li> ????? ????????? ???????
 * <lI> ????? ?????????????? ????????
 * <li> ?????????? ???????????? ?????
 * @version $Revision: 1.5 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
 public class ControlsFrame extends JInternalFrame 
		implements PropertyChangeListener {
	BorderLayout borderLayout1 = new BorderLayout();

	/**
	 * ?????? ?????? ????
	 */
	MapChooserPanel mapChooserPanel = new MapChooserPanel();
	
	/**
	 * ?????? ?????? ?????????????? ????????
	 */
	SpatialSearchPanel mapSearchPanel = new SpatialSearchPanel();
	
	/**
	 * ?????? ?????? ????????? ???????
	 */
	AMFICOMSearchPanel searchPanel = new AMFICOMSearchPanel();
	
	/**
	 * ?????? ?????????? ???????????? ?????
	 */
	LayersPanel layersPanel = new LayersPanel();
	
	/**
	 * ?????? ????????
	 */
	JTabbedPane tabbedPane = new JTabbedPane();

	/**
	 * ???????? ?????????? - ????? ??? ?????????????? ? ??????? ?????? ??????
	 */	
	protected ApplicationContext aContext;
	
	/**
	 * ?? ?????????
	 */
	public ControlsFrame(MapFrame mapFrame, ApplicationContext aContext) {
		setContext(aContext);

		try {
			jbInit();
		} catch(Exception e) {
			e.printStackTrace();
		}

		setMapFrame(mapFrame);
	}

	public void setContext(ApplicationContext aContext) {
		Dispatcher disp;
		if(this.aContext != null) {
			disp = this.aContext.getDispatcher();
			if(disp != null) {
				disp.removePropertyChangeListener(MapEvent.MAP_FRAME_SHOWN, this);
				disp.removePropertyChangeListener(MapEvent.MAP_VIEW_SELECTED, this);
				disp.removePropertyChangeListener(MapEvent.MAP_VIEW_DESELECTED, this);
				disp.removePropertyChangeListener(MapEvent.MAP_VIEW_SCALE_CHANGED, this);
			}
		}
		this.aContext = aContext;
		if(aContext == null)
			return;
		disp = this.aContext.getDispatcher();
		if(disp != null) {
			disp.addPropertyChangeListener(MapEvent.MAP_FRAME_SHOWN, this);
			disp.addPropertyChangeListener(MapEvent.MAP_VIEW_SELECTED, this);
			disp.addPropertyChangeListener(MapEvent.MAP_VIEW_DESELECTED, this);
			disp.addPropertyChangeListener(MapEvent.MAP_VIEW_SCALE_CHANGED, this);
		}
	}

	public void propertyChange(PropertyChangeEvent pce) {
		if(pce.getPropertyName().equals(MapEvent.MAP_VIEW_SELECTED))
			this.searchPanel.setMapView((MapView )pce.getSource());
		if(pce.getPropertyName().equals(MapEvent.MAP_VIEW_DESELECTED))
			this.searchPanel.setMapView(null);
		if(pce.getPropertyName().equals(MapEvent.MAP_FRAME_SHOWN)) {
			try {
				setMapFrame((MapFrame )pce.getSource());
			} catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		if(pce.getPropertyName().equals(MapEvent.MAP_VIEW_SCALE_CHANGED))
			this.layersPanel.setVisibility();
	}

	public void setMapFrame(MapFrame mapFrame) {
		this.mapChooserPanel.setMapFrame(mapFrame);
		this.searchPanel.setMapFrame(mapFrame);
		this.mapSearchPanel.setMapFrame(mapFrame);
		this.layersPanel.setMapFrame(mapFrame);
	}

	private void jbInit() {
		setClosable(true);
		setResizable(true);
		setMaximizable(false);
		setIconifiable(false);

		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));

		this.setTitle(LangModelMap.getString(MapEditorApplicationModel.ITEM_VIEW_CONTROLS));
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
		
		this.tabbedPane.addTab(
				"", 
				new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/map_layers.gif")
					.getScaledInstance(16, 16, Image.SCALE_SMOOTH)),
					this.layersPanel);
		this.tabbedPane.addTab(
				"", 
				new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/map_prop.gif")
					.getScaledInstance(16, 16, Image.SCALE_SMOOTH)),
					this.mapChooserPanel);
		
		this.getContentPane().add(this.tabbedPane, BorderLayout.CENTER);
	}
}
