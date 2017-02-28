package hochschule.maicatch;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to extract e-mail-addresses
 * 
 * @author marcelm
 */
public class EMail {

	private Replacer rep = new Replacer();
	private StringBuilder row = new StringBuilder();
	private final String mailPattern = ".+@.+\\.(.{1,})[a-z]";

	/**
	 * Extract E-Mail-Addresses from the input<br>
	 * 1. Replace all spam-protections like [at] from the text with <br>
	 * Call: {@link Replacer} <br>
	 * 2. Cut extract-email-address from text as long there are @ found <br>
	 * Call: {@link #cutEMailAddressOutOfText(String)} <br>
	 * 3. Validate the found e-mail-address <br>
	 * Call: {@link #isFoundMailAddressValid(String)} <br>
	 * 4. If valid, append the result to the result-list <br>
	 * 6. delete from row from
	 * beginning to the first '@' to search for other e-mail-adresses<br>
	 * 
	 * @param input
	 *            String to search for E-Mail-Addresses <br>
	 * @param mails
	 *            list to which the results are appended
	 */
	public void isMail(String input, List<String> mails) {
		// 1.
		row.append(rep.correction(input));
		input = row.toString();
		// 2.
		while (-1 != row.indexOf("@")) {
			input = row.toString();
			input = this.cutEMailAddressOutOfText(input);
			// 3.
			if (this.isFoundMailAddressValid(input)) {
				// 5.
				mails.add(input);
			}
			// 6.
			row.delete(0, row.indexOf("@") + 1);
		}
		row.setLength(0);
	}

	/**
	 * Cuts e-mail-address out of flow text <br>
	 * 1. While the input contains '@': <br>
	 * go from '@' to left in the string and cut the rest if you match a sign
	 * <br>
	 * which shouldn´t be there <br>
	 * Call: {@link #cutTheLeftSide(String)} <br>
	 * 2. Do the same on the right side of '@'<br>
	 * Call: {@link #cutRightSide(String)} <br>
	 * 
	 * @param input String to search for E-Mail-Addresses
	 * @return String: e-mail adress
	 */
	private String cutEMailAddressOutOfText(String input) {
		input = this.cutTheLeftSide(input);
		input = this.cutRightSide(input);
		return input;
	}

	/**
	 * While the input contains '@': <br>
	 * go from '@' to right in the string and cut the rest if you match a sign
	 * <br>
	 * 
	 * @param input input-row
	 * @return String:
	 */
	private String cutRightSide(String input) {
		for (int i = input.indexOf("@") + 1; i < input.length(); i++) {
			String test = "" + input.charAt(i);
			if (test.matches("[)(\\s,;:><\\]\\[|]")) {
				input = input.substring(0, i);
				break;
			}
		}
		return input;
	}

	/**
	 * While the input contains '@': <br>
	 * go from '@' to left in the string and cut the rest if you match a sign
	 * @param input
	 *            input-row
	 * @return String:
	 */
	private String cutTheLeftSide(String input) {
		for (int i = input.indexOf("@") - 1; i >= 0; i--) {
			String test = "" + input.charAt(i);
			if (test.matches("[)(\\s,;:><\\]\\[|]")) {
				input = input.substring(++i);
				break;
			}
		}
		return input;
	}

	/**
	 * Validate the found e-mail-address if it matches the minimum requirements
	 * of an email-address:
	 * https://de.wikipedia.org/wiki/E-Mail-Adresse#Der_Dom.C3.A4nenteil_.
	 * 28Domain_Part.29 <br>
	 * Call: {@link #checkGlobalAndLocalPart(String)} <br>
	 * 
	 * @param input found-mail-address
	 * @return boolean: true if valid
	 */
	private boolean isFoundMailAddressValid(String input) {
		Matcher matcher = Pattern.compile(mailPattern).matcher(input);
		if (matcher.find()) {
			return this.checkGlobalAndLocalPart(input);
		}
		return false;
	}

	/**
	 * Checks if the e-mail-address has minimum five 6 and the global part minimum
	 * 4 signs
	 * 
	 * @param input found-mail-address
	 * @return booelan: true if valid
	 */
	private boolean checkGlobalAndLocalPart(String input) {
		if (input.length() > 5) {
			if (input.charAt(0) != '@' && (input.length() - (input.indexOf('@') + 1)) >= 4) {
				return true;
			}
		}
		return false;
	}

}
