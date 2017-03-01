package hochschule.maicatch.datagrab;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Search country specific expressions(zip and city, street, legal forms etc)
 * <br>
 * In the regular expressions went a lots of time to make them almost perfect
 * <br>
 * and make them use less resources. <br>
 * So, if maybe an expressions looks very complicated to you, don´t be scared,
 * <br>
 * use this tool and you will <br>
 * understand how it works: https://regex101.com/,
 * https://danielfett.de/de/tutorials/tutorial-regulare-ausdrucke/
 * 
 * @author marcelm
 * 
 */
public class countrySpecificValuesDE extends countrySpecificValues {

	private final Pattern cityAndZipPattern = Pattern
			.compile("\\d{5} [A-ZÜÄÖ][a-züäö]{1,25}((( |-)[A-ZÜÄÖ][a-züäö]{1,25}|( |-)[a-züäö]{1,25}){0,6})");
	private final String streetPattern = "[A-ZÖÜÄ][a-zßöüä]{1,25}(((-| )([A-ZÖÜÄ][a-zßöüä]{1,25})|(-| )[a-zßöüä]{2,25})){0,5}((s|( |-)S)tr\\. ?| )[1-9]\\d{0,2} ?[a-zA-Z]?";
	private final String postBoxPattern = "Postfach(\\:)?(\\s[0-9]{1,2})*";
	private final String[] europaLegalForms = { "SE", "SCE", "EWIV", "EVTZ" };
	private final String[] germanyLegalForms = { "e. Kfr.", "e. Kfm.", "PartG", "PartG mbB", "GmbH & Co. KG",
			"UG (haftungsbeschränkt) & Co. KG AG & Co. KG", "KGaA & Co. KG", "Stiftung & Co. KG", "VVaG",
			"UG (haftungsbeschränkt", "gGmbH", "KGaA", "GmbH & Co. KGaA", "AG & Co. KGaA", "Stiftung & Co. KGaA",
			"InvAG", "REIT-AG", "GmbH", "e. K.", "e. V.", "gAG", "BGB", "GbR", "OHG", "HGB", "eG", "AG", "KG" };
	private final String DEPNumberPattern = "(\\+)?[(\\d)][(\\d)(\\s)(\\.)(\\)(\\/)\\-\\–]*[\\d)]";

	/**
	 * Extracts zipAndCity from the input <br>
	 * 1. Search with the pattern for zip and city in the input <br>
	 * 2. Add the result to the list <br>
	 * 
	 * @param zipAndCity
	 *            list to which the results are appended
	 * @param input
	 *            input-row to search for zip and city
	 */
	public void findCityAndZip(String input, List<String> zipAndCity) {
		Matcher matcher = this.cityAndZipPattern.matcher(input);
		while (matcher.find()) {
			zipAndCity.add(matcher.group().trim());
		}
	}

	/**
	 * Extracts street-addresses from the input<br>
	 * 1. Search with regular expression for postfach and street in the input
	 * <br>
	 * and append it to the street-list <br>
	 * <br>
	 * 
	 * @param streets
	 *            list to which the results are appended
	 * @param input
	 *            input-row to search for street
	 */
	public void findStreet(List<String> streets, String input) {
		Matcher matcherPostBox = Pattern.compile(this.postBoxPattern).matcher(input);
		while (matcherPostBox.find()) {
			streets.add(matcherPostBox.group().trim());
		}
		Matcher matcherStreet = Pattern.compile(this.streetPattern).matcher(input);
		while (matcherStreet.find()) {
			if (matcherStreet.group().contains("Postfach")) {
				return;
			}
			streets.add(matcherStreet.group().trim());
		}
	}

	/**
	 * Extracts company-names from the input<br>
	 * 1. Call: {@link #checkForCompanyName(String)} <br>
	 * 2. If result isn´t empty, add it to the company-name-list
	 * 
	 * @param input
	 *            input-row to search for street
	 * @param companyName
	 *            list to which the results are appended
	 */
	public void findCompanyName(String input, List<String> companyName) {
		String result = "";
		result = this.checkForCompanyName(input);
		if ("" != result) {
			companyName.add(result);
		}

	}

