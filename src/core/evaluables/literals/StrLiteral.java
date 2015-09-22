// StringLiteral.java

package core.evaluables.literals;

import core.evaluables.Literal;

/**
 * Represents string literals.
 * User: baltasarq
 * Date: 11/19/12
 */
public class StrLiteral extends Literal {

    public StrLiteral(String lit)
    {
        super( lit );
    }

    public String getValue()
    {
        return (String) super.getValue();
    }

    @Override
    public String toString() {
        return (String) this.getValue();
    }
}
