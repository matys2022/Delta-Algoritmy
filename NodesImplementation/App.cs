using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Threading.Tasks;
using NodesImplementation.Diary.ShortCuts;
using NodesImplementation.DiaryStructs;
using NodesImplementation.Structures;


namespace NodesImplementation
{
    public class App
    {

        private DiaryStructs.Diary diary;
        public App()
        {
            this.diary = new DiaryStructs.Diary();
        }
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

            List<DiarySheet> diarySheets = new List<DiarySheet>();

            ShortCut shortCutPrevious = new ShortCut("predchozi", ()=>{});
            ShortCut shortCutNext = new ShortCut("dalsi", ()=>{});
            ShortCut shortCutNew = new ShortCut("novy", ()=>{});
            ShortCut shortCutSave = new ShortCut("uloz", ()=>{});
            ShortCut shortCutDelete = new ShortCut("smaz", ()=>{});
            ShortCut shortCutClose = new ShortCut("zavri", ()=>{});


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
                Console.WriteLine($"Počet záznamů: {diarySheets.Count}");
                Console.Write("Napiš příkaz: ");

                string? command = Console.ReadLine();

                for(int i = 0; i < shortCuts.Length-1; i++) 
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