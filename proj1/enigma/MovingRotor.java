package enigma;

import static enigma.EnigmaException.*;

/** Class that represents a rotating rotor in the enigma machine.
 *  @author Ziyuan Tang
 */
class MovingRotor extends Rotor {

    /** A rotor named NAME whose permutation in its default setting is
     *  PERM, and whose notches are at the positions indicated in NOTCHES.
     *  The Rotor is initally in its 0 setting (first character of its
     *  alphabet).
     */
    MovingRotor(String name, Permutation perm, String notches) {
        super(name, perm);
        this._notches = notches;
        this._oldnotches = notches;
    }
    /**not reflecting in moving rotor return FALSE. */
    boolean reflecting() {
        return false;
    }
    /**wrap helper P return R.*/
    public int wraphelper2(int p) {
        int r = p % alphabet().size();
        if (r < 0) {
            r += alphabet().size();
        }
        return r;
    }

    @Override
    void originalNotch() {
        this._notches = _oldnotches;
    }

    @Override
    void changeNotches(String onering) {
        StringBuilder newNotch = new StringBuilder();
        for (int i = 0; i < _notches.length(); i++) {
            int ringPostion = alphabet().toInt(onering.charAt(0));
            int curPosition = alphabet().toInt(_notches.charAt(i));
            newNotch.append(alphabet().
                    toChar(wraphelper2(curPosition - ringPostion)));
        }
        this._notches = newNotch.toString();
    }
    @Override
    boolean atNotch() {
        int a = setting();
        for (int i = 0; i < _notches.length(); i++) {
            if (alphabet().toChar(setting()) ==  _notches.charAt(i)) {
                return true;
            }
        }
        return false;
    }

    @Override
    boolean rotates() {

        return true;
    }

    @Override
    void advance() {
        set(permutation().wrap(setting() + 1));
    }

    /**my _NOTCHES.*/
    private String _notches;
    /**my _OLDNOTCHES.*/
    private String _oldnotches;
}
