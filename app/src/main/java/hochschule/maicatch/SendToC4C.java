package hochschule.maicatch;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class SendToC4C extends Activity{

    Context context;

    private String grabDataText;
    private Customer customer;
    private EditText company, firstname, lastname, street, zipCity, phone, fax, mobil, email, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_c4_c);
        context = this;
        grabEditTextInputs();


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
        setCustomerValues();


        Spinner spinner = (Spinner) findViewById(R.id.genderID);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void grabEditTextInputs() {
        company = (EditText)findViewById(R.id.comp);
        firstname = (EditText)findViewById(R.id.vorname);
        lastname = (EditText)findViewById(R.id.nachname);
        street = (EditText)findViewById(R.id.straße);
        zipCity = (EditText)findViewById(R.id.zipcity);
        phone = (EditText)findViewById(R.id.telefon);
        mobil = (EditText)findViewById(R.id.mobil);
        fax = (EditText)findViewById(R.id.fax);
        email = (EditText)findViewById(R.id.email);
        url = (EditText)findViewById(R.id.url);
    }

    private void setCustomerValues () {
        if (customer.getCompanyName().size() > 0) {
            company.setText(customer.getCompanyName().get(0));
        }
        if (customer.getfirstAndLastName().size() > 0) {
            firstname.setText(customer.getfirstAndLastName().get(0));
        }
        if (customer.getfirstAndLastName().size() > 1) {
            lastname.setText(customer.getfirstAndLastName().get(1));
        }
        if (customer.getStreets().size() > 0) {
            street.setText(customer.getStreets().get(0));
        }
        if (customer.getZipAndCity().size() > 0) {
            zipCity.setText(customer.getZipAndCity().get(0));
        }
        if (customer.getPNumbers().size() > 0) {
            phone.setText(customer.getPNumbers().get(0));
        }
        if (customer.getMNumbers().size() > 0) {
            mobil.setText(customer.getMNumbers().get(0));
        }
        if (customer.getFNumbers().size() > 0) {
            fax.setText(customer.getFNumbers().get(0));
        }
        if (customer.getMails().size() > 0) {
            email.setText(customer.getMails().get(0));
        }
        if (customer.getUrls().size() > 0) {
            url.setText(customer.getUrls().get(0));
        }

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

    // Methode con maiConnect implementieren ==> onCreatecontact


    private void CreateAndroidContact(final String firstname, final String lastname, final String email, final String phone, final String url, final String fax, final String gender, final String company, final String street){

        String Firstname = firstname;
        String Lastname = lastname;
        String Email = email;
        String Phone = phone;
        String Url = url;
        String Fax = fax;
        String Street = street;
        String Gender = gender;
        String Company = company;














    }












}
