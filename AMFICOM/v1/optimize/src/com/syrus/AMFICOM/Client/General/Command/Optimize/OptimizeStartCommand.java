// ����� ���������� ������� "�����" �� ���� ������ ������ ����������� ����
package com.syrus.AMFICOM.Client.General.Command.Optimize;

import java.util.*;

import com.syrus.AMFICOM.Client.General.*;
import com.syrus.AMFICOM.Client.General.Command.*;
import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Model.*;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.Resource.*;
import com.syrus.AMFICOM.Client.Resource.Scheme.*;


public class OptimizeStartCommand extends VoidCommand
{
	private Dispatcher dispatcher;
	private OpticalOptimizerContext optOptContext;
	private OptimizeMDIMain mdiMain;
	//---------------------------------------------------------------------------------------
	public OptimizeStartCommand(Dispatcher dispatcher, OptimizeMDIMain mdiMain)
	{  this.dispatcher = dispatcher;
		 this.mdiMain = mdiMain;
		 this.optOptContext = mdiMain.optimizerContext;
	}
	//---------------------------------------------------------------------------------------
	public void setParameter(String field, Object value)
	{	if(field.equals("optimizerContext"))
				this.optOptContext = (OpticalOptimizerContext )value;
	}
	//---------------------------------------------------------------------------------------
	public Object clone()
	{ return new OptimizeStartCommand(dispatcher, mdiMain);
	}
	//---------------------------------------------------------------------------------------
	public void execute()
	{ System.out.println("OptimizeStartCommand.execute(): starting...");
    // �������� ���� �������
		Checker checker = new Checker( mdiMain.aContext.getDataSourceInterface() );
		if( !checker.checkCommand(checker.startOptimization) )
    {
  return;
    }
		try
    {   // ��������, ��� �� ������ �������� ��� �������� �����
				if (optOptContext == null)
        { System.out.println("OptimizeStartCommand.execute(): optimizerContext == null. Aborting...");
  return;
        }
				// ������� �� ����� (����� ��������� ����� )
				if ( mdiMain.scheme == null)
        { System.out.println("OptimizeStartCommand.execute(): Scheme = null. Aborting...");
	return;
        }
				// ��������� �� ����� ����� ���� �� ������������ �����, ��� ��� ��� ������ �� ����� ���� � ������
				if(optOptContext.wavelength_set == 0)
				{ System.out.println("OptimizeStartCommand.execute(): wavelength not set. Aborting...");
           javax.swing.JOptionPane.showMessageDialog
							(Environment.getActiveWindow(),"����� ����� �� ������", "������ ������� ����������", javax.swing.JOptionPane.OK_OPTION);
	return;
				}
				// ���� ���� ��� ��� ������� ������ , �� ������ ��� �� ������
				if(optOptContext.graph_set == 0)
				{   optOptContext.dllAdapter = new DllAdapter(mdiMain);// � �� ���������� "m2g = new Map2GraphAdapter(mdiMain); g2m =  new Graph2MapAdapter()"
				}
				// ���� � ������ ���� �� ������, �� ����� (���������� � Dll ������)
				if(optOptContext.graph_set == 0)
	return;
				// ���� topology_set != 0, �� � Dll ��� ���� �������� ��������� ����� � ������ ��� ���������� � �� ����
				if (optOptContext.topology_set == 0 )
				{	// �� ������ ����� 5 ������ : ����� 1� �������, ������ � 1� �������, ����� 2� �������, ������ �� 2� �������, ����� �����
					double graph_arr[] = new double[ 5*optOptContext.dllAdapter.m2g.g.ribs.size() ];
          Rib rib;
          Iterator  ribs = optOptContext.dllAdapter.m2g.g.ribs.iterator();
          for (int i=0; ribs.hasNext(); i+=5)
          { rib = (Rib)ribs.next();
            graph_arr[i]   = rib.node1_id;
            graph_arr[i+1] = ( (Node)(optOptContext.dllAdapter.m2g.g.nodes.get(rib.node1_id)) ).attenuation;// �����, ��� ��� ��������� ����� ���������� � ����, ��� � ������� � ���� Vector
            graph_arr[i+2] = rib.node2_id;
            graph_arr[i+3] = ( (Node)(optOptContext.dllAdapter.m2g.g.nodes.get(rib.node2_id)) ).attenuation;// �����, ��� ��� ��������� ����� ���������� � ����, ��� � ������� � ���� Vector
            graph_arr[i+4] = rib.len;
          }
					System.out.println("Setting topology...");
					optOptContext.dllAdapter.SetNetTopology(graph_arr);//��� ������ ������� ���� ������� ������������������� ���� � Dll

				  // ������ ������������� ����
					// ������ {n1, attr1, n2, attr2 ...} n* - ����� �������, attr* - �������� ������� ( �����������, ������������ � � � )
					Vector v = new Vector();
          Node node;
          for(Iterator nodes = optOptContext.dllAdapter.m2g.g.nodes.iterator(); nodes.hasNext(); )
          { node = (Node)nodes.next();
            if( node.active &&  node.fixed)
            { v.add( new Double(node.id) );
              v.add( new Double(1) );// 1 - � ���� ����������� ����������� ���
            }
            else if( !node.active &&  node.fixed)
            { v.add( new Double(node.id) );
              v.add( new Double(0) );// 0 - � ���� �� ����� ���� ���� ���
            }
          }
					if (v.size() != 0)
					{ // �������� ������ double � ������� double
						Double[] Arr = ( (Double[])v.toArray( new Double[0]) );
						double[] arr = new double[v.size()];
						for(int j=0; j<v.size(); j++)
            { arr[j] = Arr[j].doubleValue();
            }
						optOptContext.dllAdapter.SetFixedRTU(arr);
					}
				  // ������������� ���� ������
					System.out.println("Setting topology - done");
					optOptContext.topology_set = 1;
				}

        // �������� ���� ������������ ����� DllAdapter � Dll, ���� ��� �� ��������
        if(optOptContext.dllAdapter != null && optOptContext.prices_set == 0)// �������� � dll ������� ������� ������ ���� ������
        {		System.out.println("Setting pricelist...");
            optOptContext.setPricelist();// ������ ���� ������������ � dll
            System.out.println("Setting pricelist - done");
        }
        if(optOptContext.prices_set == 0)
        { javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(),
            "��������� ������������ �� ������", "������ ������� ���", javax.swing.JOptionPane.OK_OPTION);
  return;
        }

				// ���� ��� ������������� ������ ���������, �� ��������� Dll
				if (optOptContext.prices_set == 1 && optOptContext.graph_set == 1)
				{
          if(!optOptContext.once_started) { firstTimePreStartInit();}
					System.out.println("Starting Dll...");
					optOptContext.Start();
          System.out.println("Starting Dll - done");
          optOptContext.once_started = true;
          mdiMain.aContext.getDispatcher().notify(new StatusMessageEvent("��� ����������� ..."));
					dispatcher.notify(new OperationEvent (this, 0, "startevent"));
				}
				else
				{ javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(),
            "���������� ������ �����������. ��� ������� �������� �������� ������.","������������������ ������ �������������", javax.swing.JOptionPane.OK_OPTION);
				}

        System.out.println("OptimizeStartCommand.execute(): done");
		 }
		 catch (Exception e)
		 { e.printStackTrace();
		 }
	}
	//---------------------------------------------------------------------------------------
  private void firstTimePreStartInit()
  { // �������� � dll ��� ��������� �����������
    System.out.println("sending optimization params to dll: starting... ");
    for(int i = 0; i<optOptContext.opt_prs.np; i++)
    { optOptContext.dllAdapter.setOptimizationParam(i, optOptContext.opt_prs.getOptimizationParam(i));
    }
    System.out.println("sending optimization params to dll: done");

    // �������� ����� �������� � dll ����� ����������� (�������������\���������)
    mdiMain.optimizerContext.dllAdapter.DllSetOptimizeMode( mdiMain.optimizerContext.optimize_mode );
    // ��������� ���� ������ ������� �����������
    ApplicationModel aModel = mdiMain.aContext.getApplicationModel();
    aModel.disable("menuOptimizeModeUnidirect");
    aModel.disable("menuOptimizeModeBidirect");
    aModel.fireModelChanged("");
    //
  }
  //---------------------------------------------------------------------------------------
}