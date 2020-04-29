package enigma;

/** An alphabet of encodable characters.  Provides a mapping from characters
 *  to and from indices into the alphabet.
 *  @author Ziyuan Tang
 */
class Alphabet {

    /** A new alphabet containing CHARS.  Character number #k has index
     *  K (numbering from 0). No character may be duplicated. */
    /**ah my CHARS.*/
    Alphabet(String chars) {
        for (int i = 0; i < chars.length(); i++) {
            for (int j = i + 1; j < chars.length(); j++) {
                if (chars.charAt(i) == (chars.charAt(j))) {
                    throw new EnigmaException("duplicated string in Alphabet");
                }
            }
            this._chars = chars;
        }
    }

    /** A default alphabet of all upper-case characters. */
    Alphabet() {
        this("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /** Returns the size of the alphabet. */
    int size() {

        return _chars.length();
    }

    /** Returns true if CH is in this alphabet. */
    boolean contains(char ch) {

        return _chars.contains(String.valueOf(ch));
    }

    /** Returns character number INDEX in the alphabet, where
     *  0 <= INDEX < size(). */
    char toChar(int index) {
        assert 0 <= index && index <= size();
        return _chars.charAt(index);
    }


    /** Returns the index of character CH which must be in
     *  the alphabet. This is the inverse of toChar().
     *  ///toInt will be -1 if ch is not in the alphabet*/
    int toInt(char ch) {
        return _chars.indexOf(ch);
    }

    /**my _CHARS.*/
    protected String _chars;

}
