/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;
import java.util.TreeMap;

import static loa.Piece.*;

/** An automated Player.
 *  @author Ziyuan Tang
 */
class MachinePlayer extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;
    /** Used to convey moves discovered by findMove. */
    private static Move _foundMove;

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
        getGame().reportMove(choice);
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
                        for (Move m : board.legalMoves(sqnow)) {
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
                        for (Move m : board.legalMoves(sqnow)) {
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
            return board.contiScore(BP);
        } else {
            return board.contiScore(WP);
        }
    }


    /**return _foundMove. */
    static Move getfoundMove() {
        return _foundMove;
    }
    /** Return a search depth for the current position. */
    private int chooseDepth() {
        return 2;
    }



}
