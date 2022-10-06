package com.example.sudoku.sudokucreators.solver;

class ColumnNode extends DancingNode {
    int size;
    String name;

    ColumnNode(String n) {
        super();
        size = 0;
        name = n;
        C = this;
    }

    void cover() {
        unlinkLR();
        for (DancingNode i = this.bottom; i != this; i = i.bottom) {
            for (DancingNode j = i.right; j != i; j = j.right) {
                j.unlinkUD();
                j.C.size--;
            }
        }
    }

    void uncover() {
        for (DancingNode i = this.top; i != this; i = i.top) {
            for (DancingNode j = i.left; j != i; j = j.left) {
                j.C.size++;
                j.relinkUD();
            }
        }
        relinkLR();
    }
}
