/**
 * $Id: MapElementsBarPanel.java,v 1.1 2004/09/13 12:33:42 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Model.ApplicationContext;

import com.syrus.AMFICOM.Client.Resource.Map.MapNodeProtoElement;

import java.awt.FlowLayout;
import java.awt.Image;

import java.util.Iterator;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 *  �� ���� �������� ������������� �������� ������� ����� ���������� �� �����
 * 
 * 
 * 
 * @version $Revision: 1.1 $, $Date: 2004/09/13 12:33:42 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public class MapElementsBarPanel extends JPanel 
{
	final static int ELEMENT_DIMENSION = 30;

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
		setProtoElements();
	}

	private void jbInit()
	{
		this.setLayout(new FlowLayout(FlowLayout.LEFT));
	}

	public void setContext(ApplicationContext aContext)
	{
	}
	
	public void setProtoElements()
	{
		ImageIcon ii;
		MapElementLabel mel;
		MapNodeProtoElement mpe;

		this.removeAll();
		
		java.util.List elements = getMapProtoElements();
		
		for(Iterator it = elements.iterator(); it.hasNext();)
		{
			mpe = (MapNodeProtoElement )it.next();
			ii = new ImageIcon(mpe.getImage().getScaledInstance(
					ELEMENT_DIMENSION, 
					ELEMENT_DIMENSION, 
					Image.SCALE_SMOOTH));
			mel = new MapElementLabel(ii, mpe);
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

	private java.util.List getMapProtoElements()
	{
		MapFrame mf = MapFrame.getMapMainFrame();
		return mf.getMapViewer().getLogicalNetLayer().getTopologicalProtos();
	}
/*
	private java.util.List getMapProtoElements()
	{
		ArrayList panelElements = new ArrayList();
		List ls;
		ls = Pool.getList(MapNodeProtoElement.typ);
		if(ls != null)
			for(Iterator it = ls.iterator(); it.hasNext();)
			{
				try 
				{
					MapNodeProtoElement mpe = (MapNodeProtoElement )it.next();
					if(mpe.isTopological())
						panelElements.add(mpe);
				} 
				catch (Exception ex) 
				{
				} 
			}
		return panelElements;
	}
*/
}

