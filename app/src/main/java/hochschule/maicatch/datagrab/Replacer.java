/**
 * @author Marcel Mansouri
 */

package hochschule.maicatch.datagrab;

/**
 * Replaces spam-protection in e-mail adresses
 * @author marcelm
 */
public class Replacer {
	
	
	/**
	 * Trys to replace spam-protectin mail-adresses
	 * @param mail e-mail-address
	 * @return returns corrected string
	 */
	public String correction(String mail) {

		// US
		// at-square
		if (mail.contains("[at]")) {
			mail = mail.replace("[at]", "@");
		}
		if (mail.contains(" [at] ")) {
			mail = mail.replace(" [at] ", "@");
		}
		if (mail.contains("[AT]")) {
			mail = mail.replace("[AT]", "@");
		}
		if (mail.contains(" [AT] ")) {
			mail = mail.replace(" [AT] ", "@");
		}
		if (mail.contains("[aT]")) {
			mail = mail.replace("[aT]", "@");
		}
		if (mail.contains(" [aT] ")) {
			mail = mail.replace(" [aT] ", "@");
		}
		if (mail.contains("[At]")) {
			mail = mail.replace("[At]", "@");
		}
		if (mail.contains(" [At] ")) {
			mail = mail.replace(" [At] ", "@");
		}

		// at-clip
		if (mail.contains("(at)")) {
			mail = mail.replace("(at)", "@");
		}
		if (mail.contains(" (at) ")) {
			mail = mail.replace(" (at) ", "@");
		}
		if (mail.contains("(AT)")) {
			mail = mail.replace("(AT)", "@");
		}
		if (mail.contains(" (AT) ")) {
			mail = mail.replace(" (AT) ", "@");
		}
		if (mail.contains("(aT)")) {
			mail = mail.replace("(aT)", "@");
		}
		if (mail.contains(" (aT) ")) {
			mail = mail.replace(" (aT) ", "@");
		}
		if (mail.contains("(At)")) {
			mail = mail.replace("(At)", "@");
		}
		if (mail.contains(" (At) ")) {
			mail = mail.replace(" (At) ", "@");
		}

		// dot
		if (mail.contains("dot")) {
			mail = mail.replace("dot", ".");
		}
		if (mail.contains("DOT")) {
			mail = mail.replace("DOT", ".");
		}
		if (mail.contains("Dot")) {
			mail = mail.replace("Dot", ".");
		}
		if (mail.contains("DOt")) {
			mail = mail.replace("DOt", ".");
		}
		if (mail.contains("dOt")) {
			mail = mail.replace("dOt", ".");
		}
		if (mail.contains("doT")) {
			mail = mail.replace("doT", ".");
		}
		if (mail.contains("DoT")) {
			mail = mail.replace("DoT", ".");
		}
		// dot-square
		if (mail.contains("[dot]")) {
			mail = mail.replace("[dot]", ".");
		}
		if (mail.contains(" [dot] ")) {
			mail = mail.replace(" [dot] ", ".");
		}
		if (mail.contains("[DOT]")) {
			mail = mail.replace("[DOT]", ".");
		}
		if (mail.contains(" [DOT] ")) {
			mail = mail.replace(" [DOT] ", ".");
		}
		if (mail.contains("[Dot]")) {
			mail = mail.replace("[Dot]", ".");
		}
		if (mail.contains(" [Dot] ")) {
			mail = mail.replace(" [Dot] ", ".");
		}
		if (mail.contains("[DOt]")) {
			mail = mail.replace("[DOt]", ".");
		}
		if (mail.contains(" [DOt] ")) {
			mail = mail.replace(" [DOt] ", ".");
		}
		if (mail.contains("[dOt]")) {
			mail = mail.replace("[dOt]", ".");
		}
		if (mail.contains(" [dOt] ")) {
			mail = mail.replace(" [dOt] ", ".");
		}
		if (mail.contains("[doT]")) {
			mail = mail.replace("[doT]", ".");
		}
		if (mail.contains(" [doT] ")) {
			mail = mail.replace(" [doT] ", ".");
		}
		if (mail.contains("[DoT]")) {
			mail = mail.replace("[DoT]", ".");
		}
		if (mail.contains(" [DoT] ")) {
			mail = mail.replace(" [DoT] ", ".");
		}

		// dot-clip
		if (mail.contains("(dot)")) {
			mail = mail.replace("(dot)", ".");
		}
		if (mail.contains(" (dot) ")) {
			mail = mail.replace(" (dot) ", ".");
		}
		if (mail.contains("(DOT)")) {
			mail = mail.replace("(DOT)", ".");
		}
		if (mail.contains(" (DOT) ")) {
			mail = mail.replace(" (DOT) ", ".");
		}
		if (mail.contains("(Dot)")) {
			mail = mail.replace("(Dot)", ".");
		}
		if (mail.contains(" (Dot) ")) {
			mail = mail.replace(" (Dot) ", ".");
		}
		if (mail.contains("(DOt)")) {
			mail = mail.replace("(DOt)", ".");
		}
		if (mail.contains(" (DOt) ")) {
			mail = mail.replace(" (DOt) ", ".");
		}
		if (mail.contains("(dOt)")) {
			mail = mail.replace("(dOt)", ".");
		}
		if (mail.contains(" (dOt) ")) {
			mail = mail.replace(" (dOt) ", ".");
		}
		if (mail.contains("(doT)")) {
			mail = mail.replace("(doT)", ".");
		}
		if (mail.contains(" (doT) ")) {
			mail = mail.replace(" (doT) ", ".");
		}
		if (mail.contains("(DoT)")) {
			mail = mail.replace("(DoT)", ".");
		}
		if (mail.contains(" (DoT) ")) {
			mail = mail.replace(" (DoT) ", ".");
		}

		// DE
		// ät-square

		if (mail.contains("[ät]")) {
			mail = mail.replace("[ät]", "@");
		}
		if (mail.contains(" [ät] ")) {
			mail = mail.replace(" [ät] ", "@");
		}
		if (mail.contains("[Ät]")) {
			mail = mail.replace("[Ät]", "@");
		}
		if (mail.contains(" [Ät] ")) {
			mail = mail.replace(" [Ät] ", "@");
		}
		if (mail.contains("[ÄT]")) {
			mail = mail.replace("[ÄT]", "@");
		}
		if (mail.contains(" [ÄT] ")) {
			mail = mail.replace(" [ÄT] ", "@");
		}
		if (mail.contains("[ÄT]")) {
			mail = mail.replace("[ÄT]", "@");
		}
		if (mail.contains(" [ÄT] ")) {
			mail = mail.replace(" [ÄT] ", "@");
		}

		// ät-clip
		if (mail.contains("(ät)")) {
			mail = mail.replace("(ät)", "@");
		}
		if (mail.contains(" (ät) ")) {
			mail = mail.replace(" (ät) ", "@");
		}
		if (mail.contains("(Ät)")) {
			mail = mail.replace("(Ät)", "@");
		}
		if (mail.contains(" (Ät) ")) {
			mail = mail.replace(" (Ät) ", "@");
		}
		if (mail.contains("(ÄT)")) {
			mail = mail.replace("(ÄT)", "@");
		}
		if (mail.contains(" (ÄT) ")) {
			mail = mail.replace(" (ÄT) ", "@");
		}
		if (mail.contains("(ÄT)")) {
			mail = mail.replace("(ÄT)", "@");
		}
		if (mail.contains(" (ÄT) ")) {
			mail = mail.replace(" (ÄT) ", "@");
		}

		// punkt
		if (mail.contains("Punkt")) {
			mail = mail.replace("Punkt", ".");
		}
		if (mail.contains("PUNKT")) {
			mail = mail.replace("PUNKT", ".");
		}

		// punkt-square
		if (mail.contains("[Punkt]")) {
			mail = mail.replace("[Punkt]", ".");
		}
		if (mail.contains(" [Punkt] ")) {
			mail = mail.replace(" [Punkt] ", ".");
		}
		if (mail.contains("[PUNKT]")) {
			mail = mail.replace("[PUNKT]", ".");
		}
		if (mail.contains(" [PUNKT] ")) {
			mail = mail.replace(" [PUNKT] ", ".");
		}

		// punkt-clip
		if (mail.contains("(Punkt)")) {
			mail = mail.replace("(Punkt)", ".");
		}
		if (mail.contains(" (Punkt) ")) {
			mail = mail.replace(" (Punkt) ", ".");
		}
		if (mail.contains("(PUNKT)")) {
			mail = mail.replace("(PUNKT)", ".");
		}
		if (mail.contains(" (PUNKT) ")) {
			mail = mail.replace(" (PUNKT) ", ".");
		}

		return mail;
	}

}
