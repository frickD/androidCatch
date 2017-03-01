package hochschule.maicatch.datagrab;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import hochschule.maicatch.R;

/**
 * Check the gender of a firstname
 * Name lists are from: http://www.albertmartin.de/vornamen/
 * @author marcelm
 */
public class GenderApiSelf {
	private List<String> maleNamesList = new ArrayList<String>();
	private List<String> femaleNamesList = new ArrayList<String>();
	private FileReader fr;
	private boolean isMale = false;
	private boolean isFemale = false;
	private Context context;

	private final String male = "male";
	private final String female = "female";
	private final String unknown = "unknown";

	public GenderApiSelf(Context context){
		this.context = context;
	}


	public String getFirstnameGender(String firstname) {
		//URL maleFirstnames = this.getClass().getResource("firstnamesMale.txt");
		//if (!this.loadFirstnames(maleFirstnames)) {
		//	return this.unknown;
		//}
		;
		if (!this.loadFirstnamesInLists(this.maleNamesList, context, R.raw.firstnames_male)) {
			return this.unknown;
		}

		//URL femaleFirstnames = this.getClass().getResource("firstnamesFemale.txt");
		//if (!this.loadFirstnames(femaleFirstnames)) {
		//	return this.unknown;
		//}

		if (!this.loadFirstnamesInLists(this.femaleNamesList, context, R.raw.firstnames_female)) {
			return this.unknown;
		}

		if (this.isFirstNameMale(this.maleNamesList, firstname)) {
			this.isMale = true;
		}
		if (this.isFirstNameFemale(this.femaleNamesList, firstname)) {
			this.isFemale = true;
		}

		return this.getGender();

	}

	/**
	 * Load the file names
	 * 
	 * @param nameFile
	 *            loaded list of firstnames of male or female
	 * @return String: unknown if exception
	 */
	private Boolean loadFirstnames(URL nameFile) {
		try {
			this.fr = new FileReader(nameFile.getFile());
		} catch (FileNotFoundException e) {
			return false;
		}
		return true;
	}

	public static String readRawTextFile(Context ctx, int resId)
	{
		InputStream inputStream = ctx.getResources().openRawResource(resId);

		InputStreamReader inputreader = new InputStreamReader(inputStream);
		BufferedReader buffreader = new BufferedReader(inputreader);
		String line;
		StringBuilder text = new StringBuilder();

		try {
			while (( line = buffreader.readLine()) != null) {
				text.append(line);
				text.append('\n');
			}
		} catch (IOException e) {
			return null;
		}
		return text.toString();
	}

	/**
	 * Load names into lists for better handling
	 * 
	 * @param nameList
	 *            loaded list of firstnames of male or female
	 * @return String: unknown if exception while file reading
	 */
	private Boolean loadFirstnamesInLists(List<String> nameList, Context context, int rawID) {
		String read = readRawTextFile(context, rawID);
		String[] test = read.split("\n");
		for (int i = 0; i < test.length; i++) {
			nameList.add(test[i]);
		}
		return true;
	}

	/**
	 * @param maleNameList
	 *            loaded list of male firstnames
	 * @param firstname
	 *            firstname that has been found
	 * @return boolean: true if name has been found in list
	 */
	private boolean isFirstNameMale(List<String> maleNameList, String firstname) {
		return this.getGenderOfFirstName(maleNameList, firstname);
	}

	/**
	 * @param femaleNameList
	 *            loaded list of female firstnames
	 * @param firstname
	 *            firstname that has been found
	 * @return boolean: true if name has been found in list
	 */
	private boolean isFirstNameFemale(List<String> femaleNameList, String firstname) {
		return this.getGenderOfFirstName(femaleNameList, firstname);
	}

	/**
	 * Search for name, in name-list
	 * 
	 * @param nameList
	 *            loaded list of firstnames of male or female
	 * @param firstname
	 *            firstname that has been found
	 * @return boolean: true if name is found in name list
	 */
	private boolean getGenderOfFirstName(List<String> nameList, String firstname) {
		for (int i = 0; i < nameList.size(); i++) {
			if (nameList.get(i).equals(firstname)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Perform checks to get gender
	 * 
	 * @return String: gender
	 */
	private String getGender() {
		if (this.isNameUnisexOrNotFound()) {
			return this.unknown;
		}
		if (this.isNameMale()) {
			return this.male;
		}
		if (this.isNameFemale()) {
			return this.female;
		}
		return this.unknown;
	}

	/**
	 * 1. Checks if a name is unisex or not found
	 * 
	 * @return boolean: true if name is female and male or nothing of both(was
	 *         not found in lists)
	 */
	private boolean isNameUnisexOrNotFound() {
		return this.isNameFemale() && this.isNameMale() || !this.isNameFemale() && !this.isNameMale();
	}

	/**
	 * Checks if firstname is male
	 * 
	 * @return boolean: true, if name has been found the male list
	 */
	private boolean isNameMale() {
		return this.isMale;
	}

	/**
	 * checks if firstname is female
	 * 
	 * @return boolean: true, if name has been found in the female list
	 */
	private boolean isNameFemale() {
		return this.isFemale;
	}
	
	public List<String> getFemaleNamesList(){

		//this.loadFirstnames(femaleFirstnames);
		this.loadFirstnamesInLists(this.femaleNamesList, context, R.raw.firstnames_female);
		return this.femaleNamesList;
	}
	
	public List<String> getMaleNamesList(){
		//URL femaleFirstnames = this.getClass().getResource("/resources/firstnamesMale.txt");
		//this.loadFirstnames(femaleFirstnames);
		this.loadFirstnamesInLists(this.femaleNamesList, context, R.raw.firstnames_male);
		return this.femaleNamesList;
	}
	
	

}
