package com.syrus.AMFICOM.Client.Optimize.UI;

import java.awt.*;
import javax.swing.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

// комбобокс, в котором отображается атрибут оптимизации
//===========================================================================================================
public class OptimizerRibAttributeComboBox extends AComboBox implements PropertyRenderer, PropertyEditor
{ //-------------------------------------------------------------
    public OptimizerRibAttributeComboBox(String s)
    { super();
      jbInit();
      // в созданном комбобоксе выбираем указанную строчку
      this.setSelected(s);
    }
    //-------------------------------------------------------------
    public void jbInit()
    { this.addItem("active");
      this.addItem("passive");
      this.addItem("tested");
      this.setRenderer(new OptimizerAttributeComboBoxRenderer());
    }
    //-------------------------------------------------------------
    public void setSelected(Object object)
    { super.setSelectedItem(object);
    }
    //-------------------------------------------------------------
    public Object getSelected()
    { //System.out.println("OptimizerRibAttributeComboBox.getSelected() = "+ super.getSelectedItem().toString());
      return super.getSelectedItem();
    }
    //-------------------------------------------------------------
}
//========================================================================================================
class OptimizerAttributeComboBoxRenderer extends DefaultListCellRenderer
{ public Component getListCellRendererComponent( JList list, Object value, int index,	 boolean isSelected, boolean cellHasFocus)
  { String s = (String) value;
    if     (s.equals("active")) { setText("активно"); }
    else if(s.equals("passive")){ setText("пассивно");}
    else if(s.equals("tested")) { setText("исключено");}
    else		{ setText("не определено");}
    setBackground(isSelected ? Color.blue : Color.white);
    setForeground(isSelected ? Color.white : Color.black);

    return this;
  }
}
//===========================================================================================================
