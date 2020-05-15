package gitlet;


import java.io.File;
import java.io.IOException;
import java.util.Set;
import java.util.Stack;
import java.util.Arrays;
import java.util.HashMap;


/** Main class for Gitlet.
 *
 * @author Ziyuan Tang
 */
public class Main {

    /** Current Working Directory. */
    static final File CWD = new File(".");
    /** GitLet repo . */
    static final File REPO = Utils.join(CWD, ".gitlet");
    /** GitLet object . */
    static final File THINGS = Utils.join(REPO, "object");
    /** GitLet branch . */
    static final File BRANCH = Utils.join(REPO, "branch");
    /** GitLet current branch . */
    static final File CBRANCH = Utils.join(REPO, "cbranch");
    /** GitLet staging area . */
    static final File STAGE = Utils.join(REPO, "stage");
    /** GitLet commit history . */
    static final File HISTORY = Utils.join(REPO, "history");



    /** ARGS are
     *  <COMMAND> <OPERAND> .... */
    public static void main(String... args) throws IOException {
        try {
            checkLength(args);
            initOrOther(args);

            _stagingArea.saveStage();
            _treeAndhistory.saveCTree(_branch.getName());
            saveCurrentBranch();
        } catch (IllegalArgumentException | IndexOutOfBoundsException error) {
            if (error.getMessage() != null) {
                System.out.println(error.getMessage());
            }
        }
    }

    /**process the command according to param.
     * @param args a string[] of commands*/
    public static void process(String... args) throws IOException {
        checkArgs(args[0], args);
        switch (args[0]) {
        case "feel good?":
            feelgreat();
        case "add":
            add(args);
            break;
        case "rm":
            rm(args);
            break;
        case "commit":
            commit(args);
            break;
        case "log":
            log(args);
            break;
        case "global-log":
            globalLog(args);
            break;
        case "find":
            find(args);
            break;
        case "reset":
            reset(args);
            break;
        case "status":
            status(args);
            break;
        case "branch":
            branch(args);
            break;
        case "rm-branch":
            rmbranch(args);
            break;
        case "merge":
            merge(args);
            break;
        case "checkout":
            checkout(args);
            break;
        default:
            throw new IllegalArgumentException("No "
                    + "command with that name exists.");
        }
    }

    /**if command is init, start the system; else, read all info we need.
     *.*/
    public static void feelgreat() throws IOException {
        System.out.println("feel great now !");

    }

    /**if command is init, start the system; else, read all info we need.
     * @param args the command and a file name .*/
    public static void initOrOther(String[] args) throws IOException {
        checkArgs(args[0], args);
        if (args[0].equals("init") && args.length == 1) {
            init(args);
        } else {
            if (!REPO.exists()) {
                throw new IllegalArgumentException("Not "
                        + "in an initialized Gitlet directory.");
            } else {
                _branch = Branch.getBranch();
                _stagingArea = StageArea.getFile(_branch.getName());
                _treeAndhistory = CommitTree.getCTree(_branch.getName());
                process(args);
            }
        }
    }
    /** Write current branch name into file. */
    public static void saveCurrentBranch() throws IOException {
        CBRANCH.createNewFile();
        Utils.writeContents(CBRANCH, _branch.getName());
        _branch.saveBranch();
    }


    /**
     * Creates a new Gitlet version-control system in the current directory.
     * @param args dummy args
     */
    public static void init(String[] args) throws IOException {
        if (!REPO.exists()) {
            REPO.mkdir();
            THINGS.mkdir();
            BRANCH.mkdir();
            STAGE.mkdir();
            HISTORY.mkdir();

        } else {
            throw new IllegalArgumentException("A gitlet version-control "
                    + "system already exists in the current directory.");
        }
        _stagingArea = new StageArea("master");
        new Branch("master").saveBranch();
        _branch = Branch.getBranch("master");
        Commit tracked = new Commit(null, "initial commit", null);
        HashMap<String, Commit> commits = new HashMap<>();
        commits.put(tracked.getSha1(), tracked);
        tracked.saveCommit();

        _treeAndhistory = new CommitTree(_branch, commits);
        _treeAndhistory.addCommit(tracked);
        _branch.addCommit(tracked);

    }

