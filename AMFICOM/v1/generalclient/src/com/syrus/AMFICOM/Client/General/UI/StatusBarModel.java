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

package com.syrus.AMFICOM.Client.General.UI;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridBagLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;

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

import com.syrus.AMFICOM.Client.General.Event.*;
import com.syrus.AMFICOM.Client.General.Lang.LangModel;

class StatusBarField
{
	StatusBarModel parent;

	JSeparator separator;
	JTextField label;
	int index;
	int start;
	int size;
	boolean visible;
	String field_id;

	public StatusBarField(StatusBarModel parent, int index, int start, int size)
	{
		this.parent = parent;

		label = new JTextField();
		separator = new JSeparator();

		this.visible = true;
		this.index = index;
		this.start = start;
		this.size = size;

		this.field_id = "field" + String.valueOf(index);

		parent.add(label,
				new XYConstraints(start, 1, size - 3, parent.getHeight() - 2));
		parent.add(separator,
				new XYConstraints(start + size - 2, 1, 1, parent.getHeight() - 2));

		separator.setVisible(true);
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.addMouseListener(new StatusBarModel_separator_mouseAdapter(this));
		separator.addMouseMotionListener(new StatusBarModel_separator_mouseMotionAdapter(this));

		label.setVisible(true);
		label.setDisabledTextColor(Color.black);
		label.setEnabled(false);
		label.setBackground(parent.getBackground());// new Color(212, 208, 200)
		label.setEditable(false);
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setRequestFocusEnabled(false);

		Border border = BorderFactory.createBevelBorder(
				BevelBorder.LOWERED,
				Color.white,
				Color.white,
				new Color(142, 142, 142),
				new Color(99, 99, 99));

		label.setBorder(border);
	}
}

public class StatusBarModel extends JPanel implements OperationListener
{
	public static String field_status = "status";
	public static String field_server = "server";
	public static String field_session = "session";
	public static String field_user = "user";
	public static String field_time = "time";

	public static String FIELD_DOMAIN = "domain";

	private ProgressBar pbar;
	private boolean pbarEnabled = false;

	StatusBarField fields[];
	int field_num;
	static public final int max_fields = 8;
	GridBagLayout gridBagLayout1 = new GridBagLayout();
	XYLayout xYLayout1 = new XYLayout();

	boolean dragging = false;
	StatusBarField dragging_sbf;
	StatusBarField right_sbf;

