package enigma;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author ziyuan tang
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _allRotors = allRotors;
        _numRotors = numRotors;
        _pawls = pawls;
        if (_numRotors - _pawls < 1) {
            throw new EnigmaException("bad setting: "
                   + "input for numRotor and pawls");
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }
    /** Return the _ALPHABET I have. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }


    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    public void insertRotors(String[] rotors) {
        rotorWillBeInsert = rotors;
        int rotorNum = 1;
        for (String subrotor: rotors) {
            for (Rotor myrotor: _allRotors) {
                if (myrotor.name().equals(subrotor)) {
                    if (!myrotor.reflecting() && rotorNum == 1) {
                        throw new EnigmaException(""
                               + "only reflect rotor can be on leftmost");
                    }
                    if (!myrotor.rotates() && !myrotor.reflecting()
                            && (rotorNum <= 1
                            || rotorNum > numRotors() - numPawls())) {
                        throw new EnigmaException("fixed "
                               + "rotor can only occur at position 2");
                    }
                    rotorMap.put(rotorNum, myrotor);

                }
            }
            if (rotorMap.get(rotorNum) == null) {
                throw new EnigmaException("Name mismatch");
            }
            rotorNum += 1;
        }
        checkRotor(rotorWillBeInsert);
    }
/**input a string list of ROTORS and check if any error.*/
    void checkRotor(String[] rotors) {
        int fixNum = numRotors() - numPawls();

        for (int i = 1; i < numRotors() + 1; i++) {
            if (i == 1 && !rotorMap.get(i).reflecting()) {
                throw new EnigmaException("only "
                       + "reflect rotor can be on leftmost");
            } else if (1 < i && i <= fixNum && (rotorMap.get(i).
                    rotates() || rotorMap.get(i).reflecting())) {
                throw new EnigmaException("start by 2 in "
                       + "range of fixnum, should be a fixed rotor but not");
            } else {
                if (fixNum < i && !rotorMap.get(i).rotates()) {
                    throw new EnigmaException("bad "
                           + "setting: should be moving rotors");
                }
            }
            if (rotors.length != numRotors()) {
                throw new EnigmaException("bad setting: number mismatch");
            }
        }
    }
    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        String[] set = setting.split("[ \\t\\n\\r]");
        if (set.length == 1) {
            if (setting.length() != numRotors() - 1) {
                throw new EnigmaException("Set my error");
            }

            for (int i = 0; i < setting.length(); i++) {
                if (_alphabet.toInt(setting.charAt(i)) == -1) {
                    throw new EnigmaException("the charactor in ");
                }
                int setTo = _alphabet.toInt(setting.charAt(i));
                rotorMap.get(i + 2).set(setTo);
            }
        } else if (set.length == 2 && firsttime) {
            firsttime = false;
            if (setting.length() != numRotors() * 2 - 1) {
                throw error("Wrong Ring format.");
            }
            String setts = set[0];
            String ring = set[1];
            String c = setting;
            for (int i = 0; i < (setting.length() - 1) / 2; i++) {
                int setTo1 = _alphabet.toInt(setting.charAt(i));
                int setTo2 = _alphabet.toInt(setting.
                        charAt(i + numRotors()));
                rotorMap.get(i + 2).set(wraphelper(setTo1 - setTo2));
                rotorMap.get(i + 2).changeNotches(String.
                        valueOf(ring.charAt(i)));
            }
        } else if (set.length == 2 && !firsttime) {
            if (setting.length() != numRotors() * 2 - 1) {
                throw error("Wrong Ring format.");
            }
            String setts = set[0];
            String ring = set[1];
            String c = setting;
            for (int i = 0; i < (setting.length() - 1) / 2; i++) {
                int setTo1 = _alphabet.toInt(setting.charAt(i));
                int setTo2 = _alphabet.toInt(setting.charAt(i + numRotors()));
                rotorMap.get(i + 2).set(wraphelper(setTo1 - setTo2));
                rotorMap.get(i + 2).originalNotch();
                rotorMap.get(i + 2).changeNotches(String.
                        valueOf(ring.charAt(i)));
            }
        }

    }

    /** WRAPHELPER helper with input P and return R. */
    public int wraphelper(int p) {
        int r = p % _alphabet.size();
        if (r < 0) {
            r += _alphabet.size();
        }
        return r;
    }
    /** MOVE1 helper. */
    void move1() {
        int rotorSize = rotorMap.size();
        if (rotorMap.get(rotorSize - 1).atNotch()
                || (rotorMap.get(rotorSize - 1).atNotch()
                        && rotorMap.get(rotorSize).atNotch())) {
            rotorMap.get(rotorSize).advance();
            rotorMap.get(rotorSize - 1).advance();
            rotorMap.get(rotorSize - 2).advance();

        } else {
            if (rotorMap.get(rotorSize).atNotch()) {
                if (rotorMap.get(rotorSize - 1).rotates()) {
                    rotorMap.get(rotorSize).advance();
                    rotorMap.get(rotorSize - 1).advance();
                } else {
                    rotorMap.get(rotorSize).advance();
                }
                for (int i = 1; i < rotorSize - 1; i++) {
                    if (rotorMap.get(i).atNotch()
                            && rotorMap.get(i - 1).rotates()) {
                        rotorMap.get(i).advance();
                        rotorMap.get(i - 1).advance();
                    }
                }
            } else {
                rotorMap.get(rotorSize).advance();
                for (int i = 1; i < rotorSize - 1; i++) {
                    if (rotorMap.get(i).atNotch()
                            && rotorMap.get(i - 1).rotates()) {
                        rotorMap.get(i).advance();
                        rotorMap.get(i - 1).advance();
                    }
                }
            }
        }
    }

