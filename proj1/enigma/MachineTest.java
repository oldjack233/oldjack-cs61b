package enigma;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.Collection;


import static enigma.TestUtils.*;

/** Class that represents a complete enigma machine.
 *  @author ziyuan tang
 */
public class MachineTest {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */
    public MachineTest() {
    }


    Collection<Rotor> addrotors() {
        myrotors.add(rotorI);
        myrotors.add(rotorII);
        myrotors.add(rotorIII);
        myrotors.add(rotorIV);
        myrotors.add(rotorV);
        myrotors.add(rotorVI);
        myrotors.add(rotorVII);
        myrotors.add(rotorVIII);
        myrotors.add(rotorBETA);
        myrotors.add(rotorGAMMA);
        myrotors.add(reflectorB);
        myrotors.add(reflectorC);
        return myrotors;
    }
    void stepSetting12() {
        settingss.add('A');
        settingss.add('X');
        settingss.add('M');
        settingss.add('R');

    }
    void stepSetting597() {
        settingss.add('A');
        settingss.add('X');
        settingss.add('J');
        settingss.add('R');

    }

    void stepSetting598() {
        settingss.add('A');
        settingss.add('Y');
        settingss.add('K');
        settingss.add('S');

    }
    @Test
    public void checkInsertRotor() {
        _machine1 = new Machine(UPPER, 5, 3, addrotors());
        String[] rotorSeq = new String[] {"B", "Beta", "III", "IV", "I"};
        _machine1.insertRotors(rotorSeq);
        System.out.println(_machine1.rotorMap);
        assertEquals(reflectorB, _machine1.rotorMap.get(1));
    }

    @Test
    public void checkSettRotor() {
        _machine1 = new Machine(UPPER, 5, 3, myrotors);
        String[] rotorSeq = new String[] {"B", "Beta", "III", "IV", "I"};
        _machine1.insertRotors(rotorSeq);
        _machine1.setRotors("ABCD");
        assertEquals(0, _machine1.rotorMap.get(2).setting());
    }

    @Test
    public void checkConvertOnce() {
        _machine1 = new Machine(UPPER, 5, 3, myrotors);
        String[] rotorSeq = new String[] {"B", "Beta", "III", "IV", "I"};
        _machine1.insertRotors(rotorSeq);
        _machine1.setRotors("AXLE");
        _machine1._plugboard = permNAVI;
        int actual = _machine1.alphabet().toInt('Y');
        assertEquals(_machine1.alphabet().toInt('Z'),
                _machine1.convert(actual));

    }
    @Test
    public void checkConvertMutiple() {
        _machine1 = new Machine(UPPER, 5, 3, myrotors);
        String[] rotorSeq = new String[] {"B", "Beta", "III", "IV", "I"};
        _machine1.insertRotors(rotorSeq);
        _machine1.setRotors("AXLE");
        _machine1._plugboard = permNAVI;
        int actual = _machine1.alphabet().toInt('Y');

        for (int i = 0; i < 12; i++) {
            actual = _machine1.convert(actual);
        }
        _machine1.convert(2);
        _machine1.updateSetArray();
        stepSetting12();
        assertEquals(settingss, _machine1.settingArray);

        stepSetting597();

    }
    @Test
    public void checkConvertMutiple598() {
        _machine1 = new Machine(UPPER, 5, 3, myrotors);
        String[] rotorSeq = new String[] {"B", "Beta", "III", "IV", "I"};
        _machine1.insertRotors(rotorSeq);
        _machine1.setRotors("AXJR");
        _machine1._plugboard = permNAVI;

        _machine1.convert(2);
        _machine1.updateSetArray();
        stepSetting598();
        System.out.println(_machine1.settingArray);
        assertEquals(settingss, _machine1.settingArray);

    }

    @Test
    public void checkConvertMutiple597() {
        _machine1 = new Machine(UPPER, 5, 3, myrotors);
        String[] rotorSeq = new String[]{"B", "Beta", "III", "IV", "I"};
        _machine1.insertRotors(rotorSeq);
        _machine1.setRotors("AXLE");
        _machine1._plugboard = permNAVI;
        int actual = _machine1.alphabet().toInt('Y');

        for (int i = 0; i < 610; i++) {
            actual = _machine1.convert(actual);
        }
        _machine1.convert(2);
        _machine1.updateSetArray();
        stepSetting597();
        System.out.println(_machine1.settingArray);
        assertEquals(settingss, _machine1.settingArray);
    }

