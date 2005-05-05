package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import com.syrus.AMFICOM.Client.Analysis.AnalysisUtil;
import com.syrus.AMFICOM.Client.Analysis.CompositeEventList;
import com.syrus.AMFICOM.Client.Analysis.Heap;
import com.syrus.AMFICOM.Client.General.Event.CurrentEventChangeListener;
import com.syrus.AMFICOM.Client.General.Event.EtalonMTMListener;
import com.syrus.AMFICOM.Client.General.Event.PrimaryRefAnalysisListener;
import com.syrus.AMFICOM.Client.General.Lang.LangModelAnalyse;
import com.syrus.AMFICOM.Client.General.Model.AnalysisResourceKeys;
import com.syrus.AMFICOM.Client.General.UI.ATable;
import com.syrus.AMFICOM.Client.General.UI.FixedSizeEditableTableModel;
import com.syrus.AMFICOM.Client.General.UI.UIGeneralStorage;
import com.syrus.AMFICOM.Client.Resource.ResourceKeys;
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEventComparer;
import com.syrus.AMFICOM.analysis.dadara.events.ConnectorDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEventUtil;
import com.syrus.AMFICOM.analysis.dadara.events.EndOfTraceDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.LinearDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.NotIdentifiedDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.SpliceDetailedEvent;
import com.syrus.io.BellcoreStructure;

