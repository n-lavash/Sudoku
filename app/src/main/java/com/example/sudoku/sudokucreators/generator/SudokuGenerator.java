package com.example.sudoku.sudokucreators.generator;

public class SudokuGenerator {
    int[][] mat;
    int N; // число столбцов/колонок
    int SRN; // квадратный корень из N
    int K; // количество пропущенных чисел

    public int[][] getMat() {
        return mat;
    }

    // Constructor
    public SudokuGenerator(int N, int K) {
        this.N = N;
        this.K = K;

        // считаем корень из N
        double SRNd = Math.sqrt(N);
        SRN = (int) SRNd;

        mat = new int[N][N];
    }

    // заполняем значения
    public void fillValues() {
        // заполняем дигональные подматрицы
        fillDiagonal();

        // заполняем оставшиеся блоки
        fillRemaining(0, /*j: */ SRN);

        // удаляем рандомные K ячеек, чтобы начать игру
        removeKDigits();
    }

    // заполняем 3 (корень из 9) матриц 3х3
    void fillDiagonal() {
        for (int i = 0; i < N; i = i + SRN)
            // для диагональных матриц начальные координаты: i == j
            fillBox(i, i);
    }

    // возвращает false если блок содержит число num
    boolean unUsedInBox(int rowStart, int colStart, int num) {
        for (int i = 0; i < SRN; i++)
            for (int j = 0; j < SRN; j++)
                if (mat[rowStart + i][colStart + j] == num)
                    return false;
        return true;
    }

    // заполняем ячейку
    void fillBox(int row, int col) {
        int num;
        for (int i = 0; i < SRN; i++) { // цикл до 3
            for (int j = 0; j < SRN; j++) { // цикл до 3
                do {
                    num = randomGenerator(N); // в переменную записываем рандомное число от 1 до 9
                }
                while (!unUsedInBox(row, col, num)); // выполняем пока блок не содержит число num

                mat[row + i][col + j] = num; // в матрицу с заданной в параметрах + вычесленной по циклу позицией записываем число
            }
        }
    }

    // генерируем число от 1 до num
    int randomGenerator(int num) {
        return (int) Math.floor((Math.random() * num + 1));
    }

    // проверка, можно ли помещать число в ячейку
    boolean CheckIfSafe(int i, int j, int num) {
        return (unUsedInRow(i, num) &&
                unUsedInCol(j, num) &&
                unUsedInBox(i - i % SRN, j - j % SRN, num));
    }

    // проверка по строке
    boolean unUsedInRow(int i, int num) {
        for (int j = 0; j < N; j++)
            if (mat[i][j] == num)
                return false;
        return true;
    }

    // проверка по столбцу
    boolean unUsedInCol(int j, int num) {
        for (int i = 0; i < N; i++)
            if (mat[i][j] == num)
                return false;
        return true;
    }

    // рекурсивно заполяем оставшиеся ячейки
    boolean fillRemaining(int i /*0*/, int j /*3*/) {
        // если номер столбца >= N и номер строки < N - 1
        // номер столбца делаем равным 0, а номер строки увеличиваем на 1
        if (j >= N && i < N - 1) {
            i = i + 1;
            j = 0;
        }
        if (i >= N && j >= N)
            return true;

        if (i < SRN) { // если i < 3
            if (j < SRN) // j < 3
                j = SRN;
        } else if (i < N - SRN) {
            if (j == (int) (i / SRN) * SRN)
                j = j + SRN;
        } else {
            if (j == N - SRN) {
                i = i + 1;
                j = 0;
                if (i >= N)
                    return true;
            }
        }

        for (int num = 1; num <= N; num++) {
            if (CheckIfSafe(i, j, num)) {
                mat[i][j] = num;
                if (fillRemaining(i, j + 1))
                    return true;

                mat[i][j] = 0;
            }
        }
        return false;
    }

    public void removeKDigits() {
        int count = K;
        while (count != 0) {
            int cellId = randomGenerator(N * N) - 1;
            int i = (cellId / N);
            int j = cellId % 9;
            if (j != 0)
                j = j - 1;

            if (mat[i][j] != 0) {
                count--;
                mat[i][j] = 0;
            }
        }
    }

    public void printSudoku() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                System.out.print(mat[i][j] + " ");
            System.out.println();
        }
        System.out.println();
    }
}
