using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using System.Threading.Tasks;
using NodesImplementation.Diary;

namespace NodesImplementation
{
    public class App
    {

        private DiaryUI diaryUI;
        public App()
        {
            this.diaryUI = new DiaryUI();
        }
        public void run()
        {
            while (true)
            {
                Console.Clear();
                diaryUI.renderMenu();
                char key = Console.ReadKey().KeyChar;
                if(key == 'Q')


                break;
            }
            

        }
    }
}