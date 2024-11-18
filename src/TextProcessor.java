import java.util.HashMap;
import java.util.Map;

public class TextProcessor {

    public Map<String, Integer> buildWordIndex(String text) {
        Map<String, Integer> wordIndex = new HashMap<>();
        String[] words = text.split("\\s+");

        for (String word : words) {
            word = word.toLowerCase().replaceAll("[^a-zA-Z0-9]", "");
            if (!word.isEmpty()) {
                wordIndex.put(word, wordIndex.getOrDefault(word, 0) + 1);
            }
        }
        return wordIndex;
    }
}