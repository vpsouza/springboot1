package me.viniciuspiedade.springboot1.util;

public enum ErrorEnum {

	DATABASE(0),
	PRODUCT_PAYLOAD_UNDEFINED(1),
	PRODUCT_NAME_UNDEFINED(2),
	PRODUCT_PARENT_NOT_WELL_DEFINED(3),
	PRODUCT_CREATE_PAYLOAD_NOT_WELL_DEFINED(4),
	PRODUCT_UPDATE_PAYLOAD_NOT_WELL_DEFINED(5),
	PRODUCT_NOT_EXISTS(6);
	
	
	  private final int code;
	
	  private ErrorEnum(int code) {
	    this.code = code;
	  }
	
	  public int getCode() {
	     return code;
	  }
	
	  @Override
	  public String toString() {
	    return String.valueOf(code);
	  }
}
