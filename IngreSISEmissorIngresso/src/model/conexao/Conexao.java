
package model.conexao;

import java.sql.Connection;
import java.sql.DriverManager;


public class Conexao {
    
    
    public static Connection getConexao(){
        
        Connection con = null;
        
        String host = "Localhost";
        String porta = "3306";
        String nomeBanco = "dbingresis";
        String usuario = "root";
        String senha = "1234";
	
        String urlBanco = "jdbc:mysql://" + host + ":" + porta + "/" + nomeBanco + "?useSSL=false";
        
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(urlBanco, usuario, senha);
            System.out.println("Conecxão OK!");
        } catch (Exception erro) {
            erro.getMessage();
            System.out.println("Não foi possível conectar ao banco de dados!");
        }
        return con;
    }
	
	public static void closeConexao(Connection con){
		try {
			con.close();
			System.out.println("Conexão com o Banco finalizado com sucesso!");
		} catch (Exception e) {
			e.getMessage();
		}
	}
	
	public static void main(String[] args) {
		Conexao.getConexao();
	}
}