    /**process the commit command.
     * @param args the command and a file name .*/
    public static void add(String[] args) throws IOException {
        if (!Utils.join(Main.CWD, args[1]).exists()) {
            throw new IllegalArgumentException("File does not exist");
        }
        CommitTree ctree = CommitTree.getCTree(_branch.getName());
        _stagingArea.add(args[1], _branch, ctree);
    }
    /**process the commit command.
     * @param args the command and a message .*/
    public static void commit(String[] args) throws IOException {
        if (args.length == 1 || (args.length == 2
                && args[1].trim().isEmpty())) {
            throw new IllegalArgumentException("Please "
                    + "enter a commit message.");
        }
        Commit newCommit = _stagingArea.commit(args, _treeAndhistory, _branch);
        for (String blobName : newCommit.getallBlobs().values()) {
            _treeAndhistory.updateTracked(blobName);
        }

        _branch.addCommit(newCommit);
        _treeAndhistory.getBranch().addCommit(newCommit);
        _treeAndhistory.addCommit(newCommit);
        _treeAndhistory.setParent(newCommit);

        _treeAndhistory.saveCTree(_branch.getName());
    }
    /**process the status command.
     * @param args the command .*/
    public static void status(String[] args) {
        CommitTree ctree = CommitTree.getCTree(_branch.getName());
        System.out.println("=== Branches ===");
        Stack<String> otherBranch = new Stack<>();
        for (String branch : BRANCH.list()) {
            if (branch.equals(_branch.getName())) {
                System.out.println("*" + branch);
            } else {
                otherBranch.push(branch);
            }
        }
        while (!otherBranch.isEmpty()) {
            System.out.println(otherBranch.pop());
        }
        _stagingArea.status(_branch);

    }
    /**process the log command.
     * @param args the command .*/
    public static void log(String[] args) throws IOException {
        CommitTree ctree = CommitTree.getCTree(_branch.getName());
        _stagingArea.log(args, ctree, _branch);

    }

    /**process the globalLog command.
     * @param args the command .*/
    public static void globalLog(String[] args) throws IOException {
        CommitTree ctree = CommitTree.getCTree(_branch.getName());
        _stagingArea.globalLog(args, ctree, _branch);

    }

    /**process the find command.
     * @param args the command with find and a file name.*/
    public static void find(String[] args) throws IOException {
        CommitTree ctree = CommitTree.getCTree(_branch.getName());
        _stagingArea.find(args, ctree, _branch);

    }

    /**process the rm command.
     * @param args the command with rm and a file name.*/
    public static void rm(String[] args) {
        CommitTree ctree = CommitTree.getCTree(_branch.getName());
        _stagingArea.rm(args, ctree, _branch);
    }

    /**check empty command.
     * @param args the command.*/
    public static void checkLength(String[] args) throws IOException {
        if (args.length == 0) {
            throw new IllegalArgumentException("please enter a command");
        }
    }

