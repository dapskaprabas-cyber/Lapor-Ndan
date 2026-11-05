package com.laporndan.controller;

import com.laporndan.config.Koneksi;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@WebServlet(name = "ProsesKomentar", urlPatterns = {"/ProsesKomentar"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,
    maxFileSize = 1024 * 1024 * 10,
    maxRequestSize = 1024 * 1024 * 50
)
public class ProsesKomentar extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Ambil data dari form detail.jsp
        String idLaporanStr = request.getParameter("id_laporan");
        String namaPengguna = request.getParameter("nama_pengguna");
        String isiKomentar = request.getParameter("isi_komentar");
        
        // Handle upload foto bukti komentar (opsional)
        Part filePart = request.getPart("foto_bukti");
        String fileName = "";
        
        if (filePart != null && filePart.getSize() > 0) {
            fileName = extractFileName(filePart);
            
            // UBAH BAGIAN INI: Arahkan langsung ke folder source webapp project di komputermu
            String uploadPath = "D:\\NetBeansProjects\\LaporNdan\\src\\main\\webapp\\assets\\uploads";
            
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }
            String filePath = uploadPath + File.separator + fileName;
            filePart.write(filePath);
        }

        // Simpan ke database dengan kolom nama_pengguna
        try {
            Connection conn = Koneksi.getKoneksi();
            String sql = "INSERT INTO komentar (id_laporan, nama_pengguna, isi_komentar, foto_bukti) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(idLaporanStr));
            pstmt.setString(2, namaPengguna);
            pstmt.setString(3, isiKomentar);
            pstmt.setString(4, fileName.isEmpty() ? null : fileName);
            pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Redirect kembali ke halaman detail laporan yang bersangkutan
        response.sendRedirect("detail.jsp?id=" + idLaporanStr);
    }

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
}