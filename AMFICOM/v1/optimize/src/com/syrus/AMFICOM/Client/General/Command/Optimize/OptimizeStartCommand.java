// класс выполнения команды "Старт" из меню команд фрейма оптимизации сети
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
    // проверка прав доступа
		Checker checker = new Checker( mdiMain.aContext.getDataSourceInterface() );
		if( !checker.checkCommand(checker.startOptimization) )
    {
  return;
    }
		try
    {   // проверка, был ли создан контекст при открытии карты
				if (optOptContext == null)
        { System.out.println("OptimizeStartCommand.execute(): optimizerContext == null. Aborting...");
  return;
        }
				// открыта ли схема (после зактрытия схемы )
				if ( mdiMain.scheme == null)
        { System.out.println("OptimizeStartCommand.execute(): Scheme = null. Aborting...");
	return;
        }
				// проверять на длину волны надо ДО формирования графа, так как это влияет на длину рёбер и вершин
				if(optOptContext.wavelength_set == 0)
				{ System.out.println("OptimizeStartCommand.execute(): wavelength not set. Aborting...");
           javax.swing.JOptionPane.showMessageDialog
							(Environment.getActiveWindow(),"длина волны не задана", "ошибка задания параметров", javax.swing.JOptionPane.OK_OPTION);
	return;
				}
				// если граф уже был успешно создан , то вторай раз не создаём
				if(optOptContext.graph_set == 0)
				{   optOptContext.dllAdapter = new DllAdapter(mdiMain);// в нём вызывается "m2g = new Map2GraphAdapter(mdiMain); g2m =  new Graph2MapAdapter()"
				}
				// если и теперь граф не создан, то выход (передавать в Dll нечего)
				if(optOptContext.graph_set == 0)
	return;
				// если topology_set != 0, то в Dll уже была передана топология графа и второй раз передавать её не надо
				if (optOptContext.topology_set == 0 )
				{	// на каждое ребро 5 даблов : номер 1й вершины, потери в 1й вершине, номер 2й вершины, потери во 2й вершине, длина ребра
					double graph_arr[] = new double[ 5*optOptContext.dllAdapter.m2g.g.ribs.size() ];
          Rib rib;
          Iterator  ribs = optOptContext.dllAdapter.m2g.g.ribs.iterator();
          for (int i=0; ribs.hasNext(); i+=5)
          { rib = (Rib)ribs.next();
            graph_arr[i]   = rib.node1_id;
            graph_arr[i+1] = ( (Node)(optOptContext.dllAdapter.m2g.g.nodes.get(rib.node1_id)) ).attenuation;// верно, так как нумерация узлов начинается с нуля, как и индексы в типе Vector
            graph_arr[i+2] = rib.node2_id;
            graph_arr[i+3] = ( (Node)(optOptContext.dllAdapter.m2g.g.nodes.get(rib.node2_id)) ).attenuation;// верно, так как нумерация узлов начинается с нуля, как и индексы в типе Vector
            graph_arr[i+4] = rib.len;
          }
					System.out.println("Setting topology...");
					optOptContext.dllAdapter.SetNetTopology(graph_arr);//при первом запуске надо сначала проинициализировать граф в Dll

				  // задать фиксированные узлы
					// формат {n1, attr1, n2, attr2 ...} n* - номер вершины, attr* - аттрибут вершины ( запрещённый, обязательный и т д )
					Vector v = new Vector();
          Node node;
          for(Iterator nodes = optOptContext.dllAdapter.m2g.g.nodes.iterator(); nodes.hasNext(); )
          { node = (Node)nodes.next();
            if( node.active &&  node.fixed)
            { v.add( new Double(node.id) );
              v.add( new Double(1) );// 1 - в узле обязательно присутствие КИС
            }
            else if( !node.active &&  node.fixed)
            { v.add( new Double(node.id) );
              v.add( new Double(0) );// 0 - в узле не может быть быть КИС
            }
          }
					if (v.size() != 0)
					{ // приводим вектор double к массиву double
						Double[] Arr = ( (Double[])v.toArray( new Double[0]) );
						double[] arr = new double[v.size()];
						for(int j=0; j<v.size(); j++)
            { arr[j] = Arr[j].doubleValue();
            }
						optOptContext.dllAdapter.SetFixedRTU(arr);
					}
				  // фиксированные узлы заданы
					System.out.println("Setting topology - done");
					optOptContext.topology_set = 1;
				}

        // передать цену оборудования через DllAdapter в Dll, если ещё не передали
        if(optOptContext.dllAdapter != null && optOptContext.prices_set == 0)// работает с dll поэтому адаптер должен быть создан
        {		System.out.println("Setting pricelist...");
            optOptContext.setPricelist();// задать цену оборудования в dll
            System.out.println("Setting pricelist - done");
        }
        if(optOptContext.prices_set == 0)
        { javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(),
            "стоимость оборудования не задана", "ошибка задания цен", javax.swing.JOptionPane.OK_OPTION);
  return;
        }

				// если все инициализации прошли нормально, то запускаем Dll
				if (optOptContext.prices_set == 1 && optOptContext.graph_set == 1)
				{
          if(!optOptContext.once_started) { firstTimePreStartInit();}
					System.out.println("Starting Dll...");
					optOptContext.Start();
          System.out.println("Starting Dll - done");
          optOptContext.once_started = true;
          mdiMain.aContext.getDispatcher().notify(new StatusMessageEvent("идёт оптимизация ..."));
					dispatcher.notify(new OperationEvent (this, 0, "startevent"));
				}
				else
				{ javax.swing.JOptionPane.showMessageDialog( Environment.getActiveWindow(),
            "Невозможно начать оптимизацию. При запуске процесса возникла ошибка.","неидентифицируемая ошибка инициализации", javax.swing.JOptionPane.OK_OPTION);
				}

        System.out.println("OptimizeStartCommand.execute(): done");
		 }
		 catch (Exception e)
		 { e.printStackTrace();
		 }
	}
	//---------------------------------------------------------------------------------------
  private void firstTimePreStartInit()
  { // передать в dll все папрметры оптимизации
    System.out.println("sending optimization params to dll: starting... ");
    for(int i = 0; i<optOptContext.opt_prs.np; i++)
    { optOptContext.dllAdapter.setOptimizationParam(i, optOptContext.opt_prs.getOptimizationParam(i));
    }
    System.out.println("sending optimization params to dll: done");

    // передать перед запуском в dll режим оптимизации (односторонний\встречный)
    mdiMain.optimizerContext.dllAdapter.DllSetOptimizeMode( mdiMain.optimizerContext.optimize_mode );
    // отключить меню выбора режимов оптимизации
    ApplicationModel aModel = mdiMain.aContext.getApplicationModel();
    aModel.disable("menuOptimizeModeUnidirect");
    aModel.disable("menuOptimizeModeBidirect");
    aModel.fireModelChanged("");
    //
  }
  //---------------------------------------------------------------------------------------
}