	/**
	 * Search for company name <br>
	 * Call: {@link #executeSearchForCompanyName(String, String[])} <br>
	 * Attention: The order in the arrays does matter, longest strings first,
	 * <br>
	 * shortest last <br>
	 * 
	 * @param input
	 *            input-row to search for company name
	 * @return String: company-name which was found or empty string
	 */
	protected String checkForCompanyName(String input) {
		String result = "";
		result = this.executeSearchForCompanyName(input, europaLegalForms);
		result = this.executeSearchForCompanyName(input, germanyLegalForms);
		return result;
	}

	/**
	 * Execute search algorithm for Company Name <br>
	 * Call: {@link #checkForCompanyNameAlgo(String, String[])} <br>
	 * Call: {@link #checkCompanyResult(String)} <br>
	 * 
	 * @param input
	 *            input-row to search for company name
	 * @param legalForms
	 *            Array of legal-forms for the country
	 * @return String: Found company name or empty string
	 */
	private String executeSearchForCompanyName(String input, String[] legalForms) {
		String result = this.checkForCompanyNameAlgo(input, legalForms);
		result = this.checkCompanyResult(result);
		return result;
	}

	/**
	 * Execute the algorithm to find company names <br>
	 * 1. search for legal-forms(companyForms) in input <br>
	 * 2. if it contains a company form: <br>
	 * 3. cut everything after a "," before <br>
	 * the occurence of the company form and <br>
	 * 4. cut everything after the <br>
	 * occurence of the company form <br>
	 * 5. give result back <br>
	 * 
	 * @param input
	 *            input-row to search for company name
	 * @param companyForms
	 *            companyForms of the country
	 * @return String: found company name or empty string
	 */
	protected String checkForCompanyNameAlgo(String input, String[] companyForms) {
		// 1.
		for (int i = 0; i < companyForms.length; i++) {
			// 2.
			if (input.contains(companyForms[i])) {
				if (input.contains(",")) {
					while (input.indexOf(",") < input.indexOf(companyForms[i])) {
						input = input.substring(input.indexOf(",") + 1, input.length());
						if (input.indexOf(",") == -1) {
							break;
						}
					}
				}
				input = input.substring(0, (input.indexOf(companyForms[i])) + companyForms[i].length());
				return input.trim();
			}
		}
		return "";
	}

	/**
	 * Check if result contains over 4 signs <br>
	 * 1. Check result if it contains over 4 signs <br>
	 * 2. if yes: return it, if no: return empty string <br>
	 * 
	 * @param result
	 *            company name that has been found or empty String
	 * @return String: company name or empty string
	 */
	protected String checkCompanyResult(String result) {
		if (result.length() > 4) {
			return result;
		}
		return "";
	}

	/**
	 * Extracts phone-numbers from text<br>
	 * 1. Search sign chains who fit to the loose format of phone numbers <br>
	 * 2. Call: {@link #escapeNotNecessarySpecialSigns(String)} <br>
	 * 3. Call: {@link #couldBePhoneNumber(String)} <br>
	 * 4. Cut out the number and the text before the number, for checking which <br>
	 * kind of number it is, with the variable begin. It´s for the case <br>
	 * that diffrent numbers are in one input-row. As example: <br>
	 * [Fax: XXXX XXXXX][, Mobile: XXXX XXXXX] <br>
	 * Call: {@link #isolateNumberWithIdentifier(String, int, Matcher)} <br>
	 * 5. Call: {@link #parsePhoneNumber(String, Matcher, List, List, List)} <br>
	 * 6. Repeate until there a no more matches
	 * 
	 * @param input
	 *            input-row to search for company name
	 * @param pNumbers
	 *            list to which the results are appended
	 * @throws IndexOutOfBoundsException
	 *             couldn´t figure out why until yet, but it happens very rarely
	 */
	public void findPhoneNumbers(String input, List<String> pNumbers, List<String> fNumbers, List<String> mNumbers)
			throws IndexOutOfBoundsException {
		int begin = 0;
		String cleanedInput = this.escapeNotNecessarySpecialSigns(input).trim();
		Matcher matcher = Pattern.compile(DEPNumberPattern).matcher(cleanedInput);
		while (matcher.find()) {
			if (this.couldBePhoneNumber(matcher.group())) {
				String typeOfNumberValidation = this.isolateNumberWithIdentifier(cleanedInput, begin, matcher);
				begin = matcher.end();
				parsePhoneNumber(typeOfNumberValidation, matcher, pNumbers, fNumbers, mNumbers);
			}
		}
	}

