package com.syrus.AMFICOM.Client.Optimize.UI;

import java.awt.*;
import java.awt.print.*;


import com.syrus.AMFICOM.Client.General.Scheme.*;
import com.syrus.AMFICOM.Client.Optimize.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

// ������� �� ������� ����� ������� ( ������������� ����������� � ���������������� ���� )
//===================================================================================================================
public class SchemePrintThread extends Thread
{
	private SchemeGraph graph; // �����
//	private SchemeSimplePanel scheme_panel; // ������, �� ������� ������������ �����
	private SchemePanelNoEdition scheme_panel; // ������, �� ������� ������������ �����
	//SchemeSimplePanel ������ ��� : public class SchemeSimplePanel extends JPanel implements KeyListener, Printable
	private ViewSolutionFrame solutionFrame;// ������� ���������� �������
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
		{	// ������ �����
			PrinterJob printJob = PrinterJob.getPrinterJob();
			if(printJob.printDialog()) // ����� ���������� � �� ��������
			{	PageFormat pf = printJob.defaultPage();
				// ������ ��������� ���������
				double smallMargin = .393701 * 72; //����� ���� ������ ( .393701" = 1 �� )
				Paper paper = pf.getPaper();
				paper.setImageableArea( smallMargin, smallMargin,
																(paper.getWidth() - (2*smallMargin)),(paper.getHeight()-(2*smallMargin)) );
				pf.setPaper(paper);
				pf = (printJob.pageDialog(pf)); // �������� ������ ��������

				double scale = graph.getScale();
				graph.setScale(scale*0.7);// ������ �������� � ��������, ����� ��������� ����� ������� ���� ���� 2.5 ��
				Graphics g = scheme_panel.getGraphics();
				Graphics2D g2d = (Graphics2D)g;
				g2d.translate(pf.getImageableX(), pf.getImageableY());// ����� ������� �� ������ �� �������� �� ������� ��� ������
				boolean grid_visibility = graph.isGridVisible();
				graph.setGridVisible(false);
				// ������ �����
				printJob.setPrintable(scheme_panel,pf); // ��������� ������
				printJob.print();//schemePanel ������� ������ ����, � �� ��� ����
				graph.setGridVisible(grid_visibility);
				graph.setScale(scale);// ���������� ������ �������

				// ������ ��������� ����� ���������
				// ����������� �� ������ ��������
				Dimension old_size = new Dimension( solutionFrame.getSize() );
				g = solutionFrame.getGraphics();
				g2d = (Graphics2D)g;
				g2d.translate(pf.getImageableX(), pf.getImageableY());// ����� ������� �� ������ �� �������� �� ������� ��� ������
				solutionFrame.setSize( (int)(pf.getImageableWidth()), 1000 );

				printJob.setPrintable(solutionFrame,pf); // ��������� ������
				printJob.print();

				solutionFrame.setSize( old_size );

//----- ������ � java.sun.com
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
//----- ����� ������� � java.sun.com
			}
		}
		catch (Exception ex){ ex.printStackTrace(); }
	}
	//---------------------------------------------------------------------
}
//===============================================================================================================
