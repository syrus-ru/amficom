package com.syrus.AMFICOM.Client.Optimize.UI;

import java.util.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

// ������������ �������� ��� ������ �����������  ���������� ���� �������
// ��������� ������� JVitCellRenderer � ������, ������ , ��� ������ �����
//================================================================================================================
public class SolutionRenderer //extends DefaultTableCellRenderer
extends ObjectResourceTableRenderer
{
  Vector unilinks = new Vector();// ������ ������, ����� ������� ���� ����� � ������
  int optimize_mode = 0; // ����� �����������. ��� ��������� ������������ ��� ���������� ���������� ���� ��������������
	//--------------------------------------------------------------------------------------------------------------
	public SolutionRenderer()
  { super();
  }
  //--------------------------------------------------------------------------------------------------------------
  // ��������� ��� ����� ������ ��������� (��� �� ����������� ��������� � �������� ������ ������ ����������� ��
  // ������������� ��������� � ������ ������ �� ����������� �����)
  public void setUnilinks(Vector uls)
  { unilinks = uls;
  }
  //--------------------------------------------------------------------------------------------------------------
  public void setOptimizeMode(int mode)
  {  optimize_mode = mode;
  }
  //--------------------------------------------------------------------------------------------------------------
  private static String colorToHTMLString(Color color)
  { String returnValue = "#";
    String hexString;
    hexString = Integer.toHexString(color.getRed());
    if (hexString.length() == 1) { hexString = '0' + hexString;}
    returnValue += hexString;
    hexString = Integer.toHexString(color.getGreen());
    if (hexString.length() == 1){  hexString = '0' + hexString;}
    returnValue += hexString;
    hexString = Integer.toHexString(color.getBlue());
    if (hexString.length() == 1){  hexString = '0' + hexString;}
    returnValue += hexString;
    return returnValue;
  }
  //--------------------------------------------------------------------------------------------------------------
	// �� ����� path - ���� ���� ������������
	public Component getTableCellRendererComponent(	JTable table,	Object value,	boolean isSelected,	boolean hasFocus,
                                                  int row, int column )
	{ Color selectedForeground = Color.WHITE;
    Color selectedBackground = Color.BLUE;
    Color foreground = Color.BLACK;
    Color background = Color.WHITE;

    Component result = null;
		ObjectResource or = (ObjectResource)value;
		if( !(or instanceof SchemePath) )
		{ System.err.println("SolutionRenderer.getTableCellRendererComponent(...): Non SchemePath-object has been sent to the table.");
	return new JVitCellRenderer("���� ������������ �� ������");
		}

		SchemePath path = (SchemePath)or;
		String s = "";
    String id = "", name = "";
		s +=  "<html><body text = \"";
    s += colorToHTMLString(isSelected ? selectedForeground : foreground);
    s += "\" bgcolor = \"";
    s += colorToHTMLString(isSelected ? selectedBackground : background);
    s += "\"<font color = \"#007733\">"+ ((SchemeElement)Pool.get( SchemeElement.typ, path.start_device_id)).name
                                          + "</font> <font color = \"#000000\"> : </font>";
    PathElement pe;
    for( Enumeration elinks = path.links.elements(); ;)
    { pe = (PathElement) elinks.nextElement();
      id = pe.link_id;
      name = pe.getName();
      if(!elinks.hasMoreElements()) // � ����� ��������� �� ������ (+ ������ ������, ���� ����� ���������� ������������)
    break;
      if( unilinks.contains(id)){ s += "("+name+")" + "->";}
      else{ s += name + "->";}
    }
//		for( int i=0; i<path.links.size()-1; i++ )
//		{  id = ((PathElement)path.links.elementAt(i)).link_id;
//       name = ((PathElement)path.links.elementAt(i)).getName();
//       if( unilinks.contains(id)){ s += "("+name+")" + "->";}
//       else{ s += name + "->";}
//		}
//		id = ((PathElement)path.links.elementAt(path.links.size()-1)).link_id;
//    name = ((PathElement)path.links.elementAt(path.links.size()-1)).getName();
    // � ������ ���������� ������������ ��������� ���� ��������������
    if(optimize_mode == 1) { s += "<font color = \"#ff0000\">";}
    if(unilinks.contains(id)) { s += "("+name+")" + " <font color = \"#000000\">;  </font>";}
    else{ s+= name + " <font color = \"#000000\">;  </font>";}
    if(optimize_mode == 1){ s += "</font>";}

		// � ����� ������ ����������� ��������� ��������� �� ������ ����
		ElementAttribute attr = (ElementAttribute) path.attributes.get("roughly_estimated_path_loss");
		if(attr != null)
		{ s += "( ~" + String.valueOf( ((double)Math.round(Double.parseDouble(attr.value)*100))/100 )+ "�� )";// ����� � �������� ���������
		}
		else // ���� �� ����� ������ �� ���� ���������, ���� ���������� �� ��������� ������
		{ s += "���������� � ��������� �� ����� ����������";
		}
    s += "</body></html>";
    result = new JVitCellRenderer(s);// ���������� ������ �������, � ������� ������� ������� ��������� ��������� ����������

		if (!isSelected)
		{	result.setForeground(foreground);//(table.getForeground());
			result.setBackground(background);//(table.getBackground());
		}
		else //	if(isSelected)
		{	 result.setForeground(selectedForeground);//(table.getSelectionForeground());
			 result.setBackground(selectedBackground);//(table.getSelectionBackground());
		}
		if (hasFocus)
		{  setBorder( UIManager.getBorder("Table.focusCellHighlightBorder") );
		}
		else
		{  setBorder(noFocusBorder);
		}
		return result;
	}
	//------------------------------------------------------------------------------------------------------------
}
//==============================================================================================================