    /**process the checkout command wrt different situation.
     * @param args the command with .*/
    public static void checkout(String[] args) throws IOException {
        if (args.length == 2) {
            checkoutBranch(args);
        } else if (args.length == 3) {
            checkoutFile(args);
        } else {
            checkoutCommit(args);
        }

    }
    /**process the checkout command wrt branch.
     * @param args the command and a branch name .*/
    public static void checkoutBranch(String[] args) throws IOException {
        _stagingArea.updateUntracked();
        if (_stagingArea.getUntracked().size() != 0) {
            throw new IllegalArgumentException("There is an"
                    + " untracked file in the way; delete it, or"
                    + " add and commit it first.");
        }
        Commit initial = _treeAndhistory.getIntial();
        String branchName = args[1].replace("/", "-");
        if (!Arrays.asList(BRANCH.list()).contains(branchName)) {
            throw new IllegalArgumentException("No such branch exists.");
        } else if (_branch.getName().equals(branchName)) {
            throw new IllegalArgumentException("No "
                    + "need to checkout the current branch.");
        }
        if (CWD.list() != null) {
            for (String file : CWD.list()) {
                HashMap<String, String> allTrakcedfile = new HashMap<>();
                if (_branch.getHead().getallBlobs() != null) {
                    allTrakcedfile.putAll(_branch.getHead().getallBlobs());
                    allTrakcedfile.putAll(_stagingArea.getTracked());
                    String sha1wanted = "";
                    if (!file.startsWith(".") && !allTrakcedfile.
                            containsValue(file) && Commit.
                            getCommitFromfile(Branch.
                            getBranch(branchName).getHead().getSha1()).
                            hasFile()
                            && Commit.getCommitFromfile(Branch.
                            getBranch(branchName).
                            getHead().getSha1()).getallBlobs().
                            containsValue(file)) {
                        HashMap<String, String> blobs = Commit.
                                getCommitFromfile(Branch.
                                        getBranch(branchName).getHead().
                                        getSha1()).getallBlobs();
                        for (String sha1 : blobs.keySet()) {
                            if (blobs.get(sha1).equals(file)) {
                                sha1wanted = sha1;
                                break;
                            }
                        }
                        String blb = Blob.getblobFromfileAsString(sha1wanted);
                        if (!Blob.getblobFromfileAsString(sha1wanted).
                                equals(Utils.readContentsAsString(Utils.
                                        join(CWD, file)))) {
                            throw new IllegalArgumentException("There is "
                                    + "an untracked file "
                                    + "in the way; delete it or add it first.");
                        }
                    }
                }
            }
            Branch otherbh = Branch.getBranch(branchName);
            HashMap<String, String> fileMap =
                    Commit.getCommitFromfile(Branch.
                            getBranch(branchName).getHead().
                            getSha1()).getallBlobs();
            checkoutBranchHelper(initial, branchName, fileMap);
        }
    }

    /**checkout branch helper.
     * responsible for deleted files and update the ctree.
     * @param initial initial commit of last commit, in case for
     *                null pointer excep.
     * @param branchName new branch name.
     * @param fileMap new branch's head commit's blobs*/
    public static void checkoutBranchHelper(Commit initial,
                                            String branchName,
        HashMap<String, String> fileMap) throws IOException {
        if (fileMap != null) {
            for (String name : fileMap.values()) {
                Utils.join(CWD, name).createNewFile();
                String sha1wanted = "";
                for (String sha1 : fileMap.keySet()) {
                    if (fileMap.get(sha1).equals(name)) {
                        sha1wanted = sha1;
                        break;
                    }
                }
                Utils.writeContents(Utils.join(CWD,
                        name), Blob.
                        getblobFromfile(sha1wanted));
            }
        }
        if (CWD.listFiles() != null) {
            for (File file : CWD.listFiles()) {
                if (!file.isHidden() && !file.isDirectory()
                        && !file.getName().equals("Makefile")
                        && !file.getName().endsWith(".iml")
                        && _stagingArea.getTracked() != null
                        && _stagingArea.getTracked().
                        containsValue(file.getName())) {
                    if (fileMap == null) {
                        file.delete();
                    } else if (!fileMap.containsValue(file.getName())) {
                        file.delete();

                    }
                }
            }
        }
        _branch = Branch.getBranch(branchName);
        if (CommitTree.checkCTreeExist(_branch.getName())) {
            _treeAndhistory = CommitTree.getCTree(_branch.getName());
        } else {
            HashMap<String, Commit> newCommits = new HashMap<>();
            for (Commit commit : _branch.getCommits()) {
                newCommits.put(commit.getSha1(), commit);
            }

            _treeAndhistory = new CommitTree(_branch, newCommits);
            for (Commit commit : _branch.getCommits()) {
                _treeAndhistory.addCommit(commit);
            }
        }
        _stagingArea.updateUntracked();
        _stagingArea.clear();
    }

