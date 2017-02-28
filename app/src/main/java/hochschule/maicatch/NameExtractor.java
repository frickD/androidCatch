package hochschule.maicatch;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import hochschule.maicatch.GenderApiSelf;

/**
 * Splits the local part of a e-mail into first and lastname and searches for
 * them in the input, if that didn´t work, perform the extractNameFromText
 * 
 * @author marcelm
 */
public class NameExtractor {

	private Context context;

	public NameExtractor(Context context){
		this.context = context;
	}
	
	private static final String FIRST_AND_LASTNAME_REG_EXP = "[a-zäöü’'‘ÆÐƎƏƐƔĲŊŒẞÞǷȜæðǝəɛɣĳŋœĸſßþƿȝĄƁÇĐƊĘĦĮƘŁØƠŞȘŢȚŦŲƯY̨Ƴąɓçđɗęħįƙłøơşșţțŧųưy̨ƴÁÀÂÄǍĂĀÃÅǺĄÆǼǢƁĆĊĈČÇĎḌĐƊÐÉÈĖÊËĚĔĒĘẸƎƏƐĠĜǦĞĢƔáàâäǎăāãåǻąæǽǣɓćċĉčçďḍđɗðéèėêëěĕēęẹǝəɛġĝǧğģɣĤḤĦIÍÌİÎÏǏĬĪĨĮỊĲĴĶƘĹĻŁĽĿʼNŃN̈ŇÑŅŊÓÒÔÖǑŎŌÕŐỌØǾƠŒĥḥħıíìiîïǐĭīĩįịĳĵķƙĸĺļłľŀŉńn̈ňñņŋóòôöǒŏōõőọøǿơœŔŘŖŚŜŠŞȘṢẞŤŢṬŦÞÚÙÛÜǓŬŪŨŰŮŲỤƯẂẀŴẄǷÝỲŶŸȲỸƳŹŻŽẒŕřŗſśŝšşșṣßťţṭŧþúùûüǔŭūũűůųụưẃẁŵẅƿýỳŷÿȳỹƴźżžẓ]{2,20}";

	/**
	 * Get first-and lastname from the mail and find them in the complete input
	 * <br>
	 * 1. If mail address has been found, the local part of the e-mail is
	 * splitted if possible <br>
	 * Call: {@link #splitLocalPartOfEMail(List)} <br>
	 * 2. Split result has to be two words, they might be first- and lastname.
	 * <br>
	 * Call: {@link #isLocalPartSplittedCorrectly(String[])} <br>
	 * 3. Search for the words in the input <br>
	 * Call: {@link #findNames(List, String, String, String, List, List)} <br>
	 * 4. Search the words with replaced umlauts in the text <br>
	 * Call: {@link #replaceUmlauts(String)} <br>
	 * Call: {@link #findNames(List, String, String, String, List, List)} <br>
	 * 5. Fill the result list with the results <br>
	 * Call: {@link #fillResultList(List, List)} <br>
	 * Call: {@link #fillResultList(List, List)} <br>
	 * 
	 * @param input
	 *            complete list of input
	 * @param mail
	 *            List of mail adresses which have been found
	 * @param firstAndLastName
	 *            index 0: firstname , index 1 : lastname
	 */
	public void extractNamefromMail(List<String> input, List<String> mail, List<String> firstAndLastName) {
		List<String> firstnameSet = new ArrayList<String>();
		List<String> lastnameSet = new ArrayList<String>();
		String splitLocalPartOfEMail[] = this.splitLocalPartOfEMail(mail);
		if (this.isLocalPartSplittedCorrectly(splitLocalPartOfEMail)) {
			return;
		}

		String firstname = splitLocalPartOfEMail[0].toLowerCase();
		String lastname = splitLocalPartOfEMail[1].toLowerCase();
		this.findNames(input, mail.get(0), firstname, lastname, firstnameSet, lastnameSet);

		this.findNames(input, mail.get(0), this.replaceUmlauts(firstname), this.replaceUmlauts(lastname), firstnameSet, lastnameSet);

		this.fillResultList(firstAndLastName, firstnameSet);
		this.fillResultList(firstAndLastName, lastnameSet);
	}

