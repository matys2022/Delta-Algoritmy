using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Runtime.InteropServices;
using System.Threading.Tasks;
using NodesImplementation.Diary.NavigationMenu;
using NodesImplementation.Diary.ShortCuts;
using NodesImplementation.Structures;

namespace NodesImplementation.Diary
{


    public class DiaryUI
    {

        private char?[,] createNoteSelector(LinkedListParent<DiarySheet>[] diarySheets, decimal availableWidth, decimal availableHeight)
        {
            char?[,] helpRows = new char?[ (int)availableWidth, (int)availableWidth];


            

            for(int i = 0; i < availableHeight; i++)
            {
                for(int j = 0; j < availableWidth; j++)
                {
                    
                }
            }
        }

        private char?[,] readNoteContent(DiarySheet sheet, decimal availableWidth, decimal availableHeight)
        {
            char?[,] helpRows = new char?[ (int)availableWidth, (int)availableWidth];
            
        }

        private char?[,] createStringDialog(string heading, decimal availableWidth, decimal availableHeight, int sizeX, int sizeY, string input, out string dialogInput)
        {
            char?[,] helpRows = new char?[ (int)availableWidth, (int)availableWidth];

            int startX = (int)Math.Floor((availableWidth-sizeX)/2);
            int startY = (int)Math.Floor((availableHeight-sizeY)/2);

            for(int i = 0; i < availableHeight; i++)
            {
                for(int j = 0; j < availableWidth; j++)
                {
                    if((i<startY && j<startX) || (i>startY+sizeY && j < startX + sizeX))
                    {
                        helpRows[i,j] = null;
                    }
                    else if((i==startY || i==startY+sizeY))
                    {
                        helpRows[i,j] = '—';
                    }else if ((j==startX || j == startX + sizeX))
                    {
                        helpRows[i,j] = '|';
                    }else if ((i>startY && j>startX) && (i<startY+sizeY && j < startX + sizeX))
                    {
                        if(i == startY + 3)
                        {
                            helpRows[i,j] = '—';
                        }else
                        if(i < startY + 2)
                        {
                            
                            foreach(char chr in heading)
                            {
                                // Will crash in case of long heading
                                helpRows[i, j++] = chr;
                            }
                        }
                        else
                        {
                            helpRows[i,j] = '█';
                        }
                    }
                    
                }
            }

        }

        private char?[,] createCheckDialog(string heading, decimal availableWidth, decimal availableHeight, int sizeX, int sizeY, out bool approved)
        {
            char?[,] helpRows = new char?[ (int)availableWidth, (int)availableWidth];
        }

        private char?[,] createHelp(NavigationMenuItem[] navigationMenuItems, decimal availableWidth, decimal availableHeight)
        {
            char?[,] helpRows = new char?[ (int)availableWidth, (int)availableWidth];
            
            int rowIx = 0;
            int colIx = 0;

            for(int i = 0; i < navigationMenuItems.Length; i++)
            {
                string field = $"'{navigationMenuItems[i].shortCut.keyCombination}' => {navigationMenuItems[i].hint}";
                
                if(field.Length <= availableWidth){    

                    for(int j = 0; j < field.Length; j++)
                    {
                        helpRows[rowIx, colIx] = field[j];
                        colIx++;
                    }
                }else{
                    throw new Exception("Not enough space for hint to be viewed");
                }
                
                if(navigationMenuItems.Length > i + 1)
                {
                    if($"'{navigationMenuItems[i+1].shortCut.keyCombination}' => {navigationMenuItems[i+1].hint}".Length > (availableWidth - colIx - 1 - 3*(i-1)))
                    {
                        rowIx++;
                        colIx = 0;
                        continue;
                    }
                }
                if(availableHeight <= rowIx)
                {
                    throw new Exception("Not enough space for all hints to be viewed");
                }

                helpRows[rowIx, colIx+1] = ' ';
                helpRows[rowIx, colIx+2] = '|';
                helpRows[rowIx, colIx+3] = ' ';
                colIx+=3;

                

            }

            return helpRows;

        }



        public void renderMenu()
        {
            decimal margin = 6;

            char?[,] matrix = new char?[Console.WindowHeight, Console.WindowWidth];
            
            Console.WriteLine(Console.WindowHeight + " X " + Console.WindowWidth);


            decimal topBottomWidth = Math.Ceiling(Decimal.Parse((margin/2).ToString()));


            for(int i = 0; i < Console.WindowHeight; i++)
            {
                for(int j = 0; j < Console.WindowWidth; j++)
                {
                    if(i - topBottomWidth < 0 || j - margin < 0 || j > Console.WindowWidth - 1 - margin)
                    {
                        matrix[i,j] = j%2 == 0 ? '█':' ';    
                    }
                    if(i == Console.WindowHeight - 1 - topBottomWidth){
                        
                        matrix[i,j] = '—';
                    }

                    if(i > Console.WindowHeight - 1 - topBottomWidth && j > margin - 1 && j < Console.WindowWidth - 1 - margin)
                    {

                        ShortCut quitShortCut = new ShortCut("Q", ()=>{});
                        ShortCut newNoteShortCut = new ShortCut("N", ()=>{});
                        ShortCut removeNoteShortCut = new ShortCut("R", ()=>{});
                        ShortCut moveUpShortCut = new ShortCut("⌃", ()=>{});
                        ShortCut moveDownShortCut = new ShortCut("⌄", ()=>{});


                        NavigationMenuItem[] navigationMenuItems = {
                            new NavigationMenuItem(quitShortCut, "Quit"), 
                            new NavigationMenuItem(newNoteShortCut, "New record"), 
                            new NavigationMenuItem(removeNoteShortCut, "remove selected record"), 
                            new NavigationMenuItem(moveUpShortCut, "Select previous record"), 
                            new NavigationMenuItem(moveDownShortCut, "New next record")
                        };

                        char?[,] help = createHelp(navigationMenuItems, Console.WindowWidth - 2 * margin, Console.WindowHeight - topBottomWidth);

                        matrix[i,j] = '#';


                        int x = (int)(i - (Console.WindowHeight - topBottomWidth));
                        int y = (int)(j - margin);

                        char? value = help[x,y ];

                        if (value.HasValue)
                        {
                            matrix[i,j] = value;
                        }


                    }

                    if(i - topBottomWidth + 1  > 0 && j + 1 - margin > 0 && j < Console.WindowWidth - 1 - margin && i < Console.WindowHeight - 1 - topBottomWidth)
                    {


                        matrix[i,j] = 'C';



                    }
                }
            }
            

            render(matrix);
        }
        private void render(char?[,] matrix)
        {
            for(int i = 0; i < matrix.GetLength(0); i++)
            {
                for(int j = 0; j < matrix.GetLength(1); j++)
                {
                    if(matrix[i,j] != null)
                    {
                        Console.Write(matrix[i,j]);
                    }
                    else
                    {
                        Console.Write(" ");
                    }
                }
                if(i != matrix.GetLength(0)-1)
                Console.WriteLine();
            }
        }
    }
}