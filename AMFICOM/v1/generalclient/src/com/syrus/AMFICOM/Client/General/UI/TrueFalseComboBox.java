package com.syrus.AMFICOM.Client.General.UI;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ItemEvent;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JList;

public class TrueFalseComboBox extends AComboBox
		implements PropertyRenderer, PropertyEditor
{

	public class CurentState
	{
		protected boolean state;
		protected String stateText;

		public CurentState()
		{
			state = true;
		}

		public void setState(boolean st)
		{
			state = st;
		}

		public boolean getState()
		{
			return state ;
		}

		public String getStateText()
		{

			if ( getState() == true)
			{
				stateText = "True";
			}
			else
			{
				stateText = "False";
			}

			return stateText ;
		}
	}

	public CurentState returnValue = new CurentState();

	public TrueFalseComboBox()
	{
		super(AComboBox.SMALL_FONT);

        this.addItem("True");
        this.addItem("False");

		this.setBounds(new Rectangle(0, 0, 20, 25));
		this.setRenderer(new TFCBRenderer());

		this.addItemListener(new java.awt.event.ItemListener()
        {
            public void itemStateChanged(ItemEvent e)
            {
                TrueFalseComboBox_itemStateChanged(e);
            }
        });
	}

	void TrueFalseComboBox_itemStateChanged(ItemEvent e)
	{
		if ( this.getSelectedIndex() == 0)
		{
			returnValue.setState(true);
		}
		else
		{
			returnValue.setState(false);
		}
	}

	public CurentState getCurentState()
	{
		returnValue.setState(this.getSelectedIndex() == 0);
		return returnValue;
	}

	public void setState( CurentState state)
	{
		returnValue = state;
	}

	public Object getSelected()
	{
		return getCurentState();
	}

	public void setSelected(Object obj)
	{
        returnValue.setState(((CurentState )obj).getState());
	}
}

class TFCBRenderer extends DefaultListCellRenderer
{
	public TFCBRenderer()
	{
		super();
	}

	public Component getListCellRendererComponent(
			JList list,
			Object value,
			int index,
			boolean isSelected,
			boolean cellHasFocus)
	{
		String txt = "";
		if(value.equals("True"))
			txt = "Да";
		else
			txt = "Нет";
		return super.getListCellRendererComponent(
			list,
			txt,
			index,
			isSelected,
			cellHasFocus);
	}
}