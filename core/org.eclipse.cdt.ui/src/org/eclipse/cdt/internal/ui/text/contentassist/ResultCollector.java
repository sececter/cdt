/**********************************************************************
 * Copyright (c) 2002,2003 Rational Software Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Common Public License v0.5
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v05.html
 * 
 * Contributors: 
 * IBM Rational Software - Initial API and implementation
***********************************************************************/
package org.eclipse.cdt.internal.ui.text.contentassist;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.cdt.core.parser.IProblem;
import org.eclipse.cdt.core.parser.ast.ASTAccessVisibility;
import org.eclipse.cdt.internal.ui.CElementImageProvider;
import org.eclipse.cdt.internal.ui.util.ImageDescriptorRegistry;
import org.eclipse.cdt.ui.CUIPlugin;
import org.eclipse.cdt.ui.FunctionPrototypeSummary;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.swt.graphics.Image;

/**
 * @author hamer
 *
 * The Result Collector class receives information from the completion engine
 * as a completion requestor. It might also receive information from others 
 * such as the results of a search operation to generate completion proposals.
 * 
 */
public class ResultCollector extends CompletionRequestorAdaptor {
	private Set completions = new HashSet();
	private ImageDescriptorRegistry registry = CUIPlugin.getImageDescriptorRegistry();
	private IProblem fLastProblem;	
	private ITextViewer fTextViewer;
	
	public ResultCollector(){
		completions.clear();
		fTextViewer = null;
	}
	