    /**process the checkout command wrt File.
     * @param args the command, --, and a file name .*/
    public static void checkoutFile(String[] args) throws IOException {
        HashMap<String, String> hp = _branch.getHead().getallBlobs();
        String ar2 = args[2];
        boolean b = hp.containsValue(ar2);
        if (_branch.getHead().getallBlobs() != null) {
            if (!_branch.getHead().
                    getallBlobs().containsValue(args[2])) {
                throw new IllegalArgumentException("File "
                        + "does not exist in that commit.");
            }
        }

        Set<String> kyset = _branch.getHead().getallBlobs().keySet();
        String sha1need = "";
        for (String sha1 : _branch.getHead().getallBlobs().keySet()) {
            if (_branch.getHead().getallBlobs().get(sha1).equals(args[2])) {
                sha1need = sha1;
            }
        }
        if (Utils.join(CWD, args[2]).exists()) {
            Utils.restrictedDelete(Utils.join(CWD, args[2]));
        }
        Utils.join(CWD, args[2]).createNewFile();
        String content = Blob.getblobFromfile(sha1need);
        Utils.writeContents(Utils.join(CWD, args[2]),
                Blob.getblobFromfile(sha1need));
    }
    /**process the checkout command wrt File.
     * @param args the command, commit id, --, and a file name .*/
    public static void checkoutCommit(String[] args) throws IOException {
        Commit commit = null;
        boolean find = false;
        for (String sha1 : THINGS.list()) {
            if (sha1.startsWith(args[1])) {
                commit = Commit.getCommitFromfile(sha1);
                find = true;

            }
        }
        if (!find) {
            throw new IllegalArgumentException("No"
                    + " commit with that id exists.");
        }
        if (!_stagingArea.getTracked().containsValue(args[3])) {
            throw new IllegalArgumentException("File does "
                    + "not exist in that commit.");
        }
        String sha1need = "";
        for (String sha1 : commit.getallBlobs().keySet()) {
            if (commit.getallBlobs().get(sha1).equals(args[3])) {
                sha1need = sha1;
            }
        }
        Utils.join(CWD, args[3]).createNewFile();
        Utils.writeContents(Utils.join(CWD, args[3]),
                Blob.getblobFromfile(sha1need));

    }
    /**process the branch command.
     * Creates a new branch with the given name,
     * and points it at the current head node. A branch is nothing
     * more than a name for a reference (a SHA-1 identifier) to a
     * commit node. This command does NOT immediately switch to the
     * newly created branch (just as in real Git). Before you ever
     * call branch, your code should be running with a default branch
     * called "master".
     * @param args the command: branch and its name. .*/
    public static void branch(String[] args) throws IOException {
        if (Arrays.asList(BRANCH.list()).contains(args[1])) {
            throw new IllegalArgumentException("A branch"
                    + " with that name already exists.");
        } else {
            new Branch(args[1], _branch.getHead().getSha1()).saveBranch();
            Branch.getBranch(args[1]).addCommit(_branch.getCommits());
            _stagingArea.saveStage(args[1]);
        }
    }
    /** Deletes the branch with the given name. This only means
     * to delete the pointer associated with the branch; it does not
     * mean to delete all commits that were created under the branch,
     * or anything like that.
     * @param args remove the branch within the name.*/
    public static void rmbranch(String[] args) throws IOException {
        if (!Arrays.asList(BRANCH.list()).contains(args[1])) {
            throw new IllegalArgumentException("A"
                    + " branch with that name does not exist.");
        } else if (_branch.getName().equals(args[1])) {
            throw new IllegalArgumentException("Cannot"
                    + " remove the current branch.");
        } else {
            Utils.join(BRANCH, args[1]).delete();
            Utils.join(HISTORY, args[1]).delete();
        }
    }

