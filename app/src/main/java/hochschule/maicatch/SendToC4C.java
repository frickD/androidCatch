package hochschule.maicatch;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SendToC4C extends Activity{

    Context context;

    private String grabDataText;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_c4_c);
        context = this;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                grabDataText = null;
            } else {
                grabDataText= extras.getString("grabDataText");
            }
        } else {
            grabDataText = (String) savedInstanceState.getSerializable("grabDataText");
        }

         customer = handleResponse(grabDataText, "Germany");

        Spinner spinner = (Spinner) findViewById(R.id.genderID);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public Customer handleResponse(String input, String countryParameter) {

        List<String> validatedInput = new ArrayList<String>();
        this.splitInputIntoLines(input, validatedInput);


        EMail eMailFinder = new EMail();
        Url urlFinder = new Url();
        List<String> mails = new ArrayList<String>();
        List<String> urls = new ArrayList<String>();
        List<String> pNumbers = new ArrayList<String>();
        List<String> fNumbers = new ArrayList<String>();
        List<String> mNumbers = new ArrayList<String>();
        List<String> streets = new ArrayList<String>();
        List<String> zipAndCity = new ArrayList<String>();
        List<String> companyName = new ArrayList<String>();
        NameExtractor nameExtractor = new NameExtractor(context);
        List<String> firstAndLastName = new ArrayList<String>();
        GenderApiSelf localServiceGender = new GenderApiSelf(context);
        String gender = "";

        countrySpecificValues country = this.getCountry(countryParameter);

        for (int i = 0; i < validatedInput.size(); i++) {
            String cleanInput = validatedInput.get(i).trim();
            country.findCityAndZip(cleanInput, zipAndCity);
            country.findStreet(streets, cleanInput);
            country.findCompanyName(cleanInput, companyName);
            country.findPhoneNumbers(cleanInput, pNumbers, fNumbers, mNumbers);
            eMailFinder.isMail(cleanInput, mails);
            urlFinder.isUrl(cleanInput, urls);
        }

        if(!mails.isEmpty()){
            nameExtractor.extractNamefromMail(validatedInput, mails, firstAndLastName);
        }
        nameExtractor.extractNameFromText(validatedInput, firstAndLastName);
        if(!firstAndLastName.isEmpty() && !firstAndLastName.get(0).equals("")){
            gender = localServiceGender.getFirstnameGender(firstAndLastName.get(0));
        }

        Customer customer = new Customer(mails, urls, pNumbers, fNumbers, mNumbers, streets, zipAndCity, companyName, firstAndLastName, gender);
        return customer;
    }

    /**
     * Get input into lines in a list
     * 1. Split input into lines  <br>
     * 2. Trim and append them to a list
     * @param input complete input from user-interface
     * @param validatedInput list to fill in the splitted lines of the complete input string
     */
    private void splitInputIntoLines(String input, List<String> validatedInput){
        String inputLines[] = input.split("\\r|\\n|\\||\\<|\\>|\\;|\\·|\\•");
        for (int i = 0; i < inputLines.length; i++) {
            if (!inputLines[i].isEmpty()) {
                validatedInput.add(inputLines[i].trim());
            }
        }
    }

    /**
     * Returns an Country object which fits to the country-paramter
     * @param countryParameter country user selected on the interface
     * @return Country: country of the parameter or country germany if default
     */
    private countrySpecificValues getCountry(String countryParameter) {
        switch (countryParameter) {
            case "Germany":
                return new countrySpecificValuesDE();
            case "United States":
                return new countrySpecificValuesUS();
            default:
                return new countrySpecificValuesDE();
        }

    }

}
