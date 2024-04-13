package symbol.base.punctuator.semicolon;

import symbol.symbol.Terminal;
import symbol.symbol.type.Table;
import symbol.symbol.invalidity.InvalidityException;
import symbol.symbol.sentence.Sentence;

public class Semicolon extends Terminal {
    public static String[] strings = {";"};

    public Semicolon(Integer row, Integer column, String string) {
        super(row, column, null, string);
    }

    public static Semicolon parse(Sentence sentence, Table table) throws InvalidityException {
        Integer row = sentence.getRow();
        Integer column = sentence.getColumn();
        for (String string : strings) {
            if (sentence.startsWith(string)) {
                sentence.remove(string.length());
                return new Semicolon(row, column, string);
            }
        }
        throw new InvalidityException();
    }
}
