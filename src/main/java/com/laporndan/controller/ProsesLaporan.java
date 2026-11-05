package com.laporndan.controller;

import com.laporndan.config.HibernateUtil;
import com.laporndan.model.Laporan;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import org.hibernate.Session;
import org.hibernate.Transaction;

@WebServlet(name = "ProsesLaporan", urlPatterns = {"/ProsesLaporan"})
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,        // 10MB
    maxRequestSize = 1024 * 1024 * 50      // 50MB
)
public class ProsesLaporan extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        // 1. Ambil data dari form index.jsp
        String namaPelapor = request.getParameter("nama_pelapor");
        String tanggal = request.getParameter("tanggal");
        String waktu = request.getParameter("waktu");
        String lokasi = request.getParameter("lokasi");
        String kedalamanStr = request.getParameter("kedalaman");
        int kedalaman = Integer.parseInt(kedalamanStr);
        
        // 2. Handle Upload File Gambar ke folder source project
        Part filePart = request.getPart("foto");
        String fileName = extractFileName(filePart);
        
        String uploadPath = "D:\\NetBeansProjects\\LaporNdan\\src\\main\\webapp\\assets\\uploads";
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String filePath = uploadPath + File.separator + fileName;
        filePart.write(filePath);

        // 3. Simpan ke Database menggunakan Hibernate Session
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        
        try {
            tx = session.beginTransaction();
            
            // Buat objek Laporan dan masukkan datanya lewat setter
            Laporan laporan = new Laporan();
            laporan.setNamaPelapor(namaPelapor);
            laporan.setTanggal(tanggal);
            laporan.setWaktu(waktu);
            laporan.setLokasi(lokasi);
            laporan.setKedalaman(kedalaman);
            laporan.setFoto(fileName);
            laporan.setStatus("Belum Ditangani");
            
            // Simpan objek langsung pakai Hibernate!
            session.persist(laporan);
            
            tx.commit();
            
            // Redirect kembali ke index.jsp jika berhasil
            response.sendRedirect("index.jsp");
            
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
            try (PrintWriter out = response.getWriter()) {
                out.println("<h3>Gagal menyimpan ke database via Hibernate: " + e.getMessage() + "</h3>");
            }
        } finally {
            session.close();
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