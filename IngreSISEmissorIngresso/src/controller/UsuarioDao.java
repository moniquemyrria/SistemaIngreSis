package controller;

import com.mysql.jdbc.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import model.bean.Usuario;
import model.conexao.Conexao;

public class UsuarioDao {
    
    Connection con = null; // Criando conexão com DB
    PreparedStatement ps = null; // Preparando uma ação
    ResultSet rs = null; // Retorna uma Informações do banco
    String sql = "";

    public void cadastrar(Usuario usuario) {

        con = Conexao.getConexao();
	sql = "insert into usuario(loginUsuario, senhaUsuario, tipoUsuario, statusUsuario) values (?, ?, ?, 'ATIVO')";
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, usuario.getLoginUsuario());
            ps.setString(2, usuario.getSenhaUsuario());
            ps.setString(3, usuario.getTipoUsuario());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Novo Usuário cadastrado com sucesso.");
	} catch (Exception e) {
            e.getMessage();
            JOptionPane.showMessageDialog(null, "Não foi possivel cadastrar o Usuário.");
	} finally {
            Conexao.closeConexao(con);
	}
    }
    
    public List<Usuario> consultar(String nome){
		
	con = Conexao.getConexao();
	sql = "select *from usuario where loginUsuario = '" + nome + "'";

	List<Usuario> lista = new ArrayList<Usuario>();
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
		rs = ps.executeQuery();
		while (rs.next()){
                    Usuario usuario = new Usuario();
                    usuario.setIdUsuario(rs.getInt("idUsuario"));
                    usuario.setLoginUsuario(rs.getString("loginUsuario"));
                    usuario.setSenhaUsuario(rs.getString("senhaUsuario"));
                    usuario.setTipoUsuario(rs.getString("tipoUsuario"));
                    lista.add(usuario);
		}
	} catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel Listar os Usuarios!");
        } finally {
                Conexao.closeConexao(con);
        }
        return lista;
    }
    
     public void alterar(Usuario usuario) {

	con = Conexao.getConexao();
	sql = "update usuario set loginUsuario = ?, senhaUsuario = ?, tipoUsuario = ? where idUsuario = ?";
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setString(1, usuario.getLoginUsuario());
            ps.setString(2, usuario.getSenhaUsuario());
            ps.setString(3, usuario.getTipoUsuario());
            ps.setInt(4, usuario.getIdUsuario());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Dados do Usuário alterado com sucesso.");

        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel Altear dados!");
        } finally {
            Conexao.closeConexao(con);
	}
    }
     
    public void excluir(Usuario usuario) {

	con = Conexao.getConexao();
	sql = "update usuario set statusUsuario = 'DELETADO' where idUsuario = ?";
	try {
            ps = (PreparedStatement) con.prepareStatement(sql);
            ps.setInt(1, usuario.getIdUsuario());
            ps.execute();
            JOptionPane.showMessageDialog(null, "Usuário excluído com sucesso.");

        } catch (Exception e) {
            e.getMessage();
            System.out.println("Nao foi possivel excluir os dados!");
        } finally {
            Conexao.closeConexao(con);
	}
    }
}
