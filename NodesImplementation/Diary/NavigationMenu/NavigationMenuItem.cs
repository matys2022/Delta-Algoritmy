using System;
using System.Collections.Generic;
using System.Diagnostics.Contracts;
using System.Linq;
using System.Threading.Tasks;
using NodesImplementation.Diary.ShortCuts;

namespace NodesImplementation.Diary.NavigationMenu
{
    public class NavigationMenuItem
    {
        public ShortCut shortCut;
        public string hint;

        public NavigationMenuItem(ShortCut shortCut, string hint)
        {
            this.shortCut = shortCut;
            this.hint = hint;
        }

    }
}