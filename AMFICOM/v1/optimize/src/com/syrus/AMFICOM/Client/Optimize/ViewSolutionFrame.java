// ????? ????????? ???????????????? ????? (???????? ?? ???? ?????) ? ??????? ? ????????? ????
package com.syrus.AMFICOM.Client.Optimize;


import java.awt.*;
import java.util.*;
import java.awt.event.*;
import java.beans.*;//??? PropertyChangeListener

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelOptimize;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Optimize.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

// ?????, ? ??? ???????. ??? ????????? ?????? ??????, ???????? ?????? ????? ???, ????? ???? ????? ? ??? ?????? ?????????
//================================================================================================================
public class ViewSolutionFrame extends JInternalFrame implements ActionListener, PropertyChangeListener,
																			 OperationListener,
																			 ListSelectionListener, // ????? ??????????? ????? ?? ??????? ???????
																			 Printable
{ 	private BorderLayout borderLayout1 = new BorderLayout();
	private ObjectResourceTablePane tablePane = new ObjectResourceTablePane();

	private Dispatcher dispatcher;
	private OptimizeMDIMain mdiMain;
	private SolutionRenderer solutionRenderer =  new SolutionRenderer();
	private boolean has_been_updated = false;
	//-------------------------------------------------------------------------------------------------------------
	public TableModel getTableModelForReport()
	{ return this.tablePane.getTable().getModel();
	}
	//-------------------------------------------------------------------------------------------------------------
	//  ??? ?? ?????? ????????? - ??????? ?? ????? ????? OptimizeMDIMain, ?? ???? ???...
	public ViewSolutionFrame(OptimizeMDIMain mdiMain)
	{	super();
		try
		{  this.dispatcher = mdiMain.getInternalDispatcher();
			 this.mdiMain = mdiMain;
			 this.dispatcher.register(this, "scheme_updated_event");
			 this.dispatcher.register(this, SchemeNavigateEvent.type);

			 jbInit();
			 place();
		}
		catch (Exception e)
		{  e.printStackTrace();
		}
	}
	//-------------------------------------------------------------------------------------------------------------
	//???????????????????? ? ??????????
	public void place()
	{		Dimension dim = this.mdiMain.scrollPane.getSize();
			int width = (int)(dim.width*(1-0.22-0.22)), height = dim.height/5;
			setBounds( 0, dim.height-height, width, height );
			setVisible(true);
	}
	//-------------------------------------------------------------------------------------------------------------
	public int print(Graphics g, PageFormat pf, int pi) throws PrinterException
	{	if (pi > 0)
		{	return Printable.NO_SUCH_PAGE;
		}
		this.printAll(g);
		return Printable.PAGE_EXISTS;
	}
	//-------------------------------------------------------------------------------------------------------------
	private void jbInit() throws Exception
	{	this.setFrameIcon( new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/general.gif")) );
		this.setTitle(LangModelOptimize.getString("frameSolutionTitle") );
		setDefaultCloseOperation(HIDE_ON_CLOSE);// ?? ?????????, ? ??????
		this.getContentPane().setLayout(borderLayout1);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setResizable(true);
		this.getContentPane().add(tablePane, BorderLayout.CENTER);

		this.tablePane.initialize( new OptimizeSolveDisplayModel(), new DataSet());
		tablePane.setRenderer(this.solutionRenderer);
		this.getContentPane().add(this.tablePane, BorderLayout.CENTER);
		this.tablePane.getTable().getColumnModel().getColumn(0).addPropertyChangeListener(this); // ??????????? ????????? ?????? ?????? (? ?????? ? ?????? ?????? ??????? )
		this.tablePane.addListSelectionListener(this);// ??????????? ????? ?? ??????? ???????

    this.solutionRenderer.setUnilinks(this.mdiMain.optimizerContext.unilinks); // ?????? ??????, ????? ??????? ???? ????? ? ??????
    UpdateTable();
	}
	//-------------------------------------------------------------------------------------------------------------
	 // ??????? ? ??????? ?????????? ?? ???????
	void UpdateTable()
	{
		if(this.mdiMain.scheme == null)
		{ System.err.println("ViewSolutionFrame.UpdateTable(): scheme=null. Update aborting...");
	return;
		}
    this.solutionRenderer.setOptimizeMode( this.mdiMain.optimizerContext.optimize_mode ); // ???????? ????????????? ????? ???????????
    ArrayList dataList = new ArrayList(this.mdiMain.scheme.paths);
    if(dataList.size() != 0)
		{ this.tablePane.setContents(dataList);
    }
    else { ((ObjectResourceTableModel)this.tablePane.getTable().getModel()).clearTable(); }
    this.tablePane.updateUI();
    this.has_been_updated = true;
	}
	//-------------------------------------------------------------------------------------------------------------
	public void ClearTable()
	{ this.tablePane.setContents(new ArrayList());
	}
	//-------------------------------------------------------------------------------------------------------------
	// ??? ????????? stopevent ????????? ?????????? ??????? ??????? ??????? ?????
	public void operationPerformed(OperationEvent ae)
	{ // ??????? "????? ????????? ????? ????????"
		if(ae.getActionCommand().equals("scheme_updated_event"))
		{ this.UpdateTable();// ???????? ??????? ???????? ??????? ?? ????? (ismMapContext)
			setVisible(true);
		}
		// ??????? "?????? ????" - ???? ?????????? ??????????????? ?????? ? ???????
		if(ae.getActionCommand().equals(SchemeNavigateEvent.type))
		{	SchemeNavigateEvent sne = (SchemeNavigateEvent)ae;
			if(sne.SCHEME_PATH_SELECTED)
			{	 if(this.has_been_updated)//???? ??????? ??? ??? ???????? ? ???????, ?? ?????? ??????? ?????????? ????
         { SchemePath[] path = (SchemePath[] )sne.getSource();
           this.tablePane.setSelected(path[0]);
         }
			}
		}
	}
	//-------------------------------------------------------------------------------------------------------------
	public void actionPerformed(ActionEvent ae){}
	//-------------------------------------------------------------------------------------------------------------
	// handle any width changes in the table
	public void propertyChange(PropertyChangeEvent e)
	{ if ("width".equals(e.getPropertyName()))
		{	 // call subroutine to resize all rows to fit the contents of column 1
			 refreshTableRows();
		}
	}
	//-------------------------------------------------------------------------------------------------------------
	// update all the row heights to fit the cell contents of column 1
	private void refreshTableRows()
	{	// get the current width of column 1
		JTable table = this.tablePane.getTable();
		int col = 0;
		int width = table.getColumnModel().getColumn(col).getWidth();
		// total number of rows in the table
		int nRows = table.getRowCount();
		// now, loop through all the rows
		for (int n=0; n<nRows; n++)
		{	// get the custom cell renderer used to display the cell [this is an instance of the MyCellRenderer implemented above]
			Component comp = table.getCellRenderer(n, col).getTableCellRendererComponent(
                       table, table.getModel().getValueAt(n,col),false, false, n, col);
			// get current row height
			int height = table.getRowHeight(n);
			// set the cell component's dimensions to current table column width (the resized value) and the current row height
			comp.setSize(width, height);			// now, ask the component for its preferred height
			// this should give the row height required to display the entire cell contents
			// for the new column size
			double preferredHeight = comp.getPreferredSize().getHeight();//set the new row height based on the preferred height of the cell component
			table.setRowHeight(n, (int)preferredHeight);
		}
	}
	//-------------------------------------------------------------------------------------------------------------
	// ??, ??? ??????? ? ???????, ???? ?????????? ?? ?????
	public void valueChanged(ListSelectionEvent e)
	{if(e.getValueIsAdjusting()){return;}
	 try
	 {
		//System.out.println("List value " + e.getFirstIndex() + " to " + e.getLastIndex());
		ObjectResource obj_selected = (ObjectResource) this.tablePane.getSelectedObject();

		if( this.tablePane.getContents().size() == 0 )  // ???? ??????? ? ???????? ????? ?? ??????? ??? ????????? ?? ?????
		{ this.dispatcher.notify(new SchemeNavigateEvent(new SchemePath[0],
														 SchemeNavigateEvent. SCHEME_ALL_DESELECTED_EVENT) );
		}
		else
		{  ObjectResource obj_deselected = (ObjectResource) this.tablePane.getObjectAt(e.getFirstIndex());
			if( obj_deselected.equals(obj_selected) )
			{	obj_deselected = (ObjectResource) this.tablePane.getObjectAt(e.getLastIndex());
			}
			
			if(obj_deselected != null)
			{	this.dispatcher.notify(new SchemeNavigateEvent(new SchemePath[]{(SchemePath)obj_deselected},
																											SchemeNavigateEvent. SCHEME_PATH_DESELECTED_EVENT) );
			}
			if(obj_selected != null)
			{	this.dispatcher.notify(new SchemeNavigateEvent(new SchemePath[]{(SchemePath)obj_selected},
																											SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT) );
			}
		}
	 }
	 catch (Exception ex)
	 { ex.printStackTrace();
	 }
	}
	//-------------------------------------------------------------------------------------------------------------
}
//================================================================================================================
	class MyStubResource extends StubResource
	{	String name = "";
		public  MyStubResource(String name)
		{		super();
				this.name = name;
		}
		public String getName()
		{		return this.name;
		}
	}
//================================================================================================================
