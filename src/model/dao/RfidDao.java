/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import connection.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.bean.Evento;
import model.bean.Relatorio;
import model.bean.Rfid;

/**
 *
 * @author Lucas B Tsuchiya
 */
public class RfidDao {
    
    public boolean save (Rfid r){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try {
            
            stmt = con.prepareStatement("INSERT INTO cadastro (uid_tag,nome,matricula,curso) VALUES (?,?,?,?)");
            stmt.setString(1, r.getUid_tag());
            stmt.setString(2, r.getNome());
            stmt.setInt(3, r.getMatricula());
            stmt.setString(4, r.getCurso());
            stmt.executeUpdate();
            return true;
            
        } catch (SQLException ex) {
            System.err.println("Erro ao salvar: "+ex);
            return false;
        }finally{
            ConnectionFactory.closeConnection(con, stmt);
        }
       
}
    
    public boolean Gerar_relatorio (Relatorio r){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try {
            stmt = con.prepareStatement("INSERT INTO relatorio (nome_aluno, matricula_aluno, nome_evento, data_evento, responsavel, curso_aluno) VALUES (?,?,?,?,?,?)");
            stmt.setString(1, r.getNome_aluno());
            stmt.setInt(2, r.getMatricula_aluno());
            stmt.setString(3, r.getNome_evento());
            stmt.setString(4, r.getData_evento());
            stmt.setString(5, r.getResponsavel());
            stmt.setString(6, r.getCurso_aluno());
            stmt.executeUpdate();
            return true;
            
        } catch (SQLException ex) {
            System.err.println("Erro ao salvar: "+ex);
            return false;
        }finally{
            ConnectionFactory.closeConnection(con, stmt);
        }
       
}
    
       public boolean criarEvento (Evento e){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        
        try {
            
            stmt = con.prepareStatement("INSERT INTO evento (nome_evento, responsavel, data_evento) VALUES (?,?,?)");
            stmt.setString(1, e.getNome_evento());
            stmt.setString(2, e.getResponsavel());
            stmt.setString(3, e.getData());
            stmt.executeUpdate();
            return true;
            
        } catch (SQLException ex) {
            System.err.println("Erro ao salvar: "+ex);
            return false;
        }finally{
            ConnectionFactory.closeConnection(con, stmt);
        }
       
}
    
    
    
    public Rfid validar (String uidtag){
        
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        System.out.println("PArametro: " +uidtag);
        try {
            stmt = con.prepareStatement("SELECT *FROM cadastro WHERE uid_tag = ?");
            stmt.setString(1, uidtag);
            rs = stmt.executeQuery();
            Rfid dados = new Rfid();
            boolean r = rs.next();
            System.out.println("RS:" +r);
            
            if(r){
                System.out.println("ENCONTROU BD!! ");
                
                while (rs.next()){
                    dados.setId(rs.getInt("id"));
                    dados.setUid_tag(rs.getString("uid_tag"));
                    dados.setNome(rs.getString("nome"));
                    dados.setMatricula(rs.getInt("matricula"));
                  
                    dados.setCurso(rs.getString("curso"));
                }
                
                return dados;
             }else{
                System.err.println("NÃO ENCONTROU: ");
                return null;
            }
        
            
        } catch (SQLException ex) {
            System.err.println("NÃO ENCONTROU 2: "+ex);
            return null;
        }finally{
            ConnectionFactory.closeConnection(con, stmt);
        }
    }
    
