package com.syrus.AMFICOM.Client.Optimize.UI;

import java.awt.*;
import java.awt.print.*;


import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

// выводит на печавть схему решения ( схематическое отображение и оптимизированный путь )
//===================================================================================================================
public class SchemePrintThread extends Thread
{
	private SchemeGraph graph; // схема
//	private SchemeSimplePanel scheme_panel; // панель, на которой отображается схема
	private SchemePanelNoEdition scheme_panel; // панель, на которой отображается схема
	//SchemeSimplePanel задано так : public class SchemeSimplePanel extends JPanel implements KeyListener, Printable
	private ViewSolutionFrame solutionFrame;// таблица подробного решения
	//---------------------------------------------------------------------
	public SchemePrintThread (SchemePanelNoEdition scheme_panel, ViewSolutionFrame solutionFrame)
	{ super();
		this.scheme_panel = scheme_panel;
		this.graph = scheme_panel.getGraph();
		this.solutionFrame = solutionFrame;
	}
	//---------------------------------------------------------------------
	public void run()
	{	try
		{	// печать схемы
			PrinterJob printJob = PrinterJob.getPrinterJob();
			if(printJob.printDialog()) // можно передумать и не печатать
			{	PageFormat pf = printJob.defaultPage();
				// ставим начальные настройки
				double smallMargin = .393701 * 72; //малые поля бумаги ( .393701" = 1 см )
				Paper paper = pf.getPaper();
				paper.setImageableArea( smallMargin, smallMargin,
																(paper.getWidth() - (2*smallMargin)),(paper.getHeight()-(2*smallMargin)) );
				pf.setPaper(paper);
				pf = (printJob.pageDialog(pf)); // уточняем формат страницы

				double scale = graph.getScale();
				graph.setScale(scale*0.7);// делаем поправку к масштабу, чтобы растояние между точками сети было 2.5 мм
				Graphics g = scheme_panel.getGraphics();
				Graphics2D g2d = (Graphics2D)g;
				g2d.translate(pf.getImageableX(), pf.getImageableY());// чтобы видимое на экране не выходило за границы при печати
				boolean grid_visibility = graph.isGridVisible();
				graph.setGridVisible(false);
				// печать графа
				printJob.setPrintable(scheme_panel,pf); // указываем формат
				printJob.print();//schemePanel печаает только граф, а не всю себя
				graph.setGridVisible(grid_visibility);
				graph.setScale(scale);// возвращаем старый масштаб

				// печать подробной нитки маршрутов
				// выравниваем по ширине страницы
				Dimension old_size = new Dimension( solutionFrame.getSize() );
				g = solutionFrame.getGraphics();
				g2d = (Graphics2D)g;
				g2d.translate(pf.getImageableX(), pf.getImageableY());// чтобы видимое на экране не выходило за границы при печати
				solutionFrame.setSize( (int)(pf.getImageableWidth()), 1000 );

				printJob.setPrintable(solutionFrame,pf); // указываем формат
				printJob.print();

				solutionFrame.setSize( old_size );

//----- пример с java.sun.com
//				PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();  // set up printing attributes
//				aset.add (OrientationRequested.PORTRAIT);
//				aset.add (new Copies(1));
//				pj = PrinterJob.getPrinterJob();
//				if (pj.printDialog(aset))
//			  {  pj.setPrintable (this);
//				   try
//			     { pj.print (aset);
//					 }
//					 catch (PrinterException e)
//			     { addMessage ("Printing error: ");
//				   }
//				}
//----- конец примера с java.sun.com
			}
		}
		catch (Exception ex){ ex.printStackTrace(); }
	}
	//---------------------------------------------------------------------
}
//===============================================================================================================
