/*
 * Название: $Id: ControlsFrame.java,v 1.1 2005/03/02 12:30:40 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
*/

package com.syrus.AMFICOM.Client.Map.Operations;

import com.syrus.AMFICOM.Client.General.Event.Dispatcher;
import com.syrus.AMFICOM.Client.General.Event.MapEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationEvent;
import com.syrus.AMFICOM.Client.General.Event.OperationListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;
import com.syrus.AMFICOM.Client.Map.UI.MapFrame;
import com.syrus.AMFICOM.mapview.MapView;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;

/**
 * Окно операций на карте - содержит панель с 4 закладками
 * <li> Выбор вида
 * <li> Поиск элементов АМФИКОМ
 * <lI> Поиск географических объектов
 * <li> Управление отображением слоев
 * @version $Revision: 1.1 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
 public class ControlsFrame extends JInternalFrame 
		implements OperationListener
{
	BorderLayout borderLayout1 = new BorderLayout();

	/**
	 * панель выбора вида
	 */
	MapChooserPanel mapChooserPanel = new MapChooserPanel();
	
	/**
	 * панель поиска географических объектов
	 */
	SpatialSearchPanel mapSearchPanel = new SpatialSearchPanel();
	
	/**
	 * панель поиска элементов АМФИКОМ
	 */
	AMFICOMSearchPanel searchPanel = new AMFICOMSearchPanel();
	
	/**
	 * панель управления отображением слоев
	 */
	LayersPanel layersPanel = new LayersPanel();
	
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
	public ControlsFrame(MapFrame mapFrame, ApplicationContext aContext)
	{
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		setContext(aContext);
		setMapFrame(mapFrame);
	}

	public void setContext(ApplicationContext aContext)
	{
		Dispatcher disp;
		if(this.aContext != null)
		{
			disp = this.aContext.getDispatcher();
			if(disp != null)
			{
				disp.unregister(this, MapEvent.MAP_FRAME_SHOWN);
				disp.unregister(this, MapEvent.MAP_VIEW_SELECTED);
				disp.unregister(this, MapEvent.MAP_VIEW_DESELECTED);
			}
		}
		this.aContext = aContext;
		if(aContext == null)
			return;
		disp = this.aContext.getDispatcher();
		if(disp != null)
		{
			disp.register(this, MapEvent.MAP_FRAME_SHOWN);
			disp.register(this, MapEvent.MAP_VIEW_SELECTED);
			disp.register(this, MapEvent.MAP_VIEW_DESELECTED);
		}
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_SELECTED))
			this.searchPanel.setMapView((MapView)ae.getSource());
		if(ae.getActionCommand().equals(MapEvent.MAP_VIEW_DESELECTED))
			this.searchPanel.setMapView(null);
		if(	ae.getActionCommand().equals(MapEvent.MAP_FRAME_SHOWN))
		{
			try 
			{
				setMapFrame((MapFrame)ae.getSource());
			} 
			catch (Exception ex) 
			{
				ex.printStackTrace();
			} 
		}
	}

	public void setMapFrame(MapFrame mapFrame)
	{
		this.mapChooserPanel.setMapFrame(mapFrame);
		this.searchPanel.setMapFrame(mapFrame);
		this.mapSearchPanel.setMapFrame(mapFrame);
		this.layersPanel.setMapFrame(mapFrame);
	}
	
	private void jbInit()
	{
		setClosable(true);
		setResizable(true);
		setMaximizable(false);
		setIconifiable(false);

		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));

		this.setTitle(LangModelMap.getString("menuViewSetup"));
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
