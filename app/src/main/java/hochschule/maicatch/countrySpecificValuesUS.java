package hochschule.maicatch;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @deprecated not finished
 * @author marcelm <br>
 *         In the regular expressions went a lots of time to make them almost
 *         perfect and make them use less resources. <br>
 *         So, if maybe an expressions looks very complicated to you, don´t be
 *         scared, use this tool and you will <br>
 *         understand how it works: https://regex101.com/,
 *         https://danielfett.de/de/tutorials/tutorial-regulare-ausdrucke/
 */
public class countrySpecificValuesUS extends countrySpecificValues{

	private Pattern cityAndZipPattern = Pattern
			.compile("(\\w{2,40}\\s*){1,3},?\\s*([A-Z][a-z]{1,15}|[A-Z]{2})\\,?\\s*\\d{5}");
	private String streetPattern = "[\\d]{1,4}(\\s[A-Z]{1}[a-z]{1,20}){1,4}\\d{0,4}";
	private String postBoxPattern = "Postfach(\\:)?(\\s[0-9]{1,2})*";
	private String[] unitedStates = { "LLC", "Limited Liability Company",
			  "LLP", "Limited Liability Partnership", "OHG", "Inc.", "Corporation",
			  "Incorporated", "Corporation", "LLP", "Limited Liability Partnership" };

	/**
	 * searches for the zip and city <br>
	 * 1. Search with the pattern for zip and city in the input <br>
	 * 2. add the result to the list <br>
	 * 
	 * @param zipAndCity
	 *            list appending the results
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
	 * Searches with a regular expression for the street, if it doesn´t match,
	 * 1. Search with regular expression for street or postfach in the input and
	 * append it to the street-list <br>
	 * 2. If no street was found call: @link #wrongStreetFormat(List, List, int)
	 * <br>
	 * 
	 * @param streets
	 *            list of strings to add the search result
	 * @param input
	 *            input input list of Strings of the whole input to search for
	 *            the street
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
	 * 1. call {@link #checkForCompanyName(String)} 2. if result isn´t empty,
	 * add it to the company-name-list
	 * 
	 * @param input
	 *            input-row in which is search for company
	 * @param companyName
	 *            list to append results
	 */
	public void findCompanyName(String input, List<String> companyName) {
		String result = "";
		result = this.checkForCompanyName(input);
		if ("" != result) {
			companyName.add(result);
		}

	}

	/**
	 * Attention: The order in the arrays does matter, longest strings first,
	 * shortest last <br>
	 * 1. Search with legal {@link #checkForCompanyNameAlgo(String, String[])}
	 * forms of the country in the input <br>
	 * 2. Check result with: {@link #checkResult(String)} <br>
	 * 3. Give checked result back <br>
	 * 
	 * @param input
	 *            input-row in which is search for company
	 * @return company-name which was found or empty string
	 */
	protected String checkForCompanyName(String input) {
		String result = "";
		result = this.executeSearchForCompanyName(input, unitedStates);
		return result;
	}

	/**
	 * Execute search algorithm for Company Name
	 * 
	 * @param input
	 * @param legalForms
	 * @return Found company name or empty string
	 */
	private String executeSearchForCompanyName(String input, String[] legalForms) {
		String result = this.checkForCompanyNameAlgo(input, legalForms);
		result = this.checkCompanyResult(result);
		return result;
	}

	/**
	 * 1. search for legal-forms(companyForms) in input <br>
	 * 2. if it contains a company form: 3. cut everything after a "," before
	 * the occurence of the company form and 4. 3. cut everything after the
	 * occurence of the company form 5. give result back
	 * 
	 * @param input
	 *            input to be checked
	 * @param companyForms
	 *            companyForms of the country
	 * @return return found company name or empty string
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
	 * 1. Check result if it contains over 4 signs 2. if yes: return it,
	 * if no: return empty string
	 * 
	 * @param result
	 *            street-result
	 * @return
	 */
	protected String checkCompanyResult(String result) {
		if (result.length() > 4) {
			return result;
		}
		return "";
	}

	/**
	 * extracts phone-numbers from text<br/>
	 * 1. Search sign chains who fit to the loose format of phone numbers <br>
	 * 2. count the digits in the chain:if digits > 7, it has to to be a phone
	 * number: <br>
	 * because the longest zip-code which only contains numbers has 6 digits and
	 * probability <br>
	 * 3. check if sign chains if they are phone, mobile or faxnumbers and ad
	 * them to their lists <br>
	 * until there an no more matches <br>
	 * 4. clean flexibleInput for another row<br>
	 * 
	 * @param input
	 *            String to search for phone numbers
	 * @param pNumbers
	 *            list of strings to add the search result
	 * @throws IndexOutOfBoundsException
	 *             couldn´t figure out why until yet
	 */

	private StringBuilder pNumber = new StringBuilder();
	private StringBuilder flexibleInput = new StringBuilder();
	private String DEPNumberPattern = "(\\+)?[(\\d)][(\\d)(\\s)(\\.)(\\)(\\/)\\-]*[\\d\\)]";

	public void findPhoneNumbers(String input, List<String> pNumbers, List<String> fNumbers, List<String> mNumbers)
			throws IndexOutOfBoundsException {
		// 1.
		flexibleInput.append(input);
		Matcher matcher = Pattern.compile(DEPNumberPattern).matcher(flexibleInput);
		while (matcher.find()) {
			Matcher digetsMatchter = Pattern.compile("\\d").matcher(matcher.group());
			int digetsCounter = 0;
			// 2.
			while (digetsMatchter.find()) {
				digetsCounter++;
			}
			// 3.
			if (digetsCounter > 7) {
				parsePhoneNumber(input, matcher, pNumbers, fNumbers, mNumbers);
			}
			// 4.
			flexibleInput.subSequence(matcher.end(), flexibleInput.length());
		}
		flexibleInput.setLength(0);
	}

	/**
	 * appends matched Phone numbers to their lists
	 * 
	 * @param matcher
	 * @param numbers
	 */
	private void appendMatchedPhoneNumber(Matcher matcher, List<String> numbers) {
		pNumber.append(matcher.group().trim());
		numbers.add(pNumber.toString());
		pNumber.setLength(0);
	}

	/**
	 * Check if the found number is phone, fax or mobile and appends them to their lists.<br>
	 * If it is no fax or mobile, we guess it is phone.
	 * @param input
	 * @param matcher
	 * @param pNumbers
	 * @param fNumbers
	 * @param mNumbers
	 */
	private void parsePhoneNumber(String input, Matcher matcher, List<String> pNumbers, List<String> fNumbers,
			List<String> mNumbers) {
		String inputLowerCase = input.toLowerCase();

		if (isFax(inputLowerCase)) {
			appendMatchedPhoneNumber(matcher, fNumbers);
		} else if (isMobile(inputLowerCase)) {
			appendMatchedPhoneNumber(matcher, mNumbers);
		} else {
			appendMatchedPhoneNumber(matcher, pNumbers);
		}
	}
	
	/**
	 * Return true if the line beginns with f(no-case-sensitive)
	 * Example: Fax: +49-211-798-16697
	 * @param input
	 * @return
	 */
	private boolean isFax(String input) {
		return input.matches("f.*");
	}
	/**
	 * Return true if the line beginns with m(no-case-sensitive)
	 * Example: Mobile: +49-211-798-16697
	 * @param input
	 * @return
	 */
	private boolean isMobile(String input) {
		return input.matches("m.*");
	}


}
