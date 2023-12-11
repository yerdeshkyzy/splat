package splat.semanticanalyzer;

import java.util.*;

import splat.parser.elements.*;

public class SemanticAnalyzer {

	private ProgramAST progAST;
	private Map<String, FunctionDecl> funcMap = new HashMap<String, FunctionDecl>();
	private Map<String, Type> progVarMap = new HashMap<String, Type>();

	public SemanticAnalyzer(ProgramAST progAST) {
		this.progAST = progAST;
	}

	public void analyze() throws SemanticAnalysisException {
		
		// Checks to make sure we don't use the same labels more than once
		// for our program functions and variables 
		checkNoDuplicateProgLabels();
		
		// This sets the maps that will be needed later when we need to
		// typecheck variable references and function calls in the 
		// program body
		setProgVarAndFuncMaps();
		
		// Perform semantic analysis on the functions
		for (FunctionDecl funcDecl : funcMap.values()) {	
			analyzeFuncDecl(funcDecl);
		}
		
		// Perform semantic analysis on the program body
		for (Statement stmt : progAST.getStmts()) {
			stmt.analyze(funcMap, progVarMap);
		}
		
	}

	private void analyzeFuncDecl(FunctionDecl funcDecl) throws SemanticAnalysisException {
		
		// Checks to make sure we don't use the same labels more than once
		// among our function parameters, local variables, and function names
		checkNoDuplicateFuncLabels(funcDecl);
		
		// Get the types of the parameters and local variables
		Map<String, Type> varAndParamMap = getVarAndParamMap(funcDecl);
		
		// Perform semantic analysis on the function body
		for (Statement stmt : funcDecl.getStmts()) {
			stmt.analyze(funcMap, varAndParamMap);
		}
	}
	
	
	private Map<String, Type> getVarAndParamMap(FunctionDecl funcDecl) throws SemanticAnalysisException {
		
		// FIXME: Somewhat similar to setProgVarAndFuncMaps()
		Map<String, Type> varAndParamMap = new HashMap<>();
		Set<String> labelSet = new HashSet<String>();
		labelSet.add(funcDecl.getLabel().toString());
		varAndParamMap.put("return", funcDecl.getType());
		for (VariableDecl decl: funcDecl.getParams()) {
			String label = decl.getLabel().toString();
			for (String labelFunction : funcMap.keySet()) {
				if (labelFunction.equals(label)) {
					throw new SemanticAnalysisException("Function and parameter names clash: "+ label, funcDecl);
				}

			}

			if (!labelSet.contains(label)) {
				labelSet.add(label);
				varAndParamMap.put(label, decl.getType());
			}
		}
		for (VariableDecl decl : funcDecl.getLocalVars()) {

			String label = decl.getLabel().toString();
			if (!labelSet.contains(label)) {
				labelSet.add(label);
				varAndParamMap.put(label, decl.getType());
			} else {
				throw new SemanticAnalysisException("Parameter and variable names clash: " + label , funcDecl);
			}
		}
		int i = 1;
		if (!varAndParamMap.get("return").toString().equals("void")) {
			i = 0;
		}
		for (Statement stmt : funcDecl.getStmts()) {
			if (stmt instanceof Return) {
				i++;
			} else if (stmt instanceof IfStmts) {
				i++;
			}
		}
		if (i<1) {
			throw new SemanticAnalysisException("Nothing returned", funcDecl);
		}
		return varAndParamMap;
	}

	private void checkNoDuplicateFuncLabels(FunctionDecl funcDecl) 
									throws SemanticAnalysisException {
		
		// FIXME: Similar to checkNoDuplicateProgLabels()
		String labels = funcDecl.getLabel().toString();
		int i=0;
		for (String label: funcMap.keySet()) {
			if(labels.equals(label)) {
				i++;
			}
		}
		if (i>1) {
			throw new SemanticAnalysisException("Functions' name clash: "+funcDecl.getLabel(),funcDecl);
		}
	}
	
	private void checkNoDuplicateProgLabels() throws SemanticAnalysisException {
		
		Set<String> labels = new HashSet<String>();
		
 		for (Declaration decl : progAST.getDecls()) {
 			String label = decl.getLabel().toString();
 			
			if (labels.contains(label)) {
				throw new SemanticAnalysisException("Cannot have duplicate label '"
						+ label + "' in program", decl);
			} else {
				labels.add(label);
			}
			
		}
	}
	
	private void setProgVarAndFuncMaps() {
		
		for (Declaration decl : progAST.getDecls()) {

			String label = decl.getLabel().toString();
			
			if (decl instanceof FunctionDecl) {
				FunctionDecl funcDecl = (FunctionDecl)decl;
				funcMap.put(label, funcDecl);
				
			} else if (decl instanceof VariableDecl) {
				VariableDecl varDecl = (VariableDecl)decl;
				progVarMap.put(label, varDecl.getType());
			}
		}
	}
}
