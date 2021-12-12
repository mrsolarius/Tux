package fr.litopia.tux.game.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class Word {
    /**
     * Permet de normaliser le mot en supprimant les accents et les caractères spéciaux
     * @param str : String à normaliser
     * @return str : String normalisé
     */
    public static String normalize(String str) {
        String normalized = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(normalized).replaceAll("");
    }
}
