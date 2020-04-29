package enigma;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import java.util.Scanner;
import java.util.Collection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.NoSuchElementException;


import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Ziyuan Tang
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }

        _config = getInput(args[0]);

        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }

        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        _machine = this.readConfig();
        if (_input.hasNext()) {
            boolean empty = true;
            settingline = _input.nextLine().trim();
            while (empty) {
                if (!settingline.isEmpty()) {
                    empty = false;
                } else {
                    if (_input.hasNext()) {
                        _output.append("\n");
                    } else {
                        throw new EnigmaException("bad input: no input");
                    }
                    settingline = _input.nextLine().trim();
                }
            }
            if (settingline.isEmpty()) {
                throw new EnigmaException("Setting line is empty");
            }
            if (settingline.charAt(0) != '*') {
                throw new EnigmaException("Setting should have a *");
            }
        } else {
            throw new EnigmaException("Input file is emtpy, what???");
        }
        boolean firstsetup = true;
        while (_input.hasNext()) {
            if (!firstsetup) {
                _machine.rotorMap = new HashMap<>();
                setUp(_machine, settingline);
            } else {
                setUp(_machine, settingline);
                firstsetup = false;
            }
            settingline = _input.nextLine();
            while (!settingline.contains("*")) {
                printMessageLine(_machine.convert(settingline));
                if (_input.hasNextLine()) {
                    settingline = _input.nextLine();
                } else {
                    break;
                }
            }
        }
    }



    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            int numRotor;
            int numPawl;
            String alp;
            if (_config.hasNext()) {
                alp = _config.next().trim();
            } else {
                throw new EnigmaException("There is not alphabet");
            }
            _alphabet = new Alphabet(alp);

            if (_config.hasNextInt()) {
                numRotor = _config.nextInt();
            } else {
                throw new EnigmaException("There is no number for rotors");
            }

            if (_config.hasNextInt()) {
                numPawl = _config.nextInt();
            } else {
                throw new EnigmaException("There is no number for pawls");
            }
            if (numRotor < numPawl) {
                throw new EnigmaException("More pawls than rotors");
            }
            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotor, numPawl, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /**check if notch in alp.*/
    void checknotch() {
        String rotorNotches = roDes.substring(1);
        for (int i = 0; i < rotorNotches.length(); i++) {
            if (!_alphabet.contains(rotorNotches.charAt(i))) {
                throw new EnigmaException("The notch is not in the alp");
            }
        }
    }
    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        try {
            String roName = _config.next(); roDes = _config.next();
            Rotor rotor;
            if (roDes.length() < 1) {
                throw new EnigmaException("wrong description for rotor");
            }
            String rotorType = roDes.substring(0, 1);
            String M = "M"; String N = "N"; String R = "R";
            if (!rotorType.equals(M) && !rotorType.equals(N) && !rotorType.
                    equals(R)) {
                throw new EnigmaException("Rotype confi X");
            }
            checknotch(); String rotorNotches = roDes.substring(1);
            StringBuilder permutation = new StringBuilder();
            StringBuilder cycle = new StringBuilder(_config.next());
            if (cycle.charAt(0) != '(') {
                throw new EnigmaException("The permu not starting with (");
            }
            String cycleTest = cycle.toString().trim();
            if (cycleTest.charAt(cycleTest.length() - 1) != ')') {
                throw new EnigmaException("The permutation is not end with )");
            }
            while (cycle.charAt(0) == '(') {
                permutation.append(cycle.toString()); permutation.append(" ");
                if (_config.hasNext() && _config.hasNext("[[ \\t\\n\\r]?"
                       + "[\\(][A-Za-z01.23.45.6789_]*[\\)][ \\t\\n\\r]?]*")) {
                    if (!_config.hasNext("[A-Za-z012345..6789_]*")) {
                        cycle = new StringBuilder(_config.next());
                        if (cycle.charAt(0) != '(') {
                            break;
                        }
                    } else {
                        break;
                    }
                } else {
                    break;
                }
            }
            Permutation p = new Permutation(permutation.toString(), _alphabet);
            if (rotorType.equals("M")) {
                rotor = new MovingRotor(roName, p, rotorNotches);
            } else if (rotorType.equals("N")) {
                rotor = new FixedRotor(roName, p);
            } else if (rotorType.equals("R")) {
                String testLength = permutation.toString();
                testLength = testLength.replaceAll("[() \\t\\n\\r]", "");
                if (testLength.length() == _alphabet.size()) {
                    rotor = new Reflector(roName, p);
                } else {
                    throw new EnigmaException("Hey! wrong");
                }
            } else {
                throw new EnigmaException("The rotor type is not M or N or R");
            }
            return rotor;
        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
    }
    /** check CHECKREPEATROTOR using ROTORS return FALSE.*/
    public boolean checkRepeatRotor(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            for (int j = i + 1; j < rotors.length; j++) {
                if (rotors[i].equals(rotors[j])) {
                    return true;
                }
            }
        } return false;
    }
    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    public void setUp(Machine M, String settings) {
        Machine helperM = M; numRotors = M.numRotors(); numPawls = M.numPawls();
        settings = settings.trim();
        if (numPawls >= numRotors || settings.trim().charAt(0) != '*') {
            throw new EnigmaException("bad settingg!");
        }
        rotorsStringList = settings.split("[ \\t\\n\\r]");
        List<String> list = new ArrayList<String>(Arrays.
                asList(rotorsStringList));
        list.removeIf(element -> element.equals(""));
        rotorsStringList = list.toArray(new String[0]);
        settings.replace("*", "");
        if (rotorsStringList.length < numRotors + 1) {
            throw new EnigmaException("bad setting5");
        }
        String[] rotorWillBeUsed = new String[numRotors];
        System.arraycopy(rotorsStringList, 1,
                rotorWillBeUsed, 0, numRotors);
        if (checkRepeatRotor(rotorWillBeUsed)) {
            throw new EnigmaException("Repeated rotors in setting, no way!!");
        }
        if (rotorsStringList.length < numRotors + 2) {
            throw new EnigmaException("miss initial setting");
        }
        String setting = rotorsStringList[numRotors + 1];
        if (rotorsStringList.length > numRotors + 2
                && rotorsStringList[numRotors + 2].charAt(0) != '(') {
            if (rotorsStringList[numRotors + 2].length()
                    == rotorsStringList[numRotors + 1].length()) {
                setting = setting + " " + rotorsStringList[numRotors + 2];
                hasRing = true;
            } else {
                throw new EnigmaException("bad ring setting");
            }
        } else {
            hasRing = false;
        }
        if (setting.charAt(0) == '(') {
            throw new EnigmaException("miss initial setting!!");
        }
        if (setting.length() != M.numRotors() - 1
                && setting.length() != M.numRotors() * 2 - 1) {
            throw new EnigmaException("bad setting1");
        }
        char[] initialsettingChar = setting.
                replaceAll(" ", "").toCharArray();
        for (char c : initialsettingChar) {
            if (!_alphabet.contains(c)) {
                throw new EnigmaException("bad setting2");
            }
        }
        if (!hasRing && rotorsStringList.length > M.numRotors() + 2
                && rotorsStringList[M.numRotors() + 2].charAt(0) != '(') {
            throw new EnigmaException("bad setting3");
        }
        M.insertRotors(rotorWillBeUsed);
        setupPlugBoard(helperM, rotorsStringList, hasRing);
        M.setRotors(setting);
    }


    /**help SETUPPLUGBOARD within M, ROTORSSTRINGLIST, WITHRING.*/
    void setupPlugBoard(Machine M,
                        String[] rotorsStringlist, boolean withRing) {
        if (!withRing) {
            String[] plugBoardList = new String[
                    rotorsStringlist.length - numRotors - 2];
            System.arraycopy(rotorsStringlist, numRotors + 2,
                    plugBoardList, 0,
                    rotorsStringlist.length - numRotors - 2);

            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < plugBoardList.length; i++) {
                sb.append(plugBoardList[i]);
            }
            String plugBoard = Arrays.toString(plugBoardList);
            plugBoard = plugBoard.replaceAll(",", "");
            if (plugBoard.charAt(1) == '(') {
                Boolean test = plugBoard.contains(",");
                M.setPlugboard(new Permutation(plugBoard.substring(1,
                        plugBoard.length() - 1), _alphabet));
            }
        } else {
            if (rotorsStringlist.length - numRotors - 3 > 0) {
                String[] plugBoardList = new String
                        [rotorsStringlist.length - numRotors - 3];
                System.arraycopy(rotorsStringlist, numRotors + 3,
                        plugBoardList,
                        0, rotorsStringlist.length - numRotors - 3);

                StringBuffer sbsb = new StringBuffer();
                for (int i = 0; i < plugBoardList.length; i++) {
                    sbsb.append(plugBoardList[i]);
                }
                String plugBoard = Arrays.toString(plugBoardList);
                plugBoard = plugBoard.replaceAll(",", "");
                if (plugBoard.charAt(1) == '(') {
                    M.setPlugboard(new Permutation(plugBoard.
                            substring(1, plugBoard.length() - 1), _alphabet));
                }
            } else {
                M.setPlugboard(new Permutation("", _alphabet));
            }
        }
    }



    /** print addtional line between


    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        if (msg.isEmpty()) {
            _output.append("\n");
        }
        while (msg.length() != 0) {
            if (msg.length() >= 5) {
                String five = msg.substring(0, 5);
                msg = msg.substring(5);
                if (msg.isEmpty()) {
                    five = five + "\n";
                } else {
                    five = five + " ";
                }
                _output.append(five);

            } else {
                msg = msg + "\n";
                _output.append(msg);
                msg = "";
            }
        }
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;


    /** collection of ALLROTORS. */
    private Collection<Rotor> allRotors = new ArrayList<>();
    /** my ROTORSTRINGLIST. */
    private String[] rotorsStringList;
    /** my _MACHINE. */
    private Machine _machine;
    /** my NUMROTORS. */
    private int numRotors;
    /**my NUMPAWLS. */
    private int numPawls;
    /** my SETTINGLINE. */
    private String settingline;
    /** judge HASRING or not. */
    private boolean hasRing = false;
    /**for RODES rotor description.*/
    private String roDes;
}
