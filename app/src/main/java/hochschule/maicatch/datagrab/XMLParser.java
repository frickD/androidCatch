package hochschule.maicatch.datagrab;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;

import org.xml.sax.SAXException;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import hochschule.maicatch.MainActivity;
import hochschule.maicatch.R;

/**
 * Fills an xml-file with data and <br>
 * sends it to the webservice to create <br>
 * an account or update it
 * @author marcelm
 *
 */
public class XMLParser {
	
	private final String companyNamePath = "/Envelope/Body/CustomerBundleMaintainRequest_sync_V1/Customer/Organisation/FirstLineName";
	private final String cityPath = "/Envelope/Body/CustomerBundleMaintainRequest_sync_V1/Customer/AddressInformation/Address/PostalAddress/CityName";
	private final String zipCodePath = "/Envelope/Body/CustomerBundleMaintainRequest_sync_V1/Customer/AddressInformation/Address/PostalAddress/CompanyPostalCode";
	private final String streetNamePath = "/Envelope/Body/CustomerBundleMaintainRequest_sync_V1/Customer/AddressInformation/Address/PostalAddress/StreetName";
	private final String houseIDPath = "/Envelope/Body/CustomerBundleMaintainRequest_sync_V1/Customer/AddressInformation/Address/PostalAddress/HouseID";
	private final String contactPersonGenderPath ="/Envelope/Body/CustomerBundleMaintainRequest_sync_V1/Customer/ContactPerson/FormOfAddressCode";
	private final String contactPersonEMailPath = "/Envelope/Body/CustomerBundleMaintainRequest_sync_V1/Customer/ContactPerson/WorkplaceEmail/URI";
	private final String contactPersonFirstNamePath = "/Envelope/Body/CustomerBundleMaintainRequest_sync_V1/Customer/ContactPerson/GivenName";
	private final String contactPersonLastNamePath = "/Envelope/Body/CustomerBundleMaintainRequest_sync_V1/Customer/ContactPerson/FamilyName";
	private final String companyWebsitePath = "/Envelope/Body/CustomerBundleMaintainRequest_sync_V1/Customer/ContactPerson/WorkplaceWebURI";
	private final String contactPersonPhoneNumberPath = "/Envelope/Body/CustomerBundleMaintainRequest_sync_V1/Customer/ContactPerson/WorkplaceTelephone/FormattedNumberDescription";
	private final String contactPersonMobileNumberPath = "/Envelope/Body/CustomerBundleMaintainRequest_sync_V1/Customer/ContactPerson/WorkplaceTelephone/FormattedNumberDescription";
	private Document document = null;
	private XPath xPath =  XPathFactory.newInstance().newXPath();
	private Context context;
    private int responseCode;
	
	public XMLParser(Context ctx){
		this.context = ctx;
		DocumentBuilderFactory builderFactory =
		        DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
		    builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		    e.printStackTrace();  
		}
		
