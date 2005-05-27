/**
 * $Id: MapElementsBarPanel.java,v 1.15 2005/05/27 15:14:59 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.Map.Controllers.NodeTypeController;
import com.syrus.AMFICOM.client.model.ApplicationContext;
import com.syrus.AMFICOM.map.SiteNodeType;

import java.awt.Dimension;
import java.awt.FlowLayout;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 *  �� ���� �������� ������������� �������� ������� ����� ���������� �� �����
 * 
 * 
 * 
 * @version $Revision: 1.15 $, $Date: 2005/05/27 15:14:59 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class MapElementsBarPanel extends JPanel 
{
	ApplicationContext aContext;

	public MapElementsBarPanel()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
		  e.printStackTrace();
		}
	}

	private void jbInit()
	{
		this.setLayout(new FlowLayout(FlowLayout.CENTER));
		this.setPreferredSize(new Dimension(MapElementLabel.ELEMENT_DIMENSION + 10, -1));
		this.setBorder(BorderFactory.createEtchedBorder());
	}

	public void setContext(ApplicationContext aContext)
	{
		this.aContext = aContext;
		setProtoElements();
	}
	
	public void setProtoElements()
	{
		MapElementLabel mel;
		SiteNodeType mpe;

		this.removeAll();
		
		List elements = getMapProtoElements();
		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			mpe = (SiteNodeType )it.next();
			mel = new MapElementLabel(mpe);
			mel.setToolTipText( mpe.getName());
			this.add(mel);
		}
	}

//�������� ��������� ������
	public void setEnableDisablePanel(boolean b)
	{
		for (int i = 0; i < this.getComponentCount(); i++)
			((MapElementLabel)this.getComponent(i)).setEnabled(b);
	}

	private List getMapProtoElements()
	{
		Collection types = NodeTypeController.getTopologicalNodeTypes();
		List returnTypes = new LinkedList(types);
		Collections.sort(returnTypes, MapTreeModel.siteNodeTypeComparator);
		return returnTypes;
	}
}

