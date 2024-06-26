package symbol.symbol.warning;

import symbol.symbol.Symbol;
import symbol.symbol.Terminal;

import java.util.ArrayList;
import java.util.HashSet;

public abstract class Warning {
    public final Symbol parent;
    public final Symbol child;
    public String message;

    public Warning(Symbol parent, Symbol child) {
        this.parent = parent;
        this.child = child;
        this.message = null;
    }

    public Warning(Symbol parent, Symbol child, String message) {
        this.parent = parent;
        this.child = child;
        this.message = message;
    }

    @Override
    public String toString() {
        int row = child.row == null ? parent.row : child.row;
        int column = child.column == null ? parent.column : child.column;
        String warningName = null;
        if (this instanceof Danger) {
            warningName = "\u001b[31m" + "危险行为" + "\u001B[0m";
        }
        if (this instanceof Discouragement) {
            warningName = "\u001b[33m" + "不良行为" + "\u001B[0m";
        }
        if (this instanceof Strangeness) {
            warningName = "\u001b[34m" + "怪异行为" + "\u001B[0m";
        }
        String parentName = parent.getClass().getName();
        parentName = parentName.contains(".") ? parentName.substring(parentName.lastIndexOf(".") + ".".length()) : parentName;
        String childName = child.getClass().getName();
        childName = childName.contains(".") ? childName.substring(childName.lastIndexOf(".") + ".".length()) : childName;

        String prefix = String.format("行号 %03d 列号 %03d %s %s", row, column, warningName, message + "\u3000".repeat(Math.max(0, 10 - message.length())));
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Symbol> symbols = parent.traversal(new ArrayList<>());
        HashSet<Symbol> visited = new HashSet<>();
        for (Symbol symbol : symbols) {
            if (!visited.contains(symbol)) {
                visited.add(symbol);
                if ((symbol instanceof Terminal) && !visited.contains(child)) {
                    stringBuilder.append(symbol.toString());
                }
                if (symbol.equals(child)) {
                    String background = "";
                    if (this instanceof Danger) {
                        background = "\u001b[41m";
                    }
                    if (this instanceof Discouragement) {
                        background = "\u001b[43m";
                    }
                    if (this instanceof Strangeness) {
                        background = "\u001b[44m";
                    }
                    stringBuilder.append(background + "\u001b[30m" + child.toString() + "\u001B[0m");
                }
            } else {
                visited.remove(symbol);
            }
        }
        return prefix + "    " + stringBuilder.toString().replaceAll("\\s+", " ");
    }
}
