package com.syrus.AMFICOM.Client.Optimize.UI;

import java.awt.*;
import javax.swing.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

// комбобокс, в котором отображается
// атрибут оптимизации ( можно или нельзя располагать в этом узле КИС )
//===========================================================================================================
public class OptimizerNodeAttributeComboBox extends AComboBox implements PropertyRenderer, PropertyEditor
{ //-------------------------------------------------------------
	public OptimizerNodeAttributeComboBox(String s)
	{	super();
		jbInit();
		// в созданном комбобоксе выбираем указанную строчку
		this.setSelected(s);
	}
	//-------------------------------------------------------------
	public void jbInit()
	{	this.addItem("restricted");
		this.addItem("optional");
		this.addItem("obligatory");
		this.setRenderer(new OptimizerNodeAttributeComboBoxRenderer());
	}
	//-------------------------------------------------------------
	public void setSelected(Object object)
	{ //System.out.println("OptimizerNodeAttributeComboBox.setSelected() = "+ object.toString());
		super.setSelectedItem(object);
	}
	//-------------------------------------------------------------
	public Object getSelected()
	{ //System.out.println("OptimizerNodeAttributeComboBox.getSelected() = "+ super.getSelectedItem().toString());
		return super.getSelectedItem();
	}
}
//===========================================================================================================
class OptimizerNodeAttributeComboBoxRenderer extends DefaultListCellRenderer
{ public Component getListCellRendererComponent( JList list, Object value, int index,	 boolean isSelected, boolean cellHasFocus)
	{	String s = (String) value;
		if      (s.equals("restricted")){ setText("запрещено"); }
		else if(s.equals("obligatory"))	{ setText("желательно");}
		else if(s.equals("optional"))	  { setText("оптимально");}
		setBackground(isSelected ? Color.blue : Color.white);
		setForeground(isSelected ? Color.white : Color.black);

		return this;
	}
}
//===========================================================================================================
