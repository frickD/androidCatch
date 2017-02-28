package hochschule.maicatch;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Object for one person or company, with lists for informations
 * 
 * @author marcelm
 */
public class Customer {

	private List<String> mail = new ArrayList<String>();
	private List<String> url = new ArrayList<String>();
	private List<String> pNumber = new ArrayList<String>();
	private List<String> fNumber = new ArrayList<String>();
	private List<String> mNumber = new ArrayList<String>();
	private List<String> street = new ArrayList<String>();
	private List<String> zipAndCity = new ArrayList<String>();
	private List<String> companyName = new ArrayList<String>();
	private List<String> firstAndLastName = new ArrayList<String>();
	private String gender;
	
	/**
	 * Constructor to fill the lists
	 * @param mails list for e-mail-addresses
	 * @param urls list for URLs
	 * @param pNumbers list for phone-numbers
	 * @param fNumbers list for fax-numbers
	 * @param mNumbers list for mobile-numbers
	 * @param streets list for street-addresses
	 * @param zipAndCity list for zip code and city
	 * @param companyName list for company-names
	 * @param firstAndLastName list for first- and lastname(in this order)
	 * @param gender list for gender
	 */
	public Customer(List<String> mails, List<String> urls, List<String> pNumbers, List<String> fNumbers,
			List<String> mNumbers, List<String> streets, List<String> zipAndCity, List<String> companyName,
			List<String> firstAndLastName, String gender) {
		this.mail = mails;
		this.url = urls;
		this.pNumber = pNumbers;
		this.fNumber = fNumbers;
		this.mNumber = mNumbers;
		this.street = streets;
		this.zipAndCity = zipAndCity;
		this.companyName = companyName;
		this.firstAndLastName = firstAndLastName;
		this.gender = gender;
	}

	public List<String> getMails() {
		return mail;
	}

	public List<String> getUrls() {
		return url;
	}

	public List<String> getPNumbers() {
		return pNumber;
	}

	public List<String> getFNumbers() {
		return fNumber;
	}

	public List<String> getMNumbers() {
		return mNumber;
	}

	public List<String> getStreets() {
		return street;
	}

	public List<String> getZipAndCity() {
		return zipAndCity;
	}

	public List<String> getCompanyName() {
		return companyName;
	}

	public List<String> getfirstAndLastName() {
		return firstAndLastName;
	}

	public String getGender() {
		return gender;
	}

	@Override
	public String toString() {
		return "\n" + "Firma: " + companyName + "\n" + "Straße: " + street + "\n" + "PLZ u. Stadt :" + zipAndCity + "\n"
				+ "Telefonnummern :" + pNumber + "\n" + "\n" + "E-Mail :" + mail + "\n" + "URL´s :" + url;
	}
}
