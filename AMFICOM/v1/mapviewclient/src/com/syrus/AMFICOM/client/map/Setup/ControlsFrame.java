/*
 * ��������: $Id: ControlsFrame.java,v 1.8 2005/02/10 11:48:39 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: �������
*/

package com.syrus.AMFICOM.Client.Map.Setup;

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
 * ���� �������� �� ����� - �������� ������ � 4 ����������
 * <li> ����� ����
 * <li> ����� ��������� �������
 * <lI> ����� �������������� ��������
 * <li> ���������� ������������ �����
 * @version $Revision: 1.8 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
 public class ControlsFrame extends JInternalFrame 
		implements OperationListener
{
	BorderLayout borderLayout1 = new BorderLayout();

	/**
	 * ������ ������ ����
	 */
	MapChooserPanel mapChooserPanel = new MapChooserPanel();
	
	/**
	 * ������ ������ �������������� ��������
	 */
	SpatialSearchPanel mapSearchPanel = new SpatialSearchPanel();
	
	/**
	 * ������ ������ ��������� �������
	 */
	AMFICOMSearchPanel searchPanel = new AMFICOMSearchPanel();
	
	/**
	 * ������ ���������� ������������ �����
	 */
	LayersPanel layersPanel = new LayersPanel();
	
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
	public ControlsFrame(MapFrame mmf, ApplicationContext aContext)
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
		setMapFrame(mmf);
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

//	public void setVisible(boolean isVisible)
//	{
//		super.setVisible(isVisible);
//		if(!isVisible)
//			return;
//		
//		//����� ���� ���������� �������, ���������, ���� �� ������� ���� �����,
//		//� ����������� � ����
//		MapFrame mmf = MapFrame.getMapMainFrame();
//		if(mmf != null)
//			if(mmf.isVisible())
//				if(mmf.getParent().equals(this.getParent()))
//					setMapFrame(mmf);
//	}

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
				new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/map_prop.gif")
					.getScaledInstance(16, 16, Image.SCALE_SMOOTH)),
					this.mapChooserPanel);
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
		
		this.getContentPane().add(this.tabbedPane, BorderLayout.CENTER);
	}
}
