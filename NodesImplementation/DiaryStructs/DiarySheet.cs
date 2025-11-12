using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NodesImplementation.DiaryStructs
{
    public class DiarySheet
    {
        private DateOnly dateCreated;

        private string sheetTitle;

        public string SheetTitle
        {
            private set => sheetTitle = value;
            get => sheetTitle;
        }

        public DiarySheet(string sheetTitle)
        {
            this.sheetTitle = sheetTitle;
            this.dateCreated = DateOnly.FromDateTime(DateTime.Today);
        }

        public string GetCreationDate()
        {
            return $"{dateCreated.Day}-{dateCreated.Month}-{dateCreated.Year}";           
        }
    }
}