package com.syrus.AMFICOM.Client.Survey.Test;

import com.syrus.AMFICOM.CORBA.General.TestTemporalType;
import com.syrus.AMFICOM.CORBA.General.TestTimeStamps;
import com.syrus.AMFICOM.CORBA.General.TestTimeStampsPackage.PeriodicalTestParameters;
import com.syrus.AMFICOM.CORBA.Survey.Parameter_Transferable;
import com.syrus.AMFICOM.Client.General.UI.ObjectResourceComboBox;
import com.syrus.AMFICOM.Client.Resource.Map.MapKISNodeElement;
import com.syrus.AMFICOM.Client.Resource.Map.MapTransmissionPathElement;
import com.syrus.AMFICOM.Client.Resource.Result.Parameter;
import com.syrus.AMFICOM.Client.Resource.Result.Test;
import com.syrus.io.ByteArray;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DebugGraphics;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;

import oracle.jdeveloper.layout.XYConstraints;
import oracle.jdeveloper.layout.XYLayout;


public class TestDialog extends JDialog {//JFrame {

  public int retcode = 0;

  int times_count=0;
  JPanel contentPane;
  public Test tt = new Test("");
  Vector test_times = new Vector();
  Vector tests1 = new Vector();
  Vector test1 = new Vector();
  String[] izmer = {"4","8","16","32","64","128","256"};
  String[] volna = {"1550","1350","1670"};
  String[] rres = {"0.25","0.5","1","2","4","8","16"};
  String[] ppulswd = {"100","200","500"};
  Vector res = new Vector();
  Vector pulswd = new Vector();
  String[] reflect = {"4096","8192","16384","32768","65536","131072","262144"};
  String[] test_plan_diskret = {"Минута","Час","Сутки","Месяц","Год"};
  String[] kis = {"КИС1","КИС2","КИС3","КИС4","КИС5",};
  String[] volokno = {"Волокно1","Волокно2","Волокно3","Волокно4","Волокно5",};
  String[] tip_testa = {"Тест1","Тест2"};
  String[] mmonth = {"\u042F\u043D\u0432\u0430\u0440\u044C","\u0424\u0435\u0432\u0440\u0430\u043B\u044C","\u041C\u0430\u0440\u0442","\u0410\u043F\u0440\u0435\u043B\u044C","\u041C\u0430\u0439","\u0418\u044E\u043D\u044C","\u0418\u044E\u043B\u044C","\u0410\u0432\u0433\u0443\u0441\u0442",
		      "\u0421\u0435\u043D\u0442\u044F\u0431\u0440\u044C","\u041E\u043A\u0442\u044F\u0431\u0440\u044C","\u041D\u043E\u044F\u0431\u0440\u044C","\u0414\u0435\u043A\u0430\u0431\u0440\u044C",};
  String[] yyear = {"2002","2003","2004","2005","2006","2007","2008","2009","2010",};
  String[] dday = {"1","2","3","4","5","6","7","8","9","10",
		      "11","12","13","14","15","16","17","18","19","20",
		      "21","22","23","24","25","26","27","28","29","30","31"};
  String[] promezh = {"\u0441\u0435\u043A.","\u043C\u0438\u043D.","\u0447\u0430\u0441."};
  Calendar cal = Calendar.getInstance();
  //JViewport jViewport = new JViewport();
  int day_temp = Integer.parseInt(cal.getTime().toString().substring(8,10));

  ButtonGroup buttonGroup1 = new ButtonGroup();
  JTabbedPane jTabbedPane1 = new JTabbedPane();
  JLabel jLabel18 = new JLabel();
  JLabel jLabel17 = new JLabel();
  JLabel jLabel16 = new JLabel();
  JLabel jLabel15 = new JLabel();
  JLabel jLabel14 = new JLabel();
  JLabel jLabel13 = new JLabel();
  JRadioButton jRadioButton3 = new JRadioButton();
  JLabel jLabel12 = new JLabel();
  JRadioButton jRadioButton2 = new JRadioButton();
  JLabel jLabel11 = new JLabel();
  JRadioButton jRadioButton1 = new JRadioButton();
  JLabel jLabel10 = new JLabel();
  JComboBox jComboBox7 = new JComboBox(promezh);
  JComboBox jComboBox6 = new JComboBox(dday);
  JComboBox jComboBox5 = new JComboBox(mmonth);
  JComboBox jComboBox4 = new JComboBox(yyear);
  ObjectResourceComboBox jComboBox3 = new ObjectResourceComboBox("testtype");//  JComboBox jComboBox3 = new JComboBox(tip_testa);
  ObjectResourceComboBox jComboBox2 = new ObjectResourceComboBox("mappathelement");//JComboBox jComboBox2 = new JComboBox(volokno);
  MonthCanvas canv = new MonthCanvas(cal,day_temp);
  ObjectResourceComboBox jComboBox1 = new ObjectResourceComboBox("mapkiselement");//JComboBox jComboBox1 = new JComboBox(kis);
  JButton jButton4 = new JButton();
  JTextField jTextField4 = new JTextField();
  JTextField jTextField3 = new JTextField();
  JLabel jLabel9 = new JLabel();
  JTextField jTextField2 = new JTextField();
  JTextField jTextField1 = new JTextField();
  JLabel jLabel8 = new JLabel();
  JPanel jPanel7 = new JPanel();
  JLabel jLabel7 = new JLabel();
  JPanel jPanel6 = new JPanel();
  JLabel jLabel6 = new JLabel();
  JPanel jPanel5 = new JPanel();
  JPanel jPanel4 = new JPanel();
  JLabel jLabel5 = new JLabel();
  JLabel jLabel4 = new JLabel();
  JPanel jPanel3 = new JPanel();
  JLabel jLabel3 = new JLabel();
  JLabel jLabel2 = new JLabel();
  JPanel jPanel1 = new JPanel();
  JLabel jLabel1 = new JLabel();
  CurrentTime ttime = new CurrentTime(jLabel7);
  XYLayout xYLayout5 = new XYLayout();
  XYLayout xYLayout4 = new XYLayout();
  XYLayout xYLayout3 = new XYLayout();
  XYLayout xYLayout1 = new XYLayout();
  JScrollPane jScrollPane1 = new JScrollPane();
  JComboBox jComboBox8 = new JComboBox(test_plan_diskret);
  TestPlan tp = new TestPlan(tests1, jComboBox8);
  JLabel jLabel19 = new JLabel();
  XYLayout xYLayout6 = new XYLayout();
  JLabel jLabel110 = new JLabel();
  JLabel jLabel111 = new JLabel();
  JLabel jLabel112 = new JLabel();
  JLabel jLabel113 = new JLabel();
  JLabel jLabel114 = new JLabel();
  JTextField jTextField5 = new JTextField();
  JScrollPane jScrollPane2 = new JScrollPane();
  JList jList1 = new JList();
  JPanel jPanel2 = new JPanel();
  JLabel jLabel20 = new JLabel();
  JLabel jLabel21 = new JLabel();
  JComboBox jComboBox10 = new JComboBox(izmer);
  JComboBox jComboBox11 = new JComboBox(ppulswd);
  JComboBox jComboBox12 = new JComboBox(rres);
  JComboBox jComboBox13 = new JComboBox(reflect);
  JComboBox jComboBox9 = new JComboBox(volna);
  XYLayout xYLayout2 = new XYLayout();
  JButton jButton3 = new JButton();
  JButton jButton2 = new JButton();
  JButton jButton1 = new JButton();
  JLabel jLabel22 = new JLabel();
  XYLayout xYLayout7 = new XYLayout();
  JButton jButton5 = new JButton();

