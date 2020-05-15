package gitlet;

import java.io.IOException;
import java.io.Serializable;
/** class for Gitlet.
 *
 * @author Ziyuan Tang
 */
public class Blob implements Serializable {
    /**constructor with NAME.*/
    Blob(String name) {
        _name = name;
        _content = Utils.readContents(Utils.join(Main.CWD, name));
        _sha1 = Utils.sha1(_name, _content);
    }

    /** Save file content as a file which name is sha1 code. */
    public void saveContent() throws IOException {
        Utils.join(Main.THINGS, _sha1).createNewFile();
        Utils.writeContents(Utils.join(Main.THINGS, _sha1), _content);
    }


    /**get method for the blob, read as string.
     * @param sha1 the blob's sha1 used to identify the blob.
      * @return the blob in string format.*/
    public static String getblobFromfile(String sha1) {
        if (Utils.join(Main.THINGS, sha1).exists()) {
            return Utils.readContentsAsString(Utils.join(Main.THINGS, sha1));
        } else {
            throw new IllegalArgumentException("no such commit file exist");
        }
    }
    /**get method for the blob, read as string.
     * @param sha1 the blob's sha1 used to identify the blob.
     * @return the blob in string format.*/
    public static String getblobFromfileAsString(String sha1) {
        if (Utils.join(Main.THINGS, sha1).exists()) {
            return Utils.readContentsAsString(Utils.join(Main.THINGS, sha1));
        } else {
            throw new IllegalArgumentException("no such commit file exist");
        }
    }


/**get method for _sha1.
 * @return the sha1 code of this bolb*/
    public String getSha1() {
        return _sha1;
    }
    /**get method for blob's name.
     * @return the name of this blob.*/
    public String getName() {
        return _name;
    }
    /**name.*/
    private String _name;
    /**name.*/
    private  static byte[] _content;
    /**name.*/
    private static String _sha1;
}
