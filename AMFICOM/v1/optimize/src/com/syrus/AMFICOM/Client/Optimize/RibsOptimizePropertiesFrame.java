package com.syrus.AMFICOM.Client.Optimize;

import java.awt.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Resource.Map.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

// окно,  в котором отображаются некоторые свойства выбранного на схеме ребра
//================================================================================================================
public class RibsOptimizePropertiesFrame extends JInternalFrame implements OperationListener
{	OptimizeMDIMain mdiMain;
	OptimizeRibsPanel panel;
	//-------------------------------------------------------------------------------------
	public RibsOptimizePropertiesFrame(){ try{} catch(Exception e){	e.printStackTrace(); } }
	//-------------------------------------------------------------------------------------
	public RibsOptimizePropertiesFrame(ApplicationContext aContext, OptimizeMDIMain mdiMain)
	{try
	 { this.mdiMain = mdiMain;
		 panel = new OptimizeRibsPanel(mdiMain);
		 panel.setMDIMain(mdiMain);
		 jbInit();
		 place();
	 }
	 catch(Exception e) {e.printStackTrace();}
	}
	//-------------------------------------------------------------------------------------
	public void place() // автопозиционирование
	{	Dimension dim = new Dimension( mdiMain.scrollPane.getSize() );
		if(mdiMain.map_is_opened)//если карта открыта, то оставляем место под окно свойств элементов карты
    {  setBounds((int)(dim.width*(1-0.22)),(int)(dim.height*(1-0.2)),(int)(dim.width*0.22),(int)(dim.height*(0.2)));
    }
    else // иначе растягиваем окно, чтоб не оставвалось пустого места
    {  setBounds((int)( dim.width*(1-0.22)), (int)(dim.height*(1-0.2)),(int)(dim.width*0.22),(int)(dim.height*0.2));
    }
		setVisible(true);
	}
	//-------------------------------------------------------------------------------------
	private void jbInit() throws Exception
	{	this.setFrameIcon( new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/general.gif")) );
		this.setTitle("Режим волокон");
		setDefaultCloseOperation(HIDE_ON_CLOSE); //не закрываем, а прячем
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
	{	if(	ae.getActionCommand().equals("mapselectevent")){}
		if(	ae.getActionCommand().equals("mapchangeevent"))
		{	panel.updateTable();
		}
		//if(	ae.getActionCommand().equals("mapselectionchangeevent")){}
	}
	//-------------------------------------------------------------------------------------
}