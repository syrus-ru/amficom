package com.syrus.AMFICOM.Client.Optimize.UI;

 //PropertyRenderer
import java.awt.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

//===================================================================================================================
// txtArea выравнивается по borderLayout.NORTH; панель, на которой она лежит, замечает изменение её высоты и меняет сама свою высоту
public class JVitCellRenderer extends JEditorPane implements TableCellRenderer
{
    protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
    private Color unselectedForeground;
    private Color unselectedBackground;
    BorderLayout borderLayout = new BorderLayout();
    String s = "";
    //-----------------------------------------------------------------
    public JVitCellRenderer()
    {	super();
      setOpaque(true);
      setBackground(Color.WHITE);
      setBorder(noFocusBorder);
      setContentType("text/html");
    }
    //-----------------------------------------------------------------
    public JVitCellRenderer(String s)
    { this();
      this.s = s;
      setMyText(this.s);
    }
    //-----------------------------------------------------------------
    public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus,
                                                     int row,	 int column )
    {	if (column==1) {setText("Подробный маршрут");}
      else {setText("???");}
      setOpaque(true);
      return this;
    }
    //-----------------------------------------------------------------
    // добавляет на панель JTextArea, в который помещает текст (автоперенос поддерживается автоматически)
    public void setMyText(String s)
    {	setText(s);
      setToolTipText("Подробный маршрут");
    }
    //-----------------------------------------------------------------
    protected void setValue(Object value)
    { setMyText((value == null) ? "" : value.toString());
    }
    //-----------------------------------------------------------------
    // надо переопределить, иначе выделение касается только части ячейки
    public void setBackground(Color bg)
    {	//super.setBackground(Color.WHITE);
      super.setBackground(bg);
    }
    //-----------------------------------------------------------------
    // надо переопределить, иначе выделение касается только части ячейки
    public void setForeground(Color bg)
    {	super.setForeground(bg);
    }
}
//================================================================================================================
// ***************************************************************************************************************

//package com.syrus.AMFICOM.Client.Optimize.UI;
//
// //PropertyRenderer
//import java.awt.*;
//
//import javax.swing.*;
//import javax.swing.border.*;
//import javax.swing.table.*;
//import com.syrus.AMFICOM.Client.General.Command.Optimize.*;
//
//===================================================================================================================
//  //  рендереер ячейки, который увеличивает высоту панели так, чтобы в него влезал весь текст при текущей ширине.
//  // txtArea выравнивается по borderLayout.NORTH; панель, на которой она лежит, замечает изменение её высоты и меняет сама свою высоту
//public class JVitCellRenderer extends JTextArea implements TableCellRenderer
//{
//		protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
//		private Color unselectedForeground;
//		private Color unselectedBackground;
//		BorderLayout borderLayout = new BorderLayout();
//		String s = "";
//		//-----------------------------------------------------------------
//		public JVitCellRenderer()
//		{	super();
//			setOpaque(true);
//			setBorder(noFocusBorder);
//		}
//		//-----------------------------------------------------------------
//		public JVitCellRenderer(String s)
//		{ this();
//			this.s = s;
//			setMyText(this.s);
//		}
//		//-----------------------------------------------------------------
//		public Component getTableCellRendererComponent ( JTable table, Object value, boolean isSelected, boolean hasFocus,
//																										 int row,	 int column )
//	 {	if (column==1) {setText("Подробный маршрут");}
//			else {setText("???");}
//			setOpaque(true);
//			return this;
//	 }
//	 //-----------------------------------------------------------------
//	 // добавляет на панель JTextArea, в который помещает текст (автоперенос поддерживается автоматически)
//	 public void setMyText(String s)
//	 {	setLineWrap(true); setWrapStyleWord(true); //автоперенос по словам
//			setText(s);
//			setToolTipText("Подробный маршрут");
//		}
//		//-----------------------------------------------------------------
//		protected void setValue(Object value)
//		{ setMyText((value == null) ? "" : value.toString());
//		}
//		//-----------------------------------------------------------------
//		// надо переопределить, иначе выделение касается только части ячейки
//		public void setBackground(Color bg)
//		{	super.setBackground(bg);
//		}
//		//-----------------------------------------------------------------
//		// надо переопределить, иначе выделение касается только части ячейки
//		public void setForeground(Color bg)
//		{	super.setForeground(bg);
//		}
//}
//================================================================================================================
