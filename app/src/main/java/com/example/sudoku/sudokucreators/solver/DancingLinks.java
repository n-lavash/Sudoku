package com.example.sudoku.sudokucreators.solver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DancingLinks {

    private ColumnNode header;
    private List<DancingNode> answer;

    private int[][] solution;

    private void search(int k) {
        if (header.right == header) {
            handleSolution(answer);
        } else {
            ColumnNode c = selectColumnNodeHeuristic();
            c.cover();

            for (DancingNode r = c.bottom; r != c; r = r.bottom) {
                answer.add(r);

                for (DancingNode j = r.right; j != r; j = j.right) {
                    j.C.cover();
                }

                search(k + 1);

                r = answer.remove(answer.size() - 1);
                c = r.C;

                for (DancingNode j = r.left; j != r; j = j.left) {
                    j.C.uncover();
                }
            }
            c.uncover();
        }
    }

    private ColumnNode selectColumnNodeHeuristic() {
        int min = Integer.MAX_VALUE;
        ColumnNode ret = null;
        for (ColumnNode c = (ColumnNode) header.right; c != header; c = (ColumnNode) c.right) {
            if (c.size < min) {
                min = c.size;
                ret = c;
            }
        }
        return ret;
    }

    private ColumnNode makeDLXBoard(boolean[][] grid) {
        final int COLS = grid[0].length;

        ColumnNode headerNode = new ColumnNode("header");
        List<ColumnNode> columnNodes = new ArrayList<>();

        for (int i = 0; i < COLS; i++) {
            ColumnNode n = new ColumnNode(Integer.toString(i));
            columnNodes.add(n);
            headerNode = (ColumnNode) headerNode.hookRight(n);
        }
        headerNode = headerNode.right.C;

        for (boolean[] aGrid : grid) {
            DancingNode prev = null;
            for (int j = 0; j < COLS; j++) {
                if (aGrid[j]) {
                    ColumnNode col = columnNodes.get(j);
                    DancingNode newNode = new DancingNode(col);
                    if (prev == null)
                        prev = newNode;
                    col.top.hookDown(newNode);
                    prev = prev.hookRight(newNode);
                    col.size++;
                }
            }
        }

        headerNode.size = COLS;

        return headerNode;
    }

    private void handleSolution(List<DancingNode> answer) {
        solution = parseBoard(answer);
    }

    DancingLinks(boolean[][] cover) {
        header = makeDLXBoard(cover);
    }

    public void runSolver() {
        answer = new LinkedList<>();
        search(0);
    }

    private int size = 9;

    private int[][] parseBoard(List<DancingNode> answer) {
        int[][] result = new int[size][size];
        for (DancingNode n : answer) {
            DancingNode rcNode = n;
            int min = Integer.parseInt(rcNode.C.name);
            for (DancingNode tmp = n.right; tmp != n; tmp = tmp.right) {
                int val = Integer.parseInt(tmp.C.name);
                if (val < min) {
                    min = val;
                    rcNode = tmp;
                }
            }
            int ans1 = Integer.parseInt(rcNode.C.name);
            int ans2 = Integer.parseInt(rcNode.right.C.name);
            int r = ans1 / size;
            int c = ans1 % size;
            int num = (ans2 % size) + 1;
            result[r][c] = num;
        }
        return result;
    }

    public int[][] getSolution() {
        return solution;
    }
}
