package gitlet;
import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/** class for Gitlet.
 *
 * @author Ziyuan Tang
 */

public class Commit implements Serializable {
    /**cTree message CTREE MESSAGE.
     * @param allBlobs blobs this commit traked.
     * @param ctree the ctree commit in.
     * @param message commit's message.*/
    public Commit(HashMap<String, String> allBlobs,
                  String message, CommitTree ctree) {
        _message = message;
        _ctree = ctree;
        ZonedDateTime dateTime = ZonedDateTime.now();
        DateTimeFormatter formatter
                = DateTimeFormatter.ofPattern("EEE LLL d HH:mm:ss y Z");
        _ZDT = dateTime.format(formatter);

        _stagedBlobs = allBlobs;
        _allpastBlobs = allBlobs;
        _commitSHA1 = Utils.sha1(_message, _ZDT);
        _ismerged = false;
        _mergeFrom = "";
    }
    /**useless.
     * @param currCommit s
     * @param pastCommit s
     * @param stageadded  s*/
    public static void updateTracked(
            Commit pastCommit, Commit currCommit,
            HashMap<String, String> stageadded) {

    }
    /**check if this commit has file.
     * @return boolean*/
    public boolean hasFile() {
        return _allpastBlobs != null && !_allpastBlobs.isEmpty();
    }

    /**set the commit status merged.*/
    public void setTobeMerged() {
        _ismerged = true;
    }

    /**set the commit status merged.
     * @param branchname  the branch to add.*/
    public void beingMergedFrom(String branchname) {
        _mergeFrom = branchname;
    }
    /**set the commit status merged.
     * @return the branch of merging.*/
    public String beingMergedFrom() {
        return _mergeFrom;
    }

    /**set the commit status merged.
     * @return the branch of merging.*/
    public Commit getbeingMergedFrom() {
        return _mergeFromCommit;
    }

    /**set the commit status merged.
     * @param commit  the branch to add.*/
    public void beingMergedFrom(Commit commit) {
        _mergeFromCommit = commit;
    }



    /**return true only is the curerent commit has been merged.
     * @return true is merged*/
    public boolean isMerged() {
        return _ismerged;
    }
    /**get method for the commit.
     * @param id sha1 code for this commit.
     * @return Commit.*/
    public static Commit getCommitFromfile(String id) {
        if (Utils.join(Main.THINGS, id).exists()) {
            return Utils.readObject(Utils.join(Main.THINGS, id), Commit.class);
        } else {
            throw new IllegalArgumentException("no such commit file exist");
        }
    }
    /**save method for the commit.*/
    public void saveCommit() throws IOException {
        File newFile = Utils.join(Main.THINGS, getSha1());
        newFile.createNewFile();
        Utils.writeObject(newFile, this);
    }
    /**get method for the blobs.
     * @return HashMap.*/
    public HashMap<String, String> getstagedBlobs() {
        return _stagedBlobs;
    }
    /**get method for the blobs.
     * @return HashMap.*/
    public HashMap<String, String> getallBlobs() {
        return _allpastBlobs;
    }
    /**set the commit's parent to another commit.
     * @param parent parent to be set to current commit.*/
    public void setParent(Commit parent) throws IOException {
        this._parent = parent;
    }
    /**get method for the time, date.
     * @return String.*/
    public String getZdt() {
        return _ZDT;
    }
    /**get method for the message.
     * @return String.*/
    public String getMessage() {
        return _message;
    }
    /**get method for the parent.
     * @return Commit.*/
    public Commit getParent() {
        return _parent;
    }
    /**@return cTree message .*/
    public String getSha1() {
        return _commitSHA1;
    }
    /**@return cTree message.*/
    public CommitTree getCTree() {
        return _ctree;
    }
    /**merge from whom.*/
    private String _mergeFrom;
    /**merge from whom.*/
    private Commit _mergeFromCommit;
    /**is merged ot not.*/
    private boolean _ismerged;
    /**commit's parent.*/
    private Commit _parent;
    /**commit's tree.*/
    private CommitTree _ctree;
    /**commit's _stagedBlobs.*/
    private HashMap<String, String> _stagedBlobs;
    /**commit's all past Blobs.*/
    private HashMap<String, String> _allpastBlobs;
    /**@ cTree message.*/
    private String _message;
    /**cTree message.*/
    private String _ZDT;
    /**cTree message.*/
    private String _commitSHA1;



}
