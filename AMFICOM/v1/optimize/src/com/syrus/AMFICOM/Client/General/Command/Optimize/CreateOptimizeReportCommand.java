package com.syrus.AMFICOM.Client.General.Command.Optimize;

import com.syrus.AMFICOM.Client.General.Command.OpenTypedTemplateCommand;
import com.syrus.AMFICOM.Client.General.Command.VoidCommand;
import com.syrus.AMFICOM.Client.General.Lang.LangModelOptimize;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Optimize.*;

import com.syrus.AMFICOM.Client.General.Report.*;

// ������� "������� ��" - ��������� ��� ����, ����� ��������
//========================================================================================================
public class CreateOptimizeReportCommand extends VoidCommand
{ private ApplicationContext aContext;
  private OptimizeMDIMain mdiMain;
  //--------------------------------------------------------------------------------
  public CreateOptimizeReportCommand(){}
  //--------------------------------------------------------------------------------
  public CreateOptimizeReportCommand (  ApplicationContext aContext, OptimizeMDIMain optimizeMDIMain )
  {	 this.aContext = aContext;
     this.mdiMain = optimizeMDIMain;
  }
  //--------------------------------------------------------------------------------
  public void setParameter(String field, Object value)
  {	 if(field.equals("aContext"))
     { setApplicationContext((ApplicationContext)value);
     }
  }
  //--------------------------------------------------------------------------------
  public void setApplicationContext(ApplicationContext aContext)
  { this.aContext = aContext;
  }
  //--------------------------------------------------------------------------------
  public Object clone()
  { return new CreateOptimizeReportCommand(aContext, mdiMain);
  }
  //--------------------------------------------------------------------------------
  public void execute()
  {		System.out.println("CreateOptimizeReportCommand.execute() starting ...");
  		AMTReport amtr = new AMTReport();
  		amtr.addReportPanel( "�����", this.mdiMain.schemeFrame.schemePanel); // !!! �������� ��������� ��������� �� ������������
 		amtr.addReportTable( LangModelOptimize.getString("frameOptimizationParamsTitle"), 
							 this.mdiMain.paramsFrame.getTableModelForReport() );
  		amtr.addReportTable("���� ������������", this.mdiMain.solutionFrame.getTableModelForReport());
		new OpenTypedTemplateCommand(aContext, ReportTemplate.rtt_Optimization,   amtr); 

  		System.out.println("CreateOptimizeReportCommand.execute() - done");
  }
  //--------------------------------------------------------------------------------
  // ������� ����������� ��������� ������ ���� (������ �������� ����������� �� ������� close_all)
  private void disableSomeMenuItems( ApplicationModel aModel )
  {
//      aModel.setAllItemsEnabled(false);
//      aModel.enable("menuSession");
//      aModel.enable("menuSessionNew");
//      aModel.enable("menuSessionConnection");
      aModel.fireModelChanged("");
  }
  //-----------------------------------------------------------------------------------------
}
//========================================================================================================
