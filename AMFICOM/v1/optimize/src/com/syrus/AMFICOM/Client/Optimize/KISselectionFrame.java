package com.syrus.AMFICOM.Client.Optimize;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;

import oracle.jdeveloper.layout.*;
import com.syrus.AMFICOM.Client.General.UI.*;
import com.syrus.AMFICOM.Client.Resource.ISMDirectory.*;
import com.syrus.AMFICOM.Client.Resource.Network.*;
import com.syrus.AMFICOM.Client.Resource.NetworkDirectory.*;
import com.syrus.io.*;
import com.syrus.AMFICOM.Client.General.Command.Optimize.*;

// окно задания цен на оборудование и длины волны тестирования
//==================================================================================================================
public class KISselectionFrame extends JInternalFrame
{
	JPanel jPanel1 = new JPanel(); JPanel jPanel2 = new JPanel(); JPanel jPanel3 = new JPanel();
  JLabel jLabel5 = new JLabel(); JLabel jLabel6 = new JLabel();
  JTextField jTextField2 = new JTextField();
	JTextField jTextField3 = new JTextField(); JTextField jTextField4 = new JTextField();

	private BorderLayout borderLayout1 = new BorderLayout();// to check if something was changed
  //-------------------------------------
	public Hashtable ht; // содеражит всю необходимую информацию об оборудовании

	boolean fileOpened    = false; // to check if a file was opened or not
	String fileName       = "";
	boolean dataEdited    = false;
	int _width;
	String filePath            = "D:/Java/NetOptimisation/Data";// путь, открываемый по умолчанию
	Vector reflectometers      = new Vector();// reflectometers' vector
	Vector reflectometers2show = new Vector();// рефлектометры, которые мы будем показывать
	Vector switchers           = new Vector();// switchers' vector
	OpticalOptimizerContext optOptContext;    // контекст оптической оптимизации
	OptimizeMDIMain mdiMain;                  // главное окно

