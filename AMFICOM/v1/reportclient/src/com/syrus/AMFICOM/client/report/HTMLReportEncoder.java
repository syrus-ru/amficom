/*
 * $Id: HTMLReportEncoder.java,v 1.1 2005/09/07 14:26:10 peskovsky Exp $
 *
 * Copyright � 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.font.FontRenderContext;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextPane;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.report.AttachedTextStorableElement;
import com.syrus.AMFICOM.report.DataStorableElement;
import com.syrus.AMFICOM.report.ImageStorableElement;
import com.syrus.AMFICOM.report.StorableElement;
import com.syrus.AMFICOM.report.TextAttachingType;

public class HTMLReportEncoder {
	private static final String FILES_DIRECTORY = ".files";
	private List<RenderingComponent> components = null;
	private String reportTitle = null;
	/**
	 * ������������ ��� ���������� ��� ������ � �������������
	 */
	private int imagesSaved = 0;
	
	private String filesDirName = null;
	
	public HTMLReportEncoder(List<RenderingComponent> components, String reportTitle) {
		this.components = components;
		this.reportTitle = reportTitle;
	}
	
	public void encodeToHTML() {
		String fileName = this.getHTMLFileName();
		if (fileName == null)
			return;
		
		try {
			File htmlFile = new File(fileName);
			String absoluteHTMLFileName = htmlFile.getAbsolutePath();
			String htmlNameWOExtension =
				absoluteHTMLFileName.substring(0,absoluteHTMLFileName.lastIndexOf('.'));
			this.filesDirName = htmlNameWOExtension + FILES_DIRECTORY;
			File imageDir = new File(this.filesDirName);
			//������� ������� ��� �������� ����������� �� ����� ������ 
			if(!imageDir.exists())
				imageDir.mkdir();
			
			FileOutputStream out = new FileOutputStream(htmlFile);
			
			List<Integer> xs = new ArrayList<Integer>();
			List<Integer> ys = new ArrayList<Integer>();
			this.getAxisValuesMatrices(xs,ys);
			int[][] componentsMatrix = this.createComponentsMatrix(xs,ys);
			
			int mainTableWidth = xs.get(xs.size() - 1);
			this.encodeHeader(out,mainTableWidth);
			this.encodeAllComponents(componentsMatrix,xs,ys,out);
			this.encodeFooter(out);			
			out.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * ���������� ������������� ������ xs � ys ����� � ������
	 * ��������� ����������� �� x,y.
	 * @param xs ������ �������� x � x + width ��� ���� ��������
	 * @param ys ������ �������� y � y + height ��� ���� ��������
	 */
	private void getAxisValuesMatrices(
			List<Integer> xs,
			List<Integer> ys) {
		//��� �������� �������� ������ ������������ ������, ��������� �� ��
		//����� ������� ����� ������� ���������, � ����� ��� (�� ��������
		//��������� ������ ���������� ������������� ��������) - �� ����� �������
		//�������� ����� � �������.
		for (RenderingComponent component : this.components) {
			xs.add(new Integer(component.getX()));
			ys.add(new Integer(component.getY()));

			xs.add(new Integer(component.getX() + component.getWidth()));
			ys.add(new Integer(component.getY() + component.getHeight()));
		}
		Collections.sort(xs);
		Collections.sort(ys);		
	}
	
	/**
	 * @param componentsMatrix ������� � ��������� ���������, -1 � �����
	 * �� ���������� � -2 � ������������� �������.
	 * @param out
	 * @throws IOException 
	 */
	private void encodeAllComponents(
			int[][] componentsMatrix,
			List<Integer> xs,
			List<Integer> ys,			
			FileOutputStream out) throws IOException {
		int rowCount = componentsMatrix.length;
		int columnCount = componentsMatrix[0].length;
		
		for (int row = 0; row < rowCount; row++) {
			out.write("<tr>\n".getBytes());
			for (int col = 0; col < columnCount; col++) {
				if (componentsMatrix[row][col] == -2) // -2 ��� ������ ��� �����������
					continue;
				//�������������� ������
				Dimension size = findComponentGridSize(componentsMatrix, col, row);
				int cellHeight = ys.get(row + size.height).intValue() - ys.get(row).intValue();
				int cellWidth = xs.get(col + size.width).intValue() - xs.get(col).intValue();	
	
				String buffer =
					"<td"
					+ " width=\"" + cellWidth + "\""
					+ " height=\""+ cellHeight + "\"";
				if (cellWidth > 1)
					buffer += "colspan=\"" + cellWidth + "\"";
				if (cellHeight > 1)
					buffer += "rowspan=\"" + cellHeight + "\"";
				
				//����� ���������
				if (componentsMatrix[row][col] > 0) {
					//� ������ ���� ���������
					RenderingComponent component = this.components.get(componentsMatrix[row][col]);

					//���������� ������������ ���� TD
					//��� ������ ��� ����� ��������� ������
					//��� ������� align
					String endOfTDTag = null;
					if (component instanceof AttachedTextComponent) {				
						Font labelFont = ((AttachedTextComponent)component).getFont();
						String fontSize = Integer.toString(labelFont.getSize());
						endOfTDTag = " style=\"font-size: " + fontSize + "px" + ";\">";		
					}
					else if (component instanceof TableDataRenderingComponent)
						endOfTDTag = " valign=\"Top\">";
					else
						endOfTDTag = ">";
					
					buffer += endOfTDTag;
					out.write(buffer.getBytes());					
					
					if (component instanceof AttachedTextComponent)
						this.encodeTextComponent((AttachedTextComponent)component,out);
					else if (component instanceof ImageRenderingComponent) {
						ImageRenderingComponent irComponent =
							(ImageRenderingComponent)component;
						String imageFileShortName = this.createImageFileForComponent(
								irComponent);
						this.encodeImageComponent(
								irComponent,
								imageFileShortName,
								out);
					}
					else if (component instanceof TableDataRenderingComponent)
						this.encodeTableComponent((TableDataRenderingComponent)component,out);
					else
						throw new AssertionError("HTMLReportEncoder - encodeAllComponents -Unknown component!!");
				}
				out.write("</td>\n".getBytes());
				
				//�������� ������ ��� �������������
				for (int i = row; i < row + size.height; i++)
					for (int j = col; j < col + size.width; j++)
					componentsMatrix[i][j] = -2;
			}
			out.write("</tr>\n".getBytes());
		}
	}
	
	private String createImageFileForComponent(ImageRenderingComponent component) throws ImageFormatException, IOException {
		String fileShortName = "image" + (this.imagesSaved++) + ".jpg";
		File imageFile = new File(this.filesDirName + "/" + fileShortName);
		
		FileOutputStream jpgOs = new FileOutputStream(imageFile);
		JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(jpgOs);
		enc.encode(component.getImage());
		jpgOs.close();
		
		return fileShortName;
	}
	
	private void encodeTextComponent(
			AttachedTextComponent component,
			FileOutputStream out) throws IOException {
		Font labelFont = component.getFont();
		String fontName = labelFont.getName();

		String labelText = new String(component.getText());
		//TODO ��������� ������ replaceAll
		labelText.replaceAll("[ ]","&nbsp");
		labelText.replaceAll("[\n]","<br>");		

		String italicTagStart = "";
		String italicTagEnd = "";
		if (labelFont.isItalic()) {
			italicTagStart = "<i>";
			italicTagEnd = "</i>";
		}
		String boldTagStart = "";
		String boldTagEnd = "";
		if (labelFont.isBold()) {
			boldTagStart = "<b>";
			boldTagEnd = "</b>";
		}

		String result = 
		 	"<font face=\"" + fontName + "\">"
			+ boldTagStart + italicTagStart
			+ labelText
			+ italicTagEnd + boldTagEnd
			+ "</font>";

		out.write(result.getBytes());
	}

	private void encodeTableComponent(
			TableDataRenderingComponent component,
			FileOutputStream out) throws IOException {
		JTable table = component.getTable();
		String buffer = "\n<table frame=\"box\" border=\"1\" width=\""
			+ table.getWidth()
			+ "\""
			+ " style=\"font-size: 13";
		
		buffer += "px";
		buffer += ";\">\n\n";
	
		for (int i = 0; i < table.getRowCount(); i++) {
			buffer += "<tr>";
			for (int j = 0; j < table.getColumnCount(); j++) {
				float startTableWidth = table.getColumnModel()
						.getTotalColumnWidth();
				float currTableWidth = table.getWidth();
				int columnWidth = (int) (table.getColumnModel().getColumn(j)
						.getWidth()
						* currTableWidth / startTableWidth);
	
				buffer += "<td width=\""
						+ (new Integer(columnWidth)).toString() + "\"";
				buffer += ">";
	
				if (table.getValueAt(i, j) != "")
					buffer += table.getValueAt(i, j);
				else
					buffer += "&nbsp";
				buffer += "</td>\n";
			}
			buffer += "</tr>\n";
		}
		buffer += "</table>\n\n";
		out.write(buffer.getBytes());
	}
	
	private void encodeHeader(FileOutputStream out, int mainTableWidth)
		throws java.io.IOException {
		String buff =
			"<html>\n\n"
			
			+ "<head>\n"
			+ "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1251\">\n"
			+ "<meta name=\"GENERATOR\" content=\"AMFICOM HTML generator\">\n"
			+ "<meta name=\"ProgId\" content=\"AMFICOM report\">\n"
			+ "<title>"
			+ LangModelReport.getString("report.reportForTemplate") + " " + this.reportTitle
			+ "</title>\n"
			+ "</head>\n\n"
			
			+ "<body>\n\n"
			+ "<table border=\"0\" width=\"" + mainTableWidth
			+ "\" style=\"font-size: 0\"> \n\n";

		out.write(buff.getBytes());
	}

	private void encodeFooter(FileOutputStream out)
		throws java.io.FileNotFoundException, java.io.IOException {
		String buff =
			"</table>\n\n"
			+ "</body>\n\n"
			+ "</html>";
		
		out.write(buff.getBytes());
	}

	private void encodeImageComponent(
		ImageRenderingComponent component,
		String imageFileName,
		FileOutputStream out) throws IOException {
		//TODO ��� ���������� ����������
		String buffer = "<img border=\"0\" "
			+ "src=\"" + imageFileName + "\" "
			+ "width=\"" + Integer.toString(component.getWidth()) + "\" "
			+ "height=\"" + Integer.toString(component.getHeight())
			+ "\">";
		out.write(buffer.getBytes());
	}
	
	private Dimension findComponentGridSize(
			int[][] componentsMatrix,
			int xStartValue,
			int yStartValue) {
		int rowCount = componentsMatrix.length;
		int columnCount = componentsMatrix[0].length;

		if (	(xStartValue >= rowCount)
			||	(yStartValue >= columnCount))
			return null;

		int startValue = componentsMatrix[yStartValue][xStartValue];
		if (startValue == -1)
			return new Dimension(1, 1);

		//���� ���������� �������, ���������� ��������		
		int cols = 0;
		while (componentsMatrix[yStartValue][xStartValue + cols] == startValue) {
			cols++;
			if ((xStartValue + cols) >= columnCount)
				break;
		}

		//���� ���������� �����, ���������� ��������		
		int rows = 0;
		boolean toBreak = false;
		while (!toBreak) {
			rows++;
			if (yStartValue + rows >= rowCount)
				break;

			for (int x = xStartValue; x < xStartValue + cols; x++)
				if (componentsMatrix[yStartValue + rows][x] != startValue)
					toBreak = true;
		}

		return new Dimension(cols, rows);
	}
	
	
	private String getHTMLFileName() {
		String fileName = null;
		JFileChooser fileChooser = new JFileChooser();

		String[] filters = {"htm","html"};
		ChoosableFileFilter bmpFilter = new ChoosableFileFilter(
				filters,
				"HTML file formats");
		fileChooser.addChoosableFileFilter(bmpFilter);

		fileChooser.setDialogTitle("�������� ���� ��� ������");
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showOpenDialog(Environment.getActiveWindow());
		if(option == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getPath();
			if(!	(	fileName.endsWith(".htm")
					 || fileName.endsWith(".html")))
				return null;
		}

		if(fileName == null)
			return null;

		if(!(new File(fileName)).exists())
			return null;

		return fileName;
	}
	
	/**
	 * @param xs
	 * @param ys
	 * @return ������� int'��, � ������� ���������� ���� ������ ����������,
	 * ������� �������� ��������������� ������, ���� -1, ���� � ������ �����. 
	 */
	private int[][] createComponentsMatrix(
			List<Integer> xs,
			List<Integer> ys) {
		if (xs == null)
			return null;
		if (xs.size() < 2)
			return null;
		if (ys == null)
			return null;
		if (ys.size() < 2)
			return null;

		int[][] result = new int[ys.size() - 1][];
		for (int i = 0; i < result.length; i++)
			result[i] = new int[xs.size() - 1];

		for (int row = 0; row < ys.size() - 1; row++) {
			int yValue = (ys.get(row).intValue() + ys.get(row + 1).intValue()) / 2;
			for (int column = 0; column < xs.size() - 1; column++) {
				int xValue = (xs.get(column).intValue() + xs.get(column + 1)) / 2;
				RenderingComponent componentAtPoint = getComponentAtPoint(xValue, yValue);
				result[row][column] = (componentAtPoint != null) 
					? this.components.indexOf(componentAtPoint) : 0;
			}
		}

		return result;
	}
	
	private RenderingComponent getComponentAtPoint(int x,int y) {
		for (RenderingComponent component : this.components)
			if (((JComponent)component).getBounds().contains(x,y))
				return component;
		return null;
	}
}
