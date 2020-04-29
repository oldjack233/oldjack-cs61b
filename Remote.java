package gitlet;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;

/** The remote class for which the current
 * working directory can do remote comand.
 * @author Huixuan Lin
 */
public class Remote implements Serializable {

    /** The current working directory for remote. */
    private File cwd;
    /** GitLet repo directory for remote. */
    private File repo;
    /** GitLet object directory for remote. */
    private File object;
    /** GitLet branch information. */
    private File branch;
    /** GitLet current branch information. */
    private File currentBranch;
    /** GitLet staging area directory. */
    private File stage;
    /** GitLet commit history file. */
    private File history;

    /** Constructor of remote with NAME and DIRECTORY. */
    public Remote(String name, String directory) throws IOException {
        _name = name;
        repo = new File(directory);
        cwd = Utils.join(repo, "..");
        object = Utils.join(repo, "object");
        branch = Utils.join(repo, "branch");
        stage = Utils.join(repo, "stage");
        history = Utils.join(repo, "history");
        currentBranch = Utils.join(repo, "current-branch");
        saveRemote();
    }

    /** Get the name of remote.
     * @return name of remote. */
    public String getName() {
        return _name;
    }

    /** Return the gitlet repo directory for remote.
     * @return current repo directory. */
    public File getRepo() {
        return repo;
    }


    public File getStage() {
        return stage;
    }

    public File getObject() {
        return object;
    }

    public File getHistory() {
        return history;
    }
    /** Return the branch.
     * @return current branch of remote. */
    public File getBranch() {
        return branch;
    }

    /**
     * Reads in and deserializes a blob from a file with name NAME in OBJECT.
     * @param  sha1 of blob to load
     * @return Blob read from file
     */
    public String blobFromFile(String sha1) {
        File blobFile = Utils.join(object, sha1);
        if (!blobFile.exists()) {
            throw new IllegalArgumentException(
                    "No blob file with that name found.");
        }
        return Utils.readContentsAsString(blobFile);
    }

    /** Reads in and deserializes a tree from a file with SHA1 code.
     * @param  sha1 of tree to load
     * @return Tree read from file */
    public Commit.CommitTree treeFromFile(String sha1) {
        File treeFile = Utils.join(object, sha1);
        if (!treeFile.exists()) {
            throw new IllegalArgumentException(
                    "No tree file with that name found.");
        }
        return Utils.readObject(treeFile, Commit.CommitTree.class);
    }

    /** Reads in and deserializes a commit from a file with SHA1 code.
     * @param  sha1 of commit to load
     * @return Commit read from file */
    public Commit commitFromFile(String sha1) {
        File commitFile = Utils.join(object, sha1);
        if (!commitFile.exists()) {
            throw new IllegalArgumentException(
                    "No commit file with that name found.");
        }
        return Utils.readObject(commitFile, Commit.class);
    }

    /** Reads in and deserializes a branch.
     * @param  name of branch to load
     * @return Branch read from file */
    public Branch branchFromFile(String name) {
        File branchFile = Utils.join(branch, name);
        if (!branchFile.exists()) {
            throw new IllegalArgumentException(
                    "No branch file with that name found.");
        }
        return Utils.readObject(branchFile, Branch.class);
    }

    /** Reads in and deserializes current history
     * @return Stage read from file */
    public HashMap<String, Commit> historyFromFile() {
        File stageFile = Utils.join(history, "current");
        if (!stageFile.exists()) {
            throw new IllegalArgumentException(
                    "No history file.");
        }
        return Utils.readObject(stageFile, HashMap.class);
    }

    /** Reads in and deserializes a history.
     * @return Stage read from file */
    public Commit.StageArea stageFromFile(String name) {
        File stageFile = Utils.join(stage, name);
        if (!stageFile.exists()) {
            throw new IllegalArgumentException(
                    "No stage file with that branch found.");
        }
        return Utils.readObject(stageFile, Commit.StageArea.class);
    }

    /**
     * Checks out all the files tracked by the given FILEMAP.
     * Removes tracked files that
     * are not present in that commit.
     */
    public void reset(HashMap<String, String> fileMap) throws IOException {
        if (cwd.list() != null) {
            for (File file : cwd.listFiles()) {
                if (!file.isHidden()
                        && !file.getName().equals("Makefile")
                        && !file.getName().endsWith(".iml")) {
                    file.delete();
                }
            }
        }
        for (String name : fileMap.keySet()) {
            Utils.join(cwd, name).createNewFile();
            Utils.writeContents(Utils.join(cwd,
                    name), fileMap.get(name));
        }
    }

    /** Save a remote to a file as future use. */
    public void saveRemote() throws IOException {
        Utils.join(Main.REMOTE, _name).createNewFile();
        Utils.writeObject(Utils.join(Main.REMOTE, _name), this);
    }

    /** Reads in and deserializes a remote from NAME.
     * @return The remote from file. */
    public static Remote fromFile(String name) {
        if (!Utils.join(Main.REMOTE, name).exists()) {
            throw new IllegalArgumentException(
                    "No remote file with that name found.");
        }
        return Utils.readObject(Utils.join(Main.REMOTE, name), Remote.class);
    }

    /** Name of the remote. */
    private String _name;

}