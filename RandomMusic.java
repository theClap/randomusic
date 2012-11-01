//This program takes a key and randomly generates
//a diatonic chorale

import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

public class RandomMusic {
    public static String tonic, superTonic, median, subDominant, dominant, subMedian, leadingTone;
    
    public static void main(String[]args) {
        int measure=0, chord, keyLength;	
        String keyString="",keyStringChar="";
        char keyChar=0, accidental=0;
		
	Scanner scan = new Scanner(System.in);
	Random generator = new Random();
	
        while(measure<=0) {
            try {
                System.out.println("How many measures of music?I suggest 4 as a good starting point: ");
                measure = scan.nextInt();
            } catch(InputMismatchException e) {
                System.out.println("Try again.");
            }
            scan.nextLine(); 
        }
        
	while(keyChar <65 || keyChar >71) {	
            System.out.println("What key would you like? C is a good place to start: ");
            keyString = scan.nextLine();
            
            keyStringChar=keyString.toUpperCase();
            keyChar = keyStringChar.charAt(0);
        }
	chord = measure * 4;                                                // 4/4 time signature 
		
	int choir [][] = new int [4][chord];                                //numeral representation
	String choirStringArray[][] = new String [4][chord];                //alphabetical representation
							
	keyLength = keyString.length();                                     //check if is #/b	
	//if natural (not #/b)
	if(keyLength == 1) {
            keySwitchNat(keyChar);		
	}                                                               
        else {                                                              //else sends to identical function to handle sharp/flat
            accidental = keyString.charAt(1);
            keySwitchAcc(keyChar, choir, accidental);
	}
		
	for(int i=0;i<chord;i++) {
            choir[0][i]= generator.nextInt(7)+1;                            //well, its not a melody
            choir[3][i]= generator.nextInt(7)+1;                            //without some homophony 
	}
        
	choir=theory(choir,chord);
        //number representation
        printString(choir,chord);
	System.out.println("");
        //note representation of SATB
	choirStringArray=toStringArray(choir,chord);
        
		
	for(int x=0;x<4;++x) {
            for(int y=0;y<chord;++y) {
		System.out.printf("%-4s",choirStringArray[x][y]);		
            }				
            System.out.println("");	
        }		
    }
        
    public static String[][] toStringArray(int[][] choir,int chord) {
        String choirStringArray[][] = new String[4][chord];
        for (int x = 0; x < 4; x++) {
            for (int col = 0; col < chord; col++) {
                switch (choir[x][col]) {
                    case 1:
                        choirStringArray[x][col] = tonic;
                        break;
                    case 2:
                        choirStringArray[x][col] = superTonic;
                        break;
                    case 3:
                        choirStringArray[x][col] = median;
                        break;
                    case 4:
                        choirStringArray[x][col] = subDominant;
                        break;
                    case 5:
                        choirStringArray[x][col] = dominant;
                        break;
                    case 6:
                        choirStringArray[x][col] = subMedian;
                        break;
                    case 7:
                        choirStringArray[x][col] = leadingTone;
                        break;
                }
            }
        }
        return choirStringArray;
    }

    public static void printString(int[][] choir, int chord) {
        for (int x = 0; x < 4; ++x) {
            for (int y = 0; y < chord; ++y) {
                System.out.printf("%-4s", choir[x][y]);
            }
            System.out.println("");
        }
    }

