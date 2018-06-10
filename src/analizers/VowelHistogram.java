package analizers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class VowelHistogram implements IAnalizer {

	private HashMap<String, Integer> vowelMap;
	private List<String> vowels;
	@Override
	public String apply(String text) {
		this.vowelMap = new HashMap<>();
		
		this.vowels = new ArrayList<>();
		this.vowels.add("a");
		this.vowels.add("e");
		this.vowels.add("y");
		this.vowels.add("i");
		this.vowels.add("o");
		this.vowels.add("¹");
		this.vowels.add("ê");
		this.vowels.add("u");
		this.vowels.add("ó");
		this.vowels.add("a");
		this.vowels.add("a");
		this.vowels.add("a");
		
		for(String vowel : this.vowels) {
			this.vowelMap.put(vowel, this.countChar(text, vowel));
		}
		
		return this.getResult();
	}

	private String getResult() {
		StringBuilder builder = new StringBuilder();
		Iterator<Entry<String, Integer>> it = this.vowelMap.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry pair = (Map.Entry)it.next();
	        builder.append(pair.getKey() + ":" + pair.getValue() + " ");
	        it.remove();
	    }
		return builder.toString();
	}

	@Override
	public String getName() {
		return "Vovel histogram";
	}

	
	private int countChar(String str, String c) {
	    int start = -1;
	    int count = 0;
	    while (true) {
	      if ((start = str.indexOf(c, start + 1)) == -1)
	        return (count);
	      count++;
	    }
	  }
}
