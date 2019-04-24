package duAutomation;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.wb.swt.SWTResourceManager;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;


import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Button;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.swt.widgets.Composite;

import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.eclipse.core.databinding.DataBindingContext;

import org.eclipse.core.databinding.observable.Realm;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.GridData;

@SuppressWarnings("deprecation")
public class duAutomator {
	@SuppressWarnings("unused")
	private DataBindingContext m_bindingContext;

	protected Shell shlGetTrace;
	public static Text text;

	public static Display display;
	private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
	public static Composite composite;

	public static String str1;
	public static Text text_1;
	public Thread t;
	public static boolean tracec = true;
	public static String MSISDN;
	public static String systemName;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Display display = Display.getDefault();
		Realm.runWithDefault(SWTObservables.getRealm(display), new Runnable() {
			public void run() {
				try {
					duAutomator window = new duAutomator();
					window.open();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Open the window.
	 * 
	 * @throws InterruptedException
	 */
	public void open() throws InterruptedException {
		display = Display.getDefault();
		createContents();
		shlGetTrace.open();
		shlGetTrace.layout();
		while (!shlGetTrace.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
				// Thread.sleep(10000);
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlGetTrace = new Shell();
		shlGetTrace.setImage(SWTResourceManager.getImage("lib\\favicon.ico"));
		shlGetTrace.setMinimumSize(new Point(1400, 730));
		shlGetTrace.setSize(884, 585);
		shlGetTrace.setText("Get Trace");
		shlGetTrace.setLayout(new GridLayout(3, false));

		Label lblMsisdn = new Label(shlGetTrace, SWT.NONE);
		lblMsisdn.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblMsisdn.setText("MSISDN");

		text = new Text(shlGetTrace, SWT.BORDER);
		GridData gd_text = new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1);
		gd_text.widthHint = 135;
		text.setLayoutData(gd_text);
				
						Button btnSdp = new Button(shlGetTrace, SWT.RADIO);
						btnSdp.addSelectionListener(new SelectionAdapter() {
							@Override
							public void widgetSelected(SelectionEvent e) {
								systemName = "SDP";
							}
						});
						formToolkit.adapt(btnSdp, true, true);
						btnSdp.setText("SDP");
		
				Button btnAir = new Button(shlGetTrace, SWT.RADIO);
				btnAir.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
						systemName = "AIR";

					}
				});
				btnAir.setText("AIR");
				formToolkit.adapt(btnAir, true, true);
		
		Button btnCcn = new Button(shlGetTrace, SWT.RADIO);
		btnCcn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				systemName = "CCN";
			}
		});
		btnCcn.setText("CCN");
		formToolkit.adapt(btnCcn, true, true);

		Button btnStartTrace = new Button(shlGetTrace, SWT.NONE);
		btnStartTrace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				text_1.setText("");
				tracec = true;
				MSISDN = text.getText();
				doUpdate();
			}
		});
		btnStartTrace.setText("Trace");

		Button btnStopTrace = new Button(shlGetTrace, SWT.NONE);
		btnStopTrace.setText("Stop Trace");
		btnStopTrace.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				tracec = false;
			}
		});
		Label label = new Label(shlGetTrace, SWT.NONE);
		formToolkit.adapt(label, true, true);

		composite = formToolkit.createComposite(shlGetTrace, SWT.NULL);
		composite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		formToolkit.paintBordersFor(composite);

		text_1 = new Text(composite, SWT.MULTI | SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
		text_1.setBounds(10, 35, 1344, 563);
		formToolkit.adapt(text_1, true, true);
		
		Label lblTrace = new Label(composite, SWT.NONE);
		lblTrace.setFont(SWTResourceManager.getFont("Trebuchet MS", 11, SWT.BOLD | SWT.ITALIC));
		lblTrace.setBounds(10, 5, 59, 25);
		formToolkit.adapt(lblTrace, true, true);
		lblTrace.setText("Trace");
		m_bindingContext = initDataBindings();
		text_1.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				text_1.setTopIndex(text_1.getLineCount() - 1);

			}
		});

	}

	private static void doUpdate() {

		duAutomator dd = new duAutomator();

		MyTask tt = new MyTask();
		dd.t = new Thread(tt);

		dd.t.start();

	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}

	public static class MyTask implements Runnable {

		public static Session nsession;
		public static Channel channel;

		public void run() {
			DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			String ExecutionStarttime = dateFormat.format(cal.getTime()).toString();
			String currdate = ExecutionStarttime.replace(":", "-").replace(" ", "_");
			System.setProperty("logfilename", "Trace/" + systemName + "/" + currdate + "_" + MSISDN);
			DOMConfigurator.configure("log4j.xml");
			String hosturi = readProperties(systemName + "_URI");
			String username = readProperties(systemName + "_UserName");
			
			String password = readProperties(systemName + "_Password");
			
			String path = readProperties(systemName + "_Path");
			

			LoginSSH(hosturi, username, password);
			Executecmd("cd " + path, "sdpuser@RY1SDPTB>");
			Executecmd("tail -0f TraceEventLogFile.txt.0 | grep " + MSISDN + " | tr \"|\" \"\\n\"", "kjfgkjh");
			//tail -0f TraceEventLogFile.txt.0 | grep 831033757870 | tr "|" "\n"
			//tail -0f TraceEventLogFile.txt.0 | grep 897987987444 | tr "|" "\n"
			//Executecmd("tail TraceEventLogFile.txt.0","kjfgkjh");
			 nsession.disconnect();
			 channel.disconnect();
			 System.out.println("channel closed");
			 System.out.println("Session closed");
			if(channel.isClosed())
			{
				System.out.println("channel closed");
			}
			if(!nsession.isConnected())
			{
				System.out.println("Session closed");
			}
			 
			 
		}

		public void Executecmd(String command, String Exitval) {
			try {
				System.out.println(command);
				String str = "";
				// String str1;
				OutputStream ops = channel.getOutputStream();
				
				PrintStream ps = new PrintStream(ops, true);
				ps.println(command.trim());
				InputStream in = channel.getInputStream();
				byte[] bt = new byte[1024];
				while (tracec) {
					int i = in.read(bt, 0, 1024);
					if (i < 0)
						break;
					str1 = new String(bt, 0, i);
					if (str1 == null) {
						break;
					}
					str = str + str1;
					// System.out.println(str);
					System.out.print(str1);
					info(str1);
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							text_1.setText(text_1.getText() + str1);
						}
					});

					//
					// new Thread(new MyTask());

					if (str.contains(Exitval)) {
						break;
					}
					if (channel.isClosed()) {
						System.out.println("exit-status: " + channel.getExitStatus());
						break;
					}
					try {
						Thread.sleep(1000);
					} catch (Exception ee) {
						ee.printStackTrace();
					}
				}
				
				
				// System.out.println("DONE");

			} catch (Exception e) {
				e.printStackTrace();
				// return "";
			}

		}

		public static void LoginSSH(String host, String username, String password) {
			try {
				// Properties config = new Properties();
				// config.put("StrictHostKeyChecking", "no");
				JSch jsch = new JSch();
				nsession = jsch.getSession(username, host, 22);
				nsession.setPassword(password);
				nsession.setConfig("StrictHostKeyChecking", "no");
				nsession.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
				nsession.connect();
				channel = nsession.openChannel("shell");
				System.out.println("Connected to: " + host);
				channel.connect();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private static Logger Log = Logger.getLogger(MyTask.class.getName());//

		// This is to print log for the beginning of the test case, as we usually run so
		// many test cases as a test suite

		public static void startTestCase(String sTestCaseName) {

			Log.info("****************************************************************************************");

			Log.info("****************************************************************************************");

			Log.info("$$$$$$$$$$$$$$$$$$$$$ " + sTestCaseName + " $$$$$$$$$$$$$$$$$$$$$$$$$");

			Log.info("****************************************************************************************");

			Log.info("****************************************************************************************");

		}

		// This is to print log for the ending of the test case

		public static void endTestCase(String sTestCaseName) {

			Log.info("XXXXXXXXXXXXXXXXXXXXXXX             " + "-E---N---D-" + "             XXXXXXXXXXXXXXXXXXXXXX");

		}

		// Need to create these methods, so that they can be called

		public static void info(String message) {

			Log.info(message);

		}

		public void warn(String message) {

			Log.warn(message);

		}

		public static void error(String message) {

			Log.error(message);

		}

		public void fatal(String message) {

			Log.fatal(message);

		}

		public void debug(String message) {

			Log.debug(message);

		}

	}

	public static String readProperties(String propkey) {
		Properties prop = new Properties();
		String retval = "";
		InputStream input = null;
		try {
			input = new FileInputStream("environment.properties");
			prop.load(input);
			retval = prop.getProperty(propkey);

		} catch (Exception e) {
		
			e.printStackTrace();
		}

		return retval;
	}
}