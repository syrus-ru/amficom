package com.syrus.AMFICOM.Client.Optimize;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Optimize.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

// ������ ����� ���������� ������ �� ������� ��������� jComboBox1 �� �������
// ����� ���������(����) � ������� ��������� � ������ "�������������", "��������" � "����������"
//===============================================================================================================
public class OptimizeRibsPanel extends JPanel implements OperationListener
{	OptimizeMDIMain mdiMain;
	ObjectResourceTablePane tablePane;
	boolean mouseSelect = true;
	BorderLayout borderLayout = new BorderLayout();

	OptimizeRibsDisplayModel dm;
	//--------------------------------------------------------------------------------------------------------------
	public OptimizeRibsPanel(OptimizeMDIMain mdiMain)
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
		dm = new OptimizeRibsDisplayModel(mdiMain);
		tablePane.initialize( dm ,	new DataSet() );

		setEnableDisable(false);
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
	{
		ObjectResource obj_selected = (ObjectResource) tablePane.getSelectedObject();
		ObjectResource obj_deselected = (ObjectResource) tablePane.getObjectAt(e.getFirstIndex());

		if( obj_deselected.equals(obj_selected) )
		{	obj_deselected = (ObjectResource) tablePane.getObjectAt(e.getLastIndex());
		}
		if(obj_deselected != null && obj_deselected instanceof SchemeCableLink)
		{	mdiMain.getInternalDispatcher().notify(new SchemeNavigateEvent(new SchemeCableLink[]{(SchemeCableLink)obj_deselected},
																										SchemeNavigateEvent.SCHEME_CABLE_LINK_DESELECTED_EVENT) );
		}
    if(obj_deselected != null && obj_deselected instanceof SchemeLink)
    {	mdiMain.getInternalDispatcher().notify(new SchemeNavigateEvent(new SchemeLink[]{(SchemeLink)obj_deselected},
                                                    SchemeNavigateEvent.SCHEME_LINK_DESELECTED_EVENT) );
    }

		if(obj_selected != null && obj_selected instanceof SchemeCableLink)
		{	mdiMain.getInternalDispatcher().notify(new SchemeNavigateEvent(new SchemeCableLink[]{(SchemeCableLink)obj_selected},
																										SchemeNavigateEvent.SCHEME_CABLE_LINK_SELECTED_EVENT) );
		}
    if(obj_selected != null && obj_selected instanceof SchemeLink)
    {	mdiMain.getInternalDispatcher().notify(new SchemeNavigateEvent(new SchemeLink[]{(SchemeLink)obj_selected},
                                                    SchemeNavigateEvent.SCHEME_LINK_SELECTED_EVENT) );
    }

	}
	//--------------------------------------------------------------------------------------------------------------
	// �������� ������� ����� ������� �� �����
	public void updateTable()
	{	JTable table = tablePane.getTable();
		//������� �������
		mouseSelect = false;
		//����� ������� ��������� �������� � �����
		table.clearSelection();
		mouseSelect = true;
		DataSet dataSet = new DataSet();// �c�� ������ �� ���������, �� ��������� ������ �������
		if(mdiMain != null)
		{	if(mdiMain.scheme != null)
			{ Vector sum = new Vector(100,100);
        for(Enumeration cls = mdiMain.scheme.getAllCableLinks(); cls.hasMoreElements();)
        { sum.add(cls.nextElement());}
        for(Enumeration ls = mdiMain.scheme.getAllLinks(); ls.hasMoreElements();)
        { sum.add(ls.nextElement());}
        dataSet = new DataSet(sum.elements());
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
	 { //����� ��������� ����� ��������� Exception �� ��� ���������
		 e.printStackTrace();
	 }
	}
	//-------------------------------------------------------------------------------------------------------------
	// ��� ��������� event ��������� ���������� ������� ������� �����
	public void operationPerformed(OperationEvent ae)
	{	// ������� "������ �������" - ���� ���������� ��������������� ������ � �������
		if(ae.getActionCommand().equals(SchemeNavigateEvent.type))
		{	SchemeNavigateEvent sne = (SchemeNavigateEvent)ae;
			if(sne.SCHEME_CABLE_LINK_SELECTED)
			{	 SchemeCableLink[] se = (SchemeCableLink[]) sne.getSource();
				 for(int i=0; i<se.length; i++)
				 { tablePane.setSelected(se[i]);
				 }
			}
      else if(sne.SCHEME_LINK_SELECTED)
      {	 SchemeLink[] se = (SchemeLink[]) sne.getSource();
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
