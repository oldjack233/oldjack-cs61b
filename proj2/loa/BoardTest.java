/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import org.junit.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.Assert.*;

import static loa.Piece.*;
import static loa.Square.sq;
import static loa.Move.mv;

/** Tests of the Board class API.
 *  @author
 */
public class BoardTest {

    /** A "general" position. */
    static final Piece[][] BOARD1 = {
        { EMP, BP,  EMP,  BP,  BP, EMP, EMP, EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP,  BP,  BP, EMP, WP  },
        { WP,  EMP,  BP, EMP, EMP,  WP, EMP, EMP  },
        { WP,  EMP,  WP,  WP, EMP,  WP, EMP, EMP  },
        { WP,  EMP, EMP, EMP,  BP, EMP, EMP, WP  },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP  },
        { EMP, BP,  BP,  BP,  EMP,  BP,  BP, EMP }
    };

    /** A position in which black, but not white, pieces are contiguous. */
    static final Piece[][] BOARD2 = {
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP,  BP,  WP,  BP,  BP,  BP, EMP, EMP },
        { EMP,  WP,  BP,  WP,  WP, EMP, EMP, EMP },
        { EMP, EMP,  BP,  BP,  WP,  WP, EMP,  WP },
        { EMP,  WP,  WP,  BP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP,  BP, EMP, EMP, EMP, EMP },
    };

    /** A position in which black, but not white, pieces are contiguous. */
    static final Piece[][] BOARD3 = {
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP,  BP,  WP,  BP,  WP, EMP, EMP, EMP },
        { EMP,  WP,  BP,  WP,  WP, EMP, EMP, EMP },
        { EMP, EMP,  BP,  BP,  WP,  WP,  WP, EMP },
        { EMP,  WP,  WP,  WP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
    };

    static final Piece[][] BOARDHHHH = {
            { BP, EMP,  EMP,  EMP,  EMP,  EMP,  BP,  BP },
            { EMP,  EMP, EMP, EMP, EMP, EMP, EMP, EMP  },
            { EMP,  EMP, BP, EMP, EMP, EMP, EMP, EMP  },
            { EMP,  EMP, WP, EMP, WP, EMP, EMP, EMP  },
            { EMP,  EMP, WP, BP, EMP, BP, EMP, EMP  },
            { EMP,  EMP, BP, EMP, EMP, EMP, EMP, BP  },
            { WP,  EMP, WP, EMP, EMP, EMP, EMP, EMP  },
            { EMP, EMP,  WP,  EMP,  EMP,  EMP,  BP,  EMP },

    };

    static final Piece[][] BOARDHH = {
            { EMP, BP,  EMP,  BP,  EMP,  EMP,  EMP,  EMP },
            { EMP,  EMP, EMP, EMP, EMP, WP, EMP, WP  },
            { EMP,  BP, WP, BP, EMP, EMP, BP, EMP  },
            { EMP,  EMP, EMP, EMP, EMP, EMP, EMP, EMP  },
            { EMP,  WP, EMP, WP, EMP, EMP, EMP, WP  },
            { BP,  EMP, EMP, EMP, EMP, WP, BP, EMP  },
            { EMP,  EMP, WP, EMP, EMP, EMP, EMP, WP  },
            { EMP, EMP,  EMP,  EMP,  WP,  EMP,  EMP,  EMP },

    };


    static final String BOARD1_STRING =
        "===\n"
        + "    - b b b - b b - \n"
        + "    - - - - - - - - \n"
        + "    w - - - b - - w \n"
        + "    w - w w - w - - \n"
        + "    w - b - - w - - \n"
        + "    w - - - b b - w \n"
        + "    w - - - - - - w \n"
        + "    - b - b b - - - \n"
        + "Next move: black\n"
        + "===";

    /** Test display */
    @Test
    public void toStringTest() {
        assertEquals(BOARD1_STRING, new Board(BOARD1, BP).toString());
    }

    /** Test legal moves. */
    @Test
    public void testLegality1() {
        Board b = new Board(BOARD1, BP);
        assertTrue("f3-b3", b.isLegal(mv("f3-b3")));
        assertTrue("f3-d5", b.isLegal(mv("f3-d5")));
        assertTrue("f3-h5", b.isLegal(mv("f3-h5")));
        assertTrue("f3-h1", b.isLegal(mv("f3-h1")));
        assertTrue("f3-b3", b.isLegal(mv("f3-b3")));
        assertFalse("f3-d1", b.isLegal(mv("f3-d1")));
        assertFalse("f3-h3", b.isLegal(mv("f3-h3")));
        assertFalse("f3-e4", b.isLegal(mv("f3-e4")));
        assertFalse("c4-c7", b.isLegal(mv("c4-c7")));
        assertFalse("b1-b4", b.isLegal(mv("b1-b4")));
    }

    /** Test contiguity. */
    @Test
    public void testContiguous1() {
        boolean[][] visited = new boolean[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                visited[i][j] = false;
            }
        }
        Board b1 = new Board(BOARD1, BP);
        Square a = sq(3, 5);
        int b = b1.numContig(sq(3, 5), visited,  BP);
        String b22 = new Board(BOARD2,  BP).toString();
        b1.piecesContiguous(BP);

        double sc = b1.contiScore(WP);

        assertFalse("Board 1 black contiguous?", b1.piecesContiguous(BP));
        assertFalse("Board 1 white contiguous?", b1.piecesContiguous(WP));
        assertFalse("Board 1 game over?", b1.gameOver());
        Board b2 = new Board(BOARD2, BP);
        assertTrue("Board 2 black contiguous?", b2.piecesContiguous(BP));
        assertFalse("Board 2 white contiguous?", b2.piecesContiguous(WP));
        assertTrue("Board 2 game over", b2.gameOver());
        Board b3 = new Board(BOARD3, BP);
        assertTrue("Board 3 white contiguous?", b3.piecesContiguous(WP));
        assertTrue("Board 3 black contiguous?", b3.piecesContiguous(BP));
        assertTrue("Board 3 game over", b2.gameOver());
    }

    @Test
    public void testEquals1() {
        Board b1 = new Board(BOARD1, BP);
        Board b2 = new Board(BOARD1, BP);

        assertEquals("Board 1 equals Board 1", b1, b2);
    }

    @Test
    public void testMove1() {
        Board b0 = new Board(BOARD1, BP);
        Board b1 = new Board(BOARD1, BP);
        b1.makeMove(mv("f3-d5"));
        assertEquals("square d5 after f3-d5", BP, b1.get(sq(3, 4)));
        assertEquals("square f3 after f3-d5", EMP, b1.get(sq(5, 2)));
        assertEquals("Check move count for board 1 after one move",
                     1, b1.movesMade());
        b1.retract();
        assertEquals("Check for board 1 restored after retraction", b0, b1);
        assertEquals("Check move count for board 1 after move + retraction",
                     0, b1.movesMade());
    }
    @Test
    public void testLegalMove() {
        Board b0 = new Board(BOARD1, BP);
        List<Move> test = b0.legalMoves(sq(5, 2));
        Square a = sq(5, 4);
        assertEquals("square d5 after f3-d5", test.size(), 4);
    }

    @Test
    public void testFindMove() {
        Board b0 = new Board(Board.INITIAL_PIECES, BP);
        double sc = b0.contiScore(WP);

        double score = MachinePlayer.findMove(b0, 3, true,
                -1, -100000000, 100000000);
        Move m1 = MachinePlayer.getfoundMove();
        Board bh = new Board(BOARDHH, BP);
        double sc2 = bh.contiScore(WP);
        double s3 = bh.contiScore(BP);
        double score2 = MachinePlayer.findMove(bh, 2, true,
                -1, -100000000, 100000000);
        Move m12 = MachinePlayer.getfoundMove();
        System.out.println();

    }

    @Test
    public void testAnnouceWinner() {
        Board bh = new Board(BOARDHHHH, WP);
        Piece winner = bh.winner();
        Board bhh = new Board(BOARDHHHH, BP);
        int [] fib = new int[10];
        int [] fib2 = new int[10];

        HashSet<Integer> a = new HashSet<>();
        a.add(5);

    }
}
