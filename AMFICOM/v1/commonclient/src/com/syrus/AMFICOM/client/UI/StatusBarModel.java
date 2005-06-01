//////////////////////////////////////////////////////////////////////////////
// *                                                                      * //
// * Syrus Systems                                                        * //
// * Департамент Системных Исследований и Разработок                      * //
// *                                                                      * //
// * Проект: АМФИКОМ - система Автоматизированного Многофункционального   * //
// *         Интеллектуального Контроля и Объектного Мониторинга          * //
// *                                                                      * //
// *         реализация Интегрированной Системы Мониторинга               * //
// *                                                                      * //
// * Название: Панель состояния                                           * //
// *                                                                      * //
// * Тип: Java 1.2.2                                                      * //
// *                                                                      * //
// * Автор: Крупенников А.В.                                              * //
// *                                                                      * //
// * Версия: 1.0                                                          * //
// * От: 1 jul 2002                                                       * //
// * Расположение: ISM\prog\java\AMFICOMConfigure\com\syrus\AMFICOM\      * //
// *        Client\General\StatusBarModel.java                            * //
// *                                                                      * //
// * Среда разработки: Oracle JDeveloper 3.2.2 (Build 915)                * //
// *                                                                      * //
// * Компилятор: Oracle javac (Java 2 SDK, Standard Edition, ver 1.2.2)   * //
// *                                                                      * //
// * Статус: разработка                                                   * //
// *                                                                      * //
// * Изменения:                                                           * //
// *  Кем         Верс   Когда      Комментарии                           * //
// * -----------  ----- ---------- -------------------------------------- * //
// *                                                                      * //
// * Описание:                                                            * //
// *        класс реализует строку состояния с несколькими полями, каждое * //
// *        поле имеет координаты начала (относительно начала строки) и   * //
// *        ширину. Класс предоставляет возможности по изменению ширины   * //
// *        полей состояния. Строка состояния содержит в себе список полей* //
// *        типа StatusBarField. Добавление поля осуществляется с помощью * //
// *        функции add. Каждое поле имеет порядковый номер (индекс) и    * //
// *        идентификатор. Установка содержимого поля производится        * //
// *        с помощью функции setText с указанием индекса или идентифи-   * //
// *        катора поля и строки. Поля располагаются в строке с помощью   * //
// *        класса LineLayout. Поле со специальным идентификатором time   * //
// *        обрабатывается отдельно - в нем отображается текущее время и  * //
// *        его содержимое постоянно обновляется.                         * //
// *                                                                      * //
//////////////////////////////////////////////////////////////////////////////

package com.syrus.AMFICOM.client.UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;

import com.syrus.AMFICOM.client.event.Dispatcher;
import com.syrus.AMFICOM.client.event.StatusMessageEvent;
import com.syrus.AMFICOM.client.resource.LangModel;



public class StatusBarModel extends JPanel implements PropertyChangeListener
{
	private static final long serialVersionUID = 4049354223311403313L;

	class StatusBarField
	{
		StatusBarModel parent;

		JSeparator separator;
		JTextField label;
		int index;
		int start;
		int size;
		boolean visible;
		String fieldId;

