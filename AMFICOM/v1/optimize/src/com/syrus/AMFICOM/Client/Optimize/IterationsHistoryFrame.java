package com.syrus.AMFICOM.Client.Optimize;

import java.awt.*;

import javax.swing.*;
import javax.swing.event.*;

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;
import com.syrus.AMFICOM.Client.General.Lang.LangModelOptimize;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;  // ��� ��� ������-���� ( ������ )

// ���� ����������� ���� ������� �������� ����������� �� ���������
//****************************************************************************************************************
// �����, ��������� ����� � ���������� ������, �������
// �������� � ���� ����, ��������� ����������� � �����������
public class IterationsHistoryFrame extends JInternalFrame implements OperationListener
{
	Dispatcher dispatcher;
	OpticalOptimizerContext optimizerContext;//�������� ����������� ���������� �����
	OptimizeMDIMain mdiMain; //������� ���� ( ����� ��� dispose )

	PanelCurve jpHist ;//������, �� ������� ����� ����������� ������ �������� �����������: ������ � ������� ��������
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

			 jpHist = new PanelCurve(optimizerContext, this); // this ����� ��� ���������� ����������
			 thread = new IterationsHistoryFrameThread(this);
			 thread.update = 0;// ��� �������� ���� ������ �� ���������
			 thread.start();// ������ ���� , �� ������ �� ����� ���������, ���� update = 0

			 dispatcher.register(this, "startevent");//������������� �� �������
			 dispatcher.register(this, "stopevent"); //������� ��� ����������

