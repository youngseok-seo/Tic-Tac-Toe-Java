import java.util.Random;
import java.util.Scanner;

class TTT{

	static Scanner input = new Scanner(System.in);

	public static void main(String [] args){

		int[] T;
		int Xmove;
		int state;
		int winmove;
		int nonlosemove;
		int randmove;

		T = genBoard();

		while (true){

			//Player "X" (user)

			System.out.println(printBoard(T));
			System.out.println("Player X, where would you like to play?");

			//take in user input, check if the indicated slot is valid

			while (true){

				Xmove = input.nextInt();

				if ((Xmove >= 0) && (Xmove <= 8)){
					if (T[Xmove] == 0){
						break;
					}
					else{
						System.out.println("Invalid entry: The slot is already taken.");
						System.out.println("Please enter a new number:");
					}
				}
				else{
					System.out.println("Invalid entry: The number you entered is out of range");
					System.out.println("Please enter a valid number from 0 to 8:");
				}
			}

			//place X in the indicated slot

			T[Xmove] = 1;

			//analyze the game state after X move

			state = analyzeBoard(T);

			if (state == 1){
				System.out.println(printBoard(T));
				System.out.println("Player X wins!");
				break;
			}

			if (state == 3){
				System.out.println(printBoard(T));
				System.out.println("Draw");
				break;
			}

			//Player "O" (computer)

			System.out.println(printBoard(T));
			System.out.println("Player O:");

			//place O in the best spot as analyzed by the simulation functions

			while (true){

				winmove = genWinningMove(T,2);
				if (winmove != -1){
					T[winmove] = 2;
					break;
				}

				nonlosemove = genNonLosingMove(T,2);
				if (nonlosemove != -1){
					T[nonlosemove] = 2;
					break;
				}

				randmove = genRandomMove(T);
				if (T[randmove] == 0){
					T[randmove] = 2;
					break;
				}
			} 

			//analyze the game state after O move

			state = analyzeBoard(T);

			if (state == 2){
				System.out.println(printBoard(T));
				System.out.println("Player O wins!");
				break;
			}

			if (state == 3){
				System.out.println(printBoard(T));
				System.out.println("Draw");
				break;
			}
		}	
	
	}

	public static int errorCheck(int[] T){

		//Helper function #1: check for errors

		if (T == null){
			return -1;
		}
		if (T.length != 9){
			return -1;
		}	
		for (int i : T){
			if ((i != 0) && (i != 1) && (i != 2)){
				return -1;
			}
		}
		return 0;	
	}

	public static int[] genBoard(){

		//create the behind-the-scenes board

		int[] T;
		T = new int[] {0,0,0,0,0,0,0,0,0};

		return T;
	}

	public static int printBoard(int[] T){

		int i;
		char[] board;

		//check for errors

		if (errorCheck(T) == -1){
			return -1;
		}

		//create board shown to user

		board = new char[] {'0','1','2','3','4','5','6','7','8'};

		//insert 'X' or 'O' into user-assigned slot

		for (i = 0; i < board.length; i++){
			if (T[i] == 1){
				board[i] = 'X';
			}
			else if (T[i] == 2){
				board[i] = 'O';
			}
		}

		//print new board



		System.out.println(" " + board[0] + " | " + board[1] + " | " + board[2] + " ");
		System.out.println("---|---|---");
		System.out.println(" " + board[3] + " | " + board[4] + " | " + board[5] + " ");
		System.out.println("---|---|---");
		System.out.println(" " + board[6] + " | " + board[7] + " | " + board[8] + " ");

		return 0;
	}

	public static int analyzeBoard(int[] T){

		//return 0 for In-play
		//return 1 for "X" wins
		//return 2 for "O" wins
		//return 3 for Draw
		//return -1 for ERROR

		int i;
		int row;
		int col;
		int diag1;
		int diag2;

		//check for errors

		if (errorCheck(T) == -1){
			return -1;
		}

		//analyze rows

		for (i = 0; i < 7; i = i + 3){
			row = (T[i] * T[i+1] * T[i+2]);
			if (row == 1){
				return 1;
			}
			if (row == 8){
				return 2;
			}
		}

		//analyze columns

		for (i = 0; i < 3; i = i + 1){
			col = (T[i] * T[i+3] * T[i+6]);
			if (col == 1){
				return 1;
			}
			if (col == 8){
				return 2;
			}
		}

		//analyze diagonals

		diag1 = T[0] * T[4] * T[8];
		diag2 = T[2] * T[4] * T[6];
		if ((diag1 == 1)||(diag2 == 1)){
			return 1;
		}
		else if ((diag1 == 8)||(diag2 == 8)){
			return 2;
		}

		//check if the game is in play

		for (i = 0; i < T.length; i++){
			if (T[i] == 0){
				return 0;
			}
		}

		//if the above conditions are not met, the game is a Draw

		return 3;
	}

	public static int opponent(int player){

		//Helper function #2: return the opponent

		if (player == 1){
			return 2;
		}
		if (player == 2){
			return 1;
		}

		return -1;
	}

	public static int[] copyList(int[] T){

		//Helper function #3: copy a list

		int i;
		int[] copyT;
		copyT = new int[9];

		for (i = 0; i < T.length; i++){
			copyT[i] = T[i];
		}

		return copyT;
	}

	public static int genWinningMove(int[] T, int player){

		//check for errors

		if (errorCheck(T) == -1){
			return -1;
		}

		//generate an offensive move that will win the game
		//return -1 for ERROR

		int i;
		int[] copyT;

		for (i = 0; i < T.length; i++){

			copyT = copyList(T);
			if (copyT[i] == 0){
				copyT[i] = player;
				if (analyzeBoard(copyT) == player){
					return i;
				}
			}

		}

		return -1;
	}

	public static int genNonLosingMove(int[] T, int player){

		//generate a defensive move that will prevent the opponent from winning

		return genWinningMove(T,opponent(player));
	}

	public static int genOpenMove(int[] T){

		//check for errors

		if (errorCheck(T) == -1){
			return -1;
		}

		//generate the first open slot
		//return -1 for ERROR

		int i;

		for (i = 0; i < T.length; i++){
			if (T[i] == 0){
				return i;
			}
		}

		return -1;
	}

	public static int genRandomMove(int[] T){

		//check for errors

		if (errorCheck(T) == -1){
			return -1;
		}

		//check if there is an empty slot
		//return -1 for ERROR

		boolean empty;
		int i;
		int randmove;
		Random randint = new Random();

		empty = false;
		for (i = 0; i < T.length; i++){
			if (T[i] == 0){
				empty = true;
				break;
			}
		}
		if (empty == false){
			return -1;
		}

		//generate a random move
		//use recursion if necessary

		randmove = randint.nextInt(9);
		if (T[randmove] == 0){
			return randmove;
		}
		else{
			return genRandomMove(T);
		}
	}
}