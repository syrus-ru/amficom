package com.syrus.AMFICOM.Client.Optimize;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Optimize.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

// ������, �� ������� ��������� jComboBox �� �������
// ����� ��������� � ������� ��������� � ������ "��������" � "������� ���"
//===============================================================================================================
public class OptimizeElementPanel extends JPanel implements OperationListener
{ OptimizeMDIMain mdiMain;
  ObjectResourceTablePane tablePane;
  boolean mouseSelect = true;
  BorderLayout borderLayout = new BorderLayout();
  OptimizeEquipmentsDisplayModel dm;
  boolean ef = true;// event_flag - ���� ��� ���� ���������� ��������� ��������� �����
  //--------------------------------------------------------------------------------------------------------------
  public OptimizeElementPanel(OptimizeMDIMain mdiMain)
  {	try
    {	this.mdiMain = mdiMain;
      jbInit();
      this.mdiMain.getInternalDispatcher().register(this, SchemeNavigateEvent.type);
    }
    catch(Exception ex)
    {	ex.printStackTrace();
    }
  }
  //--------------------------------------------------------------------------------------------------------------
  void jbInit() throws Exception
  {	tablePane = new ObjectResourceTablePane();
    dm = new OptimizeEquipmentsDisplayModel(mdiMain);
    tablePane.initialize( dm ,	new DataSet() );

    setEnableDisable(false);//������������� � ����� false
    this.setLayout(borderLayout);

    JTable table = tablePane.getTable();
    table.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
    // ��������� ������� �������
    table.getSelectionModel().addListSelectionListener
    ( new ListSelectionListener()
      {	public void valueChanged(ListSelectionEvent e) // ��������� �������
        {		table_selectionChanged(e);
        }
      }
    );

    this.add(tablePane, BorderLayout.CENTER);
  }
  //--------------------------------------------------------------------------------------------------------------
  public void setMDIMain(OptimizeMDIMain mdiMDIMain)
  { this.mdiMain	= mdiMDIMain;
    dm.setMDIMain(mdiMDIMain);
    setEnableDisable(true);
    updateTable();
  }
  //-------------------------------------------------------------------------------------------------------------
  // ��, ��� ������� � �������, ���� ���������� � �� �����
  public void table_selectionChanged(ListSelectionEvent e)
  { ObjectResource obj_selected = (ObjectResource) tablePane.getSelectedObject();
    ObjectResource obj_deselected = (ObjectResource) tablePane.getObjectAt(e.getFirstIndex());

    if( obj_deselected.equals(obj_selected) )
    {	obj_deselected = (ObjectResource) tablePane.getObjectAt(e.getLastIndex());
    }
    if(obj_deselected != null)
    {	ef = false;// ���� ef=false �������� ��������� � ������� �� �������� ����� �����������
      // ������� ��������� � �������� (��������� �������� �����)
      mdiMain.getInternalDispatcher().notify(new SchemeNavigateEvent(new SchemeElement[]{(SchemeElement)obj_deselected},
                                                    SchemeNavigateEvent.SCHEME_ELEMENT_DESELECTED_EVENT) );
      // � ����� �� ����� ������ �������� ������, ���������� ��������� �����
      mdiMain.getInternalDispatcher().notify(new SchemeNavigateEvent(
          new SchemeElement[] { mdiMain.scheme.getTopLevelElement((SchemeElement)obj_deselected) },
          SchemeNavigateEvent.SCHEME_ELEMENT_DESELECTED_EVENT));
      ef = true;
    }
    if(obj_selected != null)
    {	ef = false;
      // �������� ������� ( ������� ����� ���� ������ )
      mdiMain.getInternalDispatcher().notify(new SchemeNavigateEvent(new SchemeElement[]{(SchemeElement)obj_selected},
                                                    SchemeNavigateEvent.SCHEME_ELEMENT_SELECTED_EVENT) );
      // � ����� ����� ������ �������� ������, ���������� ��������� ������� ( ����� ����� ������� ������ )
      mdiMain.getInternalDispatcher().notify( new SchemeNavigateEvent(
          new SchemeElement[]{mdiMain.scheme.getTopLevelElement((SchemeElement)obj_selected)},
          SchemeNavigateEvent.SCHEME_ELEMENT_SELECTED_EVENT) );
      ef = true;
    }
  }
  //--------------------------------------------------------------------------------------------------------------
  // �������� ������� ����� ������� �� �����
  public void updateTable()
  {	JTable table = tablePane.getTable();
    //������� �������
    mouseSelect = false;
    //����� ������� ��������� �������� � �������
    table.clearSelection();
    mouseSelect = true;
    DataSet dataSet = new DataSet();// �c�� ������ �� ���������, �� ��������� ������ �������
    if(mdiMain != null)
    {	if(mdiMain.scheme != null)
      {  //dataSet = new DataSet(mdiMain.scheme.getTopLevelElements());
         dataSet = new DataSet(mdiMain.scheme.getAllTopElements().iterator() ); //getAllTopElements() ���������� ��� �������-�������� �� ���� �������
      }
    }
    tablePane.setContents(dataSet);
    table.setRowHeight( getFont().getSize() + 8 );
  }
  //--------------------------------------------------------------------------------------------------------------
  public void setSelectedObjects()
  {try
   { JTable myTable = tablePane.getTable();
    //�������� �������
    mouseSelect = false;
    //����� ������� ��������� �������� � �������
    myTable.clearSelection();
    DataSet dataSet = tablePane.getContents();
    for(int i = 0; i < dataSet.size(); i++)
    {	SchemeElement se = (SchemeElement )dataSet.get(i);
      //if(se.isSelected())
      {	myTable.getSelectionModel().addSelectionInterval(i, i);
      }
    }
    mouseSelect = true;
   }
   catch(Exception e)
   { // ����� ��������� ����� ��������� Exception, �� ��� ���������
     e.printStackTrace();
   }
  }
  //-------------------------------------------------------------------------------------------------------------
  public void operationPerformed(OperationEvent ae)
  { // ������� "������ �������" - ���� ���������� ��������������� ������ � �������
    if(ae.getActionCommand().equals(SchemeNavigateEvent.type) && ef)
    {	SchemeNavigateEvent sne = (SchemeNavigateEvent)ae;
      if(sne.SCHEME_ELEMENT_SELECTED)
      {	 SchemeElement[] se = (SchemeElement[]) sne.getSource();
         for(int i=0; i<se.length; i++)
         { tablePane.setSelected(se[i]);
         }
      }
    }
  }
  //--------------------------------------------------------------------------------------------------------------
  public void setEnableDisable( boolean b)
  {	JTable myTable = tablePane.getTable();
    myTable.setEnabled( b);
    if ( b == false )
    {		tablePane.setContents(new DataSet());
    }
  }
  //--------------------------------------------------------------------------------------------------------------
}
