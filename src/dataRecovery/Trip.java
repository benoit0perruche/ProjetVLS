package dataRecovery;


public class Trip {

	private String duration;
	private String startDate;
	private String startStation;
	private int startTerminal;
	private String endDate;
	private String endStation;
	private int endTerminal;
	private String bikeNumber;
	private Member member;

	public Trip(String duration, String startDate, String startStation,
			int startTerminal, String endDate, String endStation, int endTerminal,
			String bikeNumber, Member member) {
		this.duration = duration;
		this.startDate = startDate;
		this.startStation = startStation;
		this.startTerminal = startTerminal;
		this.endDate = endDate;
		this.endStation = endStation;
		this.endTerminal = endTerminal;
		this.bikeNumber = bikeNumber;
		this.member = member;

	}

	public String getDuration() {
		return duration;
	}

	public void setDuration(String duration) {
		this.duration = duration;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getStartStation() {
		return startStation;
	}

	public void setStartStation(String startStation) {
		this.startStation = startStation;
	}

	public int getStartTerminal() {
		return startTerminal;
	}

	public void setStartTerminal(int startTerminal) {
		this.startTerminal = startTerminal;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getEndStation() {
		return endStation;
	}

	public void setEndStation(String endStation) {
		this.endStation = endStation;
	}

	public int getEndTerminal() {
		return endTerminal;
	}

	public void setEndTerminal(int endTerminal) {
		this.endTerminal = endTerminal;
	}

	public String getBikeNumber() {
		return bikeNumber;
	}

	public void setBikeNumber(String bikeNumber) {
		this.bikeNumber = bikeNumber;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}


}
