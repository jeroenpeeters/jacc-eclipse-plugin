package nl.jeroenpeeters.eclipse.plugin.jacc;

import jacc.JaccJob;
import jacc.ParserOutput;
import jacc.Settings;
import jacc.TokensOutput;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

import compiler.Diagnostic;
import compiler.Position;
import compiler.Warning;

public class JaccBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "JaccEclipsePlugin.jaccBuilder";

	private static final String MARKER_TYPE = "JaccEclipsePlugin.jaccProblem";

	private static final String JACC_EXTENSION = ".jacc";

	@Override
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		if (kind == FULL_BUILD) {
			fullBuild(monitor);
		} else {
			IResourceDelta delta = getDelta(getProject());
			if (delta == null) {
				fullBuild(monitor);
			} else {
				incrementalBuild(delta, monitor);
			}
		}
		return null;
	}

	void check(IResource resource) {

		MessageConsole myConsole = findConsole("JACC Console");
		boolean b = false;
		try {
			if (resource instanceof IFile
					&& resource.getName().endsWith(JACC_EXTENSION)) {
				IFile file = (IFile) resource;
				deleteMarkers(file);
				
				b = true;

				MessageConsoleStream out = myConsole.newMessageStream();
				out.println("Processing: " + file.getProjectRelativePath().toString());
				out.close();
				
				PrintWriter localPrintWriter = new PrintWriter(myConsole.newMessageStream(), true);
				MyHandler localSimpleHandler = new MyHandler(file, myConsole);
				Settings localSettings = new Settings();
				
				JaccJob localJaccJob = new JaccJob(localSimpleHandler,
						localPrintWriter, localSettings);
				localJaccJob.parseGrammarFile(file.getLocation().toFile()
						.getAbsolutePath());

				localJaccJob.buildTables();
				localSettings.fillBlanks("");
				
				if(localSimpleHandler.getNumFailures() > 0){
					return;
				}

				new TokensOutput(localSimpleHandler, localJaccJob).write(file
						.getLocation().toFile().getParent()
						+ System.getProperty("file.separator")
						+ localSettings.getInterfaceName() + ".java");
				new ParserOutput(localSimpleHandler, localJaccJob).write(file
						.getLocation().toFile().getParent()
						+ System.getProperty("file.separator")
						+ localSettings.getClassName() + ".java");
				
				try {
					((IFolder) file.getParent()).refreshLocal(
							IResource.DEPTH_INFINITE, null);
					file.getProject().refreshLocal(INCREMENTAL_BUILD, null);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (NullPointerException npe){
			npe.printStackTrace();
		} catch (Exception e) {
			MessageConsoleStream out = myConsole.newMessageStream();
			out.setColor(new Color(null, 255, 0, 0));
			out.println("Problem processing JACC Grammar");
			e.printStackTrace(new PrintStream(out));
			try {
				out.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			if(b){
				MessageConsoleStream out = myConsole.newMessageStream();
				out.println("============================================================");
				try {
					out.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private void deleteMarkers(IFile file) {
		try {
			file.deleteMarkers(MARKER_TYPE, false, IResource.DEPTH_ZERO);
		} catch (CoreException ce) {
		}
	}

	protected void fullBuild(final IProgressMonitor monitor)
			throws CoreException {
		try {
			getProject().accept(new SampleResourceVisitor());
		} catch (CoreException e) {
		}
	}

	protected void incrementalBuild(IResourceDelta delta,
			IProgressMonitor monitor) throws CoreException {
		// the visitor does the work.
		delta.accept(new SampleDeltaVisitor());
	}

	private MessageConsole findConsole(String name) {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		for (int i = 0; i < existing.length; i++)
			if (name.equals(existing[i].getName())){
				conMan.showConsoleView((MessageConsole) existing[i]);
				return (MessageConsole) existing[i];
			}
		// no console found, so create a new one
		MessageConsole myConsole = new MessageConsole(name, null);
		conMan.addConsoles(new IConsole[] { myConsole });
		conMan.showConsoleView(myConsole);
		return myConsole;
	}

	class MyHandler extends compiler.Handler {

		private final MessageConsole myConsole;
		private final IFile file;
		
		private HashSet<String> reportedMessages = new HashSet<String>();

		public MyHandler(IFile file, MessageConsole myConsole) {
			this.myConsole = myConsole;
			this.file = file;
		}
		
		private void addMarker(String message, int lineNumber,
				int severity) {
			try {
				IMarker marker = file.createMarker(MARKER_TYPE);
				marker.setAttribute(IMarker.MESSAGE, message);
				marker.setAttribute(IMarker.SEVERITY, severity);
				marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
				//marker.setAttribute(IMarker.CHAR_START, colStart);
				//marker.setAttribute(IMarker.CHAR_END, colEnd);
			} catch (CoreException e) {
			}
		}

		@Override
		protected void respondTo(Diagnostic d) {
			final Position localPosition = d.getPos();
			final String message = d.getText() != null ? d.getText() : "Unknown";
			
			if(localPosition != null){
				if ((d instanceof Warning)){
					this.addMarker(message, localPosition.getRow(), 
							IMarker.SEVERITY_ERROR);
				}else{
					this.addMarker(message, localPosition.getRow(),
							IMarker.SEVERITY_ERROR);
				}
			}else{
				StringBuffer sb = new StringBuffer();
				if ((d instanceof Warning)){
					sb.append("WARNING: ");
				}else{
					sb.append("ERROR: ");
				}
				sb.append(message);
			
				try {
					if(!reportedMessages.contains(sb.toString())){
						MessageConsoleStream out  = myConsole.newMessageStream();
						out.setColor(new Color(null, 255, 0, 0));
						out.println(sb.toString());
						out.close();
					}
				} catch (IOException e) {
				}
				reportedMessages.add(sb.toString());
			}
		

		}
	}

	class SampleDeltaVisitor implements IResourceDeltaVisitor {
		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.core.resources.IResourceDeltaVisitor#visit(org.eclipse
		 * .core.resources.IResourceDelta)
		 */
		public boolean visit(IResourceDelta delta) throws CoreException {
			IResource resource = delta.getResource();
			switch (delta.getKind()) {
			case IResourceDelta.ADDED:
				// handle added resource
				check(resource);
				break;
			case IResourceDelta.REMOVED:
				// handle removed resource
				break;
			case IResourceDelta.CHANGED:
				// handle changed resource
				check(resource);
				break;
			}
			// return true to continue visiting children.
			return true;
		}
	}

	class SampleResourceVisitor implements IResourceVisitor {
		public boolean visit(IResource resource) {
			check(resource);
			// return true to continue visiting children.
			return true;
		}
	}

}

