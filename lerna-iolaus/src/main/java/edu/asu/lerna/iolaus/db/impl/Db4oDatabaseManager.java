package edu.asu.lerna.iolaus.db.impl;

import java.io.File;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectServer;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.cs.Db4oClientServer;
import com.db4o.cs.config.ServerConfiguration;
import com.db4o.ext.DatabaseFileLockedException;

import edu.asu.lerna.iolaus.db.IDatabaseManager;
import edu.asu.lerna.iolaus.domain.implementation.User;

/**
 * @description : 	This is class implements {@link IDatabaseManager} interface.
 * 					It works on initializing the DB objects for DB40 connectivity 
 * 
 * @author : Lohith Dwaraka
 *
 */
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
	
	/**
	 * Initializes the DB4O connectivity by
	 * starting the {@link ObjectServer} and initialize the {@link ObjectContainer} for querying 
	 * @throws UnsupportedEncodingException
	 */
	@PostConstruct
	public synchronized void init() throws UnsupportedEncodingException {
		close();
		ServerConfiguration configuration = Db4oClientServer
				.newServerConfiguration();
		configuration.file().blockSize(80);
		String dbfolder = env.getProperty("dbpath_folder");
		if (!dbfolder.endsWith(File.separator))
			dbfolder = dbfolder + File.separator;
		String dbpath = dbfolder + env.getProperty("dbname");
		String classPath =URLDecoder.decode(this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath(),"UTF-8");
		String dbFullPath = classPath.substring(0,classPath.indexOf("classes"))+ "classes" + File.separator + dbpath;
		logger.info("DB40 full path : "+ dbFullPath);
		//Url url = this.getClass().getClassLoader().getResource("dbFullPath");
		
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().messageLevel(1);
		config.common().objectClass(User.class).objectField("username")
				.indexed(true);
		config.common().objectClass(User.class).cascadeOnActivate(true);
		config.common().objectClass(User.class).cascadeOnUpdate(true);
		
		try
		{
		server = Db4oClientServer.openServer(configuration, dbFullPath, 0);
		client = server.openClient();
		}
		catch(DatabaseFileLockedException e)
		{
			e.printStackTrace();
		}
	}


	/**
	 * {@inheritDoc}
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

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public boolean isEncrypt() {
		return encrypt;
	}

	/**
	 * 
	 * {@inheritDoc}
	 */
	@Override
	public void setEncrypt(boolean encrypt) {
		this.encrypt = encrypt;
	}
}
