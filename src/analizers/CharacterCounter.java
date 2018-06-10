package analizers;

public class CharacterCounter implements IAnalizer {

	@Override
	public String apply(String text) {
		int counter = 0;
		for(int i = 0; i < text.length(); i++) {
			counter++;
		}
		return Integer.toString(counter);
	}

	@Override
	public String getName() {
		return "Character count";
	}

}
