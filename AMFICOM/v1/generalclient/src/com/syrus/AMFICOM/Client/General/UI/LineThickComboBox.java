package com.syrus.AMFICOM.Client.General.UI;

import java.awt.event.ItemEvent;

import javax.swing.JComboBox;

public class LineThickComboBox extends AComboBox
		implements PropertyRenderer, PropertyEditor
{
	int returnLineThickness;

	public LineThickComboBox()
	{
		super(AComboBox.SMALL_FONT);
		for (int i = 1; i < 7; i++)
		{
			this.addItem(Integer.toString(i));
		}

		this.addItemListener(new java.awt.event.ItemListener()
        {
			public void itemStateChanged(ItemEvent e)
            {
                LineThickComboBox_itemStateChanged(e);
            }
        } );
	}

	void LineThickComboBox_itemStateChanged(ItemEvent e)
	{
		returnLineThickness = Integer.parseInt( (String)this.getSelectedItem() );
	}

	public Object getSelected()
	{
		returnLineThickness = Integer.parseInt( (String)this.getSelectedItem() );
		return new Integer(returnLineThickness);
	}
	
	public void setSelected(Object obj)
	{
		int i = ((Integer )obj).intValue();
		if(i > 7)
			i = 7;
		if(i < 1)
			i = 1;
		this.setSelectedIndex(i - 1);
	}


}
