package hochschule.maicatch;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import hochschule.maicatch.datagrab.Customer;
import hochschule.maicatch.datagrab.EMail;
import hochschule.maicatch.datagrab.GenderApiSelf;
import hochschule.maicatch.datagrab.NameExtractor;
import hochschule.maicatch.datagrab.Url;
import hochschule.maicatch.datagrab.countrySpecificValues;
import hochschule.maicatch.datagrab.countrySpecificValuesDE;
import hochschule.maicatch.datagrab.countrySpecificValuesUS;

public class ContactCreator extends Activity {

    Context context;

    private String grabDataText;
    private Customer customer;
    private EditText company, firstname, lastname, street, zipCity, phone, fax, mobil, email, url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_layout);
        context = this;
        grabEditTextInputs();


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                grabDataText = null;
            } else {
                grabDataText = extras.getString("grabDataText");
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
        company = (EditText) findViewById(R.id.comp);
        firstname = (EditText) findViewById(R.id.firstname);
        lastname = (EditText) findViewById(R.id.lastname);
        street = (EditText) findViewById(R.id.street);
        zipCity = (EditText) findViewById(R.id.zipcity);
        phone = (EditText) findViewById(R.id.phone);
        mobil = (EditText) findViewById(R.id.mobil);
        fax = (EditText) findViewById(R.id.fax);
        email = (EditText) findViewById(R.id.email);
        url = (EditText) findViewById(R.id.url);
    }

    private void setCustomerValues() {
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

        if (!mails.isEmpty()) {
            nameExtractor.extractNamefromMail(validatedInput, mails, firstAndLastName);
        }
        nameExtractor.extractNameFromText(validatedInput, firstAndLastName);
        if (!firstAndLastName.isEmpty() && !firstAndLastName.get(0).equals("")) {
            gender = localServiceGender.getFirstnameGender(firstAndLastName.get(0));
        }

        Customer customer = new Customer(mails, urls, pNumbers, fNumbers, mNumbers, streets, zipAndCity, companyName, firstAndLastName, gender);
        return customer;
    }

    public void onCreateContactAndroid(View view) {
        createContactAndroid(firstname.getText().toString() + " " + lastname.getText().toString(),
                mobil.getText().toString(), email.getText().toString(), company.getText().toString(),
                "", phone.getText().toString(), "Erstellt mit der maihiro maiCatch App", "");
    }

    /**
     * Get input into lines in a list
     * 1. Split input into lines  <br>
     * 2. Trim and append them to a list
     *
     * @param input          complete input from user-interface
     * @param validatedInput list to fill in the splitted lines of the complete input string
     */
    private void splitInputIntoLines(String input, List<String> validatedInput) {
        String inputLines[] = input.split("\\r|\\n|\\||\\<|\\>|\\;|\\·|\\•");
        for (int i = 0; i < inputLines.length; i++) {
            if (!inputLines[i].isEmpty()) {
                validatedInput.add(inputLines[i].trim());
            }
        }
    }

    /**
     * Returns an Country object which fits to the country-paramter
     *
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

    private void createContactAndroid(final String displayName, final String mobileNumber, final String email, final String companyName, final String jobTitle, final String homeNumber, final String notes, final String addedIn) {
        String DisplayName = displayName;
        String MobileNumber = mobileNumber;
        String HomeNumber = homeNumber;
        String WorkNumber = "";
        String emailID = email;
        String company = companyName;
        String JobTitle = jobTitle;
        String Notes = notes + addedIn;

        ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (DisplayName != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            DisplayName).build());
        }

        //------------------------------------------------------ Mobile Number
        if (MobileNumber != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        //------------------------------------------------------ Home Numbers
        if (HomeNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE).withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }

        //------------------------------------------------------ Notes
        if (Notes != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Note.DATA1, HomeNumber)
                    .withValue(ContactsContract.CommonDataKinds.Note.NOTE, Notes)
                    .build());
        }


        //------------------------------------------------------ Work Numbers
        if (WorkNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Email
        if (emailID != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Organization
        if (!company.equals("") && !jobTitle.equals("")) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }

        // Asking the Contact provider to create a new contact
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);

            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, "Der Kontakt " + DisplayName + " wurde erstellt.", duration);
            toast.show();
        } catch (Exception e) {
            e.printStackTrace();
            Context myContext = this;
            Toast.makeText(myContext, "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}
