package com.syrus.AMFICOM.pmd.rules;

import net.sourceforge.pmd.AbstractRule;
import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.ast.ASTBlockStatement;
import net.sourceforge.pmd.ast.ASTCompilationUnit;
import net.sourceforge.pmd.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.ast.ASTLiteral;
import net.sourceforge.pmd.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.ast.ASTName;
import net.sourceforge.pmd.ast.ASTVariableDeclarator;
import net.sourceforge.pmd.ast.SimpleNode;
import net.sourceforge.pmd.rules.AvoidDuplicateLiteralsRule.ExceptionParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.text.MessageFormat;
import java.util.*;

public class DummiesStrings extends AbstractRule {

	private static final char	DEFAULT_SEPARATOR				= ',';
	private static final String	EXCEPTION_LIST_PROPERTY			= "exceptionlist";
	private static final String	SEPARATOR_PROPERTY				= "separator";
	private static final String	EXCEPTION_FILE_NAME_PROPERTY	= "exceptionfile";

	private static final String	POSSIBLE_METHODS_PREFIX			= "possibleMethod.";

	private Map					literals						= new HashMap();
	private Set					exceptions						= new HashSet();

	public Object visit(ASTCompilationUnit node, Object data) {
		this.literals.clear();

		if (hasProperty(EXCEPTION_LIST_PROPERTY)) {
			ExceptionParser p;
			if (hasProperty(SEPARATOR_PROPERTY)) {
				p = new ExceptionParser(getStringProperty(SEPARATOR_PROPERTY).charAt(0));
			} else {
				p = new ExceptionParser(DEFAULT_SEPARATOR);
			}
			this.exceptions = p.parse(getStringProperty(EXCEPTION_LIST_PROPERTY));
		} else if (hasProperty(EXCEPTION_FILE_NAME_PROPERTY)) {
			this.exceptions = new HashSet();
			try {
				LineNumberReader reader = new LineNumberReader(
																new BufferedReader(
																					new FileReader(
																									new File(
																												getStringProperty(EXCEPTION_FILE_NAME_PROPERTY)))));
				String line;
				while ((line = reader.readLine()) != null) {
					this.exceptions.add(line);
				}
				reader.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
			}
		}
		super.visit(node, data);

		List possibleMethods = new ArrayList();
		for (Iterator it = this.properties.keySet().iterator(); it.hasNext();) {
			String key = (String) it.next();
			if (key.startsWith(POSSIBLE_METHODS_PREFIX))
				possibleMethods.add(this.properties.getProperty(key));

		}

		for (Iterator i = this.literals.keySet().iterator(); i.hasNext();) {
			String key = (String) i.next();
			List occurrences = (List) this.literals.get(key);
			for (Iterator it = occurrences.iterator(); it.hasNext();) {
				ASTLiteral n = (ASTLiteral) it.next();

				boolean stringDeclaration = false;

				{
					ASTLocalVariableDeclaration s = (ASTLocalVariableDeclaration) n
							.getFirstParentOfType(ASTLocalVariableDeclaration.class);
					if (s != null)
						stringDeclaration = true;
				}

				{
					ASTVariableDeclarator s = (ASTVariableDeclarator) n
							.getFirstParentOfType(ASTVariableDeclarator.class);
					if (s != null)
						stringDeclaration = true;
				}

				if (stringDeclaration) {
					boolean finalStatic = false;
					List list = n.getParentsOfType(ASTFieldDeclaration.class);
					for (Iterator iter = list.iterator(); iter.hasNext();) {
						ASTFieldDeclaration decl = (ASTFieldDeclaration) iter.next();
						if ((!decl.isFinal()) || (!decl.isStatic())) {
							Object[] args = new Object[] { "String field declaration as not final static"};
							generateViolation(args, data, occurrences);
						} else
							finalStatic = true;

					}
					if (!finalStatic) {
						Object[] args = new Object[] { "String declaraion by literal"};
						generateViolation(args, data, occurrences);
					}
				} else {
					ASTBlockStatement prefix = (ASTBlockStatement) n.getFirstParentOfType(ASTBlockStatement.class);
					if (prefix != null) {
						for (int j = 0; j < prefix.jjtGetNumChildren(); j++) {
							String name = getName((SimpleNode) prefix.jjtGetChild(j));
							boolean found = false;
							for (Iterator iter = possibleMethods.iterator(); iter.hasNext();) {
								String methodName = (String) iter.next();
								// TODO: fix if we wanna use strict exact
								// equality
								if (name.indexOf(methodName) != -1) {
									found = true;
									break;
								}
							}
							if (!found) {
								Object[] args = new Object[] { "method " + name
										+ " with literal arguments"};
								generateViolation(args, data, occurrences);
							}

						}
					}

				}
			}

		}

		return data;
	}

	private void generateViolation(Object[] args, Object data, List occurrences) {
		String msg = MessageFormat.format(getMessage(), args);
		RuleContext ctx = (RuleContext) data;
		ctx.getReport().addRuleViolation(
											createRuleViolation(ctx, ((SimpleNode) occurrences.get(0)).getBeginLine(),
																msg));
	}

	public Object visit(ASTLiteral node, Object data) {
		if (node.getImage() == null || node.getImage().indexOf('\"') == -1) { return data; }
		//		 skip any exceptions
		if (this.exceptions.contains(node.getImage().substring(1, node.getImage().length() - 1))) { return data; }

		if (this.literals.containsKey(node.getImage())) {
			List occurrences = (List) this.literals.get(node.getImage());
			occurrences.add(node);
		} else {
			List occurrences = new ArrayList();
			occurrences.add(node);
			this.literals.put(node.getImage(), occurrences);
		}

		return data;
	}

	private String getName(SimpleNode node) {
		while (node.jjtGetNumChildren() > 0) {
			if (node.jjtGetChild(0) instanceof ASTName) { return ((ASTName) node.jjtGetChild(0)).getImage(); }
			return getName((SimpleNode) node.jjtGetChild(0));
		}
		throw new IllegalArgumentException("Check with hasNameAsChild() first!");
	}
}