    @Test
    public void checkConvertMsg() {
        _machine1 = new Machine(UPPER, 5, 3, myrotors);
        String[] rotorSeq = new String[]{"B", "Beta", "III", "IV", "I"};
        _machine1.insertRotors(rotorSeq);
        _machine1.setRotors("AXLE");
        _machine1._plugboard = permSAMPLETEST;

        String actual = _machine1.convert("FROM HIS SHOULDER HIAWATHA");
        assertEquals("QVPQS OKOIL PUBKJ ZPISF XDW", actual);

    }



    private Permutation permI = new Permutation(
            "(AELTPHQXRU) (BKNW)"
                   + " (CMOY) (DFG) (IV) (JZ) (S)", UPPER);
    private MovingRotor rotorI = new MovingRotor(
            "I", permI, "Q");

    private Permutation permII = new Permutation(
            "(FIXVYOMW) (CDKLHUP)"
                   + " (ESZ) (BJ) (GR) (NT) (A) (Q)", UPPER);
    private MovingRotor rotorII = new MovingRotor(
            "II", permII, "E");

    private Permutation permIII = new Permutation(
            "(ABDHPEJT) (CFLVMZOYQIRWUKXSG) (N)", UPPER);;
    private MovingRotor rotorIII = new MovingRotor(
            "III", permIII, "V");

    private Permutation permIV = new Permutation(
            "(AEPLIYWCOXMRFZBSTGJQNH) (DV) (KU)", UPPER);;
    private MovingRotor rotorIV = new MovingRotor(
            "IV", permIV, "J");

    private Permutation permV = new Permutation(
            "((AVOLDRWFIUQ)(BZKSMNHYC) (EGTJPX)", UPPER);;
    private MovingRotor rotorV = new MovingRotor(
            "V", permV, "Z");

    private Permutation permVI = new Permutation(
            "(AJQDVLEOZWIYTS) (CGMNHFUX) (BPRK)", UPPER);;
    private MovingRotor rotorVI = new MovingRotor(
            "VI", permVI, "ZM");

    private Permutation permVII = new Permutation(
            "((ANOUPFRIMBZTLWKSVEGCJYDHXQ)", UPPER);;
    private MovingRotor rotorVII = new MovingRotor(
            "VII", permVII, "ZM");

    private Permutation permVIII = new Permutation(
            "(AFLSETWUNDHOZVICQ) (BKJ) (GXY) (MPR)", UPPER);;
    private MovingRotor rotorVIII = new MovingRotor(
            "VIII", permVIII, "ZM");

    private Permutation permBETA = new Permutation(
            "(ALBEVFCYODJWUGNMQTZSKPR) (HIX)", UPPER);;
    private FixedRotor rotorBETA = new FixedRotor(
            "BETA", permBETA);

    private Permutation permGAMMA = new Permutation(
            "(AFNIRLBSQWVXGUZDKMTPCOYJHE)", UPPER);;
    private FixedRotor rotorGAMMA = new FixedRotor(
            "GAMMA", permGAMMA);

    private Permutation permB = new Permutation(
            "(AE) (BN) (CK) (DQ) (FU) (GY) (HW) (IJ) (LO) (MP)\n"
            + "                    (RX) (SZ) (TV)", UPPER);;
    private Reflector reflectorB = new Reflector(
            "B", permB);

    private Permutation permC = new Permutation(
            "(AR) (BD) (CO) (EJ) (FN) (GT) (HK) (IV) (LM) (PW)\n"
           + "                    (QZ) (SX) (UY)", UPPER);;
    private Reflector reflectorC = new Reflector(
            "C", permC);

    private Permutation permNAVI = new Permutation(
            "(YF) (ZH)", UPPER);

    private Permutation permSAMPLETEST = new Permutation(
            "(HQ) (EX) (IP) (TR) (BY)", UPPER);;

    /**MYROTORS.*/
    private ArrayList<Rotor> myrotors = new ArrayList<>() { };
   /**my SETTINGSS.*/
    public  ArrayList<Character> settingss = new ArrayList<>();
    /**my _MACHINE1*/
    private Machine _machine1;
}