  //Construct the frame
  public TestDialog(Frame owner, String title, boolean modal) {
    super(owner, title, modal);
    this.setResizable(false);
    enableEvents(AWTEvent.WINDOW_EVENT_MASK);
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  //Component initialization
  private void jbInit() throws Exception  {
    //setIconImage(Toolkit.getDefaultToolkit().createImage(TestDialog.class.getResource("[Your Icon]")));
    contentPane = (JPanel) this.getContentPane();
    jComboBox4.setSelectedItem(String.valueOf(cal.get(Calendar.YEAR)));
    jComboBox5.setSelectedIndex(cal.get(Calendar.MONTH));
    jComboBox6.setSelectedIndex(day_temp-1);
    jList1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    jList1.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
	jList1_mouseClicked(e);
      }
    });

    jPanel2.setBackground(SystemColor.text);
    jLabel20.setText("Детализация");
    jLabel21.setText("тестирования");
    jButton3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
	jButton3_actionPerformed(e);
      }
    });
    jButton3.setText("\u041F\u0440\u0438\u043D\u044F\u0442\u044C");
    jButton2.setText("\u041E\u0442\u043C\u0435\u043D\u0438\u0442\u044C");
    jButton1.setText("Помощь");
    jComboBox13.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
	jComboBox13_actionPerformed(e);
      }
    });
    jLabel22.setText("* 1000");
    jComboBox3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
	jComboBox3_actionPerformed(e);
      }
    });
    tp.setDebugGraphicsOptions(DebugGraphics.BUFFERED_OPTION);
    tp.setLayout(xYLayout7);
    jButton5.setText("Таблица");
    jComboBox8.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
	jComboBox8_actionPerformed(e);
      }
    });
    buttonGroup1.add(jRadioButton1);
    buttonGroup1.add(jRadioButton2);
    buttonGroup1.add(jRadioButton3);
    jScrollPane1.setBorder(BorderFactory.createLineBorder(Color.black));
    jScrollPane1.setAutoscrolls(true);
    jScrollPane2.setBorder(BorderFactory.createLineBorder(Color.black));
    jScrollPane2.setAutoscrolls(true);
    jLabel1.setText("\u041A\u0418\u0421");
    jPanel1.setLayout(xYLayout1);
    jLabel2.setText("\u0422\u0438\u043F \u0442\u0435\u0441\u0442\u0430");
    jLabel3.setText("\u0412\u043E\u043B\u043E\u043A\u043D\u043E");
    jPanel3.setBorder(BorderFactory.createLineBorder(Color.black));
    jLabel4.setText("\u0413\u043E\u0434");
    jLabel5.setText("\u041C\u0435\u0441\u044F\u0446");
    jPanel4.setLayout(xYLayout3);
    jPanel4.setBorder(BorderFactory.createLineBorder(Color.black));
    jPanel5.setLayout(xYLayout4);
    jPanel5.setBorder(BorderFactory.createLineBorder(Color.black));
    jLabel6.setText("\u0427\u0438\u0441\u043B\u043E");
    jPanel6.setLayout(xYLayout5);
    jPanel6.setBorder(BorderFactory.createLineBorder(Color.black));
    jLabel7.setText("\u0442\u0435\u043A\u0443\u0449\u0435\u0435 \u0432\u0440\u0435\u043C\u044F");
    jLabel7.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel7.setBackground(Color.lightGray);
    jLabel8.setText("Время начала");
    jLabel8.setToolTipText("");
    jTextField1.setText("12:15:15");
    jTextField1.setEnabled(false);
    jTextField2.setText("15");
    jTextField2.setEnabled(false);
    jLabel9.setText("Интервал");
    jTextField3.setEnabled(false);
    jTextField3.setText("12:15:15");
    jTextField4.setEnabled(false);
    jTextField4.setText("12:15:15");
    jButton4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
	jButton4_actionPerformed(e);
      }
    });
    jButton4.setText("\u0414\u043E\u0431\u0430\u0432\u0438\u0442\u044C");
    jButton4.setEnabled(false);
    jComboBox4.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
	jComboBox4_actionPerformed(e);
      }
    });
    jComboBox5.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
	jComboBox5_actionPerformed(e);
      }
    });
    jComboBox6.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
	jComboBox6_actionPerformed(e);
      }
    });
    jComboBox7.setEnabled(false);
    jLabel10.setText("\u041F\u043B\u0430\u043D-\u0433\u0440\u0430\u0444\u0438\u043A \u0442\u0435\u0441\u0442\u0438\u0440\u043E\u0432\u0430\u043D\u0438\u044F");
    jRadioButton1.setText("\u041E\u0434\u043D\u043E\u0440\u0430\u0437\u043E\u0432\u043E\u0435 \u0442\u0435\u0441\u0442\u0438\u0440\u043E\u0432\u0430\u043D\u0438\u0435");
    jRadioButton1.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
	jRadioButton1_actionPerformed(e);
      }
    });
    jLabel11.setText("\u0412\u0438\u0434 \u0442\u0435\u0441\u0442\u0438\u0440\u043E\u0432\u0430\u043D\u0438\u044F");
    jRadioButton2.setText("\u0422\u0435\u0441\u0442\u0438\u0440\u043E\u0432\u0430\u043D\u0438\u0435 \u043F\u043E \u0440\u0430\u0441\u043F\u0438\u0441\u0430\u043D\u0438\u044E");
    jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
	jRadioButton2_actionPerformed(e);
      }
    });
    jLabel12.setText("\u0414\u0430\u0442\u0430 \u0442\u0435\u0441\u0442\u0438\u0440\u043E\u0432\u0430\u043D\u0438\u044F");
    jRadioButton3.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(ActionEvent e) {
	jRadioButton3_actionPerformed(e);
      }
    });
    jRadioButton3.setText("\u041F\u0435\u0440\u0438\u043E\u0434\u0438\u0447\u0435\u0441\u0435\u043A\u043E\u0435 \u0442\u0435\u0441\u0442\u0438\u0440\u043E\u0432\u0430\u043D\u0438\u0435");
    jLabel13.setText("тестирования");
    jLabel14.setText("Время окончания");
    jLabel15.setText("тестирования");
    jLabel16.setText("Временные параметры тестирования");
    jLabel17.setText("Время");
    jLabel18.setText("тестирования");
    contentPane.setLayout(xYLayout2);
    this.setSize(new Dimension(700, 625)); //700x650
    this.setTitle("Планировщик тестов");

    ttime.start();
    jPanel7.setLayout(xYLayout6);
    jLabel110.setText("Длина волны");
    jLabel111.setText("Число измерений для усреднения");
    jLabel112.setText("Ширина импульса");
    jLabel113.setText("Разрешение");
    jLabel114.setText("Длина измеряемой рефлектограммы");
    jTextField5.setText("1.467");

    contentPane.add(jTabbedPane1,    new XYConstraints(0, 0, -1, 540));
    jTabbedPane1.add(jPanel1,   "Тестирование");
    jTabbedPane1.add(jPanel7,   "Параметры");
    if (jComboBox3.getSelectedIndex()==0)
    {
	jPanel7.removeAll();
	jPanel7.add(jLabel110,   new XYConstraints(23, 61, -1, -1));
	jPanel7.add(jLabel19,  new XYConstraints(25, 28, -1, -1));
	jLabel19.setText("Показатель отражения волокна");
	jPanel7.add(jLabel111,  new XYConstraints(24, 96, -1, -1));
	jPanel7.add(jComboBox10, new XYConstraints(262, 94, 52, -1));
	jPanel7.add(jComboBox9,   new XYConstraints(262, 60, 52, -1));
	jPanel7.add(jTextField5,   new XYConstraints(261, 26, 52, -1));
	jPanel7.add(jLabel114,   new XYConstraints(23, 133, -1, -1));
	jPanel7.add(jComboBox13,  new XYConstraints(262, 129, 52, -1));
	jPanel7.add(jComboBox11,    new XYConstraints(262, 202, 52, -1));
	jPanel7.add(jLabel113, new XYConstraints(24, 170, -1, -1));
	jPanel7.add(jLabel112,  new XYConstraints(24, 203, -1, -1));
	jPanel7.add(jLabel22,   new XYConstraints(315, 96, -1, -1));
	jPanel7.add(jComboBox12, new XYConstraints(262, 166, 52, -1));
    }
    else if (jComboBox3.getSelectedIndex()==1)
    {
	jPanel7.removeAll();
	jPanel7.add(jLabel19,  new XYConstraints(25, 28, -1, -1));
	jLabel19.setText("Идентификатор измерения");
	jPanel7.add(jComboBox9,   new XYConstraints(262, 26, 400, -1));
    }
    contentPane.add(jButton2,  new XYConstraints(273, 554, 128, 28));
    contentPane.add(jButton3, new XYConstraints(30, 553, 128, 28));
    contentPane.add(jButton1, new XYConstraints(535, 553, 128, 28));
    jPanel1.add(jPanel6, new XYConstraints(7, 239, 227, 266));
    jPanel6.add(jButton4, new XYConstraints(14, 224, -1, -1));
    jPanel6.add(jTextField4, new XYConstraints(33, 189, 52, 21));
    jPanel6.add(jLabel18, new XYConstraints(18, 162, -1, -1));
    jPanel6.add(jLabel17, new XYConstraints(40, 150, -1, -1));
    jPanel6.add(jScrollPane2, new XYConstraints(112, 151, 102, 100));
    jPanel6.add(jLabel16, new XYConstraints(3, 9, -1, -1));
    jPanel6.add(jTextField3, new XYConstraints(132, 120, 52, -1));
    jPanel6.add(jLabel8, new XYConstraints(62, 31, -1, -1));
    jPanel6.add(jLabel13, new XYConstraints(62, 46, -1, -1));
    jPanel6.add(jTextField1, new XYConstraints(75, 64, 52, -1));
    jPanel6.add(jComboBox7,   new XYConstraints(46, 120, -1, -1));
    jPanel6.add(jTextField2, new XYConstraints(8, 120, 32, -1));
    jPanel6.add(jLabel15, new XYConstraints(119, 102, -1, -1));
    jPanel6.add(jLabel14, new XYConstraints(108, 90, -1, -1));
    jPanel6.add(jLabel21,    new XYConstraints(11, 102, -1, -1));
    jPanel6.add(jLabel9,  new XYConstraints(20, 91, -1, -1));
    jScrollPane2.getViewport().add(jList1, null);
    jPanel1.add(jPanel5, new XYConstraints(255, 87, 426, 180));
    jPanel5.add(jLabel12, new XYConstraints(8, 19, -1, -1));
    jPanel5.add(jPanel3, new XYConstraints(136, 4, 283, 167));
    jPanel5.add(jLabel4, new XYConstraints(93, 46, -1, -1));
    jPanel5.add(jLabel5, new XYConstraints(88, 80, -1, -1));
    jPanel5.add(jComboBox6, new XYConstraints(10, 116, 73, -1));
    jPanel5.add(jLabel6, new XYConstraints(88, 118, -1, -1));
    jPanel5.add(jComboBox5, new XYConstraints(10, 79, 73, -1));
    jPanel5.add(jComboBox4, new XYConstraints(9, 42, 73, -1));
    jPanel4.add(jLabel11, new XYConstraints(17, 11, -1, -1));
    jPanel4.add(jRadioButton1, new XYConstraints(14, 37, -1, -1));
    jPanel4.add(jRadioButton3, new XYConstraints(14, 88, -1, -1));
    jPanel4.add(jRadioButton2, new XYConstraints(14, 63, -1, -1));
    jPanel1.add(jButton5,   new XYConstraints(598, 275, -1, -1));
    jPanel1.add(jLabel10,  new XYConstraints(430, 282, -1, -1));
    jPanel1.add(jComboBox8,         new XYConstraints(341, 277, -1, 25));
    jPanel1.add(jLabel20,    new XYConstraints(256, 283, -1, -1));
    jPanel3.add(canv);                               
    jPanel1.add(jComboBox3, new XYConstraints(483, 48, 198, -1)); //(483, 48, 198, -1)
    jPanel1.add(jComboBox1, new XYConstraints(5, 48, 198, -1));
    jPanel1.add(jComboBox2, new XYConstraints(241, 48, 198, -1));
    jPanel1.add(jLabel7, new XYConstraints(482, 0, 208, -1));
    jPanel1.add(jScrollPane1,  new XYConstraints(255, 306, 426, 199));
    jScrollPane1.getViewport().add(jPanel2, null);
    jPanel1.add(jLabel2, new XYConstraints(561, 28, -1, -1));
    jPanel1.add(jLabel3, new XYConstraints(315, 29, -1, -1));
    jPanel1.add(jLabel1, new XYConstraints(89, 28, -1, -1));
    jPanel1.add(jPanel4, new XYConstraints(5, 87, 228, 132));
    jPanel2.add(tp);
    jTabbedPane1.setSelectedComponent(jPanel1);
  }
  //Overridden so we can exit when window is closed
  protected void processWindowEvent(WindowEvent e) {
    super.processWindowEvent(e);
    if (e.getID() == WindowEvent.WINDOW_CLOSING) {
//      System.exit(0);
      System.out.println("exit(0)");
    }
  }

  void jComboBox4_actionPerformed(ActionEvent e) {
      day_temp=jComboBox6.getSelectedIndex()+1;
      jPanel3.removeAll();
      cal.set(Integer.parseInt(jComboBox4.getSelectedItem().toString()),
	      jComboBox5.getSelectedIndex(), day_temp);
      MonthCanvas canv = new MonthCanvas(cal,day_temp);
      jPanel3.add(canv);
  }

  void jComboBox5_actionPerformed(ActionEvent e) {
      day_temp=jComboBox6.getSelectedIndex()+1;
      jPanel3.removeAll(); ;
      cal.set(Integer.parseInt(jComboBox4.getSelectedItem().toString()),
	      jComboBox5.getSelectedIndex(), day_temp);
      MonthCanvas canv = new MonthCanvas(cal,day_temp);
      jPanel3.add(canv);
  }

  void jComboBox6_actionPerformed(ActionEvent e) {
      day_temp=jComboBox6.getSelectedIndex()+1;
      jPanel3.removeAll();
      cal.set(Integer.parseInt(jComboBox4.getSelectedItem().toString()),
	      jComboBox5.getSelectedIndex(), day_temp);
      MonthCanvas canv = new MonthCanvas(cal,day_temp);
      jPanel3.add(canv);
  }

  void jRadioButton1_actionPerformed(ActionEvent e) {
      jTextField1.setEnabled(true);
      jTextField2.setEnabled(false);
      jTextField3.setEnabled(false);
      jTextField4.setEnabled(false);
      jList1.setEnabled(false);
      jButton4.setEnabled(false);
      jComboBox7.setEnabled(false);
      tt.temporal_type = TestTemporalType.ONETIME;

  }

  void jRadioButton2_actionPerformed(ActionEvent e) {
      jTextField1.setEnabled(true);
      jButton4.setEnabled(true);
      jTextField2.setEnabled(false);
      jTextField3.setEnabled(false);
      jTextField4.setEnabled(true);
      jList1.setEnabled(true);
      jComboBox7.setEnabled(false);
      tt.temporal_type = TestTemporalType.TIMETABLE;
  }

  void jRadioButton3_actionPerformed(ActionEvent e) {
      jTextField1.setEnabled(true);
      jButton4.setEnabled(false);
      jTextField2.setEnabled(true);
      jTextField3.setEnabled(true);
      jTextField4.setEnabled(false);
      jList1.setEnabled(false);
      jComboBox7.setEnabled(true);
      tt.temporal_type = TestTemporalType.PERIODICAL;
  }

  void jButton3_actionPerformed(ActionEvent e) {
	if (tt.temporal_type == TestTemporalType.ONETIME||tt.temporal_type == TestTemporalType.PERIODICAL
		    ||tt.temporal_type == TestTemporalType.TIMETABLE)
	{

      retcode = 1;	  
  
      test1 = new Vector();
      GregorianCalendar tt_cal1 = new GregorianCalendar(Integer.parseInt(jComboBox4.getSelectedItem().toString()),
	      jComboBox5.getSelectedIndex(), jComboBox6.getSelectedIndex()+1,
	      Integer.parseInt(jTextField1.getText().substring(0,2)),
	      Integer.parseInt(jTextField1.getText().substring(3,5)),
	      Integer.parseInt(jTextField1.getText().substring(6,8)));
	  tt.start_time = tt_cal1.getTime().getTime();
      if (jRadioButton2.isSelected()==true)
      {
	  long[] times = new long [test_times.size()];
	  String vremya;
	  for (int i = 0; i < test_times.size(); i++)
	  {
	      vremya = test_times.elementAt(i).toString();
	      GregorianCalendar tt_cal2 = new GregorianCalendar(Integer.parseInt(jComboBox4.getSelectedItem().toString()),
		  jComboBox5.getSelectedIndex(), jComboBox6.getSelectedIndex()+1,
		  Integer.parseInt(vremya.substring(0,2)),
		  Integer.parseInt(vremya.substring(3,5)),
		  Integer.parseInt(vremya.substring(6,8)));
	      times[i] = tt_cal2.getTime().getTime();
	  }
	  TestTimeStamps tts = new TestTimeStamps();
	  tts.ti(times);
	  tt.time_stamps = tts;
      }
      else if (jRadioButton3.isSelected()==true)
      {
	  long dt = Integer.parseInt(jTextField2.getText())*1000;
	  int tem = jComboBox7.getSelectedIndex();
	  for (int i=0; i<tem;i++)
	      dt=dt*60;
	  GregorianCalendar tt_cal3 = new GregorianCalendar(Integer.parseInt(jComboBox4.getSelectedItem().toString()),
		  jComboBox5.getSelectedIndex(), jComboBox6.getSelectedIndex()+1,
		  Integer.parseInt(jTextField3.getText().substring(0,2)),
		  Integer.parseInt(jTextField3.getText().substring(3,5)),
		  Integer.parseInt(jTextField3.getText().substring(6,8)));

	  long end_time = tt_cal3.getTime().getTime();
	  PeriodicalTestParameters ptp = new PeriodicalTestParameters(dt, end_time);
	  TestTimeStamps tts = new TestTimeStamps();
	  tts.ptpars(ptp);
	  tt.time_stamps = tts;
      }

	  MapKISNodeElement mke = (MapKISNodeElement )jComboBox1.getSelectedItem();
//      tt.kis_id = (String )mke.KIS_id;
 	  MapTransmissionPathElement mpe = (MapTransmissionPathElement )jComboBox2.getSelectedItem();
//      tt.local_id = (String )mpe.PATH_ID;
	  
//      tt.kis_id = (String )jComboBox1.getSelected();
//      tt.local_id = (String )jComboBox2.getSelected();
      tt.test_type_id = (String )jComboBox3.getSelected();
//      tt.status = TestStatus.SHEDULED;
      Parameter_Transferable par_trans;
//      if (jComboBox3.getSelectedIndex()==0)
      if((tt.test_type_id.equals("trace_and_analyse")) ||
		(tt.test_type_id.equals("trace_simple")))
      {
	  ByteArray ref_ior;
	  ByteArray ref_wvlen;
	  ByteArray ref_scans;
	  ByteArray ref_trclen;
	  ByteArray ref_res;
	  ByteArray ref_pulswd;
    try {
      ref_ior = new ByteArray(Double.parseDouble(jTextField5.getText()));
      par_trans = new Parameter_Transferable("", "", "ref_ior", "", "double", "double", "", ref_ior.getBytes());
//      tt.addArgument(new Parameter(par_trans));

      ref_wvlen = new ByteArray(Integer.parseInt(jComboBox9.getSelectedItem().toString()));
      par_trans = new Parameter_Transferable("", "", "ref_wvlen", "", "int", "int", "", ref_wvlen.getBytes());
//      tt.addArgument(new Parameter(par_trans));

      ref_scans = new ByteArray(Double.parseDouble(jComboBox10.getSelectedItem().toString()));
      par_trans = new Parameter_Transferable("", "", "ref_scans", "", "double", "double", "", ref_scans.getBytes());
//      tt.addArgument(new Parameter(par_trans));

      ref_trclen = new ByteArray(Double.parseDouble(jComboBox13.getSelectedItem().toString()));
      par_trans = new Parameter_Transferable("", "", "ref_trclen", "", "double", "double", "", ref_trclen.getBytes());
//      tt.addArgument(new Parameter(par_trans));

      ref_res = new ByteArray(Double.parseDouble(jComboBox12.getSelectedItem().toString()));
      par_trans = new Parameter_Transferable("", "", "ref_res", "", "double", "double", "", ref_res.getBytes());
//      tt.addArgument(new Parameter(par_trans));

      ref_pulswd = new ByteArray(Long.parseLong(jComboBox11.getSelectedItem().toString()));
      par_trans = new Parameter_Transferable("", "", "ref_pulswd", "", "long", "long", "", ref_pulswd.getBytes());
      par_trans.codename = "ref_pulswd";
      par_trans.param_type_id = "long";
      par_trans.value = ref_pulswd.getBytes();
//      tt.addArgument(new Parameter(par_trans));
    }
    catch(java.io.IOException ex)
	  {}
/*    
	  TestParameter_Transferable[] tpt = new TestParameter_Transferable[6];
	  try
	  {
	  ref_ior = new ByteArray(Double.parseDouble(jTextField5.getText()));
	  tpt[0]= new TestParameter_Transferable("ref_ior",ref_ior.getBytes());
	  ref_wvlen = new ByteArray(Integer.parseInt(jComboBox9.getSelectedItem().toString()));
	  tpt[1]= new TestParameter_Transferable("ref_wvlen",ref_wvlen.getBytes());
	  ref_scans = new ByteArray(Double.parseDouble(jComboBox10.getSelectedItem().toString()));
	  tpt[2]= new TestParameter_Transferable("ref_scans",ref_scans.getBytes());
	  ref_trclen = new ByteArray(Double.parseDouble(jComboBox13.getSelectedItem().toString()));
	  tpt[3]= new TestParameter_Transferable("ref_trclen",ref_trclen.getBytes());
	  ref_res = new ByteArray(Double.parseDouble(jComboBox12.getSelectedItem().toString()));
	  tpt[4]= new TestParameter_Transferable("ref_res",ref_res.getBytes());
	  ref_pulswd = new ByteArray(Long.parseLong(jComboBox11.getSelectedItem().toString()));
	  tpt[5]= new TestParameter_Transferable("ref_pulswd",ref_pulswd.getBytes());
	  }
	  catch(java.io.IOException ex)
	  {}
	  tt.parameters=tpt;
*/
      }
      else 
//	  if (jComboBox3.getSelectedIndex()==1)
      if (tt.test_type_id.equals("trace_get_trace"))
      {
	  ByteArray ref_characterizationidentity;
    try {
      ref_characterizationidentity = new ByteArray(jComboBox9.getSelectedItem().toString());
      par_trans = new Parameter_Transferable();
      par_trans.codename = "ref_characterizationidentity";
      par_trans.param_type_id = "string";
      par_trans.value = ref_characterizationidentity.getBytes();
//      tt.addArgument(new Parameter(par_trans));
    }
    catch(java.io.IOException ex)
	  {}
/*
	  TestParameter_Transferable[] tpt = new TestParameter_Transferable[1];
	  try
	  {
	      ref_characterizationidentity = new ByteArray(jComboBox9.getSelectedItem().toString());
	      tpt[0]= new TestParameter_Transferable("ref_characterizationidentity",ref_characterizationidentity.getBytes());
	  }
	  catch(java.io.IOException ex)
	  {}
	  tt.parameters=tpt;
*/
      }

      test1.addElement((String )jComboBox3.getSelected());
      test1.addElement(String.valueOf(tt.temporal_type.value()));
      test1.addElement(String.valueOf(tt.start_time));
      if (tt.temporal_type.value()==TestTemporalType._TIMETABLE)
      {
	  long[] times_s = tt.time_stamps.ti();
	  for (int i = 0; i < times_s.length; i++)
	  {
	      test1.addElement(String.valueOf(times_s[i]));
	  }
      }
      else if (tt.temporal_type.value()==TestTemporalType._PERIODICAL)
      {
	  test1.addElement(String.valueOf(tt.time_stamps.ptpars().dt));
	  test1.addElement(String.valueOf(tt.time_stamps.ptpars().end_time));
      }

//      tp.addTest(test1);
//      tp.repaint();

	  dispose();
	}
	else
	{
		JOptionPane.showMessageDialog(null, "Заданы не все параметры теста!");
	}
  }

  void jButton4_actionPerformed(ActionEvent e) {
      if (jButton4.getText().equals("Добавить"))
      {
	  test_times.addElement(jTextField4.getText());
	  jList1.setListData(test_times);
      }
      else
      {
	  if(jTextField4.getText().equals(""))
	      test_times.removeElementAt(jList1.getSelectedIndex());
	  else
	      test_times.set(jList1.getSelectedIndex(),jTextField4.getText());
	  jList1.setListData(test_times);
	  jButton4.setText("Добавить");
      }
  }

  void jList1_mouseClicked(MouseEvent e) {
      jButton4.setText("Изменить");
  }

  void jComboBox13_actionPerformed(ActionEvent e) {
    int i = jComboBox13.getSelectedIndex();

    if (i==0)
    {
	String[] tmp1 = {"0.25","0.5","1","2","4","8","16"};
	String[] tmp2 = {"100","200","500"};
	res.clear();
	res.addElement(tmp1);
	pulswd.clear();
	pulswd.addElement(tmp2);
    }
    else if (i==1)
    {
	String[] tmp1 = {"0.5","1","2","4","8","16"};
	String[] tmp2 = {"100","200","500","1000"};
	res.clear();
	res.addElement(tmp1);
	pulswd.clear();
	pulswd.addElement(tmp2);
    }
     else if (i==2)
    {
	String[] tmp1 = {"1","2","4","8","16"};
	String[] tmp2 = {"100","200","500","1000"};
	res.clear();
	res.addElement(tmp1);
	pulswd.clear();
	pulswd.addElement(tmp2);
    }
     else if (i==3)
    {
	String[] tmp1 = {"2","4","8","16"};
	String[] tmp2 = {"100","200","500","1000","5000"};
	res.clear();
	res.addElement(tmp1);
	pulswd.clear();
	pulswd.addElement(tmp2);
    }
     else if (i==4)
    {
	String[] tmp1 = {"4","8","16"};
	String[] tmp2 = {"100","200","500","1000","5000","10000"};
	res.clear();
	res.addElement(tmp1);
	pulswd.clear();
	pulswd.addElement(tmp2);
    }
     else if (i==5)
    {
	String[] tmp1 = {"8","16"};
	String[] tmp2 = {"100","200","500","1000","5000","10000"};
	res.clear();
	res.addElement(tmp1);
	pulswd.clear();
	pulswd.addElement(tmp2);
    }
     else if (i==6)
    {
	String[] tmp1 = {"16"};
	String[] tmp2 = {"100","200","500","1000","5000","10000","20000"};
	res.clear();
	res.addElement(tmp1);
	pulswd.clear();
	pulswd.addElement(tmp2);
    }
    jComboBox12.removeAllItems();
    jComboBox11.removeAllItems();

    String[] tmp3 = (String[]) res.get(0);
    String[] tmp4 = (String[]) pulswd.get(0);

    for (i = 0; i <tmp3.length; i++)
	jComboBox12.addItem(tmp3[i]);
    for (i = 0; i <tmp4.length; i++)
	jComboBox11.addItem(tmp4[i]);

  }

  void jComboBox3_actionPerformed(ActionEvent e) {
      if (jComboBox3.getSelectedIndex()==0)
      {
	jPanel7.removeAll();
	jPanel7.add(jLabel110,   new XYConstraints(23, 61, -1, -1));
	jPanel7.add(jLabel19,  new XYConstraints(25, 28, -1, -1));
	jLabel19.setText("Показатель отражения волокна");
	jPanel7.add(jLabel111,  new XYConstraints(24, 96, -1, -1));
	jPanel7.add(jComboBox10, new XYConstraints(262, 94, 52, -1));
	jPanel7.add(jComboBox9,   new XYConstraints(262, 60, 52, -1));
	jPanel7.add(jTextField5,   new XYConstraints(261, 26, 51, -1));
	jPanel7.add(jLabel114,   new XYConstraints(23, 133, -1, -1));
	jPanel7.add(jComboBox13,  new XYConstraints(262, 129, 52, -1));
	jPanel7.add(jComboBox11,    new XYConstraints(262, 202, 52, -1));
	jPanel7.add(jLabel113, new XYConstraints(24, 170, -1, -1));
	jPanel7.add(jLabel112,  new XYConstraints(24, 203, -1, -1));
	jPanel7.add(jLabel22,   new XYConstraints(315, 96, -1, -1));
	jPanel7.add(jComboBox12, new XYConstraints(262, 166, 52, -1));
	jPanel7.repaint();
    }
    else if (jComboBox3.getSelectedIndex()==1)
    {
	jPanel7.removeAll();
	jPanel7.add(jLabel19,  new XYConstraints(25, 28, -1, -1));
	jLabel19.setText("Идентификатор измерения");
	jPanel7.add(jComboBox9,   new XYConstraints(262, 26, 400, -1));
	jPanel7.repaint();
    }
  }

  void jComboBox8_actionPerformed(ActionEvent e) {
      tp.repaint();
  }

}

