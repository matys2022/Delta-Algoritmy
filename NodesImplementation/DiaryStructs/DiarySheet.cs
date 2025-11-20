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

        private string? sheetText;
        public string? SheetText
        {
            get => sheetText;
            private set => sheetText = value;
        }

        public DiarySheet(string sheetTitle, DateOnly? date = null)
        {
            this.sheetTitle = sheetTitle;
            if(date == null){
                this.dateCreated = DateOnly.FromDateTime(DateTime.Today);
            }
            else
            {
                this.dateCreated = (DateOnly)date;
            }
        }

        public void SetText(string str)
        {
            sheetText = str;
        }

        public string GetCreationDate()
        {
            return $"{dateCreated.Day}-{dateCreated.Month}-{dateCreated.Year}";           
        }
    }
}