    public List<Rfid> read(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Rfid> lista = new ArrayList();
        
        try{
            stmt = con.prepareStatement("SELECT *FROM cadastro");
            rs = stmt.executeQuery();
            
            while (rs.next()){
                Rfid rfid = new Rfid();
                rfid.setId(rs.getInt("id"));
                rfid.setUid_tag(rs.getString("uid_tag"));
                rfid.setNome(rs.getString("nome"));
                rfid.setMatricula(rs.getInt("matricula"));
                rfid.setCurso(rs.getString("curso"));
                lista.add(rfid);
            }
        } catch (SQLException ex){
            Logger.getLogger(RfidDao.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return lista; 
    }
    
    public List<Relatorio> readRelatorio(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Relatorio> lista = new ArrayList();
        
        try{
            stmt = con.prepareStatement("SELECT *FROM relatorio");
            rs = stmt.executeQuery();
            
            while (rs.next()){
                Relatorio r = new Relatorio();
                r.setNome_aluno(rs.getString("nome_aluno"));
                r.setMatricula_aluno(rs.getInt("matricula_aluno"));
                r.setNome_evento(rs.getString("nome_evento"));
                r.setData_evento(rs.getString("data_evento"));
                r.setResponsavel(rs.getString("responsavel"));
                r.setCurso_aluno(rs.getString("curso_aluno"));
                lista.add(r);
            }
        } catch (SQLException ex){
            Logger.getLogger(RfidDao.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return lista; 
    }
    public List<Relatorio> readRelatorioForEvento(String evento){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Relatorio> lista = new ArrayList();
        
        try{
            stmt = con.prepareStatement("SELECT *FROM relatorio WHERE nome_evento = ?");
            stmt.setString(1, evento);
            rs = stmt.executeQuery();
            
            while (rs.next()){
                Relatorio r = new Relatorio();
                r.setNome_aluno(rs.getString("nome_aluno"));
                r.setMatricula_aluno(rs.getInt("matricula_aluno"));
                r.setNome_evento(rs.getString("nome_evento"));
                r.setData_evento(rs.getString("data_evento"));
                r.setResponsavel(rs.getString("responsavel"));
                r.setCurso_aluno(rs.getString("curso_aluno"));
                lista.add(r);
            }
        } catch (SQLException ex){
            Logger.getLogger(RfidDao.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return lista; 
    }
    
    public List<Relatorio> readRelatorioBusca(String busca, String Parametro){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Relatorio> lista = new ArrayList();
        
        try{
            if(Parametro == "Evento"){
                stmt = con.prepareStatement("SELECT *FROM relatorio WHERE nome_evento = ?");
                stmt.setString(1, busca);
            } else if(Parametro == "Data"){
                stmt = con.prepareStatement("SELECT *FROM relatorio WHERE data_evento = ?");
                stmt.setString(1, busca);
            }else if(Parametro == "Responsável"){
                stmt = con.prepareStatement("SELECT *FROM relatorio WHERE responsavel = ?");
                stmt.setString(1, busca);
            }else if(Parametro == "Aluno"){
                stmt = con.prepareStatement("SELECT *FROM relatorio WHERE nome_aluno = ?");
                stmt.setString(1, busca);
            }else if(Parametro == "Matricula"){
                stmt = con.prepareStatement("SELECT *FROM relatorio WHERE matricula_aluno = ?");
                stmt.setString(1, busca);
            }else if (Parametro == "Curso"){
                stmt = con.prepareStatement("SELECT *FROM relatorio WHERE curso_aluno = ?");
                stmt.setString(1, busca);
            }else{
                stmt = con.prepareStatement("SELECT *FROM relatorio");
            }
            
            rs = stmt.executeQuery();
            
            while (rs.next()){
                Relatorio r = new Relatorio();
                r.setNome_aluno(rs.getString("nome_aluno"));
                r.setMatricula_aluno(rs.getInt("matricula_aluno"));
                r.setNome_evento(rs.getString("nome_evento"));
                r.setData_evento(rs.getString("data_evento"));
                r.setResponsavel(rs.getString("responsavel"));
                r.setCurso_aluno(rs.getString("curso_aluno"));
                lista.add(r);
            }
        } catch (SQLException ex){
            Logger.getLogger(RfidDao.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return lista; 
    }
    
    
    public List<Evento> readEvento(){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        List<Evento> lista = new ArrayList();
        
        try{
            stmt = con.prepareStatement("SELECT *FROM evento");
            rs = stmt.executeQuery();
            System.out.println("ENCONTROU BD Eventos!! ");
            while (rs.next()){
                Evento e = new Evento();
                e.setNome_evento(rs.getString("nome_evento"));
                e.setResponsavel(rs.getString("responsavel"));
                e.setData(rs.getString("data_evento"));
                lista.add(e);
                   System.out.println("Teste" +e.getNome_evento());
            }
        } catch (SQLException ex){
            Logger.getLogger(RfidDao.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return lista; 
    }
    
    
     public Rfid readUid(String uidtag){
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        System.out.println("PArametro readUid: " +uidtag);
        
        try{
            stmt = con.prepareStatement("SELECT *FROM cadastro where uid_tag = ?");
            stmt.setString(1, uidtag);
            rs = stmt.executeQuery();
            Rfid rfid = new Rfid();
            while (rs.next()){
                rfid.setId(rs.getInt("id"));
                rfid.setUid_tag(rs.getString("uid_tag"));
                rfid.setNome(rs.getString("nome"));
                rfid.setMatricula(rs.getInt("matricula"));
                rfid.setCurso(rs.getString("curso"));
            }
            
            return rfid;
            
            
            
        } catch (SQLException ex){
            Logger.getLogger(RfidDao.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            ConnectionFactory.closeConnection(con, stmt, rs);
        }
        return null;
        
    }

    
    
    public static void main(String[] args){
        Rfid rfid = new Rfid();
        rfid.setUid_tag("a7 c1 a0 99");
        rfid.setNome("Lucas Buzetto Tsuchiya");
        rfid.setMatricula(1512170);
        rfid.setCurso("Engenharia de Computação");
        
        RfidDao dao = new RfidDao();
        if(dao.save(rfid)){
            System.out.println("Salvo com sucesso");
        }else{
            System.err.println("Erro ao salvar 2!");
        }
        Rfid teste = null;
        String parametro = "19 15 04 89";
        teste = dao.validar(parametro);
        System.err.println(teste);
        
        Evento e = new Evento();
        e.setNome_evento("Palestra");
        e.setResponsavel("Mariana");
        e.setData("20/07/2018");
        dao.criarEvento(e);
    }
}
