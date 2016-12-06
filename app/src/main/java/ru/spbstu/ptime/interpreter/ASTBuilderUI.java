package ru.spbstu.ptime.interpreter;

import android.support.v4.util.Pair;

import java.util.List;

import ru.spbstu.ptime.constructor.ItemAdapter;
import ru.spbstu.ptime.constructor.items.ListItem;

/**
 * Created by venedikttsulenev on 06/12/16.
 */

public class ASTBuilderUI implements ASTBuilder {
    private Program program;

    ASTBuilderUI(List<Pair<Long, ListItem>> itemList) {
        ASTNode root = itemList.get(0).second.getASTNode();
        ASTNode curr = root;
        for (Pair<Long, ListItem> item : itemList.subList(1, itemList.size())) {
            ASTNode next = item.second.getASTNode();
            curr.setNext(next);
            curr = next;
        }
        program = new Program("Untitled", root);
    }

    @Override
    public Program getProgram() {
        return program;
    }
}