    /**Merges files from the given branch into the current branch.
     * This method is a bit complicated, so here's a more detailed
     * description:
     * check fail cases before start merging.
     * @param args cmd.*/
    public static void mergeChecker(String[] args) throws IOException {
        boolean find = false;
        for (String branchName : BRANCH.list()) {
            if (branchName.equals(args[1])) {
                find = true;
            }
        }
        if (!find) {
            throw new IllegalArgumentException("A "
                    + "branch with that name does not exist.");
        }
        if (args[1].equals(_branch.getName())) {
            throw new IllegalArgumentException("Cannot "
                    + "merge a branch with itself.");
        }
        if (!_stagingArea.getRemoval().isEmpty() || !_stagingArea
                .getStagedAdded().isEmpty()) {
            throw new IllegalArgumentException("You have uncommitted changes.");
        }
        if (CWD.list() != null) {
            for (String file : CWD.list()) {
                HashMap<String, String> allTrakcedfile = new HashMap<>();
                if (_branch.getHead().getallBlobs() != null) {
                    allTrakcedfile.putAll(_branch.getHead().getallBlobs());
                    allTrakcedfile.putAll(_stagingArea.getTracked());
                    String sha1wanted = "";
                    if (!file.startsWith(".") && !allTrakcedfile.
                            containsValue(file) && Commit.
                            getCommitFromfile(Branch.
                                    getBranch(args[1]).getHead().getSha1()).
                            hasFile()
                            && Commit.getCommitFromfile(Branch.
                            getBranch(args[1]).
                            getHead().getSha1()).getallBlobs().
                            containsValue(file)) {
                        HashMap<String, String> blobs = Commit.
                                getCommitFromfile(Branch.
                                getBranch(args[1]).getHead().
                                        getSha1()).getallBlobs();
                        for (String sha1 : blobs.keySet()) {
                            if (blobs.get(sha1).equals(file)) {
                                sha1wanted = sha1;
                                break;
                            }
                        }
                        String xx = Blob.getblobFromfileAsString(sha1wanted);
                        String yy = Utils.readContentsAsString(Utils.
                                join(CWD, file));
                        if (!Blob.getblobFromfileAsString(sha1wanted).
                            equals(Utils.readContentsAsString(Utils.
                                    join(CWD, file)))) {
                            throw new IllegalArgumentException("There is "
                                + "an untracked file "
                                + "in the way; delete it or add it first.");
                        }
                    }
                }
            }
        }
    }

    /**Merges files from the given branch into the current branch.
     * This method is a bit complicated, so here's a more detailed
     * description:
     * @param args cmd.*/
    public static void merge(String[] args) throws IOException {
        if (mergeConflict(args)) {
            System.out.println("Encountered a merge conflict.");
            mergeC(args, mergeConflictHash(args));

        } else {
            mergeChecker(args);
            mergeN(args);
        }
    }

    /**Merges files from the given branch into the current branch.
     * This method is a bit complicated, so here's a more detailed
     * description:
     * THIS CASE, there exist conflict, jumping over them.
     * @param args cmd.
     * @param conflict the conflicting files and sha1.*/
    public static void mergeC(
            String[] args, HashMap<String, String> conflict)
            throws IOException {
        StageArea otherStage = StageArea.getFile(args[1]);
        mergeHis(args);

        for (String trackedsha1 : otherStage.getTracked().keySet()) {
            if (!_stagingArea.getTracked().containsKey(trackedsha1)) {
                _stagingArea.getTracked().put(trackedsha1,
                        otherStage.getTracked().get(trackedsha1));
            }
        }
        for (String removalsha1 : otherStage.getRemovalhistory().keySet()) {
            _stagingArea.getRemovalhistory().put(removalsha1,
                    otherStage.getRemovalhistory().get(removalsha1));
        }
        for (String filenameNew : _stagingArea.getTracked().values()) {
            if (!conflict.containsKey(filenameNew)) {
                boolean exist = false; boolean removal = false;
                String[] fl = CWD.list();
                for (File curFile : CWD.listFiles()) {
                    if (!curFile.isHidden() && !curFile.isDirectory()
                            && curFile.equals(filenameNew)) {
                        exist = true;
                        break;
                    }
                }
                if (_stagingArea.getRemovalhistory().
                        containsValue(filenameNew)) {
                    removal = true;
                }
                if (!exist && !removal) {
                    Utils.join(CWD, filenameNew).createNewFile();
                    String sha1wanted = "";
                    for (String sha1 : _stagingArea.getTracked().keySet()) {
                        if (_stagingArea.getTracked().get(sha1)
                                .equals(filenameNew)) {
                            sha1wanted = sha1;
                            break;
                        }
                    }
                    Utils.writeContents(Utils.join(CWD,
                            filenameNew), Blob.
                            getblobFromfile(sha1wanted));
                }
                if (!exist && removal) {
                    Utils.restrictedDelete(Utils.join(CWD, filenameNew));
                }
            }
        }
    }