	boolean newFileOpened = false;   // to check if the new file was opened or not
	boolean prices_exist = false;	 // флаг того, были ли заданы цены на оборудование
	String newFileName    = "";
	File workFile;
  Border border1;
  TitledBorder titledBorder1;
  Border border2;
  TitledBorder titledBorder2;
  GridBagLayout gridBagLayout2 = new GridBagLayout();
  AComboBox jComboBox1 = new AComboBox();
  JLabel jLabel2 = new JLabel();
  AComboBox jComboBox_wave = new AComboBox();
  JLabel jLabel3 = new JLabel();
  JTextField jTextField1 = new JTextField();
  JLabel jLabel4 = new JLabel();
  AComboBox jComboBox2 = new AComboBox();
  GridBagLayout gridBagLayout1 = new GridBagLayout();
	//-------------------------------------------------------------------------------------------------------------
	public KISselectionFrame()
  { try
    { jbInit();
    }
    catch (Exception e)
    {e.printStackTrace();
    }
  }
	//-------------------------------------------------------------------------------------------------------------
	// на вход хэштейбл из кисов( лазеры + коммутаторы )
	public KISselectionFrame(OpticalOptimizerContext optOptContext, Hashtable ht, OptimizeMDIMain mdiMain)
	{this.ht = ht;// это надо запомнить для reassignComboList();
	 this.optOptContext = optOptContext;// чтобы установить флаги состояния (утановлена ли длина волны и т.д.)
	 this.mdiMain = mdiMain;

	 // инициализируем комбобокс
	 jComboBox_wave.addItem("все");
	 jComboBox_wave.addItem("850");	 jComboBox_wave.addItem("1300"); jComboBox_wave.addItem("1310");
	 jComboBox_wave.addItem("1550"); jComboBox_wave.addItem("1625");

	 String rn  = "";  // reflectometer name
	 String sn  = "";  // switcher name
	 String sID = "";  // switch. ID
	 String rID = "";  // refl. ID
	 String sp  = "";  // switch. price
	 String rp  = "";  // refl. price
	 String spn = "";  // switch. ports number
	 Vector rdd = new Vector();// refl. dynamical diapazons
	 Vector rwl = new Vector();//refl.wavelengths
	 for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
	 { rn = ""; sn = "";
           EquipmentType kistype = (EquipmentType)enum.nextElement();
	   //System.out.println(kistype.id + " " + kistype.eq_class);

     // вектор записей о рефлектометрах и их свойствах
	   if(kistype != null)
	   { if(kistype.eq_class.equals("switch")) //checker, if the KIS type is switcher
       { sn = kistype.name; //getting of name
         //System.out.print(sn);
         sID = kistype.id; // getting of ID
         Hashtable ht2 = kistype.characteristics;
         for(Enumeration enum2 = ht2.elements(); enum2.hasMoreElements(); )
         { Characteristic ch = (Characteristic)enum2.nextElement();
           if(ch.type_id.equals("Channel_number"))
           { spn = (ch.value); //getting of port number
           }
         }
         switchers.add(new SwitcherShortData(sID, sn, spn, sp)); //filling of the appropriate wector with switchers' data
       }
      else if(kistype.eq_class.equals("tester")) // checker, if KIS type is REFL.
      { rn = kistype.name; //getting its name
        //System.out.print(rn);
        rID = kistype.id;  //getting its ID
        Hashtable ht2 = kistype.characteristics;
        rdd = new Vector();//в clear() вера подорвана ...
        rwl = new Vector();
		    for(Enumeration enum2 = ht2.elements(); enum2.hasMoreElements(); )
		    { Characteristic ch = (Characteristic )enum2.nextElement();
		      if( ch.type_id.equals("Diapazon_850") )
		      { // rdd - reflectometer dynamic diapazon, rwl - refl wavelength
            rdd.add((ch.value)); // adding the dynamical area to the list
            rwl.add("850");      // adding to the list the wavelength for dynamic area above
		      }
		      if( ch.type_id.equals("Diapazon_1300") )
		      { rdd.add((ch.value));
            rwl.add("1300");
		      }
		      if( ch.type_id.equals("Diapazon_1310") )
		      { rdd.add((ch.value));
            rwl.add("1310");
		      }
		      if( ch.type_id.equals("Diapazon_1550") )
		      { rdd.add((ch.value));
            rwl.add("1550");
		      }
		      if( ch.type_id.equals("Diapazon_1625") )
		      {  rdd.add((ch.value));
             rwl.add("1625");
		      }
		    }
		    reflectometers.add(new ReflectometerShortData(rID, rn, rwl, rdd, rp));//filling of the appropriate vector with reflectometers' data
		  }
    }
	 }// for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
	 String tmp;
	 for(int i=0; i<reflectometers.size(); i++)
	 {   tmp = ( (ReflectometerShortData)(reflectometers.elementAt(i)) ).reflectometerName;
			 jComboBox1.addItem(tmp); // Adding the refl. name to ComboBox
	 }
	 for(int i=0; i<switchers.size(); i++)
	 {   tmp = ( (SwitcherShortData)(switchers.elementAt(i)) ).switcherName;
			 jComboBox2.addItem(tmp); // Adding the switch. name to ComboBox
	 }

	 try
   { jbInit();
   }
	 catch(Exception e)
   {e.printStackTrace();
   }
	}
	//-------------------------------------------------------------------------------------------------------------
	//  расположить в верхнем правом углу
	public void place()
	{  Dimension dim = mdiMain.scrollPane.getSize();
		 int width = (int)(0.22*dim.width), height = 190;//height = getMinimumSize().height;
		 setBounds( dim.width - width, 0, width, height );
		 setVisible(true);
     // устанавливаем размер шрифта таким, чтобы он полностью помещался по высоте в комбобоксы
     jComboBox1.setFont(getFont().deriveFont( (float)(jComboBox1.getHeight()-8) ));
     jComboBox2.setFont(getFont().deriveFont( (float)(jComboBox2.getHeight()-8) ));
     jComboBox_wave.setFont(getFont().deriveFont( (float)(jComboBox_wave.getHeight()-8) ));
	}
	//-------------------------------------------------------------------------------------------------------------
	private void jbInit() throws Exception
	{ border1 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder1 = new TitledBorder(border1,"Оптический коммутатор");
    border2 = BorderFactory.createEtchedBorder(Color.white,new Color(148, 145, 140));
    titledBorder2 = new TitledBorder(border2,"Оптический рефлектометр");
    this.setFrameIcon( new ImageIcon(Toolkit.getDefaultToolkit().getImage("images/main/general.gif")) );
		setClosable(true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);// не закрываем, а прячем

		this.addComponentListener(new java.awt.event.ComponentAdapter()
		{	public void componentResized(ComponentEvent e)
			{	this_componentResized(e);
			}
		});
		this.getContentPane().setLayout(new BorderLayout());
		jPanel1.setLayout(borderLayout1);
		jPanel2.setLayout(gridBagLayout2);
		jPanel2.setBorder(titledBorder2);
		jPanel2.setMinimumSize(new Dimension(258, 95));
		jPanel2.setPreferredSize(new Dimension(258, 95));
		jPanel3.setBorder(titledBorder1);
		jPanel3.setMinimumSize(new Dimension(293, 80));
		jPanel3.setPreferredSize(new Dimension(293, 80));
		jPanel3.setLayout(gridBagLayout1);
		jLabel5.setRequestFocusEnabled(true);
    jLabel5.setText("Цена:");
		jLabel6.setText("Число портов:");
		jTextField3.setMinimumSize(new Dimension(64, 21)); jTextField3.setPreferredSize(new Dimension(64, 21));
		jTextField3.setEditable(false);
		jTextField4.setMinimumSize(new Dimension(64, 21));
		jTextField4.setPreferredSize(new Dimension(64, 21));
		jTextField4.addFocusListener(new java.awt.event.FocusAdapter()
		{ public void focusLost(FocusEvent e)
			{	jTextField4_focusLost(e);
			}
		});
		jTextField2.setMinimumSize(new Dimension(64, 21));
		jTextField2.setPreferredSize(new Dimension(64, 21));
		jTextField2.addFocusListener(new java.awt.event.FocusAdapter()
		{	public void focusLost(FocusEvent e)
			{	jTextField2_focusLost(e);
			}
		});
		jTextField2.addActionListener(new java.awt.event.ActionListener()
		{ public void actionPerformed(ActionEvent e)
			{ jTextField2_actionPerformed(e);
			}
		});
		this.setIconifiable(true);
		this.setNormalBounds(new Rectangle(0, 0, 280, 190));
				this.setResizable(true);
		this.setMaximumSize(new Dimension(1024, 190));
		this.setMinimumSize(new Dimension(272, 190));
		this.setPreferredSize(new Dimension(380, 190));
		this.addInternalFrameListener(new javax.swing.event.InternalFrameAdapter()
		{	public void internalFrameClosing(InternalFrameEvent e)
			{	this_internalFrameClosing(e);
			}
		});
    jComboBox1.setAlignmentX((float) 0.0);
    jComboBox1.setAlignmentY((float) 0.0);
    jComboBox1.setAutoscrolls(false);
    jComboBox1.setMaximumSize(new Dimension(213, 20));
    jComboBox1.setMinimumSize(new Dimension(213, 20));
    jComboBox1.setNextFocusableComponent(jTextField2);
    jComboBox1.setOpaque(true);
    jComboBox1.setPreferredSize(new Dimension(213, 20));
    jComboBox1.addActionListener(new java.awt.event.ActionListener()
		{ public void actionPerformed(ActionEvent e)
			{ jComboBox1_actionPerformed(e);
			}
		});
    jComboBox1.setToolTipText("\"\"");
    jComboBox1.setMaximumRowCount(8);
    jComboBox1.setPopupVisible(false);
    jComboBox1.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				jComboBox1_itemStateChanged(e);
			}
		});
    jLabel2.setText("Длина волны:");
    jComboBox_wave.addItemListener(new java.awt.event.ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				jComboBox_wave_itemStateChanged(e);
			}
		});
    jComboBox_wave.setPopupVisible(false);
    jLabel3.setText("Диапазон:");
    jTextField1.setMinimumSize(new Dimension(64, 21));
    jTextField1.setPreferredSize(new Dimension(64, 21));
    jTextField1.setEditable(false);
    jLabel4.setRequestFocusEnabled(true);
    jLabel4.setText("Цена:");
    jComboBox2.setNextFocusableComponent(jTextField4);
    jComboBox2.addActionListener(new java.awt.event.ActionListener()
		{ public void actionPerformed(ActionEvent e)
			{ jComboBox2_actionPerformed(e);
			}
		});
    jComboBox2.setToolTipText("\"\"");
    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    jPanel2.add( jComboBox1, new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE,
                 GridBagConstraints.REMAINDER, 1, 1.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL,
                 new Insets(0, 0, 0, 0), 0, 0) );
    jPanel2.add(jLabel2, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 3), 0, 0));
    jPanel2.add(jComboBox_wave, new GridBagConstraints(1, 1, 3, GridBagConstraints.RELATIVE, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel2.add(jLabel3, new GridBagConstraints(0, 2, 1, GridBagConstraints.REMAINDER, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 3), 0, 0));
    jPanel2.add(jTextField1, new GridBagConstraints(1, 2, 1, GridBagConstraints.REMAINDER, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 2), 0, 0));
    jPanel2.add(jLabel4, new GridBagConstraints(2, 2, GridBagConstraints.RELATIVE, GridBagConstraints.REMAINDER, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 3), 0, 0));
    jPanel2.add(jTextField2, new GridBagConstraints(3, 2, GridBagConstraints.REMAINDER, GridBagConstraints.REMAINDER, 1.0, 1.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(1, 1, 0, 1), 0, 0));
		jPanel1.add(jPanel3,  BorderLayout.CENTER);
    jPanel3.add(jComboBox2, new GridBagConstraints(GridBagConstraints.RELATIVE, GridBagConstraints.RELATIVE, 4, 1, 1.0, 0.0
            ,GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel3.add(jTextField3,new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel3.add(jTextField4, new GridBagConstraints(3, 1, 1, 1, 1.0, 0.0
            ,GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
    jPanel3.add(jLabel5, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 5, 0, 3), 0, 0));
    jPanel3.add(jLabel6, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
            ,GridBagConstraints.EAST, GridBagConstraints.NONE, new Insets(0, 0, 0, 3), 0, 0));
		jPanel1.add(jPanel2, BorderLayout.NORTH);

		//jMenuBar1.setBorder(BorderFactory.createEtchedBorder()); //это вызовем снаружи
		this.setBounds(0,0,330, 280);

		//this.showReflectometerData(this.jComboBox1.getSelectedIndex());заменяем поиском по строке , а не по индексу
		this.showReflectometerData(this.jComboBox1.getSelectedItem().toString());
		this.showSwitcherData(this.jComboBox2.getSelectedIndex());
		setToolTipText("");
		this.setTitle("файл цен не выбран");
		setMenuHightlight(false);

		_width = this.getWidth();
  }
	//----------------------------------------------------------------------------------------------------------------
	// если надо отфильтровать по длине волны (key), то фильтруем
	public void reassignComboList(String key)
	{
		String rn  = "";  // reflectometer name
		String sn  = "";  // switcher name
		String sID = "";  // switch. ID
		String rID = "";  // refl. ID
		String sp  = "";  // switch. price
		String rp  = "";  // refl. price
		String spn = "";  // switch. ports number
		String rdd = "";  // refl. dynamical diapazon

		reflectometers2show = new Vector();// очищаем предыдущие записи о рефлектометрах
		jComboBox1.removeAllItems();  // очищаем комбобокс

		for(Enumeration enum = ht.elements(); enum.hasMoreElements();)
		{
			rn = ""; sn = "";
			EquipmentType kistype = (EquipmentType)enum.nextElement();
			//System.out.println(kistype.id + " " + kistype.eq_class + "\n");
			if(kistype != null)
			{  if(kistype.eq_class.equals("tester")) // checker, if KIS type is REFL.
				 {   rn = kistype.name; //getting its name
						 rID = kistype.id;  //getting its ID
						 Hashtable ht2 = kistype.characteristics;
						 String full_key = "Diapazon_"+key;

						 for(Enumeration enum2 = ht2.elements(); enum2.hasMoreElements();)//по всем характеристикам
						 {  Characteristic ch = (Characteristic) enum2.nextElement();
								if( ch.type_id.equals(full_key) || ( key.equals("все") && ch.type_id.startsWith("Diapazon_") ))// если выбрано "все" , то все и выводим вне зависимости от длины волны
								{ rdd = (ch.value); //  если не указана длина волны, то показываем первый попавшийся динамич диапазон
									if( !rn.equals("") )
									{  reflectometers2show.add(new ReflectometerShownData(rID, rn, rdd, rp));//filling of the appropriate wector with reflectometers' data
										 break;//ht2
									}
								}
						 }
				 }
			}
		}//for(Enumeration enum = ht.elements(); enum.hasMoreElements();)

		String tmp;
		for(int i=0; i<reflectometers2show.size(); i++)
		{   tmp = ( (ReflectometerShownData)(reflectometers2show.elementAt(i)) ).reflectometerName;
				jComboBox1.addItem(tmp); // Adding the refl. name to ComboBox
		}
	}
	//---------------------------------------------------------------------------------------------------------------
	void this_componentResized(ComponentEvent e)
	{
		int width = this.getWidth();
		_width = width;
	}
	//----------------------------------------------------------------------------------------------------------------
	void showSwitcherData(int index) // To show data about switchers
	{
		if(index<0) return;
		String portNumb;
		portNumb = ((SwitcherShortData)(switchers.elementAt(index))).portNumber;
		jTextField3.setText(portNumb);
		String price;
		price = ((SwitcherShortData)(switchers.elementAt(index))).price;
		if(price.equals("")) price = "?";
		jTextField4.setText(price);
		summ(jTextField4.getText(), jTextField2.getText());
	}
	//----------------------------------------------------------------------------------------------------------------
	// To show data about reflectometers
	void showReflectometerData(int index)
	{	if(index<0) return;

		String dynamicWindow = "";
		String swl = jComboBox_wave.getSelectedItem().toString();// swl - selected wavelength
		// по всем длинам волн, на которых может работать данный рефлектометр
		for(int i=0; i<((ReflectometerShortData) (reflectometers.elementAt(index))).wavelength.size(); i++)
		{  String wl = (String)( ( (ReflectometerShortData) (reflectometers.elementAt(index)) ).wavelength.elementAt(i) );
			 if( wl.equals(swl) || swl.equals("все") )
			 {  dynamicWindow = (String)((ReflectometerShortData) (reflectometers.elementAt(index))).dynamicWindow.elementAt(i);
					break;
			 }
		}
		jTextField1.setText(dynamicWindow);

		String price;
		price = ( (ReflectometerShortData)(reflectometers.elementAt(index)) ).price;
		if(price.equals("")) price = "?";
		jTextField2.setText(price);

		//summ(jTextField4.getText(), jTextField2.getText());
	}
	//----------------------------------------------------------------------------------------------------------------
	// show data about reflectometers
	// ищем по reflectometerName. Двух одинаковых reflectometerName быть НЕ ДОЛЖНО ! иначе работать может неправильно
	void showReflectometerData(String name)
	{	if(name.equals("")) return;

		String dynamicWindow = "";
		String price;
		String swl = jComboBox_wave.getSelectedItem().toString();// swl - selected wavelength
		for(int i=0; i<reflectometers.size(); i++)
		{ if( ((ReflectometerShortData)( reflectometers.elementAt(i)) ).reflectometerName.equals(name) )
			{	// по всем длинам волн, на которых может работать данный рефлектометр
				for(int j=0; j<((ReflectometerShortData) (reflectometers.elementAt(i))).wavelength.size(); j++)
				{ String wl = (String)( ( (ReflectometerShortData) (reflectometers.elementAt(i)) ).wavelength.elementAt(j) );
					if( wl.equals(swl) || swl.equals("все") )
					{ dynamicWindow = (String)((ReflectometerShortData) (reflectometers.elementAt(i))).dynamicWindow.elementAt(j);
						break;
					}
				}

				jTextField1.setText(dynamicWindow);
				price = ( (ReflectometerShortData)( reflectometers.elementAt(i)) ).price;
				if(price.equals("")) { price = "?";}
				jTextField2.setText(price);

				break;
			}
		}
		//summ(jTextField4.getText(), jTextField2.getText());// сумму не считаем
	}
	//----------------------------------------------------------------------------------------------------------------
	void jComboBox1_actionPerformed(ActionEvent e)
	{
		//showReflectometerData(jComboBox1.getSelectedIndex()); эту строку заменили на :
		if(jComboBox1.getSelectedItem() == null)
			return;
		showReflectometerData(jComboBox1.getSelectedItem().toString());
		setToolTipText();
	}
	//----------------------------------------------------------------------------------------------------------------
	void jComboBox1_itemStateChanged(ItemEvent e)
	{
		if(jComboBox1.getSelectedItem() == null)
			return;
		showReflectometerData(jComboBox1.getSelectedItem().toString());
		setToolTipText();
	}
	//----------------------------------------------------------------------------------------------------------------
	void jComboBox2_actionPerformed(ActionEvent e)
	{
		showSwitcherData(jComboBox2.getSelectedIndex());
		setToolTipText();
	}
	//----------------------------------------------------------------------------------------------------------------
	//menu "Open price file"
  void jMenuItem1_actionPerformed(ActionEvent e)
	{ menuFileOpenExecute();
  }
	//----------------------------------------------------------------------------------------------------------------
	public void menuFileOpenExecute()
  {
    if(dataEdited) //if data was edited...
    { String []key = new String[1];
      key[0] = "";
      DialogExit dlg = new DialogExit(null, key);
      Dimension dlgSize = dlg.getPreferredSize();
      Dimension frmSize = getSize();
      Point loc = getLocation();
      dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
      dlg.setModal(true);
      dlg.pack();
      dlg.show();
      if(key[0].equals("cancel")) return; // do nothing,
      else
      if(key[0].equals("no")){} //continue without saving
      else
      if(key[0].equals("yes") && fileOpened)
      {	if(!saveDataToFileWithNoName(workFile)) return; // save to the opened file
      }
      else
      if(key[0].equals("yes") && !fileOpened)
      {	if(!saveDataToFileWithName()) return; //save to the new file
      }
      dataEdited = false;
    }
    openFile(); // open file with prices

  }
  //----------------------------------------------------------------------------------------------------------------
  // routine to open price file
	boolean openFile()
	{
		boolean ret = true;
		JFileChooser chooser = new JFileChooser(filePath);
		ChoosableFileFilter filter = new ChoosableFileFilter("prc", "Price file");
		chooser.setFileFilter(filter);
		int returnVal = chooser.showOpenDialog(this);

		if(returnVal == JFileChooser.APPROVE_OPTION)
		{
			File file = chooser.getSelectedFile();
			this.workFile = file;
			this.setTitle("файл цен: "+file.getName());
			IniFile iniFile;// = new IniFile(file);
			try
			{	iniFile = new IniFile(file);
				setPricesFromFile(iniFile);
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			String filename = chooser.getSelectedFile().getName();
			setMenuHightlight(true);
			fileName = filename;
		}
		else ret = false; // if file was not opened, or error
		return ret;
	}
	//----------------------------------------------------------------------------------------------------------------
	// editing of the text area
	void jTextField2_actionPerformed(ActionEvent e){}
	//----------------------------------------------------------------------------------------------------------------
	// suppose editing of the text area
	void jTextField2_focusLost(FocusEvent e)
	{
		String price = jTextField2.getText();
		String name = jComboBox1.getSelectedItem().toString();//int index = jComboBox1.getSelectedIndex();
		for(int i=0; i<reflectometers.size(); i++)
		{  if( ((ReflectometerShortData)( reflectometers.elementAt(i)) ).reflectometerName.equals(name) )
			 { ((ReflectometerShortData)(reflectometers.elementAt(i))).price = price;
				 dataEdited = true;
//				 this.jMenuItem4.setEnabled(true);
				 summ(jTextField4.getText(), jTextField2.getText()); // getting of the summary price
				 break;
			 }
		}
	}
	//----------------------------------------------------------------------------------------------------------------
	// editing of the text area
	//----------------------------------------------------------------------------------------------------------------
	// suppose editing of the text area
	void jTextField4_focusLost(FocusEvent e)
	{	String price = jTextField4.getText();
		int index = jComboBox2.getSelectedIndex();
		((SwitcherShortData)(switchers.elementAt(index))).price = price;
		dataEdited = true;
//		this.jMenuItem4.setEnabled(true);
		summ(jTextField4.getText(), jTextField2.getText()); // getting of the summary price

	}
	//----------------------------------------------------------------------------------------------------------------
	// set prices from file
	void setPricesFromFile(IniFile iniFile)
	{	for(int i=0; i<reflectometers.size(); i++)
		{
			String price = (String)(
			iniFile.getValue(((ReflectometerShortData)(reflectometers.elementAt(i))).ID));
			// obtaining the value according to the ID

			if(price == null) price = "";

			((ReflectometerShortData)(reflectometers.elementAt(i))).price = price;
			// setting ID to the class ReflectometerShortData
		}
		for(int i=0; i<switchers.size(); i++) // the same operations, as above, for switchers
		{
			String price = (String)(
			iniFile.getValue(((SwitcherShortData)(switchers.elementAt(i))).ID));
			if(price == null) price = "";
			((SwitcherShortData)(switchers.elementAt(i))).price = price;
		}
		this.showSwitcherData(jComboBox2.getSelectedIndex());
		this.showReflectometerData( jComboBox1.getSelectedItem().toString() );
	}
	//--------------------------------------------------------------------------------------------------------------
	//Save price file
	void jMenuItem3_actionPerformed(ActionEvent e)
	{	menuFileSaveExecute();
	}
	//--------------------------------------------------------------------------------------------------------------
  public void menuFileSaveExecute()
  { if(fileOpened)
    {	if(!saveDataToFileWithNoName(workFile))
      {  return; // save to olready opened file
      }
    }
    else if(!saveDataToFileWithName())
      return; // save to the new file

    dataEdited = false;
    setMenuHightlight(true);

  }
  //--------------------------------------------------------------------------------------------------------------
	// routine to save file. Asks for name.
	boolean saveDataToFileWithName()
	{	boolean ret = true;
		JFileChooser chooser = new JFileChooser(filePath);//("c:/");
		ChoosableFileFilter filter = new ChoosableFileFilter("prc", "Price file");
		chooser.setFileFilter(filter);

		int returnVal = chooser.showSaveDialog(this);
		//System.out.println("Default saver works");

		if(returnVal == JFileChooser.APPROVE_OPTION)
		{	File file = chooser.getSelectedFile();
      // если файлу не задали расширения, то дописываем его
      if (!file.getName().endsWith(".prc"))
      { file.renameTo(new File(file.getPath()+".prc"));
      }
			workFile = file;
			this.setTitle("файл цен: " + file.getName());
			IniFile iniFile;
			try
			{	iniFile = new IniFile(file);
				setPricesToFile(iniFile);
			}
			catch(Exception ex)
			{	ex.printStackTrace();
			}
			dataEdited = false;
			fileOpened = true;
		}
		else ret = false;
    return ret;
	}
	//--------------------------------------------------------------------------------------------------------------
	// routine to save. Does not ask about anything
	boolean saveDataToFileWithNoName(File file)
	{	 boolean ret = true;
		 this.setTitle("файл цен: "+file.getName());

		 IniFile iniFile;
		 try
		 {	iniFile = new IniFile(file);
				setPricesToFile(iniFile);
				dataEdited = false;
				fileOpened = true;
		 }
		 catch(Exception ex)
		 { ex.printStackTrace();
			 ret = false;
		 }
		 return ret;
	}
	//----------------------------------------------------------------------------------------------------------------
	//setting of the prices to file.
	void setPricesToFile(IniFile iniFile)
	{ for(int i=0; i<reflectometers.size(); i++)
		{	String price = ((ReflectometerShortData)
			(reflectometers.elementAt(i))).price;
			String ID = ((ReflectometerShortData)
									 (reflectometers.elementAt(i))).ID;
			iniFile.setValue(ID, price); // sets price in accordance to ID
		}

		for(int i=0; i<switchers.size(); i++)
		{	String price = ((SwitcherShortData)(switchers.elementAt(i))).price;
			String ID = ((SwitcherShortData)(switchers.elementAt(i))).ID;
			iniFile.setValue(ID, price); // sets price in accordance to ID
		}
		iniFile.saveKeys(); // performs saving
	}
//----------------------------------------------------------------------------------------------------------------
	void summ(String s1, String s2) // summing of Strings
	{	double summa = getNumber(s1)+getNumber(s2);
		//jTextField5.setText(String.valueOf(summa));
	}
//----------------------------------------------------------------------------------------------------------------
	double getNumber(String s) // String  -> double
	{	double ret;
		try
		{	ret = Double.parseDouble(s);
		}
		catch(Exception e)
		{ ret = 0.;
		}
		return ret;
	}
	//----------------------------------------------------------------------------------------------------------------
 //about dialog (HELP)
	void jMenuItem8_actionPerformed(ActionEvent e)
	{	 DialogAbout dlg = new DialogAbout(null);
		 Dimension dlgSize = dlg.getPreferredSize();
		 Dimension frmSize = getSize();
		 Point loc = getLocation();
		 dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
		 dlg.setModal(true);
		 dlg.pack();
		 dlg.show();
	}
	//----------------------------------------------------------------------------------------------------------------
	void setToolTipText() // Setting of the tool tip text.
	{	jComboBox1.setToolTipText((String)(jComboBox1.getSelectedItem()));
		jComboBox2.setToolTipText((String)(jComboBox2.getSelectedItem()));
	}
	//----------------------------------------------------------------------------------------------------------------
	void jMenuItem7_actionPerformed(ActionEvent e) //Exit
	{	if(dataEdited) // checker, if data was edited
		{	String []key = new String[1];
			key[0] = "";
			DialogExit dlg = new DialogExit(null, key);
			Dimension dlgSize = dlg.getPreferredSize();
			Dimension frmSize = getSize();
			Point loc = getLocation();
			dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
			dlg.setModal(true);
			dlg.pack();
			dlg.show();
			if(key[0].equals("cancel"))
     {return;} // do nothing
			else if(key[0].equals("no"))
			{this.dispose(); // exit anyway
      }
			else if(key[0].equals("yes") && fileOpened)
			{	this.saveDataToFileWithNoName(workFile);
      }
			//save to the file opened already
			else if(key[0].equals("yes") && fileOpened == false)
			{ this.saveDataToFileWithName();
      }
			dataEdited = false; // save to the new name
		}
		this.dispose(); //exit
	}
	//----------------------------------------------------------------------------------------------------------------
	void jMenuItem4_actionPerformed(ActionEvent e)
	{	 menuFileSaveAsExecute(); //save
	}
	//----------------------------------------------------------------------------------------------------------------
  public void menuFileSaveAsExecute()
  {  saveDataToFileWithName();
  }
	//----------------------------------------------------------------------------------------------------------------
  void jMenuItem2_actionPerformed(ActionEvent e) //close price file
	{	menuFileCloseExecute();
	}
	//----------------------------------------------------------------------------------------------------------------
  public void menuFileCloseExecute()
  { if(dataEdited)
    { if(fileOpened == true)
      {	saveDataToFileWithName(); //save to new file
      }
    }

    setMenuHightlight(false); // set able/disable save options
    for(int i=0; i<reflectometers.size(); i++)
    { ((ReflectometerShortData)(reflectometers.elementAt(i))).price = "";
    }
    for(int i=0; i<switchers.size(); i++)
    { ((SwitcherShortData)(switchers.elementAt(i))).price = "";
    }
    this.showReflectometerData(this.jComboBox1.getSelectedItem().toString());
    this.showSwitcherData(this.jComboBox2.getSelectedIndex());
    setToolTipText();
    this.setTitle("файл цен не выбран");
  }
  //----------------------------------------------------------------------------------------------------------------
	void setMenuHightlight(boolean set)
	{	 fileOpened = set;
//		 this.jMenuItem2.setEnabled(set);
//		 this.jMenuItem3.setEnabled(set);
//		 this.jMenuItem4.setEnabled(set);
	}
	//----------------------------------------------------------------------------------------------------------------
	void jComboBox_wave_itemStateChanged(ItemEvent e)
	{  reassignComboList(jComboBox_wave.getSelectedItem().toString());

		 if(!jComboBox_wave.getSelectedItem().toString().equals("все"))
		 { optOptContext.wavelength = (int)Double.parseDouble(jComboBox_wave.getSelectedItem().toString());
			 optOptContext.wavelength_set = 1;
		 }
		 else
		 { optOptContext.wavelength = -1;
			 optOptContext.wavelength_set = 0;
		 }
		 optOptContext.graph_set = 0;// если поменяли длину волны, то предыдущий граф ( если он вобще был создан ) неверен
	}
	//----------------------------------------------------------------------------------------------------------------
	// если что-то изменяли, то спрашиваем о сохранении
	void this_internalFrameClosing(InternalFrameEvent e)
	{	setDefaultCloseOperation(HIDE_ON_CLOSE);//close by default
		if(dataEdited) //if prices was edited
		{ String []key = new String[1];
			key[0] = "";
			DialogExit dlg = new DialogExit(null, key);
			Dimension dlgSize = dlg.getPreferredSize();
			Dimension frmSize = getSize();
			Point loc = getLocation();
			dlg.setLocation((frmSize.width - dlgSize.width) / 2 + loc.x, (frmSize.height - dlgSize.height) / 2 + loc.y);
			dlg.setModal(true);
			dlg.pack();
			dlg.show();
			if(key[0].equals("cancel"))
			{  setDefaultCloseOperation(DO_NOTHING_ON_CLOSE); // do nothing
			}
			else if(key[0].equals("no"))
			{ setDefaultCloseOperation(HIDE_ON_CLOSE);//close
			}
			else if(key[0].equals("yes") && fileOpened)
			{ setDefaultCloseOperation(HIDE_ON_CLOSE );//close after saving
				this.saveDataToFileWithNoName(workFile);
			}
			//save to the file opened already
			else if(key[0].equals("yes") && fileOpened == false)
			{  setDefaultCloseOperation(HIDE_ON_CLOSE );//close after saving
				 this.saveDataToFileWithName();
			}
		}
	}
  //----------------------------------------------------------------------------------------------------------------
}
//==================================================================================================================
// поля - описание тех свойств рефлектометра, которые нам нужны ( не все )
class ReflectometerShortData
{ String ID;
	String reflectometerName;
	Vector dynamicWindow = new Vector(); //динамические диапазоны для соответствующих длин волн (соответствие по индексу)
	Vector wavelength = new Vector();    // длины волн, на которых может работать рефлектометр
	String price;
	//----------------------------------------------------------------------------------------------------------------
	public ReflectometerShortData()
	{	ID = "";
		reflectometerName = "";
		dynamicWindow.clear();
		wavelength.clear();
		price = "";
	}
	//----------------------------------------------------------------------------------------------------------------
	public ReflectometerShortData(String id, String n, Vector wl, Vector dw, String p)
	{ ID = id;
		reflectometerName = n;
		price = p;
		for(int i=0; i<wl.size(); i++)
		{ wavelength.add( wl.elementAt(i) );
			dynamicWindow.add(dw.elementAt(i)); //так можно делать, так как wavelength.size() =dynamicWindow.size()
		}
	}
}
//==================================================================================================================
// поля - описание тех свойств рефлектометра, которые нам нужны в данный момент для отображения в форме ( ещё короче чем ReflectometerShortData()
class ReflectometerShownData
{ String ID;
	String reflectometerName;
	String dynamicWindow ; //динамические диапазоны для соответствующих длин волн (соответствие по индексу)
	String price;
	//----------------------------------------------------------------------------------------------------------------
	public ReflectometerShownData()
	{	ID = "";
		reflectometerName = "";
		dynamicWindow = "";
		price = "";
	}
	//----------------------------------------------------------------------------------------------------------------
	public ReflectometerShownData(String id, String n, String dw, String p)
	{ ID = id;
		reflectometerName = n;
		dynamicWindow = dw;
		price = p;
	}
}
//==================================================================================================================
class SwitcherShortData
{ String ID;
	String switcherName;
	String portNumber;
	String price;
	//----------------------------------------------------------------------------------------------------------------
	public SwitcherShortData()
	{	ID = "";
		switcherName = "";
		portNumber = "";
		price = "";
	}
	//----------------------------------------------------------------------------------------------------------------
	public SwitcherShortData(String id, String n, String pn, String p)
	{	ID = id;
		switcherName = n;
		portNumber = pn;
		price = p;
	}
	//----------------------------------------------------------------------------------------------------------------
}
//==================================================================================================================