public class EventsFrame extends ATableFrame
implements EtalonMTMListener, PrimaryRefAnalysisListener,
    CurrentEventChangeListener
{
	private static final String DASH = "-----";

	private FixedSizeEditableTableModel tModel;
	private JTable jTable;

	private JPanel mainPanel = new JPanel();
	private JScrollPane scrollPane = new JScrollPane();
	private JViewport viewport = new JViewport();

    protected static class TableView {
        public static final int COMP = 10;
        public static final int PRIM = 11;
        public static final int ETAL = 12;
        private int viewMode;
        public TableView(int viewMode) {
            this.viewMode = viewMode;
        }
        public int nRows(CompositeEventList eList) {
            switch(viewMode) {
            case COMP: return eList.getNCompositeEvents();
            case PRIM: return eList.getNEvents();
            case ETAL: return eList.getNEtalonEvents();
            default: throw new InternalError();
            }
        }
        public int nPri1(CompositeEventList eList, int row) {
            switch(viewMode) {
            case COMP: return eList.getC2P(row);
            case PRIM: return row;
            case ETAL: return eList.getC2P(eList.getE2C(row));
            default: throw new InternalError();
            }
        }
        public int nEt1(CompositeEventList eList, int row) {
            switch(viewMode) {
            case COMP: return eList.getC2E(row);
            case PRIM: return eList.getC2E(eList.getP2C(row));
            case ETAL: return row;
            default: throw new InternalError();
            }
        }
        public void toNextRow(CompositeEventList.Walker walker) {
            switch(viewMode) {
            case COMP:
                walker.toNextCompositeEvent();
                break;
            case PRIM:
                walker.toNextEvent();
                break;
            case ETAL:
                walker.toNextEtalonEvent();
                break;
            default:
                throw new InternalError();
            }
        }
        /*public int currentRow(CompositeEventList.Walker walker) {
            switch(viewMode) {
            case COMP: return walker.getCompositeEvent();
            case PRIM: return walker.getEvent2();
            case ETAL: return walker.getEtalonEvent2();
            default: throw new InternalError();
            }
        }*/
        public int currentRow2() {
            switch(viewMode) {
            case COMP: return Heap.getCurrentCompositeEvent();
            case PRIM: return Heap.getCurrentEvent2();
            case ETAL: return Heap.getCurrentEtalonEvent2();
            default: throw new InternalError();
            }
        }
    }

    protected TableView view; // State

	public EventsFrame()
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

		this.initModule();
	}

	private void initModule()
	{
        view = new TableView(TableView.PRIM); // XXX; другие виды пока не поддерживаются
		Heap.addEtalonMTMListener(this);
		Heap.addCurrentEventChangeListener(this);
        Heap.addPrimaryRefAnalysisListener(this);
	}

	private void selectRow(int row) {
		this.jTable.setRowSelectionInterval(row, row);
		this.jTable.scrollRectToVisible(this.jTable.getCellRect(this.jTable.getSelectedRow(), this.jTable.getSelectedColumn(), true));
	}

	public String getReportTitle()
	{
		return LangModelAnalyse.getString("eventTableTitle");
	}

	public TableModel getTableModel()
	{
		return tModel;
	}

	/*
	private void setComparedWithEtalonEventsColor()
	{
		if(getEtalon() == null || getData() == null)
		{
			setNoComparedWithEtalonColor();
			return;
		}

		int []newEvents = SimpleReflectogramEventComparer.getNewEventsList(getData(), getEtalon());
		int []amplChengedEvents = SimpleReflectogramEventComparer.getChangedAmplitudeEventsList(getData(), getEtalon(), .5);
		int []lossChengedEvents = SimpleReflectogramEventComparer.getChangedLossEventsList(getData(), getEtalon(), .5);

		EventTableRenderer rend = (EventTableRenderer)jTable.getDefaultRenderer(Object.class);
		rend.setNewEventsList(newEvents);
		rend.setAmplitudeChangedEventsList(amplChengedEvents);
		rend.setLossChangedEventsList(lossChengedEvents);
		this.jTable.revalidate();
		this.jTable.repaint();
        
        // FIXME: debug: development-time console code
        ModelTraceComparer.compareMTAEToMTM(Heap.getMTAEPrimary(), Heap.getMTMEtalon()); // XXX: will crush if no etalon will be at this moment
	}

	private void setNoComparedWithEtalonColor()
	{
		EventTableRenderer rend = (EventTableRenderer)jTable.getDefaultRenderer(Object.class);
		rend.setNewEventsList(null);
		rend.setAmplitudeChangedEventsList(null);
		rend.setLossChangedEventsList(null);
		this.jTable.revalidate();
		this.jTable.repaint();
	}
    */

	private void jbInit() throws Exception
	{
		setFrameIcon((Icon) UIManager.get(ResourceKeys.ICON_GENERAL));
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);

		tModel = new FixedSizeEditableTableModel (
					new String[] {LangModelAnalyse.getString("eventNum"),
												LangModelAnalyse.getString("eventType"),
												LangModelAnalyse.getString("eventStartLocationKM"),
												LangModelAnalyse.getString("eventLengthKM"),
												LangModelAnalyse.getString("eventReflectanceDB"),
												LangModelAnalyse.getString("eventLossDB"),
												LangModelAnalyse.getString("eventLeadAttenuationDBKM")},
					new Object[] {"", "", "", "", "", "", "", ""},
					null,
					null);

		jTable = new ATable(tModel);

		{
			FontMetrics fontMetrics = this.jTable.getFontMetrics(this.jTable.getFont());			
			jTable.getColumnModel().getColumn(0).setPreferredWidth(fontMetrics.stringWidth("WW"));
			jTable.getColumnModel().getColumn(0).setMaxWidth(fontMetrics.stringWidth("WWWW"));
		}

		setContentPane(mainPanel);
		this.setResizable(true);
		this.setClosable(true);
		this.setIconifiable(true);
		this.setTitle(LangModelAnalyse.getString("eventTableTitle"));

		mainPanel.setLayout(new BorderLayout());
		mainPanel.setBorder(BorderFactory.createLoweredBevelBorder());
		scrollPane.setViewport(viewport);
		scrollPane.setAutoscrolls(true);

		jTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ListSelectionModel rowSM = jTable.getSelectionModel();
		jTable.getColumnModel().setSelectionModel(rowSM);
		rowSM.addListSelectionListener(new ListSelectionListener()
		{
			public void valueChanged(ListSelectionEvent e) {
			//Ignore extra messages.
			if (e.getValueIsAdjusting()) return;

			ListSelectionModel lsm = (ListSelectionModel)e.getSource();
			if (lsm.isSelectionEmpty())
			{
						//no rows are selected
			}
			else
			{
				int selected = lsm.getMinSelectionIndex();
                if (Heap.getCurrentEvent2() != selected)
                    Heap.setCurrentEvent(selected);
			}
		}
		});

