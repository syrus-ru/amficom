package com.syrus.AMFICOM.Client.Optimize;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelOptimize;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;  // это для статус-бара ( модель )

// окно отображения хода истории процесса оптимизации по итерациям
//****************************************************************************************************************
// класс, наследует фрэйм и агрегирует кривую, которая
// содержит в себе тред, регулярно вызываеющий её перерисовку
public class IterationsHistoryFrame extends JInternalFrame implements OperationListener
{
	Dispatcher dispatcher;
	OpticalOptimizerContext optimizerContext;//контекст оптимизации оптических сетей
	OptimizeMDIMain mdiMain; //главное окно ( нужно для dispose )

	PanelCurve jpHist ;//панель, на которой будет отбражаться график процесса оптимизации: лучшее и среднее значение
	JPanel statusBarPanel = new JPanel();
	StatusBarModel statusBar = new StatusBarModel(0);
	IterationsHistoryFrameThread  thread;
//---------------------------------------------------------------------------
	public IterationsHistoryFrame(Dispatcher dispatcher, OpticalOptimizerContext optimizerContext, OptimizeMDIMain mdiMain)
	{ if (optimizerContext == null)
	return;
		try
		{  this.setClosable(true); this.setResizable(true); this.setMaximizable(true);
			 this.dispatcher = dispatcher;
			 this.optimizerContext = optimizerContext;
			 this.mdiMain = mdiMain;

			 jpHist = new PanelCurve(optimizerContext, this); // this нужен для обновления статусбара
			 thread = new IterationsHistoryFrameThread(this);
			 thread.update = 0;// при создании тред ничего не обновляет
			 thread.start();// создаём тред , он ничего не будет обновлять, пока update = 0

			 dispatcher.register(this, "startevent");//подписываемся на события
			 dispatcher.register(this, "stopevent"); //которые нас интересуют

			 jbInit();
			 place();
		}
		catch(Exception e)
		{  e.printStackTrace();
		}
	}
	//---------------------------------------------------------------------------
	//устанавливаем размеры
	public void place()
	{	Dimension dim = mdiMain.scrollPane.getSize();
		int width  = (int)(0.22*dim.width);
		int height = (int)(0.47*dim.height - 218);//218 - фиксированная высота окана выбора цен
		if(height<200) { height=200;}

		setBounds( dim.width-width, (int)(218 - 28 + dim.height*0.18), width, height);// - 28 потому что осволбодилось место из-за удаления меню в окне просмотра цен на оборудование

		setVisible(true);
	}
	//---------------------------------------------------------------------------
	private void jbInit() throws Exception
	{ this.setFrameIcon( new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/general.gif")) );
		this.setTitle( LangModelOptimize.getString("frameIterationsHistoryTitle") );
		setDefaultCloseOperation(HIDE_ON_CLOSE);// не закрываем, а прячем
		this.addInternalFrameListener ( new javax.swing.event.InternalFrameAdapter()
			{  public void internalFrameClosing(InternalFrameEvent e)
				 {this_internalFrameClosing(e);
				 }
			});

		statusBarPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		statusBarPanel.setLayout(new BorderLayout());
		statusBarPanel.add(statusBar, BorderLayout.CENTER);

		this.getContentPane().add(statusBarPanel, BorderLayout.SOUTH);
		statusBar.add("prices");
		statusBar.add("iteration");
		statusBar.setWidth("prices", 150);
		statusBar.setWidth("iteration", 50);
		statusBar.organize();
    updateStatusbar(-1,-1);

		this.getContentPane().add(jpHist, BorderLayout.CENTER);
	}
	//---------------------------------------------------------------------------
	// события кидает OptimizeStartCommand и OptimizeStopCommand
	public void operationPerformed(OperationEvent e)
	{	if ( e.getActionCommand().equals("stopevent") )
		{  thread.update = 0;
		}
		if ( e.getActionCommand().equals("startevent") )
		{  thread.update = 1;
		}
	}
	//---------------------------------------------------------------------------
	// при "закрытии" окна
	void this_internalFrameClosing(InternalFrameEvent e){}
	//---------------------------------------------------------------------------
  public void updateStatusbar ( double best, double iter)
  { //String s = String.valueOf( ((double)((int)(best*100)))/100 )+" \\ "+String.valueOf( ((double)((int)(avrg*100)))/100 );
    String s = String.valueOf( ((double)((int)(best*100)))/100 );
    if(best == -1 ){s = "--";}
    if(optimizerContext.is_in_progress == 1)//имеет смысл спрашивать только если процесс был уже запущен
    { // выводим информацию о непропингованных рёбрах
      int untested = (int)optimizerContext.dllAdapter.GetBestUnpingedRibsNumber();
      if(untested!=0)
      { s += "; непротестировано линков:"+String.valueOf(untested);
      }
    }
    statusBar.setText("prices", "цена: " + s);
    if(iter != -1)
    { s = String.valueOf((int)iter)+" \\ "+String.valueOf((int)optimizerContext.dllAdapter.GetIterationCounter());
    }
    else { s = "-- \\ --";}
    statusBar.setText("iteration", "шаг: " + s);
	}
  //---------------------------------------------------------------------------
	public PanelCurve getItHistPanel()
  {
    return jpHist;
  } 
}
//==================================================================================================================
//==================================================================================================================
// класс содержит 2 кривые: лучшее и среднее зачения
class PanelCurve extends JPanel
{	BorderLayout borderLayout1 = new BorderLayout();
	OpticalOptimizerContext optimizerContext;
	IterationsHistoryFrame iterFrame;
	int iter_count = 0;// локальный счётчик итераций (для вспомогательных нужд)
	final int hist_size = 600;// размер буфера историии
	double hist_min, hist_max;// наибольшее и наименьшее значения на графиках, отображаемые окном ( испольуются для автомасштабирования )
	double best, average;
	double[] hist_best = new double [hist_size];// буфер истории
	//---------------------------------------------------------------------------
	public PanelCurve(OpticalOptimizerContext optimizerContext, IterationsHistoryFrame iterFrame)
	{ this.iterFrame = iterFrame;
		try
		{  jbInit();
		}
		catch(Exception ex){ex.printStackTrace();}
	}
	//---------------------------------------------------------------------------
	void jbInit() throws Exception
	{ this.setLayout(borderLayout1);
	}
	//---------------------------------------------------------------------------
	// добавляет в историю 2 значения: среднее и лучшее решение, и вызывает их перерисовку
	// эта функция регулярно вызывается тредом ( и только им )
	public void Write2FitHist(double best)
	{ int i;
		iter_count++;
		if(iter_count < 3) // в начале могут быть некорректные значения
	return;
		if(iter_count == 3)
		{   for(i=0; i<iter_count; i++)
				{ hist_best[i] = best;
				}
				hist_max = best; hist_min = 0;
		}
		if(iter_count > hist_size-1)     // если история полна, то делаем сдвиг записей истории
		{   for(i=1; i<hist_size; i++)
				{   hist_best[i-1] = hist_best[i];
				}
				hist_best[hist_size-1] = best;// и записываем последние данные о фитнесе в конец
		}
		else // если не полна (поколений меньше hist_size)
		{   hist_best[iter_count] = best;
		}

		iterFrame.updateStatusbar(best, iter_count);// обновить информацию в статусбаре
		iterFrame.repaint();// перерисовать
	}
	//---------------------------------------------------------------------------
	public void paint(Graphics g)
	{  DrawHistory(g);
	}
	//---------------------------------------------------------------------------
	public void DrawHistory(Graphics g)
  { int i, j , k, border = 20;
		int x, y, x_min = 2, y_min = 2, x_max = getWidth()-6, y_max = getHeight(); //для рисования
		int x_inc = 30, y_inc = 30;// ячейка координатной сетки

		//стираем всё
		g.setColor(Color.white);
		g.fillRect(x_min, y_min, x_max,  y_max);
		// рисуем сетку
		g.setColor(Color.gray);
		for( x=x_min; x<x_max; x+=x_inc){ g.drawLine(x, y_min, x, y_max); }
		for( y=y_min; y<y_max; y+=y_inc){ g.drawLine(x_min, y, x_max, y); }
		// график хода решения
    if(iter_count < 3)
 {return;}
    double x_scale, y_scale;
		int x1, x2, y1, y2;
		//масштабы
		x_scale = (x_max - x_min - 2*border)/(double)hist_size;
    // перемасштабируем не каждый шаг, чтоб не дёргалось сильно
		if( (double)(iter_count/10) == (double)iter_count/10)
		{	hist_max = hist_best[0]; hist_min = hist_best[0];// будем искать экстремумы заново, так как старые уже могли "уехать" влево
			for(int el_cou=0; el_cou < (iter_count<hist_size ? iter_count: hist_size); el_cou++) // автомастштабирование
			{ if(hist_max < hist_best[el_cou]) { hist_max = hist_best[el_cou];}
				if(hist_min > hist_best[el_cou]) { hist_min = hist_best[el_cou];}
      }
		}
		if(hist_max == hist_min){ hist_max = hist_min + 1;}
		y_scale = (y_max - y_min - 2*border)/(hist_max - hist_min);
    // лучшее решение
		x2 = x_min + border;
		y2 =(int)(y_max - border - (hist_best[0]-hist_min)* y_scale);
		g.setColor(Color.RED);
    for(i = 0; i < (iter_count > hist_size ? hist_size : iter_count); i++)
		{ x1 = x2; y1 = y2;
			x2 = (int)(x_min + border + x_scale * i);
			y2 = (int)(y_max - border - y_scale * (hist_best[i] - hist_min) );
			g.drawLine(x1,y1,x2,y2);
		}
//    // среднее решение
//		x2 = x_min + border;
//		y2 =(int)( y_max - border - (hist_best[0]-hist_min) * y_scale );
//		g.setColor(Color.BLACK);
//		for(i = 0; i < (iter_count > hist_size ? hist_size : iter_count); i++)
//		{ x1 = x2; y1 = y2;
//			x2 = (int)(x_min + border + x_scale * i);
//			y2 = (int)(y_max - border - y_scale * (hist_best[i] - hist_min) );
//			g.drawLine(x1,y1,x2,y2);
//		}
  }
}
//===============================================================================================================
//===============================================================================================================
class IterationsHistoryFrameThread extends Thread
{ private IterationsHistoryFrame frame;
  private DllAdapter dllAdapter;
  public int update = 0;// при создании вызываем start(), но не выполняем
  // обновление, пока update не будет установлено извне в 0
  //---------------------------------------------------------------------------------
  IterationsHistoryFrameThread(IterationsHistoryFrame frame)
  { super();
    this.frame = frame;
    dllAdapter = frame.mdiMain.optimizerContext.dllAdapter;
  }
  //---------------------------------------------------------------------------------
  public void run()
  { try
    { for(;;)
      { if(update != 0)
        { frame.jpHist.Write2FitHist(dllAdapter.GetBestPrice());
        }
        sleep(1000);
      }
    }
    catch (Exception e)
    { System.err.println( "IterationsHistoryFrameThread.run(): " + e.toString() );
    }
  }
  //---------------------------------------------------------------------------------
}
//===============================================================================================================