		try {
		    document = builder.parse(context.getResources().openRawResource(R.raw.update));
		} catch (SAXException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	/**
	 * Fills value to node, if value is not empty
	 * @param value value to fill in node
	 * @param nodePath path to node
	 */
	private void setValueToNode(String value, String nodePath){
		if(value.equals("")){
			return;
		}
		NodeList nodeList = this.getNodeList(nodePath);
		nodeList.item(0).getFirstChild().setNodeValue(value);
	}
	
	private void setCompanyName(String companyName, String nodePath){
		this.setValueToNode(companyName, nodePath);
	}
	
	private void setCity(String city, String nodePath){
		this.setValueToNode(city, nodePath);
	}
	
	public void setZipCode(String zipCode, String nodePath){
		this.setValueToNode(zipCode, nodePath);
	}
	
	private void setStreetname(String streetName, String nodePath){
		this.setValueToNode(streetName, nodePath);
	}
	
	private void setHouseID(String houseID, String nodePath){
		this.setValueToNode(houseID, nodePath);
	}
	
	private void setContactPersonMail(String eMail, String nodePath){
		this.setValueToNode(eMail, nodePath);
	}
	
	private void setContactPersonFirstName(String firstName, String nodePath){
		this.setValueToNode(firstName, nodePath);
	}
	
	private void setContactPersonLastName(String lastName, String nodePath){
		this.setValueToNode(lastName, nodePath);
	}
	
	private void setCompanyWebsite(String companyWebsiteURL, String nodePath){
		this.setValueToNode(companyWebsiteURL, nodePath);
	}
	
	private void setContactPersonPhoneNumber(String phoneNumber, String nodePath){
		this.setValueToNode(phoneNumber, nodePath);
	}
	
	private void setContactPersonMobileNumber(String mobileNumber, String nodePath){
		if(mobileNumber.equals("")){
			return;
		}
		NodeList nodeList = this.getNodeList(nodePath);
		nodeList.item(1).getFirstChild().setNodeValue(mobileNumber);
	}
	
	private void setContactPersonGender(String gender, String nodePath){
		this.setValueToNode(gender, nodePath);
	}
	
	/**
	 * Create node-list to manipualte the xml-template
	 * @param path path to first node of the xml-template
	 * @return list of all nodes
	 */
	private NodeList getNodeList(String path){
		NodeList nodeList = null;
		try {
			nodeList = (NodeList) xPath.compile(path).evaluate(document, XPathConstants.NODESET);
		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
		return nodeList;
	}
	
	/**
	 * Fills xml-template
	 * @param customer ParseObject which stores the data for the webservice
	 */
	public void fillXMLTemplate(ParseObject customer){
		this.setCompanyName(customer.getCompanyName(), this.companyNamePath);
		this.setCompanyWebsite(customer.getCompanyURL(), this.companyWebsitePath);
		this.setContactPersonGender(customer.getGender(), this.contactPersonGenderPath);
		this.setContactPersonFirstName(customer.getFirstName(), this.contactPersonFirstNamePath);
		this.setContactPersonLastName(customer.getLastName(), this.contactPersonLastNamePath);
		this.setContactPersonMail(customer.getEMail(), this.contactPersonEMailPath);
		this.setContactPersonPhoneNumber(customer.getPhoneNumber(), this.contactPersonPhoneNumberPath);
		this.setContactPersonMobileNumber(customer.getMobileNumber(), this.contactPersonMobileNumberPath);
		this.setStreetname(customer.getStreetName(), this.streetNamePath);
		this.setHouseID(customer.getHouseNumber(), this.houseIDPath);
		this.setCity(customer.getCityName(), this.cityPath);
		this.setZipCode(customer.getZipCode(), this.zipCodePath);
	}
	
	/**
	 * Sends XML to the C4C webservice to create a new customer
	 */
	public void sendToC4C() {
		try {
			Authenticator.setDefault(new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication("", "".toCharArray());
				}
			});
            new RetrieveFeedTask().execute("");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    public int getResponseCode(){
        return responseCode;
    }
	
	/**
	 * Creates connection to C4C.
	 * @param url URL to C4C-Tenant-Webservice
	 * @param username Username for an C4C-Account
	 * @param password Password for an C4C-Account
	 * @return HttpURLConnection Connection to C4C-Webservice
	 * @throws IOException
	 */
	private HttpURLConnection createConnectionToC4C(URL url, String username, String password) throws IOException{
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
        connection.setRequestProperty  ("Authorization", username + "," + password);
        connection.setDoOutput(true);
        return connection;
	}
	
	/**
	 * Writes xml-string for webservice in utf-8 to outputstream
	 * @param xml data for outputstream
	 * @param wr outputstream
	 * @throws IOException
	 */
	private void writeStringToOutPutStreamInUTF8(String xml, DataOutputStream wr) throws IOException{
		byte[] xmlBytes = xml.getBytes("UTF-8");
		for(int i = 0; i < xmlBytes.length; i++){
			wr.writeByte(xml.getBytes("UTF-8")[i]);
		}
	}
	
	/**
	 * Makes first node of a document to string(with all it´s child-nodes) <br>
	 * Source: http://stackoverflow.com/questions/7299752/get-full-xml-text-from-node-instance
	 * @param node main-node of the xml-template(with all it´s child nodes)
	 * @return xml-template as String
	 * @throws TransformerFactoryConfigurationError
	 * @throws TransformerException
	 */
	private String node2String(Node node) throws TransformerFactoryConfigurationError, TransformerException {
        // you may prefer to use single instances of Transformer, and
        // StringWriter rather than create each time. That would be up to your
        // judgement and whether your app is single threaded etc
        StreamResult xmlOutput = new StreamResult(new StringWriter());
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(node), xmlOutput);
        return xmlOutput.getWriter().toString();
    }
	
	/**
	 * Controlling the answer of the server <br>
	 * Has to be performed to trigger the server!
	 * @param connection Connection to C4C-Webservice.
	 * @throws IOException
	 */
	private int checkAnswerOfTheServer(HttpURLConnection connection) throws IOException{
		int responseCode = connection.getResponseCode();
		return responseCode;
        //Map<String, List<String>> permission = connection.getHeaderFields();
	}

    private class RetrieveFeedTask extends AsyncTask<String, Void, Integer> {

        private Exception exception;

        private ProgressDialog mProgressDialog = new ProgressDialog(context);

        @Override
        protected void onPreExecute() {
            mProgressDialog.setTitle("Sending Contact to C4C....");
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        protected Integer doInBackground(String... urls) {
            try {
                String xml = node2String(document.getDocumentElement());
                URL url = new URL(urls[0]);
                HttpURLConnection connection = createConnectionToC4C(url, "mc_test1", "maiConnect3");
                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                writeStringToOutPutStreamInUTF8(xml, wr);
                wr.flush();
                wr.close();
                return checkAnswerOfTheServer(connection);
            } catch (Exception e) {
                this.exception = e;
                return 0;
            }
        }

        @Override
        protected void onPostExecute(Integer answer) {
            responseCode = answer;
            if (mProgressDialog != null)
                mProgressDialog.dismiss();

            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            Intent myIntent = new Intent(context, MainActivity.class);
            myIntent.putExtra("ResponseCode", 1);
            context.startActivity(myIntent);
        }
    }
}