/** MOVE. */
    void move() {
        int rotorSize = rotorMap.size();
        if (rotorSize == 2) {
            rotorMap.get(rotorSize).advance();
        } else if (rotorSize == 3) {
            if (rotorMap.get(rotorSize).atNotch()
                    && rotorMap.get(rotorSize - 1).rotates()) {
                rotorMap.get(rotorSize).advance();
                rotorMap.get(rotorSize - 1).advance();
            } else if (rotorMap.get(rotorSize).atNotch()
                    && !rotorMap.get(rotorSize - 1).rotates()) {
                rotorMap.get(rotorSize).advance();
            } else {
                rotorMap.get(rotorSize).advance();
            }
        } else if (rotorSize == 4) {
            if (rotorMap.get(2).rotates()) {
                move1();
            } else {
                if (rotorMap.get(rotorSize).atNotch()
                       && rotorMap.get(rotorSize - 1).rotates()) {
                    rotorMap.get(rotorSize).advance();
                    rotorMap.get(rotorSize - 1).advance();
                } else if (rotorMap.get(rotorSize).atNotch()
                        && !rotorMap.get(rotorSize - 1).rotates()) {
                    rotorMap.get(rotorSize).advance();
                } else {
                    rotorMap.get(rotorSize).advance();
                }
            }
        } else {
            move1();
        }
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }
    /**UPDATESETARRAY. */
    void updateSetArray() {
        for (int i = 2; i < rotorMap.size() + 1; i++) {
            settingArray.add(_alphabet.toChar(rotorMap.get(i).setting()));
        }
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        move();
        if (_plugboard != null) {
            char keyboardInput = _plugboard.alphabet().toChar(c);
            String afterPlug = _plugboard._cycleMap.
                    get(String.valueOf(keyboardInput));
            indexBeforeSetting = _alphabet.toInt(afterPlug.charAt(0));

        } else {
            indexBeforeSetting = c;
        }
        Rotor I = rotorMap.get(numRotors());
        int index = rotorMap.get(numRotors()).
                permutation().wrap(indexBeforeSetting);

        for (int i = 1; i < rotorMap.size(); i++) {
            index = rotorMap.get(numRotors() + 1 - i).
                    convertForward(index);
        }
        index = rotorMap.get(1).convertForward(index);

        for (int i = 2; i < rotorMap.size() + 1; i++) {
            index = rotorMap.get(i).convertBackward(index);
        }
        if (_plugboard != null) {
            String beforePlug = _plugboard._ReverseCycleMap.
                    get(String.valueOf(_alphabet.toChar(index)));
            index = _alphabet.toInt(beforePlug.charAt(0));
        }
        return index;
    }


    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        ArrayList<Integer> inputIndex = new ArrayList<>();
        ArrayList<Integer> ouputIndex = new ArrayList<>();
        ArrayList<Character> ouputChar = new ArrayList<>();
        msg = msg.replaceAll("\\s", "");
        for (int i = 0; i < msg.length(); i++) {
            if (_alphabet.toInt(msg.charAt(i)) == -1) {
                throw new EnigmaException("not in alphabet");
            }
            inputIndex.add(_alphabet.toInt(msg.charAt(i)));

        }
        for (Integer inputIndexx : inputIndex) {
            ouputIndex.add(convert(inputIndexx));
        }

        for (Integer ouputIndexx : ouputIndex) {
            ouputChar.add(_alphabet.toChar(ouputIndexx));
        }

        StringBuilder builder = new StringBuilder(ouputChar.size());
        for (Character ch: ouputChar) {
            builder.append(ch);
        }

        return builder.toString();
    }
    /** return _ALLROTORS. */
    Collection<Rotor> allRotors() {
        return _allRotors;
    }
    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;
    /** Common alphabet of my rotors. */
    private int _numRotors;
    /** Common alphabet of my rotors. */
    private int _pawls;
    /** Common alphabet of my rotors. */
    private Collection<Rotor> _allRotors;
    /** Common alphabet of my rotors. */
    private ArrayList<Rotor>  _rotorArray;
    /** Common alphabet of my rotors. */
    protected HashMap<Integer, Rotor> rotorMap = new HashMap<>();
    /** Common alphabet of my rotors. */
    protected Permutation _plugboard;
    /** Common alphabet of my rotors. */
    protected  ArrayList<Character>  settingArray = new ArrayList<>();
    /** Common alphabet of my rotors. */
    protected int indexBeforeSetting;
    /** Common alphabet of my rotors. */
    protected String[] rotorWillBeInsert;
    /** Common alphabet of my rotors. */
    private boolean firsttime = true;

}
