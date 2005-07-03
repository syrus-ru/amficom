/*
 * Название: $Id: ControlsFrame.java,v 1.9 2005/06/22 13:21:53 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
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
import com.syrus.AMFICOM.client.model.MapEditorApplicationModel;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.mapview.MapView;

/**
 * Окно операций на карте - содержит панель с 4 закладками
 * <li> Выбор вида
 * <li> Поиск элементов АМФИКОМ
 * <lI> Поиск географических объектов
 * <li> Управление отображением слоев
 * @version $Revision: 1.9 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
 public class ControlsFrame extends JInternalFrame 
		implements PropertyChangeListener {
	BorderLayout borderLayout1 = new BorderLayout();

	/**
	 * панель выбора вида
	 */
	MapChooserPanel mapChooserPanel;
	
	/**
	 * панель поиска географических объектов
	 */
	SpatialSearchPanel mapSearchPanel;
	
	/**
	 * панель поиска элементов АМФИКОМ
	 */
	AMFICOMSearchPanel searchPanel;
	
	/**
	 * панель управления отображением слоев
	 */
	LayersPanel layersPanel;
	
	/**
	 * панель закладок
	 */
	JTabbedPane tabbedPane = new JTabbedPane();

	/**
	 * контекст приложения - нужен для взаимодействия с другими окнами модуля
	 */	
	protected ApplicationContext aContext;
	
	/**
	 * По умолчанию
	 */
	public ControlsFrame(MapFrame mapFrame, ApplicationContext aContext) {
		setContext(aContext);
		this.mapChooserPanel = new MapChooserPanel(mapFrame);
		this.mapSearchPanel = new SpatialSearchPanel(mapFrame);
		this.searchPanel = new AMFICOMSearchPanel(mapFrame);
		this.layersPanel = new LayersPanel(mapFrame);
		jbInit();
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
