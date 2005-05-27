package com.syrus.AMFICOM.Client.Analysis.Reflectometry.UI;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
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
import com.syrus.AMFICOM.analysis.dadara.MathRef;
import com.syrus.AMFICOM.analysis.dadara.ModelTraceComparer;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEvent;
import com.syrus.AMFICOM.analysis.dadara.SimpleReflectogramEventComparer;
import com.syrus.AMFICOM.analysis.dadara.events.ConnectorDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.DetailedEventUtil;
import com.syrus.AMFICOM.analysis.dadara.events.EndOfTraceDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.LinearDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.NotIdentifiedDetailedEvent;
import com.syrus.AMFICOM.analysis.dadara.events.SpliceDetailedEvent;
import com.syrus.AMFICOM.client.UI.*;
import com.syrus.AMFICOM.client.UI.ATable;
import com.syrus.AMFICOM.client.resource.ResourceKeys;
import com.syrus.io.BellcoreStructure;

public class EventsFrame extends ATableFrame
implements EtalonMTMListener, PrimaryRefAnalysisListener,
    CurrentEventChangeListener
{
	private static final String DASH = "-----";

	private FixedSizeEditableTableModel tModel;
	JTable jTable;

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
        public int nComp(CompositeEventList eList, int row) {
            switch(viewMode) {
            case COMP: return row;
            case PRIM: return eList.getP2C(row);
            case ETAL: return eList.getE2C(row);
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
        public int currentRow2() {
            switch(viewMode) {
            case COMP: return Heap.getCurrentCompositeEvent();
            case PRIM: return Heap.getCurrentEvent2();
            case ETAL: return Heap.getCurrentEtalonEvent2();
            default: throw new InternalError();
            }
        }
        public void moveTo(int row) {
            switch(viewMode) {
            case COMP: Heap.setCurrentCompositeEvent(row); return;
            case PRIM: Heap.setCurrentEvent(row); return;
            case ETAL: Heap.setCurrentEtalonEvent(row); return;
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
        view = new TableView(TableView.PRIM); // @todo: автоматически переключать виды
		Heap.addEtalonMTMListener(this);
		Heap.addCurrentEventChangeListener(this);
        Heap.addPrimaryRefAnalysisListener(this);
	}

	public String getReportTitle()
	{
		return LangModelAnalyse.getString("eventTableTitle");
	}

	public TableModel getTableModel()
	{
		return tModel;
	}

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
                if (view.currentRow2() != selected)
                    view.moveTo(selected);
			}
		}
		});

		final JPopupMenu popupMenu = this.createPopupMenu();

        // FIXME: event edition is currently for internal use only. Should be either improved or disabled in final version. 
		this.jTable.addMouseListener(new MouseAdapter() {

			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)) {
					popupMenu.show((Component) e.getSource(), e.getX(), e.getY());
				}
			}
		});

		jTable.setDefaultRenderer(Object.class, new EventTableRenderer(jTable));

		mainPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.getViewport().add(jTable);
	}
	
	private JPopupMenu createPopupMenu() {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem joinPreviousMenuItem = new JMenuItem("Join previous");
        JMenuItem splitTo2MenuItem = new JMenuItem("Split to 2");
        JMenuItem splitTo3MenuItem = new JMenuItem("Split to 3");
        JMenuItem changeToConnectorMenuItem = new JMenuItem("Change to CONNECTOR");
        JMenuItem changeToGainMenuItem = new JMenuItem("Change to GAIN");
        JMenuItem changeToLinearMenuItem = new JMenuItem("Change to LINEAR");
        JMenuItem changeToLossMenuItem = new JMenuItem("Change to LOSS");
        JMenuItem changeToNonidMenuItem = new JMenuItem("Change to NOT IDENTIFIED");
        JMenuItem moveBeginToMarkerA = new JMenuItem("Move begin to Marker A");
        JMenuItem moveEndToMarkerA = new JMenuItem("Move end to Marker A");

		joinPreviousMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
                Heap.joinCurrentEventWithPrevious();
			}
		});

        splitTo2MenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Heap.splitCurrentEventToN(2);
            }
        });

        splitTo3MenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Heap.splitCurrentEventToN(3);
            }
        });

        changeToConnectorMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Heap.changeCurrentEventType(SimpleReflectogramEvent.CONNECTOR);
            }
        });

        changeToGainMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Heap.changeCurrentEventType(SimpleReflectogramEvent.GAIN);
            }
        });

        changeToLinearMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Heap.changeCurrentEventType(SimpleReflectogramEvent.LINEAR);
            }
        });

        changeToLossMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Heap.changeCurrentEventType(SimpleReflectogramEvent.LOSS);
            }
        });

        changeToNonidMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Heap.changeCurrentEventType(SimpleReflectogramEvent.NOTIDENTIFIED);
            }
        });

        moveBeginToMarkerA.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Heap.hasMarkerPosition())
                    Heap.changeCurrentEventBegin(Heap.getMarkerPosition());
            }
        });

        moveEndToMarkerA.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Heap.hasMarkerPosition())
                    Heap.changeCurrentEventEnd(Heap.getMarkerPosition());
            }
        });

        popupMenu.add(joinPreviousMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(splitTo2MenuItem);
        popupMenu.add(splitTo3MenuItem);
        popupMenu.addSeparator();
        popupMenu.add(changeToLinearMenuItem);
        popupMenu.add(changeToLossMenuItem);
        popupMenu.add(changeToGainMenuItem);
        popupMenu.add(changeToConnectorMenuItem);
        popupMenu.add(changeToNonidMenuItem);
        popupMenu.addSeparator();
        popupMenu.add(moveBeginToMarkerA);
        popupMenu.add(moveEndToMarkerA);
		return popupMenu;
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
    }

	private void setTableModel()
	{
        if (Heap.getMTAEPrimary() == null)
            return; // XXX
        DetailedEvent[] pevents = Heap.getMTAEPrimary().getDetailedEvents();
        DetailedEvent[] eevents = Heap.getMTMEtalon() != null ?
                Heap.getMTMEtalon().getMTAE().getDetailedEvents() : null;
        BellcoreStructure bs = Heap.getBSPrimaryTrace();
        double resMt = bs.getResolution();
        double resKm = resMt / 1000.0;
		double sigma = MathRef.calcSigma(bs.getWavelength(), bs.getPulsewidth());

        int nRows = view.nRows(Heap.getEventList());
		tModel.clearTable();

        CompositeEventList eList = Heap.getEventList();
        CompositeEventList.Walker w = eList.new Walker();
		for (int row = 0; row < nRows; row++, view.toNextRow(w))
		{
            // nPri или nEt может быть -1, но не оба одновременно
            int nPri = view.nPri1(eList, row);
            int nEt = view.nEt1(eList, row);

            // выбираем, параметры какого события будем выводить - primary или etalon
            // предпочтение отдается primary
            DetailedEvent ev = nPri >= 0 ? pevents[nPri] : eevents[nEt];

            int sType = ev.getEventType();

            String vCol1 = AnalysisUtil.getSimpleEventNameByType(sType); // тип
            String vCol2 = Double.toString(MathRef.round_3(
                    resKm * ev.getBegin())); //начало
            String vCol3 = Double.toString(MathRef.round_3(
                    resKm * (DetailedEventUtil.getWidth(ev)))); //протяженность

			switch (sType)
			{
			case SimpleReflectogramEvent.DEADZONE:
				tModel.addRow(String.valueOf(nPri + 1), new Object[] {
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
				tModel.addRow(String.valueOf(nPri + 1), new Object[] {
                     vCol1,
                     vCol2,
                     vCol3,
					 DASH, // отраж
					 Double.toString(MathRef.round_3(((LinearDetailedEvent)ev).getLoss())), // потери
					 Double.toString(MathRef.round_4(((LinearDetailedEvent)ev).getAttenuation() / resKm)) //затух
				});
				break;
			case SimpleReflectogramEvent.NOTIDENTIFIED:
				tModel.addRow(String.valueOf(nPri + 1), new Object[] {
                    vCol1,
                    vCol2,
                    vCol3,
                    DASH, // отраж
                    //dash, // потери
                    Double.toString(MathRef.round_3(((NotIdentifiedDetailedEvent)ev).getLoss())), // потери
                    DASH  // затух
				});
				break;
			case SimpleReflectogramEvent.CONNECTOR:
				tModel.addRow(String.valueOf(nPri + 1), new Object[] {
                    vCol1,
                    vCol2,
                    vCol3,
                    Double.toString(MathRef.round_2(MathRef.calcReflectance(sigma, ((ConnectorDetailedEvent)ev).getAmpl()))), // отраж
                    Double.toString(MathRef.round_3(((ConnectorDetailedEvent)ev).getLoss())), // потери
                    DASH  // затух
				});
				break;
			case SimpleReflectogramEvent.GAIN:
				tModel.addRow(String.valueOf(nPri + 1), new Object[] {
                    vCol1,
                    vCol2,
                    vCol3,
				    DASH, // отраж
				    Double.toString(MathRef.round_3(((SpliceDetailedEvent)ev).getLoss())), // потери
				    DASH  // затух
				});
				break;
			case SimpleReflectogramEvent.LOSS:
				tModel.addRow(String.valueOf(nPri + 1), new Object[] {
                    vCol1,
                    vCol2,
                    vCol3,
				    DASH, // отраж
				    Double.toString(MathRef.round_3(((SpliceDetailedEvent)ev).getLoss())), // потери
				    DASH  // затух
				});
				break;
			case SimpleReflectogramEvent.ENDOFTRACE:
				tModel.addRow(String.valueOf(nPri + 1), new Object[] {
                    vCol1,
                    vCol2,
                    vCol3,
				    Double.toString(MathRef.round_2(MathRef.calcReflectance(sigma, ((EndOfTraceDetailedEvent)ev).getAmpl()))), // отраж
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

    private void updateCompDebug() {
//      System.err.println("MTAE = " + Heap.getMTAEPrimary());
//      System.err.println("MTM = "  + Heap.getMTMEtalon());
        // FIXME: debug: development-time console code
        if (Heap.getMTAEPrimary() != null && Heap.getMTMEtalon() != null)
            ModelTraceComparer.compareMTAEToMTM(Heap.getMTAEPrimary(), Heap.getMTMEtalon());
    }

	public void etalonMTMCUpdated()
	{
        setTableModel();
        updateTableModel();
        updateColors();
        updateCompDebug();
	}

	public void etalonMTMRemoved()
	{
        setTableModel();
        updateTableModel();
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
            CommonUIUtilities.arrangeTableColumns(this.jTable);
            updateTableModel();
            updateCompDebug();
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
