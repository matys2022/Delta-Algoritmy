using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using NodesImplementation.Diary.ShortCuts;

namespace NodesImplementation.Structures
{
    public class LabelShortCut
    {

        public ShortCut shortCut;
        public string hint;

        public LabelShortCut(ShortCut shortCut, string hint)
        {
            this.shortCut = shortCut;
            this.hint = hint;
        }

    }
}