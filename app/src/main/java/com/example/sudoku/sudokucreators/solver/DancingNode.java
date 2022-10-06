package com.example.sudoku.sudokucreators.solver;

public class DancingNode {
    DancingNode left, right, top, bottom;
    ColumnNode C;

    DancingNode hookDown(DancingNode node) {
        assert (this.C == node.C);
        node.bottom = this.bottom;
        node.bottom.top = node;
        node.top = this;
        this.bottom = node;
        return node;
    }

    DancingNode hookRight(DancingNode node) {
        node.right = this.right;
        node.right.left = node;
        node.left = this;
        this.right = node;
        return node;
    }

    void unlinkLR() {
        this.left.right = this.right;
        this.right.left = this.left;
    }

    void relinkLR() {
        this.left.right = this.right.left = this;
    }

    void unlinkUD() {
        this.top.bottom = this.bottom;
        this.bottom.top = this.top;
    }

    void relinkUD() {
        this.top.bottom = this.bottom.top = this;
    }

    DancingNode() {
        left = right = top = bottom = this;
    }

    DancingNode(ColumnNode c) {
        this();
        C = c;
    }
}