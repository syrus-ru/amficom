//  ласс просмотра оптимизированных путей (подробно по всем узлам) в таблице в текстовом виде
package com.syrus.AMFICOM.Client.Optimize;

import java.awt.*;
import java.awt.event.*;
import java.beans.*;//дл€ PropertyChangeListener

import javax.swing.*;
import javax.swing.event.*;
import java.util.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Optimize.UI.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;

import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

// ‘рейм, в нЄм таблица. ѕри изменении ширины фрэйма, мен€етс€ высота €чеек так, чтобы весь текст в них влезал полностью
//================================================================================================================
public class ViewSolutionFrame extends JInternalFrame implements ActionListener, PropertyChangeListener,
																			 OperationListener,
																			 ListSelectionListener, // чтобы отлавливать клики по строкам таблицы
																			 Printable
{ private BorderLayout borderLayout1 = new BorderLayout();
	ObjectResourceTablePane tablePane = new ObjectResourceTablePane();

	private Dispatcher dispatcher;
	private OptimizeMDIMain mdiMain;
	private SolutionRenderer solutionRenderer =  new SolutionRenderer();
  private boolean has_been_updated = false;
	//-------------------------------------------------------------------------------------------------------------
	//  это не совсем правильно - таскать за собой везде OptimizeMDIMain, но пока так...
	public ViewSolutionFrame(OptimizeMDIMain mdiMain)
	{	super();
		try
		{  this.dispatcher = mdiMain.getInternalDispatcher();
			 this.mdiMain = mdiMain;
			 dispatcher.register(this, "scheme_updated_event");
			 dispatcher.register(this, SchemeNavigateEvent.type);

			 jbInit();
			 place();
		}
		catch (Exception e)
		{  e.printStackTrace();
		}
	}
	//-------------------------------------------------------------------------------------------------------------
	//автопозиционирование и авторазмер
	public void place()
	{		Dimension dim = mdiMain.scrollPane.getSize();
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
		this.setTitle("ƒетализаци€ решени€");
		setDefaultCloseOperation(HIDE_ON_CLOSE);// не закрываем, а пр€чем
		this.getContentPane().setLayout(borderLayout1);
		this.setClosable(true);
		this.setMaximizable(true);
		this.setResizable(true);
		this.getContentPane().add(tablePane, BorderLayout.CENTER);

		tablePane.initialize( new OptimizeSolveDisplayModel(), new DataSet());
		tablePane.setRenderer(solutionRenderer);
		this.getContentPane().add(tablePane, BorderLayout.CENTER);
		tablePane.getTable().getColumnModel().getColumn(0).addPropertyChangeListener(this); // отлавливаем изменени€ ширины фрэйма (а значит и ширины €чейки таблицы )
		tablePane.addListSelectionListener(this);// отлавливаем клики по строкам таблицы

    solutionRenderer.setUnilinks(mdiMain.optimizerContext.unilinks); // список линков, имена которых надо брать в скобки
    UpdateTable();
	}
	//-------------------------------------------------------------------------------------------------------------
	 // вывести в таблицу переданное ей решение
	void UpdateTable()
	{
		if(mdiMain.scheme == null)
		{ System.err.println("ViewSolutionFrame.UpdateTable(): scheme=null. Update aborting...");
	return;
		}
    solutionRenderer.setOptimizeMode( mdiMain.optimizerContext.optimize_mode ); // передать установленный режим оптимизации
    DataSet dataSet = new DataSet(mdiMain.scheme.paths);
    if(dataSet.size() != 0)
		{ tablePane.setContents(dataSet);
    }
    else { ((ObjectResourceTableModel)tablePane.getTable().getModel()).clearTable(); }
    tablePane.updateUI();
    has_been_updated = true;
	}
	//-------------------------------------------------------------------------------------------------------------
	public void ClearTable()
	{ tablePane.setContents(new DataSet());
	}
	//-------------------------------------------------------------------------------------------------------------
	// при получении stopevent обновл€ем содержимое таблицы текущим списком путей
	public void operationPerformed(OperationEvent ae)
	{ // событие "схема обновлена новым решением"
		if(ae.getActionCommand().equals("scheme_updated_event"))
		{ this.UpdateTable();// обновить таблицу текущими данными из карты (ismMapContext)
			setVisible(true);
		}
		// событие "выбран путь" - надо подсветить соответствующую строку в таблице
		if(ae.getActionCommand().equals(SchemeNavigateEvent.type))
		{	SchemeNavigateEvent sne = (SchemeNavigateEvent)ae;
			if(sne.SCHEME_PATH_SELECTED)
			{	 if(has_been_updated)//если маршрут уже был передана в таблицу, то просим таблицу подсветить путь
         { SchemePath[] path = (SchemePath[] )sne.getSource();
           tablePane.setSelected(path[0]);
         }
			}
		}
	}
	//-------------------------------------------------------------------------------------------------------------
	public void actionPerformed(ActionEvent ae){};
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
		JTable table = tablePane.getTable();
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
	// то, что выбрали в таблице, надо отобразить на карте
	public void valueChanged(ListSelectionEvent e)
	{if(e.getValueIsAdjusting()){return;}
	 try
	 {
		//System.out.println("List value " + e.getFirstIndex() + " to " + e.getLastIndex());
		ObjectResource obj_selected = (ObjectResource) tablePane.getSelectedObject();

		ObjectResource obj_deselected = (ObjectResource) tablePane.getObjectAt(e.getFirstIndex());
		if( obj_deselected.equals(obj_selected) )
		{	obj_deselected = (ObjectResource) tablePane.getObjectAt(e.getLastIndex());
		}

		if(obj_deselected != null)
		{	dispatcher.notify(new SchemeNavigateEvent(new SchemePath[]{(SchemePath)obj_deselected},
																										SchemeNavigateEvent.SCHEME_PATH_DESELECTED_EVENT) );
		}
		if(obj_selected != null)
		{	dispatcher.notify(new SchemeNavigateEvent(new SchemePath[]{(SchemePath)obj_selected},
																										SchemeNavigateEvent.SCHEME_PATH_SELECTED_EVENT) );
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
		{		return name;
		}
	}
//================================================================================================================
