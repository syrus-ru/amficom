/**
 * $Id: NodeSizePanel.java,v 1.2 2004/09/15 08:21:49 krupenn Exp $
 *
 * Syrus Systems
 * Научно-технический центр
 * Проект: АМФИКОМ Автоматизированный МногоФункциональный
 *         Интеллектуальный Комплекс Объектного Мониторинга
 *
 * Платформа: java 1.4.1
 */

package com.syrus.AMFICOM.Client.Map.UI;

import com.syrus.AMFICOM.Client.General.Lang.LangModelMap;
import com.syrus.AMFICOM.Client.Map.LogicalNetLayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

/**
 * Панель, на которой располагаются две кнопки увеличения и уменьшения
 * коэффициента масштабирования изображений элементов карты
 * 
 * 
 * 
 * @version $Revision: 1.2 $, $Date: 2004/09/15 08:21:49 $
 * @module
 * @author $Author: krupenn $
 * @see
 */
public final class NodeSizePanel extends JPanel 
{
	private LogicalNetLayer lnl;
	private XYLayout xYLayout1 = new XYLayout();
	private JButton leftButton = new JButton();
	private JButton rightButton = new JButton();

	private static final double COEF = 1.2;

	public NodeSizePanel(LogicalNetLayer lnl)
	{
		this();
		setLogicalNetLayer(lnl);
	}
	
	public void setLogicalNetLayer(LogicalNetLayer lnl)
	{
		this.lnl = lnl;
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
		this.setLayout(xYLayout1);
		xYLayout1.setWidth(60);
		xYLayout1.setHeight(27);

	    ImageIcon icon1 = new ImageIcon("images/farther.gif");
		leftButton.setIcon(icon1);
//		leftButton.setText("<<");
		leftButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					reduce();
				}
			});
	    ImageIcon icon2 = new ImageIcon("images/closer.gif");
		rightButton.setIcon(icon2);
//		rightButton.setText(">>");
		rightButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					enlarge();
				}
			});
			
	    leftButton.setToolTipText(LangModelMap.getString("ReduceIcon"));
	    rightButton.setToolTipText(LangModelMap.getString("EnlargeIcon"));
		
		this.add(leftButton, new XYConstraints(5, 2, 24, 24));
		this.add(rightButton, new XYConstraints(35, 2, 24, 24));
	}

	private void enlarge()
	{
		if(lnl == null)
			return;
		lnl.setDefaultScale(lnl.getDefaultScale() * COEF);
		lnl.repaint();
	}

	private void reduce()
	{
		if(lnl == null)
			return;
		lnl.setDefaultScale(lnl.getDefaultScale() / COEF);
		lnl.repaint();
	}

	public void setEnabled(boolean bool)
	{
		leftButton.setEnabled(bool);
		rightButton.setEnabled(bool);
	}


}
