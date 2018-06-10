package analizers;

import java.util.ArrayList;
import java.util.List;

public class VowelAndconsonantCompare implements IAnalizer {

	private List<Character> vowels;
	
	public VowelAndconsonantCompare() {
		this.vowels = new ArrayList<>();
		this.vowels.add('a');
		this.vowels.add('e');
		this.vowels.add('y');
		this.vowels.add('i');
		this.vowels.add('o');
		this.vowels.add('¹');
		this.vowels.add('ê');
		this.vowels.add('u');
		this.vowels.add('ó');
		this.vowels.add('a');
		this.vowels.add('a');
		this.vowels.add('a');
	}
	
	@Override
	public String apply(String text) {
		int vowels = 0;
		int consonants = 0;
		for(int i = 0; i < text.length(); i++) {
			if(this.vowels.contains(text.charAt(i))) {
				vowels++;
			}
			else {
				consonants++;
			}
		}
		if(vowels == consonants) {
			return "Same amount of vowels and consonants";
		}
		else if(vowels > consonants) {
			return "More vowels";
		}
		else {
			return "More consonants";
		}
	}

	@Override
	public String getName() {
		return "Vowel and Consonant compare";
	}

}
