package com.syrus.AMFICOM.Client.Optimize;

import java.awt.*;

import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModelOptimize;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;
import javax.swing.table.TableModel;

// это окно,  в котором отображаются некоторые свойства выбранного на карте элемента
// оборудования (в основном по поводу размещения в нём КИС)
//================================================================================================================
public class NodesOptimizePropertiesFrame extends JInternalFrame implements OperationListener
{	OptimizeMDIMain mdiMain;
	OptimizeElementPanel panel ;
	//-------------------------------------------------------------------------------------
	public NodesOptimizePropertiesFrame()
	{ try{} catch(Exception e){	e.printStackTrace(); }
	}
	//-------------------------------------------------------------------------------------
	public NodesOptimizePropertiesFrame(ApplicationContext aContext, OptimizeMDIMain mdiMain)
	{try
	 {this.mdiMain = mdiMain;
		panel = new OptimizeElementPanel(mdiMain);
		panel.setMDIMain(mdiMain);

		jbInit();
		place();
	 }
	 catch(Exception e)
	 { e.printStackTrace();
	 }
	}
	//-------------------------------------------------------------------------------------
	public void place()
  { // в kisSelectFrame,iterHistFrame,paramsFrame,solutionFrame вызывается эта ф-я. Следить, чтоб не было зацикленных вызывов!
		Dimension dim = new Dimension( mdiMain.scrollPane.getSize() );
		setBounds( (int)( dim.width*(1-0.22-0.22) ), (int)( dim.height*(1-0.2)),(int)(dim.width*0.22), (int)(dim.height*(0.2)));
		setVisible(true);
	}
	//-------------------------------------------------------------------------------------
	private void jbInit() throws Exception
	{	this.setFrameIcon( new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/general.gif")) );
		this.setTitle(LangModelOptimize.getString("frameNodesOptimizePropertiesTitle") );
		setDefaultCloseOperation(HIDE_ON_CLOSE);// не закрываем, а прячем
		this.setClosable(true);
		this.setIconifiable(false);
		this.setMaximizable(true);
		this.setResizable(true);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(panel, BorderLayout.CENTER);
	}
	//-------------------------------------------------------------------------------------
	public void setMapContext(OptimizeMDIMain mdiMain)
	{	panel.setMDIMain(mdiMain);
	}
	//-------------------------------------------------------------------------------------
	public void operationPerformed(OperationEvent ae)
	{	if(	ae.getActionCommand().equals("mapselectevent"))
		{	//setMapContext((MapContext )ae.getSource());
			//setMapContext((MapContext )ae.getSource());
		}
		if(	ae.getActionCommand().equals("mapchangeevent"))
		{	panel.updateTable();
		}
		if(	ae.getActionCommand().equals("mapselectionchangeevent"))
		{	//panel.setSelectedObjects();
		}
	}
	//-------------------------------------------------------------------------------------
  public TableModel getTableForReport()
  {
    return panel.getTableModel();
  }
}