//		jTable.setPreferredScrollableViewportSize(new Dimension(200, 213));
//		jTable.setMinimumSize(new Dimension(200, 213));

		jTable.setDefaultRenderer(Object.class, new EventTableRenderer(jTable));

		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
	}

	private void updateTableModel()
	{
        // подгоняем текущее выделение к тому месту в таблице,
        // которое должно соответствовать текущей паре событий
        // XXX: если текущего выделения не должно быть, надо бы его убрать.
		int nEvent = view.currentRow2();
		if (nEvent != -1 && nEvent < this.jTable.getRowCount())
		{
			this.jTable.setRowSelectionInterval(nEvent, nEvent);
			this.jTable.scrollRectToVisible(this.jTable.getCellRect(
                    this.jTable.getSelectedRow(),
                    this.jTable.getSelectedColumn(),
                    true));
		}
	}

    private void updateColors() {
        this.jTable.revalidate();
        this.jTable.repaint();
        /*
        if (Heap.getMTMEtalon() == null)
            setNoComparedWithEtalonColor();
        else
            setComparedWithEtalonEventsColor();
        */
    }

	private void setTableModel()
	{
        if (Heap.getMTAEPrimary() == null)
            return; // XXX
        DetailedEvent[] devents = Heap.getMTAEPrimary().getDetailedEvents();
        BellcoreStructure bs = Heap.getBSPrimaryTrace();
        double resMt = bs.getResolution();
        double resKm = resMt / 1000.0;
		double sigma = MathRef.calcSigma(bs.getWavelength(), bs.getPulsewidth());

        int nRows = view.nRows(Heap.getEventList());
		tModel.clearTable();

        CompositeEventList.Walker w = Heap.getEventList().new Walker();
		for (int i = 0; i < nRows; i++, view.toNextRow(w))
		{
//            // выбираем событие, которое будем отображать
//            int nPrimary = w.getEvent2();
//            int nEtalon = w.getEtalonEvent2();
//            // nPrimary или nEtalon может быть -1, но не оба одновременно
//			SimpleReflectogramEvent sre = nPrimary >= 0
//                ? Heap.getMTAEPrimary().getSimpleEvent(nPrimary)
//                : Heap.getMTMEtalon().getMTAE().getSimpleEvent(nEtalon);

            int sType = devents[i].getEventType();
			String eventTypeName = AnalysisUtil.getSimpleEventNameByType(sType);
            String vCol1 = eventTypeName;
            String vCol2 = Double.toString(MathRef.round_3(
                    resKm * devents[i].getBegin())); //начало
            String vCol3 = Double.toString(MathRef.round_3(
                    resKm * (DetailedEventUtil.getWidth(devents[i])))); //протяженность

			switch (sType)
			{
			case SimpleReflectogramEvent.DEADZONE:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
					 vCol1,
                     vCol2,
                     vCol3,
					 DASH, // отраж
					 DASH, // потери
					 DASH  // затух
				});
				break;
			case SimpleReflectogramEvent.LINEAR:
			    // TODO: использовать только один параметр в data[] вместо трех
				tModel.addRow(String.valueOf(i + 1), new Object[] {
                     vCol1,
                     vCol2,
                     vCol3,
					 DASH, // отраж
					 Double.toString(MathRef.round_3(((LinearDetailedEvent)devents[i]).getLoss())), // потери
					 Double.toString(MathRef.round_4(((LinearDetailedEvent)devents[i]).getAttenuation() / resKm)) //затух
				});
				break;
			case SimpleReflectogramEvent.NOTIDENTIFIED:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
                    vCol1,
                    vCol2,
                    vCol3,
                    DASH, // отраж
                    //dash, // потери
                    Double.toString(MathRef.round_3(((NotIdentifiedDetailedEvent)devents[i]).getLoss())), // потери
                    DASH  // затух
				});
				break;
			case SimpleReflectogramEvent.CONNECTOR:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
                    vCol1,
                    vCol2,
                    vCol3,
                    Double.toString(MathRef.round_2(MathRef.calcReflectance(sigma, ((ConnectorDetailedEvent)devents[i]).getAmpl()))), // отраж
                    Double.toString(MathRef.round_3(((ConnectorDetailedEvent)devents[i]).getLoss())), // потери
                    DASH  // затух
				});
				break;
			case SimpleReflectogramEvent.GAIN:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
                    vCol1,
                    vCol2,
                    vCol3,
				    DASH, // отраж
				    Double.toString(MathRef.round_3(((SpliceDetailedEvent)devents[i]).getLoss())), // потери
				    DASH  // затух
				});
				break;
			case SimpleReflectogramEvent.LOSS:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
                    vCol1,
                    vCol2,
                    vCol3,
				    DASH, // отраж
				    Double.toString(MathRef.round_3(((SpliceDetailedEvent)devents[i]).getLoss())), // потери
				    DASH  // затух
				});
				break;
			case SimpleReflectogramEvent.ENDOFTRACE:
				tModel.addRow(String.valueOf(i + 1), new Object[] {
                    vCol1,
                    vCol2,
                    vCol3,
				    Double.toString(MathRef.round_2(MathRef.calcReflectance(sigma, ((EndOfTraceDetailedEvent)devents[i]).getAmpl()))), // отраж
				    DASH, // потери
				    DASH  // затух
				});
				break;
			}			
		}
	}
	
	private class EventTableRenderer extends DefaultTableCellRenderer
	{
		JTable table;
        int viewMode;

		public EventTableRenderer(JTable table)
		{
			this.table = table;
		}

		public Component getTableCellRendererComponent(	JTable table1,
														Object value,
														boolean isSelected1,
														boolean hasFocus,
														int row,
														int column) {
			Component c = super.getTableCellRendererComponent(table1, value,
                    isSelected1, hasFocus, row, column);

			int nPrimary = view.nPri1(Heap.getEventList(), row);
			int nEtalon = view.nEt1(Heap.getEventList(), row);

            //SimpleReflectogramEventComparer comp = Heap.getEventComparer();
            String colorCode = null;

            if (Heap.getMTMEtalon() == null) {
                // no etalon - use default colors
            } else if (nPrimary < 0) {
                colorCode = isSelected1
                    ? AnalysisResourceKeys.COLOR_EVENTS_LOST_SELECTED
                    : AnalysisResourceKeys.COLOR_EVENTS_LOST;
            } else if (nEtalon < 0) {
                colorCode = isSelected1
                ? AnalysisResourceKeys.COLOR_EVENTS_NEW_SELECTED
                : AnalysisResourceKeys.COLOR_EVENTS_NEW;
            } else {
                //System.err.println("row " + row + " column " + column);
                // FIXME: add MTAE.getComplexEvent(int) or add caching of output CE[] in MTAE
                DetailedEvent pri =
                    Heap.getMTAEPrimary().getDetailedEvents()[nPrimary];
                DetailedEvent et =
                    Heap.getMTMEtalon().getMTAE().getDetailedEvents()[nEtalon];
                if (SimpleReflectogramEventComparer.eventsAreDifferent(pri, et,
                        SimpleReflectogramEventComparer.CHANGETYPE_LOSS,
                        0.5))
                    colorCode = isSelected1
                    ? AnalysisResourceKeys.COLOR_EVENTS_LOSS_CHANGED_SELECTED
                    : AnalysisResourceKeys.COLOR_EVENTS_LOSS_CHANGED;
                else if (SimpleReflectogramEventComparer.eventsAreDifferent(pri, et,
                        SimpleReflectogramEventComparer.CHANGETYPE_AMPL,
                        0.5))
                    colorCode = isSelected1
                    ? AnalysisResourceKeys.COLOR_EVENTS_AMPLITUDE_CHANGED_SELECTED
                    : AnalysisResourceKeys.COLOR_EVENTS_AMPLITUDE_CHANGED;
                // no change - use default colors
            }

			if (colorCode != null)
				c.setForeground(UIManager.getColor(colorCode));
            else
                c.setForeground(Color.BLACK); // XXX: default color

			return c;
		}
	}

	public void etalonMTMCUpdated()
	{
        updateColors();
	}

	public void etalonMTMRemoved()
	{
        updateColors();
	}

	public void currentEventChanged()
	{
		updateTableModel();
	}

    public void primaryRefAnalysisCUpdated() {
        updateColors();
        if (Heap.getRefAnalysisPrimary() != null)
        {
            setTableModel();
            updateTableModel();
            UIGeneralStorage.arrangeTableColumns(this.jTable);
        }
        setVisible(true);
    }

    public void primaryRefAnalysisRemoved() {
        tModel.clearTable();
        this.jTable.revalidate();
        this.jTable.repaint();
        setVisible(false);
    }
}