	/**
	 * Splits the local part of the first mail address(part before @) that has
	 * been found
	 * 
	 * @param mail
	 *            complete list of found e-mail-adresses
	 * @return String[]: splitted values
	 */
	private String[] splitLocalPartOfEMail(List<String> mail) {
		String[] firstAndLastname;
		String localEMailPart = mail.get(0).substring(0, mail.get(0).indexOf("@"));
		firstAndLastname = localEMailPart.toLowerCase().split("\\-|\\.|\\_");
		return firstAndLastname;
	}

	/**
	 * Return true if the local part of the mail has been splitted in two <br>
	 * parts(guessed first- and lastname) and none of them is empty
	 * 
	 * @param firstAndLastname
	 *            splitted local part of the-e-mail
	 * @return boolean: true if the local part is splitted correct
	 */
	private boolean isLocalPartSplittedCorrectly(String[] firstAndLastname) {
		return (firstAndLastname.length != 2 || firstAndLastname[0].isEmpty() || firstAndLastname[1].isEmpty());
	}

	/**
	 * Check if a name has been found
	 * 
	 * @param nameSet
	 *            list to which the results are appended(index 0: firstname,
	 *            index 1: lastname)
	 * @return boolean: true if the name was found in the input
	 */
	private boolean isNameNotSet(List<String> nameSet) {
		return nameSet.isEmpty();
	}

	/**
	 * Extract names from text(needs name in the local mailpart and in the text)
	 * Performs search functions for finding Names <br>
	 * Call: {@link #goTroughInputAndExcludeEMail(List, String, String, List)}
	 * <br>
	 * Call: {@link #findShortFirstName(List, List, int)} <br>
	 * Call: {@link #findShortLastName(List, List, int)} <br>
	 * Call: {@link #checkifDupplicates(List, List, List, int, int)} <br>
	 * Call: {@link #checkOrder(List, List, List, int, int)} <br>
	 * 
	 * @param input
	 *            complete list of input
	 * @param mail
	 *            mail where is the local e-mail-part from
	 * @param firstname
	 *            first part of the splitted local part of the mail
	 * @param lastname
	 *            last part of the splitted local part of the mail
	 * @param firstnameSet
	 *            list to append the result of firstname searching
	 * @param lastnameSet
	 *            list to append the result of lastname searching
	 */
	private void findNames(List<String> input, String mail, String firstname, String lastname,
			List<String> firstnameSet, List<String> lastnameSet) {
		int positionFirstName = -1;
		int positionLastName = -1;

		if (this.isNameNotSet(firstnameSet)) {
			positionFirstName = this.goTroughInputAndExcludeEMail(input, mail, firstname, firstnameSet);
		}
		if (this.isNameNotSet(lastnameSet)) {
			positionLastName = this.goTroughInputAndExcludeEMail(input, mail, lastname, lastnameSet);
		}
		this.findShortFirstName(input, firstnameSet, positionLastName);
		this.findShortLastName(input, lastnameSet, positionFirstName);
		this.checkifDupplicates(firstnameSet, lastnameSet, input, positionFirstName, positionLastName);
		this.checkOrder(firstnameSet, lastnameSet, input, positionFirstName, positionLastName);
	}

	/**
	 * Searches for Name in input <br>
	 * 1. Go trough input 2. Search only if the line doesn´t contain the mail
	 * Call: {@link #searchForName(String, String, List)} <br>
	 * 
	 * @param input
	 *            complete list of input
	 * @param mail
	 *            mail where is the local e-mail-part from
	 * @param name
	 *            one part of the splitted local part of the mail
	 * @param nameSet
	 *            list to append the result of name searching
	 * @return int: -1 if no name has been found, position of the line in input
	 *         where name has been found
	 */
	private int goTroughInputAndExcludeEMail(List<String> input, String mail, String name, List<String> nameSet) {
		for (int i = 0; i < input.size(); i++) {
			if (!input.get(i).contains(mail)) {
				if (this.searchForName(input.get(i), name, nameSet)) {
					return i;
				}
				;
			}
		}
		return -1;
	}

