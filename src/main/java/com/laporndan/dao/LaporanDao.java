package com.laporndan.dao;

import com.laporndan.config.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class LaporanDao {
    
    // Method untuk mengambil semua data laporan dari database
    public static List<String[]> getAllLaporan() {
        List<String[]> listLaporan = new ArrayList<>();
        Connection conn = Koneksi.getKoneksi();
        
        if (conn != null) {
            try {
                String sql = "SELECT * FROM laporan ORDER BY id DESC";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                
                while (rs.next()) {
                    String[] data = new String[7];
                    data[0] = rs.getString("id");
                    data[1] = rs.getString("tanggal");
                    data[2] = rs.getString("waktu");
                    data[3] = rs.getString("lokasi");
                    data[4] = rs.getString("kedalaman");
                    data[5] = rs.getString("foto");
                    data[6] = rs.getString("status");
                    
                    listLaporan.add(data);
                }
                
                rs.close();
                stmt.close();
                conn.close();
            } catch (Exception e) {
                System.out.println("Gagal mengambil data laporan: " + e.getMessage());
            }
        }
        return listLaporan;
    }
   
    // Method untuk mengubah status laporan menjadi Selesai
    public static boolean updateStatus(int id) {
        boolean status = false;
        Connection conn = Koneksi.getKoneksi();
        try {
            String sql = "UPDATE laporan SET status = 'Selesai' WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            
            int rows = ps.executeUpdate();
            if (rows > 0) status = true;
            
            ps.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Gagal Update Status: " + e.getMessage());
        }
        return status;
    }

    // Method untuk menghapus laporan berdasarkan ID
    public static boolean hapusLaporan(int id) {
        boolean status = false;
        Connection conn = Koneksi.getKoneksi();
        try {
            String sql = "DELETE FROM laporan WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            
            int rows = ps.executeUpdate();
            if (rows > 0) status = true;
            
            ps.close();
            conn.close();
        } catch (Exception e) {
            System.out.println("Gagal Hapus Data: " + e.getMessage());
        }
        return status;
    }
}