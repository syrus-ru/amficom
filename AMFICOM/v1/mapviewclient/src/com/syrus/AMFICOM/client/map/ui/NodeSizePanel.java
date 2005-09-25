/**
 * $Id: NodeSizePanel.java,v 1.15 2005/09/25 16:08:03 krupenn Exp $
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
import com.syrus.AMFICOM.client.map.MapConnectionException;
import com.syrus.AMFICOM.client.map.MapDataException;
import com.syrus.AMFICOM.client.resource.LangModelMap;
import com.syrus.AMFICOM.client.resource.MapEditorResourceKeys;

/**
 * ������, �� ������� ������������� ��� ������ ���������� � ����������
 * ������������ ��������������� ����������� ��������� �����
 * @version $Revision: 1.15 $, $Date: 2005/09/25 16:08:03 $
 * @author $Author: krupenn $
 * @module mapviewclient
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

	    ImageIcon icon1 = new ImageIcon("images/farther.gif"); //$NON-NLS-1$
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
	    ImageIcon icon2 = new ImageIcon("images/closer.gif"); //$NON-NLS-1$
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
			
	    this.leftButton.setToolTipText(LangModelMap.getString(MapEditorResourceKeys.TOOLTIP_REDUCE_ICON));
	    this.rightButton.setToolTipText(LangModelMap.getString(MapEditorResourceKeys.TOOLTIP_ENLARGE_ICON));
		
		this.add(this.leftButton, BorderLayout.WEST);
		this.add(this.rightButton, BorderLayout.EAST);
	}

	void enlarge()
	{
		if(this.logicalNetLayer == null)
			return;
		try {
			this.logicalNetLayer.setDefaultScale(this.logicalNetLayer.getDefaultScale() * COEF);
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.NEED_REPAINT));
	}

	void reduce()
	{
		if(this.logicalNetLayer == null)
			return;
		try {
			this.logicalNetLayer.setDefaultScale(this.logicalNetLayer.getDefaultScale() / COEF);
		} catch(MapDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(MapConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.logicalNetLayer.getContext().getDispatcher().firePropertyChange(new MapEvent(this, MapEvent.NEED_REPAINT));
	}

	@Override
	public void setEnabled(boolean bool)
	{
		this.leftButton.setEnabled(bool);
		this.rightButton.setEnabled(bool);
	}


}
