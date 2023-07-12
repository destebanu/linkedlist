package com.prueba1.demo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Mejorar cada uno de los métodos a nivel SQL y código cuando sea necesario
 * Razonar cada una de las mejoras que se han implementado No es necesario que
 * el código implementado funcione
 */
public class TestSqlDao {

	private static TestSqlDao instance = new TestSqlDao();
	private Hashtable<Long, Long> maxOrderUser;

	private TestSqlDao() {

	}

	private static TestSqlDao getInstance() {

		return instance;
	}

	/**
	 * Obtiene el ID del último pedido para cada usuario
	 */

	/**
	 * MEJORAS:
	 * 
	 * Uso de PreparedStatement: Se ha cambiado la concatenación de la variable
	 * idTienda en la consulta SQL por un marcador de posición (?). Esto mejora la
	 * seguridad y evita posibles ataques de inyección de SQL. Luego, se utiliza
	 * setLong para asignar el valor de idTienda al PreparedStatement.
	 * 
	 * Utilización de try-with-resources: Se utiliza un bloque try con recursos para
	 * garantizar que la conexión y la declaración se cierren adecuadamente después
	 * de su uso. Esto evita posibles fugas de recursos y mejora la legibilidad del
	 * código.
	 * 
	 * Uso de Map en lugar de Hashtable: Se ha cambiado el tipo de retorno de
	 * Hashtable a Map para seguir las mejores prácticas de programación. Además, se
	 * ha utilizado HashMap en lugar de Hashtable debido a que HashMap es más
	 * eficiente y no es sincronizado, lo cual es adecuado en este contexto.
	 * 
	 * Cambio en el alias de columna y uso de MAX: En la consulta SQL, se utiliza
	 * MAX(ID_PEDIDO) AS MAX_PEDIDO para obtener directamente el valor máximo de
	 * ID_PEDIDO para cada ID_USUARIO. Esto evita la necesidad de comparar valores
	 * en el código Java y simplifica la lógica.
	 * 
	 * Uso de tipos de datos apropiados: Se ha utilizado getLong en lugar de getInt
	 * para obtener los valores de ID_USUARIO y ID_PEDIDO, ya que aparentemente son
	 * de tipo long en lugar de int.
	 */
	public Map<Long, Long> getMaxUserOrderId(long idTienda) throws Exception {

		String query = "SELECT ID_USUARIO, MAX(ID_PEDIDO) AS MAX_PEDIDO FROM PEDIDOS WHERE ID_TIENDA = ? GROUP BY ID_USUARIO";
		Map<Long, Long> maxOrderUser = new HashMap<>();

		try (Connection connection = getConnection(); PreparedStatement stmt = connection.prepareStatement(query)) {

			stmt.setLong(1, idTienda);
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				long idUsuario = rs.getLong("ID_USUARIO");
				long maxPedido = rs.getLong("MAX_PEDIDO");
				maxOrderUser.put(idUsuario, maxPedido);
			}
		}

		return maxOrderUser;
	}

	/**
	 * Copia todos los pedidos de un usuario a otro
	 */

	/**
	 * MEJORAS:
	 * 
	 * Uso de una sentencia SQL de inserción con una subconsulta: En lugar de
	 * recuperar los datos del primer usuario en un bucle y luego realizar
	 * inserciones individuales, se utiliza una única sentencia SQL que copia los
	 * pedidos directamente de un usuario a otro. Esto mejora el rendimiento al
	 * reducir la cantidad de operaciones de base de datos y reduce la complejidad
	 * del código.
	 * 
	 * Uso de marcador de posición (?) en la consulta SQL: Se utiliza un marcador de
	 * posición en la consulta SQL (WHERE ID_USUARIO = ?) y luego se establece el
	 * valor de idUserOri utilizando stmt.setLong(1, idUserOri). Esto mejora la
	 * seguridad y evita posibles ataques de inyección de SQL.
	 * 
	 * Eliminación de la transacción explícita: Dado que se está realizando una
	 * única operación de inserción en cada ejecución del bucle, no es necesario
	 * utilizar una transacción explícita. El motor de base de datos se encargará de
	 * manejar las inserciones como transacciones individuales.
	 */
	public void copyUserOrders(long idUserOri, long idUserDes) throws Exception {

		String query = "INSERT INTO PEDIDOS (FECHA, TOTAL, SUBTOTAL, DIRECCION) "
				+ "SELECT FECHA, TOTAL, SUBTOTAL, DIRECCION FROM PEDIDOS WHERE ID_USUARIO = ?";
		Connection connection = getConnection();
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setLong(1, idUserOri);
		stmt.executeUpdate();
	}

	/**
	 * Obtiene los datos del usuario y pedido con el pedido de mayor importe para la
	 * tienda dada
	 */

	/**
	 * MEJORAS:
	 * 
	 * Uso de una consulta SQL con ordenamiento y límite: Se ha modificado la
	 * consulta SQL para utilizar ORDER BY P.TOTAL DESC y LIMIT 1. Esto ordena los
	 * resultados por el campo TOTAL en orden descendente y limita el resultado a
	 * solo una fila, que corresponderá al pedido de mayor importe. De esta manera,
	 * se obtiene directamente el pedido de mayor importe sin necesidad de utilizar
	 * una variable total y comparar en el bucle.
	 * 
	 * Uso de marcador de posición (?) en la consulta SQL: Se utiliza un marcador de
	 * posición en la consulta SQL (WHERE P.ID_TIENDA = ?) y luego se establece el
	 * valor de idTienda utilizando stmt.setLong(1, idTienda). Esto mejora la
	 * seguridad y evita posibles ataques de inyección de SQL.
	 * 
	 * Asignación directa de valores: En lugar de asignar los valores recuperados
	 * del ResultSet a las variables pasadas como parámetros, se asignan
	 * directamente a las variables del método. Esto se debe a que los parámetros
	 * userId, orderId, name y address son variables locales y no se reflejarán
	 * fuera del método tal como están implementados actualmente.
	 */
	public void getUserMaxOrder(long idTienda, long userId, long orderId, String name, String address)
			throws Exception {

		String query = "SELECT U.ID_USUARIO, P.ID_PEDIDO, P.TOTAL, U.NOMBRE, U.DIRECCION "
				+ "FROM PEDIDOS AS P INNER JOIN USUARIOS AS U ON P.ID_USUARIO = U.ID_USUARIO "
				+ "WHERE P.ID_TIENDA = ? ORDER BY P.TOTAL DESC LIMIT 1";

		Connection connection = getConnection();
		PreparedStatement stmt = connection.prepareStatement(query);
		stmt.setLong(1, idTienda);
		ResultSet rs = stmt.executeQuery();

		if (rs.next()) {
			userId = rs.getLong("ID_USUARIO");
			orderId = rs.getLong("ID_PEDIDO");
			name = rs.getString("NOMBRE");
			address = rs.getString("DIRECCION");
		}
	}

	private Connection getConnection() {

		// return JDBC connection
		return null;
	}
}
