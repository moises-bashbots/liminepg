package mysql;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConnectorMariaDB {
	public enum DatabaseType { POSTGRES, MSSQL, MARIADB }

	public static Connection conn = null;
	public static String scheme = "asset";
	public static String server = "localhost";
	public static String port = "5432";
	public static String user = "robot";
	public static String password = "r0b0t";
	public static String jdbcUrl = "";
	public static String searchPath = "";
	public static DatabaseType databaseType = DatabaseType.POSTGRES;

	private static final String MYSQL_CONF_NAME = "mysql.conf";
	private static final String MSSQL_CONF_NAME = "mssql.conf";

	private Connection connection = null;
	private String localScheme = scheme;
	private String localServer = server;
	private String localPort = port;
	private String localUser = user;
	private String localPassword = password;
	private String localJdbcUrl = jdbcUrl;
	private String localSearchPath = searchPath;
	private DatabaseType localDatabaseType = databaseType;

	private static final class ConnectionSettings {
		private String scheme;
		private String server;
		private String port;
		private String user;
		private String password;
		private String jdbcUrl;
		private String searchPath;
		private DatabaseType databaseType;

		private ConnectionSettings(DatabaseType databaseType) {
			this.databaseType = databaseType;
			this.server = "localhost";
			this.user = "robot";
			this.password = "r0b0t";
			this.scheme = "asset";
			this.jdbcUrl = "";
			this.searchPath = "";
			applyTypeDefaults();
		}

		private void applyTypeDefaults() {
			switch (databaseType) {
			case MSSQL:
				if (port == null || port.isEmpty() || "5432".equals(port) || "3306".equals(port)) {
					port = "1433";
				}
				break;
			case MARIADB:
				if (port == null || port.isEmpty() || "5432".equals(port)) {
					port = "3306";
				}
				break;
			case POSTGRES:
			default:
				if (port == null || port.isEmpty() || "3306".equals(port)) {
					port = "5432";
				}
				break;
			}
		}
	}

	public ConnectorMariaDB() {
	}

	public ConnectorMariaDB(DatabaseType databaseType) {
		this.localDatabaseType = databaseType;
		this.localPort = null;
		applyTypeDefaults();
	}

	public static ConnectorMariaDB postgresConnector() {
		return new ConnectorMariaDB(DatabaseType.POSTGRES);
	}

	public static ConnectorMariaDB mssqlConnector() {
		return new ConnectorMariaDB(DatabaseType.MSSQL);
	}

	public static void connect() {
		if ("localhost".equals(server) && jdbcUrl.isEmpty()) {
			System.out.println("Default server, we will try to read the customized configuration!");
			readConf();
		}
		ConnectorMariaDB connector = postgresConnector();
		connector.copyFromStaticConfiguration();
		connector.open();
		conn = connector.getConnection();
	}

	public void connectUsingDefaultConf() {
		String fileName = resolveDefaultConfPath(localDatabaseType == DatabaseType.MSSQL ? MSSQL_CONF_NAME : MYSQL_CONF_NAME);
		loadConf(fileName);
		open();
	}

	public void connect(String pathFileConf) {
		loadConf(pathFileConf);
		open();
	}

	private void open() {
		try {
			Class.forName(driverClassFor(localDatabaseType));
		} catch (ClassNotFoundException e) {
			System.out.println("Driver class not found: " + e.getMessage());
			return;
		}

		String url = localJdbcUrl.isEmpty() ? buildJdbcUrl() : localJdbcUrl;
		System.out.println("Connecting with: " + url);

		try {
			if (localJdbcUrl.isEmpty()) {
				connection = DriverManager.getConnection(url, localUser, localPassword);
			} else {
				connection = DriverManager.getConnection(url);
			}
			postConnect();
			System.out.println("Connected to " + localDatabaseType + " on " + localServer + ":" + localPort);
		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void postConnect() throws SQLException {
		if (connection == null) {
			return;
		}
		try (Statement st = connection.createStatement()) {
			switch (localDatabaseType) {
			case POSTGRES:
				if (localSearchPath != null && !localSearchPath.isEmpty()) {
					st.execute("SET search_path TO " + localSearchPath);
				}
				break;
			case MARIADB:
				st.execute("SET GLOBAL max_allowed_packet=1073741824");
				break;
			case MSSQL:
			default:
				break;
			}
		}
	}

	private String buildJdbcUrl() {
		switch (localDatabaseType) {
		case MSSQL:
			return "jdbc:sqlserver://" + localServer + ":" + localPort
					+ ";databaseName=" + localScheme + ";encrypt=true;trustServerCertificate=true;";
		case MARIADB:
			return "jdbc:mariadb://" + localServer + ":" + localPort + "/" + localScheme
					+ "?useUnicode=true&serverTimezone=UTC";
		case POSTGRES:
		default:
			return "jdbc:postgresql://" + localServer + ":" + localPort + "/" + localScheme;
		}
	}

	private String driverClassFor(DatabaseType type) {
		switch (type) {
		case MSSQL:
			return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		case MARIADB:
			return "org.mariadb.jdbc.Driver";
		case POSTGRES:
		default:
			return "org.postgresql.Driver";
		}
	}

	private void copyFromStaticConfiguration() {
		this.localScheme = scheme;
		this.localServer = server;
		this.localPort = port;
		this.localUser = user;
		this.localPassword = password;
		this.localJdbcUrl = jdbcUrl;
		this.localSearchPath = searchPath;
		this.localDatabaseType = databaseType;
		applyTypeDefaults();
	}

	public static void disconnect() {
		if (conn == null) {
			return;
		}
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("Closed connection on " + server);
	}

	public static void main(String[] args) {
		readConf();
		connect();
		disconnect();
	}

	public static void readConf() {
		ConnectionSettings settings = readSettings(resolveDefaultConfPath(MYSQL_CONF_NAME), DatabaseType.POSTGRES);
		applyStaticSettings(settings);
		showStaticSettings();
	}

	public void loadConf(String pathFileConf) {
		ConnectionSettings settings = readSettings(pathFileConf, localDatabaseType);
		applySettings(settings);
	}

	private static ConnectionSettings readSettings(String fileName, DatabaseType fallbackType) {
		ConnectionSettings settings = new ConnectionSettings(fallbackType);
		File confFile = new File(fileName);
		if (!confFile.exists()) {
			System.out.println("Config file not found! " + fileName);
			return settings;
		}

		System.out.println("Reading " + fileName);
		try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
			List<String> confLines = stream.collect(Collectors.toList());
			for (String rawLine : confLines) {
				String line = rawLine.trim();
				if (line.startsWith("#") || line.isEmpty()) {
					continue;
				}
				String[] words = line.split(";", 2);
				if (words.length < 2) {
					continue;
				}
				String key = words[0].trim().toLowerCase(Locale.ROOT);
				String value = words[1].trim();
				applySetting(settings, key, value);
				System.out.println(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return settings;
	}

	private static void applySetting(ConnectionSettings settings, String key, String value) {
		switch (key) {
		case "dbms":
		case "database_type":
			settings.databaseType = parseDatabaseType(value);
			settings.applyTypeDefaults();
			break;
		case "server":
			settings.server = value;
			break;
		case "port":
			settings.port = value;
			break;
		case "user":
			settings.user = value;
			break;
		case "password":
			settings.password = value;
			break;
		case "database":
		case "scheme":
			settings.scheme = value;
			break;
		case "jdbc_url":
		case "url":
			settings.jdbcUrl = value;
			break;
		case "search_path":
		case "schema":
			settings.searchPath = value;
			break;
		default:
			break;
		}
	}

	private static DatabaseType parseDatabaseType(String value) {
		String normalized = value.trim().toLowerCase(Locale.ROOT);
		switch (normalized) {
		case "mssql":
		case "sqlserver":
		case "sql_server":
			return DatabaseType.MSSQL;
		case "mariadb":
		case "mysql":
			return DatabaseType.MARIADB;
		case "postgres":
		case "postgresql":
		default:
			return DatabaseType.POSTGRES;
		}
	}

	private static void applyStaticSettings(ConnectionSettings settings) {
		databaseType = settings.databaseType;
		server = settings.server;
		port = settings.port;
		user = settings.user;
		password = settings.password;
		scheme = settings.scheme;
		jdbcUrl = settings.jdbcUrl;
		searchPath = settings.searchPath;
	}

	private void applySettings(ConnectionSettings settings) {
		this.localDatabaseType = settings.databaseType;
		this.localServer = settings.server;
		this.localPort = settings.port;
		this.localUser = settings.user;
		this.localPassword = settings.password;
		this.localScheme = settings.scheme;
		this.localJdbcUrl = settings.jdbcUrl;
		this.localSearchPath = settings.searchPath;
	}

	private static void showStaticSettings() {
		System.out.println("DatabaseType: " + databaseType);
		System.out.println("Server: " + server);
		System.out.println("Port: " + port);
		System.out.println("User: " + user);
		System.out.println("Scheme: " + scheme);
		System.out.println("SearchPath: " + searchPath);
	}

	private void applyTypeDefaults() {
		if (localPort == null || localPort.isEmpty()) {
			switch (localDatabaseType) {
			case MSSQL:
				localPort = "1433";
				break;
			case MARIADB:
				localPort = "3306";
				break;
			case POSTGRES:
			default:
				localPort = "5432";
				break;
			}
		}
	}

	private static String resolveDefaultConfPath(String confName) {
		String relative = System.getProperty("os.name", "").toLowerCase(Locale.ROOT).contains("win")
				? "..\\Conf\\" + confName
				: "../Conf/" + confName;
		if (new File(relative).exists()) {
			return relative;
		}
		return System.getProperty("user.home") + File.separator + "App" + File.separator + "Conf" + File.separator + confName;
	}

	public static Connection getConn() {
		return conn;
	}

	public static void setConn(Connection conn) {
		ConnectorMariaDB.conn = conn;
	}

	public Connection getConnection() {
		return connection;
	}

	public String getServer() {
		return localServer;
	}

	public void setServer(String server) {
		this.localServer = server;
	}

	public String getPort() {
		return localPort;
	}

	public void setPort(String port) {
		this.localPort = port;
	}

	public String getUser() {
		return localUser;
	}

	public void setUser(String user) {
		this.localUser = user;
	}

	public String getPassword() {
		return localPassword;
	}

	public void setPassword(String password) {
		this.localPassword = password;
	}

	public static String getScheme() {
		return scheme;
	}

	public static void setScheme(String scheme) {
		ConnectorMariaDB.scheme = scheme;
	}
}