package hochschule.maicatch.datagrab;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to bring german specific data in the right form for the C4C-Webservice-Template
 * @author marcelm
 *
 */
public class ParseObjectDE {
	
	/**
	 * Extracts the street name from a whole street adress: "Gutenbergstr." 1
	 * @param street Full street-address
	 * @return street name
	 */
	public String getStreetName(String street){
		if(!street.equals("")){
			Pattern p = Pattern.compile("[0-9]");
			Matcher m = p.matcher(street);
			if (m.find()) {
			   int endOfStreetName = m.start();
			   return street.substring(0, endOfStreetName).trim();
			}
		}
		return street;
	}
	
	/**
	 * Extracts the house number from a whole street adress: Gutenbergstr. "1"
	 * @param street Full street-address
	 * @return house number
	 */
	public String getHouseNumber(String street){
		if(!street.equals("")){
			Pattern p = Pattern.compile("[0-9]");
			Matcher m = p.matcher(street);
			if (m.find()) {
			   int beginOfHouseNumber = m.start();
			   return street.substring(beginOfHouseNumber, street.length()).trim();
			}
		}
		return street;
	}
	
	/**
	 * Extracts the city name from a whole city address: 85354 "Freising"
	 * @param zipCity Zip-Code and city name
	 * @return city name
	 */
	public String getCityName(String zipCity){
		if(!zipCity.equals("")){
			Pattern p = Pattern.compile("[A-ZÄÖÜ]");
			Matcher m = p.matcher(zipCity);
			if (m.find()) {
			   int beginOfCityName = m.start();
			   return zipCity.substring(beginOfCityName,zipCity.length());
			}
		}
		return zipCity;
	}
	
	/**
	 * Extracts the zip code from acity address: "85354" Freising
	 * @param zipCity Zip-Code and city name
	 * @return  zip code
	 */
	public String getZipCode(String zipCity){
		if(!zipCity.equals("")){
			Pattern p = Pattern.compile("[A-ZÄÖÜ]");
			Matcher m = p.matcher(zipCity);
			if (m.find()) {
			   int beginOfZipCode = m.start();
			   return zipCity.substring(0, beginOfZipCode).trim();
			}
		}
		return zipCity;
	}
	

}
