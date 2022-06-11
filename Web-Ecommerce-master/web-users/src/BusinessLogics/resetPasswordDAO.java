package BusinessLogics;

import JavaBeans.resetPass;
import BusinessLogics.Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;

public class resetPasswordDAO {
    public static int themToken(int iduser, String tokenKey){
        int status = 0;
        try {
            String sql= "INSERT INTO reset_password (`iduser`, `token_key`, `expire`) " +
                    "VALUES (?, ?, NOW() + INTERVAL + 10 MINUTE );";
            Connection db = Database.connect();
            PreparedStatement pst = db.prepareStatement(sql);
            pst.setInt(1, iduser);
            pst.setString(2, tokenKey);

            status = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }
    // đọc token truyền vào
    public static resetPass readTokenKey(String tokenKey){
        resetPass resetPass = null;
        try {
            String sql = "select * from reset_password where token_key = ?";
            Connection db = Database.connect();
            PreparedStatement pst = db.prepareStatement(sql);
            pst.setString(1, tokenKey);
            ResultSet rs = pst.executeQuery();
            while(rs.next()){
                resetPass = new resetPass();
                resetPass.setIduser(rs.getInt("iduser"));
                resetPass.setTokenKey(rs.getString("token_key"));
                resetPass.setCreate(rs.getTimestamp("expire"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resetPass;
    }
    
    // tính tgian hiện tại với tgian expire, nếu >0 thì mới cho reset, <= 0 là hết hạn
    public static long checkToken(String tokenKey){
        long status = 0;
        try {
            String sql = "SELECT expire FROM reset_password WHERE token_key = ?";
            Connection db = Database.connect();
            PreparedStatement pst = db.prepareStatement(sql);
            pst.setString(1, tokenKey);
            ResultSet rs = pst.executeQuery();
            while (rs.next()){
                Timestamp getExpire = rs.getTimestamp("expire");
                Timestamp getNowTime = getExpire ;
                Statement stm = db.createStatement();
                ResultSet rs1 = stm.executeQuery("SELECT NOW() as now");
                while (rs1.next()){
                    getNowTime = rs1.getTimestamp("now");
                }
                status = getExpire.getTime() - getNowTime.getTime();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return status;
    }
    
    // nếu hết hạn/ sau khi reset thì xóa token.
    public static int deleteToken(int iduser){
        int delete = 0;
        try {
            Connection db = Database.connect();
            String sql = "DELETE FROM `reset_password` WHERE `iduser`= ? ;";
            PreparedStatement pst = db.prepareStatement(sql);
            pst.setInt(1, iduser);
            delete = pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return delete;
    }
}
