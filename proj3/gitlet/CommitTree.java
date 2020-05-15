package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.SortedSet;
/** class for Gitlet.
 *
 * @author Ziyuan Tang


/**commitTree.*/
public  class CommitTree implements Serializable {
    /**
     * CommitTree's constructor.
     * @param branch branch we on.
     * @param commits commits we have.
     */
    public CommitTree(Branch branch, HashMap<String, Commit> commits) {
        _branch = branch;

        _history = commits;
        _treeSha1 = Utils.sha1(_branch.toString(), _history.toString());
    }

    /**
     * update the tracked file we have on this branch.
     * @param fileName things we are going to update.
     */
    public void updateTracked(String fileName) {
        Commit last = orderedHistory.getLast();
        if (!last.getMessage().equals("initial commit")) {
            for (String blobSha1 : last.getstagedBlobs().keySet()) {
                if (_alltracked.get(blobSha1) == null) {
                    _alltracked.put(blobSha1, last.
                            getstagedBlobs().get(blobSha1));
                }
            }
        } else {
            Blob blob = new Blob(fileName);
            _alltracked.put(blob.getSha1(), blob.getName());
        }
    }

    /**
     * add commit to history.
     * @param commit things we are going to add to history.
     */
    public void addCommit(Commit commit) throws IOException {
        orderedHistory.add(commit);
        _history.put(commit.getSha1(), commit);

    }
    /**
     * set this commit's parent to another commit.
     * @param commit things we are going to have a parent.
     */
    public void setParent(Commit commit) throws IOException {
        Commit parent;
        if (orderedHistory.size() == 1) {
            parent = orderedHistory.get(0);
        } else {
            parent = orderedHistory.get(orderedHistory.size() - 2);
        }
        commit.setParent(parent);
        commit.saveCommit();
    }


    /**@return _treeSha1.*/
    public String getSHA1() {
        return _treeSha1;
    }
    /**@return _history.*/
    public HashMap<String, Commit> getHistory() {
        return _history;
    }
    /** @return orderedHistory.*/
    public LinkedList<Commit> getOrderedHistory() {
        return orderedHistory;
    }
    /** @return orderedHistory.*/
    public Branch getBranch() {
        return _branch;
    }
    /**@return _history.*/
    public Commit getIntial() {
        return orderedHistory.getFirst();
    }




    /** set ctree to another branch.
     * @param branch set the branch to branch.*/
    public void setBranch(Branch branch) {
        _branch = branch;
        orderedHistory.clear();

    }
    /**save this CommitTree.
     * @param branch we on.*/
    public void saveCTree(String branch) throws IOException {
        Utils.join(Main.HISTORY, branch).createNewFile();
        Utils.writeObject(Utils.join(Main.HISTORY, branch), this);
    }

    /**
     * @param branch get ctree on this branch
     * @return CommitTree
     */
    public static gitlet.CommitTree getCTree(String branch) {
        File cTreeFile = Utils.join(Main.HISTORY, branch);
        if (cTreeFile.exists()) {
            return Utils.readObject(cTreeFile,
                    gitlet.CommitTree.class);
        } else {
            throw new IndexOutOfBoundsException(
                    "No commitTree file found.");
        }
    }

    /**
     * @param branch get ctree on this branch
     * @return CommitTree
     */
    public static boolean checkCTreeExist(String branch) {
        File cTreeFile = Utils.join(Main.HISTORY, branch);
        return cTreeFile.exists();
    }
    /**ordered history of commits.*/
    private  LinkedList<Commit> orderedHistory = new LinkedList<>();
    /**_lastCommit .*/
    private Commit _lastCommit;
    /**current _branch.*/
    private  Branch _branch;
    /** _history.*/
    private HashMap<String, Commit> _history;
    /**trakced files.*/
    private  HashMap<String, String> _alltracked = new HashMap<>();
    /**
     * Collection of staged files. Key is the file sha1,
     * value is the file's name.
     */
    private HashMap<String, String> _staged;
    /**
     * Sorted set of staged files' name.
     */
    private SortedSet<String> _stagedName;
    /**
     * Collection of files going to be removed.
     */
    private SortedSet<String> _removal;
    /**
     * SHA1 code for this tree.
     */
    private String _treeSha1;
}


