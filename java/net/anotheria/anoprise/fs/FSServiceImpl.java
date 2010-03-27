package net.anotheria.anoprise.fs;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.apache.log4j.Logger;

/**
 * Main implementation for file system service.
 * 
 * @author abolbat
 * @version 1.0, 2010/02/13
 * @param <T>
 *            - {@link FSSaveable} object type
 */
public class FSServiceImpl<T extends FSSaveable> implements FSService<T> {

	/**
	 * Configuration.
	 */
	private final FSServiceConfig config;

	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(FSServiceImpl.class.getName());

	/**
	 * Prefix for logging.
	 */
	private static final String SERVICE_LOG_PREFIX = "FS_SERVICE: ";

	/**
	 * Default constructor.
	 * 
	 * @param aConfig
	 *            - {@link FSServiceConfig}
	 */
	public FSServiceImpl(FSServiceConfig aConfig) {
		this.config = aConfig;
	}

	@Override
	public T read(String ownerId) throws FSServiceException {
		String filePath = config.getStoreFilePath(ownerId);
		File file = new File(filePath);

		if (!file.exists()) {
			log.debug(SERVICE_LOG_PREFIX + "Item not found. Owner id: " + ownerId + ". File path: " + filePath);
			throw new FSItemNotFoundException(ownerId);
		}

		ObjectInputStream in = null;
		try {
			synchronized (this) {
				in = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
				@SuppressWarnings("unchecked")
				T result = (T) in.readObject();
				in.close();
				return result;
			}
		} catch (IOException ioe) {
			log.error(SERVICE_LOG_PREFIX + "IOException: " + ioe.getMessage());
			throw new FSServiceException(ioe.getMessage(), ioe);
		} catch (ClassNotFoundException cnfe) {
			log.error(SERVICE_LOG_PREFIX + "ClassNotFoundException: " + cnfe.getMessage());
			throw new FSServiceException(cnfe.getMessage(), cnfe);
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException ignored) {
					log.debug(SERVICE_LOG_PREFIX + "Ignored IOException: " + ignored.getMessage());
				}
		}
	}

	@Override
	public void save(T t) throws FSServiceException {
		String folderPath = config.getStoreFolderPath(t.getOwnerId());
		String filePath = config.getStoreFilePath(t.getOwnerId());

		File file = new File(folderPath);
		if (!file.exists())
			if (!file.mkdirs())
				throw new FSServiceException(SERVICE_LOG_PREFIX + "Can't create needed folder structure");

		file = new File(filePath);

		ObjectOutputStream out = null;
		try {
			synchronized (this) {
				out = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
				out.writeObject(t);
				out.close();
			}
		} catch (IOException ioe) {
			log.error(SERVICE_LOG_PREFIX + "IOException: " + ioe.getMessage());
			throw new FSServiceException(ioe.getMessage(), ioe);
		} finally {
			if (out != null)
				try {
					out.close();
				} catch (IOException ignored) {
					log.debug(SERVICE_LOG_PREFIX + "Ignored IOException: " + ignored.getMessage());
				}
		}
	}

	@Override
	public void delete(String ownerId) throws FSServiceException {
		String filePath = config.getStoreFilePath(ownerId);
		File f = new File(filePath);

		if (!f.exists())
			return;

		if (!f.delete())
			throw new FSServiceException("Deletion filed. Owner id: " + ownerId + ". File path: " + filePath);
	}

}