		public StatusBarField(StatusBarModel parent, int index, int start, int size)
		{
			this.parent = parent;

			this.label = new JTextField();
			this.separator = new JSeparator();

			this.visible = true;
			this.index = index;
			this.start = start;
			this.size = size;

			this.fieldId = StatusBarModel.FIELD_PREFIX + String.valueOf(index);

			parent.add(this.label,
					new XYConstraints(start, 1, size - 3, parent.getHeight() - 2));
			parent.add(this.separator,
					new XYConstraints(start + size - 2, 1, 1, parent.getHeight() - 2));

			this.separator.setVisible(true);
			this.separator.setOrientation(SwingConstants.VERTICAL);
			this.separator.addMouseListener(new StatusBarModelSeparatorMouseAdapter(this));
			this.separator.addMouseMotionListener(new StatusBarModelSeparatorMouseMotionAdapter(this));

			this.label.setVisible(true);
			this.label.setDisabledTextColor(Color.black);
			this.label.setEnabled(false);
			this.label.setBackground(parent.getBackground());// new Color(212, 208, 200)
			this.label.setEditable(false);
			this.label.setHorizontalAlignment(SwingConstants.LEFT);
			this.label.setRequestFocusEnabled(false);

			Border border = BorderFactory.createBevelBorder(
					BevelBorder.LOWERED,
					Color.white,
					Color.white,
					new Color(142, 142, 142),
					new Color(99, 99, 99));

			this.label.setBorder(border);
		}
	}
	
	class StatusBarModelSeparatorMouseAdapter extends java.awt.event.MouseAdapter
	{
		StatusBarField adaptee;

		StatusBarModelSeparatorMouseAdapter(StatusBarField adaptee)
		{
			this.adaptee = adaptee;
		}

		public void mouseEntered(MouseEvent e)
		{
			this.adaptee.parent.separatorMouseEntered(e);
		}

		public void mouseExited(MouseEvent e)
		{
			this.adaptee.parent.separatorMouseExited(e);
		}


		public void mousePressed(MouseEvent e)
		{
			this.adaptee.parent.separatorMousePressed(e, this.adaptee);
		}

		public void mouseReleased(MouseEvent e)
		{
			this.adaptee.parent.separatorMouseReleased(e);
		}
	}

	class StatusBarModelSeparatorMouseMotionAdapter extends java.awt.event.MouseMotionAdapter
	{
		StatusBarField adaptee;

		StatusBarModelSeparatorMouseMotionAdapter(StatusBarField adaptee)
		{
			this.adaptee = adaptee;
		}

		public void mouseDragged(MouseEvent e)
		{
			this.adaptee.parent.separatorMouseDragged(e);
		}
	}

	class StatusBarModelThisComponentAdapter extends java.awt.event.ComponentAdapter
	{
		StatusBarModel adaptee;

		StatusBarModelThisComponentAdapter(StatusBarModel adaptee)
		{
			this.adaptee = adaptee;
		}

		public void componentResized(ComponentEvent e)
		{
			this.adaptee.thisComponentResized(e);
		}
	}

	class MyTimeDisplay extends Thread
	{
		StatusBarField sbf = null;
		String curTime;
		String newTime;
		SimpleDateFormat sdf =
			new java.text.SimpleDateFormat("HH:mm:ss");

		public MyTimeDisplay ()
		{
		}

		public MyTimeDisplay (StatusBarField sbf)
		{
			this();
			this.sbf = sbf;
		}

		public void run()
		{
			if(this.sbf == null)
				return;
			this.curTime = this.sdf.format(new Date(System.currentTimeMillis()));
			while (true)
			{
				this.newTime = this.sdf.format(new Date(System.currentTimeMillis()));
				if(!this.curTime.equals(this.newTime))
					this.curTime = this.newTime;
				this.sbf.label.setText(this.curTime);
				try
				{
					sleep(100);
				}
				catch (Exception e)
				{
//					System.out.print("Woke up");
				}
			}
		}
	}
	
	public static final String FIELD_PREFIX = "field";
	public static final String FIELD_DOMAIN = "domain";
	public static final String FIELD_STATUS = "status";
	public static final String FIELD_SERVER = "server";
	public static final String FIELD_SESSION = "session";
	public static final String FIELD_USER = "user";
	public static final String FIELD_TIME = "time";

	private ProgressBar pbar;
	private boolean pbarEnabled = false;

	StatusBarField fields[];
	int fieldNum;
	public static final int MAX_FIELDS = 8;
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	XYLayout xYLayout1 = new XYLayout();

	boolean dragging = false;
	StatusBarField draggingSbf;
	StatusBarField rightSbf;

