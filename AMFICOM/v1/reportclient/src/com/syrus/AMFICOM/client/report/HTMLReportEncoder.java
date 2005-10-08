/*
 * $Id: HTMLReportEncoder.java,v 1.7 2005/10/08 13:30:14 arseniy Exp $
 *
 * Copyright © 2004 Syrus Systems.
 * Dept. of Science & Technology.
 * Project: AMFICOM.
 */
package com.syrus.AMFICOM.client.report;

import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import com.syrus.AMFICOM.client.UI.ChoosableFileFilter;
import com.syrus.AMFICOM.client.model.Environment;
import com.syrus.AMFICOM.report.ReportTemplate;

public class HTMLReportEncoder {
	private static final String FILE_SEPARATOR = System.getProperty("file.separator");
	private static final String FILES_DIRECTORY = ".files";
	private List<RenderingComponent> components = null;
	private ReportTemplate reportTemplate = null;
	/**
	 * Используется для присвоения имён файлам с изображениями
	 */
	private int imagesSaved = 0;
	
	private String absoluteFilesDirName = null;
	private String relativeFilesDirName = null;	
	
	public HTMLReportEncoder(List<RenderingComponent> components, ReportTemplate template) {
		this.components = components;
		this.reportTemplate = template;
	}

	public void encodeToHTML() throws IOException {
		String fileName = this.getHTMLFileName();
		if (fileName == null)
			return;
		
		encodeToHTML(fileName);
	}
	
