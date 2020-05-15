package gitlet;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ArrayList;

import static gitlet.Main.CWD;
/** class for Gitlet.
 *
 * @author Ziyuan Tang

/**Constructor.*/
public class StageArea implements Serializable {
    /**store the added file.*/
    private HashMap<String, String> stagedAdded = new HashMap<>();
    /**store the removed file.*/
    private HashMap<String, String> stagedRemoved = new HashMap<>();
    /**store the removed file.*/
    private HashMap<String, String> removal = new HashMap<>();
    /**store the removed file.*/
    private HashMap<String, String> removalhistory = new HashMap<>();
    /**store the trackedDeleted file.*/
    private HashMap<String, String> trackedDeleted = new HashMap<>();
    /**store the trackedModified file.*/
    private HashMap<String, String> trackedModified = new HashMap<>();
    /**store the untracked file.*/
    private HashMap<String, String> untracked = new HashMap<>();
    /**store the tracked file.*/
    private  HashMap<String, String> tracked = new HashMap<>();
    /**store the current tracked file.*/
    private  HashMap<String, String> currentTracked = new HashMap<>();
    /**CommitTree.
     * @param branch branch for the stagearea to work on.*/
    StageArea(String branch) {
        _branch = branch.replace("/", "-");
    }

    /**helper method for command status.
     * @param cbranch dummy operands.
     * @return a string[] that contains for
     * different status information we need.*/
    public String[] status(Branch cbranch) {
        updateUntracked();
        statusHelper();
        statusHelper2();
        statusHelper3();
        String[] toReturn = new String[4];
        Arrays.fill(toReturn, "");
        System.out.println("\n=== Staged Files ===");
        if (getStagedAdded() != null && !getStagedAdded().isEmpty()) {
            for (String name : getStagedAdded().values()) {
                toReturn[0] += name + "\n";
            }
        }
        System.out.println(toReturn[0]);
        System.out.println("=== Removed Files ===");
        if (getRemoval() != null && !getRemoval().isEmpty()) {
            for (String name : getRemoval().values()) {
                toReturn[1] += name + "\n";
            }
        }
        System.out.println(toReturn[1]);
        System.out.println("=== Modifications Not Staged For Commit ===");
        for (String key : trackedDeleted.keySet()) {
            toReturn[2] += key + " (deleted)\n";
        }
        for (String key : trackedModified.keySet()) {
            toReturn[2] += key + " (modified)\n";
        }
        System.out.println(toReturn[2]);
        System.out.println("=== Untracked Files ===");
        for (String key : untracked.keySet()) {
            toReturn[3] += key + "\n";
        }
        System.out.println(toReturn[3]);
        trackedDeleted.clear();
        trackedModified.clear();
        return toReturn;
    }