class MonthCanvas extends Container {
    static String[] monthNames;
    static String[] weekNames;
    int day_temp;

    MonthCanvas(Calendar cal, int day_temp) {
	this.day_temp=day_temp;
	setLayout(new BorderLayout());

	// Add the month label.
	JPanel p = new JPanel(new GridLayout(2, 1));
	p.setPreferredSize( new Dimension(220,40));
	String month=monthNames[cal.get(Calendar.MONTH)];

		if (month.equals("January"))
		    month="\u042F\u043D\u0432\u0430\u0440\u044C";
		if (month.equals("February"))
		    month="\u0424\u0435\u0432\u0440\u0430\u043B\u044C";
		if (month.equals("March"))
		    month="\u041C\u0430\u0440\u0442";
		if (month.equals("April"))
		    month="\u0410\u043F\u0440\u0435\u043B\u044C";
		if (month.equals("May"))
		    month="\u041C\u0430\u0439";
		if (month.equals("June"))
		    month="\u0418\u044E\u043D\u044C";
		if (month.equals("July"))
		    month="\u0418\u044E\u043B\u044C";
		if (month.equals("August"))
		    month="\u0410\u0432\u0433\u0443\u0441\u0442";
		if (month.equals("September"))
		    month="\u0421\u0435\u043D\u0442\u044F\u0431\u0440\u044C";
		if (month.equals("October"))
		    month="\u041E\u043A\u0442\u044F\u0431\u0440\u044C";
		if (month.equals("November"))
		    month="\u041D\u043E\u044F\u0431\u0440\u044C";
		if (month.equals("December"))
		    month="\u0414\u0435\u043A\u0430\u0431\u0440\u044C";

	JLabel l = new JLabel(month, JLabel.CENTER);
	p.setBackground(Color.black);
	//l.setBackground(Color.black);
	l.setForeground(Color.white);
	p.add(l);

	// Add the day of the week labels.
	JPanel q = new JPanel(new GridLayout(0, weekNames.length));
	for (int i=0; i<weekNames.length; i++) {
	    l = new JLabel(weekNames[i], JLabel.CENTER);
	    q.setBackground(Color.gray);
	    q.add(l);
	}
	p.add(q);
	add(p, BorderLayout.NORTH);

	// Create a grid for the days.
	JPanel p1 = new JPanel(new GridLayout(0, weekNames.length));
	p1.setPreferredSize( new Dimension(200,110));

	// Get the day of the week of the first day.
	cal.set(Calendar.DAY_OF_MONTH, 1);
	int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

	// Fill the first few cells with blanks.
	for (int i=cal.getFirstDayOfWeek(); i<dayOfWeek; i++) {
	    p1.add(new JLabel(" ",JLabel.CENTER));
	}

	// Add the days.
	for (int i=cal.getMinimum(Calendar.DAY_OF_MONTH);
		i<=cal.getMaximum(Calendar.DAY_OF_MONTH); i++)
	    {
		Calendar c = Calendar.getInstance();
		c.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), i);

