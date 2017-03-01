package hochschule.maicatch.datagrab;

import java.util.List;

/**
 * Abstract class for using it in the input handler for different country's <br>
 * TEST
 * @author marcelm
 */
public abstract class countrySpecificValues {
	public abstract void findCityAndZip(String input, List<String> zipAndCity);

	public abstract void findStreet(List<String> streets, String input);

	public abstract void findCompanyName(String input, List<String> companyName);

	protected abstract String checkForCompanyName(String input);

	protected abstract String checkForCompanyNameAlgo(String input, String[] companyForms);

	protected abstract String checkCompanyResult(String result);

	public abstract void findPhoneNumbers(String input, List<String> pNumbers, List<String> fNumbers,
			List<String> mNumbers);
}