    /**Helper function for the status command.
     * responsible for trackedmodified.*/
    public void statusHelper() {
        for (String filenameInCWD : Main.CWD.list()) {
            if (!filenameInCWD.equals("proj3.iml")
                    && !filenameInCWD.equals("gitlet")
                    && !filenameInCWD.equals("Makefile")
                    && !filenameInCWD.equals("testing")
                    && !filenameInCWD.equals("out")
                    && !filenameInCWD.startsWith(".")
                    && !filenameInCWD.endsWith(".iml")) {
                for (String filenameInTracked: tracked.values()) {
                    if (filenameInCWD.equals(filenameInTracked)) {
                        Blob cur = new Blob(filenameInCWD);
                        String sha1wanted = "";
                        for (String sha1 : tracked.keySet()) {
                            if (tracked.get(sha1).equals(filenameInCWD)) {
                                sha1wanted = sha1;
                                break;
                            }
                        }
                        String cursha1 = cur.getSha1();
                        if (!cur.getSha1().equals(sha1wanted)) {
                            trackedModified.put(filenameInCWD, null);
                        }
                    }
                }
            }
        }
        for (String filenameInCWD : Main.CWD.list()) {
            if (!filenameInCWD.equals("proj3.iml")
                    && !filenameInCWD.equals("gitlet")
                    && !filenameInCWD.equals("Makefile")
                    && !filenameInCWD.equals("testing")
                    && !filenameInCWD.equals("out")
                    && !filenameInCWD.startsWith(".")
                    && !filenameInCWD.endsWith(".iml")) {
                Blob cur = new Blob(filenameInCWD);
                for (String addFileName: stagedAdded.values()) {
                    if (filenameInCWD.equals(addFileName)) {
                        for (String sha1 : stagedAdded.keySet()) {
                            if (stagedAdded.get(sha1).equals(addFileName)) {
                                if (!sha1.equals(cur.getSha1())) {
                                    trackedModified.put(addFileName, null);
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    /**Helper function for the status command.
     * responsible for trackdeleted.*/
    public void statusHelper2() {
        long validfile = 0;
        if (Main.CWD.list() != null) {
            for (String filename : Main.CWD.list()) {
                if (!filename.equals("proj3.iml")
                        && !filename.equals("gitlet")
                        && !filename.equals("Makefile")
                        && !filename.equals("testing")
                        && !filename.equals("out")
                        && !filename.startsWith(".")
                        && !filename.endsWith(".iml")) {
                    validfile++;
                }
            }
            for (String addFileName : stagedAdded.values()) {
                long i = 0;
                for (String filenameInCWD : Main.CWD.list()) {
                    if (!filenameInCWD.equals("proj3.iml")
                            && !filenameInCWD.equals("gitlet")
                            && !filenameInCWD.equals("Makefile")
                            && !filenameInCWD.equals("testing")
                            && !filenameInCWD.equals("out")
                            && !filenameInCWD.startsWith(".")
                            && !filenameInCWD.endsWith(".iml")) {
                        if (filenameInCWD.equals(addFileName)) {
                            i = 0;
                            break;
                        } else if (i == validfile) {
                            trackedDeleted.put(addFileName, null);
                            i = 0;
                        } else {
                            i++;
                        }
                    }
                }
            }
        }
    }

    /**Helper function for the status command.
     * responsible for trackdeleted.*/
    public void statusHelper3() {
        long validfile = 0;
        if (Main.CWD.list() != null) {
            for (String filename : Main.CWD.list()) {
                if (!filename.equals("proj3.iml")
                        && !filename.equals("gitlet")
                        && !filename.equals("Makefile")
                        && !filename.equals("testing")
                        && !filename.equals("out")
                        && !filename.startsWith(".")
                        && !filename.endsWith(".iml")) {
                    validfile++;
                }
            }
            boolean find = false;
            for (String trakedfileName : tracked.values()) {
                long i = 0;
                for (String filenameInCWD : Main.CWD.list()) {
                    if (!filenameInCWD.equals("proj3.iml")
                            && !filenameInCWD.equals("gitlet")
                            && !filenameInCWD.equals("Makefile")
                            && !filenameInCWD.equals("testing")
                            && !filenameInCWD.equals("out")
                            && !filenameInCWD.startsWith(".")
                            && !filenameInCWD.endsWith(".iml")) {
                        find = true;
                        if (filenameInCWD.equals(trakedfileName)) {
                            i = 0;
                            break;
                        } else if (i == validfile) {
                            if (!removal.containsValue(trakedfileName)) {
                                trackedDeleted.put(trakedfileName, null);
                                i = 0;
                            }
                        } else {
                            i++;
                        }
                    }
                }
                if (!find) {
                    for (String trakedfileName2 : tracked.values()) {
                        trackedDeleted.put(trakedfileName2, null);
                    }
                }
            }
        }
    }


    /**when do command status, update all untrakced file.*/
    public void updateUntracked() {
        if (Main.CWD.list() != null) {
            for (String filename : Main.CWD.list()) {
                if (!filename.equals("proj3.iml")
                        && !filename.equals("gitlet")
                        && !filename.equals("Makefile")
                        && !filename.equals("testing")
                        && !filename.equals("out")
                        && !filename.startsWith(".")
                        && !filename.endsWith(".iml")) {
                    if (!stagedAdded.containsValue(filename)
                            && !tracked.containsValue(filename)) {
                        untracked.put(filename, null);
                    }

                    if (stagedAdded.containsValue(filename)
                            || tracked.containsValue(filename)) {
                        untracked.remove(filename);
                    }
                }
            }
            HashMap<String, String> handDelete = new HashMap<>();
            for (String untrackedFile : untracked.keySet()) {
                if (Main.CWD.list() != null) {
                    boolean find = false;
                    for (String filename : Main.CWD.list()) {
                        if (filename.equals(untrackedFile)) {
                            find = true;
                            break;
                        }
                    }
                    if (!find) {
                        handDelete.put(untrackedFile, null);
                    }
                }
            }
            for (String handelte : handDelete.keySet()) {
                untracked.remove(handelte);
            }
        }
    }

    /**get what is removed.
     * @return a hashmap of removed files.*/
    public HashMap<String, String> getStageRemoved() {
        return stagedRemoved;
    }
    /**get what is removal.
     * @return a hashmap of removal files.*/
    public HashMap<String, String> getRemoval() {
        return removal;
    }
    /**get what is removal history.
     * @return a hashmap of removal files.*/
    public HashMap<String, String> getRemovalhistory() {
        return removalhistory;
    }
    /**get what is being added.
     * @return a hashmap of added files.*/
    public HashMap<String, String> getStagedAdded() {
        return stagedAdded;
    }
    /**get what is tracked deleted files.
     * @return a hashmap of deleted files.*/
    public HashMap<String, String> getTrackedDeleted() {
        return trackedDeleted;
    }
    /**get what is tracked modified files.
     * @return a hashmap of modified files.*/
    public HashMap<String, String> getTrackedModified() {
        return trackedModified;
    }
    /**process the add command.
     * @param args String[] of commands.
     * @param branch dummy.
     * @param ctree historical information we need*/
    public void add(String args, Branch branch,
                    CommitTree ctree) throws IOException {
        boolean markRemoveBefore = false;
        String sha1wanted = "";
        if (removal.containsValue(args)) {
            for (String sha1 : removal.keySet()) {
                if (removal.get(sha1).equals(args)) {
                    sha1wanted = sha1;
                }
            }
            removal.remove(sha1wanted);
            markRemoveBefore = true;
        }
        HashMap<String, String> lastCommitBlob = ctree.getOrderedHistory().
                getLast().getallBlobs();
        if (!markRemoveBefore) {
            Blob blob = new Blob(args);
            if (lastCommitBlob != null && lastCommitBlob.size() != 0
                    && lastCommitBlob.containsKey(blob.getSha1())) {
                if (stagedAdded.containsKey(blob.getSha1())) {
                    stagedAdded.remove(blob.getSha1());
                } else {
                    return;
                }
            } else {
                blob.saveContent();
                stagedAdded.put(blob.getSha1(), blob.getName());
                currentTracked.put(blob.getSha1(), blob.getName());
                updateUntracked();
            }
        }
    }

    /**process the rm command.
     * @param args String[] of commands.
     * @param branch dummy.
     * @param ctree historical information we need*/
    public void rm(String[] args, CommitTree ctree, Branch branch) {
        boolean failcase = true;
        boolean deleted = false;
        if (stagedAdded.containsValue(args[1])) {
            Blob repeatBlob = new Blob(args[1]);
            stagedAdded.remove(repeatBlob.getSha1());
            failcase = false;

        }
        if (ctree.getOrderedHistory().getLast().getallBlobs() != null
                && ctree.getOrderedHistory().getLast().
                getallBlobs().containsValue(args[1])) {
            HashMap<String, String> pastBlobs = ctree.
                    getOrderedHistory().getLast().
                    getallBlobs();
            String sha1wanted = "";
            for (String sha1 : pastBlobs.keySet()) {
                if (pastBlobs.get(sha1).equals(args[1])) {
                    sha1wanted = sha1;
                    break;
                }
            }
            removal.put(sha1wanted, args[1]);
            removalhistory.put(sha1wanted, args[1]);
            Utils.restrictedDelete(Utils.join(CWD, args[1]));
            failcase = false;


        }


        updateUntracked();

        if (failcase) {
            throw new IllegalArgumentException("No reason to remove the file");
        }
    }
    /**process the commit command.
     * @param args String[] of commands.
     * @param branch dummy.
     * @param ctree historical information we need
     * @return return the new commit.*/
    public Commit commit(String[] args, CommitTree ctree,
                          Branch branch) throws IOException {
        if ((stagedAdded.isEmpty() && removal.isEmpty()) || ctree == null) {
            throw new IllegalArgumentException("No"
                    + " changes added to the commit.");
        }
        tracked.putAll(stagedAdded);
        Commit newCommit = new Commit(tracked, args[1], ctree);
        newCommit.updateTracked(branch.getHead(), newCommit, stagedAdded);
        updateUntracked();
        stagedAdded.clear();
        currentTracked.clear();
        removal.clear();
        newCommit.saveCommit();
        return newCommit;
    }

    /**process the log command.
     * @param args String[] of commands.
     * @param branch dummy.
     * @param ctree historical information we need*/
    public void log(String[] args, CommitTree ctree, Branch branch) {
        String[] names = Main.THINGS.list();
        HashMap<String, String> test = new HashMap<>();
        String a = test.get("1");
        String headSha1 = branch.getHead().getSha1();
        Commit commit = ctree.getHistory().get(headSha1);
        while (commit != null) {
            if (commit.isMerged()) {
                CommitTree ctre2e = commit.getCTree();
                System.out.println("===\ncommit " + commit.getSha1());
                System.out.println("Merge: " + commit.getSha1().substring(0, 7)
                        + " " + commit.getbeingMergedFrom().getSha1().
                        substring(0, 7));
                System.out.println("Date: "
                                + commit.getZdt());
                System.out.println("Merged " + commit.beingMergedFrom()
                        + " into " + commit.getCTree().getBranch().
                        getName() + ".");
                System.out.println();
            }

            System.out.println("===\ncommit " + commit.getSha1());
            System.out.println("Date: "
                    + commit.getZdt()
                    + "\n" + commit.getMessage());

            System.out.println();
            commit = commit.getParent();
        }
    }
    /**helper arraylist for the global log, looks stupid.*/
    private ArrayList<Commit> checkCommit = new ArrayList<>();

    /**process the global log command.
     * @param args String[] of commands.
     * @param branch dummy.
     * @param ctree historical information we need*/
    public void globalLog(String[] args, CommitTree ctree, Branch branch) {
        checkCommit.clear();
        String[] names = Main.HISTORY.list();
        for (String branchName : names) {
            File cTreeFile = Utils.join(Main.HISTORY, branchName);
            CommitTree oneCTree = Utils.readObject(cTreeFile,
                    gitlet.CommitTree.class);
            String headSha1 = oneCTree.getBranch().getHead().getSha1();
            Commit commit = ctree.getHistory().get(headSha1);
            while (commit != null) {
                if (!checkCommit.contains(commit)) {
                    if (commit.isMerged()) {
                        CommitTree ctre2e = commit.getCTree();
                        System.out.println("===\ncommit " + commit.getSha1());
                        System.out.println("Merge " + commit.getSha1().
                                substring(0, 7)
                                + commit.getbeingMergedFrom().getSha1().
                                substring(0, 7));
                        System.out.println("Date: "
                                + commit.getZdt());
                        System.out.println("Merge: " + commit.beingMergedFrom()
                                + " into " + commit.getCTree().
                                getBranch().getName());
                    }
                    System.out.println("===\ncommit " + commit.getSha1());
                    System.out.println("Date: "
                            + commit.getZdt()
                            + "\n" + commit.getMessage());

                    System.out.println();
                    checkCommit.add(commit);
                }
                commit = commit.getParent();
            }
        }
    }
    /**helper arraylist for the global log, looks stupid.*/
    private ArrayList<String> checksha1 = new ArrayList<>();

    /**process the find command.
     * @param args String[] of commands.
     * @param branch dummy.
     * @param ctree historical information we need*/
    public void find(String[] args, CommitTree ctree, Branch branch) {
        checksha1.clear();
        String[] names = Main.HISTORY.list();
        boolean find = false;
        if (names == null) {
            throw new IllegalArgumentException("Found "
                    + "no commit with that message.");
        } else {
            for (String branchName : names) {
                CommitTree oneCTree = CommitTree.getCTree(branchName);
                String headSha1 = oneCTree.getBranch().getHead().getSha1();
                Commit commit = oneCTree.getHistory().get(headSha1);
                while (commit != null) {
                    if (commit.getMessage().contains(args[1])
                            && !checksha1.contains(commit.getSha1())) {
                        System.out.println(commit.getSha1());
                        find = true;
                        checksha1.add(commit.getSha1());
                        commit = commit.getParent();
                    } else {
                        commit = commit.getParent();
                    }
                }
            }
        }
        if (!find) {
            throw new IllegalArgumentException("Found "
                    + "no commit with that message.");
        }
    }


    /**clear what we have in stageArea.*/
    public void clear() {
        stagedAdded.clear();
        stagedRemoved.clear();

    }
    /** Return true if there is a tree in staging area. */
    public HashMap<String, String> getTracked() {
        return tracked;
    }
    /** Return true if there is a tree in staging area. */
    public HashMap<String, String> getUntracked() {
        return untracked;
    }
    /** CommitTree.*/
    public void saveStage() throws IOException {
        saveStage(_branch);
    }
    /**@ CommitTree BRANCH.*/
    public void saveStage(String branch) throws IOException {
        Utils.join(Main.STAGE, branch).createNewFile();
        _branch = branch;
        Utils.writeObject(Utils.join(Main.STAGE, branch), this);
    }
    /**@return CommitTree.*/
    public static gitlet.StageArea getFile() {
        if (!Main.CBRANCH.exists()) {
            throw new IllegalArgumentException(
                    "No current branch file found.");
        }
        String currentBranch = Utils.readContentsAsString(Main.CBRANCH);
        return getFile(currentBranch);
    }
    /**@return CommitTree BRANCH.*/
    public static gitlet.StageArea getFile(String branch) {
        if (!Utils.join(Main.STAGE, branch.replace("/", "-")).exists()) {
            throw new IllegalArgumentException(
                    "No stage of branch with this name found.");
        }
        return Utils.readObject(Utils.join(Main.STAGE,
                branch.replace("/", "-")), gitlet.StageArea.class);
    }
    /**branch we have in the stagingarea.*/
    private String _branch;
}
