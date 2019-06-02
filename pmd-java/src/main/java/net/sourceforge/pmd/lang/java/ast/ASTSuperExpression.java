/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */

package net.sourceforge.pmd.lang.java.ast;

import org.checkerframework.checker.nullness.qual.Nullable;

import net.sourceforge.pmd.lang.ast.Node;


/**
 * The "super" keyword. Technically not an expression but it's easier to analyse that way.
 *
 * <pre class="grammar">
 *
 * SuperExpression ::= "super"
 *  *                | {@link ASTClassOrInterfaceType TypeName} "." "super"
 *
 * </pre>
 */
public final class ASTSuperExpression extends AbstractJavaTypeNode implements ASTPrimaryExpression, LeftRecursiveNode {
    ASTSuperExpression(int id) {
        super(id);
    }


    ASTSuperExpression(JavaParser p, int id) {
        super(p, id);
    }

    @Override
    public void jjtClose() {
        super.jjtClose();

        if (jjtGetNumChildren() > 0) {
            // There's a qualifier
            Node child = jjtGetChild(0);
            if (child instanceof ASTAmbiguousName) {
                this.replaceChildAt(0, ((ASTAmbiguousName) child).forceTypeContext());
            }
        }
    }

    @Nullable
    public ASTClassOrInterfaceType getQualifier() {
        return jjtGetNumChildren() > 0 ? (ASTClassOrInterfaceType) jjtGetChild(0) : null;
    }

    /**
     * Accept the visitor. *
     */
    @Override
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }


    @Override
    public <T> void jjtAccept(SideEffectingVisitor<T> visitor, T data) {
        visitor.visit(this, data);
    }


}
