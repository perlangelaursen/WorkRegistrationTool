
package softwarehuset;

public class Address {
	private String city, street;
	private int streetNumber;
	
	public Address (String city, String street, int streetNumber){
		this.city = city;
		this.street = street;
		this.streetNumber = streetNumber;
	}
	public String getCity() {
		return city;
	}
	public String getStreet() {
		return street;
	}
	public int getStreetNumber() {
		return streetNumber;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public void setStreetNumber(int streetNumber) {
		this.streetNumber = streetNumber;
	}
}

