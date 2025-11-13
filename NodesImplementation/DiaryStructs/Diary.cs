using System;
using System.Collections.Generic;
using System.Drawing;
using System.Linq;
using System.Runtime.InteropServices;
using System.Threading.Tasks;
using NodesImplementation.Structures;

namespace NodesImplementation.DiaryStructs
{
    public class Diary
    {

        public void renderMenu(IEnumerable<LabelShortCut> labelShortCuts)
        {
            foreach(LabelShortCut labelShortCut in labelShortCuts)
            {
                Console.WriteLine($"{labelShortCut.shortCut.keyCombination} - {labelShortCut.hint}");
            }
        }
        private void render()
        {

        }
    }
}