		// Make sure we haven't skipped to the next month.
		if (c.get(Calendar.MONTH) != cal.get(Calendar.MONTH))
		{
		    break;
		}
		    p1.add(new DayCanvas(c, day_temp));
	    }
	add(p1, BorderLayout.CENTER);
    }

    static {
	// Initialize month names.
	Calendar cal = Calendar.getInstance();
	monthNames = new String[cal.getMaximum(Calendar.MONTH) -
				cal.getMinimum(Calendar.MONTH) + 1];

	// Roll the calendar until it uses the first month.
	while (cal.get(Calendar.MONTH) != cal.getMinimum(Calendar.MONTH)) {
	    cal.roll(Calendar.MONTH, false);
	}

	// Use SimpleDateFormat to fill monthNames.
	for (int i=0; i<monthNames.length; i++) {
	    SimpleDateFormat sdf = new SimpleDateFormat("MMMM");
	    monthNames[i] = sdf.format(cal.getTime());
	    cal.roll(Calendar.MONTH, true);
	}

	// Initialize day of week names.  The first element contains the name
	// of getFirstDayOfWeek().
	weekNames = new String[cal.getMaximum(Calendar.DAY_OF_WEEK) -
			       cal.getMinimum(Calendar.DAY_OF_WEEK)+1];

	// Roll the calendar until it uses the first day of week.
	while (cal.get(Calendar.DAY_OF_WEEK) != cal.getFirstDayOfWeek()) {
	    cal.roll(Calendar.DAY_OF_WEEK, false);
	}

	// Use SimpleDateFormat to fill weekNames.
	for (int i=0; i<weekNames.length; i++) {
	    SimpleDateFormat sdf = new SimpleDateFormat("E");
	    weekNames[i] = sdf.format(cal.getTime());

	    // Only take the first two characters.
	    if (weekNames[i].length() > 2)
	    {
		if (weekNames[i].substring(0, 2).equals("Mo"))
		    weekNames[i]="\u041F\u043D";
		if (weekNames[i].substring(0, 2).equals("Tu"))
		    weekNames[i]="\u0412\u0442";
		if (weekNames[i].substring(0, 2).equals("We"))
		    weekNames[i]="\u0421\u0440";
		if (weekNames[i].substring(0, 2).equals("Th"))
		    weekNames[i]="\u0427\u0442";
		if (weekNames[i].substring(0, 2).equals("Fr"))
		    weekNames[i]="\u041F\u0442";
		if (weekNames[i].substring(0, 2).equals("Sa"))
		    weekNames[i]="\u0421\u0431";
		if (weekNames[i].substring(0, 2).equals("Su"))
		    weekNames[i]="\u0412\u0441";
		if (weekNames[i].length() > 2)
		weekNames[i] = weekNames[i].substring(0, 2);
	    }
	    cal.roll(Calendar.DAY_OF_WEEK, true);
	}
    }
}

