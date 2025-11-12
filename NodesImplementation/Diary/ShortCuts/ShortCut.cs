using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace NodesImplementation.Diary.ShortCuts
{
    public class ShortCut
    {
        public string keyCombination;

        private Action action;

        public ShortCut(string keyCombination, Action action)
        {
            this.action = action;
            this.keyCombination = keyCombination;
        }
    }
}