	/**
	 * Searches for the first name and ads them to the list <br>
	 * Call: {@link #addNames(List, String)} <br>
	 * 
	 * @param input
	 *            input-row to search for name
	 * @param name
	 *            one part of the splitted local part of the mail
	 * @param nameSet
	 *            list to append the result for name searching
	 * @return boolean: true if a name has been set
	 */
	private boolean searchForName(String input, String name, List<String> nameSet) {
		if (input.toLowerCase().contains(name)) {
			this.addNames(nameSet, name);
			return true;
		}
		return false;
	}

	/**
	 * Add found name to the the given list
	 * 
	 * @param nameSet
	 *            list to append the result for name searching
	 * @param name
	 *            name that has been found
	 */
	private void addNames(List<String> nameSet, String name) {
		nameSet.add(this.makeStringToNomen(name));
	}

	/**
	 * If the firstname in the email has only length one, it could be a short
	 * form of the name. <br>
	 * So it searches in the row where the other name was found, for the full
	 * name. <br>
	 * Call: {@link #makeStringToNomen(String)} <br>
	 * 
	 * @param input
	 *            complete list of input
	 * @param nameSet
	 *            list to append the result for lastname searching
	 * @param nameFoundPosition
	 *            the String in the input list where the lastname has been found
	 */

	private void findShortFirstName(List<String> input, List<String> nameSet, int nameFoundPosition) {
		if (nameFoundPosition < 0) {
			return;
		}
		if (!nameSet.isEmpty() && nameSet.get(0).length() == 1) {
			if (this.couldBeFirstAndLastName(input.get(nameFoundPosition))) {
				nameSet.set(0, this.makeStringToNomen(this.extractFirstName(input.get(nameFoundPosition))));
			}
		}
	}

	/**
	 * Extracts this part 'Marcel' from a String like this: "Marcel Mansouri"
	 * 
	 * @param nameRow
	 *            String for extracting one part of it
	 * @return String: firstname name extracted
	 */
	private String extractFirstName(String nameRow) {
		return nameRow.substring(0, nameRow.indexOf(" "));
	}

	/**
	 * If the lastname in the email has only length one, it could be a short
	 * form of the name. <br>
	 * So it searches in the row where the other name was found, for the full
	 * name. <br>
	 * Call: {@link #couldBeFirstAndLastName(String)} <br>
	 * Call: {@link #makeStringToNomen(String)} <br>
	 * 
	 * @param input
	 *            complete list of input
	 * @param firstNameSet
	 *            list to append the result for lastname searching
	 * @param nameFoundPosition
	 *            the String in the input list where the firstname has been
	 *            found
	 */
	private void findShortLastName(List<String> input, List<String> firstNameSet, int nameFoundPosition) {
		if (nameFoundPosition < 0) {
			return;
		}
		if (!firstNameSet.isEmpty() && firstNameSet.get(0).length() == 1) {
			if (this.couldBeFirstAndLastName(input.get(nameFoundPosition))) {
				firstNameSet.set(0, this.makeStringToNomen(this.extractLastName(input.get(nameFoundPosition))));
			}
		}
	}

	/**
	 * Extracts this part 'Mansouri' from a String like this: "Marcel Mansouri"
	 * 
	 * @param nameRow
	 *            String for extracting one part of it
	 * @return String: lastname extracted
	 */
	private String extractLastName(String nameRow) {
		return nameRow.substring(nameRow.indexOf(" ") + 1, nameRow.length());
	}

	/**
	 * Return true if it has the format of First- and Lastname in one Line As
	 * example: "Marcel Mansouri"
	 * 
	 * @param inputLine
	 *            Line that has the format
	 * @return boolean: true if the line has the format of two names
	 */
	private boolean couldBeFirstAndLastName(String inputLine) {
		return inputLine.toLowerCase().matches(String.format("%s %s", FIRST_AND_LASTNAME_REG_EXP, FIRST_AND_LASTNAME_REG_EXP));
		}

