using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Threading.Tasks;
using NodesImplementation.DiaryStructs;


namespace NodesImplementation
{
    public class App
    {

        private Diary diary;
        public App()
        {
            this.diary = new Diary();
        }
        public void run()
        {
            while (true)
            {
                Console.Clear();
                diary.renderMenu();
                char key = Console.ReadKey().KeyChar;
                if(key == 'Q')


                break;
            }
            

        }
    }
}