    public static int[][] theory(int[][] choir, int chord) {
        boolean isFifth = false, isFourth = false, tiT = false, faT = false, tiA = false, faA = false;
        int alto, tenor, interval;
        /*
         * This loop is the meat of the algorithm. Here we apply on the basic
         * rules of 17th century harmony that still apply to this day. However
         * all art is subjective, these laws/rules applied make up the basic
         * doctrine that has shaped music for over 400 years The laws being: no
         * succession of the intervals parallel 5th, 4th, or Unison/Octave(i.e.
         * Same Note) and the Rules being: Notes that have an adjacent interval
         * of a minor 2nd will resolve immediately to their most adjacent
         * diatonic tone. e.g. Fa-Mi,Ti-Do and eventually Ra-Do, Fi-Sol, Le-Sol,
         * Te-La*
         */
        for (int j = 0; j < chord; j++) {
            do {
                /*
                 * This section of conditionals handles the common law of
                 * tension and release which is the basic principal of all
                 * music. your tension notes in a diatonic major scale are
                 */
                if (faA) {
                    alto = 3;					//fa-mi    
                } else if (tiA) {
                    alto = 1;					//ti-do
                } else {
                    alto = getAlto();                           //get a number 1-7
                }
                if (faT) {
                    tenor = 3;
                } else if (tiT) {
                    tenor = 1;
                } else {
                    tenor = getTenor();                         //get a number 1-7		        		
                }
                //this if-else if-else statement is due to the octal set of numbers in a scale, 1-7
                //states if "Do" is in between the alto and tenor note or else if not or else they're equal 
                if (alto < tenor) {
                    interval = Math.abs((alto + 8) - tenor);
                    //System.out.println("if: alto: "+alto+" tenor: "+tenor+" interval: "+interval);    
                } else if (alto > tenor) {
                    interval = Math.abs(alto - (tenor - 1));
                    //System.out.println("else: alto: "+alto+" tenor: "+tenor+" interval: "+interval);
                } else {
                    interval = 0;
                }
                //System.out.print("Test: " +interval+" ");	

            } while (interval > 6 || interval < 1 || alto == tenor || (isFifth && interval == 5) || (interval == 4 && isFourth));

            /*
             * String of logic behind 17/18th century music theory
             */
            if (alto == 4) {
                faA = true;
                tiA = false;
                //System.out.print("alto = "+alto+" ");
            } else if (alto == 7) {
                //System.out.print("alto = "+alto+" ");
                tiA = true;
                faA = false;
            } else {
                tiA = false;
                faA = false;
            }

            if (tenor == 4) {
                //System.out.print("tenor = "+tenor+" ");
                faT = true;
                tiT = false;
            } else if (tenor == 7) {
                //System.out.print("tenor = "+tenor+" ");
                tiT = true;
                faT = false;
            } else {
                tiT = false;
                faT = false;
            }

            if (interval == 5) {//if one is true the other has to be false
                isFifth = true;	
                isFourth = false;
            } else if (interval == 4) {
                isFourth = true;
                isFifth = false;
            } else {//deflag both
                isFifth = false;
                isFourth = false;
            }

            choir[1][j] = alto;
            choir[2][j] = tenor;
        }
        return choir;
    }

    public static int getTenor() {
        int t;
        t = 1 + (int) (Math.random() * ((7 - 1) + 1));
        return t;
    }

    public static int getAlto() {
        int a;
        a = 1 + (int) (Math.random() * ((7 - 1) + 1));
        return a;
    }
        
        /*THE REST OF THIS IS ONLY SWITCH-STATEMENT SUBROUTINES*/
        