class DayCanvas extends Component implements MouseListener {
    int day;
    int day_temp;
    Calendar cal;

    DayCanvas(Calendar cal, int day_temp) {
	this.cal = cal;
	this.day_temp = day_temp;
	addMouseListener(this);
    }

    public void paint(Graphics g) {
    super.paint(g);
	int day = cal.get(Calendar.DAY_OF_MONTH);
	int week = cal.get(Calendar.DAY_OF_WEEK);
	int month = cal.get(Calendar.MONTH);
	int year = cal.get(Calendar.YEAR);
	int hour = cal.get(Calendar.HOUR);
	int minute = cal.get(Calendar.MINUTE);
	int second = cal.get(Calendar.SECOND);

	FontMetrics fm = g.getFontMetrics();

	if (day==day_temp)
	    g.setColor(Color.blue);
	else
	    g.setColor(Color.black);
	g.drawString(""+day,
	    (getSize().width-fm.stringWidth(""+day))/2,
	    (getSize().height-fm.getHeight())/2+fm.getAscent());
    }

    // Event handling methods.
    int[] fieldValues = {
	Calendar.ERA, Calendar.YEAR, Calendar.MONTH,
	Calendar.DAY_OF_MONTH, Calendar.DAY_OF_WEEK,
	Calendar.DAY_OF_WEEK_IN_MONTH, Calendar.DAY_OF_YEAR,
	Calendar.WEEK_OF_MONTH, Calendar.WEEK_OF_YEAR,
	Calendar.HOUR, Calendar.HOUR_OF_DAY, Calendar.AM_PM,
	Calendar.MILLISECOND, Calendar.MINUTE, Calendar.SECOND,
	Calendar.ZONE_OFFSET, Calendar.DST_OFFSET,};
    String[] fieldNames = {
	"ERA", "YEAR", "MONTH",
	"DAY_OF_MONTH", "DAY_OF_WEEK",
	"DAY_OF_WEEK_IN_MONTH", "DAY_OF_YEAR",
	"WEEK_OF_MONTH", "WEEK_OF_YEAR",
	"HOUR", "HOUR_OF_DAY", "AM_PM",
	"MILLISECOND", "MINUTE", "SECOND",
	"ZONE_OFFSET", "DST_OFFSET", };