    /**Merges files from the given branch into the current branch.
     * This method is a bit complicated, so here's a more detailed
     * description:
     * @param args cmd.*/
    public static void mergeN(String[] args) throws IOException {
        StageArea otherStage = StageArea.getFile(args[1]);
        mergeHis(args);

        for (String trackedsha1 : otherStage.getTracked().keySet()) {
            if (!_stagingArea.getTracked().containsKey(trackedsha1)) {
                _stagingArea.getTracked().put(trackedsha1,
                        otherStage.getTracked().get(trackedsha1));
            }
        }
        for (String removalsha1 : otherStage.getRemovalhistory().keySet()) {
            _stagingArea.getRemovalhistory().put(removalsha1,
                    otherStage.getRemovalhistory().get(removalsha1));
        }

        for (String filenameNew : _stagingArea.getTracked().values()) {
            boolean exist = false; boolean removal = false;
            String[] fl = CWD.list();
            for (File curFile : CWD.listFiles()) {
                if (!curFile.isHidden() && !curFile.isDirectory()
                        && curFile.equals(filenameNew)) {
                    exist = true;
                    break;
                }
            }
            if (_stagingArea.getRemovalhistory().containsValue(filenameNew)) {
                removal = true;
            }
            if (!exist && !removal) {
                Utils.join(CWD, filenameNew).createNewFile();
                String sha1wanted = "";
                for (String sha1 : _stagingArea.getTracked().keySet()) {
                    if (_stagingArea.getTracked().get(sha1)
                            .equals(filenameNew)) {
                        sha1wanted = sha1;
                        break;
                    }
                }
                String bl = Blob.getblobFromfile(sha1wanted);
                Utils.writeContents(Utils.join(CWD,
                        filenameNew), Blob.
                        getblobFromfile(sha1wanted));
            }
            if (!exist && removal) {
                Utils.restrictedDelete(Utils.join(CWD, filenameNew));
            }
        }

    }
    /**Merges files from the given branch into the current branch.
     * This method is a bit complicated, so here's a more detailed
     * description:
     * THIS CASE, there exist conflict, jumping over them.
     * @param args cmd.*/
    public static void mergeHis(String[] args)
            throws IOException {
        CommitTree othertree = CommitTree.getCTree(args[1]);
        Commit lstCommitMergedin = othertree.
                getOrderedHistory().getLast();
        _treeAndhistory.getOrderedHistory().getLast().setTobeMerged();
        _treeAndhistory.getHistory().get(
                _treeAndhistory.getOrderedHistory().getLast()
                        .getSha1()).setTobeMerged();
        _treeAndhistory.getOrderedHistory().getLast().beingMergedFrom(args[1]);
        _treeAndhistory.getOrderedHistory().getLast().
                beingMergedFrom(lstCommitMergedin);
        _treeAndhistory.getHistory().get(
                _treeAndhistory.getOrderedHistory().getLast()
                        .getSha1()).beingMergedFrom(args[1]);
        _treeAndhistory.getHistory().get(
                _treeAndhistory.getOrderedHistory().getLast()
                        .getSha1()).beingMergedFrom(lstCommitMergedin);

    }
    /**Helper method for merge.
     * @param args  cmds.
     * @return return a hashmap of SHA1 to FILENAME
     * which are going to be deleted or overwrited by merge call.*/
    public static  HashMap<String, String> mergeConflictHash(String[] args)
            throws IOException {
        StageArea otherStage = StageArea.getFile(args[1]);
        HashMap<String, String> other = otherStage.getTracked();
        HashMap<String, String> here = _stagingArea.getTracked();
        HashMap<String, String> otherpossible = new HashMap<>();
        HashMap<String, String> herepossible = new HashMap<>();
        HashMap<String, String> repeated = new HashMap<>();
        HashMap<String, String> finalother = new HashMap<>();
        HashMap<String, String> finalhere = new HashMap<>();
        for (String sha1 : other.keySet()) {
            for (String sha12 : other.keySet()) {
                if (!sha1.equals(sha12) && other.get(sha1).
                        equals(other.get(sha12))) {
                    otherpossible.put(other.get(sha1), sha1);
                }
            }
        }
        for (String sha1 : here.keySet()) {
            for (String sha12 : here.keySet()) {
                if (!sha1.equals(sha12) && here.get(sha1).
                        equals(here.get(sha12))) {
                    herepossible.put(here.get(sha1), sha1);
                }
            }
        }
        for (String possibleFile : otherpossible.keySet()) {
            for (String possibleFile2 : herepossible.keySet()) {
                if (possibleFile.equals(possibleFile2)) {
                    repeated.put(possibleFile, null);
                }
            }
        }
        for (String repeatFile : repeated.keySet()) {
            finalhere.put(repeatFile, herepossible.get(repeatFile));
            finalother.put(repeatFile, otherpossible.get(repeatFile));
        }
        mergePrt(finalhere, finalother);
        return finalhere;
    }

