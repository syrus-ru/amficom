package com.syrus.AMFICOM.mcm;

public class MeasurementException extends Exception {
	private static final long serialVersionUID = 5663146797166499357L;

	public static final int OTHER_CODE = 0;
	public static final int IDENTIFIER_GENERATION_FAILED_CODE = 1;
	public static final int DATABASE_CALL_FAILED_CODE = 2;
	
	private int code;

	public MeasurementException(String message, int code) {
		super(message);
		this.code = code;
	}
	
	public MeasurementException(String message, int code, Exception e) {
		super(message, e);
		this.code = code;
	}
	
	public int getCode() {
		return this.code;
	}
}
