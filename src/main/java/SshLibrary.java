import com.netease.robotframework.ssh.SshClient;

import org.robotframework.javalib.annotation.Autowired;
import org.robotframework.javalib.library.AnnotationLibrary;


public class SshLibrary extends AnnotationLibrary {

	public static final String KEYWORD_PATTERN = "com/netease/robotframework/ssh/**/*.class";
	public static final String ROBOT_LIBRARY_SCOPE = "GLOBAL";
	public static final String ROBOT_LIBRARY_VERSION = "1.0.0";
	public static final String ROBOT_LIBRARY_DOC_FORMAT = "ROBOT";
	public static final String LIBRARY_DOCUMENTATION = "SshLibrary is a library to implement SSH and SFTP";
	public static final String LIBRARY_INITIALIZATION_DOCUMENTATION = "SshLibrary can be imported directly without any arguments.\n\r"
			+ "\n"
			+ "Examples:\n"
			+ "| Library | SshLibrary |\n";

	public SshLibrary() {
		super("com/acme/**/keyword/**/*.class");
        addKeywordPattern(KEYWORD_PATTERN);
        createKeywordFactory(); // => init annotations
	}
	
	@Autowired
	protected SshClient sshClient;
	
	@Override
    public Object runKeyword(String keywordName, Object[] args) {
        return super.runKeyword(keywordName, toStrings(args));
    }
    
    @Override
    public String getKeywordDocumentation(String keywordName) {
    	if (keywordName.equals("__intro__"))
            return LIBRARY_DOCUMENTATION;
    	if (keywordName.equals("__init__"))
            return LIBRARY_INITIALIZATION_DOCUMENTATION;
    	try {
    		return super.getKeywordDocumentation(keywordName);
    	} catch (Exception e) {
    		return "";
    	}
    }
    
    private Object[] toStrings(Object[] args) {
        Object[] newArgs = new Object[args.length];
        for (int i = 0; i < newArgs.length; i++) {
            if (args[i].getClass().isArray()) {
                newArgs[i] = args[i];
            } else {
                newArgs[i] = args[i].toString();
            }
        }
        return newArgs;
    }
}