	/**
	 * Escapes Signs from the input-row to search, which shouldn´t be in a phone
	 * number <br>
	 * It´s easier to use regular-expression this way
	 * 
	 * @param input
	 *            input-row to search for phone-numbers
	 * @return inputRow withouth escaped signs
	 */
	private String escapeNotNecessarySpecialSigns(String input) {
		return input = input.replaceAll("[^\\w\\.\\+\\-\\,\\ ]", "");
	}
	
	/**
	 * Checks if the found number has over 7 digits.
	 * @param foundNumber number we found with our regex for phone-numbers
	 * @return true if it has a length over 7 digits(shorter phone-numbers are really rare)
	 */
	private boolean couldBePhoneNumber(String foundNumber){
		Matcher digetsMatchter = Pattern.compile("\\d").matcher(foundNumber);
		int digitsCounter = 0;
		// 2.
		while (digetsMatchter.find()) {
			digitsCounter++;
		}
		return digitsCounter > 7;
	}
	
	/**
	 * Returns a possible phone number probably with it´s identifier. <br>
	 * Purpose Example, in one line are two numbers: Fax: 0000 0000, Mobil: 1111 1111 <br>
	 * First return: "Fax: 0000 0000" <br>
	 * Second return: "Mobil: 1111 1111" <br>
	 * Makes possible to identifiy the type of number with: {@link #parsePhoneNumber(String, Matcher, List, List, List)}
	 * @param input input-row to search for phone-numbers
	 * @param begin position 0 or the end after a phone number, that has found before
	 * @param matcher Machter that searches with a regex for possible phone-numbers
	 * @return possible phone-number probably with it´s identifier
	 */
	private String isolateNumberWithIdentifier(String input, int begin, Matcher matcher){
		return input.substring(begin, matcher.end());
	}

	/**
	 * Appends matched Phone numbers to their lists
	 * 
	 * @param matcher
	 *            compiled regex-pattern and input-row for search
	 * @param numbers
	 *            list to which the results are appended
	 */
	private void appendMatchedPhoneNumber(Matcher matcher, List<String> numbers) {
		numbers.add(matcher.group().trim());
	}

	/**
	 * Check if the found number is phone, fax or mobile and appends them to
	 * their lists.<br>
	 * Call: {@link #isFax(String)} Call: {@link #isMobile(String)} If it is no
	 * fax or mobile, we guess it is phone.
	 * 
	 * @param typeOfNumberValidation
	 *            isolated number with the text before it
	 * @param matcher
	 *            compiled regex-pattern and input-row for search
	 * @param pNumbers
	 *            list to which the results are appended
	 * @param fNumbers
	 *            list to which the results are appended
	 * @param mNumbers
	 *            list to which the results are appended
	 */
	private void parsePhoneNumber(String typeOfNumberValidation, Matcher matcher, List<String> pNumbers,
			List<String> fNumbers, List<String> mNumbers) {
		String typeOfNumberValidationLowerCase = typeOfNumberValidation.toLowerCase();
		if (isFax(typeOfNumberValidationLowerCase)) {
			appendMatchedPhoneNumber(matcher, fNumbers);
		} else if (isMobile(typeOfNumberValidationLowerCase)) {
			appendMatchedPhoneNumber(matcher, mNumbers);
		} else {
			appendMatchedPhoneNumber(matcher, pNumbers);
		}
	}

	/**
	 * Return true if the line beginns with f(no-case-sensitive) or contains fax
	 * <br>
	 * Example: Fax: +49-211-798-16697
	 * 
	 * @param input
	 *            input-row to search for company name
	 * @return boolean: true if at beginning of the line is a 'f' or the line
	 *         contains "fax"
	 */
	private boolean isFax(String input) {
		return (input.matches("f.*") || input.contains("fax"));
	}

	/**
	 * Return true if the line beginns with m(no-case-sensitive) or contains
	 * mobile <br>
	 * Example: Mobile: +49-211-798-16697
	 * 
	 * @param input
	 *            input-row to search for company name
	 * @return boolean: true if at beginning of the line is a 'm' or the line
	 *         contains "mobil"
	 */
	private boolean isMobile(String input) {
		return (input.matches("m.*") || input.contains("mobil"));
	}

}