	public StatusBarModel()
	{
		this.setEnabled(true);
		this.addComponentListener(new StatusBarModelThisComponentAdapter(this));
//		this.setLayout(xYLayout1);
//		this.setLayout(gridBagLayout1);
		this.setLayout(new LineLayout());

//		Border border = BorderFactory.createBevelBorder(
//						BevelBorder.LOWERED,
//						Color.white,
//						Color.white,
//						new Color(142, 142, 142),
//						new Color(99, 99, 99));

	}

	public StatusBarModel(int field_num)
	{
		this();

		int i;

		this.fields = new StatusBarField[MAX_FIELDS];
		this.fieldNum = field_num;

		if(this.fieldNum == 0)
			return;

		int width = this.getWidth();
		int fWidth = width / this.fieldNum;
		int fStart = 0;

		for(i = 0; i < this.fieldNum; i++)
		{
			this.fields[i] = new StatusBarField(this, i, fStart, fWidth);
			fStart += fWidth;
		}
	}

	public void removeDispatcher(Dispatcher dispatcher) {
		if (dispatcher != null) {
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_MESSAGE, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_USER, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_SERVER, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_SESSION, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_DOMAIN, this);
			dispatcher.removePropertyChangeListener(StatusMessageEvent.STATUS_PROGRESS_BAR, this);
		}
	}

	/**
	 * @deprecated use {@link #addDispatcher(Dispatcher) addDispatcher}
	 */
	public void setDispatcher(Dispatcher disp)
	{
		addDispatcher(disp);
	}
	
	public void addDispatcher(Dispatcher disp)
	{
		if(disp != null)
		{
			disp.addPropertyChangeListener(StatusMessageEvent.STATUS_MESSAGE, this);
			disp.addPropertyChangeListener(StatusMessageEvent.STATUS_USER, this);
			disp.addPropertyChangeListener(StatusMessageEvent.STATUS_SERVER, this);
			disp.addPropertyChangeListener(StatusMessageEvent.STATUS_SESSION, this);
			disp.addPropertyChangeListener(StatusMessageEvent.STATUS_DOMAIN, this);
			disp.addPropertyChangeListener(StatusMessageEvent.STATUS_PROGRESS_BAR, this);
		}
	}

	public void setText(int index, String text)
	{
		this.fields[index].label.setText(text);
	}

	public void setText(String fieldId, String text)
	{
		try
		{
			this.fields[getIndex(fieldId)].label.setText(text);
		}
		catch (Exception ex)
		{
			// field not found
		}
	}

	public String getText(int index)
	{
		return this.fields[index].label.getText();
	}

	public String getText(String field_id)
	{
		return this.fields[getIndex(field_id)].label.getText();
	}

	public int getIndex(String field_id)
	{
		int i;
		for(i = 0; i < this.fieldNum; i++)
			if(this.fields[i].fieldId.equals(field_id))
				break;
		if(i >= this.fields.length)
			return -1;
		return i;
	}

	public String getID(int index)
	{
		return this.fields[index].fieldId;
	}

	public void setID(int index, String field_id)
	{
		this.fields[index].fieldId = field_id;
	}

	public void setIndex(int index, int old_index)
	{
		int i;
		StatusBarField sbf = this.fields[old_index];

		if(old_index > index)
		{
			for(i = old_index; i > index; i--)
				this.fields[i] = this.fields[i - 1];
		}
		else
		{
			for(i = old_index; i < index; i++)
				this.fields[i] = this.fields[i + 1];
		}

		this.fields[index] = sbf;
	}

	public void setIndex(int index, String field_id)
	{
		setIndex(index, getIndex(field_id));
	}

	public void add(int index, String field_id)
	{
		int i;

		for(i = this.fieldNum; i > index; i--)
			this.fields[i] = this.fields[i - 1];
		this.fieldNum++;

		this.fields[index] = new StatusBarField(this, index, 0, getHeight() - 2);
		this.fields[index].fieldId = field_id;

		distribute();

		if(field_id.equals(StatusBarModel.FIELD_TIME))
		{
			new MyTimeDisplay(this.fields[index]).start();
		}
	}

	public void add(int index)
	{
		add(index, FIELD_PREFIX + String.valueOf(this.fieldNum));
	}

	public void add(String field_id)
	{
		add(this.fieldNum, field_id);
	}

	public void add()
	{
		add(this.fieldNum);
	}

	public void remove(int index)
	{
		for(int i = this.fieldNum; i > index; i--)
			this.fields[i] = this.fields[i - 1];
		this.fieldNum--;
	}

	public void remove(String field_id)
	{
		remove(getIndex(field_id));
	}

	public void setWidth(int index, int width)
	{		
		this.fields[index].size = width;
		this.fields[index].label.setSize(this.fields[index].size, getHeight() - 2);
	}

	public void setWidth(String field_id, int width)
	{
		setWidth(getIndex(field_id), width);
	}

	public void setStart(int index, int start)
	{
		this.fields[index].start = start;
		this.fields[index].label.setLocation(this.fields[index].start, 1);
		this.fields[index].separator.setLocation(
				this.fields[index].start + this.fields[index].size - 2, 1);
	}

	public void setStart(String field_id, int start)
	{
		setStart(getIndex(field_id), start);
	}

	public void setVisible(boolean aFlag)
	{
//		super.setVisible(aFlag);
//		System.out.println("set visible " + aFlag);
//		if(aFlag)
//			distribute();
	}

	public void distribute()
	{
		if(this.fieldNum == 0)
			return;

		//int i;
		int width = getWidth();
		int fWidth = width / this.fieldNum;
//		int f_start = 0;
		int fStart = 0;
		if (this.pbarEnabled)
			fStart = this.pbar.getWidth() + 3;

		for(int i = 0; i < this.fieldNum; i++)
		{
			if(i == this.fieldNum - 1)
				fWidth = getWidth() - fStart;
			setWidth(i, fWidth);
			setStart(i, fStart);
			this.fields[i].label.setBounds(
					this.fields[i].start,
					1,
					fWidth - 3,
					getHeight() - 2);
			this.fields[i].separator.setBounds(
					this.fields[i].start + fWidth - 2,
					1,
					1,
					getHeight() - 2);
//			System.out.println("draw " + this.fields[i].field_id + " at " +
//					String.valueOf(f_start) + " width " +
//					String.valueOf(f_width) + " height " +
//					String.valueOf(this.fields[i].label.getHeight()));
			fStart += fWidth;
		}
	}

	public void organize()
	{
		if(this.fieldNum == 0)
			return;

		int fStart = this.fields[0].start;
		for(int i = 0; i < this.fieldNum; i++)
		{
			if(i == this.fieldNum - 1)
				setWidth(i, getWidth() - fStart);
			setStart(i, fStart);
			this.fields[i].label.setBounds(
					this.fields[i].start,
					1,
					this.fields[i].size - 3,
					getHeight() - 2);
			this.fields[i].separator.setBounds(
					this.fields[i].start + this.fields[i].size - 2,
					1,
					1,
					getHeight() - 2);
			fStart += this.fields[i].size;
		}
	}

	void separatorMouseEntered(MouseEvent e)
	{
		setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
//		System.out.println("mouse Enter");
	}

	void separatorMouseExited(MouseEvent e)
	{
		setCursor(Cursor.getDefaultCursor());
//		System.out.println("mouse Exit");
	}

	void separatorMousePressed(MouseEvent e, StatusBarField sbf)
	{
//		System.out.println("mouse pressed on ..."+ String.valueOf(sbf.index));
		if(sbf.index == this.fieldNum - 1)
			return;
		this.dragging = true;
		this.draggingSbf = sbf;
		this.rightSbf = null;
//		if(sbf.index != this.field_num - 2)
//		{
			this.rightSbf = this.fields[sbf.index + 1];
//			System.out.println("start dragging " + String.valueOf(sbf.index) +
//				" and " + String.valueOf(right_sbf.index));
//		}
//		else
//			System.out.println("start dragging " + String.valueOf(sbf.index));
	}

	void separatorMouseReleased(MouseEvent e)
	{
		this.dragging = false;
//		System.out.println("mouse Released");
	}

	void separatorMouseDragged(MouseEvent e)
	{
		if(!this.dragging)
			return;
//		System.out.println("mouse Dragged to X: " + e.getX() + "  Y: " + e.getY());
		int diff = e.getX();// - dragging_sbf.separator.getX();

//		System.out.println("offset bounds for " + dragging_sbf.field_id +
//				" at diff " + String.valueOf(diff));
//				" at X: " + x + "  Y: " + y +
//				" Height: " + currentComponent.getHeight() +
//				"   Width: " + currentComponent.getWidth());
//		if(diff > 0)
//		{
			setWidth(
					this.draggingSbf.fieldId,
					this.draggingSbf.size + diff);
			this.draggingSbf.separator.setBounds(
					this.draggingSbf.start + this.draggingSbf.size - 2,
					1,
					1,
					getHeight() - 2);
			this.draggingSbf.label.setBounds(
					this.draggingSbf.start,
					1,
					this.draggingSbf.size - 3,
					getHeight() - 2);

			setStart(
					this.rightSbf.fieldId,
					this.rightSbf.start + diff);
			setWidth(
					this.rightSbf.fieldId,
					this.rightSbf.size - diff);
			this.rightSbf.label.setBounds(
					this.rightSbf.start,
					1,
					this.rightSbf.size - 3,
					getHeight() - 2);
			this.rightSbf.separator.setBounds(
					this.rightSbf.start + this.rightSbf.size - 2,
					1,
					1,
					getHeight() - 2);
//		}
//		else
//		{
//		}
	}

	void thisComponentResized(ComponentEvent e)
	{
//		distribute();
		organize();
	}

	public void propertyChange(PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		if (evt instanceof StatusMessageEvent) {
			StatusMessageEvent sme = (StatusMessageEvent) evt;
			if (propertyName.equals(StatusMessageEvent.STATUS_MESSAGE)) {
				setText(StatusBarModel.FIELD_STATUS, sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_SESSION)) {
				setText(StatusBarModel.FIELD_SESSION, sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_SERVER)) {
				setText(StatusBarModel.FIELD_SERVER, sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_USER)) {
				setText(StatusBarModel.FIELD_USER, sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_DOMAIN)) {
				setText(StatusBarModel.FIELD_DOMAIN, sme.getText());
			} else if (propertyName.equals(StatusMessageEvent.STATUS_PROGRESS_BAR)) {
				this.setProgressBarEnable(sme.isShowProgressBar());
			}

		}

	}
	
	
	/**
	 * @deprecated use setProgressBarEnable
	 * @param en
	 */
	public void enableProgressBar (boolean en){
		this.setProgressBarEnable(en);
	}

	public void setProgressBarEnable (boolean enable)
	{
		if (enable)
		{
			this.pbar = new ProgressBar();
			this.pbarEnabled = true;
			this.distribute();
			this.add(
						this.pbar,
						new XYConstraints(
								3,
								(this.getHeight() - this.pbar.getHeight()) / 2 + 2,
								this.pbar.getWidth(),
								this.pbar.getHeight()));

			this.pbar.start(LangModel.getString("Loading_Please_wait"));
		}
		else
		{
			if (this.pbar != null){
				this.pbar.stop();
				this.pbarEnabled = false;
				this.distribute();
				this.remove(this.pbar);
				this.pbar = null;
			}
		}
	}
}



