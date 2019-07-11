package myApp;

import javax.xml.bind.annotation.XmlRootElement;

// set the root element to phone
@XmlRootElement(name = "phone")
public class Phone {

	private int id;
	private String make, model, battery;
	
	public Phone() {
		
	}
	
	public Phone(int id, String make, String model, String battery) {
		this.id = id;
		this.make = make;
		this.model = model;
		this.battery = battery;
	}

	public Phone(String make, String model, String battery) {
		this.make = make;
		this.model = model;
		this.battery = battery;
	}

	// getters and setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMake() {
		return make;
	}

	public void setMake(String make) {
		this.make = make;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBattery() {
		return battery;
	}

	public void setBattery(String battery) {
		this.battery = battery;
	}
	
	// override the toString method
	@Override
	public String toString() {
		String result = "Phone: \n";
		result += "ID: " + id + "\n";
		result += "MAKE: " + make + "\n";
		result += "MODEL: " + model + "\n";
		result += "BATTERY: " + battery + "\n";
		
		return result;
	}
	
	
}
