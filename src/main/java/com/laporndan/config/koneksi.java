package com.laporndan.config; // Sesuaikan dengan nama package lo

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    public static Connection getKoneksi() {
        Connection conn = null;
        try {
            // Driver MySQL untuk Connector/J modern
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Konfigurasi database Laragon
            String url = "jdbc:mysql://localhost:3306/db_laporndan?useSSL=false&serverTimezone=UTC";
            String user = "root";
            String password = ""; // Default Laragon kosong
            
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Koneksi ke Database Laragon Berhasil!");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Koneksi Gagal: " + e.getMessage());
        }
        return conn;
    }
}