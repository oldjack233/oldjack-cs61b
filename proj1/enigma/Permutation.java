package enigma;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author Ziyuan Tang
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */
    Permutation(String cycles, Alphabet alphabet) {
        _alphabet = alphabet;
        _cycles = cycles;
        for (int m = 0; m < _alphabet.size(); m++) {
            copyOfAlp.add(Character.toString(_alphabet.toChar(m)));
        }
        int k = 0;
        while (k < _alphabet.size()) {
            _charToIntMap.put(_alphabet.toChar(k), k);
            _IntToCharMap.put(k, _alphabet.toChar(k));
            k += 1;
        }
        if (_cycles.length() == 0) {
            for (String s : copyOfAlp) {
                _cycleMap.put(s, s);
                _ReverseCycleMap.put(s, s);
            }
        } else {
            boolean tillend = false;
            while (!tillend) {
                int start = -1; int end = -1; int i = 0;
                while (end == -1 && i < _cycles.length()) {
                    if (_cycles.charAt(i) == '(') {
                        start = i;
                    }
                    if (_cycles.charAt(i) == ')') {
                        end = i;
                    }
                    i++;
                }
                if (end == -1 || start == -1) {
                    throw new EnigmaException("bad input: "
                           + "( should always be the start letter");
                }
                String oneCycle = _cycles.substring(start + 1, end);
                checkPerm(oneCycle);
                String mapOneCycle = oneCycle.substring(1)
                     .concat(oneCycle.substring(0, 1));
                for (int j = 0; j < oneCycle.length(); j++) {
                    String head = oneCycle.substring(j, j + 1);
                    String tail = mapOneCycle.substring(j, j + 1);
                    _cycleMap.put(head, tail);
                    _ReverseCycleMap.put(tail, head);
                    copyOfAlp.remove(head);
                    copyOfAlp.remove(tail);
                }
                if (_cycles.trim().length() == end - start + 1) {
                    tillend = true;
                } else {
                    _cycles = _cycles.substring(end + 1);
                }
            }
            for (String s : copyOfAlp) {
                _cycleMap.put(s, s);
                _ReverseCycleMap.put(s, s);
            }
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        _cycles.concat(cycle);
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return _alphabet.size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */
    int permute(int p) {
        if (!_cycleMap.containsKey(String.valueOf(alphabet().toChar(p)))) {
            throw new EnigmaException("Permute Error:"
                   + " there is no such interger or chara in the alphabet");
        } else {
            char from = _IntToCharMap.get(p);
            int to = _charToIntMap.get(permute(from));

            return to;
        }
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        if (!_ReverseCycleMap.containsKey(String.
                valueOf(alphabet().toChar(c)))) {
            throw new EnigmaException("Invert Error :"
                   + "there is no such interger or chara in the alphabet");
        } else {
            char from = _IntToCharMap.get(c);
            int to = _charToIntMap.get(invert(from));
            return to;
        }
    }


    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        if (!_cycleMap.containsKey(String.valueOf(p))) {
            throw new EnigmaException("Permute Error :"
                   + "there is no such interger or chara in the alphabet");
        } else {
            String a = String.valueOf(p);
            return  _cycleMap.get(a).charAt(0);
        }
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        if (!_ReverseCycleMap.containsKey(String.valueOf(c))) {
            throw new EnigmaException("Invert Error :"
                   + "there is no such interger or chara in the alphabet");
        } else {
            String a = String.valueOf(c);
            return _ReverseCycleMap.get(a).charAt(0);
        }
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        return copyOfAlp.size() == 0;
    }

    /**check CYC is in good shape.*/
    void checkPerm(String cyc) {
        for (int i = 0; i < cyc.length(); i++) {
            if (cyc.charAt(i) == ' ') {
                throw new
                        EnigmaException("in cycle there is a whitespcae");
            }
            if (!_alphabet.contains(cyc.charAt(i))) {
                throw new
                        EnigmaException("letter in perm not in alphabet");
            }
        }
        char[] cycChars = cyc.toCharArray();

        Map<Character, Integer> map = new HashMap<>();
        for (char c : cycChars) {
            if (map.containsKey(c)) {
                throw new
                        EnigmaException("bad input: repeated letter in cycles");
            } else {
                map.put(c, 1);
            }
        }
    }


    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    /** cycle of this permutation. */
    private String _cycles;

    /** new cycle map. */
    protected HashMap<String, String> _cycleMap = new HashMap<>();

    /** new reverse cycle map. */
    protected HashMap<String, String> _ReverseCycleMap = new HashMap<>();

    /** map char to int. */
    protected HashMap<Character, Integer> _charToIntMap = new HashMap<>();

    /** map int to char. */
    private HashMap<Integer, Character> _IntToCharMap = new HashMap<>();

    /**find unmapped chars. */
    private ArrayList<String> copyOfAlp = new ArrayList<>();

}
