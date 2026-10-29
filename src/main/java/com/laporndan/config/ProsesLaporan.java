package com.laporndan.controller;

import com.laporndan.config.Koneksi;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name = "ProsesLaporan", urlPatterns = {"/ProsesLaporan"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class ProsesLaporan extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // 1. Ambil data dari form index.html
        String tanggal = request.getParameter("tanggal");
        String waktu = request.getParameter("waktu");
        String lokasi = request.getParameter("lokasi");
        String kedalamanStr = request.getParameter("kedalaman");
        int kedalaman = Integer.parseInt(kedalamanStr);
        
        // 2. Handle Upload File Gambar
        Part filePart = request.getPart("foto");
        String fileName = extractFileName(filePart);
        
        // Tentukan folder penyimpanan foto di dalam project webapp/assets/uploads/
        String uploadPath = getServletContext().getRealPath("") + File.separator + "assets" + File.separator + "uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }
        
        // Simpan file ke server
        String filePath = uploadPath + File.separator + fileName;
        filePart.write(filePath);

        // 3. Simpan ke Database menggunakan JDBC
        Connection conn = Koneksi.getKoneksi();
        String sql = "INSERT INTO laporan (tanggal, waktu, lokasi, kedalaman, foto, status) VALUES (?, ?, ?, ?, ?, 'Belum Ditangani')";
        
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, tanggal);
            pstmt.setString(2, waktu);
            pstmt.setString(3, lokasi);
            pstmt.setInt(4, kedalaman);
            pstmt.setString(5, fileName);
            
            pstmt.executeUpdate();
            
            // Kalau berhasil, arahkan kembali ke index.html atau beri alert
            response.sendRedirect("index.html");
            
        } catch (SQLException e) {
            try (PrintWriter out = response.getWriter()) {
                out.println("<h3>Gagal menyimpan ke database: " + e.getMessage() + "</h3>");
            }
        }
    }

    // Helper method buat narik nama file yang di-upload
    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");
        for (String s : items) {
            if (s.trim().startsWith("filename")) {
                return s.substring(s.indexOf("=") + 2, s.length() - 1);
            }
        }
        return "";
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}