package com.syrus.util;

public class TraceDataReader {
	static {
		System.loadLibrary("TraceData");
	}

	public TraceDataReader() {
	}

	public native byte[] getBellcoreData(String fileName);

	public native byte[] getBellcoreData(byte[] t5data);
}
