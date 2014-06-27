package dataRecovery;

public enum Member {

	REGISTRED("Registred"),
	CASUAL("Casual"),
	TRUCK("Truck");

	private final String label;

	Member(String label) {
		this.label = label;
	}

	public String getLabel() {
		return label;
	}


}
