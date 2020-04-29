/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;
import java.awt.datatransfer.SystemFlavorMap;
import java.io.File;
import java.util.HashMap;
import java.util.TreeMap;

import static loa.JackMachinePlayer.BMap;
import static loa.Piece.*;

/** An automated Player.
 *  @author Ziyuan Tang
 */
class MachinePlayer extends Player {

    /////JACK

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;
    /** Used to convey moves discovered by findMove. */
    private static Move _foundMove;
    public static File BMap = new File("loa/BlackMap2");
    public static File WMap = new File("loa/WhiteMap2");
    private static int count = 0;

    /** A new MachinePlayer with no piece or controller (intended to produce
     *  a template). */
    MachinePlayer() {
        this(null, null);
    }

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    String getMove() {
        Move choice;

        assert side() == getGame().getBoard().turn();
        int depth;
        choice = searchForMove();
//        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private Move searchForMove() {
        Board work = new Board(getBoard());
        double value;
        assert side() == work.turn();
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        count += 1;
        return _foundMove;
    }
    /**private tp static, findmove add this
     * before it, _foundmove should be pirvate*/

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    static double findMove(Board board, int depth, boolean saveMove,
                           int sense, double alpha, double beta) {
        TreeMap<Double, Move> moveScore = new TreeMap<>(); double bestscore = 0;
        if (depth == 0) {
            return contiguHelper(sense, board);
        }
        if (saveMove && sense == 1) {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Square sqnow = Square.sq(row, col);
                    if (board.get(sqnow) == WP) {
                        Board board1 = new Board(board); double score;
                        for (Move m : board.efficientMoves2(sqnow, count)) {
                            board1.makeMove(m); score = findMove(board1,
                                    depth - 1, true, -1, alpha, beta);
                            board1.retract(); moveScore.put(score, m);
                            if (score > bestscore) {
                                bestscore = score;
                            }
                            if (depth % 2 == 1) {
                                alpha = Math.max(alpha, score);
                            } else {
                                beta = Math.min(beta, score);
                            }
                            if (alpha > beta) {
                                break;
                            }
                        }
                    }
                }
            }
        } else {
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    Square sqnow = Square.sq(row, col);
                    if (board.get(sqnow) == BP) {
                        Board board1 = new Board(board); double score;
                            for (Move m : board.efficientMoves2(sqnow, count)) {
                                board1.makeMove(m); score = findMove(board1,
                                        depth - 1, true, 1, alpha, beta);
                                board1.retract(); moveScore.put(score, m);
                                if (score > bestscore) {
                                    bestscore = score;
                                }
                                if (depth % 2 == 1) {
                                    alpha = Math.max(alpha, score);
                                } else {
                                    beta = Math.min(beta, score);
                                }
                                if (alpha > beta) {
                                    break;

                            }
                        }
                    }
                }
            }
        }
        _foundMove = moveScore.lastEntry().getValue();

        return bestscore;
    }
    /**when depth is 0, return score of SENSE on BOARD. */
    static double contiguHelper(int sense, Board board) {
        if (sense == 1) {
            return board.contiScore2(BP)*1.5  - board.contiScore2(WP)*0.5;
        } else {
            return board.contiScore2(WP)*1.5 - board.contiScore2(BP)*0.5 ;
        }
    }
    @Override
    public HashMap getWMap() {
        return WhiteMap;
    }
    @Override
    public HashMap getBMap() {
        return BlackMap;
    }

    /**return _foundMove. */
    static Move getfoundMove() {
        return _foundMove;
    }


    private int validMoves() {
        int validMoves = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square sqnow = Square.sq(row, col);
                Board bd = this.getGame().getBoard();
                if (bd.get(sqnow) == WP) {
                    for (Move m : bd.legalMoves(sqnow)) {
                        validMoves ++;
                    }
                }
            }
        }
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Square sqnow = Square.sq(row, col);
                Board bd = this.getGame().getBoard();
                if (bd.get(sqnow) == BP) {
                    for (Move m : bd.legalMoves(sqnow)) {
                        validMoves ++;
                    }
                }
            }
        }
        return validMoves;
    }

    /** Return a search depth for the current position. */
    private int chooseDepthWhtie() {
        int validMoves = validMoves();
        if (validMoves >= 80) {
            return 3;
        }
        else if (validMoves >= 30) {
            return 4;
        }
        else {
            return 5;
        }
    }

    /** Return a search depth for the current position. */
    private int chooseDepthBlack() {
        int validMoves = validMoves();
        if (validMoves >= 80) {
            return 3;
        }
        else if (validMoves >= 30) {
            return 4;
        }
        else {
            return 5;
        }
    }

    /** Return a search depth for the current position. */
    private int chooseDepth() {

        return 3;
    }


    static HashMap<String, Double> WhiteMap = new HashMap<>();
    static HashMap<String, Double> BlackMap = new HashMap<>();

//    static HashMap<String, Double> WhiteMap = Utils2.readObject(WMap, HashMap.class);
//    static HashMap<String, Double> BlackMap = Utils2.readObject(BMap, HashMap.class);


}
