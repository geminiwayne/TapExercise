package com.exercise.tap;

public class Model {
	private int ID;
	private String DateTimeUTC;
	private String TapType;
	private String StopId;
	private String CompanyId;
	private String BusID;
	private String PAN;

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getDateTimeUTC() {
		return DateTimeUTC;
	}

	public void setDateTimeUTC(String dateTimeUTC) {
		DateTimeUTC = dateTimeUTC;
	}

	public String getTapType() {
		return TapType;
	}

	public void setTapType(String tapType) {
		TapType = tapType;
	}

	public String getStopId() {
		return StopId;
	}

	public void setStopId(String stopId) {
		StopId = stopId;
	}

	public String getCompanyId() {
		return CompanyId;
	}

	public void setCompanyId(String companyId) {
		CompanyId = companyId;
	}

	public String getBusID() {
		return BusID;
	}

	public void setBusID(String busID) {
		BusID = busID;
	}

	public String getPAN() {
		return PAN;
	}

	public void setPAN(String pAN) {
		PAN = pAN;
	}
}