    /**Helper method for merge.
     * @param finalhere  cmds.
     * @param finalother cmds
     * which are going to be deleted or overwrited by merge call.*/
    public static void  mergePrt(HashMap<String, String> finalhere,
                                 HashMap<String, String> finalother)
            throws IOException {
        for (String repeatedFile : finalhere.keySet()) {
            String current = System.lineSeparator()
                    + Blob.getblobFromfileAsString(finalhere.
                    get(repeatedFile));
            String merge = System.lineSeparator()
                    + Blob.getblobFromfileAsString(finalother.
                    get(repeatedFile));
            String content = "<<<<<<< HEAD"
                    + current + " file in current branch " + "======="
                    + merge + " file in other branch " + ">>>>>>>";
            Utils.join(CWD, repeatedFile).createNewFile();
            Utils.writeContents(Utils.join(CWD, repeatedFile), content);
            Blob bb = new Blob(repeatedFile);
            _stagingArea.getTracked().put(bb.getSha1(), bb.getName());
        }
    }

    /**Helper method for merge.
     * @param args  cmds.
     * @return return a hashmap of SHA1 to FILENAME
     * which are going to be deleted or overwrited by merge call.*/
    public static  boolean mergeConflict(String[] args)
            throws IOException {
        boolean findbh = false; boolean find = false;
        for (String branchName : BRANCH.list()) {
            if (branchName.equals(args[1])) {
                findbh = true;
            }
        }
        if (!findbh) {
            throw new IllegalArgumentException("A "
                    + "branch with that name does not exist.");
        }
        StageArea otherStage = StageArea.getFile(args[1]);
        HashMap<String, String> other = otherStage.getTracked();
        HashMap<String, String> here = _stagingArea.getTracked();
        HashMap<String, String> otherpossible = new HashMap<>();
        HashMap<String, String> herepossible = new HashMap<>();
        HashMap<String, String> repeated = new HashMap<>();
        HashMap<String, String> finalother = new HashMap<>();
        HashMap<String, String> finalhere = new HashMap<>();
        for (String sha1 : other.keySet()) {
            for (String sha12 : other.keySet()) {
                if (!sha1.equals(sha12) && other.get(sha1).
                        equals(other.get(sha12))) {
                    otherpossible.put(other.get(sha1), sha1);
                }
            }
        }
        for (String sha1 : here.keySet()) {
            for (String sha12 : here.keySet()) {
                if (!sha1.equals(sha12) && here.get(sha1).
                        equals(here.get(sha12))) {
                    herepossible.put(here.get(sha1), sha1);
                }
            }
        }
        for (String possibleFile : otherpossible.keySet()) {
            for (String possibleFile2 : herepossible.keySet()) {
                if (possibleFile.equals(possibleFile2)) {
                    repeated.put(possibleFile, null);
                }
            }
        }
        if (repeated.size() != 0) {
            find = true;
        }
        for (String repeatFile : repeated.keySet()) {
            finalhere.put(repeatFile, herepossible.get(repeatFile));
            finalother.put(repeatFile, otherpossible.get(repeatFile));
        }
        mergePrt(finalhere, finalother);
        return find;
    }
    /**Helper method for merge.
     * @param args  cmds.
     * @return return a hashmap of SHA1 to FILENAME
     * which are going to be deleted or overwrited by merge call.*/
    public static HashMap<String, String> mergeHelper(String[] args)
            throws IOException {
        StageArea otherStage = StageArea.getFile(args[1]);
        CommitTree othertree = CommitTree.getCTree(args[1]);
        HashMap<String, String> toReturn = new HashMap<>();
        for (String trackedsha1 : otherStage.getTracked().keySet()) {
            if (!_stagingArea.getTracked().containsKey(trackedsha1)) {
                _stagingArea.getTracked().put(trackedsha1,
                        otherStage.getTracked().get(trackedsha1));
            }
        }
        for (String removalsha1 : otherStage.getRemovalhistory().keySet()) {
            _stagingArea.getRemovalhistory().put(removalsha1,
                    otherStage.getRemovalhistory().get(removalsha1));
        }

        for (String filenameNew : _stagingArea.getTracked().values()) {
            boolean exist = false; boolean removal = false;
            for (File curFile : CWD.listFiles()) {
                if (curFile.getName().equals(filenameNew)) {
                    exist = true;
                    break;
                }
            }
            if (_stagingArea.getRemovalhistory().containsValue(filenameNew)) {
                removal = true;
            }
            String sha1wanted = "";
            if (!exist && !removal) {
                Utils.join(CWD, filenameNew).createNewFile();

                for (String sha1 : _stagingArea.getTracked().keySet()) {
                    if (_stagingArea.getTracked().get(sha1).
                            equals(filenameNew)) {
                        sha1wanted = sha1;
                        break;
                    }
                }
                toReturn.put(sha1wanted, filenameNew);
            }
            if (exist && removal) {
                toReturn.put(sha1wanted, filenameNew);
            }

        }
        return toReturn;
    }