	public void encodeToHTML(String fileName) throws IOException {
		File htmlFile = new File(fileName);
		//Абсолютное имя файла HTML 
		String absoluteHTMLFileName = htmlFile.getAbsolutePath();
		//Абсолютное имя файла HTML, только без расширения файла		
		String htmlNameWOExtension =
			absoluteHTMLFileName.substring(0,absoluteHTMLFileName.lastIndexOf('.'));
		//Абсолютное имя каталога .files для данного HTML файла		
		this.absoluteFilesDirName = htmlNameWOExtension + FILES_DIRECTORY;
		File filesDir = new File(this.absoluteFilesDirName);
		//Создали каталог для хранения изображений из этого отчёта 
		if(!filesDir.exists())
			filesDir.mkdir();
		
		this.relativeFilesDirName = filesDir.getName();
		
		FileOutputStream out = new FileOutputStream(htmlFile);
		
		List<Integer> xs = new ArrayList<Integer>();
		List<Integer> ys = new ArrayList<Integer>();
		this.getAxisValuesMatrices(xs,ys);
		int[][] componentsMatrix = this.createComponentsMatrix(xs,ys);
		
		int mainTableWidth = xs.get(xs.size() - 1).intValue();
		this.encodeHeader(out,mainTableWidth);
		this.encodeAllComponents(componentsMatrix,xs,ys,out);
		this.encodeFooter(out);			
		out.close();
	}
	/**
	 * Возвращает сортированные списки xs и ys начал и концов
	 * элементов отображений по x,y.
	 * @param xs вектор значений x и x + width для всех объектов
	 * @param ys вектор значений y и y + height для всех объектов
	 */
	private void getAxisValuesMatrices(
			List<Integer> xs,
			List<Integer> ys) {
		//Для хранения выходных данных используются списки, поскольку мы не
		//знаем заранее какие надписи привязаны, а какие нет (мы отдельно
		//учитываем только координаты непривязанных надписей) - не знаем сколько
		//выделять ячеек в массиве.
		xs.add(new Integer(0));
		ys.add(new Integer(0));
		
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
	 * @param componentsMatrix Матрица с индексами элементов, -1 в месте
	 * их отсутствия и -2 в распечатанных ячейках.
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
				if (componentsMatrix[row][col] == -2) // -2 эти ячейки уже распечатаны
					continue;
				//Подготавливаем ячейку
				Dimension size = findComponentGridSize(componentsMatrix, col, row);
				int cellHeight = ys.get(row + size.height).intValue() - ys.get(row).intValue();
				int cellWidth = xs.get(col + size.width).intValue() - xs.get(col).intValue();	
	
				StringBuffer buffer = new StringBuffer();
				buffer.append("<td");
				buffer.append(" width=\"");
				buffer.append(cellWidth);
				buffer.append("\"");
				buffer.append(" height=\"");
				buffer.append(cellHeight);
				buffer.append("\"");
				if (size.width > 1) {
					buffer.append("colspan=\"");
					buffer.append(size.width);
					buffer.append("\"");
				}
				if (size.height > 1)
					buffer.append("rowspan=\"" + size.height + "\"");
				
				//Пишем компонент
				if (componentsMatrix[row][col] < 0) {
					String emptyCellEndOfTDTag = ">";
					
					buffer.append(emptyCellEndOfTDTag);
					out.write(buffer.toString().getBytes());					
				}
				else {
					//В ячейке есть компонент
					RenderingComponent component = this.components.get(componentsMatrix[row][col]);

					//Завершение открывающего тага TD
					//для текста там нужно указывать размер
					//для таблицы align
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
					
					buffer.append(endOfTDTag);
					out.write(buffer.toString().getBytes());					
					
					if (component instanceof AttachedTextComponent) {
						AttachedTextComponent textComponent =
							(AttachedTextComponent) component;
						this.encodeText(
								textComponent.getText(),
								textComponent.getFont(),
								out);
					}
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
						throw new AssertionError("HTMLReportEncoder - encodeAllComponents - Unknown component!!");
				}
				out.write("</td>\n".getBytes());
				
				//Помечаем ячейки как распечатанные
				for (int i = row; i < row + size.height; i++)
					for (int j = col; j < col + size.width; j++)
					componentsMatrix[i][j] = -2;
			}
			out.write("</tr>\n".getBytes());
		}
	}
	
	private String createImageFileForComponent(ImageRenderingComponent component) throws ImageFormatException, IOException {
		String fileShortName = "image" + (this.imagesSaved++) + ".jpg";
		File imageFile = new File(this.absoluteFilesDirName + FILE_SEPARATOR + fileShortName);
		
		FileOutputStream jpgOs = new FileOutputStream(imageFile);
		JPEGImageEncoder enc = JPEGCodec.createJPEGEncoder(jpgOs);
		enc.encode(component.getImage());
		jpgOs.close();
		
		return fileShortName;
	}
	
	private void encodeText(
			String text,
			Font font,
			FileOutputStream out) throws IOException {
		String labelText = new String(text);
		//TODO Проверить работу replaceAll
		labelText.replaceAll("[ ]","&nbsp");
		labelText.replaceAll("[\n]","<br>");		

		out.write(this.getHTMLFontText(text,font).getBytes());
	}

	private String getHTMLFontText(String text,Font font) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<font face=\"");
		buffer.append(font.getName());
		buffer.append("\">");
		if (font.isBold())
			buffer.append("<b>");
		if (font.isItalic())
			buffer.append("<i>");
		buffer.append(text);
		if (font.isItalic())
			buffer.append("</i>");
		if (font.isBold())
			buffer.append("</b>");
		buffer.append("</font>");
		return buffer.toString();
	}
	
	private void encodeTableComponent(
			TableDataRenderingComponent component,
			FileOutputStream out) throws IOException {
		JTable table = component.getTable();
		Font tableFont = table.getFont();
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n<table frame=\"box\" border=\"1\" width=\"");
		buffer.append(table.getWidth());
		buffer.append("\"");
		buffer.append(" style=\"font-size: ");
		buffer.append(tableFont.getSize());
		buffer.append("px;\">\n\n");
	
		for (int i = 0; i < table.getRowCount(); i++) {
			buffer.append("<tr>");
			for (int j = 0; j < table.getColumnCount(); j++) {
				float startTableWidth = table.getColumnModel()
						.getTotalColumnWidth();
				float currTableWidth = table.getWidth();
				int columnWidth = (int) (table.getColumnModel().getColumn(j)
						.getWidth()
						* currTableWidth / startTableWidth);
	
				buffer.append("<td width=\"");
				buffer.append(Integer.toString(columnWidth));
				buffer.append("\">");
	
				if (table.getValueAt(i, j) instanceof String) {
					String stringValue = (String) table.getValueAt(i, j);
					if (!stringValue.equals(""))
						buffer.append(this.getHTMLFontText(stringValue,tableFont));
					else
						buffer.append("&nbsp");
				}
				buffer.append("</td>\n");
			}
			buffer.append("</tr>\n");
		}
		buffer.append("</table>\n\n");
		out.write(buffer.toString().getBytes());
	}
	
	private void encodeHeader(FileOutputStream out, int mainTableWidth)
		throws java.io.IOException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<html>\n\n");
			
		buffer.append("<head>\n");
			
		buffer.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=windows-1251\">\n");
		buffer.append("<meta name=\"GENERATOR\" content=\"AMFICOM HTML generator\">\n");
		buffer.append("<meta name=\"ProgId\" content=\"AMFICOM report\">\n");
			
		buffer.append("<title>");
		buffer.append(LangModelReport.getString("report.reportForTemplate"));
		buffer.append(" \"");
		buffer.append(this.reportTemplate.getName());
		buffer.append("\"</title>\n");
			
		buffer.append("</head>\n\n");
			
		buffer.append("<body>\n\n");
		buffer.append("<table border=\"0\" width=\"");
		buffer.append(mainTableWidth);
		buffer.append("\" style=\"font-size: 0\"> \n\n");

		out.write(buffer.toString().getBytes());
	}

	private void encodeFooter(FileOutputStream out)
		throws java.io.FileNotFoundException, java.io.IOException {
		String buff = "</table>\n\n</body>\n\n</html>";
		out.write(buff.getBytes());
	}

	private void encodeImageComponent(
		ImageRenderingComponent component,
		String imageFileName,
		FileOutputStream out) throws IOException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<img border=\"0\" ");
		buffer.append("src=\"");
		buffer.append(this.relativeFilesDirName);
		buffer.append(FILE_SEPARATOR);
		buffer.append(imageFileName);
		buffer.append("\" ");
		buffer.append("width=\"");
		buffer.append(component.getWidth());
		buffer.append("\" ");
		buffer.append("height=\"");
		buffer.append(component.getHeight());
		buffer.append("\">");
		out.write(buffer.toString().getBytes());
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

		//Ищем количество колонок, занимаемых объектом		
		int cols = 0;
		while (componentsMatrix[yStartValue][xStartValue + cols] == startValue) {
			cols++;
			if ((xStartValue + cols) >= columnCount)
				break;
		}

		//Ищем количество рядов, занимаемых объектом		
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

		fileChooser.setDialogTitle(LangModelReport.getString("report.File.selectFileToWrite"));
		fileChooser.setMultiSelectionEnabled(false);

		int option = fileChooser.showSaveDialog(Environment.getActiveWindow());
		if(option == JFileChooser.APPROVE_OPTION) {
			fileName = fileChooser.getSelectedFile().getPath();
		}

		if(fileName == null)
			return null;

		if (!	(	fileName.endsWith(".htm")
				||	fileName.endsWith(".html")))
			fileName += ".html";
		
		if(new File(fileName).exists()) {
			int answer = JOptionPane.showConfirmDialog(
					Environment.getActiveWindow(),
					LangModelReport.getString("report.File.rewriteFile"),
					LangModelReport.getString("report.File.confirm"),
					JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE);
			if (answer == JOptionPane.CANCEL_OPTION)
				return null;
		}

		return fileName;
	}
	
	/**
	 * @param xs
	 * @param ys
	 * @return Матрица int'ов, в которой содержится либо индекс компонента,
	 * который занимает соответствующую ячейку, либо -1, если в ячейке пусто. 
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
				int xValue = (xs.get(column).intValue() + xs.get(column + 1).intValue()) / 2;
				RenderingComponent componentAtPoint = getComponentAtPoint(xValue, yValue);
				result[row][column] = (componentAtPoint != null) 
					? this.components.indexOf(componentAtPoint) : -1;
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
