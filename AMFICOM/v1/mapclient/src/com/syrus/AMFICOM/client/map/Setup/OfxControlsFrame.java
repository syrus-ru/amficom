/*
 * Название: $Id: OfxControlsFrame.java,v 1.2 2004/06/22 09:54:32 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Map.Setup;
import javax.swing.*;
import java.awt.*;
import oracle.jdeveloper.layout.VerticalFlowLayout;
import java.awt.Dimension;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.ofx.geocoding.*;
import com.ofx.mapViewer.*;
import java.awt.BorderLayout;
import java.awt.Insets;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Lang.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.Map.*;
import javax.swing.JTabbedPane;

/**
 * Класс $Name:  $ используется для 
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/06/22 09:54:32 $
 * @author $Author: krupenn $
 * @see
 */
 public class OfxControlsFrame extends JInternalFrame 
		implements OperationListener
{
	BorderLayout borderLayout1 = new BorderLayout();

	OfxMapChooserPanel mcp = new OfxMapChooserPanel();
	OfxSearchPanel sp = new OfxSearchPanel();
	AMFICOMSearchPanel asp = new AMFICOMSearchPanel();
	OfxLayersPanel olp = new OfxLayersPanel();
	
	private MapMainFrame mmf;
	JTabbedPane tabbedPane = new JTabbedPane();
	
	protected ApplicationContext aContext;
	
	/**
	 * По умолчанию
	 */
	public OfxControlsFrame(MapMainFrame mmf, ApplicationContext aContext)
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
		setMapMainFrame(mmf);
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		if(aContext == null)
			return;
		Dispatcher disp = aContext.getDispatcher();
		if(disp == null)
			return;
		disp.register(this, "mapselectevent");
		disp.register(this, "mapdeselectevent");
	}

	public void operationPerformed(OperationEvent ae)
	{
		if(ae.getActionCommand().equals("mapdeselectevent"))
			setMapMainFrame(null);
		if(	ae.getActionCommand().equals("mapselectevent"))
		{
			try 
			{
				MapContext mc = (MapContext )ae.getSource();
				setMapMainFrame(mc.getLogicalNetLayer().mapMainFrame);
			} 
			catch (Exception ex) 
			{
			} 
		}
	}

	public void setMapMainFrame(MapMainFrame mmf)
	{
		this.mmf = mmf;
		mcp.setMapMainFrame(mmf);
		asp.setMapMainFrame(mmf);
		sp.setMapMainFrame(mmf);
		olp.setMapMainFrame(mmf);
	}
	
	private void jbInit() throws Exception
	{
		setClosable(true);
		setResizable(true);
		setMaximizable(false);
		setIconifiable(false);

		this.setFrameIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/general.gif")));

		this.setTitle(LangModelConfig.Text("menuViewSetup"));
		this.getContentPane().setLayout(borderLayout1);
		this.setSize(new Dimension(370, 629));
		
		tabbedPane.addTab(
				"", 
				new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/map_prop.gif")
					.getScaledInstance(16, 16, Image.SCALE_SMOOTH)),
				mcp);
		tabbedPane.addTab(
				"", 
				new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/search.gif")
					.getScaledInstance(16, 16, Image.SCALE_SMOOTH)),
				asp);
		tabbedPane.addTab(
				"", 
				new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/map_search.gif")
					.getScaledInstance(16, 16, Image.SCALE_SMOOTH)),
				sp);
		tabbedPane.addTab(
				"", 
				new ImageIcon(Toolkit.getDefaultToolkit().createImage("images/map_layers.gif")
					.getScaledInstance(16, 16, Image.SCALE_SMOOTH)),
				olp);
		
		this.getContentPane().add(tabbedPane, BorderLayout.CENTER);
	}
}