	public StatusBarModel()
	{
		super();
		try
		{
			jbInit();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public StatusBarModel(int field_num)
	{
		this();

		int i;

		this.fields = new StatusBarField[max_fields];
		this.field_num = field_num;

		if(this.field_num == 0)
			return;

		int width = this.getWidth();
		int f_width = width / this.field_num;
		int f_start = 0;

		for(i = 0; i < this.field_num; i++)
		{
			this.fields[i] = new StatusBarField(this, i, f_start, f_width);
			f_start += f_width;
		}
	}

	public void removeDispatcher(Dispatcher disp)
	{
		if(disp != null)
		{
			disp.unregister(this, StatusMessageEvent.STATUS_MESSAGE);
			disp.unregister(this, StatusMessageEvent.STATUS_USER);
			disp.unregister(this, StatusMessageEvent.STATUS_SERVER);
			disp.unregister(this, StatusMessageEvent.STATUS_SESSION);
			disp.unregister(this, StatusMessageEvent.STATUS_DOMAIN);
			disp.unregister(this, StatusMessageEvent.STATUS_PROGRESS_BAR);
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
			disp.register(this, StatusMessageEvent.STATUS_MESSAGE);
			disp.register(this, StatusMessageEvent.STATUS_USER);
			disp.register(this, StatusMessageEvent.STATUS_SERVER);
			disp.register(this, StatusMessageEvent.STATUS_SESSION);
			disp.register(this, StatusMessageEvent.STATUS_DOMAIN);
			disp.register(this, StatusMessageEvent.STATUS_PROGRESS_BAR);
		}
	}

	public void setText(int index, String text)
	{
		this.fields[index].label.setText(text);
	}

	public void setText(String field_id, String text)
	{
		try
		{
			this.fields[getIndex(field_id)].label.setText(text);
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
		for(i = 0; i < this.field_num; i++)
			if(this.fields[i].field_id.equals(field_id))
				break;
		if(i >= this.fields.length)
			return -1;
		return i;
	}

	public String getID(int index)
	{
		return this.fields[index].field_id;
	}

	public void setID(int index, String field_id)
	{
		this.fields[index].field_id = field_id;
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

		for(i = this.field_num; i > index; i--)
			this.fields[i] = this.fields[i - 1];
		this.field_num++;

		this.fields[index] = new StatusBarField(this, index, 0, getHeight() - 2);
		this.fields[index].field_id = field_id;

		distribute();

		if(field_id.equals(StatusBarModel.field_time))
		{
			new MyTimeDisplay(this.fields[index]).start();
		}
	}

	public void add(int index)
	{
		add(index, "field" + String.valueOf(this.field_num));
	}

	public void add(String field_id)
	{
		add(this.field_num, field_id);
	}

	public void add()
	{
		add(this.field_num);
	}

	public void remove(int index)
	{
		for(int i = this.field_num; i > index; i--)
			this.fields[i] = this.fields[i - 1];
		this.field_num--;
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

	private void jbInit() throws Exception
	{
		this.setEnabled(true);
		this.addComponentListener(new StatusBarModel_this_componentAdapter(this));
//		this.setLayout(xYLayout1);
//		this.setLayout(gridBagLayout1);
		this.setLayout(new LineLayout());

		Border border = BorderFactory.createBevelBorder(
						BevelBorder.LOWERED,
						Color.white,
						Color.white,
						new Color(142, 142, 142),
						new Color(99, 99, 99));
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
		if(this.field_num == 0)
			return;

		//int i;
		int width = getWidth();
		int f_width = width / this.field_num;
//		int f_start = 0;
		int f_start = 0;
		if (pbarEnabled)
			f_start = pbar.getWidth() + 3;

		for(int i = 0; i < this.field_num; i++)
		{
			if(i == this.field_num - 1)
				f_width = getWidth() - f_start;
			setWidth(i, f_width);
			setStart(i, f_start);
			this.fields[i].label.setBounds(
					this.fields[i].start,
					1,
					f_width - 3,
					getHeight() - 2);
			this.fields[i].separator.setBounds(
					this.fields[i].start + f_width - 2,
					1,
					1,
					getHeight() - 2);
//			System.out.println("draw " + this.fields[i].field_id + " at " +
//					String.valueOf(f_start) + " width " +
//					String.valueOf(f_width) + " height " +
//					String.valueOf(this.fields[i].label.getHeight()));
			f_start += f_width;
		}
	}

	public void organize()
	{
		if(this.field_num == 0)
			return;

		int f_start = this.fields[0].start;
		for(int i = 0; i < this.field_num; i++)
		{
			if(i == this.field_num - 1)
				setWidth(i, getWidth() - f_start);
			setStart(i, f_start);
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
			f_start += this.fields[i].size;
		}
	}

	void separator_mouseEntered(MouseEvent e)
	{
		setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
//		System.out.println("mouse Enter");
	}

	void separator_mouseExited(MouseEvent e)
	{
		setCursor(Cursor.getDefaultCursor());
//		System.out.println("mouse Exit");
	}

	void separator_mousePressed(MouseEvent e, StatusBarField sbf)
	{
//		System.out.println("mouse pressed on ..."+ String.valueOf(sbf.index));
		if(sbf.index == this.field_num - 1)
			return;
		dragging = true;
		dragging_sbf = sbf;
		right_sbf = null;
//		if(sbf.index != this.field_num - 2)
//		{
			right_sbf = this.fields[sbf.index + 1];
//			System.out.println("start dragging " + String.valueOf(sbf.index) +
//				" and " + String.valueOf(right_sbf.index));
//		}
//		else
//			System.out.println("start dragging " + String.valueOf(sbf.index));
	}

	void separator_mouseReleased(MouseEvent e)
	{
		dragging = false;
//		System.out.println("mouse Released");
	}

	void separator_mouseDragged(MouseEvent e)
	{
		if(!dragging)
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
					dragging_sbf.field_id,
					dragging_sbf.size + diff);
			dragging_sbf.separator.setBounds(
					dragging_sbf.start + dragging_sbf.size - 2,
					1,
					1,
					getHeight() - 2);
			dragging_sbf.label.setBounds(
					dragging_sbf.start,
					1,
					dragging_sbf.size - 3,
					getHeight() - 2);

			setStart(
					right_sbf.field_id,
					right_sbf.start + diff);
			setWidth(
					right_sbf.field_id,
					right_sbf.size - diff);
			right_sbf.label.setBounds(
					right_sbf.start,
					1,
					right_sbf.size - 3,
					getHeight() - 2);
			right_sbf.separator.setBounds(
					right_sbf.start + right_sbf.size - 2,
					1,
					1,
					getHeight() - 2);
//		}
//		else
//		{
//		}
	}

	void this_componentResized(ComponentEvent e)
	{
//		distribute();
		organize();
	}

	public void operationPerformed(OperationEvent oe)
	{
		try 
		{
			StatusMessageEvent sme = (StatusMessageEvent )oe;
			if(sme.getActionCommand().equals(StatusMessageEvent.STATUS_MESSAGE))
			{
				setText(StatusBarModel.field_status, sme.getText());
			}
			else
			if(sme.getActionCommand().equals(StatusMessageEvent.STATUS_SESSION))
			{
				setText(StatusBarModel.field_session, sme.getText());
			}
			else
			if(sme.getActionCommand().equals(StatusMessageEvent.STATUS_SERVER))
			{
				setText(StatusBarModel.field_server, sme.getText());
			}
			else
			if(sme.getActionCommand().equals(StatusMessageEvent.STATUS_USER))
			{
				setText(StatusBarModel.field_user, sme.getText());
			}
			else
			if(sme.getActionCommand().equals(StatusMessageEvent.STATUS_DOMAIN))
			{
				setText(StatusBarModel.FIELD_DOMAIN, sme.getText());
			}
			else
			if(sme.getActionCommand().equals(StatusMessageEvent.STATUS_PROGRESS_BAR))
			{
				this.setEnableProgressBar(sme.getShowProgressBar());
			}
			
		} 
		catch (Exception ex) 
		{
//			System.out.println("this is not a StatusMessageEvent so don't bother");
		} 
		
	}
	
	
	/**
	 * @deprecated use setEnableProgressBar
	 * @param en
	 */
	public void enableProgressBar (boolean en){
		this.setEnableProgressBar(en);
	}

	public void setEnableProgressBar (boolean en)
	{
		if (en)
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


class StatusBarModel_separator_mouseAdapter extends java.awt.event.MouseAdapter
{
	StatusBarField adaptee;

	StatusBarModel_separator_mouseAdapter(StatusBarField adaptee)
	{
		this.adaptee = adaptee;
	}

	public void mouseEntered(MouseEvent e)
	{
		adaptee.parent.separator_mouseEntered(e);
	}

	public void mouseExited(MouseEvent e)
	{
		adaptee.parent.separator_mouseExited(e);
	}


	public void mousePressed(MouseEvent e)
	{
		adaptee.parent.separator_mousePressed(e, adaptee);
	}

	public void mouseReleased(MouseEvent e)
	{
		adaptee.parent.separator_mouseReleased(e);
	}
}

class StatusBarModel_separator_mouseMotionAdapter extends java.awt.event.MouseMotionAdapter
{
	StatusBarField adaptee;

	StatusBarModel_separator_mouseMotionAdapter(StatusBarField adaptee)
	{
		this.adaptee = adaptee;
	}

	public void mouseDragged(MouseEvent e)
	{
		adaptee.parent.separator_mouseDragged(e);
	}
}

class StatusBarModel_this_componentAdapter extends java.awt.event.ComponentAdapter
{
	StatusBarModel adaptee;

	StatusBarModel_this_componentAdapter(StatusBarModel adaptee)
	{
		this.adaptee = adaptee;
	}

	public void componentResized(ComponentEvent e)
	{
		adaptee.this_componentResized(e);
	}
}

class MyTimeDisplay extends Thread
{
	StatusBarField sbf = null;
	String cur_time;
	String new_time;
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
		if(sbf == null)
			return;
		cur_time = sdf.format(new Date(System.currentTimeMillis()));
		while (true)
		{
			new_time = sdf.format(new Date(System.currentTimeMillis()));
			if(!cur_time.equals(new_time))
				cur_time = new_time;
			sbf.label.setText(cur_time);
			try
			{
				sleep(100);
			}
			catch (Exception e)
			{
//				System.out.print("Woke up");
			}
		}
	}
}
