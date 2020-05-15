package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
/** class for Gitlet.
 *
 * @author Ziyuan Tang
 */
public class Branch implements Serializable {
    /**lol.*/
    private  String _name;
    /**lol.*/
    private  ArrayList<Commit> _commits = new ArrayList<>();
    /**sha1 code for the branch.*/
    private String _branchSha1;
    /**constructor NAME.*/
    Branch(String name) {
        _name = name;
        _branchSha1 = Utils.sha1(_name);

    }
    /**constructor using HEAD and NAME.*/
    Branch(String name, String head) {
        _name = name;
        _head = head;
        _branchSha1 = Utils.sha1(_name);

    }
    /**COMMIT.
     * @param commits the commits to be added into the carrier.*/
    public void addCommit(ArrayList<Commit> commits) throws IOException {
        _commits.addAll(commits);
        saveBranch();
    }

    /**COMMIT.*/
    public void addCommit(Commit commit) throws IOException {
        _commits.add(commit);
        setHead(commit.getSha1());
        saveBranch();
    }
    /**set the head of the branch to the one with sha1 code SHA1.*/
    public void setHead(String sha1) {
        _head = sha1;
    }



    /**get the head commit on this branch.
     * @return Head Commit we need/*/
    public Commit getHead() {
        if (_commits.size() > 0) {
            for (Commit commit : _commits) {
                if (commit.getSha1().equals(_head)) {
                    return commit;
                }
            }
        } else {
            throw new IllegalArgumentException("no commit in branch");
        }
        return _commits.get(_commits.size());
    }
    /**get the branch sha1 code.
     * @return sah1 we need for this branch*/
    public String getBranchSha1() {
        return _branchSha1;
    }

    /**get the branch commits .
     * @return sah1 we need for this branch*/
    public ArrayList<Commit> getCommits() {
        return _commits;
    }

    /**save this branch to branch file.*/
    public void saveBranch() throws IOException {
        Utils.join(Main.BRANCH, _name).createNewFile();
        Utils.writeObject(Utils.join(Main.BRANCH, _name), this);
    }
    /**get method for this branch.
     * @return BRANCH.*/
    public static Branch getBranch(String branch) {
        File newFile = Utils.join(Main.BRANCH, branch);
        if (newFile.exists()) {
            return Utils.readObject(newFile, Branch.class);
        } else {
            throw new IndexOutOfBoundsException("no such File/Blob1");
        }
    }
    /**get method for this branch.
     * @return  BRANCH.*/
    public static Branch getBranch() {

        if (Main.CBRANCH.exists()) {
            String cbch = Utils.readContentsAsString(Main.CBRANCH);
            File newFile = Paths.get(Main.BRANCH.getPath(), cbch).toFile();
            if (newFile.exists()) {
                return Utils.readObject(newFile, Branch.class);
            } else {
                throw new IndexOutOfBoundsException("no such File/Blob2");
            }
        } else {
            throw new IndexOutOfBoundsException("no such File/Blob3");
        }
    }
    /**@return name BRANCH.*/
    public String getName() {
        return _name;
    }

    /**BRANCH.*/
    private String _head = "";
}
