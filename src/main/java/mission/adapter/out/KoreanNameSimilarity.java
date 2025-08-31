package mission.adapter.out;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

public final class KoreanNameSimilarity {

    // ===== 공개 API =====
    public static <T> java.util.Optional<Scored<T>> top1(
            String query, List<T> items, double minScore, java.util.function.Function<T, String> nameGetter) {
        return top1(query, items, nameGetter)
                .filter(s -> s.score() >= minScore);
    }

    public static <T> java.util.Optional<Scored<T>> top1(
            String query, List<T> items, java.util.function.Function<T, String> nameGetter) {
        return items.stream()
                .map(it -> new Scored<>(it, similarity(query, nameGetter.apply(it))))
                .max(Comparator.comparingDouble(Scored<T>::score));
    }

    public static double similarity(String a, String b) {
        String na = normalizeKo(a);
        String nb = normalizeKo(b);

        // 자모/초성 변환
        String jamoA = toJamo(na);
        String jamoB = toJamo(nb);
        String choA  = toChosung(na);
        String choB  = toChosung(nb);

        // n-gram(Jaccard)
        Set<String> gA = ngrams(na.replace(" ", ""), 3);
        Set<String> gB = ngrams(nb.replace(" ", ""), 3);
        double jaccard = jaccard(gA, gB);

        // Jaro-Winkler
        double jwRaw   = jaroWinkler(na, nb);
        double jwJamo  = jaroWinkler(jamoA, jamoB);
        double jwChos  = jaroWinkler(choA, choB);

        // 가중합 (원하면 조정)
        double score = 0.40 * jwJamo
                + 0.25 * jwRaw
                + 0.25 * jaccard
                + 0.10 * jwChos;
        return clamp01(score);
    }

    public static <T> List<Scored<T>> topK(String query, List<T> items, int k,
                                           java.util.function.Function<T,String> nameGetter) {
        return items.stream()
                .map(it -> new Scored<>(it, similarity(query, nameGetter.apply(it))))
                .sorted(Comparator.comparingDouble(Scored<T>::score).reversed())
                .limit(k)
                .collect(Collectors.toList());
    }

    public record Scored<T>(T item, double score) {}

    // ===== 한글 유틸 =====
    private static String normalizeKo(String s) {
        if (s == null) return "";
        String t = Normalizer.normalize(s, Normalizer.Form.NFKC)
                .toLowerCase(Locale.ROOT)
                .replaceAll("[\\p{Punct}]", " ")
                .replaceAll("\\s+", " ")
                .trim();
        return t;
    }

    // 초성/중성/종성 테이블
    private static final char HANGUL_BASE = 0xAC00;
    private static final int  CHO_CNT = 19, JUNG_CNT = 21, JONG_CNT = 28;
    private static final String[] CHO = {"ㄱ","ㄲ","ㄴ","ㄷ","ㄸ","ㄹ","ㅁ","ㅂ","ㅃ","ㅅ","ㅆ","ㅇ","ㅈ","ㅉ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"};
    private static final String[] JUNG= {"ㅏ","ㅐ","ㅑ","ㅒ","ㅓ","ㅔ","ㅕ","ㅖ","ㅗ","ㅘ","ㅙ","ㅚ","ㅛ","ㅜ","ㅝ","ㅞ","ㅟ","ㅠ","ㅡ","ㅢ","ㅣ"};
    private static final String[] JONG= {"","ㄱ","ㄲ","ㄳ","ㄴ","ㄵ","ㄶ","ㄷ","ㄹ","ㄺ","ㄻ","ㄼ","ㄽ","ㄾ","ㄿ","ㅀ","ㅁ","ㅂ","ㅄ","ㅅ","ㅆ","ㅇ","ㅈ","ㅊ","ㅋ","ㅌ","ㅍ","ㅎ"};

    public static String toChosung(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (isHangulSyllable(ch)) {
                int idx = ch - HANGUL_BASE;
                int cho = idx / (JUNG_CNT * JONG_CNT);
                sb.append(CHO[cho]);
            } else if (!Character.isWhitespace(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    public static String toJamo(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (isHangulSyllable(ch)) {
                int idx = ch - HANGUL_BASE;
                int cho  = idx / (JUNG_CNT * JONG_CNT);
                int jung = (idx % (JUNG_CNT * JONG_CNT)) / JONG_CNT;
                int jong = idx % JONG_CNT;
                sb.append(CHO[cho]).append(JUNG[jung]);
                if (jong != 0) sb.append(JONG[jong]);
            } else if (!Character.isWhitespace(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }

    private static boolean isHangulSyllable(char ch) {
        return ch >= 0xAC00 && ch <= 0xD7A3;
    }

    // ===== n-gram / Jaccard =====
    private static Set<String> ngrams(String s, int n) {
        Set<String> set = new HashSet<>();
        if (s.length() < n) { if (!s.isEmpty()) set.add(s); return set; }
        for (int i = 0; i <= s.length() - n; i++) set.add(s.substring(i, i + n));
        return set;
    }

    private static double jaccard(Set<String> a, Set<String> b) {
        if (a.isEmpty() && b.isEmpty()) return 1.0;
        Set<String> inter = new HashSet<>(a); inter.retainAll(b);
        Set<String> union = new HashSet<>(a); union.addAll(b);
        return (double) inter.size() / (double) union.size();
    }

    // ===== Jaro-Winkler =====
    public static double jaroWinkler(String s1, String s2) {
        if (s1.equals(s2)) return 1.0;
        int m = 0;

        int[] s1Matches = new int[s1.length()];
        int[] s2Matches = new int[s2.length()];
        Arrays.fill(s1Matches, -1);
        Arrays.fill(s2Matches, -1);

        int matchDistance = Math.max(s1.length(), s2.length()) / 2 - 1;

        // matches
        for (int i = 0; i < s1.length(); i++) {
            int start = Math.max(0, i - matchDistance);
            int end   = Math.min(i + matchDistance + 1, s2.length());
            for (int j = start; j < end; j++) {
                if (s2Matches[j] != -1) continue;
                if (s1.charAt(i) != s2.charAt(j)) continue;
                s1Matches[i] = j;
                s2Matches[j] = i;
                m++;
                break;
            }
        }
        if (m == 0) return 0.0;

        // transpositions
        char[] s1MatchChars = new char[m];
        char[] s2MatchChars = new char[m];
        int si = 0;
        for (int i = 0; i < s1.length(); i++) if (s1Matches[i] != -1) s1MatchChars[si++] = s1.charAt(i);
        int sj = 0;
        for (int j = 0; j < s2.length(); j++) if (s2Matches[j] != -1) s2MatchChars[sj++] = s2.charAt(j);

        int t = 0;
        for (int i = 0; i < m; i++) if (s1MatchChars[i] != s2MatchChars[i]) t++;
        t /= 2;

        double jaro = ( (m / (double) s1.length())
                + (m / (double) s2.length())
                + ((m - t) / (double) m) ) / 3.0;

        // Winkler prefix boost
        int prefix = 0;
        for (int i = 0; i < Math.min(4, Math.min(s1.length(), s2.length())); i++) {
            if (s1.charAt(i) == s2.charAt(i)) prefix++; else break;
        }
        return jaro + prefix * 0.1 * (1 - jaro);
    }

    private static double clamp01(double x) {
        return x < 0 ? 0 : (x > 1 ? 1 : x);
    }
}