package com.syrus.AMFICOM.Client.General.UI;

import java.awt.*;
import java.awt.SystemColor;


import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class JLabelRenderer extends JLabel
		implements PropertyRenderer
{
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1); 
    
    private Color unselectedForeground; 
    private Color unselectedBackground; 

    public JLabelRenderer() 
	{
		super();
		setOpaque(true);
        setBorder(noFocusBorder);
		setBackground(SystemColor.window);

		Font font = this.getFont();
		this.setFont(new Font(font.getName(), Font.PLAIN, font.getSize()));
   }

    public JLabelRenderer(String s)
	{
		this();
		setText(s);
	}

	public void setSelected(Object obj)
	{
		setText((String )obj);
	}
	
    protected void setValue(Object value) 
	{
		setText((value == null) ? "" : value.toString());
    }
	
}
