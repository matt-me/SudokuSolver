package Solver;
import java.util.Arrays;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
public class SudokuSolver extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Sudoku Solver"); // sets the text for the window
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        Text scenetitle = new Text("Sudoku Solver");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 40));
        grid.add(scenetitle, 2, 0, 9, 1);
        TextField sudoku[][] = new TextField[9][9];
        // the code below adds all of the squares from the gui form to an array to pass to the solve() method
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                TextField sudokuSquare = new TextField();
                sudoku[i][j] = sudokuSquare;
                grid.add(sudokuSquare, i + 1, j + 1, 1, 1);
            }
        }
        Button solve = new Button("Solve");
        grid.add(solve, 4, 10, 3, 1);
        // code for the solve button that solves the board
        solve.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                int grid[][] = new int[9][9];
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        if (sudoku[i][j].getText().length() == 0)
                            sudoku[i][j].setText("0");
                        grid[i][j] = Integer.parseInt(sudoku[i][j].getText());
                        
                    }
                }
                boolean couldSolve = solve(grid, 0, 0);
                for (int i = 0; i < 9; i++) {
                    for (int j = 0; j < 9; j++) {
                        sudoku[i][j].setText(grid[i][j] + "");
                    }
                }
                if (!couldSolve) {
                    Alert alert = new Alert(AlertType.INFORMATION);
                    alert.setTitle("Failure");
                    alert.setHeaderText("Solution Failure");
                    alert.setContentText("The program could not find a solution to the given problem.");
                    alert.showAndWait();
                }
            }
        });
        Button clear = new Button("Clear");
        clear.setOnAction(new EventHandler<ActionEvent>() {
           @Override public void handle(ActionEvent e) {
               for (int i = 0; i < 9; i++) {
                   for (int j = 0; j < 9; j++) {
                       sudoku[i][j].setText("");
                   }
               }
           } 
        });
        grid.add(clear, 6, 10, 3, 1);
        //finish constructing the window
        Scene scene = new Scene(grid, 425, 450);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * Takes a sudoku board (2d array) and changes the array into a solved form. Spaces should be represented as 0s.
     * Uses brute force
     * @param board - the board as a 2d array
     * @param x - the x position that the algorithm should start at (an int)
     * @param y - the y position that the algorithm should start at (an int)
     * @return true if a solution is possible. false otherwise.
     */
    public static boolean solve(int board[][], int x, int y) {
        while (y != 9 && board[x][y] != 0) { // if the square we are looking at is already filled out
            if (!Arrays.toString(getPossibilities(x, y, board)).contains(Integer.toString(board[x][y]))) {
                return false;
            }
            x = (x + 1) % 9;
            y = (x == 0) ? y + 1: y;
        }
        if (y == 9)
            return true;
        // set the variables for the next square
        int newx = (x + 1) % 9;
        int newy = (newx == 0) ? y + 1: y;
        while (newy != 9 && board[newx][newy] != 0) { // if the next square is already filled out
            newx = (newx + 1) % 9;
            newy = (newx == 0) ? newy + 1: newy;
        }
        int possible[] = getPossibilities(x, y, board); // get the possibilities of the square
        for (int k = 0; k < possible.length; k++) {
            board[x][y] = possible[k]; // all of the current possibilities
            if (!solve(board, newx, newy)) {
                board[x][y] = 0;
            } else {
                return true;
            }
        }
        return false;
    }
    /**
     * Shows part of a 2d array, printed as output
     * Example: "show(0, 0, 9, 9, board)" is enough to show the entire sudoku board
     * @param x1 the left bound of the array to be shown on the x axis
     * @param x2 the right bound of the array to be shown on the x axis
     * @param y1 the upper bound of the array to be shown on the y axis
     * @param y2 the lower bound of the array to be shown on the y axis
     * @param board the sudoku board, represented as a 2d array
     */
    public static void show(int x1, int x2, int y1, int y2, int board[][]) {
        for (int i = y1; i < y2; i++) {
            for (int j = x1; j < x2; j++) {
                System.out.print(board[i][j] + "  ");
            }
            System.out.println();
        }
    }
    /**
     * Gets the possibilities for a single square
     * @param y - the y position of the square
     * @param x - the x position of the square
     * @param board - the sudoku board, (a 2d array of ints)
     * @return an array of ints showing the possible numbers for a square in a sudoku board
     */
    private static int[] getPossibilities(int y, int x, int board[][]) {
        String possibilities = "123456789";
        int row[] = board[y];
        int column[] = getColumn(x, board);
        int quadrant[] = getQuadrant(x/3, y/3, board);
        for (int i = 0; i < column.length; i++) { // iterating through the column/row/quadrant
            if (possibilities.contains(column[i] + "")) {
                possibilities = possibilities.replace(column[i] + "", "");
            }
            if (possibilities.contains(row[i] + "")) {
                possibilities = possibilities.replace(row[i] + "", "");
            } 
            if (possibilities.contains(quadrant[i] + "")) {
                possibilities = possibilities.replace(quadrant[i] + "", "");
            }
        }
        if (board[x][y] != 0)
            possibilities = possibilities.concat(board[x][y] + "");
        int possibleInts[] = new int[possibilities.length()];
        for (int i = 0; i < possibilities.length(); i++) {
            possibleInts[i] = Integer.parseInt(Character.toString((possibilities.charAt(i))));
        }
        return possibleInts;
    }

    /**
     * This function takes a single square in a sukoku board and gives the 3x3 'area' it is in, where every square in the 'area' must be unique
     * @param x - the x position of the square
     * @param y - the y position of the square
     * @param board - the sudoku board, a 2d array
     * @return - an array of ints, length 9. The first three entries in the array are the top 3 entries in the 3x3 area.
     */
    private static int[] getQuadrant(int x, int y, int board[][]) {
        int trueX = x * 3;
        int trueY = y * 3;
        int quadrant[] = new int[9];
        if (trueX > board[0].length || trueX < 0 || trueY > board.length || trueY < 0) {
            /*System.out.println("Invalid quadrant")*/;
        } else {
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    quadrant[i*3 + j] = board[trueY + i][trueX + j];
                }
            }
        }
        return quadrant;
    }

    /**
     * returns the column of spaces that share the same given x
     * @param x - the x value of the column
     * @param board - a sudoku board, formatted as a 2d array
     * @return an array of ints representing the column
     */
    private static int[] getColumn(int x, int board[][]) {
        int column[] = new int[9];
        for (int i = 0; i < 9; i++) {
            column[i] = board[i][x];
        }
        return column;
    }
}