	/**
	 * @return the completion list
	 */
	public Set getCompletions() {
		return completions;
	}
	public void reset(ITextViewer viewer) {
		completions.clear();
		fTextViewer = viewer;
		fLastProblem = null;
	}
	/*
	 * Create a proposal
	 */
	public ICompletionProposal createProposal(String replaceString, String displayString, String infoString, String arguments, Image image, int offset, int length, int relevance){
		CCompletionProposal proposal;
			
		proposal = new CCompletionProposal(
											replaceString, // Replacement string
											offset,
											length,							
											image,
											displayString, // Display string
											relevance,
										    fTextViewer);
		
		if(arguments != null && arguments.length() > 0) {
			proposal.setContextInformation(new ContextInformation(replaceString, arguments));
		}
			
		// The info string could be populated with documentation info.
		// For now, it has the name and the parent's name if available.
		if((infoString != null)&& (!displayString.equals(infoString)))
			proposal.setAdditionalProposalInfo(infoString);
		
		return proposal;
	}
	
	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.cdt.core.ICompletionRequestor#acceptField(java.lang.String, java.lang.String, org.eclipse.cdt.core.parser.ast.ASTAccessVisibility, int, int, int)
	 */
	public void acceptField(String name, String returnType, ASTAccessVisibility visibility, 
	int completionStart, int completionLength, int relevance) {
		String replaceString = "";
		String displayString = "";
		Image image = null;
		StringBuffer infoString = new StringBuffer();
		
		// fill the replace, display and info strings
		replaceString = name;
		displayString = name;
		if(returnType != null)
			displayString+= " : " + returnType;
	
		// get the image 	
		ImageDescriptor imageDescriptor = CElementImageProvider.getFieldImageDescriptor(visibility);
		image = registry.get( imageDescriptor );

		// create proposal and add it to completions list
		ICompletionProposal proposal = createProposal(replaceString, displayString, infoString.toString(), 
			null, image, completionStart, completionLength, relevance);
		completions.add(proposal);		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.ICompletionRequestor#acceptClass(java.lang.String, int, int, int)
	 */
	public void acceptClass(
		String name,
		int completionStart,
		int completionLength,
		int relevance) {		
			String replaceString = "";
			String displayString = "";
			Image image = null;
			StringBuffer infoString = new StringBuffer();
		
			// fill the replace, display and info strings
			replaceString = name;
			displayString = name;
	
			// get the image 	
			ImageDescriptor imageDescriptor = CElementImageProvider.getClassImageDescriptor();
			image = registry.get( imageDescriptor );

			// create proposal and add it to completions list
			ICompletionProposal proposal = createProposal(replaceString, displayString, infoString.toString(), 
				null, image, completionStart, completionLength, relevance);
			completions.add(proposal);		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.ICompletionRequestor#acceptFunction(java.lang.String, java.lang.String, java.lang.String, int, int, int)
	 */
	public void acceptFunction(
		String name,
		String parameterString,
		String returnType,
		int completionStart,
		int completionLength,
		int relevance) {
			String replaceString = "";
			String displayString = "";
			String arguments = "";
			
			Image image = null;
			StringBuffer infoString = new StringBuffer();
		
			// fill the replace, display and info strings
			replaceString = name;
			displayString = name;
			String functionPrototype = returnType + " " + name;
			if(parameterString != null){
				if ((parameterString.indexOf("(") == -1) && (parameterString.indexOf(")") == -1)) 
				{	
					functionPrototype += "(" + parameterString + ")";
				}
				else {
					functionPrototype += parameterString;
				}
			}

			FunctionPrototypeSummary fproto = new FunctionPrototypeSummary(functionPrototype);
			if(fproto != null) {						
				replaceString = fproto.getName() + "()";
				displayString = fproto.getPrototypeString(true);
				infoString.append(displayString);
				arguments = fproto.getArguments();
			}
	
			// get the image 	
			ImageDescriptor imageDescriptor = CElementImageProvider.getFunctionImageDescriptor();
			image = registry.get( imageDescriptor );

			// create proposal and add it to completions list
			ICompletionProposal proposal = createProposal(replaceString, displayString, infoString.toString(), 
				arguments, image, completionStart, completionLength, relevance);
			completions.add(proposal);		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.ICompletionRequestor#acceptLocalVariable(java.lang.String, java.lang.String, int, int, int)
	 */
	public void acceptLocalVariable(
		String name,
		String returnType,
		int completionStart,
		int completionLength,
		int relevance) {
		
		String replaceString = "";
		String displayString = "";
		Image image = null;
		StringBuffer infoString = new StringBuffer();
		
		// fill the replace, display and info strings
		replaceString = name;
		displayString = name;
		if(returnType != null)
			displayString+= " : " + returnType;
		
		// get the image 	
		ImageDescriptor imageDescriptor = CElementImageProvider.getLocalVariableImageDescriptor();
		image = registry.get( imageDescriptor );

		// create proposal and add it to completions list
		ICompletionProposal proposal = createProposal(replaceString, displayString, infoString.toString(), null, image, completionStart, completionLength, relevance);
		completions.add(proposal);		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.ICompletionRequestor#acceptMacro(java.lang.String, int, int, int)
	 */
	public void acceptMacro(
		String name,
		int completionStart,
		int completionLength,
		int relevance) {
		
			String replaceString = "";
			String displayString = "";
			String arguments = "";
			Image image = null;
			StringBuffer infoString = new StringBuffer();
			String prototype = "";
			
			// fill the replace, display and info strings
			final String DEFINE ="#define "; 
			if(name.startsWith(DEFINE)){
				prototype = name.substring(DEFINE.length(), name.length());				
			}else {
				prototype = name;
			}
			int leftbracket = prototype.indexOf('(');
			int rightbracket = prototype.lastIndexOf(')');
			if(( leftbracket == -1 ) && (rightbracket == -1)) {
				replaceString = prototype;
				displayString = prototype;
			}else {
				FunctionPrototypeSummary fproto = new FunctionPrototypeSummary(prototype);
				if(fproto != null) {						
					replaceString = fproto.getName() + "()";
					displayString = fproto.getPrototypeString(true, false);
					infoString.append(displayString);
					arguments = fproto.getArguments();
				} else {
					replaceString = prototype;
					displayString = prototype;
				}
			}
			// get the image 	
			ImageDescriptor imageDescriptor = CElementImageProvider.getMacroImageDescriptor();
			image = registry.get( imageDescriptor );

			// create proposal and add it to completions list
			ICompletionProposal proposal = createProposal(replaceString, displayString, infoString.toString(), 
				arguments, image, completionStart, completionLength, relevance);
			completions.add(proposal);		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.ICompletionRequestor#acceptMethod(java.lang.String, java.lang.String, java.lang.String, org.eclipse.cdt.core.parser.ast.ASTAccessVisibility, int, int, int)
	 */
	public void acceptMethod(
		String name,
		String parameterString,
		String returnType,
		ASTAccessVisibility visibility,
		int completionStart,
		int completionLength,
		int relevance) {
		
			String replaceString = "";
			String displayString = "";
			String arguments = "";
			Image image = null;
			StringBuffer infoString = new StringBuffer();
		
			// fill the replace, display and info strings
			replaceString = name;
			displayString = name;
			String functionPrototype = returnType + " " + name;
			if(parameterString != null){
				if ((parameterString.indexOf("(") == -1) && (parameterString.indexOf(")") == -1))
				{	
					functionPrototype += "(" + parameterString + ")";
				}
				else {
					functionPrototype += parameterString;
				}
			}
				
			FunctionPrototypeSummary fproto = new FunctionPrototypeSummary(functionPrototype);
			if(fproto != null) {						
				replaceString = fproto.getName() + "()";
				displayString = fproto.getPrototypeString(true);
				infoString.append(displayString);
				arguments = fproto.getArguments();
			}
	
			// get the image 	
			ImageDescriptor imageDescriptor = CElementImageProvider.getMethodImageDescriptor(visibility);
			image = registry.get( imageDescriptor );

			// create proposal and add it to completions list
			ICompletionProposal proposal = createProposal(replaceString, displayString, infoString.toString(), 
				arguments, image, completionStart, completionLength, relevance);
			completions.add(proposal);		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.ICompletionRequestor#acceptNamespace(java.lang.String, int, int, int)
	 */
	public void acceptNamespace(
		String name,
		int completionStart,
		int completionLength,
		int relevance) {
			String replaceString = "";
			String displayString = "";
			Image image = null;
			StringBuffer infoString = new StringBuffer();
		
			// fill the replace, display and info strings
			replaceString = name;
			displayString = name;
	
			// get the image 	
			ImageDescriptor imageDescriptor = CElementImageProvider.getNamespaceImageDescriptor();
			image = registry.get( imageDescriptor );

			// create proposal and add it to completions list
			ICompletionProposal proposal = createProposal(replaceString, displayString, infoString.toString(), 
				null, image, completionStart, completionLength, relevance);
			completions.add(proposal);		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.ICompletionRequestor#acceptStruct(java.lang.String, int, int, int)
	 */
	public void acceptStruct(
		String name,
		int completionStart,
		int completionLength,
		int relevance) {
			String replaceString = "";
			String displayString = "";
			Image image = null;
			StringBuffer infoString = new StringBuffer();
		
			// fill the replace, display and info strings
			replaceString = name;
			displayString = name;
	
			// get the image 	
			ImageDescriptor imageDescriptor = CElementImageProvider.getStructImageDescriptor();
			image = registry.get( imageDescriptor );

			// create proposal and add it to completions list
			ICompletionProposal proposal = createProposal(replaceString, displayString, infoString.toString(), null, image, completionStart, completionLength, relevance);
			completions.add(proposal);		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.ICompletionRequestor#acceptUnion(java.lang.String, int, int, int)
	 */
	public void acceptUnion(
		String name,
		int completionStart,
		int completionLength,
		int relevance) {
			String replaceString = "";
			String displayString = "";
			Image image = null;
			StringBuffer infoString = new StringBuffer();
		
			// fill the replace, display and info strings
			replaceString = name;
			displayString = name;
	
			// get the image 	
			ImageDescriptor imageDescriptor = CElementImageProvider.getUnionImageDescriptor();
			image = registry.get( imageDescriptor );

			// create proposal and add it to completions list
			ICompletionProposal proposal = createProposal(replaceString, displayString, infoString.toString(), null, image, completionStart, completionLength, relevance);
			completions.add(proposal);		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.ICompletionRequestor#acceptVariable(java.lang.String, java.lang.String, int, int, int)
	 */
	public void acceptVariable(
		String name,
		String returnType,
		int completionStart,
		int completionLength,
		int relevance) {
			String replaceString = "";
			String displayString = "";
			Image image = null;
			StringBuffer infoString = new StringBuffer();
		
			// fill the replace, display and info strings
			replaceString = name;
			displayString = name;
			if(returnType != null)
				displayString+= " : " + returnType;
	
			// get the image 	
			ImageDescriptor imageDescriptor = CElementImageProvider.getVariableImageDescriptor();
			image = registry.get( imageDescriptor );

			// create proposal and add it to completions list
			ICompletionProposal proposal = createProposal(replaceString, displayString, infoString.toString(), null, image, completionStart, completionLength, relevance);
			completions.add(proposal);		
	}
	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.ICompletionRequestor#acceptEnumeration(java.lang.String, int, int, int)
	 */
	public void acceptEnumeration(
		String name,
		int completionStart,
		int completionLength,
		int relevance) {
			String replaceString = "";
			String displayString = "";
			Image image = null;
			StringBuffer infoString = new StringBuffer();
		
			// fill the replace, display and info strings
			replaceString = name;
			displayString = name;
	
			// get the image 	
			ImageDescriptor imageDescriptor = CElementImageProvider.getEnumerationImageDescriptor();
			image = registry.get( imageDescriptor );

			// create proposal and add it to completions list
			ICompletionProposal proposal = createProposal(replaceString, displayString, infoString.toString(), 
				null, image, completionStart, completionLength, relevance);
			completions.add(proposal);		
	}


	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.ICompletionRequestor#acceptEnumerator(java.lang.String, int, int, int)
	 */
	public void acceptEnumerator(
		String name,
		int completionStart,
		int completionLength,
		int relevance) {
			String replaceString = "";
			String displayString = "";
			Image image = null;
			StringBuffer infoString = new StringBuffer();
		
			// fill the replace, display and info strings
			replaceString = name;
			displayString = name;
	
			// get the image 	
			ImageDescriptor imageDescriptor = CElementImageProvider.getEnumeratorImageDescriptor();
			image = registry.get( imageDescriptor );

			// create proposal and add it to completions list
			ICompletionProposal proposal = createProposal(replaceString, displayString, infoString.toString(), 
				null, image, completionStart, completionLength, relevance);
			completions.add(proposal);		
	}


	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.ICompletionRequestor#acceptKeyword(java.lang.String, int, int, int)
	 */
	public void acceptKeyword(
		String name,
		int completionStart,
		int completionLength,
		int relevance) {
		String replaceString = "";
		String displayString = "";
		Image image = null;
		StringBuffer infoString = new StringBuffer();
		
		// fill the replace, display and info strings
		replaceString = name;
		displayString = name;
		
		// no image for keywords 	
		// create proposal and add it to completions list
		ICompletionProposal proposal = createProposal(replaceString, displayString, infoString.toString(), 
				null, image, completionStart, completionLength, relevance);
		completions.add(proposal);		
	}

	/* (non-Javadoc)
	 * @see org.eclipse.cdt.core.ICompletionRequestor#acceptError(org.eclipse.cdt.core.parser.IProblem)
	 */
	public void acceptError(IProblem error) {
		fLastProblem = error;
	}

	public String getErrorMessage() {
		if (fLastProblem != null)
			return fLastProblem.getMessage();
		return ""; //$NON-NLS-1$
	}
	
}