    public void mousePressed(MouseEvent e) {
	System.out.println("---------------------------------");
	for (int i=0; i<Calendar.FIELD_COUNT; i++) {
	    System.out.print(fieldNames[i] + "=" + cal.get(fieldValues[i]));
	    System.out.println("        ("+cal.getMaximum(fieldValues[i])+")");
	}

    }
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
}

class CurrentTime extends Thread {
    JLabel jLabel7;
    public CurrentTime(JLabel jLabel7){
    this.jLabel7=jLabel7;
    }
    public void run()
    {
	while(true)
	{
	    GregorianCalendar kalend = new GregorianCalendar();
	    String text=kalend.getTime().toString();
	    String text1="",text2="";

	    if (text.substring(0,3).equals("Mon"))
		text1="\u041F\u043D\u0434";
	    if (text.substring(0,3).equals("Teu"))
		text1="\u0412\u0442\u043D";
	    if (text.substring(0,3).equals("Wed"))
		text1="\u0421\u0440\u0434";
	    if (text.substring(0,3).equals("Thu"))
		text1="\u0427\u0442\u0432";
	    if (text.substring(0,3).equals("Fri"))
		text1="\u041F\u0442\u043D";
	    if (text.substring(0,3).equals("Sat"))
		text1="\u0421\u0443\u0431";
	    if (text.substring(0,3).equals("Sun"))
		text1="\u0412\u0441\u043A";

	    if (text.substring(4,7).equals("Jan"))
		text2=" \u042F\u043D\u0432";
	    if (text.substring(4,7).equals("Feb"))
		text2=" \u0424\u0435\u0432";
	    if (text.substring(4,7).equals("Mar"))
		text2=" \u041C\u0430\u0440";
	    if (text.substring(4,7).equals("Apr"))
		text2=" \u0410\u043F\u0440";
	    if (text.substring(4,7).equals("May"))
		text2=" \u041C\u0430\u0439";
	    if (text.substring(4,7).equals("Jun"))
		text2=" \u0418\u044E\u043D";
	    if (text.substring(4,7).equals("Jul"))
		text2=" \u0418\u044E\u043B";
	    if (text.substring(4,7).equals("Aug"))
		text2=" \u0410\u0432\u0433";
	    if (text.substring(4,7).equals("Sep"))
		text2=" \u0421\u0435\u043D";
	    if (text.substring(4,7).equals("Oct"))
		text2=" \u041E\u043A\u0442";
	    if (text.substring(4,7).equals("Nov"))
		text2=" \u041D\u043E\u044F";
	    if (text.substring(4,7).equals("Dec"))
		text2=" \u0414\u0435\u043A";

	    this.jLabel7.setText(text1+text2+text.substring(7,19)+text.substring(23,28));
	    try
	    {
		this.sleep(1000);
	    }
	    catch(InterruptedException e){}
	}
    }
}