	/**
	 * Replaces umlauts in Strings
	 * 
	 * @param name
	 *            String for replacing umlauts
	 * @return String: String with replaced umlauts
	 */
	private String replaceUmlauts(String name) {
		name = name.replace("ue", "ü");
		name = name.replace("ae", "ä");
		name = name.replace("oe", "ö");
		return name;
	}

	/**
	 * Fills into the firstAndLastName-list a name or a empty string, if nameSet
	 * is empty
	 * 
	 * @param firstAndLastName
	 *            list to which the results are appended
	 * @param nameSet
	 *            list with one name appended
	 */
	private void fillResultList(List<String> firstAndLastName, List<String> nameSet) {
		if (this.isNameNotSet(nameSet)) {
			firstAndLastName.add("");
		} else {
			firstAndLastName.add(nameSet.get(0));
		}
	}

	/**
	 * Checks if the first and the lastname are dupplicates. <br>
	 * If yes, try to replace them with names found in the input <br>
	 * Call: {@link #setNamePosition(int, int, int)} <br>
	 * Call: {@link #makeStringToNomen(String)} <br>
	 * 
	 * @param firstnameSet
	 *            list with one firstname result appended
	 * @param lastnameSet
	 *            list with one lastname result appended
	 * @param input
	 *            complete list of input
	 * @param positionFirstName
	 *            String in the input list where the firstname has been found
	 * @param positionLastName
	 *            String in the input list where the lastname has been found
	 */
	private void checkifDupplicates(List<String> firstnameSet, List<String> lastnameSet, List<String> input,
			int positionFirstName, int positionLastName) {
		int positionName = -1;
		positionName = this.setNamePosition(positionName, positionFirstName, positionLastName);
		if (positionName == -1) {
			return;
		}
		if (this.checkListsAndNameFoundLine(firstnameSet, lastnameSet, input, positionName)) {
			if (firstnameSet.get(0).equals(lastnameSet.get(0))) {
				String firstname = input.get(positionName).substring(0, input.get(positionName).indexOf(" "));
				firstnameSet.set(0, this.makeStringToNomen(firstname));
			}
		}
	}

	/**
	 * If the order of first- and lastname found in input is an other then found
	 * in input, try to perform a swap <br>
	 * Call: {@link #setNamePosition(int, int, int)} <br>
	 * Call: {@link #makeStringToNomen(String)} <br>
	 * 
	 * @param firstnameSet
	 *            list with one firstname result appended
	 * @param lastnameSet
	 *            list with one lastname result appended
	 * @param input
	 *            complete list of input
	 * @param positionFirstName
	 *            String in the input list where the firstname has been found,
	 *            -1 if nothing has been found
	 * @param positionLastName
	 *            String in the input list where the lastname has been found, -1
	 *            if nothing has been found
	 */
	private void checkOrder(List<String> firstnameSet, List<String> lastnameSet, List<String> input,
			int positionFirstName, int positionLastName) {
		int positionName = -1;
		positionName = this.setNamePosition(positionName, positionFirstName, positionLastName);
		if (positionName == -1) {
			return;
		}
		if (this.checkListsAndNameFoundLine(firstnameSet, lastnameSet, input, positionName)) {
			String firstname = input.get(positionName).substring(0, input.get(positionName).indexOf(" "));
			String lastname = input.get(positionName).substring(input.get(positionName).indexOf(" ") + 1,
					input.get(positionName).length());
			firstnameSet.set(0, this.makeStringToNomen(firstname));
			lastnameSet.set(0, this.makeStringToNomen(lastname));
		}

	}

	/**
	 * Checks if there is a position in input where names have been found
	 * 
	 * @param positionName
	 *            returned parameter with the position of found name
	 * @param positionFirstName
	 *            position where the firstname has been found, -1 if nothing has
	 *            been found
	 * @param positionLastName
	 *            position where the lastname has been found, -1 if nothing has
	 *            been found
	 * @return int: Position of name found in input, if not found -1
	 */
	private int setNamePosition(int positionName, int positionFirstName, int positionLastName) {
		if (positionFirstName != -1) {
			positionName = positionFirstName;
		} else {
			if (positionLastName != -1) {
				positionName = positionLastName;
			}
		}
		return positionName;
	}

