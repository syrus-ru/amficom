/**
 * $Id: NodeSizePanel.java,v 1.10 2005/06/06 12:57:03 krupenn Exp $
 *
 * Syrus Systems
 * ������-����������� �����
 * ������: ������� ������������������ �������������������
 *         ���������������� �������� ���������� �����������
 *
 * ���������: java 1.4.1
 */

package com.syrus.AMFICOM.client.map.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import com.syrus.AMFICOM.client.event.MapEvent;
import com.syrus.AMFICOM.client.map.LogicalNetLayer;
import com.syrus.AMFICOM.client.resource.LangModelMap;

/**
 * ������, �� ������� ������������� ��� ������ ���������� � ����������
 * ������������ ��������������� ����������� ��������� �����
 * @version $Revision: 1.10 $, $Date: 2005/06/06 12:57:03 $
 * @author $Author: krupenn $
 * @module mapviewclient_v1
 */
public final class NodeSizePanel extends JPanel 
{
	private LogicalNetLayer logicalNetLayer;
	private JButton leftButton = new JButton();
	private JButton rightButton = new JButton();

	private static Dimension buttonSize = new Dimension(24, 24);

	private static final double COEF = 1.2;

	public NodeSizePanel(LogicalNetLayer lnl)
	{
		this();
		setLogicalNetLayer(lnl);
	}
	
	public void setLogicalNetLayer(LogicalNetLayer lnl)
	{
		this.logicalNetLayer = lnl;
	}

	public NodeSizePanel()
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

	private void jbInit()
	{
		this.setLayout(new BorderLayout());

	    ImageIcon icon1 = new ImageIcon("images/farther.gif");
		this.leftButton.setIcon(icon1);
		this.leftButton.setPreferredSize(buttonSize);
		this.leftButton.setMaximumSize(buttonSize);
		this.leftButton.setMinimumSize(buttonSize);
//		this.leftButton.setText("<<");
		this.leftButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					reduce();
				}
			});
	    ImageIcon icon2 = new ImageIcon("images/closer.gif");
		this.rightButton.setIcon(icon2);
		this.rightButton.setPreferredSize(buttonSize);
		this.rightButton.setMaximumSize(buttonSize);
		this.rightButton.setMinimumSize(buttonSize);
//		this.rightButton.setText(">>");
		this.rightButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					enlarge();
				}
			});
			
	    this.leftButton.setToolTipText(LangModelMap.getString("ReduceIcon"));
	    this.rightButton.setToolTipText(LangModelMap.getString("EnlargeIcon"));
		
		this.add(this.leftButton, BorderLayout.WEST);
		this.add(this.rightButton, BorderLayout.EAST);
	}

	void enlarge()
	{
		if(this.logicalNetLayer == null)
			return;
		this.logicalNetLayer.setDefaultScale(this.logicalNetLayer.getDefaultScale() * COEF);
		this.logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.NEED_REPAINT));
	}

	void reduce()
	{
		if(this.logicalNetLayer == null)
			return;
		this.logicalNetLayer.setDefaultScale(this.logicalNetLayer.getDefaultScale() / COEF);
		this.logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.NEED_REPAINT));
	}

	public void setEnabled(boolean bool)
	{
		this.leftButton.setEnabled(bool);
		this.rightButton.setEnabled(bool);
	}


}
