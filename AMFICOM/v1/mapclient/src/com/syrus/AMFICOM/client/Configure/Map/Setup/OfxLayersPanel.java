/*
 * Название: $Id: OfxLayersPanel.java,v 1.1.1.1 2004/05/27 11:33:29 bass Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ
 *
 * Платформа: java 1.4.1
*/

package com.syrus.AMFICOM.Client.Configure.Map.Setup;
import com.ofx.geocoding.*;
import com.ofx.mapViewer.*;
import com.ofx.component.*;
import com.ofx.base.*;
import com.ofx.component.swing.JMapLegend;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.util.*;

import oracle.jdeveloper.layout.*;

import com.syrus.AMFICOM.Client.Configure.Map.*;
import com.syrus.AMFICOM.Client.Configure.Map.Setup.LayerConfig;

/**
 * Класс $Name:  $ используется для 
 * 
 * 
 * 
 * @version $Revision: 1.1.1.1 $, $Date: 2004/05/27 11:33:29 $
 * @author $Author: bass $
 * @see
 */
public class OfxLayersPanel extends JPanel
{
	GridBagLayout gridBagLayout1 = new GridBagLayout();

	private LayerConfig jMapLegend = new LayerConfig();
	private MapMainFrame mmf;
	
	/**
	 * По умолчанию
	 */
	public OfxLayersPanel()
	{
		try
		{
			jbInit();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Метод jbInit
	 * 
	 * 
	 * @exception Exception
	 */
	private void jbInit() throws Exception
	{
		this.setLayout(gridBagLayout1);
		this.setSize(new Dimension(370, 629));
		this.add(jMapLegend, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
//		this.add(selectButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
//		this.add(Box.createVerticalGlue(), new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.WEST, GridBagConstraints.VERTICAL, new Insets(5, 5, 5, 5), 0, 0));
	}

	public void setMapMainFrame(MapMainFrame mmf)
	{
		this.mmf = mmf;
		try 
		{
			jMapLegend.setMapViewer(mmf.myMapViewer.getMapViewer());
		} 
		catch (Exception ex) 
		{
		} 
	}
	
	public MapMainFrame getMapMainFrame() 
	{
		return mmf;
	}
}