    /**
     * Checks out all the files tracked by the
     * given commit. Removes tracked files that
     * are not present in that commit. Also moves
     * the current branch's head to that commit node.
     * @param args Array in format: {'reset', commitId}
     */
    public static void reset(String[] args) throws IOException {
        boolean find = false; Commit commit = null;
        for (String id : THINGS.list()) {
            if (id.startsWith(args[1])) {
                find = true;
                commit = Commit.getCommitFromfile(id);
                break;
            }
        }
        if (!find) {
            throw new IllegalArgumentException("No commit with"
                    + " that id exists.");
        } else {
            if (CWD.list() != null) {
                for (String file : CWD.list()) {
                    String sha1wanted = "";
                    if (!file.startsWith(".") && !_stagingArea.
                            getTracked().containsValue(file)
                            && commit.hasFile() && commit.
                            getallBlobs().containsValue(file)) {
                        for (String sha1 : commit.getallBlobs().
                                keySet()) {
                            if (commit.getallBlobs().get(sha1).
                                    equals(file)) {
                                sha1wanted = sha1;
                                break;
                            }
                        }
                        String one = Blob.getblobFromfile(sha1wanted);
                        String two = Utils.readContentsAsString(Utils.
                                join(CWD, file));
                        if (!Blob.getblobFromfile(sha1wanted).equals
                                (Utils.readContentsAsString(Utils.
                                        join(CWD, file)))) {
                            throw new IllegalArgumentException("There is an"
                                    + " untracked file in the way; "
                                    + "delete it, or"
                                    + " add and commit it first.");
                        }
                    }
                }
                for (File file : CWD.listFiles()) {
                    if (!file.isHidden()
                            && !file.getName().equals("Makefile")
                            && !file.getName().endsWith(".iml")) {
                        file.delete();
                    }
                }
            }
        }
        _stagingArea.clear();
        _branch.addCommit(commit);
    }

    /**
     * Checks whether the arguments of corresponding
     * command have correct format.
     * @param args commands
     * @param cmd command
     */
    public static void checkArgs(String cmd, String[] args) {
        boolean match;
        switch (cmd) {
        case "add":
        case "rm":
        case "reset":
        case "branch":
        case "rm-branch":
            match = args.length == 2;
            break;
        case "commit":
            match = args.length == 1 || args.length == 2;
            break;
        case "init":
        case "log":
        case "global-log":
        case "status":
            match = args.length == 1;
            break;
        case "checkout":
            match = (args.length == 2)
                    || (args.length == 3 && args[1].equals("--"))
                    || (args.length == 4 && args[2].equals("--"));
            break;
        default:
            match = args.length == 2;
        }
        if (!match) {
            throw new IllegalArgumentException("Incorrect operands.");
        }
    }

    /** The stage area of CWD. */
    private static StageArea _stagingArea;
    /** The commit history of CWD. */
    private static CommitTree _treeAndhistory;
    /** All branches. */
    private static HashMap<String, StageArea>
            _branches = new HashMap<>();
    /** Current branche. */
    private static Branch _branch;
}
