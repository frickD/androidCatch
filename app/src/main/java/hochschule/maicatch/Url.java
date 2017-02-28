package hochschule.maicatch;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to extract urls
 * @author marcelm
 *
 */
public class Url {
	
	private final String urlPattern = "[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)";

	/**
	 * Extract URLs from the input <br>
	 * 1. Search chains which fit to the loose regex format of URL´s and replace
	 * spam-protectin of e-mails like [at] with @ <br>
	 * 2. ignore chains with '@', they are e-mails <br>
	 * 3. if it contains no @: <br>
	 * 4. clean "//", couldn´t exclude it with the regex-pattern <br>
	 * 5. append the url to the list <br>
	 * urlPattern from this source:
	 * http://stackoverflow.com/questions/3809401/what-is-a-good-regular-
	 * expression-to-match-a-url <br>
	 * you could use it for crawling e-mail too, but I decided to use my own
	 * solution for that, because it´s faster(uses less resources)
	 * 
	 * @param input
	 *            String to search for URL´s
	 * @param urls
	 *            list to which the results are appended
	 */
	public void isUrl(String input, List<String> urls) {
		// 1.
		Replacer rep = new Replacer();
		input = rep.correction(input);

		Matcher matcher = Pattern.compile(urlPattern).matcher(input);
		while (matcher.find()) {

			String cleanUrl = matcher.group();
			// 2.
			if (cleanUrl.contains("@")) {

			} else {
				// 3. 4. 5.
				urls.add(cleanUrl.trim());
			}

		}
	}

}
