package com.syrus.AMFICOM.Client.Optimize;

import  com.syrus.AMFICOM.Client.General.UI.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import javax.swing.*;
import java.awt.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

// окно в котором вывод€тс€ параметры оптимизации, которые можно в этом окне мен€ть
//-------------------------------------------------------------------------------------------------------------
public class OpticalOptimizationParamsFrame extends JInternalFrame
																						implements OperationListener
{
	private OpticalOptimizerContext optoptContext;

	private EventTableModel paramsModel; // модель дл€ нужд задани€ параметров
	private JViewport viewport = new JViewport();

	private FixedSizeEditableTableModel paramsTableModel;
	private JPanel jPanel_params = new JPanel();
	private JTable jTable_params;
	private JScrollPane jScrollPane_params = new JScrollPane();
	private BorderLayout borderLayout1 = new BorderLayout();
	private Dispatcher dispatcher;
	private OptimizeMDIMain mdiMain;
	//-----------------------------------------------------------------------------------------------------------
	public OpticalOptimizationParamsFrame(Dispatcher dispatcher, OpticalOptimizerContext optoptContext, OptimizeMDIMain mdiMain)
	{ try
		{ this.dispatcher = dispatcher;
			this.optoptContext = optoptContext;
			this.mdiMain = mdiMain;

			jbInit();

			place();
		}
		catch(Exception e)
		{ e.printStackTrace();
		}
	}
	//-----------------------------------------------------------------------------------------------------------
	//автопозиционирование и авторазмер
	public void place()
	{		Dimension dim = mdiMain.scrollPane.getSize();
			int width = (int)(0.22*dim.width), height = (int)(0.18*dim.height);
			if(mdiMain.kisSelectFrame != null){ setBounds(dim.width - width, mdiMain.kisSelectFrame.getHeight(), width, height);}
      else { setBounds(dim.width - width, 0, width, height);}
			setVisible(true);
	}
	//-----------------------------------------------------------------------------------------------------------
	public void operationPerformed(OperationEvent ae){}
	//-----------------------------------------------------------------------------------------------------------
	private void jbInit() throws Exception
	{ this.setFrameIcon( new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/general.gif")) );
		setDefaultCloseOperation(HIDE_ON_CLOSE); // не закрываем, а пр€чем
		jPanel_params.setLayout(borderLayout1);
    paramsModel = new EventTableModel
                  (  mdiMain,
                     new String[] {"",""}, // название окна говорит само за себ€, подписи к столбцам излишни
                     new Object[] { new Double(-1), // значени€ по умолчанию (не используютс€)
                                    new Double(0.03)},
                     ((OpticalOptimizerContext)mdiMain.optimizerContext).param_descriptions, // описани€
                     new int[]{ 1 }  // номера редактируемых столбцов (начинаетс€ с 0)
                  );
    paramsModel.updateData( ((OpticalOptimizerContext)mdiMain.optimizerContext).param_values ); // значени€
    jTable_params = new JTable(paramsModel);
    //
		this.setMaximizable(true);
		this.setPreferredSize(new Dimension(120, 24));
		jScrollPane_params.getViewport().setBackground(Color.white);
    jTable_params.setBackground(Color.white);
    jPanel_params.setBackground(Color.white);
    viewport.setBackground(Color.white);
    this.getContentPane().add(jPanel_params, BorderLayout.CENTER);
		jPanel_params.add(jScrollPane_params,  BorderLayout.CENTER);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setTitle("ѕараметры  оптимизации");
    this.setBackground(Color.WHITE);
    //
		jTable_params.getColumnModel().getColumn(0).setPreferredWidth(250);
		jTable_params.setPreferredScrollableViewportSize(new Dimension(200, 213));
		jTable_params.setMinimumSize(new Dimension(200, 213));
		jTable_params.setSelectionMode(jTable_params.getSelectionModel().SINGLE_SELECTION);
    //
		jScrollPane_params.setViewport(viewport);
		jScrollPane_params.getViewport().add(jTable_params);
		jScrollPane_params.setAutoscrolls(true);
    //
		jTable_params.setAutoResizeMode(jTable_params.AUTO_RESIZE_ALL_COLUMNS);
		//jTable_params.getColumnModel().getColumn(0).setPreferredWidth(15);
		jTable_params.getColumnModel().getColumn(1).setPreferredWidth(70); //во втором столбце небольшие числа => его ширина не должна быть большой
		jTable_params.updateUI();
	}
	//-----------------------------------------------------------------------------------------------------------
}
//=============================================================================================================
class EventTableModel extends FixedSizeEditableTableModel
{
	OptimizeMDIMain mdiMain;
  //-----------------------------------------------------------------------------------------------------------
	public EventTableModel (  OptimizeMDIMain mdiMain, String[] p_columns,	Object[] p_defaultv, String[] p_rows,	int[] editable )
	{ super( p_columns, p_defaultv, p_rows, editable );
    this.mdiMain = mdiMain;
	}
	//-----------------------------------------------------------------------------------------------------------
	// изменение значеи€ €чейки
	public void setValueAt(Object value, int row, int col)
	{	super.setValueAt(value, row, col);
		// передаЄм новые параметры оптимизации в Dll
    mdiMain.optimizerContext.setOptimizationParam( row , ( (Double)value ).doubleValue() );
		//ep[current_ev].setThreshold(((Double)value).doubleValue(), col, row);
		//dispatcher.notify(new RefUpdateEvent(this, RefUpdateEvent.THRESHOLD_CHANGED_EVENT));
	}
}
//=============================================================================================================