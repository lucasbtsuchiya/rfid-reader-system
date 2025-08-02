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
import javax.swing.JOptionPane;
import model.bean.Rfid;


/**
 *
 * @author Lucas B Tsuchiya
 */
public class CadastroDao {
    
    public List<Rfid> listar(){
        
        List<Rfid> rfid = new ArrayList<> ();
        Connection con = ConnectionFactory.getConnection();
        PreparedStatement pstm = null;
        ResultSet rs = null;
        
        try 
        {
            pstm = con.prepareStatement("select *from cadastro;");
            rs = pstm.executeQuery();
            while(rs.next()){
                Rfid dados = new Rfid();
                dados.setNome(rs.getString("nome"));
                dados.setMatricula(rs.getInt("matricula"));
                dados.setCurso(rs.getString("cuso"));
                rfid.add(dados);  
            }
        }
        catch (SQLException ErroSQL)
        {
            JOptionPane.showMessageDialog(null, "Erro ao listar dados: "+ErroSQL, "ERRO", JOptionPane.ERROR_MESSAGE);
        }
        finally
        {
           ConnectionFactory.closeConnection(con, pstm, rs);
        }
        return rfid;
    
    }
       
}
