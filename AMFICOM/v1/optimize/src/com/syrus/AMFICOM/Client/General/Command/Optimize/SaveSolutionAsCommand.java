package com.syrus.AMFICOM.Client.General.Command.Optimize;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Optimize.*;
import com.syrus.AMFICOM.Client.Schematics.Elements.*;

// команда "сохранить решение отдельан от схемы под своим именем" . Сохраняется только список путей. Схема не сохраняется.
public class SaveSolutionAsCommand extends VoidCommand
{ OptimizeMDIMain    mdiMain;
  ApplicationContext aContext;
  Dispatcher dispatcher;
  //--------------------------------------------------------------------------
  public SaveSolutionAsCommand(Dispatcher dispatcher, ApplicationContext aContext, OptimizeMDIMain mdiMain)
  { this.mdiMain = mdiMain;
    this.aContext = aContext;
    this.dispatcher = dispatcher;
  }
  //--------------------------------------------------------------------------
  public Object clone()
  { return new SaveSolutionAsCommand(dispatcher, aContext, mdiMain);
  }
  //--------------------------------------------------------------------------
  public void execute()
  { // проверка прав доступа
    Checker checker = new Checker( aContext.getDataSourceInterface() );
    if( !checker.checkCommand(checker.saveResultOfOptimization) )
 return;
    System.out.println("SaveSolutionAsCommand.execute() - starting ...");
    DataSourceInterface dataSource = aContext.getDataSourceInterface();
    SaveDialog sd;
    while (true)
    {  sd = new SaveDialog(aContext, aContext.getDispatcher(), "Сохранение решения мониторинга для схемы");
       int ret = sd.init(mdiMain.scheme.getName(), "", false);
       if (ret == 0)
 return;
        if (!MyUtil.validName(sd.name))
       {  JOptionPane.showMessageDialog(Environment.getActiveWindow(), "Некорректное название решения.", "Ошибка", JOptionPane.OK_OPTION);
       }
       else
    break;
    }

    String slc_id = mdiMain.aContext.getDataSourceInterface().GetUId(SolutionCompact.typ);// уникальный идентификатор решения
    String slc_name = sd.name; // "Решение для схемы " + mdiMain.scheme.name + "; id = " + slc_id;
    SolutionCompact slc;
    { Solution s = mdiMain.optimizerContext.solution;
      slc = new SolutionCompact(s.price, mdiMain.scheme.id, s.paths, slc_id, slc_name);
    }
    slc.setTransferableFromLocal();
    Pool.put(SolutionCompact.typ, slc.id, slc);
    dataSource.SaveSchemeMonitoringSolutions(slc.id);

    System.out.println("saving additional info about scheme-decision: starting...");
    InfoToStore additional_info = new InfoToStore( mdiMain );
    additional_info.solution_compact_id = slc.id; // прописываем явно, потому что add_info можект писаться и для схемы (тогда это поле пустое), а не для решения без схеы
    additional_info.setTransferableFromLocal();
    Pool.put(InfoToStore.typ, additional_info.id, additional_info.transferable);
    dataSource.SaveSchemeOptimizeInfo(additional_info.id);
    System.out.println("saving additional info about scheme-decision: done");

    dispatcher.notify(new OperationEvent(this, 0, "scheme_saved_event"));
    System.out.println("SaveSolutionAsCommand.execute() - done");
  }
  //--------------------------------------------------------------------------
}
//===================================================================================================================
//===================================================================================================================
class SaveDialog extends JDialog
{ private JButton okButton = new JButton("OK");
  private JButton cancelButton = new JButton("Отмена");
  SchemePropsPanel panel;
  public String name = "";
  public String description = "";
  public int retCode = 0;
  ApplicationContext aContext;
  Dispatcher dispatcher;
  //--------------------------------------------------------------------------
  public SaveDialog(ApplicationContext aContext, Dispatcher dispatcher, String title)
  { super(Environment.getActiveWindow());
    this.aContext = aContext;
    this.dispatcher = dispatcher;
    setTitle(title);
    setModal(true);
    this.addWindowListener(new java.awt.event.WindowAdapter()
    { public void windowClosing(WindowEvent e)
      { //undo();
      }
    });
    Dimension frameSize = new Dimension (400, 165);
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    setSize(frameSize);
    setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
  }
  //--------------------------------------------------------------------------
  public int init(String name, String descr, boolean show_ugo)
  { panel = new SchemePropsPanel(aContext, dispatcher, show_ugo);
    panel.schemeNameTextField.setText(name);
    panel.schemeDescrTextArea.setText(descr);
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(panel, BorderLayout.CENTER);
    // BUTTONS
    JPanel buttonPanel = new JPanel();
    buttonPanel.setPreferredSize(new Dimension(300, 30));
    buttonPanel.setBorder(BorderFactory.createEmptyBorder());
    buttonPanel.setLayout(new FlowLayout());
    okButton.setPreferredSize(new Dimension(80, 25));
    cancelButton.setPreferredSize(new Dimension(80, 25));
    buttonPanel.add(okButton, FlowLayout.LEFT);
    buttonPanel.add(cancelButton, FlowLayout.CENTER);
    okButton.addActionListener(new ActionListener()
    { public void actionPerformed (ActionEvent ev)
      { this_okButtonActionPerformed();
      }
    });
    cancelButton.addActionListener(new ActionListener()
    { public void actionPerformed (ActionEvent ev)
      { //undo();
        dispose();
      }
    });
    this.getContentPane().add(buttonPanel, BorderLayout.SOUTH);
    setModal(true);
    setVisible(true);
    return retCode;
  }
  //--------------------------------------------------------------------------
  void this_okButtonActionPerformed()
  { retCode = 1;
    name = panel.getSchemeName();
    description = panel.getSchemeDescription();
    dispose();
  }
  //--------------------------------------------------------------------------
}
//===================================================================================================================
