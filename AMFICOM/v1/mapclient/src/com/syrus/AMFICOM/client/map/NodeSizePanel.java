package com.syrus.AMFICOM.Client.Map;

import javax.swing.*;
import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.Map.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import javax.swing.JButton;
import oracle.jdeveloper.layout.XYConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class NodeSizePanel extends JPanel 
{
	LogicalNetLayer lnl;
	private XYLayout xYLayout1 = new XYLayout();
	private JButton leftButton = new JButton();
	private JButton rightButton = new JButton();

	double coef = 1.2;

	public NodeSizePanel(LogicalNetLayer lnl)
	{
		this();
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

	private void jbInit() throws Exception
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
					leftButton_actionPerformed(e);
				}
			});
	    ImageIcon icon2 = new ImageIcon("images/closer.gif");
		rightButton.setIcon(icon2);
//		rightButton.setText(">>");
		rightButton.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					rightButton_actionPerformed(e);
				}
			});
			
	    leftButton.setToolTipText("Уменьшить значок");
	    rightButton.setToolTipText("Увеличить значок");
		
		this.add(leftButton, new XYConstraints(5, 2, 24, 24));
		this.add(rightButton, new XYConstraints(35, 2, 24, 24));
	}

	private void rightButton_actionPerformed(ActionEvent e)
	{
		lnl.getMapContext().defaultScale *= coef;
		lnl.getMapContext().updateZoom();

		lnl.postDirtyEvent();
		lnl.postPaintEvent();
	}

	private void leftButton_actionPerformed(ActionEvent e)
	{
		lnl.getMapContext().defaultScale /= coef;
		lnl.getMapContext().updateZoom();
		
		lnl.postDirtyEvent();
		lnl.postPaintEvent();
	}

	public void setEnabled(boolean bool)
	{
		leftButton.setEnabled(bool);
		rightButton.setEnabled(bool);
	}


}
