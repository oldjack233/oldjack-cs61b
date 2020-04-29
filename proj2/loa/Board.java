/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Formatter;
import java.util.List;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author Ziyuan Tang
 */
class Board {

    /** Default number of moves for each side that results in a draw. */
    static final int DEFAULT_MOVE_LIMIT = 60;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this();
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        this._winnerKnown = false;
        this._contents = new Piece[BOARD_SIZE][BOARD_SIZE];
        this._ccontents = new Piece[BOARD_SIZE][BOARD_SIZE];
        this.pieceCount = new int[Piece.values().length];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this.pieceCount[contents[i][j].ordinal()]++;
                this._contents[i][j] = contents[i][j];
                _board[i * 8 + j] = _contents[i][j];
                _cboard[i * 8 + j] = _contents[i][j];
            }
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(this._contents[i],
                    0, _ccontents[i], 0, BOARD_SIZE);
        }
        _turn = side;
        _cturn = side;
        _moveLimit = DEFAULT_MOVE_LIMIT;
    }
    /** a pieceCount tht contain three different pieces. */
    private int[] pieceCount;

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }
        this._ccontents = new Piece[BOARD_SIZE][BOARD_SIZE];
        _contents = new Piece[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                this._turn = board._turn;
                this._contents[i][j] = board._contents[i][j];
                _board[i * 8 + j] = _contents[i][j];
            }
        }
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(this._contents[i],
                    0, _ccontents[i], 0, BOARD_SIZE);
        }

    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        if (_board[sq.index()] == BP) {
            pieceCount[BP.ordinal()]--;
        } else if (_board[sq.index()] == WP) {
            pieceCount[WP.ordinal()]--;
        } else {
            pieceCount[EMP.ordinal()]--;
        }
        pieceCount[v.ordinal()]++;
        pieceCount[EMP.ordinal()]++;
        _board[sq.index()] = v;
        _contents[sq.row()][sq.col()] = v;
        if (next != null) {
            _turn = next;
        }
    }

    /** Set the square at SQ to V, without modifying the side that
     *  moves next. */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /** Set limit on number of moves by each side that results in a tie to
     *  LIMIT, where 2 * LIMIT > movesMade(). */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = limit * 2;
    }

    /** Assuming isLegal(MOVE), make MOVE. Assumes MOVE.isCapture()
     *  is false. */
    void makeMove(Move move) {
        assert isLegal(move);
        Piece to = _board[move.getFrom().index()];
        Piece next;
        if (to == BP) {
            next = WP;
        } else {
            next = BP;
        }
        set(move.getTo(), to, next);
        _board[move.getFrom().index()] = EMP;
        _contents[move.getFrom().row()][move.getFrom().col()] = EMP;
        _moves.add(move);

    }




    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        assert movesMade() > 0;
        Board bd = new Board(_ccontents, _cturn);
        ArrayList<Move> newMove = new ArrayList<>();
        _moves.remove(_moves.size() - 1);
        for (Move mvs : _moves) {
            bd.makeMove(mvs);
            newMove.add(mvs);
        }
        _moves.removeAll(_moves);
        _moves.addAll(newMove);
        _turn = _cturn;
        this.copyFrom(bd);

    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retractGUI() {
        assert movesMade() > 0;
        Board bd = new Board(BOARD1, _cturn);
        ArrayList<Move> newMove = new ArrayList<>();
        _moves.remove(_moves.size() - 1);
        for (Move mvs : _moves) {
            bd.makeMove(mvs);
            newMove.add(mvs);
        }
        _moves.removeAll(_moves);
        _moves.addAll(newMove);
        _turn = _cturn;
        this.copyFrom(bd);

    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        if (get(from) == get(to)) {
            return false;
        }
        if (get(from).fullName().equals("-")) {
            return false;
        }
        int dir1 = from.direction(to);
        int dir2 = to.direction(from);
        int distance = from.distance(to);
        int num = numberOfPieces(from, dir1);
        int num1 = numberOfPieces(from, dir2);

        if (distance != num + num1 - 1) {
            return false;
        }
        if (block(from, to)) {
            return false;
        }
        return from.isValidMove(to);
    }

    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        return isLegal(move.getFrom(), move.getTo());
    }

    /**Return TRUE if FROM to TO is blocking.*/
    private boolean block(Square from, Square to) {
        int dir = from.direction(to);
        int distance = from.distance(to);
        int ver = DIREC[dir][1];
        int hor = DIREC[dir][0];
        int i = 0;
        int count = 0;
        Piece ref = get(from);
        for (Square square = sq(from.col(), from.row());
             i < distance;
             square = sq(square.col() + hor, square.row() + ver)) {
            Piece a = get(square);
            if (ref.equals((get(square).opposite()))) {
                count++;
            }
            i++;
        }
        if (count > 1) {
            return true;
        } else if (count == 1) {
            Square sq = sq(from.col() + hor * distance,
                    from.row() + ver * distance);
            return get(sq).fullName().equals("-");
        } else {
            return false;
        }
    }
    /** Return TRUE if FROM to TO is a capture. */
    private boolean cap(Square from, Square to) {
        int dir = from.direction(to);
        int distance = from.distance(to);
        int ver = DIREC[dir][1];
        int hor = DIREC[dir][0];
        int i = 0;
        int count = 0;
        Piece ref = get(from);
        for (Square square = sq(from.col(), from.row());
             i < distance;
             square = sq(square.col() + hor, square.row() + ver)) {
            Piece a = get(square);
            if (ref.equals((get(square).opposite()))) {
                count++;
            }
            i++;
        }
        if (count == 1) {
            Square sq = sq(from.col()
                    + hor * distance, from.row() + ver * distance);
            return !get(sq).fullName().equals("-");
        } else {
            return false;
        }
    }
    /**Return the numberOfPieces in SQ's direction DIR. */
    private int numberOfPieces(Square sq, int dir) {
        int count = 0;
        int ver = DIREC[dir][1];
        int hor = DIREC[dir][0];
        for (Square square = sq(sq.col(), sq.row());
            exists(square.col(), square.row());
            square = sq(square.col() + hor, square.row() + ver)) {
            if (!get(square).fullName().equals("-")) {
                count++;
            }
            if (square.col() + hor >= BOARD_SIZE
                    || square.col() + hor < 0
                    || square.row() + ver >= BOARD_SIZE
                    || square.row() + ver < 0) {
                break;
            }
        }
        return count;
    }

    /** Return a sequence of all legal moves from this position @para CUR. */
    List<Move> legalMoves(Square cur) {
        ArrayList<Move> movelist = new ArrayList<>();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (cur.isValidMove(sq(i, j)) && isLegal(cur, sq(i, j))) {
                    boolean cap = cap(cur, sq(i, j));
                    movelist.add(new Move(cur, sq(i, j), cap));
                }
            }
        }
        return movelist;
    }

    /** Return true iff the game is over (either player has all his
     *  pieces continguous or there is a tie). */
    boolean gameOver() {
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        List<Integer> contiNum = getRegionSizes(side);
        boolean contig = true;
        for (int i : contiNum) {
            if (contiNum.get(0) != i) {
                contig = false;
                break;
            }
        }
        int exist = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (_board[sq(i, j).index()] == side) {
                    exist++;
                }
            }
        }

        if (contiNum.get(0) != exist) {
            contig = false;
        }
        return contig;
    }
    /**reset _WINNERKNOWN.*/
    public void reset() {
        _winnerKnown = false;
        _moves.removeAll(_moves);
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!_winnerKnown) {
            if (movesMade() <= _moveLimit) {
                if (piecesContiguous(BP) && piecesContiguous(WP)
                        && _moves.size() > 0) {
                    Move lastMove = _moves.get(_moves.size() - 1);
                    _winner = _board[lastMove.getTo().index()];
                    _winnerKnown = true;
                } else if (piecesContiguous(BP)) {
                    _winner = BP;
                    _winnerKnown = true;
                } else if (piecesContiguous(WP)) {
                    _winner = WP;
                    _winnerKnown = true;
                } else if (movesMade() == _moveLimit) {
                    if (!piecesContiguous(BP) || !piecesContiguous(WP)) {
                        _winner = EMP;
                    }
                } else {
                    _winner = null;
                }
            } else if (movesMade() > _moveLimit) {
                _winner = EMP;
                _winnerKnown = true;
            } else {
                _winner = null;
            }
        }
        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        return false;
    }
    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    int numContig(Square sq, boolean[][] visited, Piece p) {
        numContigHelper(sq, visited, p);
        int count = 0;
        for (boolean[] booleans : visited) {
            for (int j = 0; j < visited.length; j++) {
                if (booleans[j]) {
                    count += 1;
                }
            }
        }
        return count;
    }


    /** Return the size of the as-yet unvi @para SQ, @para VISITED, @para P. */
    private int numContigHelper(Square sq, boolean[][] visited, Piece p) {
        if (p == EMP) {
            return 0;
        }
        if (!visited[sq.col()][sq.row()]) {
            visited[sq.col()][sq.row()] = true;
        }

        ArrayList<Square> cleanAdj;
        cleanAdj = obtainClean(sq, visited, p);
        for (Square square : cleanAdj) {
            numContigHelper(square, visited, p);
        }
        return cleanAdj.size();
    }

    /**obtain cleanAdj, @para SQ, @para VISITED
     * , @para P, return @para CLEANADJ.*/
    private ArrayList<Square> obtainClean(
            Square sq, boolean[][] visited, Piece p) {
        Square [] adj = sq.adjacent();
        ArrayList<Square> arrayAdj = new ArrayList<>();
        ArrayList<Square> cleanAdj = new ArrayList<>();
        for (Square square : adj) {
            arrayAdj.addAll(Collections.singleton(square));
        }
        for (int i = 0; i < adj.length; i++) {
            if (!visited[arrayAdj.get(i).col()][arrayAdj.get(i).row()]
                    && _board[arrayAdj.get(i).index()] == p) {
                cleanAdj.add(arrayAdj.get(i));
            }
        }
        return cleanAdj;
    }

    /**return a new 8 * 8 boolean array
     *  with all false as default by getNewVisited.
     *  */
    private boolean[][] getNewVisited() {
        boolean[][] visited = new boolean[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                visited[i][j] = false;
            }
        }
        return visited;
    }

    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Square sq = sq(i, j);
                Piece piece = _board[sq.index()];
                if (piece == WP) {
                    boolean[][] visitedW = getNewVisited();
                    int numW = numContig(sq, visitedW, piece);
                    if (numW != 0) {
                        _whiteRegionSizes.add(numW);
                    }
                } else if (piece == BP) {
                    boolean[][] visitedB = getNewVisited();
                    int numB = numContig(sq, visitedB, piece);
                    if (numB != 0) {
                        _blackRegionSizes.add(numB);
                    }
                }
            }
        }

        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
    }

    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }

    /**return the score of contiguity for @para P.*/
    double contiScore(Piece p) {
        if (this.pieceCount[p.ordinal()] == 0) {
            return 0;
        }
        double netScore = 0;
        double total = 0;

        computeRegions();
        if (p == WP) {
            for (int i = 0; i < _whiteRegionSizes.size(); i++) {
                netScore += _whiteRegionSizes.get(i)
                        / (double) this.pieceCount[p.ordinal()];
            }
            return netScore / _whiteRegionSizes.size();
        } else if (p == BP) {
            for (int i = 0; i < _blackRegionSizes.size(); i++) {
                netScore += _blackRegionSizes.get(i)
                        / (double) this.pieceCount[p.ordinal()];
            }
            return netScore / _blackRegionSizes.size();
        }


        return netScore;
    }

    /**contiCountHelp, @para P,@para ROW, @para COL, @return COUNT.*/
    int contiCountHelp(Piece p, int row, int col) {
        int count = 0;
        if (row - 1 >= 0) {
            if (_board[sq(row - 1, col).index()] == p) {
                count += 1;
            }
        }
        if (row + 1 < 8) {
            if (_board[sq(row + 1, col).index()] == p) {
                count += 1;
            }
        }
        if (col + 1 < 8) {
            if (_board[sq(row, col + 1).index()] == p) {
                count += 1;
            }
        }
        if (0 <= col - 1) {
            if (_board[sq(row, col - 1).index()] == p) {
                count += 1;
            }
        }
        if (row + 1 < 8 && col + 1 < 8) {
            if (_board[sq(row + 1, col + 1).index()] == p) {
                count += 1;
            }
        }
        if (0 <= row - 1 && 0 <= col - 1) {
            if (_board[sq(row - 1, col - 1).index()] == p) {
                count += 1;
            }
        }
        if (row + 1 < 8 && 0 <= col - 1) {
            if (_board[sq(row + 1, col - 1).index()] == p) {
                count += 1;
            }
        }
        if (0 <= row - 1 && col + 1 < 8) {
            if (_board[sq(row - 1, col + 1).index()] == p) {
                count += 1;
            }
        }
        return count;
    }




    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** Current contents of the board.
     * Square S is at _board[S.index()]. */
    private final Piece[] _board = new Piece[BOARD_SIZE  * BOARD_SIZE];
    /** Copy of current contents of the board.
     *  Square S is at _board[S.index()]. */
    private final Piece[] _cboard = new Piece[BOARD_SIZE  * BOARD_SIZE];

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** copy of Current side on move. */
    private Piece _cturn;
    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit;
    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown = false;
    /** Cached value of the winner (BP, WP,
     *  EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;

    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of the sizes of continguous cluster
     * s of pieces, by color. */
    private final ArrayList<Integer>
        _whiteRegionSizes = new ArrayList<>(),
        _blackRegionSizes = new ArrayList<>();
    /**contents for the board. */
    private Piece[][] _contents;
    /**copy of initialcontents for the board. */
    private Piece[][] _ccontents;
    /**pieceNumber. */
    private int[] pieceNumber;
    /**from 1-7 direction to 1,0,-1 direction. */
    private static final int[][] DIREC = {
            { 0, 1 }, { 1, 1 }, { 1, 0 }, { 1, -1 }, { 0, -1 },
            { -1, -1 }, { -1, 0 }, { -1, 1 }
    };
    /**initial setting of board.*/
    static final Piece[][] BOARD1 = {
            { EMP, BP,  BP,  BP,  BP, BP, BP, EMP },
            { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP,  EMP,  EMP, EMP, WP  },
            { WP,  EMP,  EMP, EMP, EMP,  EMP, EMP, WP  },
            { WP,  EMP,  EMP,  EMP, EMP,  EMP, EMP, WP  },
            { WP,  EMP, EMP, EMP,  EMP, EMP, EMP, WP  },
            { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP  },
            { EMP, BP,  BP,  BP,  BP,  BP,  BP, EMP }
    };
}