class TestPlan extends JPanel {
    Vector tests1;
    Vector test1;
    JComboBox jComboBox8;
    //JViewport jViewport;

    TestPlan(Vector tests1, JComboBox jComboBox8) {
	this.tests1 = tests1;
	this.jComboBox8=jComboBox8;
	//this.jViewport = jViewport;
	setLayout(new XYLayout());

    //jViewport.setView(this);
    //jViewport.setViewSize(new Dimension (400,100));
    this.setPreferredSize(new Dimension (1010,1000));

    /*if (this.jComboBox8.getSelectedIndex()==0)
	this.setPreferredSize(new Dimension (947,1000));
    else if (this.jComboBox8.getSelectedIndex()==1)
	this.setPreferredSize(new Dimension (947,1000));
    else if (this.jComboBox8.getSelectedIndex()==2)
	this.setPreferredSize(new Dimension (969,1000));
    else if (this.jComboBox8.getSelectedIndex()==3)
	this.setPreferredSize(new Dimension (995,1000));
    else if (this.jComboBox8.getSelectedIndex()==4)
	this.setSize(87,1000);*/

    }

    public void addTest(Vector test1)
    {
	this.test1 = test1;
	tests1.addElement(test1);
    }

    public void paint(Graphics g) {
	super.paint(g);
	GregorianCalendar gk = new GregorianCalendar();

	long ctl = gk.getTime().getTime();
	String cts = gk.getTime().toString();
	long btl;
	String bts;
	long ftl;
	String fts;

	int otstup = 5;
	FontMetrics fm = g.getFontMetrics();
	if (this.jComboBox8.getSelectedIndex()==0)
	{
	    g.drawLine(0+otstup,20,960+otstup,20);
	    for (int i = 0; i <= 60; i++)
	    {
		g.setColor(Color.green);
		g.drawLine(i*16+otstup,17,i*16+otstup,23);
		g.setColor(Color.black);
		g.drawString(""+i,16*i-fm.stringWidth(""+i)/2+otstup,16);
	    }
	    g.setColor(Color.blue);

	    Calendar plan_cal = Calendar.getInstance();
	    plan_cal.set(plan_cal.get(Calendar.YEAR), plan_cal.get(Calendar.MONTH), plan_cal.get(Calendar.DAY_OF_MONTH),plan_cal.get(Calendar.HOUR_OF_DAY),plan_cal.get(Calendar.MINUTE),0);
	    btl = plan_cal.getTime().getTime();
	    bts = plan_cal.getTime().toString();

	    plan_cal.set(plan_cal.get(Calendar.YEAR), plan_cal.get(Calendar.MONTH), plan_cal.get(Calendar.DAY_OF_MONTH),plan_cal.get(Calendar.HOUR_OF_DAY),plan_cal.get(Calendar.MINUTE),59);
	    ftl = plan_cal.getTime().getTime();
	    fts = plan_cal.getTime().toString();

	    for (int i = 1; i <= tests1.size(); i++)
	    {
		Vector tmppp = (Vector) tests1.get(i-1);
		g.drawString(tmppp.elementAt(0).toString(),otstup-fm.stringWidth(""+i)/2,20*(i+1)-4);
		g.drawLine(0+otstup,20*(i+1),960+otstup,20*(i+1));

		if (tmppp.elementAt(1).toString().equals("1"))
		{
		    long start = Long.parseLong(tmppp.elementAt(2).toString());
		    long end = Long.parseLong(tmppp.elementAt(4).toString());
		    long step = Long.parseLong(tmppp.elementAt(3).toString());
		    g.setColor(Color.red);
		    while (start < end)
		    {
			g.drawLine((int)((start-btl)*60*16/(ftl-btl)+otstup),20*(i+1)-3,
				    (int)((start-btl)*60*16/(ftl-btl)+otstup),20*(i+1)+3);
			start = start + step;
		    }
		    g.setColor(Color.blue);
		}
		else
		{
		    for (int j = 3; j <= tmppp.size(); j++)
		    {
			long curr_time = Long.parseLong(tmppp.elementAt(j-1).toString());
			//String aa = String.valueOf(new Date(curr_time));
			g.setColor(Color.red);
			g.drawLine((int)((curr_time-btl)*60*16/(ftl-btl)+otstup),20*(i+1)-3,
				    (int)((curr_time-btl)*60*16/(ftl-btl)+otstup),20*(i+1)+3);
			g.setColor(Color.blue);
		    }
		}
	    }
	}
	else if (this.jComboBox8.getSelectedIndex()==1)
	{
	    g.drawLine(0+otstup,20,960+otstup,20);
	    for (int i = 0; i <= 60; i++)
	    {
		g.setColor(Color.green);
		g.drawLine(i*16+otstup,17,i*16+otstup,23);
		g.setColor(Color.black);
		g.drawString(""+i,16*i-fm.stringWidth(""+i)/2+otstup,16);
	    }
	    g.setColor(Color.blue);

	    Calendar plan_cal = Calendar.getInstance();
	    plan_cal.set(plan_cal.get(Calendar.YEAR), plan_cal.get(Calendar.MONTH), plan_cal.get(Calendar.DAY_OF_MONTH),gk.get(Calendar.HOUR_OF_DAY),0,0);
	    btl = plan_cal.getTime().getTime();
	    bts = plan_cal.getTime().toString();

	    plan_cal.set(plan_cal.get(Calendar.YEAR), plan_cal.get(Calendar.MONTH), plan_cal.get(Calendar.DAY_OF_MONTH),gk.get(Calendar.HOUR_OF_DAY),59,59);
	    ftl = plan_cal.getTime().getTime();
	    fts = plan_cal.getTime().toString();

	    for (int i = 1; i <= tests1.size(); i++)
	    {
		Vector tmppp = (Vector) tests1.get(i-1);
		g.drawString(tmppp.elementAt(0).toString(),otstup-fm.stringWidth(""+i)/2,20*(i+1)-4);
		g.drawLine(0+otstup,20*(i+1),960+otstup,20*(i+1));
		if (tmppp.elementAt(1).toString().equals("1"))
		{
		    long start = Long.parseLong(tmppp.elementAt(2).toString());
		    long end = Long.parseLong(tmppp.elementAt(4).toString());
		    long step = Long.parseLong(tmppp.elementAt(3).toString());
		    g.setColor(Color.red);
		    while (start < end)
		    {
			g.drawLine((int)((start-btl)*60*16/(ftl-btl)+otstup),20*(i+1)-3,
				    (int)((start-btl)*60*16/(ftl-btl)+otstup),20*(i+1)+3);
			start = start + step;
		    }
		    g.setColor(Color.blue);
		}
		else
		{
		    for (int j = 3; j <= tmppp.size(); j++)
		    {
			long curr_time = Long.parseLong(tmppp.elementAt(j-1).toString());
			String aa = String.valueOf(new Date(curr_time));
			g.setColor(Color.red);
			g.drawLine((int)((curr_time-btl)*60*16/(ftl-btl)+otstup),20*(i+1)-3,
				    (int)((curr_time-btl)*60*16/(ftl-btl)+otstup),20*(i+1)+3);
			g.setColor(Color.blue);
		    }
		}
	    }
	}
	else if (this.jComboBox8.getSelectedIndex()==2)
	{
	    g.drawLine(0+otstup,20,984+otstup,20);
	    for (int i = 0; i <= 24; i++)
	    {
		g.setColor(Color.green);
		g.drawLine(i*41+otstup,17,i*41+otstup,23);
		g.setColor(Color.black);
		g.drawString(""+i,41*i-fm.stringWidth(""+i)/2+otstup,16);
	    }
	    g.setColor(Color.blue);

	    Calendar plan_cal = Calendar.getInstance();
	    plan_cal.set(plan_cal.get(Calendar.YEAR), plan_cal.get(Calendar.MONTH), plan_cal.get(Calendar.DAY_OF_MONTH),0,0,0);
	    btl = plan_cal.getTime().getTime();
	    bts = plan_cal.getTime().toString();

	    plan_cal.set(plan_cal.get(Calendar.YEAR), plan_cal.get(Calendar.MONTH), plan_cal.get(Calendar.DAY_OF_MONTH),23,59,59);
	    ftl = plan_cal.getTime().getTime();
	    fts = plan_cal.getTime().toString();

	    for (int i = 1; i <= tests1.size(); i++)
	    {
		Vector tmppp = (Vector) tests1.get(i-1);
		g.drawString(tmppp.elementAt(0).toString(),otstup-fm.stringWidth(""+i)/2,20*(i+1)-4);
		g.drawLine(0+otstup,20*(i+1),984+otstup,20*(i+1));
		if (tmppp.elementAt(1).toString().equals("1"))
		{
		    long start = Long.parseLong(tmppp.elementAt(2).toString());
		    long end = Long.parseLong(tmppp.elementAt(4).toString());
		    long step = Long.parseLong(tmppp.elementAt(3).toString());
		    g.setColor(Color.red);
		    while (start < end)
		    {
			g.drawLine((int)((start-btl)*24*41/(ftl-btl)+otstup),20*(i+1)-3,
				    (int)((start-btl)*24*41/(ftl-btl)+otstup),20*(i+1)+3);
			start = start + step;
		    }
		    g.setColor(Color.blue);
		}
		else
		{
		    for (int j = 3; j <= tmppp.size(); j++)
		    {
			long curr_time = Long.parseLong(tmppp.elementAt(j-1).toString());
			String aa = String.valueOf(new Date(curr_time));
			g.setColor(Color.red);
			g.drawLine((int)((curr_time-btl)*24*41/(ftl-btl)+otstup),20*(i+1)-3,
				    (int)((curr_time-btl)*24*41/(ftl-btl)+otstup),20*(i+1)+3);
			g.setColor(Color.blue);
		    }
		}
	    }
	}
	else if (this.jComboBox8.getSelectedIndex()==3)
	{
	    g.drawLine(0+otstup,20,990+otstup,20);
	    for (int i = 0; i <= 30; i++)
	    {
		g.setColor(Color.green);
		g.drawLine(i*33+otstup,17,i*33+otstup,23);
		g.setColor(Color.black);
		g.drawString(""+(i+1),33*i-fm.stringWidth(""+(i+1))/2+otstup,16);
	    }
	    g.setColor(Color.blue);

	    Calendar plan_cal = Calendar.getInstance();
	    plan_cal.set(plan_cal.get(Calendar.YEAR), plan_cal.get(Calendar.MONTH), 1, 0, 0, 0);
	    btl = plan_cal.getTime().getTime();
	    bts = plan_cal.getTime().toString();

	    plan_cal.set(plan_cal.get(Calendar.YEAR), plan_cal.get(Calendar.MONTH), 31,23,59,59);
	    ftl = plan_cal.getTime().getTime();
	    fts = plan_cal.getTime().toString();

	    for (int i = 1; i <= tests1.size(); i++)
	    {
		Vector tmppp = (Vector) tests1.get(i-1);
		g.drawString(tmppp.elementAt(0).toString(),otstup-fm.stringWidth(""+(i+1))/2,20*(i+1)-4);
		g.drawLine(0+otstup,20*(i+1),990+otstup,20*(i+1));
		if (tmppp.elementAt(1).toString().equals("1"))
		{
		    long start = Long.parseLong(tmppp.elementAt(2).toString());
		    long end = Long.parseLong(tmppp.elementAt(4).toString());
		    long step = Long.parseLong(tmppp.elementAt(3).toString());
		    g.setColor(Color.red);
		    while (start < end)
		    {
			g.drawLine((int)((start-btl)*33*30/(ftl-btl)+otstup),20*(i+1)-3,
				    (int)((start-btl)*33*30/(ftl-btl)+otstup),20*(i+1)+3);
			start = start + step;
		    }
		    g.setColor(Color.blue);
		}
		else
		{
		    for (int j = 3; j <= tmppp.size(); j++)
		    {
			long curr_time = Long.parseLong(tmppp.elementAt(j-1).toString());
			String aa = String.valueOf(new Date(curr_time));
			g.setColor(Color.red);
			g.drawLine((int)((curr_time-btl)*33*30/(ftl-btl)+otstup),20*(i+1)-3,
				    (int)((curr_time-btl)*33*30/(ftl-btl)+otstup),20*(i+1)+3);
			g.setColor(Color.blue);
		    }
		}
	    }
	}
	else if (this.jComboBox8.getSelectedIndex()==4)
	{
	    g.drawLine(0+otstup,20,990+otstup,20);
	    for (int i = 0; i <= 11; i++)
	    {
		g.setColor(Color.green);
		g.drawLine(i*90+otstup,17,i*90+otstup,23);
		g.setColor(Color.black);
		g.drawString(""+(i+1),90*i-fm.stringWidth(""+(i+1))/2+otstup,16);
	    }
	    g.setColor(Color.blue);

	    Calendar plan_cal = Calendar.getInstance();
	    plan_cal.set(plan_cal.get(Calendar.YEAR), 0, 1, 0, 0, 0);
	    btl = plan_cal.getTime().getTime();
	    bts = plan_cal.getTime().toString();

	    plan_cal.set(plan_cal.get(Calendar.YEAR), 11, 31, 23, 59, 59);
	    ftl = plan_cal.getTime().getTime();
	    fts = plan_cal.getTime().toString();

	    for (int i = 1; i <= tests1.size(); i++)
	    {
		Vector tmppp = (Vector) tests1.get(i-1);
		g.drawString(tmppp.elementAt(0).toString(),otstup-fm.stringWidth(""+i)/2,20*(i+1)-4);
		g.drawLine(0+otstup,20*(i+1),990+otstup,20*(i+1));
		if (tmppp.elementAt(1).toString().equals("1"))
		{
		    long start = Long.parseLong(tmppp.elementAt(2).toString());
		    long end = Long.parseLong(tmppp.elementAt(4).toString());
		    long step = Long.parseLong(tmppp.elementAt(3).toString());
		    g.setColor(Color.red);
		    while (start < end)
		    {
			g.drawLine((int)((start-btl)*11*90/(ftl-btl)+otstup),20*(i+1)-3,
				    (int)((start-btl)*11*90/(ftl-btl)+otstup),20*(i+1)+3);
			start = start + step;
		    }
		    g.setColor(Color.blue);
		}
		else
		{
		    for (int j = 3; j <= tmppp.size(); j++)
		    {
			long curr_time = Long.parseLong(tmppp.elementAt(j-1).toString());
			String aa = String.valueOf(new Date(curr_time));
			g.setColor(Color.red);
			g.drawLine((int)((curr_time-btl)*11*90/(ftl-btl)+otstup),20*(i+1)-3,
				    (int)((curr_time-btl)*11*90/(ftl-btl)+otstup),20*(i+1)+3);
			//g.drawOval((int)((curr_time-btl)*11*82/(ftl-btl)+otstup-1),20*(i+1)-5,10,10);
			//g.fillOval((int)((curr_time-btl)*11*82/(ftl-btl)+otstup-1),20*(i+1)-5,10,10);
			g.setColor(Color.blue);
		    }
		}
	    }
	}
    }
}