	/* This function takes the natural key and assigns the proper notes*/
	public static void keySwitchNat(char keyLetter)
	{	
		switch(keyLetter)
		{
			case 'A':
				tonic = "A";
				superTonic = "B";
				median = "C#";
				subDominant = "D";
				dominant = "E";
				subMedian = "F#";
				leadingTone = "G#";
				break;
			case 'B':
				tonic = "B";
				superTonic = "C#";
				median = "D#";
				subDominant = "E";
				dominant = "F#";
				subMedian = "G#";
				leadingTone = "A#";
				break;
			case 'D':
				tonic = "D";
				superTonic = "E";
				median = "F#";
				subDominant = "G";
				dominant = "A";
				subMedian = "B";
				leadingTone = "C#";
				break;
			case 'E':
				tonic = "E";
				superTonic = "F#";
				median = "G#";
				subDominant = "A";
				dominant = "B";
				subMedian = "C#";
				leadingTone = "D#";
				break;
			case 'F':
				tonic = "F";
				superTonic = "G";
				median = "A";
				subDominant = "Bb";
				dominant = "C";
				subMedian = "D";
				leadingTone = "E";
				break;
			case 'C':
				tonic = "C";
				superTonic = "D";
				median = "E";
				subDominant = "F";
				dominant = "G";
				subMedian = "A";
				leadingTone = "B";
				break;
			case 'G':
				tonic = "G";
				superTonic = "A";
				median = "B";
				subDominant = "C";
				dominant = "D";
				subMedian = "E";
				leadingTone = "F#";	
				break;
		}
	}	
	/* This does the same as the former except handles sharps or flats*/
	public static void keySwitchAcc(char keyLetter, int chorale[][], char accidental)
	{	
		switch(keyLetter)
		{
			case 'A':
				if(accidental=='b')
				{
					tonic = "Ab";
					superTonic = "Bb";
					median = "C";
					subDominant = "Db";
					dominant = "Eb";
					subMedian = "F";
					leadingTone = "G";
				}
				else if(accidental=='#')
				{
					tonic = "A#";
					superTonic = "B#";
					median = "C*";
					subDominant = "D*";
					dominant = "E#";
					subMedian = "F*";
					leadingTone = "G*";
				}
				break;
			case 'B':
				if(accidental=='b')
				{
					tonic = "Bb";
					superTonic = "C";
					median = "D";
					subDominant = "Eb";
					dominant = "F";
					subMedian = "G";
					leadingTone = "A";
				}
				else if(accidental=='#')
				{
					tonic = "B#";
					superTonic = "C*";
					median = "D*";
					subDominant = "E#";
					dominant = "F*";
					subMedian = "G*";
					leadingTone = "A*";
				}
				break;
			case 'C':
				if(accidental=='b')
				{
					tonic = "Cb";
					superTonic = "Db";
					median = "Eb";
					subDominant = "Fb";
					dominant = "Gb";
					subMedian = "Ab";
					leadingTone = "Bb";
				}
				else if(accidental=='#')
				{
					tonic = "C#";
					superTonic = "D#";
					median = "E#";
					subDominant = "F#";
					dominant = "G#";
					subMedian = "A#";
					leadingTone = "B#";
				}
				break;
			case 'D':
				if(accidental=='b')
				{
					tonic = "Db";
					superTonic = "Eb";
					median = "F";
					subDominant = "Gb";
					dominant = "Ab";
					subMedian = "Bb";
					leadingTone = "C";
				}
				else if(accidental=='#')
				{
					tonic = "D#";
					superTonic = "E#";
					median = "F*";
					subDominant = "G#";
					dominant = "A#";
					subMedian = "B#";
					leadingTone = "C*";
				}
				break;
			case 'E':
				if(accidental=='b')
				{
					tonic = "Eb";
					superTonic = "F";
					median = "G";
					subDominant = "Ab";
					dominant = "Bb";
					subMedian = "C";
					leadingTone = "D";
				}
				else if(accidental=='#')
				{
					tonic = "E#";
					superTonic = "F*";
					median = "G*";
					subDominant = "A#";
					dominant = "B#";
					subMedian = "C*";
					leadingTone = "D*";
				}
				break;
			case 'F':
				if(accidental=='b')
				{
					tonic = "Fb";
					superTonic = "Gb";
					median = "Ab";
					subDominant = "Bbb";
					dominant = "Cb";
					subMedian = "Db";
					leadingTone = "Eb";
				}
				else if(accidental=='#')
				{
					tonic = "F#";
					superTonic = "G#";
					median = "A#";
					subDominant = "B";
					dominant = "C#";
					subMedian = "D#";
					leadingTone = "E#";
				}
				break;
			case 'G':
				if(accidental=='b')
				{
					tonic = "Gb";
					superTonic = "Ab";
					median = "Bb";
					subDominant = "Cb";
					dominant = "Db";
					subMedian = "Eb";
					leadingTone = "F";
				}
				else if(accidental=='#')
				{
					tonic = "G#";
					superTonic = "A#";
					median = "B#";
					subDominant = "C#";
					dominant = "D#";
					subMedian = "E#";
					leadingTone = "F*";
				}	
				break;
		}
	}
}