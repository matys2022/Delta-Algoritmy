using System;
using System.Collections.Generic;
using System.Data;
using System.Linq;
using System.Threading.Tasks;

namespace SortingAlgorithms.Services.Loading
{
    public class FileLoader
    {
        public FileLoader()
        {
            
        }

        public IEnumerable<string> load(string? path = null, string? data = null, int startingLineIx = 0, int? lines = null)
        {
            string records = "";
            if(path != null)
            try
            {
                using (StreamReader reader = new StreamReader(path))
                {
                    if(lines != null)
                    {
                        for(int i = startingLineIx - 1; i < lines; i++)
                        {
                            records += reader.ReadLine() + '\n';
                        }
                    }
                    else
                    {
                        records = reader.ReadToEnd();    
                    }
                };
                
            }catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
            else
            records = data??"";

            
            return records.Split(new char[] { ' ', '\n', '\r' }, StringSplitOptions.RemoveEmptyEntries);
            
        }
    }
}