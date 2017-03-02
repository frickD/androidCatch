package hochschule.maicatch.datagrab;

import java.util.HashMap;
import java.util.Map;
/**
 * Class to bring data in the right form for the C4C-Webservice-Template
 * @author marcelm
 *
 */
public class ParseObject {
	private String companyName = "";
	private String gender = "";
	private String firstName = "";
	private String lastName = "";
	private String eMail = "";
	private String companyURL = "";
	private String phoneNumber = "";
	private String mobileNumber = "";
	private String streetName = "";
	private String houseNumber = "";
	private String cityName = "";
	private String zipCode = "";
	
	/**
	 * Fills a object to parse the data in from it, in an xml-file.
	 * @param createContactMap Contains the data from user side to send to the Webservice.
	 */
	public void fillValues(Map<String, String> createContactMap){
		companyName = createContactMap.get("Company");
		gender = this.getGenderCode("Mr");
		firstName = createContactMap.get("Firstname");
		lastName = createContactMap.get("Lastname");
		eMail = createContactMap.get("E-Mail");
		companyURL = createContactMap.get("Url");
		phoneNumber = createContactMap.get("Phone");
		mobileNumber = createContactMap.get("Mobil");
		Map<String, String> countrySpecificValues = this.getCountrySpecificValues(createContactMap, "Germany");
		streetName = countrySpecificValues.get("streetName");
		houseNumber = countrySpecificValues.get("houseNumber");
		cityName = countrySpecificValues.get("cityName");
		zipCode = countrySpecificValues.get("zipCode");
	}
	
	/**
	 * returns gender code
	 * @param gender gender of found firstname
	 * @return male: 0001, female: 0002
	 */
	private String getGenderCode(String gender){
		if(gender.equals("Ms")){
			return "0001";
		}
		if(gender.equals("Mr")){
			return "0002";
		}
		return "";
	}
	
	/**
	 * Chooses country, creates a object to validate the country specific values. <br>
	 * Put´s the values in a map and returns it.
	 * @param inputMap Contains the data to validate.
	 * @param countryParameter country which was choosed on the user-interface
	 * @return Map with the results.
	 */
	private Map<String, String> getCountrySpecificValues(Map<String, String> inputMap, String countryParameter){
		Map<String, String> result = new HashMap<String, String>();
		switch (countryParameter){
		case "Germany":
			ParseObjectDE parserDE = new ParseObjectDE();
			result.put("streetName", parserDE.getStreetName(inputMap.get("Street").split(" ")[0]));
			result.put("houseNumber", parserDE.getHouseNumber(inputMap.get("Street").split(" ")[1]));
			result.put("cityName", parserDE.getCityName(inputMap.get("Zip City").split(" ")[1]));
			result.put("zipCode", parserDE.getZipCode(inputMap.get("Zip City").split(" ")[0]));
			return result;
		default:
			return result;
		}
			
	}
	
	public String getCompanyName() {
		return companyName;
	}

	public String getEMail() {
		return eMail;
	}

	public String getCompanyURL() {
		return companyURL;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getGender() {
		return gender;
	}
	
	public String getStreetName() {
		return streetName;
	}
	
	public String getHouseNumber() {
		return houseNumber;
	}
	
	public String getCityName() {
		return cityName;
	}

	public String getZipCode() {
		return zipCode;
	}
	

}
