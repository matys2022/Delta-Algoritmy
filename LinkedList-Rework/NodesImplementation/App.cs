using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using NodesImplementation.Diary.ShortCuts;
using NodesImplementation.DiaryStructs;
using NodesImplementation.Structures;
using NodesImplementation.Structures.Generic;


namespace NodesImplementation
{
    public class App
    {

        private DiaryStructs.Diary diary;
        public App()
        {
            this.diary = new DiaryStructs.Diary();
        }

        private (DiarySheet sheet, int index)? selectedSheet;

        private string editedText;
        public void run()
        {

            //Denik se ovládá následujícími příkazy:  
            // predchozi: Přesunutí na předchozí záznam 
            // dalsi: Přesunutí na další záznam 
            // novy: Vytvoření nového záznamu 
            // uloz: Uložení vytvořeného záznamu 
            // smaz: Odstranění záznamu 
            // zavri: Zavření deníku
            // Počet záznamů: 0 Zadej příkaz:

            LinkedListParent<DiarySheet> diarySheets = new LinkedListParent<DiarySheet>();



            ShortCut shortCutPrevious = new ShortCut("predchozi", ()=>{
                if(0 <= selectedSheet?.index - 1)
                    selectedSheet = (diarySheets.ElementAt((int)(selectedSheet?.index - 1)), (int)(selectedSheet?.index - 1));

            });

            ShortCut shortCutNext = new ShortCut("dalsi", () =>
            {
                if(diarySheets.Lenght > selectedSheet?.index + 1 && selectedSheet.HasValue)
                    selectedSheet = (diarySheets.ElementAt((int)(selectedSheet?.index + 1)), (int)(selectedSheet?.index + 1));

            });

            DateOnly dateOnly = new DateOnly();
            ShortCut shortCutNew = new ShortCut("novy", () =>
            {
                Console.Write("Chceš vložit datum (y/n):");
                if(Console.ReadKey().KeyChar == 'y')
                {
                    Console.WriteLine();
                    while (true)
                    {
                        Console.WriteLine("Napis jej ve formatu DD/MM/YYYY");
                        string? dateStr = Console.ReadLine();
                        if(string.IsNullOrWhiteSpace(dateStr))
                        continue;
                        string pattern = "[/]";
                        string[] parts = Regex.Split(dateStr, pattern);
                        
                        if(int.TryParse(parts[2], out int year) && year >= 1)
                        {
                            Console.WriteLine(year);
                            dateOnly = dateOnly.AddYears(year - 1);
                            
                        }
                        else
                        {
                            Console.WriteLine("Unable to parse the year, try again");
                            continue;
                        }

                        if(int.TryParse(parts[1], out int month) && month >= 1 && month <= 12)
                        {
                            Console.WriteLine(month);
                            dateOnly = dateOnly.AddMonths(month - 1);
                        }
                        else
                        {
                            Console.WriteLine("Unable to parse the month, try again");
                            continue;
                        }

                        if(int.TryParse(parts[0], out int day) && day >= 1 && day <= 31)
                        {
                            Console.WriteLine(day);
                            dateOnly = dateOnly.AddDays(day - 1);
                        }
                        else
                        {
                            Console.WriteLine("Unable to parse the day, try again");
                            continue;
                        }

                        Console.WriteLine(dateOnly.ToString() + " Datum");
                        break;
                    }
                }


                Console.Write("Vlož titulek poznámky:\n");
                diarySheets.AddAt(new DiarySheet(Console.ReadLine()??"Untitled note", dateOnly), (selectedSheet?.index??-1) + 1);
                selectedSheet = (diarySheets.ElementAt((selectedSheet?.index??-1) + 1), (selectedSheet?.index ?? -1) + 1);
                Console.Write("Vlož text poznámky:\n");
                
                string line = "";
                while (line.Trim().ToLower() != "uloz")
                {
                    editedText += line;
                    line = Console.ReadLine()+"\n";

                }
                
                diarySheets.ElementAt((int)selectedSheet?.index).SetText(editedText);
                editedText = "";

            });
            ShortCut shortCutSave = new ShortCut("uloz", ()=>{
                

            });
            ShortCut shortCutDelete = new ShortCut("smaz", () =>
            {
                if(selectedSheet.HasValue){
                    diarySheets.RemoveAt(selectedSheet.Value.index);
                    if(diarySheets.Lenght != 0)
                    {
                        // if(selectedSheet.Value.index != diarySheets.Lenght)
                        if(selectedSheet.Value.index != 0)
                            selectedSheet = (diarySheets.ElementAt(selectedSheet.Value.index-1), selectedSheet.Value.index-1);
                        else
                        {
                            selectedSheet = (diarySheets.ElementAt(selectedSheet.Value.index), selectedSheet.Value.index);
                        }
                    }
                    else
                    {
                        Console.WriteLine("Unable to remove any item, since collection doesn't contain any");
                        selectedSheet = null;
                    }
                }
                
            });
            ShortCut shortCutClose = new ShortCut("zavri", ()=>{
                Console.WriteLine("exit"); 
                throw new Exception("Program forced to end");
            });


            LabelShortCut[] shortCuts = [
                new LabelShortCut(shortCutPrevious, "Přesunutí na předchozí záznam "),
                new LabelShortCut(shortCutNext, "Přesunutí na další záznam "),
                new LabelShortCut(shortCutNew, "Vytvoření nového záznamu "),
                new LabelShortCut(shortCutSave, "Uložení vytvořeného záznamu "),
                new LabelShortCut(shortCutDelete, "Odstranění záznamu "),
                new LabelShortCut(shortCutClose, "Zavření deníku "),
            ];

            
            while (true)
            {
                

                Console.Clear();

                diary.renderMenu(
                    shortCuts
                );

                if(selectedSheet.HasValue)
                {
                    diary.renderSheet(
                        selectedSheet.Value.sheet
                    );
                }
                else
                {
                    Console.WriteLine("There are no records yet");
                }
                

                Console.WriteLine($"Počet záznamů: {diarySheets.Lenght}");
                Console.Write("Napiš příkaz: ");

                string? command = Console.ReadLine();

                for(int i = 0; i < shortCuts.Length; i++) 
                {
                    if(command == shortCuts[i].shortCut.keyCombination)
                    {
                        shortCuts[i].shortCut.Action();
                        break;
                    }
                    if(i == shortCuts.Length-1)
                    {
                        Console.WriteLine("Invalid command");
                    }
                }

            }
            

        }
    }
}