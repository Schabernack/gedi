public class StringFormatter {
	
	
	public String formatText(String text){
		String[] words;
		String tmp="";
		
		words=text.split(" ");
		
		for (int i=0;i<words.length;i++){
			tmp+=formatWord(words[i])+" ";
		}
		return tmp;
	}

	public String formatWord(String word){
		word = word.replaceAll("[^\\sa-zA-Z0-9_-]","");
		if(word.length()>1){
			if (Character.isLowerCase(word.charAt(1))){
				word=word.toLowerCase();
			}
		}
		return word;
	}
}
