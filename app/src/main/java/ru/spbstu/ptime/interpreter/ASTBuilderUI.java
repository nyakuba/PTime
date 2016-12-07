package ru.spbstu.ptime.interpreter;

import android.support.v4.util.Pair;
import android.support.v4.widget.EdgeEffectCompat;

import java.util.List;

import ru.spbstu.ptime.constructor.ItemAdapter;
import ru.spbstu.ptime.constructor.items.ListItem;
import ru.spbstu.ptime.constructor.items.LoopEndItem;
import ru.spbstu.ptime.constructor.items.LoopStartItem;

/**
 * Created by venedikttsulenev on 06/12/16.
 */

class ASTBuilderUIException extends Exception {}

public class ASTBuilderUI implements ASTBuilder {
    private Program program;

    /* index = [index of loopNode (LoopStartItem) in list] + 1 (first in loop body) */
    private Pair<ASTNode, Integer> parseLoopBody(ASTLoopNode loopNode, List<Pair<Long, ListItem>> itemList, int index) throws ASTBuilderUIException {
        ListItem beg = itemList.get(index).second, curr = beg;
        int i;
        for (i = index + 1; !(curr instanceof LoopEndItem); ++i) {
            if (curr instanceof LoopStartItem) {
                Pair<ASTNode, Integer> pair = parseLoopBody((ASTLoopNode) curr.getASTNode(), itemList, i);
                ASTNode body = pair.first;
                i = pair.second;
                ((LoopStartItem) curr).setBody(body);
                if (i == itemList.size() - 1)
                    throw new ASTBuilderUIException();
            }
            ListItem next = itemList.get(i).second;
            curr.getASTNode().setNext(next.getASTNode());
            curr = next;
        }
        ((LoopEndItem) curr).setNext(null);
        return new Pair<ASTNode, Integer>(beg.getASTNode(), i + 1);
    }

    private ASTNode parse(List<Pair<Long, ListItem>> itemList) throws ASTBuilderUIException {
        ListItem beg = itemList.get(0).second, curr = beg;
        for (int i = 1; i < itemList.size() - 1; ++i) {
            if (curr instanceof LoopStartItem) {
                Pair<ASTNode, Integer> pair = parseLoopBody((ASTLoopNode) curr.getASTNode(), itemList, i);
                ASTNode body = pair.first;
                i = pair.second;
                ((LoopStartItem) curr).setBody(body);
                if (i == itemList.size())
                    throw new ASTBuilderUIException();
            }
            ListItem next = itemList.get(i).second;
            ASTNode currAST = curr.getASTNode();
//            if (null != currAST)
                currAST.setNext(next.getASTNode());
            curr = next;
        }
//        curr.getASTNode().setNext(null);
        return beg.getASTNode();
    }

    public ASTBuilderUI(List<Pair<Long, ListItem>> itemList) {
        try {
            ASTNode root = parse(itemList);
            if (null != root)
                program = new Program("Untitled", root);

        } catch (ASTBuilderUIException e) {
            program = null;
        }
    }

    @Override
    public Program getProgram() {
        return program;
    }
}
