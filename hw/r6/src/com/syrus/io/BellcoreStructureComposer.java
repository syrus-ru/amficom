package com.syrus.io;

import com.gnnettest.questfiber.work.Characterization;
import com.gnnettest.questfiber.work.Measurement;
import com.gnnettest.questfiber.work.OTDRAcquisitionConstraints;
import com.gnnettest.questfiber.work.OTDRAcquiredData;

public class BellcoreStructureComposer {

	private BellcoreStructureComposer() {
	}

	public static BellcoreStructure createBellcoreStructure(Characterization characterization) {
    Measurement msmnt = characterization.getMeasurement();
    OTDRAcquisitionConstraints ac = (OTDRAcquisitionConstraints)msmnt.getAcquisitionConstraint();
    double[] rawdata = ((OTDRAcquiredData)msmnt.getAcquiredData()).getDataPoints();
//-----------------
/*
for (int i = 0; i < 1000; i++)
	System.out.println("[" + i + "] == " + rawdata[i]);*/
//-----------------
		//         dadaraCorrectData(rawdata);
    //double trueres = ac.getRange()*1000/rawdata.length; // - true resolution instead of ac.getResolution()

    BellcoreStructure bs = new BellcoreStructure();

    bs.addField(BellcoreStructure.GENPARAMS);
    bs.genParams.FID = msmnt.getMonitoredElementIdentity().toString();
    bs.genParams.NW = ac.getWavelength();
    bs.genParams.CMT = "QuestFiber";

    bs.addField(BellcoreStructure.SUPPARAMS);
    bs.supParams.MFID = "Nettest QuestProbe";
/*    try {
      bs.supParams.MFID = Session.otdr.getIdentity().toString();
    }
    catch (RemoteException e) {}*/
    bs.supParams.OMID = "QP1640";//msmnt.getModuleIdentity().toString();

    bs.addField(BellcoreStructure.FXDPARAMS);
    bs.fxdParams.DTS = (long)msmnt.getDate().getTime()/1000;
    bs.fxdParams.AW = (short)(ac.getWavelength()*10);
    bs.fxdParams.UD = "mt";
    bs.fxdParams.TPW = 1;
    bs.fxdParams.PWU = new short[1];
    bs.fxdParams.PWU[0] = (short)ac.getPulseWidth();
    bs.fxdParams.DS = new int [1];
    bs.fxdParams.DS[0] = (int)(ac.getResolution() * ac.getIndexOfRefraction() / 3d * 100d * 10000d/*points*/);// * 1000d/*meters*/);
    bs.fxdParams.NPPW = new int [1];
    bs.fxdParams.NPPW[0] = rawdata.length;
    bs.fxdParams.GI = (int)(ac.getIndexOfRefraction() * 100000);
		bs.fxdParams.NAV = (int)ac.getScans();
		bs.fxdParams.AR = (int)(ac.getResolution() * rawdata.length * ac.getIndexOfRefraction()/3d * 100d);// * 1000d/*meters*/);
		bs.fxdParams.AO = 0;

    bs.addField(BellcoreStructure.DATAPOINTS);
    bs.dataPts.TSF = 1;
    bs.dataPts.TNDP = rawdata.length;
    bs.dataPts.TPS = new int [1];
		bs.dataPts.SF = new short [1];
		bs.dataPts.TPS[0] = rawdata.length;
		bs.dataPts.SF[0] = 1000;
		bs.dataPts.DSF = new int[1][rawdata.length];
		for (int i = 0; i < rawdata.length; i++)
			bs.dataPts.DSF[0][i] = (int)(rawdata[i]*1000);

    bs.addField(BellcoreStructure.CKSUM);
		bs.cksum.CSM = 0;

		bs.addField(BellcoreStructure.MAP);
		bs.map.MRN = 100;
		bs.map.NB = 6;
		bs.map.B_id = new String[6];
		bs.map.B_rev = new int[6];
		bs.map.B_size = new int[6];
		bs.map.B_id[0] = "Map";
		bs.map.B_id[1] = "GenParams";
		bs.map.B_id[2] = "SupParams";
		bs.map.B_id[3] = "FxdParams";
		bs.map.B_id[4] = "DataPts";
		bs.map.B_id[5] = "Cksum";
		for (int i = 1; i < 6; i++)
			bs.map.B_rev[i] = 100;
		bs.map.B_size[1] = bs.genParams.getSize();
		bs.map.B_size[2] = bs.supParams.getSize();
		bs.map.B_size[3] = bs.fxdParams.getSize();
		bs.map.B_size[4] = bs.dataPts.getSize();
		bs.map.B_size[5] = bs.cksum.getSize();
		bs.map.MBS = bs.map.getSize();

    return bs;
  }
}