	/**
	 * 
	 * Test of the parameters, to avoid exception <br>
	 * Call: {@link #couldBeFirstAndLastName(String)} <br>
	 * 
	 * @param firstnameSet
	 *            list with one firstname result appended
	 * @param lastnameSet
	 *            list with one lasttname result appended
	 * @param input
	 *            complete list of input
	 * @param positionName
	 *            position where the first- or lastname has been found
	 * @return boolean: true, if there will be no exeception
	 */
	private boolean checkListsAndNameFoundLine(List<String> firstnameSet, List<String> lastnameSet, List<String> input,
			int positionName) {
		return !firstnameSet.isEmpty() && !lastnameSet.isEmpty() && this.couldBeFirstAndLastName(input.get(positionName));
	}

	/**
	 * Corrects the large and lower case of a word
	 * 
	 * @param name
	 *            word to be transformed
	 * @return String: a string with beginning Uppercase and following
	 *         Lowercases
	 */
	private String makeStringToNomen(String name) {
		if (name.length() >= 2) {
			name = ("" + name.charAt(0)).toUpperCase() + name.substring(1, name.length()).toLowerCase();
			return name;
		}
		return "";
	}

	/**
	 * If no names have been found with Call:
	 * {@link #extractNamefromMail(List, List, List)} we perform <br>
	 * a search with help of: genderApiSelf.GenderApiSelf 
	 * 1. Search for lines which could contain first- and lastname, with:
	 * {@link #findLinesThatCouldBeFirstAndLastName(List, List, List)} <br>
	 * 2. Extract if possible, with:
	 * {@link #findLinesThatCouldBeFirstAndLastName(List, List, List)}
	 * 
	 * @param input
	 *            complete list of input
	 * @param firstAndLastName
	 *            list to which the results are appended
	 */
	public void extractNameFromText(List<String> input, List<String> firstAndLastName) {
		if (!this.isNameNotSet(firstAndLastName)) {
			return;
		}
		GenderApiSelf name = new GenderApiSelf(context);
		List<String> maleFirstnames = name.getMaleNamesList();
		List<String> femaleFirstnames = name.getFemaleNamesList();
		if (this.findLinesThatCouldBeFirstAndLastName(input, maleFirstnames, firstAndLastName)) {
			return;
		};
		this.findLinesThatCouldBeFirstAndLastName(input, femaleFirstnames, firstAndLastName);

	}

	/**
	 * Searches for lines that could be first- and lastname, with: {@link #couldBeFirstAndLastName(String)} <br>
	 * If yes, extract the guessed firstname and check if it´s in the list {link {@link #isNameInList(String, List)} <br>
	 * If yes, add first- and lastname to the result list
	 * 
	 * @param input
	 *            complete list of input
	 * @param nameList
	 *            list of firstnames by gender
	 * @param firstAndLastName
	 *            list to which the results are appended
	 * @return true if firstname has been found and first- and lastname have
	 *         been added successfully
	 */
	private boolean findLinesThatCouldBeFirstAndLastName(List<String> input, List<String> nameList,
			List<String> firstAndLastName) {
		for (int i = 0; i < input.size(); i++) {
			String inputLine = input.get(i);
			if (this.couldBeFirstAndLastName(inputLine)) {
				String firstname = this.extractFirstName(input.get(i)).toLowerCase().trim();
				if (this.isNameInList(firstname, nameList)) {
					this.addNames(firstAndLastName, firstname);
					this.addNames(firstAndLastName, this.extractLastName(input.get(i)));
					return true;
				}
				;
			}
		}
		return false;
	}

	/**
	 * Searches for the guessed firstname in the list
	 * 
	 * @param firstname
	 *            Guessed firstname
	 * @param nameList
	 *            list of firstnames by gender
	 * @return true if firstname is in the list
	 */
	private boolean isNameInList(String firstname, List<String> nameList) {
		return nameList.contains(this.makeStringToNomen(firstname));
	}
	

	
	

}

