package com.syrus.AMFICOM.Client.General.UI;

import com.syrus.AMFICOM.resource.AbstractImageResource;
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.Resource.*;

public class ImagesPanelLabel extends JLabel
		implements MouseListener, OperationListener
{
	public AbstractImageResource ir = null;
	Dispatcher disp = null;
	
	public ImagesPanelLabel()
	{
	}

	public ImagesPanelLabel(Dispatcher disp, ImageIcon myIcon, AbstractImageResource ir)
	{
		this.disp = disp;
		this.ir = ir;

		disp.register(this, "select");
		disp.register(this, "selectir");

//		this.setBorder(new EtchedBorder( EtchedBorder.LOWERED, Color.gray, Color.gray ) );
		this.setBorder(new EtchedBorder( EtchedBorder.LOWERED, Color.white, Color.white ) );
		this.setHorizontalAlignment(SwingConstants.CENTER);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
		this.setSize(myIcon.getIconWidth(), myIcon.getIconHeight());
		this.setIcon(myIcon);
		this.addMouseListener(this);
		this.setEnabled(true);
	}

	public void mouseClicked(MouseEvent e) { }
	public void mouseExited(MouseEvent e) { }
    public void mouseReleased(MouseEvent e) { }
    public void mouseEntered(MouseEvent e) { }

    public void mousePressed(MouseEvent e)
	{
		if(disp != null)
			disp.notify(new OperationEvent(this, 0, "select"));
	}
	
	public void operationPerformed(OperationEvent oe)
	{
		if(oe.getActionCommand().equals("select"))
		{
			if(oe.getSource().equals(this))
			{
				this.setBackground(Color.blue);
//			    this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.gray, Color.red ));
			    this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.blue, Color.blue ));
			}
			else
			{
				this.setBackground(Color.white);
//			    this.setBorder(new EtchedBorder( EtchedBorder.LOWERED, Color.gray, Color.gray ) );
			    this.setBorder(new EtchedBorder( EtchedBorder.LOWERED, Color.white, Color.white ) );
			}
		}
		else
		if(oe.getActionCommand().equals("selectir"))
			if(oe.getSource().equals(this.ir))
			{
				this.setBackground(Color.blue);
//			    this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.gray, Color.red ));
			    this.setBorder(new EtchedBorder(EtchedBorder.LOWERED, Color.blue, Color.blue ));
			}
			else
			{
				this.setBackground(Color.white);
//			    this.setBorder(new EtchedBorder( EtchedBorder.LOWERED, Color.gray, Color.gray ) );
			    this.setBorder(new EtchedBorder( EtchedBorder.LOWERED, Color.white, Color.white ) );
			}
	}

	public String getToolTipText()
	{
		return ir.getId().toString();
	}

}
