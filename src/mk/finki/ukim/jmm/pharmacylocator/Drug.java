package mk.finki.ukim.jmm.pharmacylocator;


public class Drug {
	
	private String drugId;
	private String drugName;
	private String drugLatinName;
	private String drugDose;
	private String drugDetails;
	
	public Drug(String drugId, String drugName, String drugLatinName,
			String drugDose, String drugDetails) {
		this.drugId = drugId;
		this.drugName = drugName;
		this.drugLatinName = drugLatinName;
		this.drugDose = drugDose;
		this.drugDetails = drugDetails;
	}
	
	
	public String getDrugId() {
		return drugId;
	}
	public void setDrugId(String drugId) {
		this.drugId = drugId;
	}
	public String getDrugName() {
		return drugName;
	}
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}
	public String getDrugLatinName() {
		return drugLatinName;
	}
	public void setDrugLatinName(String drugLatinName) {
		this.drugLatinName = drugLatinName;
	}
	public String getDrugDose() {
		return drugDose;
	}
	public void setDrugDose(String drugDose) {
		this.drugDose = drugDose;
	}
	public String getDrugDetails() {
		return drugDetails;
	}
	public void setDrugDetails(String drugDetails) {
		this.drugDetails = drugDetails;
	}

	
	
	
	

}
