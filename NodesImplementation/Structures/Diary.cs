using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NodesImplementation.Structures
{
    public class Diary
    {
        LinkedListParent<DiarySheet> diarySheets;

        public Diary()
        {
            diarySheets = new LinkedListParent<DiarySheet>();
        }
        
        public IEnumerable<string> getSheetTitles()
        {
            List<string> titles = new List<string>(){};

            foreach(DiarySheet sheet in diarySheets)
            {
                titles.Add($"{sheet.GetCreationDate()} | {sheet.SheetTitle}");
            }

            return titles;
        }

        public void createSheet(string sheetTitle, int? index = null)
        {
            if(!index.HasValue){
                diarySheets.AddLast(new DiarySheet(sheetTitle));
            }else{
                diarySheets.AddAt(new DiarySheet(sheetTitle), index??0);
            }
        }

        public void removeSheet(int index)
        {
            diarySheets.RemoveAt(index);
        }
    }
}