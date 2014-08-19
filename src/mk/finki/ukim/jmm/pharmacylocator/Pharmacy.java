package mk.finki.ukim.jmm.pharmacylocator;

import java.util.Comparator;

public class Pharmacy implements Comparable<Pharmacy>{
	
	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}

	public String getPharmacyCity() {
		return pharmacyCity;
	}

	public void setPharmacyCity(String pharmacyCity) {
		this.pharmacyCity = pharmacyCity;
	}

	public String getPharmacyCountry() {
		return pharmacyCountry;
	}

	public void setPharmacyCountry(String pharmacyCountry) {
		this.pharmacyCountry = pharmacyCountry;
	}
	private String pharmacyName;
	private String pharmacyAdress;
	private String pharmacyTelephoneNumber;
	private String pharmacyFormattedTelephoneNumber;
	private Double pharmacyLatitude;
	private Double pharmacyLongitude;
	private String pharmacyCity;
	private String pharmacyCountry;
	private double distance;
	public Pharmacy(){
			
	}

	public String getPharmacyTelephoneNumber() {
		return pharmacyTelephoneNumber;
	}

	public void setPharmacyTelephoneNumber(String pharmacyTelephoneNumber) {
		this.pharmacyTelephoneNumber = pharmacyTelephoneNumber;
	}

	public String getPharmacyFormattedTelephoneNumber() {
		return pharmacyFormattedTelephoneNumber;
	}

	public void setPharmacyFormattedTelephoneNumber(
			String pharmacyFormattedTelephoneNumber) {
		this.pharmacyFormattedTelephoneNumber = pharmacyFormattedTelephoneNumber;
	}
	
	public String getPharmacyName() {
		return pharmacyName;
	}
	public void setPharmacyName(String pharmacyName) {
		this.pharmacyName = pharmacyName;
	}
	public String getPharmacyAdress() {
		return pharmacyAdress;
	}
	public void setPharmacyAdress(String pharmacyAdress) {
		this.pharmacyAdress = pharmacyAdress;
	}
	public Double getPharmacyLatitude() {
		return pharmacyLatitude;
	}
	public void setPharmacyLatitude(Double pharmacyLatitude) {
		this.pharmacyLatitude = pharmacyLatitude;
	}
	public Double getPharmacyLongitude() {
		return pharmacyLongitude;
	}
	public void setPharmacyLongitude(Double pharmacyLongitude) {
		this.pharmacyLongitude = pharmacyLongitude;
	}

	@Override
	public int compareTo(Pharmacy another) {
		
		 return Double.compare(this.getDistance(), another.getDistance());
		
		
		
		
	}



	
}
