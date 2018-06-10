package analizers;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

public class MostUsedWord implements IAnalizer {

	@Override
	public String apply(String text) {
		String[] words = text.split(" ");
		HashMap<String, Integer> wordCountMap = new HashMap<String, Integer>();
        for (String word : words)
        {
            if(wordCountMap.containsKey(word))
            {    
                wordCountMap.put(word, wordCountMap.get(word)+1);
            }
            else
            {
                wordCountMap.put(word, 1);
            }
        }
        
        String mostRepeatedWord = null;
        int count = 0;
        Set<Entry<String, Integer>> entrySet = wordCountMap.entrySet();
         
        for (Entry<String, Integer> entry : entrySet)
        {
            if(entry.getValue() > count)
            {
                mostRepeatedWord = entry.getKey();
                count = entry.getValue();
            }
        }
         
        return mostRepeatedWord + ": " + count;
	}

	@Override
	public String getName() {
		return "Most used word";
	}
}
