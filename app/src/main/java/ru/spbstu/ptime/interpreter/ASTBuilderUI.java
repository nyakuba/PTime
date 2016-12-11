package ru.spbstu.ptime.interpreter;

import android.support.annotation.Nullable;
import android.support.v4.util.Pair;
import android.support.v4.widget.EdgeEffectCompat;

import java.util.List;

import ru.spbstu.ptime.constructor.ItemAdapter;
import ru.spbstu.ptime.constructor.items.ListItem;
import ru.spbstu.ptime.constructor.items.LoopEndItem;
//import ru.spbstu.ptime.constructor.items.LoopStartItem;

/**
 * Created by venedikttsulenev on 06/12/16.
 */

public class ASTBuilderUI implements ASTBuilder {
    private Program program;
    private List<Pair<Long, ListItem>> itemList;
    private Pair<ASTNode, Integer> parseTillNull(int index) { /* index = [index of loopNode (LoopStartItem) in list] + 1 (first in loop body) */
        ASTNode begin = itemList.get(index).second.getASTNode();
        if (begin == null)
            return new Pair<>(null, index + 1);
        int i;
        ASTNode currAST, prevAST = begin;
        for (i = index + 1; null != (currAST = itemList.get(i).second.getASTNode()); ++i) {
            if (currAST instanceof ASTLoopNode) {
                Pair<ASTNode, Integer> pair = parseTillNull(i + 1);
                ((ASTLoopNode) currAST).setBody(pair.first);
                i = pair.second;
            }
            prevAST.setNext(currAST);
            prevAST = currAST;
        }
        return new Pair<>(begin, i + 1);
    }
    private ASTNode parse(int index) {
        ASTNode beg = itemList.get(index).second.getASTNode();
        if (beg == null)
            return null;
        ASTNode curr = beg;
        for (int i = index + 1; curr != null && i < itemList.size() - 1; ++i) {
            if (curr instanceof ASTLoopNode) {
                Pair<ASTNode, Integer> pair = parseTillNull(i);
                ASTNode body = pair.first;
                i = pair.second;
                ((ASTLoopNode) curr).setBody(body);
            }
            ASTNode next = itemList.get(i).second.getASTNode();
            curr.setNext(next);
            curr = next;
        }
        /* curr.setNext(null); */
        /* .next of any ASTNode is null by default. So curr.next == null, as needed. */
        return beg;
    }

    public ASTBuilderUI(List<Pair<Long, ListItem>> itemList) {
        this.itemList = itemList;
        ASTNode root = parse(0);
        if (null != root)
            program = new Program("Untitled", root);
        else
            program = null;
    }

    @Override
    public Program getProgram() {
        return program;
    }
}
