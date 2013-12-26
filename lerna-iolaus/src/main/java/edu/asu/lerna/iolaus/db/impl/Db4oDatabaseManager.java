package edu.asu.lerna.iolaus.db.impl;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import edu.asu.lerna.iolaus.domain.implementation.User;
import java.io.File;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ServerConfiguration;

import edu.asu.lerna.iolaus.db.IDatabaseManager;
import edu.asu.lerna.iolaus.rest.AddNodesRestController;

@Component
@PropertySource(value = "classpath:/db4o.properties")
public class Db4oDatabaseManager implements Serializable, IDatabaseManager{

	@Autowired
	private Environment env;
	
	private ObjectContainer client;

	
	private static final Logger logger = LoggerFactory
			.getLogger(Db4oDatabaseManager.class);
	
	private static final long serialVersionUID = -3325272288078647257L;
	private ObjectServer server;
	private boolean encrypt = true;
	User user;
	@PostConstruct
	public synchronized void init() {
		close();
		ServerConfiguration configuration = Db4oClientServer
				.newServerConfiguration();
		configuration.file().blockSize(80);
		String dbfolder = env.getProperty("dbpath_folder");
		if (!dbfolder.endsWith(File.separator))
			dbfolder = dbfolder + File.separator;
		String dbpath = dbfolder + env.getProperty("dbname");
		String classPath =this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
		String dbFullPath = classPath.substring(0,classPath.indexOf("classes"))+ "classes" + File.separator + dbpath;
		logger.info("DB40 full path : "+ dbFullPath);
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().messageLevel(1);
		config.common().objectClass(User.class).objectField("username")
				.indexed(true);
		config.common().objectClass(User.class).cascadeOnActivate(true);
		config.common().objectClass(User.class).cascadeOnUpdate(true);
		server = Db4oClientServer.openServer(configuration, dbFullPath, 0);
		client = server.openClient();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.asu.momo.db.impl.IDatabaseManager#getClient()
	 */
	@Override
	public ObjectContainer getClient() {
		return client;
	}

	private synchronized void close() {
		if (client != null) {
			client.close();
			client = null;
		}
		if (server != null) {
			server.close();
		}
		server = null;
	}

	@PreDestroy
	public void shutdown() {
		close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.asu.momo.db.impl.IDatabaseManager#isEncrypt()
	 */
	@Override
	public boolean isEncrypt() {
		return encrypt;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.asu.momo.db.impl.IDatabaseManager#setEncrypt(boolean)
	 */
	@Override
	public void setEncrypt(boolean encrypt) {
		this.encrypt = encrypt;
	}
}