			 jbInit();
			 place();
		}
		catch(Exception e)
		{  e.printStackTrace();
		}
	}
	//---------------------------------------------------------------------------
	//������������� �������
	public void place()
	{	Dimension dim = mdiMain.scrollPane.getSize();
		int width  = (int)(0.22*dim.width);
		int height = (int)(0.47*dim.height - 218);//218 - ������������� ������ ����� ������ ���
		if(height<200) { height=200;}

		setBounds( dim.width-width, (int)(218 - 28 + dim.height*0.18), width, height);// - 28 ������ ��� ������������� ����� ��-�� �������� ���� � ���� ��������� ��� �� ������������

		setVisible(true);
	}
	//---------------------------------------------------------------------------
	private void jbInit() throws Exception
	{ this.setFrameIcon( new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/general.gif")) );
		this.setTitle( LangModelOptimize.getString("frameIterationsHistoryTitle") );
		setDefaultCloseOperation(HIDE_ON_CLOSE);// �� ���������, � ������
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
	// ������� ������ OptimizeStartCommand � OptimizeStopCommand
	public void operationPerformed(OperationEvent e)
	{	if ( e.getActionCommand().equals("stopevent") )
		{  thread.update = 0;
		}
		if ( e.getActionCommand().equals("startevent") )
		{  thread.update = 1;
		}
	}
	//---------------------------------------------------------------------------
	// ��� "��������" ����
	void this_internalFrameClosing(InternalFrameEvent e){}
	//---------------------------------------------------------------------------
  public void updateStatusbar ( double best, double iter)
  { //String s = String.valueOf( ((double)((int)(best*100)))/100 )+" \\ "+String.valueOf( ((double)((int)(avrg*100)))/100 );
    String s = String.valueOf( ((double)((int)(best*100)))/100 );
    if(best == -1 ){s = "--";}
    if(optimizerContext.is_in_progress == 1)//����� ����� ���������� ������ ���� ������� ��� ��� �������
    { // ������� ���������� � ���������������� �����
      int untested = (int)optimizerContext.dllAdapter.GetBestUnpingedRibsNumber();
      if(untested!=0)
      { s += "; ���������������� ������:"+String.valueOf(untested);
      }
    }
    statusBar.setText("prices", "����: " + s);
    if(iter != -1)
    { s = String.valueOf((int)iter)+" \\ "+String.valueOf((int)optimizerContext.dllAdapter.GetIterationCounter());
    }
    else { s = "-- \\ --";}
    statusBar.setText("iteration", "���: " + s);
	}
  //---------------------------------------------------------------------------
	public PanelCurve getItHistPanel()
  {
    return jpHist;
  } 
}
//==================================================================================================================
//==================================================================================================================
// ����� �������� 2 ������: ������ � ������� �������
class PanelCurve extends JPanel
{	BorderLayout borderLayout1 = new BorderLayout();
	OpticalOptimizerContext optimizerContext;
	IterationsHistoryFrame iterFrame;
	int iter_count = 0;// ��������� ������� �������� (��� ��������������� ����)
	final int hist_size = 600;// ������ ������ ��������
	double hist_min, hist_max;// ���������� � ���������� �������� �� ��������, ������������ ����� ( ����������� ��� ������������������� )
	double best, average;
	double[] hist_best = new double [hist_size];// ����� �������
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
	// ��������� � ������� 2 ��������: ������� � ������ �������, � �������� �� �����������
	// ��� ������� ��������� ���������� ������ ( � ������ �� )
	public void Write2FitHist(double best)
	{ int i;
		iter_count++;
		if(iter_count < 3) // � ������ ����� ���� ������������ ��������
	return;
		if(iter_count == 3)
		{   for(i=0; i<iter_count; i++)
				{ hist_best[i] = best;
				}
				hist_max = best; hist_min = 0;
		}
		if(iter_count > hist_size-1)     // ���� ������� �����, �� ������ ����� ������� �������
		{   for(i=1; i<hist_size; i++)
				{   hist_best[i-1] = hist_best[i];
				}
				hist_best[hist_size-1] = best;// � ���������� ��������� ������ � ������� � �����
		}
		else // ���� �� ����� (��������� ������ hist_size)
		{   hist_best[iter_count] = best;
		}

		iterFrame.updateStatusbar(best, iter_count);// �������� ���������� � ����������
		iterFrame.repaint();// ������������
	}
	//---------------------------------------------------------------------------
	public void paint(Graphics g)
	{  DrawHistory(g);
	}
	//---------------------------------------------------------------------------
	public void DrawHistory(Graphics g)
  { int i, j , k, border = 20;
		int x, y, x_min = 2, y_min = 2, x_max = getWidth()-6, y_max = getHeight(); //��� ���������
		int x_inc = 30, y_inc = 30;// ������ ������������ �����

		//������� ��
		g.setColor(Color.white);
		g.fillRect(x_min, y_min, x_max,  y_max);
		// ������ �����
		g.setColor(Color.gray);
		for( x=x_min; x<x_max; x+=x_inc){ g.drawLine(x, y_min, x, y_max); }
		for( y=y_min; y<y_max; y+=y_inc){ g.drawLine(x_min, y, x_max, y); }
		// ������ ���� �������
    if(iter_count < 3)
 {return;}
    double x_scale, y_scale;
		int x1, x2, y1, y2;
		//��������
		x_scale = (x_max - x_min - 2*border)/(double)hist_size;
    // ���������������� �� ������ ���, ���� �� �������� ������
		if( (double)(iter_count/10) == (double)iter_count/10)
		{	hist_max = hist_best[0]; hist_min = hist_best[0];// ����� ������ ���������� ������, ��� ��� ������ ��� ����� "������" �����
			for(int el_cou=0; el_cou < (iter_count<hist_size ? iter_count: hist_size); el_cou++) // ��������������������
			{ if(hist_max < hist_best[el_cou]) { hist_max = hist_best[el_cou];}
				if(hist_min > hist_best[el_cou]) { hist_min = hist_best[el_cou];}
      }
		}
		if(hist_max == hist_min){ hist_max = hist_min + 1;}
		y_scale = (y_max - y_min - 2*border)/(hist_max - hist_min);
    // ������ �������
		x2 = x_min + border;
		y2 =(int)(y_max - border - (hist_best[0]-hist_min)* y_scale);
		g.setColor(Color.RED);
    for(i = 0; i < (iter_count > hist_size ? hist_size : iter_count); i++)
		{ x1 = x2; y1 = y2;
			x2 = (int)(x_min + border + x_scale * i);
			y2 = (int)(y_max - border - y_scale * (hist_best[i] - hist_min) );
			g.drawLine(x1,y1,x2,y2);
		}
//    // ������� �������
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
  public int update = 0;// ��� �������� �������� start(), �� �� ���������
  // ����������, ���� update �� ����� ����������� ����� � 0
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
